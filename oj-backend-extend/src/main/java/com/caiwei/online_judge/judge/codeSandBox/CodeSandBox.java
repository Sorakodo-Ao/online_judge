package com.caiwei.online_judge.judge.codeSandBox;

import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeRequest;
import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeResponse;

public interface CodeSandBox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
