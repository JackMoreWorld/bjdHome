package com.bgd.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.service.SysService;
import com.bgd.support.base.ResultDto;
import com.bgd.support.base.UploadFileInfo;
import com.bgd.support.entity.SysOsPo;
import com.bgd.support.entity.SysRegionPo;
import com.bgd.support.entity.VipInformationPo;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/8
 * @描述
 */

@Slf4j
@RestController
@RequestMapping(value = "/sys", produces = "application/json;charset=UTF-8")
@Api(tags = "系统模块接口")
public class SysController {

    @Autowired
    SysService sysService;


    /**
     * @描述 登录
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "用户登录")
    public ResultDto<JSONObject> login(@RequestBody VipInformationPo user) {
        try {
            JSONObject result = sysService.login(user);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 登出
     * @创建人 JackMore
     * @创建时间 2019/3/12
     */
    @GetMapping(value = "/logout")
    @ApiOperation(value = "用户退出")
    public ResultDto<String> logout(@RequestHeader String token) {
        try {
            sysService.logout(token);
            return new ResultDto<>(ResultDto.CODE_SUCC, "登出成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * @描述 上传
     * @创建人 JackMore
     * @创建时间 2019/3/8
     */
    @PostMapping(value = "/upload")
    @ApiOperation(value = "上传图片")
    public ResultDto<UploadFileInfo> uploadAttachments(@RequestParam("uploadFile") MultipartFile file) {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "", sysService.saveUploadFile(file));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 获取三级城市列表
     * @创建人 JackMore
     * @创建时间 2019/3/8
     */
    @GetMapping(value = "/listRegion/{code}")
    @ApiOperation(value = "获取三级城市列表")
    public ResultDto<List<SysRegionPo>> listSysRegion(@PathVariable("code") String code) {
        try {
            List<SysRegionPo> sysRegions = sysService.findSysRegions(code);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", sysRegions);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 系统版本
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    @GetMapping(value = "/sysOsVersion")
    @ApiOperation(value = "系统版本")
    public ResultDto<SysOsPo> sysOsVersion() {
        try {
            SysOsPo sysOs = sysService.findSysOs();
            return new ResultDto<>(ResultDto.CODE_SUCC, "", sysOs);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
