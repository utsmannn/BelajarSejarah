package com.utsman.kucingapes.belajarsejarah;

class RcGetter {
    private int idCat;
    private String nameCat;
    private String titleCat;
    private String cat;
    private int id;
    private String img;
    private String title;
    private String nameUser;

    public RcGetter(int idCat, String nameCat, String titleCat, String cat, int id, String img, String title, String nameUser) {
        this.idCat = idCat;
        this.nameCat = nameCat;
        this.titleCat = titleCat;
        this.cat = cat;
        this.id = id;
        this.img = img;
        this.title = title;
        this.nameUser = nameUser;
    }

    public RcGetter() {
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

    public String getTitleCat() {
        return titleCat;
    }

    public void setTitleCat(String titleCat) {
        this.titleCat = titleCat;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
