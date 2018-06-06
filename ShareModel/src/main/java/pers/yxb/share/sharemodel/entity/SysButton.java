package pers.yxb.share.sharemodel.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by PC-HT on 2017/12/8.
 */
@Entity
@Table(name = "sys_button")
public class SysButton extends BaseEntity{

    private String name;

    private String code;

    private String icon;

    private String acro;

    private Integer seq;


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

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
