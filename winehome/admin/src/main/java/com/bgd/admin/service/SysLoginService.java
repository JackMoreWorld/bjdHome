
package com.bgd.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.bgd.admin.dao.UserDao;
import com.bgd.admin.entity.SysUser;
import com.bgd.support.base.Constants;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.BgdStringUtil;
import com.bgd.support.utils.PasswordUtil;
import com.bgd.support.utils.RandomUtil;
import com.bgd.support.utils.RedisUtil;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Api(tags = "管理员登陆")
public class SysLoginService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RedisUtil redis;

	/**
	 * 
	 * @Title: login   
	 * @Description: 平台账户登录
	 * @param: @param sysUser
	 * @param: @return
	 * @param: @throws BusinessException      
	 * @return: String      
	 * @throws
	 */
	public String login(SysUser sysUser) throws BusinessException {
		log.info(sysUser.getName() + ",登录平台");
		if (BgdStringUtil.isEmpty(sysUser.getName())) {
			log.error("账户名不能为空");
			throw new BusinessException("账户名不能为空");
		}
		if (BgdStringUtil.isEmpty(sysUser.getPassword())) {
			throw new BusinessException("密码不能为空");
		}
		sysUser.setPassword(PasswordUtil.MD5Salt(sysUser.getPassword()));
		SysUser user = userDao.findUserByNameAndPassword(sysUser);
		if (user == null) {
			log.error("用户名或密码不正确！");
			throw new BusinessException("用户名或密码不正确！");
		}
		String token = RandomUtil.getRandomString(32);
		redis.set(Constants.PREFIX_ADMIN + token, JSONObject.toJSONString(user));
		return token;
	}

	/**
	 * 
	 * @Title: logout   
	 * @Description: 账户登出 
	 * @param: @param token
	 * @param: @throws BusinessException      
	 * @return: void      
	 * @throws
	 */
	public void logout(String token) throws BusinessException {
		if (!redis.exists(Constants.PREFIX_ADMIN + token)) {
			throw new BusinessException("登出失败,无此token");
		}
		SysUser sysUser = JSONObject.parseObject(redis.get(Constants.PREFIX_ADMIN + token), SysUser.class);
		log.info(sysUser.getName() + "--退出平台");
		redis.remove(Constants.PREFIX_ADMIN + token);
	}

}