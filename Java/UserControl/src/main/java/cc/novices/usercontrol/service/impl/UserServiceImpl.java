package cc.novices.usercontrol.service.impl;

import cc.novices.usercontrol.commons.BusinessException;
import cc.novices.usercontrol.commons.ResultEnum;
import cc.novices.usercontrol.constant.UserConstant;
import cc.novices.usercontrol.mapper.UserMapper;
import cc.novices.usercontrol.model.domain.User;
import cc.novices.usercontrol.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Novices
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-11-08 10:53:50
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    final String SALT = "novices";
    @Resource
    UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param chackPassword 校验密码
     * @return 注册账号的ID
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String chackPassword) {
        //1.校验前端传入数据
        if (StringUtils.isAnyBlank(userAccount, userPassword, chackPassword)) {//非空校验
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "注册账号、密码或确认密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "注册账号长度过短");
        }
        if (userPassword.length() < 8 || chackPassword.length() < 8) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "密码长度过短");
        }

        final String REGSTR = "\\pP|\\pS|\\s+";
        Pattern compile = Pattern.compile(REGSTR);
        if (compile.matcher(userAccount).find()) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "注册账号存在非法字符");
        }
        if (!userPassword.equals(chackPassword)) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "密码与确认密码不匹配");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        if (userMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "当前注册账号已存在");
        }

        //2.加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        if (!this.save(user)) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "注册遇到未知错误");
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户名
     * @param userPassword 用户名密码
     * @param request      请求
     * @return 登录脱敏后的用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验前端传入数据
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {//非空校验
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "账号或密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "登录账号长度过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "密码长度过短");
        }

        final String REGSTR = "\\pP|\\pS|\\s+";
        Pattern compile = Pattern.compile(REGSTR);
        if (compile.matcher(userAccount).find()) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "账号存在非法字符");
        }

        //2.加密密码

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.查询数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ResultEnum.ERROR_PARAMS, "账号或密码错误");
        }
        //4.脱敏
        User safetyUser = getSafetyUser(user);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }


    @Override
    public List<User> searchUserByTagsName_AND(List<String> tagList,long pageSize,long pageNum) {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        for (String tag : tagList){
//            queryWrapper = queryWrapper.like("tags",tag);
//        }
//        List<User> userList = userMapper.selectList(queryWrapper);
//        return userList;
        if(tagList == null || tagList.size()<1){
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }


        Page<User> userPage = this.page(new Page<>(pageNum,pageSize));
        //1.取回所有用户
        List<User> userList = userPage.getRecords();
        //2.遍历,查询是否有匹配项
        Gson gson = new Gson();
        return userList.stream().filter(user -> {
            String userTags = user.getTags();
//            if (StringUtils.isBlank(userTags)) {
//                return false;
//            }
            Set<String> userTagSet = gson.fromJson(userTags, new TypeToken<Set<String>>() {
            }.getType());
            //替换if，如果不为空，返回本身，如果为空返回orElse中的值
            userTagSet = Optional.ofNullable(userTagSet).orElse(new HashSet<>());
            for (String tagName : tagList) {
                if (!userTagSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());

    }

//    @Override
//    public List<User> searchUserByTagsName_OR(){
//
//    }

    /**
     * 获取当前登录用户，从session中取
     * @param request 当前请求，包含session
     * @return 返回当前登录用户脱敏信息
     */
    @Override
    public User current(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ResultEnum.NO_LOGIN,"用户未登录，请登录后重试");
        }
        User user = (User) userObj;
        long userId = user.getId();
        User currentUser = getById(userId);
        if(currentUser == null){
            throw new BusinessException(ResultEnum.ERROR_PARAMS,"用户不存在");
        }
        return getSafetyUser(currentUser);
    }


    /**
     * 更新用户信息
     *
     * @param user
     * @param currentUser
     * @return
     */
    @Override
    public int updateUser(User user, User currentUser){
        if(user == null || currentUser== null){
            throw new BusinessException(ResultEnum.ERROR_PARAMS);
        }
        if(!Objects.equals(user.getId(), currentUser.getId()) || currentUser.getUserType()!=UserConstant.ADMIN_USER_TYPE ){
            throw new BusinessException(ResultEnum.NO_ACCESS);
        }
        return userMapper.updateById(user);
    }


    /**
     * 用户脱敏，内部方法
     *
     * @param oldUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public User getSafetyUser(User oldUser) {
        User safetyUser = new User();
        safetyUser.setId(oldUser.getId());
        safetyUser.setUsername(oldUser.getUsername());
        safetyUser.setUserAccount(oldUser.getUserAccount());
        safetyUser.setAvatarUrl(oldUser.getAvatarUrl());
        safetyUser.setGender(oldUser.getGender());
        safetyUser.setPhone(oldUser.getPhone());
        safetyUser.setEmail(oldUser.getEmail());
        safetyUser.setTags(oldUser.getTags());
        safetyUser.setUserType(oldUser.getUserType());
        safetyUser.setCreateTime(oldUser.getCreateTime());
        return safetyUser;
    }


    /**
     * 判断当前登录用户是否为管理员
     * @param request 请求
     * @return 是返回true，否则返回false
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
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




