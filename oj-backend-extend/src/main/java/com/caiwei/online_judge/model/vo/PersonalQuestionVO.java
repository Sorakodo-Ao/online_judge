package com.caiwei.online_judge.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PersonalQuestionVO {
    /*    select title,language,status,judgeInfo,question_submit.createTime as creatTime
        from question,
                question_submit
        where question_submit.questionId = question.id
        and question_submit.userId = 1793601206915911681*/
    private String title;
    private String language;
    private List<String> tags;
    private Integer status;
    private String judgeInfoMessage;
    private Date createTime;
}
