package com.example.sharingapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.UUID;

public class Contact extends Observable implements Parcelable {
    private String username;
    private String email;
    private String id;//Smell: uninitialized var

    public Contact(String username, String email, String id) {

        this.username = username;
        this.email = email;

        if (id == null){
            setId();
        } else {
            updateId(id);
        }
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
        notifyObservers();
    }
    public String getId() {
        return this.id;
    }
    public void updateId(String id) {
        this.id = id;
        notifyObservers();
    }

    public void setEmail(String email) {
        this.email = email;
        Log.v("In Contact", "Gonna notify from contact");
        notifyObservers();
    }
    public String getEmail() {
        return this.email;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyObservers();
    }
    public String getUsername() {
        return this.username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.id,
                this.username,
                this.email});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
                String[] data = new String[3];
                in.readStringArray(data);
                return new Contact(data[1], data[2], data[0]);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
