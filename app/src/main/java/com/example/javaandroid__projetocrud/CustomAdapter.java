package com.example.javaandroid__projetocrud;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private Movies[] data;
    private String auth = "16cebbceb3b94828add0492230a1be1c";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, id;
        private final LinearLayout component;
        private final ImageView btn_del;
        public ViewHolder(@NonNull View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            id = (TextView) view.findViewById(R.id.id);
            component = (LinearLayout) view.findViewById(R.id.content);
            btn_del = (ImageView) view.findViewById(R.id.btn_del);
        }

        public TextView getNameView() {
            return name;
        }

        public TextView getIdView() {
            return id;
        }

        public LinearLayout getComponent() {
            return component;
        }

        public ImageView getBtnDel() {
            return btn_del;
        }

    }

    public CustomAdapter(Movies[] dataSet) {
        if(dataSet.length > 0){
            System.out.println(dataSet[0].getName());
        }
        data = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_row_table, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        int posi = position;
        viewHolder.getNameView().setText("Movie name: " + data[position].getName());
        viewHolder.getIdView().setText(position + " - ");
        viewHolder.getComponent().setTag(data[position]);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MovieActivity.class);
                Gson gson = new Gson();
                String json = gson.toJson(data[posi]);

                i.putExtra("movie", json);
                view.getContext().startActivity(i);
            }
        });

        viewHolder.getBtnDel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean confirm = data[posi].delete(view.getContext());
                if(confirm == true) {
                    CrudCrud crudCrud = new CrudCrud();
                    new Thread() {
                        public void run() {
                            crudCrud.deleteMovie(auth, data[posi].get_id());
                            Context context = view.getContext();
                            Activity activity = (MainActivity) getActivity(context);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    List<Movies> data_deleted = new ArrayList<Movies>();
                                    for (int i = 0; i < data.length; i++){
                                        if(data[i].get_id() == data[posi].get_id()){

                                        }else {
                                            data_deleted.add(data[i]);
                                        }
                                    }

                                    Intent i = new Intent(context, MainActivity.class);
                                    Gson gson = new Gson();

                                    String json = gson.toJson(data_deleted);
                                    i.putExtra("json", json);

                                    activity.startActivity(i);
                                    activity.finish();

                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }   
                    }.start();
                }
            }
        });
    }
    private Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return data.length;
    }


}
