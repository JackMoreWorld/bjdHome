package com.bgd.app.dao;

import com.bgd.app.entity.CommunityArticleDto;
import com.bgd.app.entity.CommunityFocusDto;
import com.bgd.app.entity.param.CommunityParam;
import com.bgd.support.entity.CommunityArticlePo;
import com.bgd.support.entity.CommunityFocusPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommunityDao {

    CommunityFocusPo findFocusById(@Param("vipId") Long vipId);

    int updateFocus(CommunityFocusPo po);

    int saveFocus(CommunityFocusPo po);

    Long countAllArticle(CommunityParam param);

    List<CommunityArticleDto> pageAllArticle(CommunityParam param);

    Long countFocusArticle(CommunityParam param);

    List<CommunityArticleDto> pageFocusArticle(CommunityParam param);

    CommunityArticleDto findArticleOne(@Param("id") Long id);

    int saveArticle(CommunityArticlePo po);

    int updateCommunity(Map<String, Object> map);

    List<CommunityFocusDto> pageMyFocus(List<Long> vipIds);

}
