package com.dinghu.framework;

import java.util.concurrent.TimeUnit;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/30 16:21
 * @Description:
 */
public class Main {

    static CacheManager manager = new CacheManager();

    public static void main(String[] args) {
        manager.createCache("userData", CacheType.LRU);
        ICache<String, User> userDataCache = manager.getCache("userData", String.class, User.class);
        userDataCache.put("user1", new User("user1"));
        userDataCache.expireAfter("user1", 1, TimeUnit.SECONDS);
        userDataCache.get("user1").ifPresent(value -> System.out.println("找到用户：" + value));
        try {
            Thread.sleep(2000L);
        } catch (Exception e) {
        }
        boolean present = userDataCache.get("user1").isPresent();
        if (!present) {
            System.out.println("用户不存在");
        }
    }

}
