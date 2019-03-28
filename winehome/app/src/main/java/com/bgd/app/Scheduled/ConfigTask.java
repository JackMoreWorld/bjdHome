package com.bgd.app.Scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/8
 * @描述
 */
@Slf4j
@Component
public class ConfigTask {


    //@Scheduled(fixedDelay = 10000)
    public void test() {
        System.out.println("-----------------");
    }



}
