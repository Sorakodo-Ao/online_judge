package com.caiwei.online_judge.judge;

import com.caiwei.online_judge.model.entity.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);

    /*String health();*/
}
