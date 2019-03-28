package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.PushManagerDao;
import com.bgd.admin.entity.PushManagerDto;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

/**
 * 首页推送信息管理 Service
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@Service
public class PushManagerService {

    @Autowired
    PushManagerDao pushManagerDao;

    @Autowired
    RedisUtil redisUtil;
    /**
     * 添加首页推荐信息
     * @param pushManagerDto 首页推荐Dto
     */
    public void addPush(PushManagerDto pushManagerDto) throws BusinessException {
        log.info("添加首页推荐信息");
        try{
            pushManagerDao.addPush(pushManagerDto);
            redisUtil.set(ManagerConstant.REDIS.PUSH+pushManagerDto.getType(), JSONObject.toJSONString(pushManagerDto));
        }catch(Exception e){
            log.error("添加首页推荐信息异常",e);
            throw new BusinessException("添加首页推荐信息异常");
        }
    }

    /**
     * 改变首页推荐信息
     * @param pushManagerDto 首页推荐Dto
     */
    @Transactional
    public void changePush(PushManagerDto pushManagerDto)throws BusinessException{
        log.info("改变首页推荐信息");
        try{
            pushManagerDao.deletePush(pushManagerDto.getType());
            Date date = new Date();
            pushManagerDto.setCreateTime(date);
            pushManagerDao.addPush(pushManagerDto);
            redisUtil.remove(ManagerConstant.REDIS.PUSH+pushManagerDto.getType());
            redisUtil.set(ManagerConstant.REDIS.PUSH+pushManagerDto.getType(), JSONObject.toJSONString(pushManagerDto));
        }catch(Exception e){
            log.error("改变首页推荐信息异常",e);
            throw new BusinessException("改变首页推荐信息异常");
        }
    }

    /**
     * 查询首页推荐信息
     * @param type 类型 1热销国家 2推荐酒庄 3推荐产品 4精选活动
     * @return
     */
    public List findPush(Integer type)throws BusinessException{
        log.info("查询首页推荐信息");
        try{
            if(type == 1){
                return pushManagerDao.findPushCountry();
            }
            if(type == 2){
                return pushManagerDao.findPushChateau();
            }
            if(type == 3){
                return pushManagerDao.findPushProduct();
            }
            if(type == 4){
                return pushManagerDao.findActivity();
            }else{
                throw new BusinessException("查询首页推荐信息类型错误");
            }
        }catch(Exception e){
            log.error("查询首页推荐信息异常 type = " + type,e);
            throw new BusinessException("查询首页推荐信息异常");
        }
    }


}
