package pers.yxb.share.helloworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-4-11 9:11
 */
@RestController
public class CustomerController {

    @RequestMapping("/yu")
    public String index() {
        return "HelloWorld";
    }
}
