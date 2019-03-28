package com.bgd.app.service;


import com.alibaba.fastjson.JSONObject;
import com.bgd.app.dao.CartDao;
import com.bgd.app.dao.ProDao;
import com.bgd.app.entity.VipCartDto;
import com.bgd.app.entity.param.CartRequestParam;
import com.bgd.app.jms.JmsSend;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.entity.ProductCapacityPo;
import com.bgd.support.entity.VipCartPo;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdDateUtil;
import com.bgd.support.utils.BgdStringUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 购物车模块 service
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-11
 */
@Service
@Slf4j
public class CartService {


    @Autowired
    CartDao cartDao;
    @Autowired
    JmsSend jmsSend;
    @Autowired
    ProDao proDao;


    /**
     * 查询购物车列表分页数据
     *
     * @param requestParamsDto 购物车请求 dto
     * @return 购物车列表分页数据
     */
    public PageDto<VipCartDto> queryCartListByPage(CartRequestParam requestParamsDto) throws BusinessException {
        try {
            PageDto<VipCartDto> result = new PageDto<>();
            PageBean<VipCartDto, CartRequestParam> pageBean = new PageBean<VipCartDto, CartRequestParam>(requestParamsDto) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return cartDao.getCartListCount(requestParamsDto.getVipId());
                }

                @Override
                protected List<VipCartDto> generateBeanList() throws BasicException {
                    //取出用户购物车列表里的数据
                    List<VipCartDto> list = cartDao.getCartList(requestParamsDto);
                    for (VipCartDto cartDto : list) {
                        //先查询是否有活动价格
                        String newCapacity = proDao.findNewCapacity(cartDto.getProductId());
                        //商品原价
                        List<ProductCapacityPo> capacityList = JSONObject.parseArray(cartDto.getCapacity(),
                                ProductCapacityPo.class);
                        if (null != newCapacity) {
                            cartDto.setCapacity(newCapacity);
                        }
                        cartDto.setContent(capacityList.get(0) == null ? null :
                                capacityList.get(0).getContent());
                        if (null != cartDto.getActEndTime()) {
                            cartDto.setRemainingTime(BgdDateUtil.ConvertToDate(cartDto.getActEndTime()).getTime() / 1000);
                        }
                    }
                    return list;
                }
            }.execute();
            Long total = pageBean.getTotalCount();
            List<VipCartDto> pageBeanBeanList = pageBean.getPageData();
            result.setList(pageBeanBeanList);
            result.setTotal(total);
            return result;
        } catch (Exception e) {
            throw new BusinessException("查询购物车列表分页数据系统异常");
        }
    }

    /**
     * 根据购物车id删除购物车信息
     *
     * @param vipId   会员id
     * @param cartIds 购物车id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void delCartById(Long vipId, String cartIds) throws BusinessException {
        try {
            String[] cartIdArray = cartIds.split(",");
            //根据购物车id删除购物车信息
            cartDao.batchDelCart(cartIdArray);
            List<Map<String, String>> list = cartDao.getProListByCartIdArray(cartIdArray);
            List<Map<String, String>> newList = Lists.newArrayList();
            for (Map<String, String> map : list) {
                Object actId = map.get("actId");
                if (BgdStringUtil.isStringNotEmpty(actId.toString())) {
                    newList.add(map);
                }
            }
            if (newList.size() > 0) {
                //删除购物车活动商品库存处理
                jmsSend.cartStockHandler(JSONObject.toJSONString(newList));
            }
        } catch (Exception e) {
            throw new BusinessException("根据购物车id删除购物车异常");
        }
    }


    /**
     * 加入购物车
     *
     * @param vipCartPo 购物车 po
     */
    @Transactional(rollbackFor = Exception.class)
    public void addCart(VipCartPo vipCartPo) throws BusinessException {
        try {
            //加入购物车
            cartDao.addCart(vipCartPo);
        } catch (Exception e) {
            throw new BusinessException("加入购物车异常");
        }
    }

    /**
     * 购物车商品购买数量更新
     *
     * @param vipId  用户id
     * @param cartId 购物车id
     * @param type   0 加 1减
     * @return 购物车商品购物数量
     */
    @Transactional(rollbackFor = Exception.class)
    public Long updCartProCount(long cartId, long type, long vipId) throws BusinessException {
        try {
            //购物车商品购买数量更新
            if (0 == type) {
                cartDao.addCartProCount(cartId, type, vipId);
            } else {
                cartDao.subCartProCount(cartId, type, vipId);
            }
            return cartDao.getCartProCount(cartId, vipId);
        } catch (Exception e) {
            throw new BusinessException("购物车商品购买数量更新异常");
        }
    }

}
