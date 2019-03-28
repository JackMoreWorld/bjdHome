package com.bgd.admin.controller.productManager;

import com.bgd.admin.entity.ActivityManagerDto;
import com.bgd.admin.entity.param.ActivityFindParam;
import com.bgd.admin.service.ActivityManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.exception.ParameterException;
import com.bgd.support.utils.BgdStringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 活动信息管理 Controller
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@RestController
@RequestMapping(value = "/ActivityManager")
@Api(tags = {"活动信息管理接口"})
public class ActivityManagerController {

    @Autowired
    ActivityManagerService activityManagerService;

    /**
     * 新增活动信息
     *
     * @param activityManagerDto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping(value = "/addActivity")
    @ApiOperation(value = "新增活动信息", notes = "根据提交的数据新增活动信息")
    public ResultDto addActivity(@RequestBody ActivityManagerDto activityManagerDto) {
        log.info("新增活动信息");
        try {
            activityManagerService.addActivity(activityManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("新增活动类型失败 type = " + activityManagerDto.getType() + "name = " + activityManagerDto.getName()
                    + "status = " + activityManagerDto.getStatus(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 删除活动信息
     *
     * @param activityId 活动ID
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping(value = "/deleteActivity")
    @ApiOperation(value = "删除活动信息", notes = "根据活动ID删除活动信息")
    public ResultDto deleteActivity(@RequestBody Long activityId) {
        log.info("删除活动信息");
        try {
            if (BgdStringUtil.isStringEmpty(activityId.toString())) {
                throw new ParameterException("删除活动信息id不能为空");
            }
            activityManagerService.deleteActivity(activityId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("删除活动产品失败 activityId = " + activityId, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 修改活动信息
     *
     * @param activityManagerDto 活动信息Dto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping(value = "/updateActivity")
    @ApiOperation(value = "修改活动信息", notes = "根据提交的数据修改活动信息")
    public ResultDto updateActivity(@RequestBody ActivityManagerDto activityManagerDto) {
        log.info("修改活动信息");
        try {
            activityManagerService.updateActivity(activityManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("修改活动信息失败 type = " + activityManagerDto.getType() + " name = " + activityManagerDto.getName()
                    + " status = " + activityManagerDto.getStatus(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            log.error("修改活动信息失败 type = " + activityManagerDto.getType() + " name = " + activityManagerDto.getName()
                    + " status = " + activityManagerDto.getStatus(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 分页查询活动信息
     *
     * @param name   活动名称
     * @param type   类型 0限时购 1有好货 2每日推荐  3新品首发 4主打酒
     * @param title  标题
     * @param status 状态0 未开始 1进行中 2停止
     * @param pageNo 页码
     * @return
     */
    @ValidToken(type = 0)
    @GetMapping(value = "/findActivity")
    @ApiOperation(value = "分页查询活动信息", notes = "根据条件查询活动信息")
    public ResultDto<PageDto<ActivityManagerDto>> findActivity(@RequestParam(required = false) String name,
                                                               @RequestParam(required = false) Integer type,
                                                               @RequestParam(required = false) String title,
                                                               @RequestParam(required = false) Integer status,
                                                               @RequestParam Integer pageNo) {
        log.info("分页从查询活动信息");
        try {
            ActivityFindParam activityFindParam = new ActivityFindParam();
            activityFindParam.setName(name);
            activityFindParam.setType(type);
            activityFindParam.setTitle(title);
            activityFindParam.setStatus(status);
            activityFindParam.setPageNo(pageNo);
            PageDto<ActivityManagerDto> pageDto = activityManagerService.findActivity(activityFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询活动类型失败 type = " + type + "活动名称 name = " + name
                    + "活动状态 status = " + status, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
