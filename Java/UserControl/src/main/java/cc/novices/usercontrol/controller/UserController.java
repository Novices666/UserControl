package cc.novices.usercontrol.controller;

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
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {

    @Resource
    UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册需要的参数封装Request
     * @return 成功返回ID，失败返回null/-1
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String chackPassword = userRegisterRequest.getChackPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, chackPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, chackPassword);
    }

    /**
     * 用户登录
     * @param userLoginRequest 用户登录需要的参数封装Request
     * @param request 请求request
     * @return 成功返回脱敏后的登录对象，失败返回null
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 通过用户名模糊搜索用户  只有管理员可以操作
     * @param username 用户名
     * @param request 请求
     * @return 用户列表
     */
    @GetMapping("/search")
    public List<User> searchByUsername(String username, HttpServletRequest request) {
        if (username == null) {
            return new ArrayList<>();
        }
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", username);
        List<User> userList = userService.list(wrapper);
        userList.forEach(user -> user.setUserpassword(null));
//        userList = userList.stream().map(user -> {
//            user.setUserpassword(null);
//            return user;
//        }).collect(Collectors.toList());
        return userList;
    }

    /**
     * 根据ID删除用户  只有管理员可以操作
     * @param userId 用户ID
     * @param request 请求
     * @return 成功返回true，失败返回false
     */
    @GetMapping("/delete")
    public boolean deleteByID(long userId, HttpServletRequest request) {
        if (userId <= 0) {
            return false;
        }
        if (!isAdmin(request)) {
            return false;
        }
        return userService.removeById(userId);
    }

    /**
     * 内部方法，判断当前登录用户是否为管理员
     * @param request 请求
     * @return 是返回true，否则返回false
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            return false;
        }
        User user = (User) userObj;
        int userType = user.getUsertype();
        if (userType == UserConstant.DEFAULT_USER_TYPE) {
            return false;
        }
        return true;
    }
}
