package com.bgd.admin.controller.productManager;

import com.bgd.admin.entity.CountryManagerDto;
import com.bgd.admin.entity.param.CountryFindParam;
import com.bgd.admin.service.CountryManagerService;
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
 * 国家信息管理 Controller
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@RestController
@RequestMapping("/countryManager")
@Api(tags = "国家信息管理接口")
public class CountryManagerController {

    @Autowired
    CountryManagerService countryManagerService;

    /**
     * 新增国家信息
     *
     * @param countryManagerDto 国家信息Dto
     * @return
     */
    //@ValidToken(type = 0)
    @PostMapping(value = "/addCountry")
    @ApiOperation(value = "新增国家信息", notes = "根据提交的数据新增国家信息")
    public ResultDto<CountryManagerDto> addCountry(@RequestBody CountryManagerDto countryManagerDto) {
        log.info("新增国家信息");
        try {
            countryManagerService.addCountry(countryManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("新增国家失败 name = " + countryManagerDto.getName(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 删除国家信息
     *
     * @param countryId 国家ID
     * @return
     */
    //@ValidToken(type = 0)
    @PostMapping(value = "/deleteCountry")
    @ApiOperation(value = "删除国家信息", notes = "根据国家ID删除国家信息")
    public ResultDto<CountryManagerDto> deleteCountry(@RequestBody Long countryId) {
        log.info("删除国家信息");
        try {
            if (BgdStringUtil.isStringEmpty(countryId.toString())) {
                throw new ParameterException("删除国家信息Id不能为空");
            }
            countryManagerService.deleteCountry(countryId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("删除国家失败 countryId = " + countryId, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 修改国家信息
     *
     * @param countryManagerDto 国家信息Dto
     * @return
     */
    //@ValidToken(type = 0)
    @PostMapping(value = "/updateCountry")
    @ApiOperation(value = "修改国家信息", notes = "根据提交的内容修改国家信息")
    public ResultDto updateCountry(@RequestBody CountryManagerDto countryManagerDto) {
        log.info("修改国家信息");
        try {
            countryManagerService.updateCountry(countryManagerDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("修改国家信息失败 name = " + countryManagerDto.getName(), e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 分页查询国家信息
     *
     * @param countryName 国家名称
     * @param pageNo      页码
     * @return
     */
    //@ValidToken(type = 0)
    @GetMapping(value = "/findCountry")
    @ApiOperation(value = "查询国家信息", notes = "根据国家名称查询国家信息")
    public ResultDto<PageDto<CountryManagerDto>> findCountry(@RequestParam(required = false) String countryName,
                                                             @RequestParam(required = false) Integer dataStatus,
                                                             @RequestParam Integer pageNo) {
        log.info("查询国家信息");
        try {
            CountryFindParam countryFindParam = new CountryFindParam();
            countryFindParam.setName(countryName);
            countryFindParam.setDataStatus(dataStatus);
            countryFindParam.setPageNo(pageNo);
            PageDto<CountryManagerDto> pageDto = countryManagerService.findCountry(countryFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.error("查询国家信息失败 name = " + countryName, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }
}
