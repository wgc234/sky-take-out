package com.wgc.context;

/**
 * 操作线程工具类
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){threadLocal.set(id);}

    public static Long getCurrentId(){return threadLocal.get();}

    public static void removeCurrentId(){threadLocal.remove();}
}
