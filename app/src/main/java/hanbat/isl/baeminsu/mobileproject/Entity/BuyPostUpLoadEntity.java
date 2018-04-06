package hanbat.isl.baeminsu.mobileproject.Entity;

/**
 * Created by baeminsu on 2017. 12. 13..
 */

public class BuyPostUpLoadEntity {

    String id;
    String title;
    String content;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {

        return title;
    }

    public String getContent() {
        return content;
    }
}
