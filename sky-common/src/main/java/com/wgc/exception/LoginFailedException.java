package com.wgc.exception;

/**
 * 登录失败异常类
 * 当用户登录时用户名不存在、密码错误或其他登录相关问题时抛出此异常
 */
public class LoginFailedException extends BaseException {
    public LoginFailedException(String message) {
        super(message);
    }
}
