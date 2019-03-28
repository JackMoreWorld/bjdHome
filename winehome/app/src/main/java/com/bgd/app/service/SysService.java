
package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.conf.Global;
import com.bgd.app.dao.SysDao;
import com.bgd.app.dao.VipDao;
import com.bgd.app.entity.VipInformationDto;
import com.bgd.support.base.Constants;
import com.bgd.support.base.FileTypeEnum;
import com.bgd.support.base.UploadFileInfo;
import com.bgd.support.entity.SysOsPo;
import com.bgd.support.entity.SysRegionPo;
import com.bgd.support.entity.VipInformationPo;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class SysService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SysDao sysDao;
    @Autowired
    VipDao vipDao;
    @Autowired
    Global global;

    /**
     * @描述 上传文件
     * @创建人 JackMore
     * @创建时间 2019/3/8
     */
    public UploadFileInfo saveUploadFile(MultipartFile file) throws BusinessException {
        try {
            if (null == file)
                return null;

            String fileType;
            if (file.getContentType().contains("image")) {
                fileType = FileTypeEnum.PICTURE.toCode();
            } else {
                fileType = FileTypeEnum.DOCUMENT.toCode();
            }
            String path = global.getTruePath();
            String nginxPath = global.getNginxPath();
            UploadFileInfo fileInfo = new UploadFileInfo();
            fileInfo.setSuffix(FileUtils.getSuffix(file.getOriginalFilename()));
            fileInfo.setAliasName(BgdStringUtil.GenerateRanNum(8) + "." + fileInfo.getSuffix());
            fileInfo.setFileType(fileType);
            fileInfo.setSize(file.getSize() / 1000);// save size as kb
            fileInfo.setContextPath(nginxPath + fileType + FileUtils.separator() + fileInfo.getAliasName());
            FileUtils.saveFileAsStream(file.getInputStream(), path + fileType + FileUtils.separator() + fileInfo.getAliasName());
            return fileInfo;
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new BusinessException("上传文件失败");
        }
    }


    /**
     * @描述 获取城市地区信息
     * @创建人 JackMore
     * @创建时间 2019/3/8
     */
    public List<SysRegionPo> findSysRegions(String code) throws BusinessException {
        try {
            List<SysRegionPo> list = null;
            String redisRegions = redisUtil.get(AppConstant.REDIS.REGION + code);
            if (redisRegions == null) {
                list = sysDao.findRegionByCode(code);
                redisUtil.set(AppConstant.REDIS.REGION + code, JSONObject.toJSONString(list));
                return list;
            }
            return JSONObject.parseArray(redisRegions, SysRegionPo.class);
        } catch (Exception e) {
            log.error("获取城市下级列表失败", e);
            throw new BusinessException("获取城市下级列表失败");
        }

    }

    /**
     * @描述 登录
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    public JSONObject login(VipInformationPo user) throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            if (BgdStringUtil.isEmpty(user.getPhone())) {
                throw new BusinessException("账户名不能为空");
            }
            if (BgdStringUtil.isEmpty(user.getPassword())) {
                throw new BusinessException("密码不能为空");
            }
            log.info(user.getPhone() + ",登录平台");
            user.setPassword(PasswordUtil.MD5Salt(user.getPassword()));
            VipInformationDto vip = vipDao.findVipByVip(user);
            if (vip == null) {
                log.error(String.format("用户名%s或密码%s不正确！", user.getPhone(), user.getPassword()));
                throw new BusinessException("用户名或密码不正确！");
            }
            String token = RandomUtil.getRandomString(32);

            redisUtil.set(Constants.PREFIX_APP + token, JSONObject.toJSONString(vip));
            result.put("token", token);
            return result;
        } catch (BusinessException e) {
            log.error("登录异常", e);
            throw e;
        }
    }

    /**
     * @描述 登出
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    public void logout(String token) throws BusinessException {
        if (!redisUtil.exists(Constants.PREFIX_APP + token)) {
            throw new BusinessException("登出失败,无此token");
        }
        VipInformationPo vip = JSONObject.parseObject(redisUtil.get(Constants.PREFIX_APP + token), VipInformationPo.class);
        log.info(vip.getName() + "--退出平台");
        redisUtil.remove(Constants.PREFIX_APP + token);
    }

    /**
     * @描述 获取最新版本
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    public SysOsPo findSysOs() throws BusinessException {
        try {
            SysOsPo sysOs = sysDao.findSysOs();
            return sysOs;
        } catch (Exception e) {
            log.error("获取版本异常", e);
            throw new BusinessException();
        }
    }


}