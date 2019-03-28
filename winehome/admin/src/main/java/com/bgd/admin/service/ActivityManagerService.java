package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.ActivityManagerDao;
import com.bgd.admin.entity.ActivityManagerDto;
import com.bgd.admin.entity.param.ActivityFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdStringUtil;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 活动信息管理 Service
 * @author sunxk 
 * @since 2019-3-18
 */
@Slf4j
@Service
public class ActivityManagerService {
    
    @Autowired
    ActivityManagerDao activityManagerDao;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 新增活动信息
     * @param activityManagerDto 活动信息Dto
     * @throws BusinessException
     */
    public void addActivity(ActivityManagerDto activityManagerDto) throws BusinessException {
        log.info("新增活动 name = "+ activityManagerDto.getName()+"活动标题title = "+ activityManagerDto.getTitle());
        try{
            activityManagerDto.setCreateTime(new Date());
            activityManagerDao.addActivity(activityManagerDto);
            redisUtil.set(ManagerConstant.REDIS.ACTIVITY+ activityManagerDto.getId(), JSONObject.toJSONString(activityManagerDto));
        }catch(Exception e){
            log.error("新增活动产品异常 name = "+ activityManagerDto.getName()+" title = "+ activityManagerDto.getTitle(),e);
            throw new BusinessException("新增活动产品异常");
        }
    }

    /**
     * 删除活动信息
     * @param activityId 活动信息Id
     * @throws BusinessException
     */
    public void deleteActivity(Long activityId) throws BusinessException{
        log.info("删除活动产品 ActivityId = " + activityId);
        try{
            if (BgdStringUtil.isStringEmpty(activityId.toString())){
                throw new BusinessException("删除活动信息Id不能为空");
            }
            activityManagerDao.deleteActivity(activityId);
            redisUtil.remove(ManagerConstant.REDIS.ACTIVITY+activityId);
        }catch(Exception e){
            log.error("删除活动产品异常",e);
            throw new BusinessException("删除活动产品异常 ActivityId = " + activityId);
        }
    }

    /**
     * 修改活动信息
     * @param activityManagerDto 活动信息Dto
     * @throws BusinessException
     */
    public void updateActivity(ActivityManagerDto activityManagerDto)throws BusinessException{
        log.info("修改活动 name = "+ activityManagerDto.getName()+"活动标题title = "+ activityManagerDto.getTitle());
        try{
            activityManagerDto.setUpdateTime(new Date());
            activityManagerDao.updateActivity(activityManagerDto);
            redisUtil.set(ManagerConstant.REDIS.ACTIVITY+ activityManagerDto.getId(),JSONObject.toJSONString(activityManagerDto));
        }catch(Exception e){
            log.error("修改活动产品异常",e);
            throw new BusinessException("修改活动产品异常 name = "+ activityManagerDto.getName()
                    +" title = "+ activityManagerDto.getTitle());
        }
    }

    /**
     * 分页查询活动
     * @param activityFindParam 活动信息Dto
     * @return
     * @throws BusinessException
     */
    public PageDto<ActivityManagerDto> findActivity(ActivityFindParam activityFindParam) throws  BusinessException{
        log.info("查询活动 name = "+ activityFindParam.getName()+"活动标题title = "+ activityFindParam.getTitle());
        try{
            PageDto<ActivityManagerDto> pageDto = new PageDto<>();
            PageBean<ActivityManagerDto, ActivityFindParam> pageBean = new PageBean<ActivityManagerDto, ActivityFindParam>(activityFindParam) {
                @Override
                protected Long generateRowCount() throws BusinessException {
                    return activityManagerDao.countActivity(activityFindParam);
                }

                @Override
                protected List<ActivityManagerDto> generateBeanList() throws BusinessException {
                    return activityManagerDao.findActivity(activityFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<ActivityManagerDto> list = pageBean.getPageData();
            pageDto.setTotal(total);
            pageDto.setList(list);
            return pageDto;
        }catch(Exception e){
            log.error("查询活动产品信息异常 name = "+ activityFindParam.getName()+"title = "+ activityFindParam.getTitle(),e);
            throw new BusinessException("查询活动产品信息异常");
        }
    }
    
}
