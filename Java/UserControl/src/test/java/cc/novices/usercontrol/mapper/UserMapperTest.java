package cc.novices.usercontrol.mapper;

import cc.novices.usercontrol.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {
    @Resource
    UserService userService;
    @Test
    void insertTest(){
        User user = new User();
        user.setUsername("novices");
        user.setUseraccount("novices123");
        user.setAvatarurl("https://www.novices.cc/favicon.ico");
        user.setGender(0);
        user.setUserpassword("123123");
        user.setPhone("1231231");
        user.setEmail("1321213");
        boolean result =  userService.save(user);
        assertTrue(result);
        System.out.println(user.getId());

    }


}