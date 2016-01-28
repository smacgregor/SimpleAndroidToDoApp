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
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    // Move these to a shared constants file
    public static final String kToDosItemPosition = "ToDoPosition";
    public static final String kToDosItemValue = "ToDoValue";

    private EditText editItemTextField;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        saveButton = (Button)findViewById(R.id.saveButton);
        editItemTextField = (EditText)findViewById(R.id.editText);

        setupEditItemTextField(getIntent().getStringExtra(kToDosItemValue));
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

    public void onSubmitItemModification(View view) {
        saveModifications();
    }

    private void saveModifications() {
        Intent modifiedData = new Intent();
        modifiedData.putExtra(kToDosItemValue, editItemTextField.getText().toString());
        modifiedData.putExtra(kToDosItemPosition, getIntent().getIntExtra(kToDosItemPosition, 0));
        setResult(RESULT_OK, modifiedData);
        finish();

    }

    private void setupEditItemTextField(final String editItemName) {
        editItemTextField.append(editItemName);

        // Only enable the save button when we have a non empty todo item name
        editItemTextField.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(!TextUtils.isEmpty(s.toString()));
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }
}
