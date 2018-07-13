package pers.yxb.share.sharemodel.entity;

import javax.persistence.*;

/**
 * Created by PC-HT on 2017/12/21.
 */
@Entity
@Table(name = "sys_permission")
public class SysPermission extends BaseEntity{

    private String status;

    @ManyToOne
    @JoinColumn(name = "MASTER_ID",insertable=false, updatable=false)
    private SysUser user;

    @ManyToOne
    @JoinColumn(name = "MASTER_ID",insertable=false, updatable=false)
    private SysRole role;

    @ManyToOne
    @JoinColumn(name = "MASTER_ID",insertable=false, updatable=false)
    private SysOrganization org;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID")
    private SysMenuButton menuButton;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }


    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }


    public SysOrganization getOrg() {
        return org;
    }

    public void setOrg(SysOrganization org) {
        this.org = org;
    }


    public SysMenuButton getMenuButton() {
        return menuButton;
    }

    public void setMenuButton(SysMenuButton menuButton) {
        this.menuButton = menuButton;
    }
}
