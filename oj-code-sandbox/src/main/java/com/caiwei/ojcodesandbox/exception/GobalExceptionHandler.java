package com.caiwei.ojcodesandbox.exception;

import com.caiwei.ojcodesandbox.model.ExecuteCodeResponse;
import com.caiwei.ojcodesandbox.model.StatusEmun;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GobalExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public ExecuteCodeResponse exceptionHandler(Exception exception) {
        log.error("Exception = {}", exception.getMessage());
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(null);
        executeCodeResponse.setMessage(exception.getMessage());
        executeCodeResponse.setStatus(StatusEmun.SANDBOX_ERROR.getStatus().toString());
        executeCodeResponse.setJudgeInfo(null);
        return executeCodeResponse;
    }
 
}