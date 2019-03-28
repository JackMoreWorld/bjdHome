package com.bgd.app.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.dao.CartDao;
import com.bgd.app.dao.OrderDao;
import com.bgd.app.dao.ProDao;
import com.bgd.app.dao.VipDao;
import com.bgd.app.entity.*;
import com.bgd.app.entity.param.VipOrderParam;
import com.bgd.app.jms.JmsSend;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.entity.*;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdDateUtil;
import com.bgd.support.utils.BgdStringUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 订单模块 service
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-12
 */

@Service
@Slf4j
public class OrderService {

    //ReentrantLock
    private static ReentrantLock REENTRANT_LOCK = new ReentrantLock();


    @Autowired
    CartDao cartDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    AddrService addrService;
    @Autowired
    TaskExecutorService executorService;
    @Autowired
    JmsSend jmsSend;
    @Autowired
    VipDao vipDao;
    @Autowired
    ProDao proDao;


    /**
     * 获取确认订单数据
     *
     * @param proInfo 商品信息  （例如：{"id":1,"count":1,"actId":1}）
     * @param cartIds 购物车id集合 (逗号分割)
     * @return 确认订单数据
     * @throws BusinessException
     */
    public JSONObject confirmOrder(String cartIds, String proInfo, Long vipId) throws BusinessException {
        JSONObject jsonObject = new JSONObject();
        try {
            String[] cartIdArray;
            //获取用户的收货地址
            VipAddrPo vipAddrPo = addrService.getVipAddr(vipId);
            //查询购物车里的商品信息
            List<CartProductDto> proList = new ArrayList<>();
            if (BgdStringUtil.isStringNotEmpty(cartIds)) {
                cartIdArray = cartIds.split(",");
                proList = cartDao.queryProList(cartIdArray);
                for (CartProductDto cartDto : proList) {
                    String newCapacity = proDao.findNewCapacity(cartDto.getProductId());
                    List<ProductCapacityPo> capacityList = JSONObject.parseArray(cartDto.getCapacity(),
                            ProductCapacityPo.class);
                    if (null != newCapacity) {
                        cartDto.setCapacity(newCapacity);
                    }
                    cartDto.setPrice(capacityList.get(0) == null ? null :
                            capacityList.get(0).getPrice());
                }
            } else {
                JSONObject object = JSONObject.parseObject(proInfo);
                if (null != object) {
                    proList = cartDao.queryProResult(object.getInteger("id"));
                    for (CartProductDto dto : proList) {
                        long actId = object.getLongValue("actId");
                        if (0L != actId) {
                            dto.setActId(actId);
                        }
                        String newCapacity = proDao.findNewCapacity(dto.getProductId());
                        List<ProductCapacityPo> capacityList = JSONObject.parseArray(dto.getCapacity(),
                                ProductCapacityPo.class);
                        if (null != newCapacity) {
                            dto.setCapacity(newCapacity);
                        } else {
                            dto.setCapacity(dto.getCapacity());
                        }
                        dto.setCount(object.getInteger("count"));
                        dto.setPrice(capacityList.get(0) == null ? null :
                                capacityList.get(0).getPrice());
                    }
                }
            }
            jsonObject.put("address", vipAddrPo);
            jsonObject.put("proList", proList);
            jsonObject.put("cartIds", cartIds);
            return jsonObject;
        } catch (Exception e) {
            throw new BusinessException("获取确认订单数据异常");
        }
    }

