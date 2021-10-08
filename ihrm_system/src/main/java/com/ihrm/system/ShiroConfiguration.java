package com.ihrm.system;

import com.ihrm.common.shiro.realm.IhrmRealm;
import com.ihrm.common.shiro.session.CustomSessionManager;
import com.ihrm.system.shiro.realm.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    //1.创建realm
    @Bean
    public IhrmRealm getRealm() {
        return new UserRealm();
    }

    //创建安全管理器
    @Bean
    public SecurityManager getSecurityManager(IhrmRealm ihrmRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(ihrmRealm);

        //将自定义的会话管理注册到安全管理器中
        securityManager.setSessionManager(sessionManager());
        //将自定义的缓存管理器注册到安全管理器中
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    //3.配置shiro的过滤器工厂
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        factoryBean.setSecurityManager(securityManager);
        //通用配置  （跳转到登录页面，跳转到未授权页面）
        factoryBean.setLoginUrl("/autherror?code=1");//跳转url
        factoryBean.setUnauthorizedUrl("/autherror?code=2");//未授权页面
        //设置过滤器集合
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/sys/login", "anon");
        filterMap.put("/autherror", "anon");
        //注册
        filterMap.put("/**", "authc");
        //配置权限
        factoryBean.setFilterChainDefinitionMap(filterMap);
        return factoryBean;
    }


    //    @Value("${spring.redis.host}")
    private String host = "127.0.0.1";

    //    @Value("${spring.redis.port}")
    private int port = 6379;

    //1.redis的控制器，操作redis
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        return redisManager;
    }

    //2.sessionDao
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        return sessionDAO;
    }

    //3.会话管理器
    public DefaultWebSessionManager sessionManager() {
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        CustomSessionManager sessionManager = new CustomSessionManager();
        //配置
        sessionManager.setSessionDAO(redisSessionDAO());
        //禁用cookie
        sessionManager.setSessionIdCookieEnabled(false);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
//        //配置session
//        sessionManager.setDeleteInvalidSessions(true);
//        sessionManager.setSessionValidationSchedulerEnabled(true);
//        //10 分钟检查一次失效 session
//        sessionManager.setSessionValidationInterval(10 * 60 * 1000);
        return sessionManager;
    }

    //4.缓存管理器
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorization = new AuthorizationAttributeSourceAdvisor();
        authorization.setSecurityManager(securityManager);
        return authorization;
    }

}
