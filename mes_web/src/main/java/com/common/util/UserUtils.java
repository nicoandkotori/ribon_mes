package com.common.util;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


/**
 *  用户操作类
 */
public class UserUtils {

    public static WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
    //private static RedisService redisService = (RedisService) context.getBean("redisService");

    private static RedisTemplate<Object, Object> redisTemplate =(RedisTemplate) context.getBean("redisTemplate");

    /**
     * 从缓存中获取当前用户
     * @return 取不到返回 new User()
   /*  *//*
    public static User getUser(String ticket){
        User user = null;
        if (!StringUtils.isEmpty(ticket)) {
            String userJson = redisService.get(ticket);
            if (!StringUtils.isEmpty(userJson)){
                user = JSON.parseObject(userJson, User.class);
            }
        }

        if (user == null) {
            // 如果没有登录，则返回实例化空的User对象。
            user = new User();
        }

        return user;
    }*/


}
