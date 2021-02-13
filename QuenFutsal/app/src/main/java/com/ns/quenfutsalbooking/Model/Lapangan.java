package com.ns.quenfutsalbooking.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lapangan implements Parcelable {
    private String name,lapanganId,harga;
    private Long ratting;

    public Lapangan() {
    }


    protected Lapangan(Parcel in) {
        name = in.readString();
        lapanganId = in.readString();
        if (in.readByte() == 0) {
            ratting = null;
        } else {
            ratting = in.readLong();
        }
    }

    public static final Creator<Lapangan> CREATOR = new Creator<Lapangan>() {
        @Override
        public Lapangan createFromParcel(Parcel in) {
            return new Lapangan(in);
        }

        @Override
        public Lapangan[] newArray(int size) {
            return new Lapangan[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRatting() {
        return ratting;
    }

    public void setRatting(Long ratting) {
        this.ratting = ratting;
    }

    public String getLapanganId() {
        return lapanganId;
    }

    public void setLapanganId(String lapanganId) {
        this.lapanganId = lapanganId;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(lapanganId);
        if (ratting == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(ratting);
        }
    }
}
