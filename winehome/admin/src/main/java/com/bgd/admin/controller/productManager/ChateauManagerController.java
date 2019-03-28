package com.bgd.admin.controller.productManager;

import com.bgd.admin.entity.ChateauManagerDto;
import com.bgd.admin.entity.param.ChateauFindParam;
import com.bgd.admin.service.ChateauManagerService;
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
 * 酒庄信息管理 Controller
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@RestController
@RequestMapping("/chateauManager")
@Api(tags = "酒庄信息管理接口")
public class ChateauManagerController {

    @Autowired
    ChateauManagerService chateauManagerService;

    /**
     * 新增酒庄信息
     *
     * @param chateauManagerDto 酒庄信息Dto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping(value = "/addChateau")
    @ApiOperation(value = "新增酒庄信息", notes = "根据提交数据新增酒庄信息")
    public ResultDto addChateau(@RequestBody ChateauManagerDto chateauManagerDto) {
        log.info("新增酒庄信息");
        try {
            chateauManagerService.addChateau(chateauManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("新增酒庄失败 name = " + chateauManagerDto.getName(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 删除酒庄信息
     *
     * @param chateauId 酒庄ID
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping(value = "/deleteChateau")
    @ApiOperation(value = "删除酒庄信息", notes = "根据酒庄ID删除酒庄信息")
    public ResultDto deleteChateau(@RequestBody Long chateauId) {
        log.info("删除酒庄信息");
        try {
            if (BgdStringUtil.isStringEmpty(chateauId.toString())) {
                throw new ParameterException("删除酒庄信息酒庄ID不能为空");
            }
            chateauManagerService.deleteChateau(chateauId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("删除酒庄失败" + chateauId, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


    /**
     * 修改酒庄信息
     *
     * @param chateauManagerDto 酒庄Dto
     * @return
     */
    @ValidToken(type = 0)
    @PostMapping(value = "/updateChateau")
    @ApiOperation(value = "修改酒庄信息", notes = "根据提交酒庄信息修改酒庄信息")
    public ResultDto updateChateau(@RequestBody ChateauManagerDto chateauManagerDto) {
        log.info("修改酒庄信息");
        try {
            chateauManagerService.updateChateau(chateauManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("修改酒庄失败 name = " + chateauManagerDto.getName(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 分页查询酒庄信息
     *
     * @param countryName  国家名称
     * @param categoryName 主营大类
     * @param name         酒庄名称
     * @param status       状态  0停用 1启用
     * @param pageNo       页码
     * @return
     */
    @ValidToken(type = 0)
    @GetMapping(value = "/findAllChateau")
    @ApiOperation(value = "分页查询酒庄信息", notes = "根据查询条件分页查询酒庄信息")
    public ResultDto<PageDto<ChateauManagerDto>> findChateau(@RequestParam(required = false) String countryName,
                                                             @RequestParam(required = false) String categoryName,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam Integer status,
                                                             @RequestParam Integer pageNo) {
        log.info("分页查询酒庄信息");
        try {
            ChateauFindParam chateauFindParam = new ChateauFindParam();
            chateauFindParam.setCountryName(countryName);
            chateauFindParam.setCategoryName(categoryName);
            chateauFindParam.setName(name);
            chateauFindParam.setStatus(status);
            chateauFindParam.setPageNo(pageNo);
            PageDto<ChateauManagerDto> pageDto = chateauManagerService.findChateau(chateauFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询酒庄失败 chateauName = " + name + " 国家countryName = " + countryName +
                    " 大类categoryName = " + categoryName + " 状态status = " + status, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

}
