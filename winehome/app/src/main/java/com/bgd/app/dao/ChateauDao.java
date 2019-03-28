package com.bgd.app.dao;

import com.bgd.app.entity.MallChateauDto;
import com.bgd.app.entity.param.ChateauParam;
import com.bgd.support.base.PageParam;
import com.bgd.support.entity.MallCountryPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChateauDao {


    Long countCountry();

    List<MallCountryPo> pageCountry(PageParam param);

    List<MallChateauDto> listChateauByCategory(MallChateauDto chateau);


    Long countChateauByRank(ChateauParam param);

    List<MallChateauDto> pageChateauByRank(ChateauParam param);


    Long countChateauByStar(ChateauParam param);

    List<MallChateauDto> pageChateauByStar(ChateauParam param);


    Long countChateauByRankAndStar(ChateauParam param);

    List<MallChateauDto> pageChateauByRankAndStar(ChateauParam param);


    MallChateauDto findChateauOne(ChateauParam param);
}
