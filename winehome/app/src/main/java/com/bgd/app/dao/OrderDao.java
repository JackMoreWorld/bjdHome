package com.bgd.app.dao;

import com.bgd.app.entity.*;
import com.bgd.app.entity.param.VipOrderParam;
import com.bgd.support.entity.MallOrderDetailPo;
import com.bgd.support.entity.MallOrderPo;
import com.bgd.support.entity.MallOrderStarPo;
import com.bgd.support.entity.ProductInformationPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDao {

    int insertOrder(MallOrderDto mallOrderDto);

    int insertOrderDetail(@Param("list") List<MallOrderDetailPo> list);

    List<MallOrderDetailDto> getOrderListByPage(VipOrderParam vipOrderParam);

    List<Map<String,Object>> getOrderCount(VipOrderParam vipOrderParam);

    int delOrder(@Param("orderId") String orderId, @Param("vipId") String vipId);

    int delOrderDetail(@Param("orderId") String orderId, @Param("vipId") String vipId);

    VipOrderDto getOrderInfo(@Param("orderNo") String orderNo, @Param("vipId") String vipId);

    int updOrderInfo(MallOrderPo mallOrderPo);

    int updOrderDetail(MallOrderDetailPo orderDetailPo);

    int updOrderStatus(MallOrderDetailPo orderDetailPo);

    int insertCapitalFlow(MallCapitalFlowDto capitalFlowDto);

    int confirmReceipt(@Param("orderId") String orderId, @Param("vipId") String vipId);

    ProductInformationPo findProductByOrderId(@Param("orderId") Long orderId);

    int saveOrUpdateOrderStar(MallOrderStarPo star);

    int updOrderDetails(MallOrderDetailPo orderDetailPo);

    int insertRefund(MallOrderRefundDto orderRefundDto);

    int insertOrderEvaluate(@Param("detailId") String detailId, @Param("remark") String remark,
                            @Param("star") Long star, @Param("vipId") Long vipId);

    int orderEvaluate(@Param("orderId") String orderId, @Param("status") String status);

    int updCouponStatus(@Param("couponId") Long couponId, @Param("vipId") Long vipId);

    int delOrderDetailById(@Param("detailId") String detailId, @Param("vipId") String vipId);

    MallOrderDetailDto getOrderDetail(@Param("detailId") String detailId, @Param("vipId") String vipId);

}
