package com.caiwei.online_judge.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PersonalQuestion {

    private String title;
    private String language;
    private String tags;//json的 list集合
    private Integer status;
    private String judgeInfo;
    private Date createTime;
}
