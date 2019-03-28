package com.bgd.app.controller;


import com.bgd.app.entity.VipCartDto;
import com.bgd.app.entity.VipInformationDto;
import com.bgd.app.entity.param.CartRequestParam;
import com.bgd.app.service.CartService;
import com.bgd.app.util.SessionUserUtils;
import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.PageDto;
import com.bgd.support.base.ResultDto;
import com.bgd.support.entity.VipCartPo;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.exception.ParameterException;
import com.bgd.support.utils.BgdStringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 购物车模块 controller
 *
 * @author wangguoqing
 * @version 1.0
 * @since 2019-03-11
 */
@Slf4j
@RestController
@RequestMapping("/cart")
@Api(tags = "购物车模块接口")
public class CartController {


    @Autowired
    CartService cartService;

    @Autowired
    SessionUserUtils userUtils;


    /**
     * 查询购物车列表分页数据
     *
     * @param token  用户登录标识
     * @param pageNo 页码
     * @return 购物车列表分页数据
     */
    @GetMapping(value = "/getCartListByPage/{pageNo}")
    @ValidToken(type = 1)
    @ApiOperation(value = "查询购物车列表分页数据", notes = "查询购物车列表分页数据")
    public ResultDto<PageDto<VipCartDto>> cartListResultByPage(@PathVariable Integer pageNo, @RequestHeader String token) {
        log.info("查询购物车列表分页数据");
        try {
            if (null == pageNo) {
                throw new ParameterException("页码不能为空");
            }
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            CartRequestParam paramsDto = new CartRequestParam();
            paramsDto.setVipId(informationDto.getId());
            paramsDto.setPageNo(pageNo);
            //查询购物车列表分页数据
            PageDto<VipCartDto> result = cartService.queryCartListByPage(paramsDto);
            return new ResultDto<>(ResultDto.CODE_SUCC, "", result);
        } catch (BusinessException e) {
            log.info("查询购物车列表系统异常");
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 根据购物车id删除购物车信息
     *
     * @param token   用户登录标识
     * @param cartIds 购物车id集合
     */
    @PostMapping(value = "/delCartById/{cartIds}")
    @ValidToken(type = 1)
    @ApiOperation(value = "根据购物车id删除购物车信息", notes = "根据购物车id删除购物车信息")
    public ResultDto delCartById(@RequestHeader String token, @PathVariable String cartIds) {
        log.info("根据购物车id删除购物车信息");
        try {
            if (BgdStringUtil.isStringEmpty(cartIds)) {
                throw new ParameterException("购物车id不能为空");
            }
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            cartService.delCartById(informationDto.getId(), cartIds);
            return new ResultDto<>(ResultDto.CODE_SUCC, "删除成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            log.info("根据购物车id删除购物车信息异常");
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 加入购物车
     *
     * @param token     用户登录标识
     * @param vipCartPo 购物车 po
     */
    @PostMapping(value = "/addCart")
    @ValidToken(type = 1)
    @ApiOperation(value = "加入购物车", notes = "加入购物车")
    public ResultDto addCart(@RequestHeader String token, @RequestBody VipCartPo vipCartPo) {
        log.info("加入购物车");
        try {
            if (BgdStringUtil.isStringEmpty(vipCartPo.getProductId().toString())) {
                throw new ParameterException("商品id不能为空");
            }
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            vipCartPo.setVipId(informationDto.getId());
            cartService.addCart(vipCartPo);
            return new ResultDto<>(ResultDto.CODE_SUCC, "加入成功", null);
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            log.info("加入购物车异常");
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }

    /**
     * 购物车数量加、减
     *
     * @param token  用户登录标识
     * @param cartId 购物车id
     * @param type   0 加 1减
     * @return
     */
    @PostMapping(value = "/updCartProCount/{cartId}/{type}")
    @ApiOperation(value = "购物车商品购买数量更新", notes = "购物车商品购买数量更新")
    public ResultDto updCartProCount(@RequestHeader String token, @PathVariable Long cartId,
                                     @PathVariable Long type) {
        log.info("购物车商品购买数量更新");
        try {
            if (BgdStringUtil.isStringEmpty(String.valueOf(cartId))) {
                throw new ParameterException("购物车id不能为空");
            }
            if (BgdStringUtil.isStringEmpty(String.valueOf(type))) {
                throw new ParameterException("type不能为空");
            }
            //获取redis缓存里的用户信息
            VipInformationDto informationDto = userUtils.getUserByRedis(token);
            return new ResultDto<>(ResultDto.CODE_SUCC, "更新成功",
                    cartService.updCartProCount(cartId, type, informationDto.getId()));
        } catch (BusinessException e) {
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("购物车商品购买数量更新异常");
            return new ResultDto<>(ResultDto.CODE_BUZ_ERROR, "业务异常", null);
        }
    }
}
