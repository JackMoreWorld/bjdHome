
package com.bgd.app.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.app.conf.AppConstant;
import com.bgd.app.dao.CommunityDao;
import com.bgd.app.entity.CommunityArticleDto;
import com.bgd.app.entity.CommunityFocusDto;
import com.bgd.app.entity.param.CommunityParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.entity.CommunityArticlePo;
import com.bgd.support.entity.CommunityFocusPo;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.RedisUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CommunityService {

    @Autowired
    CommunityDao communityDao;
    @Autowired
    RedisUtil redisUtil;

    /**
     * @描述 社区关注
     * @创建人 JackMore
     * @创建时间 2019/3/15
     */
    public void focus(Long vipId, Long focusId, Integer type) throws BusinessException {
        try {
            CommunityFocusPo po = communityDao.findFocusById(vipId);
            List<Long> focus = new ArrayList<>();
            focus.add(focusId);
            if (po == null) {
                communityDao.saveFocus(new CommunityFocusPo(vipId, JSONObject.toJSONString(focus)));
            } else {
                String focusStr = po.getFocus();
                List<Long> longs = JSONObject.parseArray(focusStr, Long.class);
                if (AppConstant.VIP_FOCUS.UNDO.equals(type)) {//取关
                    Iterator<Long> iterator = longs.iterator();
                    while (iterator.hasNext()) {
                        Long next = iterator.next();
                        if (next.equals(focusId)) {
                            iterator.remove();
                        }
                    }
                    po.setFocus(JSONObject.toJSONString(longs));
                    communityDao.updateFocus(po);
                } else {
                    longs.add(focusId);
                    TreeSet<Long> sets = Sets.newTreeSet(longs);
                    ArrayList<Long> focusList = Lists.newArrayList(sets);
                    po.setFocus(JSONObject.toJSONString(focusList));
                    communityDao.updateFocus(po);
                }

            }
        } catch (Exception e) {
            log.error("社区关注好友异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 获取社区所有帖子
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    public PageDto<CommunityArticleDto> listAllArticle(CommunityParam param) throws BusinessException {
        try {
            PageDto<CommunityArticleDto> communityArticleDtoPageDto = listArticle(param);
            return communityArticleDtoPageDto;
        } catch (Exception e) {
            log.error("查询社区帖子异常", e);
            throw new BusinessException();
        }
    }

    /**
     * @描述 获取关注人发帖
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    public PageDto<CommunityArticleDto> findFocusArticle(CommunityParam param) throws BusinessException {
        try {
            CommunityFocusPo po = communityDao.findFocusById(param.getVipId());
            if (po == null) return null;
            String focusStr = po.getFocus();
            if (focusStr == null) return null;
            List<Long> vipIds = JSONObject.parseArray(focusStr, Long.class);
            param.setFocus(vipIds);
            param.setTypeStr(new StringJoiner(",").add(AppConstant.ARTICLE_TYPE.发帖.toString()).add(AppConstant.ARTICLE_TYPE.转载.toString()).toString());
            PageDto<CommunityArticleDto> result = new PageDto<>();
            PageBean<CommunityArticleDto, CommunityParam> page = new PageBean<CommunityArticleDto, CommunityParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return communityDao.countFocusArticle(param);
                }

                @Override
                protected List<CommunityArticleDto> generateBeanList() throws BasicException {
                    return communityDao.pageFocusArticle(param);
                }
            }.execute();
            List<CommunityArticleDto> pageData = page.getPageData();
            Long totalCount = page.getTotalCount();
            result.setTotal(totalCount);
            result.setList(pageData);
            return result;
        } catch (Exception e) {
            log.error("查询关注发帖异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 查询帖子详情
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    public CommunityArticleDto findArticleDetail(Long id) throws BusinessException {
        try {
            CommunityArticleDto article = communityDao.findArticleOne(id);
            return article;
        } catch (Exception e) {
            log.error("查询发帖详情异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 跟帖
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    public void comment(CommunityArticlePo po) throws BusinessException {
        try {
            po.setType(AppConstant.ARTICLE_TYPE.跟帖);
            po.setCreateTime(new Date());
            communityDao.saveArticle(po);
        } catch (Exception e) {
            log.error("跟帖异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 给帖子点赞
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    @Async("taskExecutor")
    public void likeOrNot(Map<String, Object> map) throws BusinessException {
        try {
            communityDao.updateCommunity(map);
        } catch (Exception e) {
            log.error("点赞异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 获取我的发帖
     * @创建人 JackMore
     * @创建时间 2019/3/25
     */
    public PageDto<CommunityArticleDto> listArticle(CommunityParam param) throws BusinessException {
        try {

            PageDto<CommunityArticleDto> result = new PageDto<>();
            PageBean<CommunityArticleDto, CommunityParam> page = new PageBean<CommunityArticleDto, CommunityParam>(param) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return communityDao.countAllArticle(param);
                }

                @Override
                protected List<CommunityArticleDto> generateBeanList() throws BasicException {
                    return communityDao.pageAllArticle(param);
                }
            }.execute();
            List<CommunityArticleDto> pageData = page.getPageData();
            Long totalCount = page.getTotalCount();
            result.setTotal(totalCount);
            result.setList(pageData);
            return result;
        } catch (Exception e) {
            log.error("查询关注发帖异常", e);
            throw new BusinessException();
        }
    }


    /**
     * @描述 获取我的关注列表
     * @创建人 JackMore
     * @创建时间 2019/3/25
     */
    public List<CommunityFocusDto> findMyFocus(Long vipId) throws BusinessException {
        try {
            CommunityFocusPo po = communityDao.findFocusById(vipId);
            String focusStr = po.getFocus();
            if (focusStr == null) {
                return null;
            }
            List<Long> vipIds = JSONObject.parseArray(focusStr, Long.class);
            List<CommunityFocusDto> communityFocusPos = communityDao.pageMyFocus(vipIds);
            return communityFocusPos;
        } catch (Exception e) {
            log.error("查询关注人异常", e);
            throw new BusinessException();
        }
    }

}