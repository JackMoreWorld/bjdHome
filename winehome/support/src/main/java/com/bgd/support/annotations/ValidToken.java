package com.bgd.support.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: ValidToken
 * @Description: 权限验证
 * @author: JackMore
 * @date: 2019年2月25日 下午2:04:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidToken {
    int type();
}
