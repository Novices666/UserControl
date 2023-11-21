package cc.novices.usercontrol.mapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import cc.novices.usercontrol.model.domain.User;
import cc.novices.usercontrol.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

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
        user.setUserAccount("novices123");
        user.setAvatarUrl("https://www.novices.cc/favicon.ico");
        user.setGender(0);
        user.setUserPassword("123123");
        user.setPhone("1231231");
        user.setEmail("1321213");
        boolean result =  userService.save(user);
        assertTrue(result);
        System.out.println(user.getId());

    }

//    @Test //顺序插入1000条
//    void insertDateTest(){
//        final int DATE_NUMBER = 1000;
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        for (int i = 0;i<DATE_NUMBER;i++){
//            User user = new User();
//            user.setUserAccount("jianovices");
//            user.setUserPassword("12345678");
//            user.setUserType(0);
//            user.setUsername("假Novices");
//            user.setAvatarUrl("https://www.novices.cc/favicon.ico");
//            user.setGender(0);
//            user.setPhone("12345678910");
//            user.setEmail("123123123@123.com");
//            user.setTags("[]");
//            userService.save(user);
//        }
//        stopWatch.stop();
//        System.out.println(stopWatch.getLastTaskTimeMillis());
//    }
//    @Test //一次性插入1000条
//    void insertDateTest(){
//        final int DATE_NUMBER = 1000;
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        List<User> userList = new ArrayList<>();
//        for (int i = 0;i<DATE_NUMBER;i++){
//            User user = new User();
//            user.setUserAccount("jianovices");
//            user.setUserPassword("12345678");
//            user.setUserType(0);
//            user.setUsername("假Novices");
//            user.setAvatarUrl("https://www.novices.cc/favicon.ico");
//            user.setGender(0);
//            user.setPhone("12345678910");
//            user.setEmail("123123123@123.com");
//            user.setTags("[]");
//            userList.add(user);
//        }
//        userService.saveBatch(userList);
//        stopWatch.stop();
//        System.out.println(stopWatch.getLastTaskTimeMillis());
//    }

//    @Test//使用默认线程池
//    void insertDateTest(){
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        //分10组
//        int batchSize = 10000;
//        int j=0;
//        List<CompletableFuture<Void>> futureList = new ArrayList<>();
//        for (int i = 0; i < 500; i++) {
//            List<User> userList = new ArrayList<>();
//            while (true){
//                j++;
//                User user = new User();
//                user.setUserAccount("jianovices");
//                user.setUserPassword("12345678");
//                user.setUserType(0);
//                user.setUsername("假Novices");
//                user.setAvatarUrl("https://www.novices.cc/favicon.ico");
//                user.setGender(0);
//                user.setPhone("12345678910");
//                user.setEmail("123123123@123.com");
//                user.setTags("[]");
//                userList.add(user);
//                if (j % batchSize==0){
//                    break;
//                }
//            }
//            //异步执行
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->{
//                System.out.println("threadName:"+Thread.currentThread().getName());
//                userService.saveBatch(userList,batchSize);
//            });
//            futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }

    @Test//使用自定义线程池
    void insertDateTest(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(30,1000,1000, TimeUnit.MINUTES,new ArrayBlockingQueue<>(10000));
        //分10组
        int batchSize = 10000;
        int j=0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            List<User> userList = new ArrayList<>();
            while (true){
                j++;
                User user = new User();
                user.setUserAccount("jianovices");
                user.setUserPassword("12345678");
                user.setUserType(0);
                user.setUsername("假Novices");
                user.setAvatarUrl("https://www.novices.cc/favicon.ico");
                user.setGender(0);
                user.setPhone("12345678910");
                user.setEmail("123123123@123.com");
                user.setTags("[]");
                userList.add(user);
                if (j % batchSize==0){
                    break;
                }
            }
            //异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->{
                System.out.println("threadName:"+Thread.currentThread().getName());
                userService.saveBatch(userList,batchSize);
            },poolExecutor);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }


}