package com.polaris.userCenter.model.UserRequest;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Administrator
 * @Create 2024-01-31 14:40
 * @Version 1.0
 * ClassName RegisterRequest
 * Package com.polaris.userCenter.model.UserRequest
 * Description
 */
@Data
public class RegisterRequest implements Serializable {
    private static final long serialVersionUID = 1471471935615918L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String authCode;
}
