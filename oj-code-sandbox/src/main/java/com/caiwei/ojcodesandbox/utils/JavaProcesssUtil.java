package com.caiwei.ojcodesandbox.utils;

import com.caiwei.ojcodesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JavaProcesssUtil {
    /**
     * 获取执行进程的信息
     * @param process
     * @param operate
     * @return
     */
    public static ExecuteMessage executeProcess(Process process, String operate) {
       ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            //等待程序执行获取错误码，正常退出错误码是0
            int exitValue = process.waitFor();
            executeMessage.setExitValue(exitValue);
            if (exitValue == 0) {
                System.out.println(operate+"success!!");
                //获取进程输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder compileTotalMessage = new StringBuilder();
                String compileMessage;
                while ((compileMessage = bufferedReader.readLine()) != null) {
                    compileTotalMessage.append(compileMessage);
                }
                executeMessage.setMessage(compileTotalMessage.toString());
            } else {
                System.out.println(operate + "error!! ，error code = " + exitValue);
                //获取进程正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder compileTotalMessage = new StringBuilder();
                String compileMessage;
                while ((compileMessage = bufferedReader.readLine()) != null) {
                    compileTotalMessage.append(compileMessage);
                }
                executeMessage.setMessage(compileTotalMessage.toString());
                //获取进程错误输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder compileErrorTotalMessage = new StringBuilder();
                String compileErrorMessage;
                while ((compileErrorMessage = errorBufferedReader.readLine()) != null) {
                    compileErrorTotalMessage.append(compileErrorMessage);
                }
                executeMessage.setErrorMessage(compileErrorTotalMessage.toString());
            }
            stopWatch.stop();
            long totalTimeMillis = stopWatch.getTotalTimeMillis();//执行时间
            executeMessage.setTime(totalTimeMillis);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return executeMessage;
    }
}
