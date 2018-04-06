package hanbat.isl.baeminsu.mobileproject.Entity;

import java.io.Serializable;

/**
 * Created by baeminsu on 2017. 12. 8..
 */

public class BuyListEntity implements Serializable {

    private String id;
    private String writer;
    private String title;
    private String name;
    private String date;
    private String content;
    private int state;
    private String phone;

    public void setState(int state) {
        this.state = state;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getState() {

        return state;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


