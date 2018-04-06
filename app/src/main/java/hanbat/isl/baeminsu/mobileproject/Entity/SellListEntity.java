package hanbat.isl.baeminsu.mobileproject.Entity;

import java.io.Serializable;

/**
 * Created by baeminsu on 2017. 12. 8..
 */

public class SellListEntity implements Serializable{

    private String id;
    private String imageURL;
    private String title;
    private String price;
    private String name;
    private String date;
    private String content;
    private String writer;
    private String phone;

    public void setId(String id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {

        return phone;
    }

    public String getId() {

        return id;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriter() {

        return writer;
    }

    private int state;

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public void setState(int state) {
        this.state = state;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
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

    public int getState() {
        return state;
    }
}
