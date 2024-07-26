package com.caiwei.online_judge.judge.codeSandBox.impl;

import com.caiwei.online_judge.judge.codeSandBox.CodeSandBox;
import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeRequest;
import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱
 */
public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("thirdparty");
        return null;
    }
}
