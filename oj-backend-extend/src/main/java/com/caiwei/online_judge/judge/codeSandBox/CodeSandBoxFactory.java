package com.caiwei.online_judge.judge.codeSandBox;

import com.caiwei.online_judge.judge.codeSandBox.impl.RemoteCodeSandBox;
import com.caiwei.online_judge.judge.codeSandBox.impl.ThirdPartyCodeSandBox;

/**
 * 代码沙箱工厂，根据传入的字符串参数指定对应的代码沙箱
 */
public class CodeSandBoxFactory {
    public static CodeSandBox newInstance(String type) {
        switch (type) {
            /*case "example":
                return new ExampleCodeSandBox();*/
            case "remote":
                return new RemoteCodeSandBox();
            case "third":
                return new ThirdPartyCodeSandBox();
            default:
                /*return new ExampleCodeSandBox();*/
                return new RemoteCodeSandBox();
        }
    }
}
