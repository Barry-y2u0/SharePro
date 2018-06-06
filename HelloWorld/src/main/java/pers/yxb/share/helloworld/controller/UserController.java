package pers.yxb.share.helloworld.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.yxb.share.helloworld.entity.User;
import pers.yxb.share.helloworld.repository.UserRepository;

import java.util.List;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-4-12 17:50
 */
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/getUser")
    @Cacheable(value="user-key")
    public User getUser() {
        User user=userRepository.findByUserName("aa");
        System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
        return user;
    }

    @RequestMapping("/getUsers")
    @Cacheable(value="key-Users")
    public List<User> getUsers() {
        List<User> users=userRepository.findAll();
        System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
        return users;
    }
}
