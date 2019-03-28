package com.bgd.admin.controller.productManager;

import com.bgd.admin.entity.ProImgManagerDto;
import com.bgd.admin.entity.ProductManagerDto;
import com.bgd.admin.entity.param.ProductFindParam;
import com.bgd.admin.service.ProImgManagerService;
import com.bgd.admin.service.ProductManagerService;
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

import java.util.List;

/**
 * 商品信息管理 Controller
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@RestController
@RequestMapping("/productManager")
@Api(tags = "商品信息管理接口")
public class ProductManagerController {

    @Autowired
    ProductManagerService productManagerService;

    @Autowired
    ProImgManagerService proImgManagerService;

    /**
     * 增改商品信息
     *
     * @param productDto 商品信息Dto
     */
    //@ValidToken(type = 0)
    @PostMapping(value = "/addOrUpdateProduct")
    @ApiOperation(value = "增改商品信息", notes = "增改商品信息")
    public ResultDto addOrUpdateProduct(@RequestBody ProductManagerDto productDto) {
        log.info("增改商品信息");
        try {
            productManagerService.addOrUpdateProduct(productDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("增改商品信息", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 删除商品信息
     *
     * @param productId 商品ID
     */
    //@ValidToken(type = 0)
    @PostMapping(value = "/deleteProduct")
    @ApiOperation(value = "删除商品", notes = "删除商品")
    public ResultDto deleteProduct(@RequestBody Long productId) {
        log.info("删除商品 id = " + productId);
        try {
            if (BgdStringUtil.isStringEmpty(productId.toString())) {
                throw new ParameterException("商品ID不能为空");
            }
            productManagerService.deleteProduct(productId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", null);
        } catch (BusinessException e) {
            log.error("删除商品失败 id = " + productId, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询商品信息
     *
     * @param categoryName 商品大类名称
     * @param countryName  国家名称
     * @param chateauName  酒庄名称
     * @param name         商品名称
     * @param pageNo       页码
     */
    //@ValidToken(type = 0)
    @GetMapping(value = "/findProduct")
    @ApiOperation(value = "查询商品信息", notes = "查询商品信息")
    public ResultDto<PageDto<ProductManagerDto>> findProduct(@RequestParam(required = false) String categoryName,
                                                             @RequestParam(required = false) String countryName,
                                                             @RequestParam(required = false) String chateauName,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam Integer pageNo) {
        log.info("查询商品信息 ProductName = " + name + " 国家countryName = " + countryName +
                " 大类categoryName = " + categoryName + " 酒庄名称chateauName = " + chateauName);
        try {
            ProductFindParam productFindParam = new ProductFindParam();
            productFindParam.setCategoryName(categoryName);
            productFindParam.setChateauName(chateauName);
            productFindParam.setCountryName(countryName);
            productFindParam.setName(name);
            productFindParam.setPageNo(pageNo);
            PageDto<ProductManagerDto> pageDto = productManagerService.findProduct(productFindParam);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", pageDto);
        } catch (BusinessException e) {
            log.info("查询商品信息失败 ProductName = " + name + " 国家countryName = " + countryName +
                    " 大类categoryName = " + categoryName + " 酒庄名称chateauName = " + chateauName, e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 查询商品及图片信息
     *
     * @param productId 商品Id
     */
    //@ValidToken(type = 0)
    @GetMapping("/findProAndImg")
    @ApiOperation(value = "查询商品及图片信息", notes = "根据商品Id查询商品及图片信息")
    public ResultDto<ProductManagerDto> findProImg(@RequestParam Long productId) {
        log.info("查询商品及图片信息");
        try {
            if (BgdStringUtil.isStringEmpty(productId.toString())) {
                throw new ParameterException("查询商品及图片信息productId不能为空" + productId.toString());
            }
            ProductManagerDto productManagerDto = productManagerService.findProAndImg(productId);
            return new ResultDto<>(ResultDto.CODE_SUCC, "查询商品及图片信息成功", productManagerDto);
        } catch (BusinessException e) {
            log.error("查询商品及图片信息失败", e);
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }


}

