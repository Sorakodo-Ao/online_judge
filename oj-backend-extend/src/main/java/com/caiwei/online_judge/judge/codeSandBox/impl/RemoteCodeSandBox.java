package com.caiwei.online_judge.judge.codeSandBox.impl;

import com.caiwei.online_judge.config.RestTemplateConfig;
import com.caiwei.online_judge.judge.codeSandBox.CodeSandBox;
import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeRequest;
import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 远程调用沙箱
 */
@Slf4j
public class RemoteCodeSandBox implements CodeSandBox {
    @Resource
    private RestTemplate restTemplate;

    public void setRestTemplate() {
        RestTemplateConfig restTemplateConfig = new RestTemplateConfig();
        this.restTemplate = restTemplateConfig.restTemplate();
    }

    private static final String SERVICE_PROVIDER = "http://localhost:9091";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("remoteSandbox");
        setRestTemplate();
        String url = SERVICE_PROVIDER + "/judge";
        ExecuteCodeResponse executeCodeResponse =
                restTemplate.postForObject(url, executeCodeRequest, ExecuteCodeResponse.class);
        System.out.println("remoteSandbox return = " + executeCodeResponse);
        return executeCodeResponse;

/*        String url = SERVICE_PROVIDER + "/health";
        String str = restTemplate.getForObject(url, String.class);
        System.out.println("return string = " + str);
        return null;*/
    }
}
