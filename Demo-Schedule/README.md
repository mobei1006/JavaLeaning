### SpringBoot定时任务
经常我们需要一些定时任务来在特定的时间运行相应的功能，springboot 开启定时任务步骤如下

1. 开启注解
```java
@SpringBootApplication()
@EnableScheduling
public class Demo {
    public static void main(String[] args) {
        SpringApplication.run(Demo.class, args);
    }
}
```

2. 编写定时任务
```java
@EnableScheduling // 开启定时任务
@Component
public class SchedulingClass {
    @Scheduled(cron = "0 0/5 * * * ? ") //每5分钟执行一次
    public void demoScheduling() {
        // 编写任务
    }
}
```
具体的定时规则需要自己编写，这里不过多描述
