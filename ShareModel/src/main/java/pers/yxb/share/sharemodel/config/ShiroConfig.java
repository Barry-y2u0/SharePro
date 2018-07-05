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
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.util.WebUtils;
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

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc",myFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);

        shiroFilterFactoryBean.setLoginUrl(loginUrl);// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setSuccessUrl(successUrl);// 登录成功后要跳转的连接
        shiroFilterFactoryBean.setUnauthorizedUrl(errorUrl);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        logger.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/login", "authc");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/static/**", "anon");
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

    /**
     * 登录过滤器内部类
     */
    class MyFormAuthenticationFilter extends FormAuthenticationFilter{
        private final Logger log = LoggerFactory.getLogger(MyFormAuthenticationFilter.class);
        public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
        private String captchaParam = DEFAULT_CAPTCHA_PARAM;

        /**
         * 登录验证
         * @param request
         * @param response
         * @return
         * @throws Exception
         */
        @Override
        protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
            CaptchaUsernamePasswordToken token = createToken(request, response);
            String username = token.getUsername();
            try {
                /*图形验证码验证*/
                //session中的图形码字符串
                String captcha = (String)((HttpServletRequest)request).getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
                //比对
                if (captcha == null || !captcha.equalsIgnoreCase(token.getCaptcha())) {
                    throw new AuthenticationException("验证码错误");
                }
                Subject subject = getSubject(request, response);
                subject.login(token);//正常验证
                //到这里就算验证成功了,把用户信息放到session中
                ((HttpServletRequest) request).getSession().setAttribute("name",username);
                return onLoginSuccess(token, subject, request, response);
            }catch (AuthenticationException e) {
                return onLoginFailure(token, e, request, response);
            }
        }

        @Override
        protected CaptchaUsernamePasswordToken createToken(ServletRequest request, ServletResponse response) {
            String username = getUsername(request);
            String password = getPassword(request);
            String captcha = getCaptcha(request);
            boolean rememberMe = isRememberMe(request);
            String host = getHost(request);
            return new CaptchaUsernamePasswordToken(username, password, rememberMe, host, captcha);
        }



        public String getCaptchaParam() {
            return captchaParam;
        }

        public void setCaptchaParam(String captchaParam) {
            this.captchaParam = captchaParam;
        }

        protected String getCaptcha(ServletRequest request) {
            return WebUtils.getCleanParam(request, getCaptchaParam());
        }

        //保存异常对象到request
        @Override
        protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
            request.setAttribute(getFailureKeyAttribute(), ae);
        }

        /**
         * 自定义token
         */
        class CaptchaUsernamePasswordToken extends UsernamePasswordToken {
            private static final long serialVersionUID = 1L;

            private String captcha;//验证码字符串

            public CaptchaUsernamePasswordToken(String username, String password, boolean rememberMe, String host, String captcha) {
                super(username, password, rememberMe, host);
                this.captcha = captcha;
            }

            public String getCaptcha() {
                return captcha;
            }

            public void setCaptcha(String captcha) {
                this.captcha = captcha;
            }
        }
    }

    @Bean
    public MyFormAuthenticationFilter myFormAuthenticationFilter() {
        return new MyFormAuthenticationFilter();
    }
}