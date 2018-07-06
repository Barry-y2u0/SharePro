package pers.yxb.share.sharemodel.filter;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.yxb.share.sharemodel.exception.ShareException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-7-5 16:09
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    private final Logger log = LoggerFactory.getLogger(CustomFormAuthenticationFilter.class);
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
            /*String captcha = (String)((HttpServletRequest)request).getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
            if (captcha == null || !captcha.equalsIgnoreCase(token.getCaptcha())) {
                throw new ShareException("验证码错误");
            }*/
            Subject subject = getSubject(request, response);
            subject.login(token);//正常验证
            //到这里就算验证成功了,把用户信息放到session中
            ((HttpServletRequest) request).getSession().setAttribute("username",username);
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
