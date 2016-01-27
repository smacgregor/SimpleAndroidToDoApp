package com.example.smacgregor.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String kToDosFileName = "ToDos.txt";
    private File toDoFile;
    private final int kEditToDoResultCode = 1;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;

    private ListView listViewItems;
    private EditText addNewToDoItemTextInput;
    private Button addNewToDoItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNewToDoItemTextInput = (EditText)findViewById(R.id.etNewItem);
        addNewToDoItemButton = (Button)findViewById(R.id.btnAddItem);
        listViewItems = (ListView)findViewById(R.id.lvItems);
        readItems();

        // The array adapter will apply a model to a template list item view to produce a model
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listViewItems.setAdapter(itemsAdapter);

        setupListViewListener();
        setupAddNewToDoTextField();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == kEditToDoResultCode) {
            updateToDoItem(data.getStringExtra(EditItemActivity.kToDosItemValue), data.getIntExtra(EditItemActivity.kToDosItemPosition, 0));
        }
    }

    /**
     * Add a new item to the to-do list.
     * @param itemToAdd
     */
    public void addToDoItem(String itemToAdd) {
        itemsAdapter.add(itemToAdd);
        saveItems();
    }

    /**
     * Remove an item from the to-do list.
     * @param itemToRemove
     */
    public void removeToDoItem(String itemToRemove) {
       itemsAdapter.remove(itemToRemove);
        saveItems();
    }

    public void updateToDoItem(final String updatedItem, int position) {
        items.set(position, updatedItem);
        itemsAdapter.notifyDataSetChanged();
        saveItems();
    }

    /**
     *
     * Edit the todo item referenced by position
     * @param position
     */
    public void editItem(int position) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra(EditItemActivity.kToDosItemPosition, position);
        intent.putExtra(EditItemActivity.kToDosItemValue, itemsAdapter.getItem(position));
        startActivityForResult(intent, kEditToDoResultCode);
    }

    /**
     * Add an item to the view
     * @param view
     */
    public void onAddItem(View view) {
        EditText editText = (EditText)findViewById(R.id.etNewItem);
        String newItemText = editText.getText().toString();
        // Avoid adding an empty item
        // Shall we put up an error message for the user?
        if (newItemText.length() > 0) {
            addToDoItem(newItemText);
            editText.setText("");
        }
    }

    private void setupListViewListener() {

        listViewItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        editItem(position);
                        return;
                    }
                });

        listViewItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        removeToDoItem(items.get(position));
                        return true;
                    }
                });
    }

    private void setupAddNewToDoTextField() {
        // Only enable the add new to-do button when we have a non empty item name
        addNewToDoItemTextInput.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                addNewToDoItemButton.setEnabled(!TextUtils.isEmpty(s.toString()));
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }

    private File getToDoFile() {
        if (toDoFile == null) {
            File filesDir = getFilesDir();
            toDoFile = new File(filesDir, kToDosFileName);
        }
        return toDoFile;
    }

    private void readItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getToDoFile()));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getToDoFile(), items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