    /**
     * 下单
     *
     * @param orderDto
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addOrder(MallOrderDto orderDto) throws BusinessException {
        JSONObject jsonObject = new JSONObject();
        try {
            if (REENTRANT_LOCK.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    String[] cartIdArray = {};
                    if (BgdStringUtil.isStringNotEmpty(orderDto.getCartIds())) {
                        cartIdArray = orderDto.getCartIds().split(",");
                    }
                    List<MallOrderDetailPo> proList = JSONArray.parseArray(orderDto.getProductJsonStr(), MallOrderDetailPo.class);
                    StringBuilder sb = new StringBuilder("JZJ");
                    //生产随机订单号
                    sb.append(BgdDateUtil.formatSdf(new Date(), "yyyyMMddHHmmss"));
                    orderDto.setOrderNo(sb.toString());
                    orderDto.setDeliveryType(0);
                    orderDto.setFreight(new BigDecimal(10));
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    for (MallOrderDetailPo detailPo : proList) {
                        totalPrice = totalPrice.add(detailPo.getTotalPrice());
                    }
                    orderDto.setPrice(totalPrice);
                    orderDto.setStatus(AppConstant.PAY_STATUS.DFK);
                    //插入订单主数据
                    orderDao.insertOrder(orderDto);
                    for (MallOrderDetailPo detailPo : proList) {
                        detailPo.setOrderId(orderDto.getId());
                        //订单状态
                        detailPo.setStatus(AppConstant.ORDER_STATUS.DFK);
                        detailPo.setVipId(orderDto.getVipId());
                    }
                    //插入订单明细数据
                    orderDao.insertOrderDetail(proList);
                    jsonObject.put("price", totalPrice);
                    jsonObject.put("orderNo", orderDto.getOrderNo());
                    if (BgdStringUtil.isStringNotEmpty(orderDto.getCartIds())) {
                        //删除购物车对应数据
                        cartDao.batchDelCart(cartIdArray);
                    }
                    if (BgdStringUtil.isStringNotEmpty(orderDto.getCouponsId().toString())) {
                        //更改优惠券的使用状态
                        orderDao.updCouponStatus(orderDto.getCouponsId(), orderDto.getVipId());
                    }
                    //异步操作(下单超过24h未付款自动取消)
                    executorService.orderAsyncHandler(orderDto);
                    return jsonObject;
                } finally {
                    //释放锁
                    REENTRANT_LOCK.unlock();
                }
            } else {
                throw new BusinessException("系统繁忙，请重新下单操作");
            }
        } catch (Exception e) {
            throw new BusinessException("下单异常");
        }
    }

    /**
     * 查询会员订单列表分页数据
     *
     * @param vipOrderParam 会员订单dto
     * @return 订单列表分页数据
     * @throws BusinessException 异常
     */
    public PageDto<String> queryOrderListByPage(VipOrderParam vipOrderParam) throws BusinessException {
        try {
            PageDto<String> result = new PageDto<>();
            PageBean<MallOrderDetailDto, VipOrderParam> pageBean = new PageBean<MallOrderDetailDto, VipOrderParam>(vipOrderParam) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    List<Map<String, Object>> mapList = orderDao.getOrderCount(vipOrderParam);
                    long count = 0L;
                    List<String> orderIdList = Lists.newArrayList();
                    if (mapList.size() > 0) {
                        for (Map<String, Object> mp : mapList) {
                            long ct = Long.valueOf(mp.get("count").toString());
                            String orderId = mp.get("orderid").toString();
                            count = count + ct;
                            orderIdList.add(orderId);
                        }
                        vipOrderParam.setOrderIds(orderIdList);
                    }
                    return count;
                }

                @Override
                protected List<MallOrderDetailDto> generateBeanList() throws BasicException {
                    return orderDao.getOrderListByPage(vipOrderParam);
                }
            }.execute();
            Long total = pageBean.getTotalCount();
            List<MallOrderDetailDto> dtoList = pageBean.getPageData();
            List<String> jsonList = Lists.newArrayList();
            Map<String, List<MallOrderDetailDto>> map = new HashMap<>();
            for (MallOrderDetailDto detailDto : dtoList) {
                if (map.containsKey(detailDto.getOrderId().toString()) &&
                        detailDto.getStatus().equals(AppConstant.ORDER_STATUS.DFK)) {//map中存在此id，将数据存放当前key的map中
                    map.get(detailDto.getId().toString()).add(detailDto);
                } else {//map中不存在，新建key，用来存放数据
                    List<MallOrderDetailDto> tmpList = new ArrayList<>();
                    tmpList.add(detailDto);
                    if (!detailDto.getStatus().equals(AppConstant.ORDER_STATUS.DFK)) {
                        map.put(detailDto.getOrderId() + BgdDateUtil.formatSdf(detailDto.getPayTime(), "yyyy-MM-dd HH:mm:ss"), tmpList);
                    } else {
                        map.put(detailDto.getOrderId().toString(), tmpList);
                    }
                }
            }
            for (Map.Entry<String, List<MallOrderDetailDto>> m : map.entrySet()) {
                List<MallOrderDetailDto> list = m.getValue();
                jsonList.add(JSONObject.toJSONString(list));
            }
            result.setTotal(total);
            result.setList(jsonList);
            return result;
        } catch (Exception e) {
            throw new BusinessException("查询会员订单列表分页数据异常");
        }
    }

    /**
     * 取消订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderNo, String vipId) {
        try {
            VipOrderDto vipOrderDto = orderDao.getOrderInfo(orderNo, vipId);
            if (null != vipOrderDto) {
                //取消订单信息
                orderDao.delOrder(vipOrderDto.getId().toString(), vipId);
                orderDao.delOrderDetail(vipOrderDto.getId().toString(), vipId);
                log.info("取消订单成功");
            }
        } catch (Exception e) {
            log.info("取消订单异常", e);
        }
    }

    /**
     * 支付成功后订单相关业务处理
     *
     * @param orderNo 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderBusinessHandler(String orderNo, String transactionId) throws BusinessException {
        try {
            //查询订单信息
            VipOrderDto vipOrderDto = orderDao.getOrderInfo(orderNo, null);
            if (null != vipOrderDto && AppConstant.PAY_STATUS.DFK.equals(vipOrderDto.getStatus())) {

                MallOrderPo mallOrderPo = new MallOrderPo();
                //支付类型
                mallOrderPo.setPayType(AppConstant.PAY_TYPE.WeCHAT_PAY);
                //付款状态
                mallOrderPo.setStatus(AppConstant.PAY_STATUS.YFH);
                mallOrderPo.setOrderNo(orderNo);
                //更新订单信息
                orderDao.updOrderInfo(mallOrderPo);

                MallOrderDetailPo orderDetailPo = new MallOrderDetailPo();
                orderDetailPo.setOrderId(vipOrderDto.getId());
                orderDetailPo.setStatus(AppConstant.ORDER_STATUS.DFH);
                //更新订单明细信息
                orderDao.updOrderDetail(orderDetailPo);
                //插入资金流水
                MallCapitalFlowDto capitalFlowDto = new MallCapitalFlowDto();
                //会员id
                capitalFlowDto.setVipId(vipOrderDto.getVipId());
                //用户类型
                capitalFlowDto.setUserType(AppConstant.USER_TYPE.VIP);
                //关联id（订单id）
                capitalFlowDto.setRelationId(vipOrderDto.getId());
                //资金类型
                capitalFlowDto.setDir(AppConstant.CAPITAL_TYPE.OUT);
                //交易金额
                capitalFlowDto.setTradeMoney(vipOrderDto.getPrice().
                        subtract(BigDecimal.valueOf(vipOrderDto.getDiscountPrice())));
                //资金来源（0 订单 1 充值  2 退款）
                capitalFlowDto.setTradeSource(AppConstant.CAPITAL_SOURCE.ORDER);
                //第三方交易流水号
                capitalFlowDto.setThirdTradeNo(transactionId);
                //支付类型
                capitalFlowDto.setPayType(AppConstant.PAY_TYPE.WeCHAT_PAY);
                //备注
                capitalFlowDto.setRemark("订单支付");
                //插入资金流水数据
                orderDao.insertCapitalFlow(capitalFlowDto);

                //积分变更操作
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("vipId", vipOrderDto.getVipId());
                jsonObject.put("points", vipOrderDto.getPrice().intValue());
                jmsSend.addIntegralByOrder(JSONObject.toJSONString(jsonObject));
            }
        } catch (Exception e) {
            throw new BusinessException("支付成功后订单相关业务处理异常");
        }
    }

    /**
     * 查询订单详情
     *
     * @param detailId 订单详情id
     * @param vipId    用户id
     * @return 订单详情
     */
    public MallOrderDetailDto getOrderDetail(String detailId, String vipId) throws BusinessException {
        try {
            return orderDao.getOrderDetail(detailId, vipId);
        } catch (Exception e) {
            throw new BusinessException("查询订单详情异常");
        }
    }

    /**
     * 订单确认收货
     *
     * @param vipId    用户id
     * @param detailId 订单详情id
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmReceipt(String detailId, String vipId) throws BusinessException {
        try {
            //订单确认收货
            orderDao.confirmReceipt(detailId, vipId);
        } catch (Exception e) {
            throw new BusinessException("订单确认收货异常");
        }
    }

    /**
     * @描述 修改商品评分
     * @创建人 JackMore
     * @创建时间 2019/3/18
     */
    @Transactional(rollbackFor = Exception.class)
    public void dealProductStar(Long detailId, Long star) {
        try {
            if (star != 0) {
                ProductInformationPo product = orderDao.findProductByOrderId(detailId);
                MallOrderStarPo po = new MallOrderStarPo();
                po.setCountryId(product.getCountryId());
                po.setChateauId(product.getChateauId());
                po.setProductId(product.getId());
                po.setStar(star);
                orderDao.saveOrUpdateOrderStar(po);
            }
        } catch (Exception e) {
            log.error("修改商品评价分数异常", e);
            throw new BusinessException();
        }
    }


    /**
     * 申请退款
     *
     * @param detailId 订单详情id
     * @param vipId    会员id
     */
    @Transactional(rollbackFor = Exception.class)
    public void applyRefund(String detailId, Long vipId) throws BusinessException {
        try {
            MallOrderDetailPo orderDetailPo = new MallOrderDetailPo();
            orderDetailPo.setOrderId(Long.valueOf(detailId));
            orderDetailPo.setStatus(AppConstant.ORDER_STATUS.DTK);
            //修改订单状态
            orderDao.updOrderDetails(orderDetailPo);
            MallOrderDetailDto mallOrderDetailDto = orderDao.getOrderDetail(detailId, vipId.toString());
            MallOrderRefundDto orderRefundDto = new MallOrderRefundDto();
            orderRefundDto.setVipId(vipId);
            orderRefundDto.setDetailId(Long.valueOf(detailId));
            orderRefundDto.setStatus(0);
            //申请退款前的订单状态
            orderRefundDto.setOrderStatus(mallOrderDetailDto.getStatus());
            StringBuilder sb = new StringBuilder("TKD");
            //生产退款单号
            sb.append(BgdDateUtil.formatSdf(new Date(), "yyyyMMddHHmmss"));
            orderRefundDto.setRefundNo(sb.toString());
            //插入订单退款申请记录
            orderDao.insertRefund(orderRefundDto);
        } catch (Exception e) {
            throw new BusinessException("申请退款异常");
        }
    }

    /**
     * 订单商品评价
     *
     * @param vipId    用户id
     * @param remark   评论内容
     * @param detailId 订单详情id
     * @param star     评分等级 0差评 5中评 10好评
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderEvaluate(String detailId, String remark, Long star, Long vipId)
            throws BusinessException {
        try {
            //修改订单状态
            orderDao.orderEvaluate(detailId, AppConstant.ORDER_STATUS.YWC.toString());
            //插入订单评价数据
            orderDao.insertOrderEvaluate(detailId, remark, star, vipId);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("detailId", detailId);
            jsonObject.put("star", star);
            //评价积分处理
            jmsSend.addStarByOrder(JSONObject.toJSONString(jsonObject));
        } catch (Exception e) {
            throw new BusinessException("订单商品评价异常");
        }
    }


    /**
     * 余额支付订单处理
     *
     * @param vipId    用户id
     * @param orderNo  订单号
     * @param payPrice 支付金额
     */
    @Transactional(rollbackFor = Exception.class)
    public void balancePay(Long vipId, String orderNo, String payPrice) throws BusinessException {
        //查询订单信息
        VipOrderDto vipOrderDto = orderDao.getOrderInfo(orderNo, vipId.toString());
        if (null != vipOrderDto) {
            if (AppConstant.PAY_STATUS.DFK.equals(vipOrderDto.getStatus())) {
                //查询用户余额
                VipMallInfoPo vipMallInfo = vipDao.findVipMallInfo(vipId);
                if (null != vipMallInfo && vipMallInfo.getBalance().compareTo(new BigDecimal(payPrice)) == 1) {
                    MallOrderPo mallOrderPo = new MallOrderPo();
                    //支付类型
                    mallOrderPo.setPayType(AppConstant.PAY_TYPE.BALANCE_PAY);
                    //付款状态
                    mallOrderPo.setStatus(AppConstant.PAY_STATUS.YFH);
                    mallOrderPo.setOrderNo(orderNo);
                    //更新订单信息
                    orderDao.updOrderInfo(mallOrderPo);

                    MallOrderDetailPo orderDetailPo = new MallOrderDetailPo();
                    orderDetailPo.setOrderId(vipOrderDto.getId());
                    orderDetailPo.setStatus(AppConstant.ORDER_STATUS.DFH);
                    //更新订单明细信息
                    orderDao.updOrderStatus(orderDetailPo);

                    VipMallInfoPo po = new VipMallInfoPo();
                    po.setId(vipMallInfo.getId());
                    po.setBalance(vipMallInfo.getBalance().subtract(vipOrderDto.getPrice()).
                            subtract(BigDecimal.valueOf(vipOrderDto.getDiscountPrice())));
                    //更新用户余额
                    vipDao.updateVipMallInfo(po);
                    //插入资金流水
                    MallCapitalFlowDto capitalFlowDto = new MallCapitalFlowDto();
                    //会员id
                    capitalFlowDto.setVipId(vipOrderDto.getVipId());
                    //用户类型
                    capitalFlowDto.setUserType(AppConstant.USER_TYPE.VIP);
                    //关联id（订单id）
                    capitalFlowDto.setRelationId(vipOrderDto.getId());
                    //资金类型
                    capitalFlowDto.setDir(AppConstant.CAPITAL_TYPE.OUT);
                    //交易金额
                    capitalFlowDto.setTradeMoney(vipOrderDto.getPrice().
                            subtract(BigDecimal.valueOf(vipOrderDto.getDiscountPrice())));
                    //资金来源（0 订单 1 充值  2 退款）
                    capitalFlowDto.setTradeSource(AppConstant.CAPITAL_SOURCE.ORDER);
                    //第三方交易流水号
                    capitalFlowDto.setThirdTradeNo("余额支付");
                    //支付类型
                    capitalFlowDto.setPayType(AppConstant.PAY_TYPE.WeCHAT_PAY);
                    //备注
                    capitalFlowDto.setRemark("订单余额支付");
                    //插入资金流水数据
                    orderDao.insertCapitalFlow(capitalFlowDto);
                } else {
                    throw new BusinessException("余额不足，请充值");
                }
            } else {
                throw new BusinessException("订单已付款");
            }
        } else {
            throw new BusinessException("订单信息不存在");
        }
    }

    /**
     * 删除订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void delOrder(String detailId, String vipId) {
        try {
            //删除订单信息
            orderDao.delOrderDetailById(detailId, vipId);
            log.info("删除订单成功");
        } catch (Exception e) {
            log.info("删除订单异常", e);
        }
    }

}
