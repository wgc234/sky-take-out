package com.wgc.dto;

import lombok.Data;

@Data
public class EmployeeLoginDTO {
    /**
     * 密码
     */
    private String password;
    
    /**
     * 用户名
     */
    private String username;
}
