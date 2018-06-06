package pers.yxb.share.helloworld.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author Yuxb.
 * @description.
 * @create 2018-4-12 17:51
 */
@Entity
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false)
    private String passWord;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = true, unique = true)
    private String nickName;
    @Column(nullable = false)
    private String regTime;

    public User() {
        super();
    }
    public User(String email, String nickName, String passWord, String userName, String regTime) {
        super();
        this.email = email;
        this.nickName = nickName;
        this.passWord = passWord;
        this.userName = userName;
        this.regTime = regTime;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassWord() {
        return passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getRegTime() {
        return regTime;
    }
    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }
}
