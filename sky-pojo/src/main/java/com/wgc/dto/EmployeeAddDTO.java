package com.wgc.dto;

import lombok.Data;

/**
 * 新增员工 DTO
 */
@Data
public class EmployeeAddDTO {

    /**
     * 员工 ID（非必须）
     */
    private Long id;

    /**
     * 身份证号码（必须）
     */
    private String idNumber;

    /**
     * 姓名（必须）
     */
    private String name;

    /**
     * 手机号（必须）
     */
    private String phone;

    /**
     * 性别（必须）
     */
    private String sex;

    /**
     * 用户名（必须）
     */
    private String username;
}
