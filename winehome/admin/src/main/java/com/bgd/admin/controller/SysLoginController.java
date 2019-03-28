package com.bgd.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bgd.admin.entity.SysUser;
import com.bgd.admin.service.SysLoginService;
import com.bgd.support.base.ResultDto;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/sys")
@Api(tags = "管理员登陆")
public class SysLoginController {


    @Autowired
    SysLoginService loginService;



    /**
     * 登录
     *
     * @param sysUser
     * @return
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "登陆",notes = "登陆")
    public ResultDto<String> login(@RequestBody SysUser sysUser) {
        try {
            String result = loginService.login(sysUser);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (Exception e) {
            log.error("登录失败：" + sysUser.getName() + "：", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "登录失败", null);
        }
    }


    /**
     * 登出
     *
     * @param token
     * @return
     */
    @GetMapping(value = "/logout")
    @ApiOperation(value = "登出",notes = "登出")
    public ResultDto<String> logout(@RequestHeader String token) {
        try {
            loginService.logout(token);
            return new ResultDto<>(ResultDto.CODE_SUCC, "登出成功", null);
        }  catch (Exception e) {
            log.error("登出失败：" + token + "：", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "登出失败", e.getMessage());
        }
    }
}
