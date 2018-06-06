package pers.yxb.share.sharemodel.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by PC-HT on 2017/12/8.
 */
@Entity
@Table(name = "sys_menu")
public class SysMenu extends BaseEntity{

    private String name;

    private String code;

    private String parentId;

    private String url;

    private String icon;

    private String acro;

    private String isLeaf;

    private Integer seq;

    @OneToMany
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "MENU_ID")
    private Set<SysMenuButton> menuButtons;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAcro() {
        return acro;
    }

    public void setAcro(String acro) {
        this.acro = acro;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Set<SysMenuButton> getMenuButtons() {
        return menuButtons;
    }

    public void setMenuButtons(Set<SysMenuButton> menuButtons) {
        this.menuButtons = menuButtons;
    }
}
