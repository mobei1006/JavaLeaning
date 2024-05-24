### SpringBoot整合Caffeine来实现缓存

在我们的项目中，很多的场景都会运用到数据的查询，但是频繁的访问数据库，尤其是在并发程度比较高的时候，访问量过多会造成数据库的压力增大。所以使用缓存是一种方法来加快我们的访问速度，并且还可以减轻数据库访问的压力。
今天我们学习一下使用 SpringBoot 来整合Caffeine 来实现缓存。实现我们需要引入Caffeine 的依赖包

```xml
<dependency>
  <groupId>com.github.ben-manes.caffeine</groupId>
  <artifactId>caffeine</artifactId>
</dependency>
```
引入依赖之后，我们需要对Caffeine 进行配置。配置内容如下：
```java
@Configuration
public class CacheConfig {
    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
        // 最后一次写入或者访问后多久时间过期
        .expireAfterWrite(60, TimeUnit.SECONDS)
        // 初始化缓存的空间大小
        .initialCapacity(20)
        // 缓存的最大数量
        .maximumSize(100)
        .build();
    }
}
```
配置内容主要是过期时间，容量等。这里我们使用的是 <String, Object>，如果明确让缓存为一个特定的对象服务，那么我们可以直接将 Object 换为特定的对象。配置完成，我们就可以使用，一个简单的 demo 如下：
我们使用 HashMap 模拟我们的数据库，写一个对 User 的添加和查询。
实现我们定义一个 User 对象
```java
@Data
public class User {

    private Integer id;

    private String username;

    private String password;

}
```
然后编写对应的 Controller 层，来实现相应的接口：
```java
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/getUser")
    public Result getUser(@RequestParam("id") Integer id) {
        userService.getUserInfo(id);
        return Result.ok();
    }

    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user) {
        userService.addUser(user);
        return Result.ok();
    }

}
```
然后编写我们的业务层来实现这些方法：
```java
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
```
最后，我们就可以对这个 demo 进行测试，添加一个 id 为 1 和 2 的用户，然后连续访问 2 次 id 为 1 的用户和 id 为 2 的用户，得到的结果如下：

![image.png](https://cdn.nlark.com/yuque/0/2024/png/40783336/1716530972237-3e2b1cac-c8ad-4e30-a4f0-d858baa95d8b.png#averageHue=%2326292e&clientId=u780c4621-2841-4&from=paste&height=115&id=ufd69c903&originHeight=115&originWidth=1210&originalType=binary&ratio=1&rotation=0&showTitle=false&size=37868&status=done&style=none&taskId=u5ec48ce7-790c-412e-92f7-9cbf752d40b&title=&width=1210)

至此，我们就完成了缓存的使用
