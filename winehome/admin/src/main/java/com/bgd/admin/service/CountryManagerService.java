package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.CountryManagerDao;
import com.bgd.admin.entity.CountryManagerDto;
import com.bgd.admin.entity.param.CountryFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdStringUtil;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 国家信息管理 Service
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Service
@Slf4j
public class CountryManagerService {


    @Autowired
    CountryManagerDao countryDao;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 新增国家信息
     *
     * @param countryManagerDto 国家信息Dto
     * @throws BusinessException
     */
    public void addCountry(CountryManagerDto countryManagerDto) throws BusinessException {
        log.info("新增国家 name = " + countryManagerDto.getName());
        try {
            countryManagerDto.setCreateTime(new Date());
            countryDao.addCountry(countryManagerDto);
            redisUtil.set(ManagerConstant.REDIS.COUNTRY + countryManagerDto.getId(), JSONObject.toJSONString(countryManagerDto));
        } catch (Exception e) {
            log.error("新增国家异常 name = " + countryManagerDto.getName(), e);
            throw new BusinessException("新增国家异常");
        }
    }

    /**
     * 删除国家信息
     *
     * @param countryId
     * @throws BusinessException
     */
    public void deleteCountry(Long countryId) throws BusinessException {
        log.info("删除国家 countryId = " + countryId);
        try {
            if (BgdStringUtil.isStringEmpty(countryId.toString())) {
                throw new BusinessException("删除国家信息ID不能为空");
            }
            countryDao.deleteCountry(countryId);
            redisUtil.remove(ManagerConstant.REDIS.COUNTRY + countryId);
        } catch (Exception e) {
            log.error("删除国家异常 countryId = " + countryId, e);
            throw new BusinessException("删除国家异常");
        }
    }

    /**
     * 修改国家信息
     *
     * @param countryManagerDto 国家信息Dto
     * @throws BusinessException
     */
    public void updateCountry(CountryManagerDto countryManagerDto) throws BusinessException {
        log.info("修改国家 name = " + countryManagerDto.getName());
        try {
            countryManagerDto.setUpdateTime(new Date());
            countryDao.updateCountry(countryManagerDto);
            redisUtil.set(ManagerConstant.REDIS.COUNTRY + countryManagerDto.getId(), JSONObject.toJSONString(countryManagerDto));
        } catch (Exception e) {
            log.error("修改国家异常 name = " + countryManagerDto.getName(), e);
            throw new BusinessException();
        }

    }

    /**
     * 分页查询国家信息
     *
     * @param countryFindParam 国家信息Dto
     * @return
     * @throws BusinessException
     */
    public PageDto<CountryManagerDto> findCountry(CountryFindParam countryFindParam) throws BusinessException {
        log.info("查询国家 name = " + countryFindParam.getName());
        try {
            PageDto<CountryManagerDto> pageDto = new PageDto<>();
            PageBean<CountryManagerDto, CountryFindParam> pageBean = new PageBean<CountryManagerDto, CountryFindParam>(countryFindParam) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return countryDao.countCountry(countryFindParam);
                }

                @Override
                protected List<CountryManagerDto> generateBeanList() throws BasicException {
                    return countryDao.findCountry(countryFindParam);
                }
            }.execute();
            List<CountryManagerDto> countryManagerDtoList = pageBean.getPageData();
            long total = pageBean.getTotalCount();
            pageDto.setTotal(total);
            pageDto.setList(countryManagerDtoList);
            return pageDto;
        } catch (Exception e) {
            log.error("查询国家异常 name = " + countryFindParam, e);
            throw new BusinessException("查询国家异常");
        }

    }
}
