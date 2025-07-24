package com.wgc.enums;

/**
 * 启用禁用状态枚举类
 * 0: 禁用
 * 1: 启用
 */
public enum StatusEnum {
    /**
     * 禁用状态
     */
    DISABLE(0, "禁用"),
    
    /**
     * 启用状态
     */
    ENABLE(1, "启用");
    
    private final Integer code;
    private final String description;
    
    StatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据code获取对应的枚举值
     * @param code 状态码
     * @return 对应的枚举值
     */
    public static StatusEnum fromCode(Integer code) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态码: " + code);
    }
    
    @Override
    public String toString() {
        return code + ":" + description;
    }
}
