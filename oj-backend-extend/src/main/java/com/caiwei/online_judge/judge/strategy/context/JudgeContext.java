package com.caiwei.online_judge.judge.strategy.context;

import com.caiwei.online_judge.model.dto.question.JudgeCase;
import com.caiwei.online_judge.model.dto.questionSubmit.JudgeInfo;
import com.caiwei.online_judge.model.entity.Question;
import com.caiwei.online_judge.model.entity.QuestionSubmit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//用于定义传递给策略的参数
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeContext {
    private Question question;

    private QuestionSubmit questionSubmit;

    private JudgeInfo judgeInfo;

    private List<JudgeCase> judgeCaseList;

    private List<String> inputList;

    private List<String> outputList;

}
