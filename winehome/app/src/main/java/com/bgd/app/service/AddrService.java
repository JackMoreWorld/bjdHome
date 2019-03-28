
package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.dao.AddrDao;
import com.bgd.app.entity.VipAddrDto;
import com.bgd.support.entity.VipAddrPo;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AddrService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    AddrDao addrDao;

    /**
     * @描述 操作用户收货地址
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    @Transactional
    public void dealVipAddr(VipAddrDto addr) throws BusinessException {
        try {
            //  新增 修改 删除
            Integer operationType = addr.getOperationType();
            if (AppConstant.OPERA_TYPE.CREATE.equals(operationType)) { //新增
                VipAddrPo po = new VipAddrPo();
                transferAddr(addr, po);
                po.setCreateTime(new Date());
                addrDao.saveVipAddr(addr);
                if (AppConstant.ADDR_TYPE.默认.equals(po.getType())) {
                    defaultAddr(addr.getVipId(), po.getId());
                }
            }
            if (AppConstant.OPERA_TYPE.UPDATE.equals(operationType)) { //修改
                VipAddrPo po = new VipAddrPo();
                po.setId(addr.getId());
                transferAddr(addr, po);
                po.setUpdateTime(new Date());
                addrDao.flushVipAddr(addr);
                if (AppConstant.ADDR_TYPE.默认.equals(po.getType())) {
                    defaultAddr(addr.getVipId(), po.getId());
                }
            }
            if (AppConstant.OPERA_TYPE.DEL.equals(operationType)) { //删除
                addrDao.delAddr(addr.getVipId(), addr.getId());
            }
            List<VipAddrPo> vipAddrList = addrDao.findVipAddrList(addr.getVipId());
            redisUtil.set(AppConstant.REDIS.ADDR + addr.getVipId(), JSONObject.toJSONString(vipAddrList));
        } catch (Exception e) {
            log.error("处理收货地址异常", e);
            throw new BusinessException("处理收货地址异常");
        }
    }

    /**
     * dto -> po
     *
     * @param dto
     * @param po
     * @return
     */
    private VipAddrPo transferAddr(VipAddrDto dto, VipAddrPo po) {
        po.setVipId(dto.getVipId());
        po.setName(dto.getName());
        po.setPhone(dto.getPhone());
        po.setType(dto.getType());
        po.setProvince(dto.getProvince());
        po.setCity(dto.getCity());
        po.setArea(dto.getArea());
        po.setAddr(dto.getAddr());
        return po;
    }


    /**
     * @描述 设置默认地址
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    public void defaultAddr(Long vipId, Long addrId) {
        try {
            addrDao.setDefaultAddr(vipId, addrId);
        } catch (Exception e) {
            throw new BusinessException("设置默认地址异常");
        }
    }


    /**
     * @描述 获取用户收货地址
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    public List<VipAddrPo> getVipAddrList(Long vipId) throws BusinessException {
        try {
            String redisAddr = redisUtil.get(AppConstant.REDIS.ADDR + vipId);
            if (redisAddr != null) {
                return JSONObject.parseArray(redisAddr, VipAddrPo.class);
            }
            List<VipAddrPo> vipAddrList = addrDao.findVipAddrList(vipId);
            redisUtil.set(AppConstant.REDIS.ADDR + vipId, JSONObject.toJSONString(vipAddrList));
            return vipAddrList;
        } catch (Exception e) {
            throw new BusinessException("获取会员收货地址异常");
        }
    }

    /**
     * 获取用户的收货地址
     *
     * @param vipId 会员id
     * @return 收货地址
     * @author wangguoqing
     * @since 2019-03-12
     */
    VipAddrPo getVipAddr(Long vipId) throws BusinessException {
        try {
            VipAddrPo vipAddrPo = addrDao.getVipDefaultAddr(vipId);
            if (null == vipAddrPo) {
                return addrDao.getVipFirstAddr(vipId);
            } else {
                return vipAddrPo;
            }
        } catch (Exception e) {
            throw new BusinessException("获取用户的收货地址异常");
        }
    }

}