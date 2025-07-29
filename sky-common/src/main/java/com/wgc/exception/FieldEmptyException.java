package com.wgc.exception;

/**
 * 字段为空异常类
 * 当用户提交的数据中某些必填字段为空时抛出此异常
 */
public class FieldEmptyException extends BaseException {
    public FieldEmptyException(String message) {
        super(message);
    }
}
