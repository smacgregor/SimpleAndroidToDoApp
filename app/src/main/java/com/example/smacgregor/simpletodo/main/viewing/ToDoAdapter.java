package com.example.smacgregor.simpletodo.main.viewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.smacgregor.simpletodo.R;
import com.example.smacgregor.simpletodo.core.ToDoItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by smacgregor on 1/29/16.
 */
public class ToDoAdapter extends ArrayAdapter<ToDoItem> {

    // View Lookup cache.
    // Yikes....the adapter now has to know about View specific details!
    static class ViewHolder {
        @Bind(R.id.toDoName) TextView name;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public ToDoAdapter(Context context, List<ToDoItem> toDoItems) {
        super(context, 0, toDoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoItem toDoItem = getItem(position);

        // if the view doesn't exist yet, inflate it
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(toDoItem.name);
        return convertView;
    }
}
