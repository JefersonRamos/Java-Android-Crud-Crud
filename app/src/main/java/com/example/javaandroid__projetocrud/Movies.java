package com.example.javaandroid__projetocrud;

import android.content.Context;
import android.widget.Toast;

public class Movies {
    private String name;
    private String _id;
    private boolean confirm_delete = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean delete(Context context) {
        if(confirm_delete == false) {
            Toast.makeText(context, "Are you sure you want to delete this item? If yes click again.", Toast.LENGTH_SHORT).show();
            confirm_delete = true;
            return false;
        }else {

            return confirm_delete;
        }
    }
}
