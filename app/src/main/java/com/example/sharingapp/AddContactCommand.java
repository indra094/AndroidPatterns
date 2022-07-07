package com.example.sharingapp;

import android.content.Context;

public class AddContactCommand extends Command {
    private ContactList contactlist;
    private Contact contact;
    private Context context;

    public AddContactCommand(ContactList contactlist, Contact contact, Context context) {
        this.contactlist = contactlist;
        this.contact = contact;
        this.context = context;
    }

    @Override
    public void execute() {
        contactlist.addContact(contact);
        setIsExecuted(contactlist.saveContacts(context));
    }
}
