package com.bgd.admin.controller;

import com.bgd.admin.entity.PushManagerDto;
import com.bgd.admin.service.PushManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.exception.ParameterException;
import com.bgd.support.utils.BgdStringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  首页推荐信息管理 Controller
 * @author Sunxk
 * @since 2019-3-19
 */
@Slf4j
@RestController
@RequestMapping("/pushManager")
@Api(tags = "首页推荐信息管理接口")
public class PushManagerController {

    @Autowired
    PushManagerService pushManagerService;


    /**
     *  改变首页推荐信息
     * @param pushManagerDto 首页推荐信息Dto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping("/changePush")
    @ApiOperation(value = "改变首页推荐信息",notes = "改变首页推荐信息")
    public ResultDto changePush(@RequestBody PushManagerDto pushManagerDto){
        log.info("改变首页推荐信息");
        try{
            if(BgdStringUtil.isStringEmpty(pushManagerDto.getType().toString())){
                throw new ParameterException("改变首页推荐信息类型type不能为空");
            }
            pushManagerService.changePush(pushManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC,"",null);
        }catch(BusinessException e){
            log.error("改变首页推荐信息失败",e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR,e.getMessage(),null);
        }catch(Exception e){
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR,"业务异常",null);
        }
    }

    /**
     *  查询首页推荐信息
     * @param type 类型 1热销国家 2推荐酒庄 3推荐产品 4精选活动
     * @return
     */
    @ValidToken(type = 0)
    @GetMapping("/findPush")
    @ApiOperation(value = "查询首页推荐信息",notes = "根据类型查询首页推荐信息1热销国家 2推荐酒庄 3推荐产品 4精选活动")
    public ResultDto<List> findPush(@RequestParam Integer type){
        log.info("查询首页推荐信息");
        try{
            if(BgdStringUtil.isStringEmpty(type.toString())){
                throw new ParameterException("查询首页推荐信息type不能为空");
            }
            List list =  pushManagerService.findPush(type);
            return new ResultDto<>(ResultDto.CODE_SUCC,"",list);
        }catch(BusinessException e){
            log.error("查询首页推荐信息失败",e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR,e.getMessage(),null);
        }catch(Exception e){
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR,"业务异常",null);
        }
    }

}
