package com.bgd.admin.dao;

import org.apache.ibatis.annotations.Mapper;

import com.bgd.admin.entity.SysUser;

@Mapper
public interface UserDao {

	SysUser findUserByNameAndPassword(SysUser sysUser);
}
