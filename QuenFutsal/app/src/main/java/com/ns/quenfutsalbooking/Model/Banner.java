package com.ns.quenfutsalbooking.Model;

public class Banner {
    private String image,title,lat,lon,id;

    public Banner() {
    }

    public Banner(String image, String title, String lat, String lon, String id) {
        this.image = image;
        this.title = title;
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
