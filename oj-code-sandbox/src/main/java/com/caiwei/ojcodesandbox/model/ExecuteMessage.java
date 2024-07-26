package com.caiwei.ojcodesandbox.model;

import lombok.Data;

/**
 * 进程执行信息
 */
@Data
public class ExecuteMessage {
    private Integer exitValue;//编译执行的错误码，正常退出错误码是0
    private String message;//执行成功的结果
    private String errorMessage;//执行错误jvm返回的信息
    private Long time;//执行消耗的时间
}
