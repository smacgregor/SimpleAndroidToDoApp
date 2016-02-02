package com.example.smacgregor.simpletodo.editing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smacgregor.simpletodo.R;
import com.example.smacgregor.simpletodo.core.ToDoItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EditItemActivity extends AppCompatActivity {

    // Move these to a shared constants file
    public static final String TODO_ITEM_ID = "ToDoItemId";

    @Bind(R.id.editText) EditText editItemTextField;
    @Bind(R.id.saveButton) Button saveButton;

    private ToDoItem toDoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        ButterKnife.bind(this);

        toDoItem = ToDoItem.load(ToDoItem.class, getIntent().getLongExtra(EditItemActivity.TODO_ITEM_ID, 0));
        setupEditItemTextField(toDoItem.name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    @OnClick(R.id.saveButton)
    public void onSubmitItemModification(View view) {
        saveModifications();
    }

    private void saveModifications() {
        Intent modifiedData = new Intent();
        toDoItem.name = editItemTextField.getText().toString();
        toDoItem.save(); // save the changes
        modifiedData.putExtra(EditItemActivity.TODO_ITEM_ID, toDoItem.getId());
        setResult(RESULT_OK, modifiedData);
        finish();
    }

    @OnTextChanged(R.id.editText)
    void onToDoNameTextChanged(Editable s) {
        // Only enable the save button when we have a non empty todo item name
        saveButton.setEnabled(!TextUtils.isEmpty(s.toString()));
    }

    private void setupEditItemTextField(final String editItemName) {
        editItemTextField.append(editItemName);
    }
}
