package com.caiwei.ojcodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEmun {
    SUCCEESS(1),
    FAILURE(2),
    TIMEOUT(3),
    MEMORYOUT(4),
    SANDBOX_ERROR(6);

    private Integer status;

    }
