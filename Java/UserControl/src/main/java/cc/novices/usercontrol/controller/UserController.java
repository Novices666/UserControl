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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequestMapping("/user")
@RestController
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @Resource
    RedisTemplate redisTemplate;

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

    /**
     * 获取当前登录用户，从session中取
     * @param request 当前请求，包含session
     * @return 返回实体类
     */
    @GetMapping("/current")
    public Result current(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        User safetyUser = userService.current(request);
        return new Result<>().ok(safetyUser);
    }

    /**
     * 更新用户信息
     * 用户本身或管理员可调用
     * @param user 欲修改的用户
     * @param request 请求信息
     * @return 返回实体类
     */
    @PostMapping("/update")
    public Result updateUser(@RequestBody User user, HttpServletRequest request){
        if(user == null || request== null){
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        User currentUser = userService.current(request);
        if(!Objects.equals(user.getId(), currentUser.getId()) || currentUser.getUserType()!=UserConstant.ADMIN_USER_TYPE ){
            throw new BusinessException(ResultEnum.NO_ACCESS);
        }
        int result = userService.updateUser(user,currentUser);
        if(result != 1){
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        return new Result<>().ok("success");
    }

    /**
     * 退出登录
     * @param request 请求信息，会携带session
     * @return 返回是否退出成功
     */
    @PostMapping("/outLogin")
    public Result userOutLogin(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return new Result<>().ok("退出登录成功");
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
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ResultEnum.NO_ACCESS);
        }
        boolean reslut = userService.removeById(userId);
        if(!reslut){
            throw new BusinessException(ResultEnum.ERROR_PARAMS,"用户不存在");
        }
        return new Result<>().ok("删除成功");
    }

    /**
     * 获取全部用户
     * 方法在用户量大的时候会导致内存不足或请求时间过长
     * @param request 请求信息
     * @return 返回实体类
     */
    @GetMapping("/searchAll")
    @Deprecated
    public Result searchAll(HttpServletRequest request,long pageSize,long pageNum) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ResultEnum.NO_ACCESS);
        }
        Page<User> userPage = userService.page(new Page<>(pageNum,pageSize));
        //List<User> userPage = userService.list();
        // 验证可行性:不可行
        //userPage.forEach(user -> userService.getSafetyUser(user));
        List<User> userList =  userPage.getRecords().stream().map(user -> {
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
    public Result searchByUsername(String username, HttpServletRequest request,long pageSize,long pageNum) {
        if (username == null) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ResultEnum.NO_ACCESS);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("username", username);

        //List<User> userList = userService.list(wrapper);
        Page<User> userPage = userService.page(new Page<>(pageNum,pageSize),wrapper);
        // 验证可行性:不可行
        //userList.forEach(user -> userService.getSafetyUser(user));
        List<User> userList =  userPage.getRecords().stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return new Result<>().ok(userList);
    }





    @GetMapping("/search/tags")
    private Result searchUserByTagsName_AND(@RequestParam(required=false) List<String> tagList,long pageSize,long pageNum){
        if(tagList == null || tagList.size()<1){
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        List<User> userList = userService.searchUserByTagsName_AND(tagList,pageSize,pageNum);
        return new Result<>().ok(userList);
    }

    @GetMapping("/recommend")
    private Result recommendUsers(HttpServletRequest request,long pageSize,long pageNum){
        if(request == null){
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        User loginUser = userService.current(request);
        //自定义缓存key，确保与其他项目不冲突，且用户之间互不冲突
        String redisKey = String.format("gamedating:user:recommend:%s", loginUser.getId());
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        //如果有缓存，直接读缓存
        Page<User> userPage= (Page<User>)valueOperations.get(redisKey);
        if (userPage ==null){
            //没缓存，读数据库并存入缓存
            userPage = userService.page(new Page<>(pageNum,pageSize));
            try {
                //设置过期时间为86400秒，即一天
                valueOperations.set(redisKey,userPage,86400, TimeUnit.SECONDS);
            }catch (Exception e){
                //如果写入缓存失败，不应当影响返回，所以捕获异常
                log.error("redis set key error",e);
            }
        }
        List<User> userList =  userPage.getRecords().stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());


        return new Result<>().ok(userList);
    }

}
