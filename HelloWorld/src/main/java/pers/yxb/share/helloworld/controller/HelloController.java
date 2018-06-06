package pers.yxb.share.helloworld.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-4-12 17:48
 */
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(Locale locale, Model model) {
        return "hello world";
    }

    @RequestMapping("/uid")
    String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return session.getId();
    }
}
