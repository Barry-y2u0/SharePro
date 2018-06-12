package pers.yxb.share.sharemodel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-5-30 17:32
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @RequestMapping("/userList")
    public String userList() {
        return "userList";
    }


}
