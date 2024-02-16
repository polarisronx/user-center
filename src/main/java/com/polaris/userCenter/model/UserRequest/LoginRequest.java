package com.polaris.userCenter.model.UserRequest;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author polaris
 * @Create 2024-01-31 14:28
 * @Version 1.0
 * ClassName loginRequest
 * Package com.polaris.userCenter.model.UserRequest
 * Description
 */
@Data
public class LoginRequest implements Serializable{
    private static final long serialVersionUID = 1471471935615916L;
    private String userAccount;
    private String userPassword;
}
