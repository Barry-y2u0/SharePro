package pers.yxb.share.sharemodel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-5-30 11:12
 */
@Controller
public class IndexController extends BaseController {

    @RequestMapping("/")
    public String index() {
        return "frontstage/home";
    }

}
