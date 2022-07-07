package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditContactActivity extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private ContactList contact_list = new ContactList();
    private Contact contact;
    private ArrayList<Contact> activeBorrowers;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        context = getApplicationContext();
        contact_list.loadContacts(context);

        Intent intent = getIntent(); // Get intent from ItemsFragment
        int pos = intent.getIntExtra("position", 0);
        activeBorrowers = intent.getParcelableArrayListExtra("activeBorrowers");
        contact = contact_list.getContact(pos);
        contact.setUsername("wololo");
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);

        //Log.v("EditContactActivity", "log username "+un);
        username.setText(contact.getUsername());
        email.setText(contact.getEmail());

    }

    public boolean isUniqueContact(String username, String email, Contact currContact) {
        boolean isUnique = true;
        ArrayList<Contact> contacts = contact_list.getContacts();
        for (Contact contact : contacts) {
            if(currContact.equals(contact))
            {
                continue;
            }
            if (contact.getUsername().equals(username) || contact.getEmail().equals(email)) {
                isUnique = false;
                break;
            }
        }
        return isUnique;
    }

    public void saveContact(View view) {
        String username_str = username.getText().toString();
        String email_str = email.getText().toString();

        if (username_str.equals("")) {
            username.setError("Empty field!");
            return;
        }

        if (email_str.equals("")) {
            email.setError("Empty field!");
            return;
        }

        if (isUniqueContact(username_str, email_str, contact)) {

            // Edit item
            EditContactCommand edit_item_command = new EditContactCommand(contact_list, contact, username_str, email_str, context);
            edit_item_command.execute();

            boolean success = edit_item_command.isExecuted();
            if (!success){
                return;
            }

            // End EditContactActivity
            //Intent intent = new Intent(this, ContactsActivity.class);
            //startActivity(intent);
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
            Log.v("tag", "reached here");
            finish();
        }
        else
        {
            Toast.makeText(EditContactActivity.this, "Not unique!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isBorrower(Contact contact) {
        boolean hasBorrowed=false;
        for (Contact activeBorrower:activeBorrowers) {
            if(activeBorrower.getEmail().equals(contact.getEmail()) || activeBorrower.getUsername().equals(contact.getUsername())) {
                hasBorrowed = true;
            }
        }
        return hasBorrowed;
    }


    public void deleteContact(View view) {

        if(isBorrower(contact))
        {
            Toast.makeText(EditContactActivity.this, "Contact has borrowed something.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        DeleteContactCommand edit_item_command = new DeleteContactCommand(contact_list, contact, context);
        edit_item_command.execute();

        boolean success = edit_item_command.isExecuted();
        if (!success){
            return;
        }

        // End EditContactActivity
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
        Log.v("tag", "reached here");
        finish();
    }
}
