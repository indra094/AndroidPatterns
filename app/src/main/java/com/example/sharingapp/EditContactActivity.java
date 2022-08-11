package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditContactActivity extends AppCompatActivity implements  Observer{
    private EditText username;
    private EditText email;

    private ContactList contact_list = new ContactList();
    private ContactListController contact_list_ctlr;
    private Contact contact;
    private ContactController contactController;
    private ArrayList<Contact> activeBorrowers;
    private ArrayAdapter<String> adapter;
    private boolean on_create_update = false;
    private boolean loadedList = false;

    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        context = getApplicationContext();


        Intent intent = getIntent(); // Get intent from ItemsFragment
        int pos = intent.getIntExtra("position", 0);
        ArrayList<Contact> incomingParcel = intent.getParcelableArrayListExtra("activeBorrowers");
        activeBorrowers = (incomingParcel!=null)?incomingParcel:(new ArrayList<Contact> ());

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        contact_list_ctlr = new ContactListController(contact_list);

        on_create_update = true;
        contact_list_ctlr.addObserver(this);
        contact_list_ctlr.loadContacts(context);
        loadedList = true;

        on_create_update = false;
        contact = contact_list_ctlr.getContact(pos);
        contactController = new ContactController(contact);

        Log.v("EditContactActivity", "log username ");
        updateUI();

    }

    public boolean isUniqueContact(String username, String email, Contact currContact) {
        boolean isUnique = true;
        ArrayList<Contact> contacts = contact_list_ctlr.getContacts();
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

            boolean success = contact_list_ctlr.editContact(contact_list, contact, username_str, email_str, context);
            if(!success)
            {
                return;
            }
            // End EditContactActivity
            //Intent intent = new Intent(this, ContactsActivity.class);
            //startActivity(intent);
            cleanup();
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

    private  void cleanup() {
        contact_list_ctlr.removeObserver(this);
    }

    public void deleteContact(View view) {

        if(isBorrower(contact))
        {
            Toast.makeText(EditContactActivity.this, "Contact has borrowed something.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        boolean success=contact_list_ctlr.deleteContact(contact_list, contact, context);
        if(!success)
        {
            return;
        }

        cleanup();
        // End EditContactActivity
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
        Log.v("tag", "reached here");

        finish();
    }

    private void updateUI()
    {
        username.setText(contactController.getUsername());
        email.setText(contactController.getEmail());
    }

    @Override
    public void update() {
        if (on_create_update && loadedList) {
            Log.v("tag2", "reached2 here");
            updateUI();
        }
    }
}
