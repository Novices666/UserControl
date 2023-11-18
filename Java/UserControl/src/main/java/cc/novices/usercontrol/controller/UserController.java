package cc.novices.usercontrol.controller;

import cc.novices.usercontrol.commons.BusinessException;
import cc.novices.usercontrol.commons.Result;
import cc.novices.usercontrol.commons.ResultEnum;
import cc.novices.usercontrol.constant.UserConstant;
import cc.novices.usercontrol.model.domain.User;
import cc.novices.usercontrol.model.domain.request.UserLoginRequest;
import cc.novices.usercontrol.model.domain.request.UserRegisterRequest;
import cc.novices.usercontrol.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/user")
@RestController
@CrossOrigin
public class UserController {

    @Resource
    UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册需要的参数封装Request
     * @return 成功返回ID，失败返回null/-1
     */
    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS,"注册账号、密码或确认密码为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String chackPassword = userRegisterRequest.getChackPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, chackPassword)) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS,"注册账号、密码或确认密码为空");
        }
        long userId = userService.userRegister(userAccount, userPassword, chackPassword);
        return new Result<>().ok(userId,"注册成功");
    }

    /**
     * 用户登录
     * @param userLoginRequest 用户登录需要的参数封装Request
     * @param request 请求request
     * @return 成功返回脱敏后的登录对象，失败返回null
     */
    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS,"账号或密码为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS,"账号或密码为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return new Result<>().ok(user,"登录成功");
    }

    @PostMapping("/outLogin")
    public Result userOutLogin(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return new Result<>().ok("退出登录成功");
    }

    @GetMapping("/searchAll")
    public Result searchAll(HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ResultEnum.NO_ACCESS);
        }
        List<User> userList = userService.list();
        // 验证可行性:不可行
        //userList.forEach(user -> userService.getSafetyUser(user));
        userList = userList.stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return new Result<>().ok(userList);
    }

    /**
     * 通过用户名模糊搜索用户  只有管理员可以操作
     * @param username 用户名
     * @param request 请求
     * @return 用户列表
     */
    @GetMapping("/search")
    public Result searchByUsername(String username, HttpServletRequest request) {
        if (username == null) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ResultEnum.NO_ACCESS);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", username);
        List<User> userList = userService.list(wrapper);

        // 验证可行性:不可行
        //userList.forEach(user -> userService.getSafetyUser(user));
        userList = userList.stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return new Result<>().ok(userList);
    }

    /**
     * 根据ID删除用户  只有管理员可以操作
     * @param userId 用户ID
     * @param request 请求
     * @return 成功返回true，失败返回false
     */
    @GetMapping("/delete")
    public Result deleteByID(long userId, HttpServletRequest request) {
        if (userId <= 0) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        if (!isAdmin(request)) {
            throw new BusinessException(ResultEnum.NO_ACCESS);
        }
        boolean reslut = userService.removeById(userId);
        if(!reslut){
            throw new BusinessException(ResultEnum.ERROR_PARAMS,"用户不存在");
        }
        return new Result<>().ok("删除成功");
    }

    /**
     * 获取当前登录用户，从session中取
     * @param request 当前请求，包含session
     * @return 返回实体类
     */
    @GetMapping("/current")
    public Result current(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ResultEnum.NO_LOGIN,"用户未登录，请登录后重试");
        }
        User user = (User) userObj;
        long userId = user.getId();
        User currentUser = userService.getById(userId);
        if(currentUser == null){
            throw new BusinessException(ResultEnum.ERROR_PARAMS,"用户不存在");
        }
        User safetyUser = userService.getSafetyUser(currentUser);
        return new Result<>().ok(safetyUser);
    }


    @GetMapping("/search/tags")
    private Result searchUserByTagsName_AND(@RequestParam(required=false) List<String> tagList){
        if(tagList == null || tagList.size()<1){
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        List<User> userList = userService.searchUserByTagsName_AND(tagList);
        return new Result<>().ok(userList);
    }

    /**
     * 内部方法，判断当前登录用户是否为管理员
     * @param request 请求
     * @return 是返回true，否则返回false
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ResultEnum.NO_LOGIN,"用户未登录，请登录后重试");
        }
        User user = (User) userObj;
        int userType = user.getUserType();
        if (userType == UserConstant.DEFAULT_USER_TYPE) {
            return false;
        }
        return true;
    }
}
