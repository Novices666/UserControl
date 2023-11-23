package cc.novices.usercontrol;
import cc.novices.usercontrol.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //增
        valueOperations.set("NovicesString","dog");
        valueOperations.set("NovicesInt",1);
        valueOperations.set("NovicesDouble",2.0);
        User user = new User();
        user.setId(1L);
        user.setUsername("Novices");
        valueOperations.set("NovicesUser",user);
        //查
        Object Novices = valueOperations.get("NovicesString");
        Assertions.assertTrue("dog".equals((String)Novices));
        Novices = valueOperations.get("NovicesInt");
        Assertions.assertTrue(1==((Integer)Novices));
        Novices = valueOperations.get("NovicesDouble");
        Assertions.assertTrue(2.0==((Double)Novices));
        System.out.println(valueOperations.get("NovicesUser"));
//        valueOperations.set("NovicesString","dog");
//        redisTemplate.delete("NovicesString");
    }
}