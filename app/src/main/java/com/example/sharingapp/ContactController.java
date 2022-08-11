package com.example.sharingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class ContactController {
    private Contact contact;

    public ContactController(Contact contact) {
        this.contact = contact;
    }

    public void setId() {
        contact.setId();
    }
    public String getId() {
        return contact.getId();
    }

    public void updateId(String id) {
        contact.updateId(id);
    }

    public void setEmail(String email) {
        contact.setEmail(email);
    }
    public String getEmail() {
        return contact.getEmail();
    }

    public void setUsername(String username) {
        contact.setUsername(username);
    }
    public String getUsername() {
        return contact.getUsername();
    }

    public int describeContents() {
        return contact.describeContents();
    }

    public void writeToParcel(Parcel parcel, int i) {
        contact.writeToParcel(parcel, i);
    }


}
