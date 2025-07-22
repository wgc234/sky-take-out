package com.wgc.result;

import lombok.Data;

@Data
public class Result<T>{
    private String msg;
    private Integer code;
    private  T data;


    public static<T> Result<T> success(){
        Result<T> result=new Result<>();
        result.code=1;
        return result;
    }
    public static<T> Result<T> success(T data){
        Result<T> result=new Result<>();
        result.code=1;
        result.data=data;
        return result;
    }

    public static<T> Result<T> error(String msg){
        Result<T> result=new Result<>();
        result.code=0;
        result.msg=msg;
        return result;
    }
}
