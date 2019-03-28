
package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.dao.ChateauDao;
import com.bgd.app.dao.ProDao;
import com.bgd.app.entity.ProductDto;
import com.bgd.app.entity.ProductEvaluatDto;
import com.bgd.app.entity.param.ProParam;
import com.bgd.app.jms.JmsSend;
import com.bgd.support.base.Constants;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.entity.ProductEvaluatPo;
import com.bgd.support.entity.VipFootMarkPo;
import com.bgd.support.entity.VipInformationPo;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.RedisUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ProDao proDao;
    @Autowired
    ChateauDao chateauDao;
    @Autowired
    JmsSend jms;


    /**
     * @描述 获取商品详情 （元、活动）
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    public JSONObject getProDetail(ProParam dto, String token) throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            Map<String, Object> map = Maps.newHashMap();
            Long productId = dto.getProductId();
            map.put("productId", productId);
            ProductDto proDetail = proDao.findProDetailByMap(map);
            Integer discount = proDetail.getDiscount(); //参与活动 0否 1是

            if (discount == 1) {
                String newCapacity = proDao.findNewCapacity(productId);
                proDetail.setNewCapacity(newCapacity);
            }
            result.put("product", proDetail);
            //产品的用户评价(取三条)
            dto.setPageNo(1);
            dto.setPageSize(3);
            PageDto<ProductEvaluatDto> productEvaDtoPageDto = this.pageProductEva(dto);
            Long total = productEvaDtoPageDto.getTotal();
            List<ProductEvaluatDto> list = productEvaDtoPageDto.getList();
            result.put("evaluateTotal", total);
            result.put("evaluateList", list);
            // 足迹Mq // 从token中获取
            String user = redisUtil.get(Constants.PREFIX_APP + token);
            if (user != null) {
                VipInformationPo vipInformationPo = JSONObject.parseObject(user, VipInformationPo.class);
                Long vipId = vipInformationPo.getId();
                VipFootMarkPo foot = new VipFootMarkPo();
                foot.setVipId(vipId);
                foot.setProductId(productId);
                jms.foot(JSONObject.toJSONString(foot));
            }
            return result;
        } catch (Exception e) {
            log.error("获取商品详情异常", e);
            throw new BusinessException("获取商品详情异常");
        }


    }


    /**
     * @描述 分页获取商品评论
     * @创建人 JackMore
     * @创建时间 2019/3/19
     */
    public PageDto<ProductEvaluatDto> pageProductEva(ProParam param) throws BusinessException {
        try {
            PageDto<ProductEvaluatDto> result = new PageDto<>();
            PageBean<ProductEvaluatDto, ProParam> page = new PageBean<ProductEvaluatDto, ProParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return proDao.countProductEvaByObj(param);
                }

                @Override
                protected List<ProductEvaluatDto> generateBeanList() throws BasicException {
                    return proDao.pageProductEvaByObj(param);
                }
            }.execute();
            List<ProductEvaluatDto> pageData = page.getPageData();
            Long totalCount = page.getTotalCount();
            result.setTotal(totalCount);
            result.setList(pageData);
            return result;
        } catch (Exception e) {
            log.error("分页获取商品评论异常", e);
            throw new BusinessException("分页获取商品评论异常");
        }
    }


    /**
     * @描述
     * @创建人 JackMore
     * @创建时间 2019/3/23
     */
    public void commentProduct(ProductEvaluatPo po) throws BusinessException {
        try {
            po.setCreateTime(new Date());
            proDao.saveProEva(po);
        } catch (Exception e) {
            log.error("商品评价异常", e);
            throw new BusinessException();
        }
    }

    /**
     * @描述 筛选商品
     * @创建人 JackMore
     * @创建时间 2019/3/26
     */
    public PageDto<ProductDto> filterPro(ProParam param) {
        try {
            PageDto<ProductDto> result = new PageDto<>();
            PageBean<ProductDto, ProParam> page = new PageBean<ProductDto, ProParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return proDao.countFilterPro(param);
                }

                @Override
                protected List<ProductDto> generateBeanList() throws BasicException {
                    return proDao.pageFilterPro(param);
                }
            }.execute();
            List<ProductDto> pageData = page.getPageData();
            Long totalCount = page.getTotalCount();
            result.setTotal(totalCount);
            result.setList(pageData);
            return result;
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException();
        }
    }

}