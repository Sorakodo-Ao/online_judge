package com.caiwei.online_judge.judge.codeSandBox.model;

import com.caiwei.online_judge.model.dto.questionSubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 输出数据
     */
    private List<String> outputList;
    /**
     * 接口信息
     */
    private String message;
    /**
     * 执行状态
     */
    private String status;
    /**
     * 执行信息
     */
    private JudgeInfo judgeInfo;
}
