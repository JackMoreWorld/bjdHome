package com.bgd.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.entity.LikeDto;
import com.bgd.app.jms.JmsSend;
import com.bgd.app.service.ActService;
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
 * @创建人 JackMore
 * @创建时间 2019/3/7
 * @描述 商城活动
 */
@Slf4j
@RestController
@RequestMapping(value = "/act")
@Api(tags = "活动模块接口")
public class ActController {
    @Autowired
    ActService actService;
    @Autowired
    JmsSend jmsSend;
    /**
     * 轮播图
     * 限时购
     * 有好货
     * 每日推荐
     * 排行榜
     * 新品
     */


    /**
     * @描述 获取首页一级活动
     * @创建人 JackMore
     * @创建时间 2019/3/11
     */
    @GetMapping("/listMallActivity")
    @ApiOperation(value = "获取首页一级活动")
    public ResultDto<JSONObject> listActivity() {
        try {
            JSONObject acts = actService.getMallActivity();
            return new ResultDto<>(ResultDto.CODE_SUCC, "", acts);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 获取限时购商品列表
     * @创建人 JackMore
     * @创建时间 2019/3/13
     */
    @GetMapping("/listLimitPro/{actId}/{pageNo}")
    @ApiOperation(value = "获取限时购商品列表")
    public ResultDto<JSONObject> listLimitPro(@PathVariable Long actId, @PathVariable Integer pageNo) {
        try {
            JSONObject acts = actService.pageLimitPro(actId, pageNo);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", acts);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 获取有好货商品列表
     * @创建人 JackMore
     * @创建时间 2019/3/14
     */
    @GetMapping("/listSomePro/{actId}/{categoryId}/{pageNo}")
    @ApiOperation(value = "获取有好货商品列表")
    public ResultDto<JSONObject> listGoodPro(@PathVariable Long actId, @PathVariable Long categoryId, @PathVariable Integer pageNo) {
        try {
            JSONObject acts = actService.listGoodPro(actId, categoryId, pageNo);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", acts);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * @描述 有好货喜欢或者取消
     * @创建人 JackMore
     * @创建时间 2019/3/13
     */
    @GetMapping("/like/{actId}/{proId}/{type}")
    @ApiOperation(value = "有好货喜欢或者取消")
    public ResultDto<String> likeOrNotGoodPro(@PathVariable Long actId, @PathVariable Long proId, @PathVariable Integer type) {
        try {
            LikeDto dto = new LikeDto();
            dto.setActId(actId);
            dto.setProId(proId);
            dto.setStep(type);
            actService.likeOrNot(dto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询首页商品排行榜列表分页数据
     *
     * @param categoryId 商品分类id
     * @param pageNo     页码
     * @return 商品排行榜列表分页数据
     * @author wgq
     * @since 2019-03-18
     */
    @GetMapping(value = "/queryProRanking")
    @ApiOperation(value = "查询首页商品排行榜列表")
    public ResultDto<JSONObject> queryProRanking(@RequestParam(required = false) Long categoryId, @RequestParam Long pageNo) {
        try {
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", actService.queryProRanking(categoryId, pageNo));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询首页新品首发列表分页数据
     *
     * @param actId  活动id
     * @param pageNo 页码
     * @return 新品首发列表分页数据
     * @author wgq
     * @since 2019-03-18
     */
    @GetMapping(value = "/queryFirstNewPro/{actId}/{pageNo}")
    @ApiOperation(value = "查询首页新品首发列表")
    public ResultDto<JSONObject> queryFirstNewPro(@PathVariable Long actId, @PathVariable Long pageNo) {
        try {
            if (BgdStringUtil.isStringEmpty(actId.toString())) {
                throw new ParameterException("活动id不能为空");
            }
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询成功", actService.queryFirstNewPro(actId, pageNo));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }
}
