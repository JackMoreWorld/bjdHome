package com.bgd.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.entity.CommunityArticleDto;
import com.bgd.app.entity.CommunityFocusDto;
import com.bgd.app.entity.param.CommunityParam;
import com.bgd.app.service.CommunityService;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.entity.CommunityArticlePo;
import com.bgd.support.exception.BusinessException;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/15
 * @描述 社区
 */
@Slf4j
@RestController
@RequestMapping(value = "/community")
@Api(tags = "社区模块接口")
public class CommunityController {

    @Autowired
    CommunityService communityService;

    /**
     * @描述 社区关注
     * @创建人 JackMore
     * @创建时间 2019/3/15
     */
    @GetMapping("/focus/{vipId}/{focusId}/{type}")
    @ApiOperation(value = "社区关注或者取消")
    public ResultDto<JSONObject> focus(@PathVariable Long vipId, @PathVariable Long focusId, @PathVariable Integer type) {
        try {
            communityService.focus(vipId, focusId, type);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    @GetMapping(value = {"/homeArticle/{pageNo}"})
    @ApiOperation(value = "社区首页")
    public ResultDto<PageDto<CommunityArticleDto>> listAllArticle(@PathVariable Integer pageNo) {
        try {
            CommunityParam param = new CommunityParam();
            param.setPageNo(pageNo);
            param.setType(AppConstant.ARTICLE_TYPE.发帖);
            PageDto<CommunityArticleDto> allArticle = communityService.listAllArticle(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", allArticle);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    @GetMapping("/listFocusArticle/{vipId}/{pageNo}")
    @ApiOperation(value = "获取关注人发帖")
    public ResultDto<PageDto<CommunityArticleDto>> findFocusArticle(@PathVariable Long vipId, @PathVariable Integer pageNo) {
        try {
            CommunityParam param = new CommunityParam();
            param.setPageNo(pageNo);
            param.setVipId(vipId);
            PageDto<CommunityArticleDto> focusArticle = communityService.findFocusArticle(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", focusArticle);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    @GetMapping("/detailArticle/{id}")
    @ApiOperation(value = "获取发帖详情")
    public ResultDto<CommunityArticleDto> findArticleDetail(@PathVariable Long id) {
        try {
            CommunityArticleDto article = communityService.findArticleDetail(id);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", article);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    @PostMapping("/comment")
    @ApiOperation(value = "跟帖评论")
    public ResultDto<JSONObject> comment(@RequestBody CommunityArticlePo po) {
        try {
            communityService.comment(po);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * @描述 发帖点赞
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    @GetMapping("/like/{id}/{type}")
    @ApiOperation(value = "发帖点赞")
    public ResultDto<CommunityArticleDto> findArticleDetail(@PathVariable Long id, @PathVariable Integer type) {
        try {
            Map<String, Object> map = Maps.newHashMap();
            map.put("type", type);
            map.put("id", id);
            communityService.likeOrNot(map);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    @GetMapping("/vipArticle/{vipId}/{pageNo}")
    @ApiOperation(value = "获取会员发帖")
    public ResultDto<PageDto<CommunityArticleDto>> findMyArticle(@PathVariable Long vipId, @PathVariable Integer pageNo) {
        try {
            CommunityParam param = new CommunityParam();
            param.setVipId(vipId);
            param.setPageNo(pageNo);
            PageDto<CommunityArticleDto> myArticle = communityService.listArticle(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", myArticle);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    @GetMapping("/myFocus/{vipId}")
    @ApiOperation(value = "获取我的关注列表")
    public ResultDto<List<CommunityFocusDto>> findMyFocus(@PathVariable Long vipId) {
        try {
            List<CommunityFocusDto> myFocus = communityService.findMyFocus(vipId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", myFocus);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    @GetMapping("/allArticleComment/{pid}")
    @ApiOperation(value = "获取跟帖列表")
    public ResultDto<PageDto<CommunityArticleDto>> allArticleComment(@PathVariable Long pid) {
        try {
            CommunityParam param = new CommunityParam();
            param.setPid(pid);
            param.setType(AppConstant.ARTICLE_TYPE.跟帖);
            PageDto<CommunityArticleDto> allComment = communityService.listAllArticle(param);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", allComment);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
