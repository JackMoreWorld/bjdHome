package com.bgd.admin.controller.productManager;

import com.bgd.admin.entity.ArticleManagerDto;
import com.bgd.admin.entity.param.ArticleFindParam;
import com.bgd.admin.service.ArticleManagerService;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 论坛帖子信息管理 Controller
 *
 * @author Sunxk
 * @since 20119-3-18
 */
@Slf4j
@RestController
@RequestMapping(value = "/articleManager")
@Api(tags = "论坛帖子信息管理接口")
public class ArticleManagerController {

    @Autowired
    ArticleManagerService articleManagerService;

    /**
     * 处理论坛帖子信息
     *
     * @param articleManagerDto 帖子Dto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping("/dealArticle")
    @ApiOperation(value = "处理论坛帖子信息", notes = "根据dealType（0新增，1修改，2删除）的类型对提交的论坛帖子进行处理")
    public ResultDto dealArticle(@RequestBody ArticleManagerDto articleManagerDto) {
        log.info("根据dealType（0新增，1修改，2删除）dealType = " + articleManagerDto.getDealType() + "的类型对提交的论坛帖子进行处理");
        try {
            articleManagerService.dealArticle(articleManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("处理帖子失败 title = " + articleManagerDto.getTitle() + "会员等级vipName = " + articleManagerDto.getVipName(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 分页查询论坛帖子
     *
     * @param vipName 会员昵称
     * @param type    类型
     * @param title   帖子标题
     * @param pageNo  页码
     * @return
     */
    @ValidToken(type = 0)
    @GetMapping(value = "/findArticle")
    @ApiOperation(value = "分页查询论坛帖子", notes = "根据条件分页查询帖子信息")
    public ResultDto<PageDto<ArticleManagerDto>> findArticle(@RequestParam(required = false) String vipName,
                                                             @RequestParam(required = false) String title,
                                                             @RequestParam Integer type,
                                                             @RequestParam Integer pageNo) {
        log.info("分页查询论坛帖子");
        try {
            ArticleFindParam articleFindParam = new ArticleFindParam();
            articleFindParam.setVipName(vipName);
            articleFindParam.setType(type);
            articleFindParam.setTitle(title);
            articleFindParam.setPageNo(pageNo);
            PageDto<ArticleManagerDto> pageDto = articleManagerService.findArticle(articleFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询文章失败 title = " + title + "会员昵称 vipName = " + vipName, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }
}
