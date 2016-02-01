package com.example.smacgregor.simpletodo.core;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by smacgregor on 1/28/16.
 */

@Table(name = "ToDoItems")

public class ToDoItem extends Model {

    @Column(name = "Name")
    public String name;


    @Column(name = "Position")
    public int position;


    public ToDoItem() {
        super();
    }
}
