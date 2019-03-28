package com.bgd.support.interceptor;


import com.bgd.support.annotations.ValidToken;
import com.bgd.support.base.Constants;
import com.bgd.support.exception.BusinessException;
import com.bgd.support.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();


        ValidToken validToken = method.getAnnotation(ValidToken.class);
        // 有 @ValidToken 注解，需要token认证
        if (validToken != null) {
            try {
                String authorization = request.getHeader("token");
                int type = validToken.type();
                String prefix = "";
                if (type == 0) { //平台
                    prefix = Constants.PREFIX_ADMIN;
                } else { // app
                    prefix = Constants.PREFIX_APP;
                }


                if (authorization == null || !redisUtil.exists(prefix + authorization)) {
                    throw new BusinessException("token失效或丢失");
                }
            } catch (BusinessException e) {
                throw new BusinessException("token失效或丢失");
            }
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}
