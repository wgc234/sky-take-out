package com.wgc.handler;


import com.wgc.exception.BaseException;
import com.wgc.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息为: {}",ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
