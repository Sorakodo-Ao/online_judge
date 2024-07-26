package com.caiwei.ojcodesandbox;

import com.caiwei.ojcodesandbox.codeSandBox.Impl.JavaNativeCodeSandBox;
import com.caiwei.ojcodesandbox.model.ExecuteCodeRequest;
import com.caiwei.ojcodesandbox.model.ExecuteCodeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class OjCodeSandboxApplicationTests {
    @Test
    void contextLoads() {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(new ArrayList<>());
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setCode("public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int sum = 0;\n" +
                "        int[] arr = new int[3];\n" +
                "        for (int i = 0; i < arr.length; i++) {\n" +
                "            arr[i] = i;\n" +
                "        }\n" +
                "        for (int i : arr) {\n" +
                "            sum += i;\n" +
                "        }\n" +
                "        System.out.println(\"sum = \" + sum);\n" +
                "    }\n" +
                "}");
        JavaNativeCodeSandBox javaNativeCodeSandBox = new JavaNativeCodeSandBox();
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);

    }

}
