package cn.adolf.adolftest;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2020-11-18 17:11
 **/
public class UserBean {
    private String username;
    private String motto;
    private int id;
    private int sex;

    public UserBean(int id, String username, String motto, int sex) {
        this.username = username;
        this.motto = motto;
        this.id = id;
        this.sex = sex;
    }

    public UserBean(String username, String motto, int sex) {
        this.username = username;
        this.motto = motto;
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "username='" + username + '\'' +
                ", motto='" + motto + '\'' +
                ", id=" + id +
                ", sex=" + sex +
                '}';
    }
}
