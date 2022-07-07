package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private ContactList contact_list = new ContactList();
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        context = MainActivity.getContext();
        contact_list.loadContacts(context);

        Button button= (Button) findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveContact(v);
            }
        });
    }

    public boolean isUniqueContact(String username, String email) {
        boolean isUnique = true;
        ArrayList<Contact> contacts = contact_list.getContacts();
        for (Contact contact:contacts) {
            if(contact.getUsername().equals(username) || contact.getEmail().equals(email))
            {
                isUnique = false;
                break;
            }
        }
        return  isUnique;
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

        if(isUniqueContact(username_str, email_str)) {
            Contact contact = new Contact(username_str, email_str, null);

            AddContactCommand add_contact_command = new AddContactCommand(contact_list, contact, context);
            add_contact_command.execute();

            boolean success = add_contact_command.isExecuted();
            if (!success){
                return;
            }

            // End AddContactActivity
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
            Log.v("tag", "reached here");
            finish();
        }
        else {
            Toast.makeText(AddContactActivity.this, "Not unique!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
