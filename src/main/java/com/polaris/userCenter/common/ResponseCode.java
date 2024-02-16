package com.polaris.userCenter.common;

import lombok.Getter;

/**
 * @Author Administrator
 * @Create 2024-02-16 16:31
 * @Version 1.0
 * ClassName ResponseCode
 * Package com.polaris.userCenter.common
 * Description 自定义的响应码
 */
@Getter
public enum ResponseCode {
    SUCCESS(20000, "success", "操作成功"),
    PARAMS_ERROR(40000, "param_error", "参数错误"),
    NULL_ERROR(40001, "null_error", "参数为空"),
    NOT_LOGIN_ERROR(40100, "not_login", "未登录"),
    UNKNOWN_ERROR(40200, "unknown_error", "未知错误"),
    NO_AUTH_ERROR(40300, "not_admin", "无权限"),
    SYSTEM_ERROR(50000,"system_error", "系统内部异常");

    private final int code;
    private final String message;
    private final String description;

    ResponseCode (int code, String msg, String description){
        this.code = code;
        this.message = msg;
        this.description = description;
    }


}
