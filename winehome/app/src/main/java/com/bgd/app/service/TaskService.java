
package com.bgd.app.service;

import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskService {
    @Autowired
    RedisUtil redisUtil;


}