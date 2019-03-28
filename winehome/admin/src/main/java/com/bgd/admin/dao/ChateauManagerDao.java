package com.bgd.admin.dao;

import com.bgd.admin.entity.ChateauManagerDto;
import com.bgd.admin.entity.param.ChateauFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 酒庄信息管理 Dao
 *
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface ChateauManagerDao {

    void addChateau(ChateauManagerDto chateauManagerDto);

    void deleteChateau(@Param("chateauId") Long chateauId);

    void updateChateau(ChateauManagerDto chateauManagerDto);

    Long countChateau(ChateauFindParam chateauFindParam);
    List<ChateauManagerDto> findChateau(ChateauFindParam chateauFindParam);

}
