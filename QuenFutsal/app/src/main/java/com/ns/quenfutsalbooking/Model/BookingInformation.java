package com.ns.quenfutsalbooking.Model;

import com.google.firebase.Timestamp;

public class BookingInformation {
    private  String cityBook,costumerName, costumerPhone,costumerEmail, time, lapanganId, lapanganName, tempatId, tempatName, tempatAddress, tempatOpenHours,harga, extraTime;
    private Long slot;
    private Timestamp  timestamp;
    private boolean done;

    public BookingInformation() {
    }

    public BookingInformation(String cityBook, String costumerName, String costumerPhone, String costumerEmail, String time, String lapanganId, String lapanganName,
                              String tempatId, String tempatName, String tempatAddress, String tempatOpenHours, String harga, String extraTime, Long slot,
                              Timestamp timestamp, boolean done) {
        this.cityBook = cityBook;
        this.costumerName = costumerName;
        this.costumerPhone = costumerPhone;
        this.costumerEmail = costumerEmail;
        this.time = time;
        this.lapanganId = lapanganId;
        this.lapanganName = lapanganName;
        this.tempatId = tempatId;
        this.tempatName = tempatName;
        this.tempatAddress = tempatAddress;
        this.tempatOpenHours = tempatOpenHours;
        this.harga = harga;
        this.extraTime = extraTime;
        this.slot = slot;
        this.timestamp = timestamp;
        this.done = done;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLapanganId() {
        return lapanganId;
    }

    public void setLapanganId(String lapanganId) {
        this.lapanganId = lapanganId;
    }

    public String getLapanganName() {
        return lapanganName;
    }

    public void setLapanganName(String lapanganName) {
        this.lapanganName = lapanganName;
    }

    public String getTempatId() {
        return tempatId;
    }

    public void setTempatId(String tempatId) {
        this.tempatId = tempatId;
    }

    public String getTempatName() {
        return tempatName;
    }

    public void setTempatName(String tempatName) {
        this.tempatName = tempatName;
    }

    public String getTempatAddress() {
        return tempatAddress;
    }

    public void setTempatAddress(String tempatAddress) {
        this.tempatAddress = tempatAddress;
    }

    public String getTempatOpenHours() {
        return tempatOpenHours;
    }

    public void setTempatOpenHours(String tempatOpenHours) {
        this.tempatOpenHours = tempatOpenHours;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getCityBook() {
        return cityBook;
    }

    public void setCityBook(String cityBook) {
        this.cityBook = cityBook;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(String extraTime) {
        this.extraTime = extraTime;
    }

    public String getCostumerEmail() {
        return costumerEmail;
    }

    public void setCostumerEmail(String costumerEmail) {
        this.costumerEmail = costumerEmail;
    }
}
