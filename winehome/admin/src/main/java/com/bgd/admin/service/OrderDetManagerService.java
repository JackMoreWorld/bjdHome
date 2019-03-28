package com.bgd.admin.service;

import com.bgd.admin.dao.OrderDetManagerDao;
import com.bgd.admin.entity.OrderDetManagerDto;
import com.bgd.admin.entity.param.OrderDetFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 订单详情 Service
 *
 * @author Sunxk
 * @since 2019-3-22
 */
@Slf4j
@Service
public class OrderDetManagerService {

    //订单详情状态  1待付款 2待发货 3待退款 4已退款 5已发货 6待评价 7已完成
    private static final Integer 待退款 = 3;
    private static final Integer 已退款 = 4;

    //审核状态  0待审核  1 审核通过 2审核不通过
    private static final Integer 待审核 = 0;
    private static final Integer 审核通过 = 1;
    private static final Integer 审核不通过 = 2;

    @Autowired
    OrderDetManagerDao orderDetManagerDao;

    /**
     * 退款申请订单处理
     *
     * @param id     退款订单Id
     * @param result 审核结果
     * @throws BusinessException
     */
    public void auditRefund(Long id, String result) throws BusinessException {
        log.info("退款申请订单处理");
        try {
            //查询订单详情状态和订单退款申请状态
            Map<String, Integer> statusMap = orderDetManagerDao.findOneOrderStatus(id);
            Integer detailStatus = statusMap.get("detailStatus");
            Integer refundStatus = statusMap.get("refundStatus");
            //订单详情表为待退款状态且订单退款申请为待审核状态
            if (detailStatus.equals(待退款) && refundStatus.equals(待审核)) {
                //审核通过
                if (result.equals("pass")) {
                    orderDetManagerDao.updateOrderDetail(已退款.longValue());
                    orderDetManagerDao.updateOrderRefund(审核通过.longValue());
                    //通知会员 审核已通过退款不日到账
                    //审核不通过
                } else if (result.equals("noPass")) {
                    //订单详情在申请退款时存起来 如果退款不成功将状态回写
                    //orderDetManagerDao.updateOrderDetail(已退款.longValue());
                    orderDetManagerDao.updateOrderRefund(审核不通过.longValue());
                    //通知会员 审核未通过请联系客服了解详情
                    //审核结果异常
                } else {
                    throw new BusinessException("订单审核状态异常 result = " + result);
                }
            } else {
                throw new BusinessException("退款申请订单处理订单状态异常 detailStatus= " + detailStatus +
                        " refundStatus= " + refundStatus);
            }
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

    /**
     * 分页查询待审核退款申请订单
     *
     * @param orderDetFindParam 待审核退款申请订单查询条件
     * @throws BusinessException
     */
    public PageDto<OrderDetManagerDto> findOrderDetRefund(OrderDetFindParam orderDetFindParam) throws BusinessException {
        log.info("分页查询待审核退款申请订单");
        try {
            PageDto<OrderDetManagerDto> pageDto = new PageDto<>();
            PageBean<OrderDetManagerDto, OrderDetFindParam> pageBean =
                    new PageBean<OrderDetManagerDto, OrderDetFindParam>(orderDetFindParam) {
                        @Override
                        protected Long generateRowCount() throws BusinessException {
                            return orderDetManagerDao.countOrderDet(orderDetFindParam);
                        }

                        @Override
                        protected List<OrderDetManagerDto> generateBeanList() throws BusinessException {
                            return orderDetManagerDao.findOrderDet(orderDetFindParam);
                        }
                    }.execute();
            long total = pageBean.getTotalCount();
            List<OrderDetManagerDto> list = pageBean.getPageData();
            pageDto.setTotal(total);
            pageDto.setList(list);
            return pageDto;
        } catch (Exception e) {
            log.error("分页查询待审核退款申请订单异常", e);
            throw new BusinessException();
        }
    }


}
