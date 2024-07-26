package com.caiwei.ojcodesandbox.codeSandBox.Impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.caiwei.ojcodesandbox.codeSandBox.CodeSandBox;
import com.caiwei.ojcodesandbox.model.*;
import com.caiwei.ojcodesandbox.utils.JavaProcesssUtil;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class JavaNativeCodeSandBox implements CodeSandBox {

    private static final String GLOBAL_CODE_DIR_NAME = "tempCode";

    private static final String JAVA_NAME = "Main.java";

    private static final Long TIME_OUT = 10000L;//超时

    //黑名单
    private static final List<String> BLACK_LIST = Arrays.asList("Files", "File", "exec", "write", "read");

    //查找树
    private static final WordTree wordTree = new WordTree();

    static {
        wordTree.addWords(BLACK_LIST);
    }

    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        String language = executeCodeRequest.getLanguage();
        log.info("进入java判题机, language = " + language);

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        //响应体
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        //1.拿到项目的工作目录
        String userDir = System.getProperty("user.dir");
        //存放提交代码的目录
        String fullPathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;

        //2.判断全局代码目录，没有则创建
        if (!FileUtil.exist(fullPathName)) {
            FileUtil.mkdir(fullPathName);
        }
        //分级,因为用户的代码文件的类都要求是Main,所以要隔离开来
        String userCodeParentPath = fullPathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + JAVA_NAME;
        File userCodefile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        //3.编译
        //判断code中是否有文件危险操作（黑名单）
        FoundWord foundWord = wordTree.matchWord(code);
        if (foundWord != null) {
            log.info("danger operation = " + foundWord.getFoundWord());
            executeCodeResponse.setOutputList(null);
            executeCodeResponse.setMessage("danger");
            executeCodeResponse.setStatus(StatusEmun.SANDBOX_ERROR.getStatus().toString());
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMessage("danger");
            executeCodeResponse.setJudgeInfo(judgeInfo);
            return executeCodeResponse;
        }

        //javac编译
        String compile = String.format("javac -encoding utf-8 %s", userCodefile.getAbsolutePath());
        log.info("compileCommand = " + compile);
        //编译得到class文件
        Process compileProcess = null;
        try {
            compileProcess = Runtime.getRuntime().exec(compile);
            ExecuteMessage executeMessage = JavaProcesssUtil.executeProcess(compileProcess, "compile");
            log.info("编译结束, compileMessage = " + executeMessage);
        } catch (IOException e) {
            return getErrorResponse(e);
        }

        //4.执行
        // 情况一：inputList没有参数
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        List<Long> memoryList = new ArrayList<>();
        if (inputList.isEmpty()) {
            String run = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main", userCodeParentPath);
            log.info("no args,run cmd = " + run);
            try {
                Process runProcess = Runtime.getRuntime().exec(run);
                //超时控制
                new Thread(() -> {
                    try {
                        //开启保护线程，run运行线程超时就摧毁
                        Thread.sleep(TIME_OUT);
                        log.info("超时检测 , timeout!!!!!!!!!!!!");
                        //超时时间到了，自动销毁
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                //内存检测(非常不专业)
                new Thread(() -> {
                    Long i = 0L;
                    while ((i++) < (TIME_OUT / 2)) {
                        Runtime r = Runtime.getRuntime();
                        long freeMemory = r.freeMemory();
                        long totalMemory = r.totalMemory();
                        long useMemory = (totalMemory - freeMemory) / 1024;//单位kb
                        memoryList.add(useMemory);
                    }
                }).start();
                ExecuteMessage executeMessage = JavaProcesssUtil.executeProcess(runProcess, "run");
                log.info("运行结束, compileMessage = " + executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                return getErrorResponse(e);
            }
        } else {
            //情况二：inputList比如: "1 2" ,"3 4"
            for (String input : inputList) {
                String run = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, input);

                log.info("args,run cmd = " + run);
                try {
                    Process runProcess = Runtime.getRuntime().exec(run);
                    //超时控制
                    new Thread(() -> {
                        try {
                            //开启保护线程，run运行线程超时就摧毁
                            Thread.sleep(TIME_OUT);
                            log.info("超时检测 , timeout!!!!!!!!!!!!");
                            runProcess.destroy();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                    //内存检测(这个非常不专业)
                    new Thread(() -> {
                        Long i = 0L;
                        while ((i++) < (TIME_OUT / 2)) {
                            Runtime r = Runtime.getRuntime();
                            long freeMemory = r.freeMemory();
                            long totalMemory = r.totalMemory();
                            long useMemory = ((totalMemory - freeMemory) / 1024);//单位kb
                            memoryList.add(useMemory);
                        }
                    }).start();
                    ExecuteMessage executeMessage = JavaProcesssUtil.executeProcess(runProcess, "run");
                    log.info("运行结束, compileMessage = " + executeMessage);
                    executeMessageList.add(executeMessage);
                } catch (Exception e) {
                    return getErrorResponse(e);
                }
            }

        }

        //5.包装响应
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        long maxMemory = 0;
        //封装信息
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StringUtils.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                //含有错误信息，状态设置为失败
                executeCodeResponse.setStatus(StatusEmun.FAILURE.getStatus().toString());
                break;
            }
            Long time = executeMessage.getTime();
            //取每个用例运行后的最大的时间
            if (time != null) {
                //maxTime = maxTime > time ? maxTime : time;
                maxTime = Math.max(maxTime, time);
            }
            outputList.add(executeMessage.getMessage());
        }
        //取得最大消耗内存
        for (Long memory : memoryList) {
            maxMemory = Math.max(maxMemory, memory);
        }
        //正常输出
        if (outputList.size() == executeMessageList.size()) {
            //状态设置为成功
            executeCodeResponse.setStatus(StatusEmun.SUCCEESS.getStatus().toString());
            executeCodeResponse.setMessage("success");
        }
        executeCodeResponse.setOutputList(outputList);

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage("success");
        judgeInfo.setMemory((int) (maxMemory - (30 * 1024)));
        judgeInfo.setTime(maxTime);

        executeCodeResponse.setJudgeInfo(judgeInfo);

        //6.文件清理
        if (userCodefile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            log.info("删除文件" + (del ? "成功" : "失败"));
        }

        return executeCodeResponse;
    }

    /**
     * 获取错误的返回(错误处理)
     *
     * @param throwable
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable throwable) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(null);
        executeCodeResponse.setMessage(throwable.getMessage());
        executeCodeResponse.setStatus(StatusEmun.SANDBOX_ERROR.getStatus().toString());//表示沙箱错误
        executeCodeResponse.setJudgeInfo(null);
        return executeCodeResponse;
    }
}
