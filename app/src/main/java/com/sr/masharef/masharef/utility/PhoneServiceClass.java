package com.sr.masharef.masharef.utility;

/**
 * Created by lenovo on 12/01/2017.
 */

public class PhoneServiceClass {
    private String name,occupation,workplace,mobile;
    private int rating,thumbnail;

    public PhoneServiceClass(String PerName, String perOccupation, String perWorkplace, String perMobile, int PerRating, int PerThumbnail){
        this.name=PerName;
        this.occupation=perOccupation;
        this.workplace=perWorkplace;
        this.mobile=perMobile;
        this.rating=PerRating;
        this.thumbnail=PerThumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getrating() {
        return rating;
    }

    public void setrating(int rating) {
        this.rating = rating;
    }
}
