package hanbat.isl.baeminsu.mobileproject.Entity;

/**
 * Created by baeminsu on 2017. 12. 13..
 */

public class SellPostUpLoadEntity {

    String title;
    String content;
    String imagePath;
    String price;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getPrice() {
        return price;
    }
}
