package com.polaris.userCenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.polaris.userCenter.common.BaseResponse;
import com.polaris.userCenter.common.ResultUtil;
import com.polaris.userCenter.model.UserRequest.LoginRequest;
import com.polaris.userCenter.model.UserRequest.RegisterRequest;
import com.polaris.userCenter.model.domain.User;
import com.polaris.userCenter.service.UserService;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.List;
import java.util.stream.Collectors;

import static com.polaris.userCenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * @Author polaris
 * @Create 2024-01-31 13:57
 * @Version 1.0
 * ClassName userController
 * Package com.polaris.userCenter.controller
 * Description
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /*
     * @author polaris
     * @description 用户登录
     **/
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        // 信息校验
        if (loginRequest == null){
            return null;
        }
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        return ResultUtil.success(userService.UserLogin(userAccount, userPassword, request));
    }

    /*
     * @author polaris
     * @create 2024/2/15
     * @description 用户注销
     **/
    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request){
        if (request == null){
            return null;
        }
        return userService.UserLogout(request);
    }
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody RegisterRequest registerRequest){
        if (registerRequest == null){
            return null;
        }
        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        String authCode = registerRequest.getAuthCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,authCode)){
            return null;
        }
        return ResultUtil.success(userService.userRegister(userAccount, userPassword,checkPassword,authCode));
    }

    /*
     * @author polaris
     * @description 用户删除
     **/
    @GetMapping("/delete/{id}")
    public BaseResponse<Boolean> userDelete(@PathVariable long id,HttpServletRequest request){
        // 鉴权  信息校验
        if (!userService.isAdmin(request)||id <= 0){
            return  null;
        }
        return ResultUtil.success(userService.removeById(id));
    }

    /*
     * @author polaris
     * @description 用户查询
     **/
    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(String userAccount,HttpServletRequest request){
        // 鉴权 信息校验
        if (!userService.isAdmin(request)){
            return ResultUtil.success(Collections.emptyList());
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userAccount)){
            userQueryWrapper.eq("user_account",userAccount);
        }
        List<User> list = userService.list(userQueryWrapper);
        // 用户信息脱敏
        return ResultUtil.success(list.stream().map(user->
                userService.filterUserSafetyInfo(user)).collect(Collectors.toList()));
    }

    /*
     * @author polaris
     * @description 当前用户信息获取
     **/
    @GetMapping("/current")
    public BaseResponse<User> currentUser(HttpServletRequest request){
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATUS);
        if (userObject == null){
            return null;
        }
        // todo 校验用户是否合法
        User currentUser = (User)userObject;
        return ResultUtil.success(userService.filterUserSafetyInfo(userService.query().eq("id", currentUser.getId()).one()));
    }
}
