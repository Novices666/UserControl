package cc.novices.usercontrol.service.impl;

import cc.novices.usercontrol.model.domain.User;
import cc.novices.usercontrol.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    UserService userService;
    @Test
    void userRegister() {
        String userAccount = "";
        String userPassword = "";
        String chackPassword = "";
        //非空
        long result = userService.userRegister(userAccount, userPassword, chackPassword);
        assertEquals(-1,result);

        userAccount = "123";
        userPassword = "123456";
        chackPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, chackPassword);
        assertEquals(-1,result);

        userAccount = "novices";
        userPassword = "123456";
        chackPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, chackPassword);
        assertEquals(-1,result);

        userAccount = "novices123";
        userPassword = "12345678";
        chackPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, chackPassword);
        assertEquals(-1,result);

        userAccount = "novice  s123";
        userPassword = "12345678";
        chackPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, chackPassword);
        assertEquals(-1,result);

        userAccount = "novices123";
        userPassword = "12345678";
        chackPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, chackPassword);
        assertEquals(-1,result);

    }

    @Test
    void userLogin() {
        String userAccount = "novices";
        String userPassword = "123456";
        userService.userLogin(userAccount,userPassword,null);
    }

    @Test
    void testSearchUserByTagsName_AND(){
        List<String> list = Arrays.asList("java","python","C++");
        List<User> userList = userService.searchUserByTagsName_AND(list);
        assertEquals(1,userList.size());
    }
}