package com.example.sharingapp;

import android.content.Context;

public class DeleteContactCommand extends Command{
    private ContactList contactlist;
    private Contact contact;
    private Context context;

    public DeleteContactCommand(ContactList contactlist, Contact contact, Context context) {
        this.contactlist = contactlist;
        this.contact = contact;
        this.context = context;
    }

    public void execute() {
        contactlist.deleteContact(contact);
        setIsExecuted(contactlist.saveContacts(context));
    }
}
