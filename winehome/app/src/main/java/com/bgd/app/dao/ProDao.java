package com.bgd.app.dao;

import com.bgd.app.entity.*;
import com.bgd.app.entity.param.ProParam;
import com.bgd.support.entity.ProductEvaluatPo;
import com.bgd.support.entity.ProductInformationPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProDao {

    ProductDto findProDetailByMap(Map<String, Object> map);

    String findNewCapacity(@Param("proId") Long proId);

    Long countProductEvaByObj(ProParam param);

    List<ProductEvaluatDto> pageProductEvaByObj(ProParam param);

    MallChateauDto findChateauByProId(@Param("proId") Long proId);

    Long countProByChateauId(@Param("chateauId") Long chateauId);

    Long countProByObj(ProParam param);

    List<ProductInformationPo> pageProByObj(ProParam param);

    int saveProEva(ProductEvaluatPo po);

    Long countFilterPro(ProParam param);

    List<ProductDto> pageFilterPro(ProParam param);

}
