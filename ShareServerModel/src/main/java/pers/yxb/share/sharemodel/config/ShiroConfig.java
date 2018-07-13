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
import pers.yxb.share.sharemodel.filter.CustomFormAuthenticationFilter;
import pers.yxb.share.sharemodel.service.ISysUserService;

import javax.servlet.Filter;
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

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        CustomFormAuthenticationFilter formAuthenticationFilter = new CustomFormAuthenticationFilter();
        filters.put("authc",formAuthenticationFilter);
        shiroFilterFactoryBean.setFilters(filters);

        shiroFilterFactoryBean.setLoginUrl(loginUrl);// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setSuccessUrl(successUrl);// 登录成功后要跳转的连接
        shiroFilterFactoryBean.setUnauthorizedUrl(errorUrl);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        logger.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/ref/**", "anon");
        filterChainDefinitionMap.put("/defaultKaptcha", "anon");
        filterChainDefinitionMap.put("/login", "authc");
        filterChainDefinitionMap.put("/logout", "logout");

        filterChainDefinitionMap.put("/**", "authc");


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

    @Bean(name = "myShiroRealm")
    public MyShiroRealm myShiroRealm(EhCacheManager cacheManager) {
        MyShiroRealm realm = new MyShiroRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        realm.setCacheManager(cacheManager);
        return realm;
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
}