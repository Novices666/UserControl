package cc.novices.usercontrol;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cc.novices.usercontrol.mapper")
public class UserControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserControlApplication.class, args);
    }

}