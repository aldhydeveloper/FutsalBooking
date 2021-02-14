package com.ns.quenfutsalbooking.Model;

public class Cart {
    String id, costumerName, costumerPhone, placeName, lapanganName, price, time, extraTime, status;

    public Cart() {
    }

    public Cart(String id, String costumerName, String costumerPhone, String placeName,
                String lapanganName, String price, String time, String extraTime, String status) {
        this.id = id;
        this.costumerName = costumerName;
        this.costumerPhone = costumerPhone;
        this.placeName = placeName;
        this.lapanganName = lapanganName;
        this.price = price;
        this.time = time;
        this.extraTime = extraTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCostumerName() {
        return costumerName;
    }

    public void setCostumerName(String costumerName) {
        this.costumerName = costumerName;
    }

    public String getCostumerPhone() {
        return costumerPhone;
    }

    public void setCostumerPhone(String costumerPhone) {
        this.costumerPhone = costumerPhone;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLapanganName() {
        return lapanganName;
    }

    public void setLapanganName(String lapanganName) {
        this.lapanganName = lapanganName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(String extraTime) {
        this.extraTime = extraTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
