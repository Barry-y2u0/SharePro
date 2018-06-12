package pers.yxb.share.sharemodel.service;

import pers.yxb.share.sharemodel.entity.SysUser;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-5-30 15:50
 */
public interface ISysUserService {
    SysUser findByUsername(String username);
}
