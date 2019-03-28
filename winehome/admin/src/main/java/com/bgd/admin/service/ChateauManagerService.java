package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.ChateauManagerDao;
import com.bgd.admin.entity.ChateauManagerDto;
import com.bgd.admin.entity.param.ChateauFindParam;
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
 * 酒庄信息管理 Service
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Service
@Slf4j
public class ChateauManagerService {

    @Autowired
    ChateauManagerDao chateauManagerDao;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 新增酒庄信息
     *
     * @param chateauManagerDto 酒庄信息Dto
     * @throws BusinessException
     */
    public void addChateau(ChateauManagerDto chateauManagerDto) throws BusinessException {
        log.info("新增酒庄 name = " + chateauManagerDto.getName());
        try {
            chateauManagerDto.setCreateTime(new Date());
            chateauManagerDao.addChateau(chateauManagerDto);
            redisUtil.set(ManagerConstant.REDIS.CHATEAU + chateauManagerDto.getId(), JSONObject.toJSONString(chateauManagerDto));
        } catch (Exception e) {
            log.error("新增酒庄异常 name = " + chateauManagerDto.getName(), e);
            throw new BusinessException("新增酒庄异常");
        }
    }

    /**
     * 删除酒庄信息
     *
     * @param chateauId 酒庄ID
     * @throws BusinessException
     */
    public void deleteChateau(Long chateauId) throws BusinessException {
        log.info("删除酒庄 chateauId = " + chateauId);
        try {
            if (BgdStringUtil.isStringEmpty(chateauId.toString())) {
                throw new BusinessException("删除酒庄信息ID不能为空");
            }
            chateauManagerDao.deleteChateau(chateauId);
            redisUtil.remove(ManagerConstant.REDIS.CHATEAU + chateauId);
        } catch (Exception e) {
            log.error("删除酒庄异常 chateauId = " + chateauId, e);
            throw new BusinessException("删除酒庄异常");
        }
    }

    /**
     * 修改酒庄信息
     *
     * @param chateauManagerDto 酒庄信息Dto
     * @throws BusinessException
     */
    public void updateChateau(ChateauManagerDto chateauManagerDto) throws BusinessException {
        log.info("修改酒庄 name = " + chateauManagerDto.getName());
        try {
            chateauManagerDto.setUpdateTime(new Date());
            chateauManagerDao.updateChateau(chateauManagerDto);
            redisUtil.set(ManagerConstant.REDIS.CHATEAU + chateauManagerDto.getId(), JSONObject.toJSONString(chateauManagerDto));
        } catch (Exception e) {
            log.error("修改酒庄异常 name = " + chateauManagerDto.getName(), e);
            throw new BusinessException("修改酒庄异常");
        }
    }

    /**
     * 分页查询酒庄信息
     *
     * @param chateauFindParam 酒庄信息查询条件
     * @return
     * @throws BusinessException
     */
    public PageDto<ChateauManagerDto> findChateau(ChateauFindParam chateauFindParam) throws BusinessException {
        log.info("查询酒庄信息 name = " + chateauFindParam.getName() + "主营大类CategoryName = " + chateauFindParam.getCategoryName()
                + "国家名称 countryName = " + chateauFindParam.getCountryName() + "状态" + chateauFindParam.getStatus());
        try {
            PageDto<ChateauManagerDto> pageDto = new PageDto<>();
            PageBean<ChateauManagerDto, ChateauFindParam> pageBean = new PageBean<ChateauManagerDto, ChateauFindParam>(chateauFindParam) {
                @Override
                protected Long generateRowCount() throws BusinessException {
                    return chateauManagerDao.countChateau(chateauFindParam);
                }

                @Override
                protected List<ChateauManagerDto> generateBeanList() throws BusinessException {
                    return chateauManagerDao.findChateau(chateauFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<ChateauManagerDto> chateauManagerDtoList = pageBean.getPageData();
            pageDto.setTotal(total);
            pageDto.setList(chateauManagerDtoList);
            return pageDto;
        } catch (Exception e) {
            log.error("查询酒庄信息异常", e);
            throw new BusinessException("查询酒庄信息异常");
        }
    }
}
