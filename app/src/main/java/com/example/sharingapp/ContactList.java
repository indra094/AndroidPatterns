package com.example.sharingapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactList extends Observable{
    private ArrayList<Contact> contacts;
    private String FILENAME="contacts.sav";

    public void setContacts(ArrayList<Contact> contact_list) {
        contacts = contact_list;
        notifyObservers();
    }
    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public ArrayList<String> getAllUsernames() {
        ArrayList<String> usernames=new ArrayList<String>();
        for (Contact contact:contacts) {
            usernames.add(contact.getUsername());
        }
        return usernames;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
        notifyObservers();
    }
    public void deleteContact(Contact contact) {
        contacts.remove(contact);
        notifyObservers();
    }
    public Contact getContact(int index) {
        Log.v("ContactList", "Num of contacts "+contacts.size() +" and index is "+index);
        return contacts.get(index);
    }

    public int getSize() {
        return contacts.size();
    }

    public int getIndex(Contact contact) {
        int pos = 0;
        for (Contact dbContact : contacts) {
            if (dbContact.getId().equals(contact.getId())) {
                return pos;
            }
            pos = pos+1;
        }
        return -1;
    }

    public boolean hasContact(Contact contact) {
        for(Contact dbContact:contacts) {
            if(dbContact.getId().equals(contact.getId()))
            {
                return true;
            }
        }

        Log.v("ContactList", " Contact name is "+contact.getUsername());
        return false;
    }

    public Contact getContactByUsername(String username) {
        for (Contact contact:contacts) {
            if(contact.getUsername().equals(username))
            {
                return contact;
            }
        }
        return null;
    }

    public void loadContacts(Context context) {
        try{
            FileInputStream streamInput = context.openFileInput(FILENAME);
            InputStreamReader reader = new InputStreamReader(streamInput);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Contact> >() {}.getType();
            contacts = gson.fromJson(reader, listType);
            streamInput.close();
            Log.v("ContactList", "Loaded contacts "+contacts.size());
        } catch (FileNotFoundException err) {
            contacts = new ArrayList<Contact>();
            Log.v("ContactList", "file not found");
        } catch (IOException err) {
            contacts = new ArrayList<Contact>();
            Log.v("ContactList", "ioexception");
        }

        notifyObservers();
    }
    public boolean saveContacts(Context context) {
        try {
            FileOutputStream outputStream = context.openFileOutput(FILENAME, 0);
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
            Gson gson = new Gson();
            gson.toJson(contacts, streamWriter);
            streamWriter.flush();
            outputStream.close();
        } catch (FileNotFoundException err) {
            err.printStackTrace();
            return false;
        } catch (IOException err) {
            err.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isUsernameAvailable(String username) {
        boolean isAvailable=false;
        for (Contact contact:contacts) {
            if(contact.getUsername().equals(username))
            {
                isAvailable = true;
                break;
            }
        }
        return isAvailable;
    }

}
