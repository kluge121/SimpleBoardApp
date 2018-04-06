package hanbat.isl.baeminsu.mobileproject.Set;

/**
 * Created by baeminsu on 2017. 12. 7..
 */

public class UserInfo {

    private static UserInfo userInfo = null;

    public synchronized static UserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }


    private UserInfo() {

    }

    private String id;
    private String name;
    private String phone;


    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void initInfo(){
        id ="";
        name="";
        phone="";
    }

}
