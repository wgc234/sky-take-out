package com.wgc.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeLoginVO {
    /**
     * 主键值
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * jwt令牌
     */
    private String token;

    /**
     * 用户名
     */
    private String userName;

}
