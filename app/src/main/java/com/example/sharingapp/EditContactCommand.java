package com.example.sharingapp;

import android.content.Context;

public class EditContactCommand extends Command {
    private ContactList contactlist;
    private Contact old_contact;
    private  String email;
    private  String userId;

    private Context context;

    public EditContactCommand(ContactList contactlist, Contact old_contact, String email, String userId, Context context) {
        this.contactlist = contactlist;
        this.old_contact = old_contact;
        this.userId = userId;
        this.email = email;
        this.context = context;
    }

    public void execute() {
        Contact new_contact=old_contact;
        new_contact.setEmail(email);
        new_contact.setUsername(userId);
        contactlist.deleteContact(old_contact);
        contactlist.addContact(new_contact);
        setIsExecuted(contactlist.saveContacts(context));
    }
}