package com.utsman.kucingapes.mobilelearningprodisejarah.Content;

public class ModelContentList {
    private String title;
    private String imgUrl;
    private String cat;
    private String body;

    public ModelContentList() {
    }

    public ModelContentList(String title, String imgUrl, String cat, String body) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.cat = cat;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
