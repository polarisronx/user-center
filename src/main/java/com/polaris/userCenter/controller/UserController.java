package com.polaris.userCenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.polaris.userCenter.common.BaseResponse;
import com.polaris.userCenter.common.ResponseCode;
import com.polaris.userCenter.common.ResultUtil;
import com.polaris.userCenter.exception.BusinessException;
import com.polaris.userCenter.model.UserRequest.LoginRequest;
import com.polaris.userCenter.model.UserRequest.RegisterRequest;
import com.polaris.userCenter.model.domain.User;
import com.polaris.userCenter.service.PythonService;
import com.polaris.userCenter.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
    @Resource
    private PythonService pythonService;

    /*
     * @author polaris
     * @description 用户登录
     **/
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        // 信息校验
        if (loginRequest == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ResponseCode.NULL_ERROR,"用户登录信息不能为空");
        }
        return ResultUtil.success(userService.UserLogin(userAccount, userPassword, request));
    }

    /*
     * @author polaris
     * @create 2024/2/15
     * @description 用户注销
     **/
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        return ResultUtil.success(userService.UserLogout(request));
    }
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody RegisterRequest registerRequest){
        if (registerRequest == null){
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        String authCode = registerRequest.getAuthCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,authCode)){
            throw new BusinessException(ResponseCode.NULL_ERROR,"用户注册信息不能为空");
        }
        return ResultUtil.success(userService.userRegister(userAccount, userPassword,checkPassword,authCode));
    }

    /*
     * @author polaris
     * @description 用户删除
     **/
    @GetMapping("/delete/{id}")
    public BaseResponse<Boolean> userDelete(@PathVariable long id,HttpServletRequest request){
        if (id <= 0) throw new BusinessException(ResponseCode.PARAMS_ERROR,"用户ID不合法");
        // 鉴权  信息校验
        if (!userService.isAdmin(request)) throw new BusinessException(ResponseCode.NO_AUTH_ERROR);
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
            throw new BusinessException(ResponseCode.NO_AUTH_ERROR);
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
            throw new BusinessException(ResponseCode.NOT_LOGIN_ERROR);
        }
        // todo 校验用户是否合法
        User currentUser = (User)userObject;
        return ResultUtil.success(userService.filterUserSafetyInfo(userService.query().eq("id", currentUser.getId()).one()));
    }

    /*
     *
     * @param RegisterRequest
     * @description 用feign远程调用python写的用户注册服务，该服务注册在 nacos 微服务注册中心上，
     * 该服务需要接收一个application/json格式的数据，该数据包含用户名和密码.
     * 该服务用Django框架搭建.端口8000.请求地址
     *  http://127.0.0.1:8000/user/register
     * 运用feign本地远程调用地址
     *  http://127.0.0.1:8080/api/user/python/register
     * @author polaris
     * @create 2024/2/23
     **/
    @PostMapping("/python/register")
    public BaseResponse<HashMap<String,Object>> pythonRegister(@RequestBody RegisterRequest registerRequest){
        HashMap<String, Object> map;
        map = pythonService.userRegister(registerRequest);
        System.out.println(map);
        return ResultUtil.success(map);
    }
}
