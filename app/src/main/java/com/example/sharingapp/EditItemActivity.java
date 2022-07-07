package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Editing a pre-existing item consists of deleting the old item and adding a new item with the old
 * item's id.
 */
public class EditItemActivity extends AppCompatActivity {

    private ItemList item_list = new ItemList();
    private Item item;
    private Context context;

    private Bitmap image;
    private int REQUEST_CODE = 1;
    private ImageView photo;

    private EditText title;
    private EditText maker;
    private EditText description;
    private EditText length;
    private EditText width;
    private EditText height;
    private Spinner borrower_spinner;
    private EditText invisible;
    private ContactList contact_list=new ContactList();

    private TextView  borrower_tv;
    private Switch status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        title = (EditText) findViewById(R.id.title);
        maker = (EditText) findViewById(R.id.maker);
        description = (EditText) findViewById(R.id.description);
        length = (EditText) findViewById(R.id.length);
        width = (EditText) findViewById(R.id.width);
        height = (EditText) findViewById(R.id.height);
        borrower_spinner = (Spinner) findViewById(R.id.borrower_spinner);
        borrower_tv = (TextView) findViewById(R.id.borrower_tv);
        photo = (ImageView) findViewById(R.id.image_view);
        status = (Switch) findViewById(R.id.available_switch);

        context = getApplicationContext();
        item_list.loadItems(context);
        contact_list.loadContacts(context);

        List<String> branchList = new ArrayList<>();

        ArrayList<String> usernames = contact_list.getAllUsernames();
        for (String username:usernames) {
            branchList.add(username);
        }

        ArrayAdapter<String> branchListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, branchList);
        borrower_spinner.setAdapter(branchListAdapter);

        Intent intent = getIntent(); // Get intent from ItemsFragment
        int pos = intent.getIntExtra("position", 0);

        item = item_list.getItem(pos);

        title.setText(item.getTitle());
        maker.setText(item.getMaker());
        description.setText(item.getDescription());

        Dimensions dimensions = item.getDimensions();

        length.setText(dimensions.getLength());
        width.setText(dimensions.getWidth());
        height.setText(dimensions.getHeight());

        String status_str = item.getStatus();
        if (status_str.equals("Borrowed")) {
            status.setChecked(false);
            borrower_spinner.setSelection(branchList.indexOf(item.getBorrower().getUsername()));
        } else {
            borrower_tv.setVisibility(View.GONE);
            //borrower.setVisibility(View.GONE);
        }

        image = item.getImage();
        if (image != null) {
            photo.setImageBitmap(image);
        } else {
            photo.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    public void addPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public void deletePhoto(View view) {
        image = null;
        photo.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent intent) {
        super.onActivityResult(request_code, result_code, intent);
        if (request_code == REQUEST_CODE && result_code == RESULT_OK) {
            Bundle extras = intent.getExtras();
            image = (Bitmap) extras.get("data");
            photo.setImageBitmap(image);
        }
    }

    public void deleteItem(View view) {
        DeleteItemCommand delete_item_command = new DeleteItemCommand(item_list, item, context);
        delete_item_command.execute();

        boolean success = delete_item_command.isExecuted();
        if (!success){
            return;
        }

        // End EditItemActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveItem(View view) {

        String title_str = title.getText().toString();
        String maker_str = maker.getText().toString();
        String description_str = description.getText().toString();
        String length_str = length.getText().toString();
        String width_str = width.getText().toString();
        String height_str = height.getText().toString();
        String borrower_username_str = borrower_spinner.getSelectedItem().toString();

        Dimensions dimensions = new Dimensions(length_str, width_str, height_str);

        if (title_str.equals("")) {
            title.setError("Empty field!");
            return;
        }

        if (maker_str.equals("")) {
            maker.setError("Empty field!");
            return;
        }

        if (description_str.equals("")) {
            description.setError("Empty field!");
            return;
        }

        if (length_str.equals("")) {
            length.setError("Empty field!");
            return;
        }

        if (width_str.equals("")) {
            width.setError("Empty field!");
            return;
        }

        if (height_str.equals("")) {
            height.setError("Empty field!");
            return;
        }

        //if (borrower_str.equals("") && !status.isChecked()) {
        //    borrower.setError("Empty field!");
        //    return;
        //}

        // Reuse the item id
        String id = item.getId();

        Item updated_item = new Item(title_str, maker_str, description_str, dimensions, image, id);

        boolean checked = status.isChecked();
        if (!checked) {
            updated_item.setStatus("Borrowed");
            Contact borrower = contact_list.getContactByUsername(borrower_username_str);
            updated_item.setBorrower(borrower);
        }

        // Edit item
        EditItemCommand edit_item_command = new EditItemCommand(item_list, item, updated_item, context);
        edit_item_command.execute();

        boolean success = edit_item_command.isExecuted();
        if (!success){
            return;
        }

        // End EditItemActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Checked = Available
     * Unchecked = Borrowed
     */
    public void toggleSwitch(View view){
        if (status.isChecked()) {
            // Means was previously borrowed
            //borrower.setVisibility(View.GONE);
            borrower_tv.setVisibility(View.GONE);
            //item.setBorrower("");
            item.setStatus("Available");

        } else {
            // Means was previously available
            borrower_spinner.setVisibility(View.VISIBLE);
            borrower_tv.setVisibility(View.VISIBLE);
        }
    }
}