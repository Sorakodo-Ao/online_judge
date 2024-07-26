package com.caiwei.online_judge.judge.strategy.judgeStrategy;

import cn.hutool.json.JSONUtil;
import com.caiwei.online_judge.judge.strategy.JudgeStrategy;
import com.caiwei.online_judge.judge.strategy.context.JudgeContext;
import com.caiwei.online_judge.model.dto.question.JudgeCase;
import com.caiwei.online_judge.model.dto.question.JudgeConfig;
import com.caiwei.online_judge.model.dto.questionSubmit.JudgeInfo;
import com.caiwei.online_judge.model.entity.Question;
import com.caiwei.online_judge.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.stream.Collectors;

public class JavaJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        Question question = judgeContext.getQuestion();
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();


        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPT;
        //题目不需要输入输出用例，只有答案
        if (judgeCaseList.size() == 0
                || judgeCaseList.get(0).getInput().isEmpty()
                || judgeCaseList.get(0).getOutput().isEmpty()) {
            String output = outputList.get(0);
            String answer = question.getAnswer();
            if (!output.equals(answer)) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            }
        } else {
            //预期输出用例
            List<String> outputCaseList = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
            //输出judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;答案和预期答案不同时
            if (outputList.size() != outputCaseList.size()) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            }
            if (outputList != null && !outputList.isEmpty()) {
                for (int i = 0; i < outputCaseList.size(); i++) {
                    if (!outputList.get(i).equals(outputCaseList.get(i))) {
                        judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                    }
                }
            }
        }


        //判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        //获得执行后的信息
        String message = judgeInfo.getMessage();
        Integer memory = judgeInfo.getMemory();
        Integer time = judgeInfo.getTime();

        //内存溢出和超时
        if (memory > judgeConfig.getMemoryLimit()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
        }
        if (time > judgeConfig.getTimeLimit()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
        }
        //危险操作
        if ("danger".equals(message)) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.DANGEROUS_OPERATION;
        }
        //设置返回的判题策略信息
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        judgeInfoResponse.setMemory(judgeInfo.getMemory());
        judgeInfoResponse.setTime(time);

        return judgeInfoResponse;
    }
}
