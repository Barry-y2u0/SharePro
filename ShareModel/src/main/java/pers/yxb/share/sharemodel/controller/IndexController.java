package pers.yxb.share.sharemodel.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import pers.yxb.share.sharemodel.exception.ShareException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-5-30 11:12
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private Producer kaptchaProducer;

    @GetMapping("/login")
    public String home() {
        return "frontstage/home";
    }

    /**
     * 获取图片验证码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/defaultKaptcha")
    @ResponseBody
    public ModelAndView defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String captcha = kaptchaProducer.createText();
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, captcha);
        BufferedImage bi = kaptchaProducer.createImage(captcha);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    /**
     * 登录被自定义拦截器CustomFormAuthenticationFilter处理过后调用此方法
     * @param request
     * @param map
     * @return
     */
    @PostMapping(value = "/login")
    public String login(HttpServletRequest request, Map<String, Object> map) {
        String exception = (String) request.getAttribute("shiroLoginFailure");
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                msg = "帐号不存在";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                msg = "密码不正确";
            } else if (ShareException.class.getName().equals(exception)){
                msg = "验证码错误";
            } else {
                msg = "else >> " + exception;
            }
        }
        System.out.println("++++++++++++++++" + msg);
        map.put("msg", msg);
        return "frontstage/home";
    }

    @RequestMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    /**
     * 登录成功后跳转到后台首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "backstage/main";
    }

    /**
     * 403
     * @return
     */
    @RequestMapping("/403")
    public String error() {
        return "403";
    }
}