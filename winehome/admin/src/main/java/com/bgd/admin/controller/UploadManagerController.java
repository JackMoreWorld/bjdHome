package com.bgd.admin.controller;

import com.bgd.admin.service.UploadManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.ResultDto;
import com.bgd.support.base.UploadFileInfo;
import com.bgd.support.exception.ParameterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传管理 controller
 * @author sunxk
 * @since 2019-3-25
 */
@Slf4j
@RestController
@RequestMapping("/upload")
@Api(tags = "文件上传管理")
public class UploadManagerController {


    @Autowired
    UploadManagerService uploadManagerService;

    /**
     *  上传图片
     * @param file 图片信息
     */
    //@ValidToken(type = 0)
    @PostMapping(value = "/img")
    @ApiOperation(value = "上传图片",notes = "上传图片并返回对应信息")
    public ResultDto<UploadFileInfo> uploadImg (@RequestParam("uploadImg") MultipartFile file){
        log.info("上传图片"+file.getOriginalFilename());
        try{
            return new ResultDto<>(ResultDto.CODE_SUCC,"上传图片成功",uploadManagerService.uploadImg(file));
        }catch(ParameterException e){
            log.error("上传图片失败+file.getOriginalFilename()",e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR,e.getMessage(),null);
        }catch(Exception e){
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR,"业务异常",null);
        }
    }


}
