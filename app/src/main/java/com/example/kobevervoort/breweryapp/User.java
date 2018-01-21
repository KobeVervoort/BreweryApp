package com.example.kobevervoort.breweryapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kobevervoort on 21/01/2018.
 */

public class User implements Parcelable{
    private String id;
    private String name;
    private String email;

    public User() {

    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected User(Parcel in){
        id = in.readString();
        name = in.readString();
        email = in.readString();
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(id);
        out.writeString(name);
        out.writeString(email);
    }

    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

