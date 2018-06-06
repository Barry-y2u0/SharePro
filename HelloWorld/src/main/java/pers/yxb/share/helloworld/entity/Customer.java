package pers.yxb.share.helloworld.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-4-11 9:10
 */
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity implements Serializable {

    @Column
    private String name;

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
