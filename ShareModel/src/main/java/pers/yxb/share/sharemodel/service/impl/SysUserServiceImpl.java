package pers.yxb.share.sharemodel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.yxb.share.sharemodel.config.datasource.TargetDataSource;
import pers.yxb.share.sharemodel.dao.ISysUserDao;
import pers.yxb.share.sharemodel.entity.SysUser;
import pers.yxb.share.sharemodel.service.ISysUserService;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-5-30 17:31
 */
@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private ISysUserDao userDao;

    //@TargetDataSource("ds1")
    public SysUser findByUsername(String username) {
        System.out.println("++++++++++++++++call method findByUsername");
        return userDao.findByUsername(username);
    }
}
