package com.ihrm.common.interceptor;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.excetpion.CommonException;
import com.ihrm.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//将实例换JwtInterceptor类交给Spring容器管理
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 1.拦截器获取token数据
         */
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")) {
            //替换"Bearer "字符串为""
            String token = authorization.replace("Bearer ", "");
            //获取Claims对象 判断不为null并存放到 存放到request域中
            Claims claims = jwtUtils.parseJwt(token);
            if (claims != null) {
                //通过claims获取用户的可访问API权限字符串
                String apis = (String) claims.get("apis");
                //通过handler判断权限名称
                HandlerMethod method = (HandlerMethod) handler;
                //获取到改方法上对应的注解
                RequestMapping methodAnnotation = method.getMethodAnnotation(RequestMapping.class);
                String name = methodAnnotation.name();
                if (apis.contains(name)) {
                    request.setAttribute("user_claims", claims);
                    return true;
                } else {
                    throw new CommonException(ResultCode.UNAUTHORISE);
                }
            }
        }
        throw new CommonException(ResultCode.UNAUTHENTICATED);
    }
}
