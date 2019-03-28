package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.conf.ManagerConstant;
import com.bgd.admin.dao.ArticleManagerDao;
import com.bgd.admin.entity.ArticleManagerDto;
import com.bgd.admin.entity.param.ArticleFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdStringUtil;
import com.bgd.support.utils.RandomUtil;
import com.bgd.support.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 论坛帖子信息管理 Service
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Slf4j
@Service
public class ArticleManagerService {

    @Autowired
    ArticleManagerDao articleManagerDao;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 处理论坛帖子信息
     *
     * @param articleManagerDto 帖子Dto
     * @throws BusinessException
     */
    public void dealArticle(ArticleManagerDto articleManagerDto) throws BusinessException {

        try {
            Integer dealType = articleManagerDto.getDealType();
            if (ManagerConstant.OPERA_TYPE.CREATE.equals(dealType)) {
                log.info("新增文章 title = " + articleManagerDto.getTitle() + "文章vipName  = " + articleManagerDto.getVipName());
                String random = RandomUtil.getRandomString(5);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                //帖子编号生成规则五位随机数拼接当前年月日
                articleManagerDto.setNo(random + sdf.format(new Date()));
                articleManagerDto.setCreateTime(new Date());
                articleManagerDao.addArticle(articleManagerDto);
                redisUtil.set(ManagerConstant.REDIS.ARTICLE + articleManagerDto.getId(), JSONObject.toJSONString(articleManagerDto));
            }
            if (ManagerConstant.OPERA_TYPE.DEL.equals(dealType)) {
                if (BgdStringUtil.isStringEmpty(articleManagerDto.getId())) {
                    throw new BusinessException("删除帖子信息ID不能为空");
                }
                log.info("删除文章 title = " + articleManagerDto.getTitle());
                articleManagerDao.deleteArticle(articleManagerDto.getId());
                redisUtil.remove(ManagerConstant.REDIS.ARTICLE + articleManagerDto.getId());
            }
            if (ManagerConstant.OPERA_TYPE.UPDATE.equals(dealType)) {
                log.info("修改文章 title = " + articleManagerDto.getTitle());
                articleManagerDto.setUpdateTime(new Date());
                articleManagerDao.updateArticle(articleManagerDto);
                redisUtil.set(ManagerConstant.REDIS.ARTICLE + articleManagerDto.getId(), JSONObject.toJSONString(articleManagerDto));
            }
        } catch (Exception e) {
            log.error("处理文章信息异常 dealType = " + articleManagerDto.getDealType(), e);
            throw new BusinessException("处理文章信息异常 dealType = " + articleManagerDto.getDealType());
        }

    }

    /**
     * 分页查询帖子信息
     *
     * @param articleFindParam 帖子信息查询条件
     * @return
     * @throws BusinessException
     */
    public PageDto<ArticleManagerDto> findArticle(ArticleFindParam articleFindParam) throws BusinessException {
        log.info("查询文章标题 title = " + articleFindParam.getTitle() + "文章 vipName = " + articleFindParam.getVipName());
        try {
            PageDto<ArticleManagerDto> pageDto = new PageDto<>();
            PageBean<ArticleManagerDto, ArticleFindParam> pageBean =
                    new PageBean<ArticleManagerDto, ArticleFindParam>(articleFindParam) {
                @Override
                protected Long generateRowCount() throws BusinessException {
                    return articleManagerDao.countArticle(articleFindParam);
                }

                @Override
                protected List<ArticleManagerDto> generateBeanList() throws BusinessException {
                    return articleManagerDao.findArticle(articleFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<ArticleManagerDto> articleManagerDtoList = pageBean.getPageData();
            pageDto.setTotal(total);
            pageDto.setList(articleManagerDtoList);
            return pageDto;
        } catch (Exception e) {
            log.error("查询文章信息异常title = " + articleFindParam.getTitle()
                    + "文章 vipName = " + articleFindParam.getVipName(), e);
            throw new BusinessException("查询文章信息异常");
        }
    }

}
