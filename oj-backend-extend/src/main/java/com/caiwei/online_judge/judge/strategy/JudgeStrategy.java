package com.caiwei.online_judge.judge.strategy;

import com.caiwei.online_judge.judge.strategy.context.JudgeContext;
import com.caiwei.online_judge.model.dto.questionSubmit.JudgeInfo;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
