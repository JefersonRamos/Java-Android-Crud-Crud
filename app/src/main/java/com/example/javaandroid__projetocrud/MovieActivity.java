package com.example.javaandroid__projetocrud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;

public class MovieActivity extends AppCompatActivity {

    private static boolean update = false;
    private static String auth = "16cebbceb3b94828add0492230a1be1c";

    private static String id;
    private static String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie);

        Bundle bundle = getIntent().getExtras();

        Button btnAdd = (Button) findViewById(R.id.btn_add);
        EditText movieName = (EditText) findViewById(R.id.movie_name);

        if(bundle != null){
            if(bundle.getString("movie")!= null)
            {
                update = true;
                String json = bundle.getString("movie");
                Gson gson = new Gson();
                Movies movies = gson.fromJson(json, Movies.class);

                id = movies.get_id();
                name = movies.getName();

                btnAdd.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                btnAdd.setText("Update");

                movieName.setText(name);

            }
        }else {
            update = false;
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = movieName.getText().toString();
                view = findViewById(android.R.id.content);
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if(update) {
                    if(!id.isEmpty() && !name.isEmpty()){
                        updateMovie(name, id);
                    }else {
                        Toast.makeText(getApplicationContext(), "Fill in the name field", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(!name.isEmpty()){
                        addMovie(name);
                    }else {
                        Toast.makeText(getApplicationContext(), "Fill in the name field", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void addMovie(String name) {
        CrudCrud crudCrud = new CrudCrud();
        crudCrud.setAuth(auth);

        new Thread() {
            public void run() {
                crudCrud.newMovie(name);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), name + " as added !!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();

    }

    public void updateMovie(String name, String id) {
        CrudCrud crudCrud = new CrudCrud();
        crudCrud.setAuth(auth);

        new Thread() {
            public void run() {
                crudCrud.updateMovie(name, id);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Your name has been updated.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();

    }
}