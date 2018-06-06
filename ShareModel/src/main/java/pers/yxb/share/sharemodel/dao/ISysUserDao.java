package pers.yxb.share.sharemodel.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pers.yxb.share.sharemodel.entity.SysUser;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PC-HT on 2017/12/11.
 */
@Repository
public interface ISysUserDao extends JpaRepository<SysUser, Serializable> {

    SysUser findByUsername(String username);

    List<SysUser> findAll();
}
