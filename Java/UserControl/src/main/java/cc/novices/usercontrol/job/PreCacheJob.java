package cc.novices.usercontrol.job;
/**
 * 定时任务类
 * 缓存指定用户的推荐信息
 */

import cc.novices.usercontrol.model.domain.User;
import cc.novices.usercontrol.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class PreCacheJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    //重点用户
    private List<Long> mainUserList = List.of(2L);

    //每天执行，预热推荐用户
    @Scheduled(cron = "0 0 0 * * *")
    public void doCacheRecommendUser(){
        for (Long userId: mainUserList){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
            String redisKey = String.format("gamedating:user:recommend:%s", userId);
            ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
            //写缓存
            try {
                //设置过期时间为86400秒，即一天
                valueOperations.set(redisKey,userPage,86400, TimeUnit.SECONDS);
            }catch (Exception e){
                log.error("redis set key error",e);
            }
        }
    }

}