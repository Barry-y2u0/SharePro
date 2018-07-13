package pers.yxb.share.sharemodel.entity;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by PC-HT on 2017/12/8.
 */
@Entity
@Table(name = "sys_role", schema = "db_share", catalog = "")
public class SysRole extends BaseEntity{

    private String name;

    private String memo;

    @ManyToMany
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "ROLE_ID")},inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
    private Set<SysUser> users;

    @OneToMany
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "MASTER_ID")
    private Set<SysPermission> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public Set<SysUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SysUser> users) {
        this.users = users;
    }


    public Set<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<SysPermission> permissions) {
        this.permissions = permissions;
    }

    @Transient
    public Set<String> getPermissionNames() {
        Collection<SysPermission> pers = getPermissions();
        Set<String> set = new HashSet<String>();
        SysMenuButton menuButton = null;
        for (SysPermission per : pers) {
            menuButton = per.getMenuButton();
            set.add(menuButton.getMenu().getCode()+"-"+menuButton.getButton().getCode());
        }
        return set;
    }
}
