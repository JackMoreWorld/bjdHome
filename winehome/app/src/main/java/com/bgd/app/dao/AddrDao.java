package com.bgd.app.dao;

import com.bgd.support.entity.VipAddrPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddrDao {


    int saveVipAddr(VipAddrPo addr);

    int flushVipAddr(VipAddrPo addr);

    int setDefaultAddr(Long vipId, Long addrId);

    int  delAddr(Long vipId, Long addrId);

    List<VipAddrPo> findVipAddrList(@Param("vipId") Long vipId);

    //获取会员默认收货地址
    VipAddrPo getVipDefaultAddr(@Param("vipId") Long vipId);


    //获取会员默认收货地址
    VipAddrPo getVipFirstAddr(@Param("vipId") Long vipId);
}
