package com.bgd.admin.dao;

import com.bgd.admin.entity.VipManagerDto;
import com.bgd.admin.entity.param.VipFindParam;
import javafx.geometry.VPos;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VipManagerDao {

    Long countVip(VipFindParam vipFindParam);
    List<VipManagerDto> findVip(VipFindParam vipFindParam);

}
