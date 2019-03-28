package com.bgd.admin.dao;

import com.bgd.admin.entity.ArticleManagerDto;
import com.bgd.admin.entity.param.ArticleFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 论坛帖子信息管理Dao
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface ArticleManagerDao {

    void addArticle(ArticleManagerDto articleManagerDto);

    void deleteArticle(@Param("articleId") Long articleId);

    void updateArticle(ArticleManagerDto articleManagerDto);

    Long countArticle(ArticleFindParam articleFindParam);
    List<ArticleManagerDto> findArticle(ArticleFindParam articleFindParam);

}
