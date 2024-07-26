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
import java.util.*;

@Slf4j
public class CppNativeCodeSandbox implements CodeSandBox {
    private static final String GLOBAL_CODE_DIR_NAME = "tempCode";

    private static final String CPP_NAME = "Main.cpp";//转为的c/cpp文件名

    private static final String DESTINATION_NAME = "Main.exe";

    private static final Long TIME_OUT = 10000L;//设置沙箱超时时间，防止资源占用

    //黑名单
    private static final List<String> BLACK_LIST = Arrays.asList("Files", "File", "file", "exec", "write", "read");

    //查找树
    private static final WordTree wordTree = new WordTree();

    static {
        wordTree.addWords(BLACK_LIST);
    }

    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String language = executeCodeRequest.getLanguage();
        log.info("进入c/cpp判题机, language = " + language);
        String code = executeCodeRequest.getCode();
        //返回的响应体
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
        String userCodePath = userCodeParentPath + File.separator + CPP_NAME;
        File userCodefile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        //3.编译
        //判断code中是否有文件危险操作（黑名单）
        FoundWord foundWord = wordTree.matchWord(code);
        if (foundWord != null) {
            log.info("危险操作！！！");
            log.info("danger operation = " + foundWord.getFoundWord());
            executeCodeResponse.setOutputList(null);
            executeCodeResponse.setMessage("danger");
            executeCodeResponse.setStatus(StatusEmun.SANDBOX_ERROR.getStatus().toString());
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMessage("danger");
            executeCodeResponse.setJudgeInfo(judgeInfo);
            executeCodeResponse.setJudgeInfo(null);
            return executeCodeResponse;
        }

        //cpp文件绝对路径
        String codefileAbsolutePath = userCodefile.getAbsolutePath();
        //编译后的exe绝对路径
        String executeFilePath = userCodefile.getParent() + File.separator + DESTINATION_NAME;
        log.info("cpp文件绝对路径 = " + codefileAbsolutePath
                + "  exe绝对路径 = " + executeFilePath);
        Long totalTime = 0L;

        //gcc编译
        String compileCommand = String.format("gcc -o %s %s", executeFilePath, codefileAbsolutePath);
        //编译得到exe文件
        Process compileProcess = null;
        try {
            compileProcess = Runtime.getRuntime().exec(compileCommand);
            ExecuteMessage executeMessage = JavaProcesssUtil.executeProcess(compileProcess, "compile");
            log.info("编译结束, compileMessage = " + executeMessage);
            Long compileTime = executeMessage.getTime();
            totalTime += compileTime;
        } catch (IOException e) {
            return getErrorResponse(e);
        }

        //4.执行
        String runCommand = executeFilePath;
        //执行信息
        ExecuteMessage executeMessage = null;
        try {
            Process runProcess = Runtime.getRuntime().exec(runCommand);
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

            executeMessage = JavaProcesssUtil.executeProcess(runProcess, "run");
            log.info("运行结束, runMessage = " + executeMessage);
        } catch (Exception e) {
            return getErrorResponse(e);
        }


        //5.包装响应 封装信息
        String errorMessage = executeMessage.getErrorMessage();
        if (StringUtils.isNotBlank(errorMessage)) {
            executeCodeResponse.setMessage(errorMessage);
            //含有错误信息，状态设置为失败
            executeCodeResponse.setStatus(StatusEmun.FAILURE.getStatus().toString());
            executeCodeResponse.setMessage("failure");
        } else {
            //正常输出
            executeCodeResponse.setStatus(StatusEmun.SUCCEESS.getStatus().toString());
            executeCodeResponse.setMessage("success");
        }
        //输出结果
        List<String> outputList = new ArrayList<>();
        outputList.add(executeMessage.getMessage());

        //获得编译执行的总时间
        Long runTime = executeMessage.getTime();
        totalTime += runTime;

        //todo 设置随机内存，无docker，无法记录（也可以记录，比如在代码中嵌入内存检测，但也是不专业，作罢）
        Random random = new Random();
        int memory = random.nextInt(10);

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage("success");
        judgeInfo.setMemory(memory * 1024);
        judgeInfo.setTime(totalTime);

        executeCodeResponse.setJudgeInfo(judgeInfo);
        executeCodeResponse.setOutputList(outputList);

        //6.文件清理
        if (userCodefile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
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

    public static void main(String[] args) {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("a", "b"));
        executeCodeRequest.setLanguage("cpp");
        executeCodeRequest.setCode("#include<stdio.h>\n" +
                "int main(){\n" +
                "printf(\"%d\",(1+2));\n" +
                "return 0;\n" +
                "}");
        ExecuteCodeResponse executeCodeResponse = new CppNativeCodeSandbox().executeCode(executeCodeRequest);
        System.out.println("executeCodeResponse = " + executeCodeResponse);
    }
}
