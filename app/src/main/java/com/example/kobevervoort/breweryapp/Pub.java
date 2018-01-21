package com.example.kobevervoort.breweryapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kobevervoort on 19/01/2018.
 */

/**
 * Pub needs to be a parcelable class to be able to pass Pub objects from one acitvity to another
 * via intents. Could have also used serializable, which is easier to implement, but way slower
 */

public class Pub implements Parcelable {

    public String name;
    public String status;
    public String street;
    public String city;
    public String phone;
    public String id;

    public Pub(String name, String status, String street, String city, String phone, String id) {
        this.name = name;
        this.status = status;
        this.street = street;
        this.city = city;
        this.phone = phone;
        this.id = id;
    }

    protected Pub(Parcel in){
        name = in.readString();
        status = in.readString();
        street = in.readString();
        city = in.readString();
        phone = in.readString();
        id = in.readString();
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(status);
        out.writeString(street);
        out.writeString(city);
        out.writeString(phone);
        out.writeString(id);
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Pub> CREATOR
            = new Parcelable.Creator<Pub>() {
        public Pub createFromParcel(Parcel in) {
            return new Pub(in);
        }

        public Pub[] newArray(int size) {
            return new Pub[size];
        }
    };
}