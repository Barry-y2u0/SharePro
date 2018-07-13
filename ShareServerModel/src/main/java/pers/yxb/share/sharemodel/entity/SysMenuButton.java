package pers.yxb.share.sharemodel.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by PC-HT on 2017/12/8.
 */
@Entity
@Table(name = "sys_menu_button")
public class SysMenuButton extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "MENU_ID", referencedColumnName = "ID")
    private SysMenu menu;

    @ManyToOne
    @JoinColumn(name = "BTN_ID", referencedColumnName = "ID")
    private SysButton button;

    @OneToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "RESOURCE_ID")
    private Set<SysPermission> permissions;


    public SysMenu getMenu() {
        return menu;
    }

    public void setMenu(SysMenu menu) {
        this.menu = menu;
    }


    public SysButton getButton() {
        return button;
    }

    public void setButton(SysButton button) {
        this.button = button;
    }

    public Set<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<SysPermission> permissions) {
        this.permissions = permissions;
    }
}
