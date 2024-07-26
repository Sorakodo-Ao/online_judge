package com.caiwei.ojcodesandbox.controller;


import com.caiwei.ojcodesandbox.codeSandBox.CodeSandBox;
import com.caiwei.ojcodesandbox.codeSandBox.Impl.CppNativeCodeSandbox;
import com.caiwei.ojcodesandbox.codeSandBox.Impl.JavaNativeCodeSandBox;
import com.caiwei.ojcodesandbox.codeSandBox.Impl.RemoteCodeSandBox;
import com.caiwei.ojcodesandbox.model.ExecuteCodeRequest;
import com.caiwei.ojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JudgeController {
    @PostMapping("/judge")
    public ExecuteCodeResponse judge(@RequestBody ExecuteCodeRequest executeCodeRequest) {
        System.out.println("executeCodeRequest = " + executeCodeRequest);
        String language = executeCodeRequest.getLanguage();
        CodeSandBox codeSandBox = null;
        //根据language选择对应的判题机
        if(language.equals("java")){
            codeSandBox = new JavaNativeCodeSandBox();
        } else if (language.equals("c") || language.equals("cpp")) {
            codeSandBox = new CppNativeCodeSandbox();
        }else{
            codeSandBox = new RemoteCodeSandBox();
        }
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        return executeCodeResponse;
    }
}
