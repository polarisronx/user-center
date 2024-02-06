package com.polaris.usercenter.controller;

import com.polaris.usercenter.model.UserRequest.LoginRequest;
import com.polaris.usercenter.model.UserRequest.RegisterRequest;
import com.polaris.usercenter.model.domain.User;
import com.polaris.usercenter.service.UserService;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.polaris.usercenter.constant.UserContant.ADMIN_ROLE;
import static com.polaris.usercenter.constant.UserContant.USER_LOGIN_STATUS;

/**
 * @Author polaris
 * @Create 2024-01-31 13:57
 * @Version 1.0
 * ClassName userController
 * Package com.polaris.usercenter.controller
 * Description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public User userLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        // 信息校验
        if (loginRequest == null){
            return null;
        }
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        return userService.Userlogin(userAccount, userPassword, request);
    }
    @PostMapping("/register")
    public long userRegister(@RequestBody RegisterRequest registerRequest){
        if (registerRequest == null){
            return -1;
        }
        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return -1;
        }
        return userService.userRegister(userAccount, userPassword,checkPassword);
    }
    @GetMapping("/delete/{id}")
    public boolean userDelete(@PathVariable long id,HttpServletRequest request){
        // 鉴权  信息校验
        if (!userService.isAdmin(request)||id <= 0){
            return  false;
        }
        return userService.removeById(id);
    }
    @GetMapping("/search/{username}")
    public List<User> userSearch(@PathVariable String username,HttpServletRequest request){
        // 鉴权 信息校验
        if (!userService.isAdmin(request)||username == null){
            return Collections.emptyList();
        }
        // 用户信息脱敏
        return userService.query().like("userName", username).list().stream().map(user->
                userService.filterUserSafetyInfo(user)).collect(Collectors.toList());
    }
    @GetMapping("/current")
    public User currentUser(HttpServletRequest request){
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (userObject == null){
            return null;
        }
        // todo 校验用户是否合法
        User currentUser = (User)userObject;
        return userService.filterUserSafetyInfo(userService.query().eq("id", currentUser.getId()).one());
    }
}
