package cc.novices.usercontrol.service;

import cc.novices.usercontrol.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 16014
* @description 针对表【user】的数据库操作Service
* @createDate 2023-11-08 10:53:50
*/
public interface UserService extends IService<User> {
    long userRegister(String userAccount,String userPassword,String chackPassword);
}
