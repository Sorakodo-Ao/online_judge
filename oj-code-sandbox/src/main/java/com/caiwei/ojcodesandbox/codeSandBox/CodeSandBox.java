package com.caiwei.ojcodesandbox.codeSandBox;


import com.caiwei.ojcodesandbox.model.ExecuteCodeRequest;
import com.caiwei.ojcodesandbox.model.ExecuteCodeResponse;

public interface CodeSandBox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
