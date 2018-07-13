package pers.yxb.share.sharemodel.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by PC-HT on 2017/12/8.
 */
@Entity
@Table(name = "sys_organization")
public class SysOrganization extends BaseEntity{

    private String name;

    private String parentId;

    private Integer grade;

    private String icon;

    private Integer seq;

    @ManyToMany
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(name = "sys_user_org", joinColumns = {@JoinColumn(name = "ORG_ID")},inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
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
