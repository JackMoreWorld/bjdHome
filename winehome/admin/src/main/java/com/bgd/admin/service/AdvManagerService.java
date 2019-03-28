package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.AdvManagerDao;
import com.bgd.admin.entity.AdvManagerDto;
import com.bgd.admin.entity.param.AdvFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.exception.ParameterException;
import com.bgd.support.utils.BgdStringUtil;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 广告信息管理 Service
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@Service
public class AdvManagerService {

    @Autowired
    AdvManagerDao advManagerDao;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 广告信息处理
     *
     * @param advManagerDto 广告信息Dao
     * @throws ParameterException
     */
    public void dealAdvertising(AdvManagerDto advManagerDto) throws ParameterException {

        try {
            Integer dealType = advManagerDto.getDealType();
            if (ManagerConstant.OPERA_TYPE.CREATE.equals(dealType)) {
                log.info("新增广告 广告位名称 name = " + advManagerDto.getName());
                advManagerDto.setCreateTime(new Date());
                advManagerDao.addAdvertising(advManagerDto);
                redisUtil.set(ManagerConstant.REDIS.ADVERTISING + advManagerDto.getId(), JSONObject.toJSONString(advManagerDto));
            }
            if (ManagerConstant.OPERA_TYPE.DEL.equals(dealType)) {
                if (BgdStringUtil.isStringEmpty(advManagerDto.getId().toString())) {
                    throw new BusinessException("删除广告信息ID不能为空");
                }
                log.info("删除广告 广告位名称 name = " + advManagerDto.getName());
                advManagerDao.deleteAdvertising(advManagerDto.getId());
                redisUtil.remove(ManagerConstant.REDIS.ADVERTISING + advManagerDto.getId());
            }
            if (ManagerConstant.OPERA_TYPE.UPDATE.equals(dealType)) {
                log.info("修改广告 广告位名称 name = " + advManagerDto.getName());
                advManagerDto.setUpdateTime(new Date());
                advManagerDao.updateAdvertising(advManagerDto);
                redisUtil.set(ManagerConstant.REDIS.ADVERTISING + advManagerDto.getId(), JSONObject.toJSONString(advManagerDto));
            }
        } catch (Exception e) {
            log.error("处理广告信息异常", e);
            throw new ParameterException("处理广告信息异常");

        }

    }

    /**
     * 分页查询广告信息
     *
     * @param advFindParam 广告信息查询参数
     * @return
     * @throws ParameterException
     */
    public PageDto<AdvManagerDto> findAdvertising(AdvFindParam advFindParam) throws ParameterException {
        log.info("查询广告位名称 name = " + advFindParam.getName());
        try {
            PageDto<AdvManagerDto> pageDto = new PageDto<>();
            PageBean<AdvManagerDto, AdvFindParam> pageBean = new PageBean<AdvManagerDto, AdvFindParam>(advFindParam) {
                @Override
                protected Long generateRowCount() throws BusinessException {
                    return advManagerDao.countAdvertising(advFindParam);
                }

                @Override
                protected List<AdvManagerDto> generateBeanList() throws BusinessException {
                    return advManagerDao.findAdvertising(advFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<AdvManagerDto> advManagerDtoList = pageBean.getPageData();
            pageDto.setTotal(total);
            pageDto.setList(advManagerDtoList);
            return pageDto;
        } catch (Exception e) {
            log.error("查询广告信息异常", e);
            throw new ParameterException("查询广告信息异常");
        }
    }

}
