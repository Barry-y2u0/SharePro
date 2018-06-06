package pers.share.yxb.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-5-28 17:47
 */
public class HelloController {
    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }
}
