
package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.dao.ChateauDao;
import com.bgd.app.dao.ProDao;
import com.bgd.app.entity.MallChateauDto;
import com.bgd.app.entity.param.ChateauParam;
import com.bgd.app.entity.param.ProParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.PageParam;
import com.bgd.support.entity.MallCountryPo;
import com.bgd.support.entity.ProductInformationPo;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChateauService {


    @Autowired
    ChateauDao chateauDao;
    @Autowired
    ProDao proDao;


    /**
     * @描述 国家信息
     * @创建人 JackMore
     * @创建时间 2019/3/5
     */
    public PageDto<MallCountryPo> listCountryS(PageParam param) throws BusinessException {
        try {
            PageDto<MallCountryPo> result = new PageDto<>();
            PageBean<MallCountryPo, PageParam> page = new PageBean<MallCountryPo, PageParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return chateauDao.countCountry();
                }

                @Override
                protected List<MallCountryPo> generateBeanList() throws BasicException {
                    return chateauDao.pageCountry(param);
                }
            }.execute();
            List<MallCountryPo> pageData = page.getPageData();
            Long totalCount = page.getTotalCount();
            result.setTotal(totalCount);
            result.setList(pageData);
            return result;
        } catch (Exception e) {
            log.error("查询国家列表异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 大搜索获取酒庄列表
     * @创建人 JackMore
     * @创建时间 2019/3/5
     */
    public PageDto<MallChateauDto> listChateauBySearch(ChateauParam param) throws BusinessException {
        try {
            PageDto<MallChateauDto> result = new PageDto<>();
            //排序规则 0综合 1销量  2评价
            Integer sortTpe = param.getSortTpe();
            sortTpe = sortTpe == null ? 1 : sortTpe; // 默认以销量
            if (AppConstant.SORT_TYPE.销量.equals(sortTpe)) { // 销量
                result = pageChateauByRank(param);
            } else if (AppConstant.SORT_TYPE.信用.equals(sortTpe)) {// 信用
                result = pageChateauByStar(param);
            } else {
                result = pageChateauByRankAndStar(param);
            }
            return result;
        } catch (Exception e) {
            log.error("大搜索酒庄列表异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 根据销量排行酒庄
     * @创建人 JackMore
     * @创建时间 2019/3/18
     */
    public PageDto<MallChateauDto> pageChateauByRank(ChateauParam param) {
        PageDto<MallChateauDto> result = new PageDto<>();
        PageBean<MallChateauDto, ChateauParam> page = new PageBean<MallChateauDto, ChateauParam>(param) {
            @Override
            protected Long generateRowCount() throws BasicException {
                return chateauDao.countChateauByRank(param);
            }

            @Override
            protected List<MallChateauDto> generateBeanList() throws BasicException {
                return chateauDao.pageChateauByRank(param);
            }
        }.execute();
        List<MallChateauDto> pageData = page.getPageData();
        Long totalCount = page.getTotalCount();
        result.setTotal(totalCount);
        result.setList(pageData);
        return result;
    }


    /**
     * @描述 根据销量排行酒庄
     * @创建人 JackMore
     * @创建时间 2019/3/18
     */
    public PageDto<MallChateauDto> pageChateauByStar(ChateauParam param) {
        PageDto<MallChateauDto> result = new PageDto<>();
        PageBean<MallChateauDto, ChateauParam> page = new PageBean<MallChateauDto, ChateauParam>(param) {
            @Override
            protected Long generateRowCount() throws BasicException {
                return chateauDao.countChateauByStar(param);
            }

            @Override
            protected List<MallChateauDto> generateBeanList() throws BasicException {
                return chateauDao.pageChateauByStar(param);
            }
        }.execute();
        List<MallChateauDto> pageData = page.getPageData();
        Long totalCount = page.getTotalCount();
        result.setTotal(totalCount);
        result.setList(pageData);
        return result;
    }


    /**
     * @描述 根据销量排行酒庄
     * @创建人 JackMore
     * @创建时间 2019/3/18
     */
    public PageDto<MallChateauDto> pageChateauByRankAndStar(ChateauParam param) {
        PageDto<MallChateauDto> result = new PageDto<>();
        PageBean<MallChateauDto, ChateauParam> page = new PageBean<MallChateauDto, ChateauParam>(param) {
            @Override
            protected Long generateRowCount() throws BasicException {
                return chateauDao.countChateauByRankAndStar(param);
            }

            @Override
            protected List<MallChateauDto> generateBeanList() throws BasicException {
                return chateauDao.pageChateauByRankAndStar(param);
            }
        }.execute();
        List<MallChateauDto> pageData = page.getPageData();
        Long totalCount = page.getTotalCount();
        result.setTotal(totalCount);
        result.setList(pageData);
        return result;
    }


    /**
     * @描述 获取酒庄详情
     * @创建人 JackMore
     * @创建时间 2019/3/26
     */
    public JSONObject getChateauDetail(ChateauParam param) {
        JSONObject result = new JSONObject();
        try {
            ProParam dto = new ProParam();
            MallChateauDto chateauOne = chateauDao.findChateauOne(param);
            Long proCount = proDao.countProByChateauId(chateauOne.getId());
            dto.setPageNo(1);
            dto.setPageSize(3);
            dto.setChateauId(chateauOne.getId());
            PageDto<ProductInformationPo> productInformationPoPageDto = this.pageProduct(dto);
            List<ProductInformationPo> proList = productInformationPoPageDto.getList();
            result.put("chateau", chateauOne);
            result.put("proCount", proCount);
            result.put("chateauProList", proList);
            return result;
        } catch (Exception e) {
            log.error("获取酒庄详情异常", e);
            throw new BusinessException();
        }
    }

    /**
     * @描述 分页获取商品
     * @创建人 JackMore
     * @创建时间 2019/3/19
     */
    public PageDto<ProductInformationPo> pageProduct(ProParam param) throws BusinessException {
        try {
            PageDto<ProductInformationPo> result = new PageDto<>();
            PageBean<ProductInformationPo, PageParam> page = new PageBean<ProductInformationPo, PageParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return proDao.countProByObj(param);
                }

                @Override
                protected List<ProductInformationPo> generateBeanList() throws BasicException {
                    return proDao.pageProByObj(param);
                }
            }.execute();
            List<ProductInformationPo> pageData = page.getPageData();
            Long totalCount = page.getTotalCount();
            result.setTotal(totalCount);
            result.setList(pageData);
            return result;
        } catch (Exception e) {
            log.error("分页获取商品异常", e);
            throw new BusinessException("分页获取商品异常");
        }
    }
}