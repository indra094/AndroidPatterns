package com.example.sharingapp;
import android.util.Log;

import java.util.ArrayList;

/**
 * Superclass of Item, ItemList, Contact, ContactList
 */
public class Observable {

    private ArrayList<Observer> observers = null;
    private int count=0;
    private boolean touched=false;
    public Observable(){
        observers = new ArrayList<Observer>();
    }

    // Notify observers when need to update any changes made to model
    public void notifyObservers() {

        if(!touched)
        {
            Log.v("In obserber","has not been touched");
        }
        for (Observer observer : observers) {
            Log.v("In Observers", "Update observer");
            observer.update();
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
        touched=true;
        count++;
    }

    public void removeObserver(Observer observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
            count--;
        }
    }
}
