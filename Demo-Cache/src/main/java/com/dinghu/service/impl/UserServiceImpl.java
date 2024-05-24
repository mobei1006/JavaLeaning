package com.dinghu.service.impl;

import com.dinghu.bean.User;
import com.dinghu.service.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 18:43
 * @Description:
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    // 使用y一个HashMap来模拟数据库
    public HashMap<String, User> database = new HashMap<>();

    // 注入缓存
    @Resource
    private Cache<String, Object> caffeineCache;


    @Override
    public User getUserInfo(Integer id) {
        String key = id.toString();
        User user = (User) caffeineCache.getIfPresent(key);
        if (ObjectUtils.isEmpty(user)) {
            log.info("缓存数据为空，走数据库查询");
            user = database.get(key);
            caffeineCache.put(key, user);
            return user;
        } else {
            log.info("缓存数据不为空，走缓存，无需查数据库");
            return user;
        }
    }

    @Override
    public void addUser(User user) {
        String key = user.getId().toString();
        database.put(key, user);
    }
}
