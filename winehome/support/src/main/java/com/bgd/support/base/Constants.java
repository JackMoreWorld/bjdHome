
package com.bgd.support.base;

import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: Constants
 * @Description: 公用常量
 * @author: JackMore
 * @date: 2019年2月25日 下午2:15:55
 */
@Component
public interface Constants {

	String PREFIX = "bgd:token:";
	String PREFIX_ADMIN = PREFIX + "admin:";
	String PREFIX_APP = PREFIX + "app:";

}