package com.wgc.dto;

import lombok.Data;

/**
 * @Description:
     * 修改密码
 */

@Data
public class EmployeeUpdatePasswordDTO {

    //员工id
    private Integer empId;

    //新密码
    private String newPassword;

    //旧密码
    private String oldPassword;
}
