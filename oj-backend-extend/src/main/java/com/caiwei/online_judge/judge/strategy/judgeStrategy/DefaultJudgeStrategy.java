package com.caiwei.online_judge.judge.strategy.judgeStrategy;

import com.caiwei.online_judge.judge.strategy.JudgeStrategy;
import com.caiwei.online_judge.judge.strategy.context.JudgeContext;
import com.caiwei.online_judge.model.dto.questionSubmit.JudgeInfo;

//默认策略
public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMessage(null);
        judgeInfoResponse.setMemory(null);
        judgeInfoResponse.setTime(1);
        return judgeInfoResponse;
    }
}
