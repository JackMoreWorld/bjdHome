package com.bgd.admin.service;

import com.bgd.admin.dao.ProImgManagerDao;
import com.bgd.admin.entity.ProImgManagerDto;
import com.bgd.support.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品图片信息管理 Service
 *
 * @author sunxk
 * @since 2019-3-25
 */
@Slf4j
@Service
public class ProImgManagerService {

    @Autowired
    ProImgManagerDao proImgManagerDao;

    /**
     * 新增商品图片信息
     *
     * @param list 商品图片信息列表
     */
    public void addProImg(List<ProImgManagerDto> list) throws BusinessException {
        log.info("新增商品图片信息");
        try {
            Long productId = list.get(0).getProductId();
            proImgManagerDao.deleteProImg(productId);
            proImgManagerDao.addProImg(list);
        } catch (Exception e) {
            log.error("新增商品图片信息异常", e);
            throw new BusinessException("新增商品图片信息异常");
        }
    }

    /**
     * 查询商品信息
     *
     * @param productId 商品ID
     */
    public List<ProImgManagerDto> findProImg(Long productId) throws BusinessException {
        log.info("查询商品信息");
        try {
            return proImgManagerDao.findProImg(productId);
        } catch (Exception e) {
            log.error("查询商品信息失败", e);
            throw new BusinessException("查询商品信息失败");
        }
    }

}
