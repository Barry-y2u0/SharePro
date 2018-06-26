package pers.yxb.share.sharemodel.config;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import pers.yxb.share.sharemodel.entity.SysOrganization;
import pers.yxb.share.sharemodel.entity.SysRole;
import pers.yxb.share.sharemodel.entity.SysUser;
import pers.yxb.share.sharemodel.service.ISysUserService;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by PC-HT on 2017/12/11.
 */
@Configuration
@Order(1)
public class ShiroConfig {
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Value("${shiro.loginUrl}")
    private String loginUrl;

    @Value("${shiro.successUrl}")
    private String successUrl;

    @Value("${shiro.errorUrl}")
    private String errorUrl;

    /**
     * Shiro入口过滤器
     * @param securityManager
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);// 必须设置 SecurityManager
        logger.info("loginUrl:" + loginUrl);
        shiroFilterFactoryBean.setLoginUrl(loginUrl);// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setSuccessUrl(successUrl);// 登录成功后要跳转的连接
        shiroFilterFactoryBean.setUnauthorizedUrl(errorUrl);

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc",myFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        logger.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/login", "authc");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/**", "anon");//anon 可以理解为不拦截

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    class MyShiroRealm extends JdbcRealm {

        @Autowired
        private ISysUserService userService;

        /**
         * 登录认证
         */
        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
            UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;
            SysUser user=userService.findByUsername(token.getUsername());
            if(user!=null){
                return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getCredentialSalt()), getName());
            }
            return null;
        }

        /**
         * 权限认证
         */
        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
            logger.info("##################执行Shiro权限认证##################");
            //获取当前登录输入的用户名，等价于(String) principalCollection.fromRealm(getName()).iterator().next();
            String loginName = (String)super.getAvailablePrincipal(principalCollection);
            SysUser user=userService.findByUsername(loginName);
            if(user!=null){
                SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();//权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
                info.setRoles(user.getRolesName());//用户的角色集合


                info.addStringPermissions(user.getPermissionNames());

                Collection<SysRole> roles=user.getRoles();//用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
                for (SysRole role : roles) {
                    info.addStringPermissions(role.getPermissionNames());
                }

                Collection<SysOrganization> orgs=user.getOrgs();//用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
                for (SysOrganization org : orgs) {
                    info.addStringPermissions(org.getPermissionNames());
                }

                return info;
            }
            return null;
        }
    }

    @Bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    /**
     * realm加密
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("SHA-1");
        matcher.setHashIterations(1024);
        matcher.setStoredCredentialsHexEncoded(true);
        return matcher;
    }

    @Bean(name = "myShiroRealm")
    public MyShiroRealm myShiroRealm(EhCacheManager cacheManager) {
        MyShiroRealm realm = new MyShiroRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        realm.setCacheManager(cacheManager);
        return realm;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroRealm myShiroRealm) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setCacheManager(getEhCacheManager());//必须放在首位,否则出错
        dwsm.setRealm(myShiroRealm);

        return dwsm;
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public MyFormAuthenticationFilter myFormAuthenticationFilter() {
        return new MyFormAuthenticationFilter();
    }

    class MyFormAuthenticationFilter extends FormAuthenticationFilter{
        private final Logger log = LoggerFactory.getLogger(MyFormAuthenticationFilter.class);
        @Override
        protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
            // 判断是否是登陆请求
            if (isLoginRequest(request, response)) {
                if (isLoginSubmission(request, response)) {
                    if (log.isTraceEnabled()) {
                        log.trace("Login submission detected.  Attempting to execute login.");
                    }

                    if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {// 不是ajax请求
                        String vcode = request.getParameter("vcode");
                        HttpServletRequest httpservletrequest = (HttpServletRequest) request;
                        String vvcode = (String) httpservletrequest.getSession().getAttribute("SESSION_CODE");
                        if (vvcode == null || "".equals(vvcode)  || !vvcode.equals(vcode)) {
                            response.setCharacterEncoding("UTF-8");
                            PrintWriter out = response.getWriter();
                            out.println("{success:false,message:'验证码错误'}");
                            out.flush();
                            out.close();
                            return false;
                        }
                    }
                    return executeLogin(request, response);
                } else {
                    if (log.isTraceEnabled()) {
                        log.trace("Login page view.");
                    }
                    return true;
                }
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + getLoginUrl() + "]");
                }

                // 如果不是ajax请求，则返回到登陆
                if (!"XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {// 不是ajax请求
                    saveRequestAndRedirectToLogin(request, response);
                } else {
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    out.print("timeout");
                    out.flush();
                    out.close();
                }
                return false;
            }
        }
    }
}