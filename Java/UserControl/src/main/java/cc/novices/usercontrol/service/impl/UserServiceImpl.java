package cc.novices.usercontrol.service.impl;

import cc.novices.usercontrol.constant.UserConstant;
import cc.novices.usercontrol.mapper.UserMapper;
import cc.novices.usercontrol.model.domain.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.novices.usercontrol.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @author 16014
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
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || chackPassword.length() < 8) {
            return -1;
        }

        final String REGSTR = "\\pP|\\pS|\\s+";
        Pattern compile = Pattern.compile(REGSTR);
        if (compile.matcher(userAccount).find()) {
            return -1;
        }
        if (!userPassword.equals(chackPassword)) {
            return -1;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        if (userMapper.selectCount(queryWrapper) > 0) {
            return -1;
        }

        //2.加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据库
        User user = new User();
        user.setUseraccount(userAccount);
        user.setUserpassword(encryptPassword);
        if (!this.save(user)) {
            return -1;
        }
        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount 用户名
     * @param userPassword 用户名密码
     * @param request 请求
     * @return 登录脱敏后的用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验前端传入数据
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {//非空校验
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        final String REGSTR = "\\pP|\\pS|\\s+";
        Pattern compile = Pattern.compile(REGSTR);
        if (compile.matcher(userAccount).find()) {
            return null;
        }

        //2.加密密码

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.查询数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }
        //4.脱敏
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUseraccount(user.getUseraccount());
        safetyUser.setAvatarurl(user.getAvatarurl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUsertype(user.getUsertype());

        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }
}




