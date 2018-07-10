package com.utsman.kucingapes.mobilelearningprodisejarah;

public class ModelCategory {
    private String titleCat;
    private String imgCat;
    private int idCat;
    private String nameCat;

    public ModelCategory() {
    }

    public ModelCategory(String titleCat, String imgCat, int idCat, String nameCat) {
        this.titleCat = titleCat;
        this.imgCat = imgCat;
        this.idCat = idCat;
        this.nameCat = nameCat;
    }

    public String getTitleCat() {
        return titleCat;
    }

    public void setTitleCat(String titleCat) {
        this.titleCat = titleCat;
    }

    public String getImgCat() {
        return imgCat;
    }

    public void setImgCat(String imgCat) {
        this.imgCat = imgCat;
    }

    public int getIdCat() {
        return idCat;
    }

    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    public String getNameCat() {
        return nameCat;
    }

    public void setNameCat(String nameCat) {
        this.nameCat = nameCat;
    }
}
