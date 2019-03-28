package com.bgd.app.dao;

import com.bgd.app.entity.CartProductDto;
import com.bgd.app.entity.VipCartChateauDto;
import com.bgd.app.entity.VipCartDto;
import com.bgd.app.entity.param.CartRequestParam;
import com.bgd.support.entity.VipCartPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 购物车 dao
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-11
 */
@Mapper
public interface CartDao {


    /**
     * 查询购物车列表分页数据
     *
     * @param requestParamsDto 购物车请求 dto
     * @return 购物车列表分页数据
     */
    List<VipCartDto> getCartList(CartRequestParam requestParamsDto);

    //查询购物车count数
    Long getCartListCount(Long vipId);

    //根据购物车id删除购物车信息
    int delCartById(@Param("vipId") Long vipId, @Param("cartId") Long cartId);

    //加入购物车
    int addCart(VipCartPo vipCartPo);

    List<VipCartChateauDto> getCartChateau(VipCartChateauDto vipCartDto);

    List<CartProductDto> queryProList(@Param("array") String[] cartIdArray);

    Long addCartProCount(@Param("cartId") long cartId, @Param("type") long type, @Param("vipId") long vipId);

    Long subCartProCount(@Param("cartId") long cartId, @Param("type") long type, @Param("vipId") long vipId);

    Long getCartProCount(@Param("cartId") long cartId, @Param("vipId") long vipId);

    //根据购物车id批量删除购物车信息
    int batchDelCart(@Param("array") String[] cartIdArray);

    List<CartProductDto> queryProResult(long proId);

    List<Map<String, String>> getProListByCartIdArray(@Param("array") String[] cartIdArray);

    int updActProStock(@Param("list") List<Map<String, String>> list);
}
