package com.mz.shiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.Filter;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by Frank on 2018/8/23.
 */
@Configuration
public class ShiroConfig {

  private static final String COOKIEID = "msid";
  private static final String MD5 = "md5";

  @Bean
  public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
    EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
    ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache-shiro.xml"));
    return ehCacheManagerFactoryBean;
  }

  @Bean
  public EhCacheCacheManager ehCacheCacheManager() {
    EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
    ehCacheCacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
    return ehCacheCacheManager;
  }

  @Bean
  public SpringCacheManagerWrapper springCacheManagerWrapper() {
    SpringCacheManagerWrapper springCacheManagerWrapper = new SpringCacheManagerWrapper();
    springCacheManagerWrapper.setCacheManager(ehCacheCacheManager());
    return springCacheManagerWrapper;
  }

  @Bean
  public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher() {
    RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(
        springCacheManagerWrapper());
    retryLimitHashedCredentialsMatcher.setHashAlgorithmName(MD5);
    retryLimitHashedCredentialsMatcher.setHashIterations(2);
    retryLimitHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
    return retryLimitHashedCredentialsMatcher;
  }

  @Bean
  public MyRealm myRealm() {
    MyRealm myRealm = new MyRealm();
    myRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
    myRealm.setCachingEnabled(true);
    myRealm.setCacheManager(springCacheManagerWrapper());
    myRealm.setAuthenticationCachingEnabled(true);
    myRealm.setAuthenticationCacheName("authenticationCache");
    myRealm.setAuthorizationCachingEnabled(true);
    myRealm.setAuthorizationCacheName("authorizationCache");
    return myRealm;
  }

  @Bean
  public JavaUuidSessionIdGenerator javaUuidSessionIdGenerator() {
    return new JavaUuidSessionIdGenerator();
  }

  @Bean
  public RedisSessionDAO redisSessionDAO() {
    RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
    redisSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
    redisSessionDAO.setSessionIdGenerator(javaUuidSessionIdGenerator());
    return redisSessionDAO;
  }

  @Bean
  public SimpleCookie simpleCookie() {
    SimpleCookie simpleCookie = new SimpleCookie();
    simpleCookie.setName(COOKIEID);
    simpleCookie.setDomain("");
    simpleCookie.setPath("/");
    simpleCookie.setHttpOnly(true);
    simpleCookie.setMaxAge(-1);
    return simpleCookie;
  }

  /*@Bean
  public MySessionValidationScheduler mySessionValidationScheduler() {
    MySessionValidationScheduler mySessionValidationScheduler = new MySessionValidationScheduler();
    mySessionValidationScheduler.setSessionManager(defaultWebSessionManager());
    mySessionValidationScheduler.setInterval(1800000);
    return mySessionValidationScheduler;
  }*/

  @Bean
  public DefaultWebSessionManager defaultWebSessionManager() {
    DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
    defaultWebSessionManager.setDeleteInvalidSessions(true);
    defaultWebSessionManager.setSessionDAO(redisSessionDAO());
    defaultWebSessionManager.setSessionValidationSchedulerEnabled(false);
    defaultWebSessionManager.setSessionIdCookieEnabled(true);
    defaultWebSessionManager.setSessionIdCookie(simpleCookie());
    return defaultWebSessionManager;
  }

  /*@Bean
  public MySessionManager mySessionManager() {
    MySessionManager mySessionManager = new MySessionManager();
    mySessionManager.setGlobalSessionTimeout(1800000);
    mySessionManager.setDeleteInvalidSessions(true);
    mySessionManager.setSessionDAO(redisSessionDAO());
    mySessionManager.setSessionIdCookieEnabled(true);
    mySessionManager.setSessionIdCookie(simpleCookie());
    //mySessionManager.setSessionValidationSchedulerEnabled(true);
    //mySessionManager.setSessionValidationScheduler(mySessionValidationScheduler());
    List<SessionListener> mySessionListeners = new ArrayList<>();
    mySessionListeners.add(new MySessionListener());
    mySessionManager.setSessionListeners(mySessionListeners);
    return mySessionManager;
  }*/

  @Bean
  public DefaultWebSecurityManager defaultWebSecurityManager() {
    DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
    defaultWebSecurityManager.setRealm(myRealm());
    defaultWebSecurityManager.setSessionManager(defaultWebSessionManager());
    return defaultWebSecurityManager;
  }

  public MyLogoutFilter myLogoutFilter() {
    MyLogoutFilter myLogoutFilter = new MyLogoutFilter();
    myLogoutFilter.setRedirectUrl(getUrl("client.login.url"));
    return myLogoutFilter;
  }

  public KickoutSessionControlFilter kickoutSessionControlFilter() {
    KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
    kickoutSessionControlFilter.setCacheManager(springCacheManagerWrapper());
    kickoutSessionControlFilter.setSessionManager(defaultWebSessionManager());
    kickoutSessionControlFilter.setKickoutAfter(false);
    kickoutSessionControlFilter.setMaxSession(50);
    kickoutSessionControlFilter.setKickoutUrl(getUrl("client.login.url"));
    return kickoutSessionControlFilter;
  }

  public CaptchaFormAuthenticationFilter captchaFormAuthenticationFilter() {
    return new CaptchaFormAuthenticationFilter();
  }

  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean() {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
    shiroFilterFactoryBean.setLoginUrl("/loginService.do");
    shiroFilterFactoryBean.setSuccessUrl(getUrl("client.success.url"));
    Map<String, Filter> filterMap = new LinkedHashMap<>();
    filterMap.put("authc", captchaFormAuthenticationFilter());
    filterMap.put("kickout", kickoutSessionControlFilter());
    filterMap.put("logout", myLogoutFilter());
    shiroFilterFactoryBean.setFilters(filterMap);
    Map<String, String> filterChainDefinitions = new LinkedHashMap<>();
    filterChainDefinitions.put("/remote/**", "anon");
    filterChainDefinitions.put("/dwr/**", "anon");
    filterChainDefinitions.put("/app/appconfig/dataByConfigKey", "anon");
    filterChainDefinitions.put("/kaptcha", "anon");
    filterChainDefinitions.put("/login.do", "anon");
    filterChainDefinitions.put("/authImage", "anon");
    filterChainDefinitions.put("/layout.do", "anon");
    filterChainDefinitions.put("/static/**", "anon");
    filterChainDefinitions.put("/js/**", "anon");
    filterChainDefinitions.put("/css/**", "anon");
    filterChainDefinitions.put("/images/**", "anon");
    filterChainDefinitions.put("/user/**", "anon");
    filterChainDefinitions.put("/getUser", "anon");
    filterChainDefinitions.put("/test*", "anon");
    filterChainDefinitions.put("/chat/**", "anon");
    filterChainDefinitions.put("/websocket", "anon");
    filterChainDefinitions.put("/chatsocket", "anon");
    filterChainDefinitions.put("/sdk", "anon");
    filterChainDefinitions.put("/pay/thirdpayconfig/*", "anon");
    filterChainDefinitions.put("/filefrontg/**", "anon");
    filterChainDefinitions.put("/settlement/findTotalMoney", "anon");
    filterChainDefinitions.put("/account/findAccountList", "anon");
    filterChainDefinitions.put("/account/integralImport", "anon");
    filterChainDefinitions.put("/**", "kickout,authc");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitions);
    return shiroFilterFactoryBean;
  }

  @Bean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
        new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
    return authorizationAttributeSourceAdvisor;
  }

  private String getUrl(String key) {
    Properties properties = new Properties();
    try {
      properties.load(new ClassPathResource("shiro-client.properties").getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return String.valueOf(properties.get(key));
  }
}
