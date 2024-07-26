package com.caiwei.online_judge.judge.codeSandBox;

import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeRequest;
import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过代理类，增加日志输出功能(调用代理类)
 * 静态代理，aop是动态代理
 */
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {
    private final CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求参数 = {}", executeCodeRequest);
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应参数 = {}", executeCodeResponse);
        return executeCodeResponse;
    }
}
