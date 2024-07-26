package com.caiwei.online_judge;

import com.caiwei.online_judge.judge.JudgeService;
import com.caiwei.online_judge.judge.JudgeServiceImpl;
import com.caiwei.online_judge.judge.codeSandBox.CodeSandBox;
import com.caiwei.online_judge.judge.codeSandBox.CodeSandBoxFactory;
import com.caiwei.online_judge.judge.codeSandBox.CodeSandBoxProxy;
import com.caiwei.online_judge.judge.codeSandBox.model.ExecuteCodeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class test {
    @Value("${codeSandBox.type}")
    private String type;

    @Resource
    private JudgeService judgeService;
/*    @Test
    void testRestTemplate(){
        String health = judgeService.health();
        System.out.println("health = " + health);
    }*/


    @Test
    void TestList(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        int sum = 0;
        for (Integer integer : list) {
            sum += integer;
        }
        System.out.println(sum);
    }

    @Test
    void codeSandBoxTest() {
/*        System.out.println("type = " + type);
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest().builder()
                .code("main")
                .language("java")
                .inputList(new ArrayList<String>()).build();

        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);
        codeSandBoxProxy.executeCode(executeCodeRequest);*/
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest().builder()
                .code("main")
                .language("java")
                .inputList(new ArrayList<String>()).build();
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        System.out.println("codeSandBox = " + codeSandBox);
        codeSandBox.executeCode(executeCodeRequest);
    }
}
