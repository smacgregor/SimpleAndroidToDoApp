package com.example.smacgregor.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String kToDosFileName = "ToDos.txt";

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listViewItems;

    private File toDoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewItems = (ListView)findViewById(R.id.lvItems);
        readItems();

        // The array adapter will apply our template list item view against our model
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listViewItems.setAdapter(itemsAdapter);

        setupListViewListener();
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
            addItem(newItemText);
            editText.setText("");
        }
    }

    /**
     * Add a new item to the to-do list.
     * @param itemToAdd
     */
    public void addItem(String itemToAdd) {
        itemsAdapter.add(itemToAdd);
        saveItems();
    }

    /**
     * Remove an item from the to-do list.
     * @param itemToRemove
     */
    public void removeItem(String itemToRemove) {
       itemsAdapter.remove(itemToRemove);
        saveItems();
    }

    private void setupListViewListener() {
        listViewItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        removeItem(items.get(position));
                        return true;
                    }
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
