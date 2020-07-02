package com.hui.gmall.order.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/7/2 15:44
 */
@Component
@EnableScheduling
public class OrderTask {
    // 5 每分钟的第五秒
// 0/5 没隔五秒执行一次
    @Scheduled(cron = "5 * * * * ?")
    public void  work(){
        System.out.println("Thread ====== "+ Thread.currentThread());
    }
    @Scheduled(cron = "0/5 * * * * ?")
    public void  work1(){
        System.out.println("Thread1 ====== "+ Thread.currentThread());
    }

}
