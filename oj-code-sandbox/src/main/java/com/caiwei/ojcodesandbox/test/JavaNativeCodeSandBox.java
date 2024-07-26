//package com.caiwei.ojcodesandbox.test;
//
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.io.resource.ResourceUtil;
//import cn.hutool.dfa.FoundWord;
//import cn.hutool.dfa.WordTree;
//import com.caiwei.ojcodesandbox.codeSandBox.CodeSandBox;
//import com.caiwei.ojcodesandbox.model.ExecuteCodeRequest;
//import com.caiwei.ojcodesandbox.model.ExecuteCodeResponse;
//import com.caiwei.ojcodesandbox.model.ExecuteMessage;
//import com.caiwei.ojcodesandbox.model.JudgeInfo;
//import com.caiwei.ojcodesandbox.utils.ProcesssUtil;
//import io.micrometer.common.util.StringUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//public class JavaNativeCodeSandBox implements CodeSandBox {
//
//    private static final String GLOBAL_CODE_DIR_NAME = "tempCode";
//
//    private static final String JAVA_NAME = "Main.java";
//
//    private static final Long TIME_OUT = 5000L;
//
//    private static final List<String> BLACK_LIST = Arrays.asList("Files", "exec");
//
//    private static final WordTree wordTree = new WordTree();
//    static {
//        wordTree.addWords(BLACK_LIST);
//    }
//
//
//
//    public static void main(String[] args) {
//        JavaNativeCodeSandBox javaNativeCodeSandBox = new JavaNativeCodeSandBox();
//        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
//        executeCodeRequest.setInputList(Arrays.asList("1 2"));
//        executeCodeRequest.setLanguage("java");
//        String code = ResourceUtil.readStr("testCode.simpleCompteArgs/Main.java", StandardCharsets.UTF_8);
//        executeCodeRequest.setCode(code);
//        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBox.executeCode(executeCodeRequest);
//        System.out.println("executeCodeResponse = " + executeCodeResponse);
//    }
//
//    @Override
//    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
//
//
//        List<String> inputList = executeCodeRequest.getInputList();
//        String language = executeCodeRequest.getLanguage();
//        String code = executeCodeRequest.getCode();
//
//        code = ResourceUtil.readStr("testCode.simpleCompteArgs/Main.java", StandardCharsets.UTF_8);
//
//        //1.拿到项目的工作目录
//        String userDir = System.getProperty("user.dir");
//        //存放提交代码的目录
//        String fullPathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
//
//        //2.判断全局代码目录，没有则创建
//        if (!FileUtil.exist(fullPathName)) {
//            FileUtil.mkdir(fullPathName);
//        }
//        //分级,因为用户的代码文件的类都要求是Main,所以要隔离开来
//        String userCodeParentPath = fullPathName + File.separator + UUID.randomUUID();
//        String userCodePath = userCodeParentPath + File.separator + JAVA_NAME;
//        File userCodefile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
//
//        //3.编译
//        //判断code中是否有文件危险操作（黑名单）
//
//        FoundWord foundWord = wordTree.matchWord(code);
//        if (foundWord != null) {
//            System.out.println(foundWord.getFoundWord());
//            return null;
//        }
//
//        //javac编译
//        String compile = String.format("javac -encoding utf-8 %s", userCodefile.getAbsolutePath());
//        //编译得到class文件
//        Process compileProcess = null;
//        try {
//            compileProcess = Runtime.getRuntime().exec(compile);
//            ExecuteMessage executeMessage = ProcesssUtil.executeProcess(compileProcess, "compile");
//            System.out.println(executeMessage);
//        } catch (IOException e) {
//            return getErrorResponse(e);
//        }
//
//
//        //4.执行 inputList: "1 2" ,"3 4"
//        List<ExecuteMessage> executeMessageList = new ArrayList<>();
//        for (String input : inputList) {
//            String run = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, input);
//
//            System.out.println("run = " + run);
//            try {
//                Process runProcess = Runtime.getRuntime().exec(run);
//                //超时控制
//                new Thread(() -> {
//                    try {
//                        //开启保护线程，run运行线程超时就摧毁
//                        Thread.sleep(TIME_OUT);
//                        System.out.println("timeout!!!!!!!!!!!!");
//                        runProcess.destroy();
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }).start();
//                ExecuteMessage executeMessage = ProcesssUtil.executeProcess(runProcess, "run");
//                System.out.println(executeMessage);
//                executeMessageList.add(executeMessage);
//            } catch (Exception e) {
//                return getErrorResponse(e);
//            }
//
//        }
//
//        System.out.println("运行完成");
//        //5.包装响应
//        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
//        List<String> outputList = new ArrayList<>();
//        long maxTime = 0;
//        //封装信息
//        for (ExecuteMessage executeMessage : executeMessageList) {
//            String errorMessage = executeMessage.getErrorMessage();
//            if (StringUtils.isNotBlank(errorMessage)) {
//                executeCodeResponse.setMessage(errorMessage);
//                executeCodeResponse.setStatus("3");
//                break;
//            }
//            Long time = executeMessage.getTime();
//            //取每个用例运行后的最大的时间
//            if (time != null) {
//                //maxTime = maxTime > time ? maxTime : time;
//                maxTime = Math.max(maxTime, time);
//            }
//            outputList.add(executeMessage.getMessage());
//        }
//        //正常输出
//        if (outputList.size() == executeMessageList.size()) {
//            executeCodeResponse.setStatus("1");
//        }
//        executeCodeResponse.setOutputList(outputList);
//
//        JudgeInfo judgeInfo = new JudgeInfo();
///*        judgeInfo.setMessage();
//
//        judgeInfo.setMemory();*/
//
//
//        judgeInfo.setTime(maxTime);
//
//        executeCodeResponse.setJudgeInfo(judgeInfo);
//
//        //6.文件清理
//        if (userCodefile.getParentFile() != null) {
//            boolean del = FileUtil.del(userCodeParentPath);
//            System.out.println("删除" + (del ? "成功" : "失败"));
//        }
//
//        return executeCodeResponse;
//    }
//
///**
//     * 获取错误的返回(错误处理)
//     *
//     * @param throwable
//     * @return*/
//
//
//
//    private ExecuteCodeResponse getErrorResponse(Throwable throwable) {
//        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
//        executeCodeResponse.setOutputList(new ArrayList<String>());
//        executeCodeResponse.setMessage(throwable.getMessage());
//        executeCodeResponse.setStatus("2");//表示沙箱错误
//        executeCodeResponse.setJudgeInfo(new JudgeInfo());
//        return executeCodeResponse;
//    }
//}
