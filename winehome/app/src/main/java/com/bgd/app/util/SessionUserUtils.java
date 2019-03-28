package com.bgd.app.util;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.entity.VipInformationDto;
import com.bgd.support.base.Constants;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.exception.ParameterException;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取用户信息utils
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-12
 */
@Component
@Slf4j
public class SessionUserUtils {


    @Autowired
    private RedisUtil redisUtil;


    /**
     * 获取redis缓存里的用户信息
     *
     * @param token 用户登录标识
     * @return 用户信息
     * @throws BusinessException 异常
     */
    public VipInformationDto getUserByRedis(String token) throws BusinessException {
        try {
            if (null == token) {
                throw new ParameterException("token不能为空");
            }
            //获取redis缓存里的用户信息
            String redisUser = redisUtil.get(Constants.PREFIX_APP + token);
            log.info("用户信息：" + redisUser);
            if (null == redisUser) {
                throw new BusinessException("用户信息失效");
            }
            return JSONObject.parseObject(redisUser, VipInformationDto.class);
        } catch (Exception e) {
            throw e;
        }
    }
}
