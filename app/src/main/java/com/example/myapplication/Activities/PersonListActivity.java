package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Classes.DataBase;
import com.example.myapplication.Classes.Person;
import com.example.myapplication.R;

import java.util.ArrayList;

public class PersonListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btn_add_person;
    PersonListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        recyclerView = findViewById(R.id.recycler_view_person_list);
        btn_add_person = findViewById(R.id.btn_add_person);
        adapter = new PersonListAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btn_add_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonListActivity.this, AddPersonActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.refreshFromDataBase();
    }
}

class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.MyViewHolder> {

    private ArrayList<Person> people;
    private View.OnClickListener listener;
    private long last_click_time = 0;
    private int last_click_position = -1;
    private DataBase dataBase;

    PersonListAdapter(final Context context) {
        dataBase = new DataBase(context);
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long time = System.currentTimeMillis();
                int current_position = (int) v.getTag();
                String msg;

                if (time - last_click_time > 2000 || last_click_position != current_position) {
                    msg = "برای حذف دوباره دکمه را لمس کنید";
                    last_click_time = time;
                    last_click_position = current_position;
                } else {
                    last_click_time = 0;
                    last_click_position = -1;
                    int result = dataBase.removePerson(people.get(current_position).getId());
                    if (result >= 1) {
                        msg = "شخص با موفقیت حذف شد";
                        people.remove(current_position);
                        notifyDataSetChanged();
                    } else {
                        msg = "عملیات حذف شخص با خطا همراه بود";
                    }
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        };
        refreshFromDataBase();
    }

    public void refreshFromDataBase() {
        people = dataBase.getPeople("", Person.KEY_FIRST_NAME);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Person person = people.get(position);
        holder.tv_name.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));
        holder.btn_remove.setTag(position);
        //holder.btn_remove.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        Button btn_remove;

        MyViewHolder(@NonNull View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_full_name);
            btn_remove = view.findViewById(R.id.btn_remove_person);
            btn_remove.setOnClickListener(listener);
        }
    }


}

