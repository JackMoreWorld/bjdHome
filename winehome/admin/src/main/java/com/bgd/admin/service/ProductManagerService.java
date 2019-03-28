package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.ProImgManagerDao;
import com.bgd.admin.dao.ProductManagerDao;
import com.bgd.admin.entity.ProImgManagerDto;
import com.bgd.admin.entity.ProductManagerDto;
import com.bgd.admin.entity.param.ProductFindParam;
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
 * 商品管理 Service
 *
 * @author Sunxk
 * @since 2019-3-6
 */
@Slf4j
@Service
public class ProductManagerService {

    @Autowired
    ProductManagerDao productManagerDao;

    @Autowired
    ProImgManagerDao proImgManagerDao;

    @Autowired
    ProImgManagerService proImgManagerService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 新增商品信息
     *
     * @param productDto
     * @throws BusinessException
     */
    public void addOrUpdateProduct(ProductManagerDto productDto) throws BusinessException {
        log.info("增改商品信息name = " + productDto.getName());
        try {
            if (BgdStringUtil.isStringEmpty(productDto.getId().toString())) {
                //id为空执行新增
                productDto.setCreateTime(new Date());
                //插入商品信息并返回ID
                productManagerDao.addProduct(productDto);
                if (BgdStringUtil.isStringEmpty(productDto.getId().toString())) {
                    throw new BusinessException("新增商品异常");
                }
                List<ProImgManagerDto> proImgList = productDto.getImgS();
                //将商品ID插入商品图片列表
                if (!proImgList.isEmpty()) {
                    for (ProImgManagerDto proImg : productDto.getImgS()) {
                        proImg.setProductId(productDto.getId());
                    }
                    //插入对应图片信息
                    proImgManagerDao.addProImg(proImgList);
                }
                redisUtil.set(ManagerConstant.REDIS.PRODUCT + productDto.getId(), JSONObject.toJSONString(productDto));
            } else {
                //id不为空执行修改
                productDto.setUpdateTime(new Date());
                productManagerDao.updateProduct(productDto);
                if (productDto.getImgS().size() != 0) {
                    proImgManagerService.addProImg(productDto.getImgS());
                }
                redisUtil.set(ManagerConstant.REDIS.PRODUCT + productDto.getId(), JSONObject.toJSONString(productDto));
            }
        } catch (Exception e) {
            log.error("增改商品信息异常", e);
            throw new BusinessException("增改商品信息异常");
        }
    }

    /**
     * 删除商品信息
     *
     * @param productId 商品ID
     * @throws BusinessException
     */
    public void deleteProduct(Long productId) throws BusinessException {
        log.info("删除商品id = " + productId);
        try {
            productManagerDao.deleteProduct(productId);
            proImgManagerDao.deleteProImg(productId);
            redisUtil.remove(ManagerConstant.REDIS.PRODUCT + productId);
        } catch (Exception e) {
            log.error("删除商品异常 id = " + productId, e);
            throw new BusinessException("删除商品异常");
        }
    }

    /**
     * 分页查询商品信息
     *
     * @param productFindParam 商品信息查询条件
     * @return
     * @throws BusinessException
     */
    public PageDto<ProductManagerDto> findProduct(ProductFindParam productFindParam) throws BusinessException {
        log.info("查询商品信息 name = " + productFindParam.getName()
                + "主营大类CategoryName = " + productFindParam.getCategoryName()
                + "国家名称 countryName = " + productFindParam.getCountryName()
                + "酒庄名称chateauName" + productFindParam.getChateauName());
        try {
            PageDto<ProductManagerDto> pageDto = new PageDto<>();
            PageBean<ProductManagerDto, ProductFindParam> pageBean = new PageBean<ProductManagerDto, ProductFindParam>(productFindParam) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return productManagerDao.countProduct(productFindParam);
                }

                @Override
                protected List<ProductManagerDto> generateBeanList() throws BasicException {
                    return productManagerDao.findProduct(productFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<ProductManagerDto> pageBeanList = pageBean.getPageData();
            pageDto.setTotal(total);
            pageDto.setList(pageBeanList);
            return pageDto;
        } catch (Exception e) {
            log.error("查询商品信息异常 name = " + productFindParam.getName()
                    + "主营大类CategoryName = " + productFindParam.getCategoryName()
                    + "国家名称 countryName = " + productFindParam.getCountryName()
                    + "酒庄名称chateauName" + productFindParam.getChateauName(), e);
            throw new BusinessException("查询商品异常");
        }
    }

    /**
     * 查询商品及其图片信息
     *
     * @param productId 商品ID
     */
    public ProductManagerDto findProAndImg(Long productId) throws BusinessException {
        log.info("查询商品及其图片信息");
        try {
            return productManagerDao.findOneProduct(productId);
        } catch (Exception e) {
            log.error("查询商品及其图片信息异常", e);
            throw new BusinessException("查询商品及其图片信息异常");
        }
    }
}
