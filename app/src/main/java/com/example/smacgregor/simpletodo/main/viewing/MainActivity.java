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

import com.activeandroid.query.Select;
import com.example.smacgregor.simpletodo.R;
import com.example.smacgregor.simpletodo.core.ToDoItem;
import com.example.smacgregor.simpletodo.editing.EditItemActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity {

    private final int kEditToDoResultCode = 1;

    private List<ToDoItem> items;
    private ToDoAdapter itemsAdapter;

    @Bind(R.id.lvItems) ListView listViewItems;
    @Bind(R.id.etNewItem) EditText addNewToDoItemTextInput;
    @Bind(R.id.btnAddItem) Button addNewToDoItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        readItems();

        // The array adapter will apply a model to a template list item view to produce a model
        itemsAdapter = new ToDoAdapter(this, items);
        listViewItems.setAdapter(itemsAdapter);
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
            ToDoItem toDoItem = ToDoItem.load(ToDoItem.class, data.getLongExtra(EditItemActivity.TODO_ITEM_ID, 0));
            updateToDoItem(toDoItem);
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
        newToDoItem.save();
    }

    /**
     * Remove an item from the to-do list.
     * @param itemToRemove
     */
    public void removeToDoItem(ToDoItem itemToRemove) {
        itemToRemove.delete();
        itemsAdapter.remove(itemToRemove);
    }

    public void updateToDoItem(ToDoItem toDoItem) {
        itemsAdapter.notifyDataSetChanged();
    }

    /**
     * Edit the todo item referenced by position
     * @param toDoItem
     */
    public void editToDoItem(ToDoItem toDoItem) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra(EditItemActivity.TODO_ITEM_ID, toDoItem.getId());
        startActivityForResult(intent, kEditToDoResultCode);
    }

    /**
     * Add an item to the view
     * @param view
     */
    @OnClick(R.id.btnAddItem)
    public void onAddItem(Button saveButton) {
        String newItemText = addNewToDoItemTextInput.getText().toString();
        addToDoItem(newItemText);
        addNewToDoItemTextInput.setText("");
    }

    @OnItemClick(R.id.lvItems)
    void onItemClick(int position) {
        editToDoItem(itemsAdapter.getItem(position));
    }

    @OnItemLongClick(R.id.lvItems)
    boolean onItemLongClick(int position) {
        removeToDoItem(itemsAdapter.getItem(position));
        return true;
    }

    @OnTextChanged(R.id.etNewItem)
    void onToDoTextFieldChanged(Editable s) {
        // Only enable the add new to-do button when we have a non empty item name
        addNewToDoItemButton.setEnabled(!TextUtils.isEmpty(s.toString()));
    }

    private void readItems() {
        // a db query blocking the main thread...
        items = new Select().from(ToDoItem.class).orderBy("Position ASC").execute();
    }
}
