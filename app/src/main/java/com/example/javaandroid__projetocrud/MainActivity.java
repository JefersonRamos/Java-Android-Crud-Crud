package com.example.javaandroid__projetocrud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    boolean loading = true;
    int statusApi;
    CrudCrud crudAuth;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    private String auth = "16cebbceb3b94828add0492230a1be1c";
    private boolean read = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        progressBar = findViewById(R.id.loadingAPI);

        if(bundle != null) {
            if (bundle.getString("json") != null) {
                read = false;
                progressBar.setVisibility(View.GONE);
                String json = bundle.getString("json");

                Gson gson = new Gson();

                Movies[] movies = gson.fromJson(json, Movies[].class);
                listData(movies);
            }
        }

        if(read == true) {
            progressBar.setVisibility(View.VISIBLE);

            crudAuth = new CrudCrud();
            crudAuth.setAuth(auth);
            conexionCrud();
        }


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MovieActivity.class);
                startActivity(intent);
            }
        });
    }

    public void conexionCrud() {
        new Thread() {
            public void run() {
                int conexion = (int) crudAuth.conexion();
                statusApi = conexion;
                if(conexion == 200) {
                    loading = false;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            new Thread() {
                                public void run() {
                                    String response = crudAuth.getMoviesAll();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            ObjectMapper mapper = new ObjectMapper();
                                            try {
                                                Movies[] movies = mapper.readValue(response, Movies[].class);
                                                listData(movies);
                                            } catch (JsonProcessingException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }.start();
                        }
                    });
                }
            }
        }.start();
    }

    public void listData(Movies[] data) {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        CustomAdapter mAdapter = new CustomAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}