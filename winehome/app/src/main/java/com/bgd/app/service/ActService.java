
package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.dao.ActDao;
import com.bgd.app.entity.*;
import com.bgd.app.entity.param.ActProParam;
import com.bgd.app.entity.param.FirstNewProParam;
import com.bgd.app.entity.param.ProRankingRequestParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.entity.MallActivityPo;
import com.bgd.support.entity.MallBannerPo;
import com.bgd.support.entity.ProductCategoryPo;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdDateUtil;
import com.bgd.support.utils.RedisUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ActService {

    @Autowired
    ActDao actDao;
    @Autowired
    RedisUtil redisUtil;

    /**
     * @描述 获取首页轮播图
     * @创建人 JackMore
     * @创建时间 2019/3/18
     */
    public List<MallBannerPo> getMallBanner() throws BusinessException {
        try {
            List<MallBannerPo> bannerList = actDao.findBannerList();
            return bannerList;
        } catch (Exception e) {
            log.error("获取首页轮播图列表异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 获取商城首页活动
     * @创建人 JackMore
     * @创建时间 2019/3/11
     */
    public JSONObject getMallActivity() throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            List<MallActivityProductDto> xsg = Lists.newArrayList(); //限时购
            List<MallActivityProductDto> yhh = Lists.newArrayList(); //有好货
            List<MallActivityProductDto> tj = Lists.newArrayList(); //每日推荐
            Map<String, Long> map = Maps.newHashMap();

            String startTime = null;
            Long endTime = null;
            //获取所有活动
            List<MallActivityPo> all = actDao.findMallActivity();
            for (MallActivityPo act : all) {
                final Integer type = act.getType();
                Long actId = act.getId();
                map.put("actId", actId);

                if (AppConstant.ACT_TYPE.限时购 == type) {
                    map.put("limit", 2L);
                    xsg = findLimitActPro(map);
                    if (xsg != null && xsg.size() > 0) {
                        startTime = xsg.get(0).getStartTimeStr().substring(11);
                        String endTimeStr = xsg.get(0).getEndTimeStr();
                        endTime = BgdDateUtil.parse(endTimeStr, "yyyy-MM-dd HH:mm:ss").getTime();
                    }
                }
                if (AppConstant.ACT_TYPE.有好货 == type) {
                    map.put("limit", 2L);
                    yhh = findActPro(map);
                }
                if (AppConstant.ACT_TYPE.每日推荐 == type) {
                    map.put("limit", 1L);
                    tj = findActPro(map);
                }
            }
            if (endTime != null) {
                result.put("startTime", startTime);
                result.put("endTime", endTime);
            }
            result.put("xsg", xsg);
            result.put("yhh", yhh);
            result.put("tj", tj);
            return result;
        } catch (Exception e) {
            log.error("获取首页活动异常：", e);
            throw new BusinessException("获取首页活动异常");
        }
    }

    private List<MallActivityProductDto> findActPro(Map<String, Long> map) {
        return actDao.findHomeActPro(map);
    }

    //限时购单独写
    private List<MallActivityProductDto> findLimitActPro(Map<String, Long> map) {
        return actDao.findHomeLimitActPro(map);
    }


    /**
     * @描述 限时购详情页
     * @创建人 JackMore
     * @创建时间 2019/3/13
     */
    public JSONObject pageLimitPro(Long actId, Integer pageNo) {
        try {
            JSONObject result = new JSONObject();
            // 1.时间列表
            Map<String, Object> map = Maps.newHashMap();
            String nowDay = BgdDateUtil.format(new Date());
            map.put("actId", actId);
            map.put("actDay", nowDay);
            TimingDto now = new TimingDto();
            List<TimingDto> limitTiming = actDao.findLimitTiming(map);
            result.put("listTime", limitTiming);
            // 2.当前时间点下的抢购商品列表
            long nowTime = new Date().getTime();
            for (TimingDto index : limitTiming) {
                long startTime = BgdDateUtil.parse(index.getStartTime(), "yyyy-MM-dd HH:mm:ss").getTime();
                long endTime = BgdDateUtil.parse(index.getEndTime(), "yyyy-MM-dd HH:mm:ss").getTime();

                if (startTime <= nowTime && endTime >= nowTime) {
                    now = index;
                    result.put("startTime", now.getStartTime());
                    result.put("endTime", now.getEndTime());
                    break;
                }
            }
            ActProParam param = new ActProParam();
            param.setPageNo(pageNo);
            param.setActDay(nowDay);
            param.setActId(actId);
            param.setTiming(now.getStartTime());
            PageBean<LimitActProDto, ActProParam> page = new PageBean<LimitActProDto, ActProParam>(param) {
                @Override
                protected Long generateRowCount() throws BusinessException {
                    return actDao.countLimitActPro(param);
                }

                @Override
                protected List<LimitActProDto> generateBeanList() throws BusinessException {
                    return actDao.pageLimitActPro(param);
                }
            }.execute();

            List<LimitActProDto> pageData = page.getPageData();
            Long totalCount = page.getTotalCount();
            result.put("total", totalCount);
            result.put("actPros", pageData);
            return result;
        } catch (BusinessException e) {
            log.error("", e);
            throw e;
        }

    }


    /**
     * @描述 有好货商品列表查询
     * @创建人 JackMore
     * @创建时间 2019/3/14
     */
    public JSONObject listGoodPro(Long actId, Long categoryId, Integer pageNo) {
        JSONObject result = new JSONObject();
        // 获取有好货酒品类列表
        Map<String, Object> map = Maps.newHashMap();
        map.put("actId", actId);
        List<ProductCategoryPo> categorys = actDao.findCategoryList(map);
        result.put("categoryList", categorys);
        ProductCategoryPo _default = categorys.get(0);
        //获取酒列表

        ActProParam param = new ActProParam();
        param.setPageNo(pageNo);
        param.setActId(actId);
        if (categoryId == null) {
            param.setCategoryId(_default.getId());
        } else {
            param.setCategoryId(categoryId);
        }


        PageBean<GoodActProDto, ActProParam> page = new PageBean<GoodActProDto, ActProParam>(param) {
            @Override
            protected Long generateRowCount() throws BasicException {
                return actDao.countGoodActPro(param);
            }

            @Override
            protected List<GoodActProDto> generateBeanList() throws BasicException {
                return actDao.pageGoodActPro(param);
            }
        }.execute();
        List<GoodActProDto> pageData = page.getPageData();
        Long totalCount = page.getTotalCount();
        result.put("total", totalCount);
        result.put("actPros", pageData);
        return result;
    }


    /**
     * @描述 有好货商品喜欢数字改动
     * @创建人 JackMore
     * @创建时间 2019/3/13
     */
    @Async("taskExecutor")
    public void likeOrNot(LikeDto dto) {
        try {
            actDao.likeOrNot(dto);
        } catch (Exception e) {
            log.error("有好货点赞异常", e);
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
    public JSONObject queryProRanking(Long categoryId, Long pageNo) throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            // 查询首页商品排行榜列表分页数据
            List<ProductCategoryPo> categoryList = actDao.queryCategoryList();
            result.put("categoryList", categoryList);
            ProductCategoryPo categoryPo = categoryList.get(0);
            ProRankingRequestParam requestParam = new ProRankingRequestParam();
            requestParam.setPageNo(pageNo.hashCode());
            requestParam.setCategoryId(categoryId == null ? categoryPo.getId() : categoryId);
            PageBean<ProRankingDto, ProRankingRequestParam> pageBean = new PageBean<ProRankingDto,
                    ProRankingRequestParam>(requestParam) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return actDao.queryProRankingCount(requestParam);
                }

                @Override
                protected List<ProRankingDto> generateBeanList() throws BasicException {
                    return actDao.queryProRankingList(requestParam);
                }
            }.execute();
            List<ProRankingDto> beanList = pageBean.getPageData();
            result.put("total", pageBean.getTotalCount());
            result.put("list", beanList);
            return result;
        } catch (Exception e) {
            throw new BusinessException("查询首页商品排行榜列表分页数据异常");
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
    public JSONObject queryFirstNewPro(Long actId, Long pageNo) throws BusinessException {
        try {
            JSONObject result = new JSONObject();
            //活动图片
            result.put("actImg", actDao.findMallActivityById(actId).getImg());
            // 查询首页新品首发列表分页数据
            FirstNewProParam param = new FirstNewProParam();
            param.setPageNo(pageNo.hashCode());
            param.setActId(actId);
            PageBean<FirstNewProDto, FirstNewProParam> pageBean = new PageBean<FirstNewProDto, FirstNewProParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return actDao.queryFirstNewProCount(param);
                }

                @Override
                protected List<FirstNewProDto> generateBeanList() throws BasicException {
                    return actDao.queryFirstNewProList(param);
                }
            }.execute();
            List<FirstNewProDto> beanList = pageBean.getPageData();
            result.put("total", pageBean.getTotalCount());
            result.put("list", beanList);
            return result;
        } catch (Exception e) {
            throw new BusinessException("查询首页商品排行榜列表分页数据异常");
        }
    }

}