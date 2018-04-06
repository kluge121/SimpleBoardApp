package hanbat.isl.baeminsu.mobileproject.Entity;

/**
 * Created by baeminsu on 2017. 12. 13..
 */

public class JoinEntity {

    String id;
    String password;
    String name;
    String phone;

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {

        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
