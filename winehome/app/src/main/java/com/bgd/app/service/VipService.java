
package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.conf.Global;
import com.bgd.app.dao.MallDao;
import com.bgd.app.dao.VipCenterDao;
import com.bgd.app.dao.VipDao;
import com.bgd.app.entity.VipInformationDto;
import com.bgd.app.entity.param.SecureParam;
import com.bgd.app.jms.JmsSend;
import com.bgd.app.util.AppUtil;
import com.bgd.support.entity.*;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VipService {

    @Autowired
    VipDao vipDao;
    @Autowired
    VipCenterDao vipCenterDao;
    @Autowired
    MallDao mallDao;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    AppUtil appUtil;
    @Autowired
    JmsSend jmsSend;
    @Autowired
    Global global;

    /**
     * @描述 获取手机号验证码
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    public JSONObject getPhoneCode(String phone, Integer type) throws BusinessException {
        JSONObject result = new JSONObject();
        try {
            if (BgdStringUtil.isStringEmpty(phone)) {
                log.error("手机号不可为空");
                throw new BusinessException("手机号不可为空");
            }
            result.put("code", appUtil.sendSms(phone, prefix(type)));
        } catch (BusinessException e) {
            throw e;
        }
        return result;
    }

    /**
     * @描述 校验手机验证码是否失效
     * @创建人 JackMore
     * @创建时间 2019/3/21
     */
    public boolean validPhoneCode(String phone, String code, Integer type) {
        String pre = prefix(type);
        boolean exists = redisUtil.exists(pre + phone + code);
        return exists;
    }

    /**
     * @描述 验证码分类
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    private String prefix(Integer type) {
        if (type == null) return null;
        if (type == 1) {
            return AppConstant.PHONE_CODE.注册;
        } else if (type == 1) {
            return AppConstant.PHONE_CODE.密码;
        } else {
            return AppConstant.PHONE_CODE.支付密码;
        }
    }

    @Transactional
    public JSONObject regVip(VipInformationDto vip) throws BusinessException {
        JSONObject result = new JSONObject();
        if (BgdStringUtil.isStringEmpty(vip.getPhone())) {
            log.error("手机号不可为空");
            throw new BusinessException("手机号不可为空");
        }
        if (BgdStringUtil.isStringEmpty(vip.getPassword())) {
            log.error("密码不可为空");
            throw new BusinessException("密码不可为空");
        }
        if (!validPhoneCode(vip.getPhone(), vip.getCode(), 1)) { // 注册
            throw new BusinessException("验证码错误");
        }
        VipInformationPo po = new VipInformationPo();
        po.setPhone(vip.getPhone());
        VipInformationDto vipByVip = vipDao.findVipByVip(po);
        if (vipByVip != null) {
            throw new BusinessException("该手机号已注册");
        }
        po.setName("小酒" + vip.getPhone());
        po.setPhone(vip.getPhone());
        po.setPassword(PasswordUtil.MD5Salt(vip.getPassword()));
        String code = RandomUtil.getRandomString(11);
        po.setInviteCode(code);
        String regCode = vip.getRegCode();

        po.setLogo(global.getDefaultImg());
        po.setCreateTime(new Date());
        vipDao.saveVip(po);

        if (regCode != null) { // 有邀请码
            po.setRegCode(regCode);
            MallInviteRecordPo recordPo = new MallInviteRecordPo();
            recordPo.setCode(code);
            recordPo.setInviteCode(regCode);
            jmsSend.invite(JSONObject.toJSONString(recordPo));
        } else { //无邀请码
            VipReward reward = new VipReward();
            reward.setVipId(po.getId());
            reward.setType(1);
            reward.setStatus(AppConstant.USED_TYPE.未领);
            reward.setPrice(new BigDecimal(50));
            reward.setCreateTime(new Date());
            vipDao.saveVipReward(reward);
        }

        VipMallInfoPo mallInfo = new VipMallInfoPo();
        mallInfo.setVipId(po.getId());
        mallInfo.setLevelId(AppConstant.VIP_LEVEL.普通);
        mallInfo.setVipNo(RandomUtil.getRandomString(10));
        mallInfo.setCreateTime(new Date());
        vipDao.saveVipMallInfo(mallInfo);
        result.put("vipId", po.getId());
        return result;
    }


    /**
     * @描述 完善用户中心信息
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    @Transactional
    public JSONObject completeVipInfo(VipInformationPo vip) throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            vip.setPassword(null);
            vipDao.flushVip(vip);
            result.put("vipId", vip);
            return result;
        } catch (Exception e) {
            log.error("完善用户信息失败", e);
            throw e;
        }
    }


    /**
     * @描述 密码修改
     * @创建人 JackMore
     * @创建时间 2019/3/21
     */
    @Transactional
    public JSONObject secureVipInfo(SecureParam param) throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            Long vipId = param.getVipId();
            String passWord = param.getPassword();
            String payWord = param.getPayWord();

            VipInformationPo vip = new VipInformationPo();
            vip.setId(vipId);
            vip.setPassword(passWord);
            vip.setPayPassWord(payWord);
            vipDao.flushVip(vip);
            result.put("vipId", vipId);
            return result;
        } catch (Exception e) {
            log.error("完善用户信息失败", e);
            throw e;
        }
    }


    /**
     * @描述 根据vipId获取会员基础信息
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    public VipInformationDto getVipMsgById(Long vipId) throws BusinessException {
        try {
            VipInformationDto vip = new VipInformationDto();
            vip.setId(vipId);
            VipInformationDto vipByVip = vipDao.findVipByVip(vip);
            return vipByVip;
        } catch (Exception e) {
            log.error("获取vip信息异常", e);
            throw new BusinessException();
        }

    }


    /**
     * @描述 根据vipId获取关注酒庄列表
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    public void focusOrNot(Long vipId, Long chateauId, Integer status) throws BusinessException {
        try {
            vipDao.saveOrUpdateFocus(vipId, chateauId, status);
        } catch (Exception e) {
            log.error("关注酒庄信息异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 根据vipId获取关注酒庄列表
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    public void markFoot(VipFootMarkPo po) throws BusinessException {
        try {
            vipDao.saveFoot(po);
        } catch (Exception e) {
            log.error("用户足迹", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 根据vipId获取关注酒庄列表
     * @创建人 JackMore
     * @创建时间 2019/3/6
     */
    public void collectOrCancel(Long vipId, Long productId, Integer status) throws BusinessException {
        try {
            vipDao.saveOrUpdateCollect(vipId, productId, status);
        } catch (Exception e) {
            log.error("收藏单品信息异常", e);
            throw new BusinessException();
        }
    }

    /**
     * @描述 获取优惠券
     * @创建人 JackMore
     * @创建时间 2019/3/7
     */
    public void gatherCoupons(Long vipId, Long couponsId) throws BusinessException {
        try {
            VipCouponsPo coupons = new VipCouponsPo();
            coupons.setVipId(vipId);
            coupons.setMallCouponsId(couponsId);
            List<VipCouponsPo> vipCoupons = vipCenterDao.findVipCouponsList(coupons);
            if (vipCoupons != null) {
                throw new BusinessException("该优惠券已领！");
            }
            MallCouponsPo mallCoupons = mallDao.findMallCouponsById(couponsId);
            if (mallCoupons == null) {
                throw new BusinessException("该优惠券不存在！");
            }
            coupons.setChateauId(mallCoupons.getChateauId());
            coupons.setLevelId(mallCoupons.getLevelId());
            coupons.setType(mallCoupons.getType());
            coupons.setPrice(mallCoupons.getPrice());
            vipDao.saveVipCoupons(coupons);

        } catch (Exception e) {
            log.error("会员领取优惠券异常", e);
            throw e;
        }
    }

    /**
     * @return void
     * @author Sunxk
     * @date 2019/3/14
     * @Param [id]
     */
    public Long callUpLevelByPointsP(Long id, Long points) throws BusinessException {
        try {
            Map<String, Long> map = new HashMap<String, Long>();
            map.put("vipId", id);
            map.put("points", points);
            vipDao.callUpLevelByPointsP(map);
            Long status = map.get("status");
            return status;
        } catch (Exception e) {
            log.error("提升会员等级异常", e);
            throw new BusinessException("提升会员等级异常");
        }
    }


    /**
     * @描述 会员签到 领积分
     * @创建人 JackMore
     * @创建时间 2019/3/19
     */
    @Transactional
    public void signIn(Long vipId) throws BusinessException {
        try {
            //查询是否签到
            //TODO 签到积分数额未定
            String nowDay = BgdDateUtil.format(new Date());
            VipRecordPo po = new VipRecordPo();
            po.setVipId(vipId);
            po.setType(AppConstant.RECORD_TYPE.签到);
            po.setRecordDay(nowDay);
            VipRecordPo vipRecord = vipDao.findVipRecord(po);
            if (vipRecord != null) {
                throw new BusinessException("今日已签到！");
            }
            po.setContent("签到领积分");
            vipDao.saveVipRecord(po);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("vipId", vipId);
            jsonObject.put("points", 5);
            jmsSend.addIntegralByOrder(JSONObject.toJSONString(jsonObject));
        } catch (Exception e) {
            log.error("签到操作异常", e);
            throw e;
        }
    }


    /**
     * @描述 获取会员登录红包
     * @创建人 JackMore
     * @创建时间 2019/3/27
     */
    public VipReward findVipReward(Long vipId) {
        try {
            VipReward vipReward = new VipReward();
            vipReward.setVipId(vipId);
            vipReward.setType(1);
            vipReward.setStatus(AppConstant.USED_TYPE.未领);
            VipReward rewardOne = vipDao.findRewardOne(vipReward);
            return rewardOne;
        } catch (Exception e) {
            log.error("查询会员红包异常", e);
            throw new BusinessException();
        }
    }


}