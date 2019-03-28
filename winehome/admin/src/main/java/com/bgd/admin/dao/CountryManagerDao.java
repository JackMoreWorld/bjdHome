package com.bgd.admin.dao;

import com.bgd.admin.entity.CountryManagerDto;
import com.bgd.admin.entity.param.CountryFindParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 国家信息管理 Dao
 * @author Sunxk
 * @since 2019-3-18
 */
@Mapper
public interface CountryManagerDao {

	void addCountry(CountryManagerDto countryManagerDto);
	void deleteCountry(@Param("countryId") Long countryId);
	void updateCountry(CountryManagerDto countryManagerDto);

	Long countCountry(CountryFindParam countryFindParam);
	List<CountryManagerDto> findCountry(CountryFindParam countryFindParam);

}
