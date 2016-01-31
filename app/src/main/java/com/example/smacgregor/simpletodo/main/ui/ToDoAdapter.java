package com.example.smacgregor.simpletodo.main.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.smacgregor.simpletodo.R;
import com.example.smacgregor.simpletodo.core.ToDoItem;

import java.util.List;

/**
 * Created by smacgregor on 1/29/16.
 */
public class ToDoAdapter extends ArrayAdapter<ToDoItem> {
    public ToDoAdapter(Context context, List<ToDoItem> toDoItems) {
        super(context, 0, toDoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoItem toDoItem = getItem(position);

        // is the view doesn't exist yet, inflate it
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        TextView nameView = (TextView) convertView.findViewById(R.id.toDoName);
        nameView.setText(toDoItem.name);

        return convertView;
    }
}
