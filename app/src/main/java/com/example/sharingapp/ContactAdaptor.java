package com.example.sharingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ContactAdaptor extends ArrayAdapter<Contact> {
    private LayoutInflater inflator;
    private Context context;

    public ContactAdaptor(@NonNull Context context, int resource, @NonNull List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        inflator = LayoutInflater.from(context);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Contact contact = getItem(position);

        String username = "Username: " + contact.getUsername();
        String email = "Email: " + contact.getEmail();

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.contactlist_contact, parent, false);
        }

        TextView username_tv = (TextView) convertView.findViewById(R.id.username_tv);
        TextView email_tv = (TextView) convertView.findViewById(R.id.email_tv);

        username_tv.setText(username);
        email_tv.setText(email);

        return convertView;
    }
}
