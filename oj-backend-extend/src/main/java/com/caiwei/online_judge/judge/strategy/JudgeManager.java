package com.caiwei.online_judge.judge.strategy;

import com.caiwei.online_judge.judge.strategy.context.JudgeContext;
import com.caiwei.online_judge.judge.strategy.judgeStrategy.CppJudgeStrategy;
import com.caiwei.online_judge.judge.strategy.judgeStrategy.DefaultJudgeStrategy;
import com.caiwei.online_judge.judge.strategy.judgeStrategy.JavaJudgeStrategy;
import com.caiwei.online_judge.model.dto.questionSubmit.JudgeInfo;
import com.caiwei.online_judge.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;


//调用哪种策略
@Service
public class JudgeManager {
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        //获取题目提交选择的语言
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = null;
        if ("java".equals(language)) {
            judgeStrategy = new JavaJudgeStrategy();
        } else if ("c".equals(language) || "cpp".equals(language)) {
            judgeStrategy = new CppJudgeStrategy();
        }else {
            judgeStrategy = new DefaultJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
