package com.ns.quenfutsalbooking.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class TempatFutsal  implements Parcelable {

    private String name, address, openHours, tempatId;

    public TempatFutsal() {
    }

    protected TempatFutsal(Parcel in) {
        name = in.readString();
        address = in.readString();
        openHours = in.readString();
        tempatId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(openHours);
        dest.writeString(tempatId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TempatFutsal> CREATOR = new Creator<TempatFutsal>() {
        @Override
        public TempatFutsal createFromParcel(Parcel in) {
            return new TempatFutsal(in);
        }

        @Override
        public TempatFutsal[] newArray(int size) {
            return new TempatFutsal[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getTempatId() {
        return tempatId;
    }

    public void setTempatId(String tempatId) {
        this.tempatId = tempatId;
    }
}
