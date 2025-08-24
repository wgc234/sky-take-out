package com.wgc.dto;

import lombok.Data;

@Data
public class EmployeeUpdateDTO {

    // 属性定义
    private Long id; // format: int64
    private String idNumber;
    private String name;
    private String phone;
    private String sex;
    private String username;
}
