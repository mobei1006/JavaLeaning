package com.dinghu.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 13:51
 * @Description:
 */

@Component
@EnableScheduling
@Slf4j
public class TestSchedule {

    @Scheduled(cron = "0 0 / 5 * * * ?") // 每5分钟执行一次
    public void demoSchedule() {
        // 编写任务
        log.info("定时任务执行");
    }


}
