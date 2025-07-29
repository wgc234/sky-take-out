package com.wgc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 员工映射实体类，用于对接员工表相关字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    /**
     * 主键ID
     */
//    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 性别（例如：男/女）
     */
    private String sex;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 账户状态（例如：启用/禁用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    private Long createUser;

    /**
     * 更新人ID
     */
    private Long updateUser;
}
