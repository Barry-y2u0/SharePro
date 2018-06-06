package pers.yxb.share.sharemodel.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by PC-HT on 2017/12/8.
 */
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity implements Serializable {

    private String username;

    private String password;

    private String salt;

    private String realName;

    private String sex;

    private Date birthday;

    private String phone;

    private String email;

    private String degree;

    private String adress;

    @ManyToMany
    @Cascade(value = CascadeType.SAVE_UPDATE)
    @JoinTable(name = "sys_user_org", joinColumns = {@JoinColumn(name = "USER_ID")},inverseJoinColumns = {@JoinColumn(name = "ORG_ID")})
    private Set<SysOrganization> orgs;

    @ManyToMany
    @Cascade(value = CascadeType.SAVE_UPDATE)
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "USER_ID")},inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    private Set<SysRole> roles;

    @OneToMany
    @Cascade(value = CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "MASTER_ID")
    private Set<SysPermission> permissions;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Set<SysOrganization> getOrgs() {
        return orgs;
    }

    public void setOrgs(Set<SysOrganization> orgs) {
        this.orgs = orgs;
    }

    public Set<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }

    public Set<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<SysPermission> permissions) {
        this.permissions = permissions;
    }

    @Transient
    public Set<String> getRolesName() {
        Collection<SysRole> roles = getRoles();
        Set<String> set = new HashSet<String>();
        for (SysRole role : roles) {
            set.add(role.getName());
        }
        return set;
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

    public String getCredentialSalt() {
        return username + salt;
    }
}
