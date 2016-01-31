package com.example.smacgregor.simpletodo.main.viewing;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.smacgregor.simpletodo.editing.EditItemActivity;
import com.example.smacgregor.simpletodo.R;
import com.example.smacgregor.simpletodo.core.ToDoItem;
import com.example.smacgregor.simpletodo.core.ToDoItemDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int kEditToDoResultCode = 1;

    private List<ToDoItem> items;
    private ToDoAdapter itemsAdapter;

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
        itemsAdapter = new ToDoAdapter(this, items);
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
            updateToDoItem((ToDoItem)data.getSerializableExtra(EditItemActivity.TODO_ITEM));
        }
    }

    /**
     * Add a new item to the to-do list.
     * @param itemToAdd
     */
    public void addToDoItem(String itemToAdd) {
        ToDoItem newToDoItem = new ToDoItem();
        newToDoItem.name = itemToAdd;
        newToDoItem.position = itemsAdapter.getCount();
        itemsAdapter.add(newToDoItem);
        newToDoItem.key = ToDoItemDatabase.getInstance(this).addToDoItem(newToDoItem);
    }

    /**
     * Remove an item from the to-do list.
     * @param itemToRemove
     */
    public void removeToDoItem(ToDoItem itemToRemove) {
        ToDoItemDatabase.getInstance(this).deleteToDo(itemToRemove);
        itemsAdapter.remove(itemToRemove);
    }

    public void updateToDoItem(ToDoItem toDoItem) {
        ToDoItemDatabase.getInstance(this).updateToDoItem(toDoItem);
        items.set(toDoItem.position, toDoItem);
        itemsAdapter.notifyDataSetChanged();
    }

    /**
     * Edit the todo item referenced by position
     * @param toDoItem
     */
    public void editToDoItem(ToDoItem toDoItem) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra(EditItemActivity.TODO_ITEM, toDoItem);
        startActivityForResult(intent, kEditToDoResultCode);
    }

    /**
     * Add an item to the view
     * @param view
     */
    public void onAddItem(View view) {
        EditText editText = (EditText)findViewById(R.id.etNewItem);
        String newItemText = editText.getText().toString();
        addToDoItem(newItemText);
        editText.setText("");
    }

    private void setupListViewListener() {

        listViewItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        editToDoItem(itemsAdapter.getItem(position));
                    }
                });

        listViewItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        removeToDoItem(itemsAdapter.getItem(position));
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

    private void readItems() {
        // ideally this would run on a background thread and then update the UI when complete
        items = ToDoItemDatabase.getInstance(this).getAllToDoItems();
    }
}
