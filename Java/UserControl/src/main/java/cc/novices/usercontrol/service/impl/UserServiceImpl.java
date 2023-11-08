package cc.novices.usercontrol.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.novices.usercontrol.model.domain.User;
import cc.novices.usercontrol.service.UserService;
import cc.novices.usercontrol.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Pattern;

/**
* @author 16014
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-11-08 10:53:50
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    UserMapper userMapper;
    @Override
    public long userRegister(String userAccount, String userPassword, String chackPassword) {
        //1.校验前端传入数据
        if(StringUtils.isAnyBlank(userAccount,userPassword,chackPassword)){//非空校验
            return -1;
        }
        if (userAccount.length()<4){
            return -1;
        }
        if (userPassword.length()<8||chackPassword.length()<8){
            return -1;
        }

        final String REGSTR = "\\pP|\\pS|\\s+";
        Pattern compile = Pattern.compile(REGSTR);
        if (compile.matcher(userAccount).find()) {
            return -1;
        }
        if (!userPassword.equals(chackPassword)){
            return -1;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        if (userMapper.selectCount(queryWrapper)>0){
            return -1;
        }

        //2.加密密码
        final String SALT = "novices";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //3.插入数据库
        User user = new User();
        user.setUseraccount(userAccount);
        user.setUserpassword(encryptPassword);
        if(!this.save(user)){
            return -1;
        }
        return user.getId();
    }
}




