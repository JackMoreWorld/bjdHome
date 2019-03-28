package com.bgd.admin.dao;

import com.bgd.admin.entity.ProImgManagerDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品图片信息管理 Dao
 * @author Sunxk
 * @since 2019-3-25
 */
@Mapper
public interface ProImgManagerDao {

    int deleteProImg(@Param("productId")Long productId);

    int addProImg(@Param("list") List<ProImgManagerDto> list);

    List<ProImgManagerDto> findProImg(@Param("productId") Long productId);
}
