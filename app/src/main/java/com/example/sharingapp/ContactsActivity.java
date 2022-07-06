package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {
    private ContactList contact_list=new ContactList();
    private ItemList item_list=new ItemList();
    private ListView my_contacts;
    private ContactAdaptor adapter;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_contacts_fragment);
        context = getApplicationContext();
        contact_list.loadContacts(context);
        item_list.loadItems(context);
        my_contacts = (ListView) findViewById(R.id.my_contacts);
        adapter = new ContactAdaptor(context, R.id.my_contacts, contact_list.getContacts());
        my_contacts.setAdapter(adapter);
    }


    protected void onStart() {
        super.onStart();
        Log.v("In contactsActivity", "hit on start");
        adapter.notifyDataSetChanged();

        // When item is long clicked, this starts EditContactActivity
        my_contacts.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {

                Contact contact = adapter.getItem(pos);
                int meta_pos = contact_list.getIndex(contact);
                if (meta_pos >= 0) {
                    Intent editIntent = new Intent(context, EditContactActivity.class);
                    editIntent.putExtra("position", meta_pos);
                    editIntent.putExtra("activeBorrowers", item_list.getActiveBorrowers());
                    startActivity(editIntent);
                    my_contacts.invalidate();
                    finish();
                }
                return true;
            }
        });
    }

    public void addItemActivity(View view) {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
        my_contacts.invalidate();
        finish();
    }
}
