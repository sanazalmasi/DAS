package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.A;
import com.example.myapplication.Classes.Bedehi;
import com.example.myapplication.Classes.DataBase;
import com.example.myapplication.Classes.Person;
import com.example.myapplication.R;

import java.util.ArrayList;

public class PrivatePageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BedehiListAdapter adapter;
    Button btn_back_to_main, btn_back_to_login;
    View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_page);

        int personID = getIntent().getIntExtra(Person.KEY_ID, -1);

        recyclerView = findViewById(R.id.recycler_view_bedehies);
        btn_back_to_login = findViewById(R.id.btn_back_to_login);
        btn_back_to_main = findViewById(R.id.btn_back_to_main);

        adapter = new BedehiListAdapter(PrivatePageActivity.this, personID);
        recyclerView.setLayoutManager(new LinearLayoutManager(PrivatePageActivity.this));
        recyclerView.setAdapter(adapter);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_back_to_main) {
                    A.finish_login_activity = true;
                    finish();
                } else if (v == btn_back_to_login) {
                    finish();
                }
            }
        };

        btn_back_to_main.setOnClickListener(listener);
        btn_back_to_login.setOnClickListener(listener);

    }

    class BedehiListAdapter extends RecyclerView.Adapter<BedehiListAdapter.MyViewHolder> {

        public ArrayList<Person> people;
        public ArrayList<Integer> bedehiAmount;
        public ArrayList<Bedehi> allBedehis;
        public ArrayList<Integer>[] peoplePurchasesID;
        public View.OnClickListener listener;
        public DataBase dataBase;
        public int fromID;

        public BedehiListAdapter(Context context, final int fromID) {
            this.fromID = fromID;
            dataBase = new DataBase(context);
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    if (v.getId() == R.id.btn_pay) {
                        dataBase.pay(fromID, people.get(position).getId());
                        dataBase.pay(people.get(position).getId(), fromID);
                        refreshFromDatabase();
                        notifyDataSetChanged();
                    } else if (v.getId() == R.id.btn_show_related_purchases) {
                        Intent intent = new Intent(PrivatePageActivity.this, MainActivity.class);
                        intent.putExtra("purchases", peoplePurchasesID[position]);
                        startActivity(intent);
                    }
                }
            };
            refreshFromDatabase();
        }

        public void refreshFromDatabase() {
            people = dataBase.getPeople(Person.KEY_ID + "!=" + fromID, null);
            peoplePurchasesID = new ArrayList[people.size()];
            bedehiAmount = new ArrayList<>(people.size() + 1);
            allBedehis = dataBase.getBedehiesAsList(
                    Bedehi.KEY_FROM + "='" + fromID + "' OR " + Bedehi.KEY_TO + "='" + fromID + "'"
            );
            for (int i = 0; i < people.size(); i++) {
                bedehiAmount.add(calculateBedehiesTo(i, people.get(i).getId()));
            }
        }

        private int calculateBedehiesTo(int toIndex, int toID) {
            peoplePurchasesID[toIndex] = new ArrayList<>();
            int ans = 0;
            for (int i = 0; i < allBedehis.size(); i++) {
                Bedehi bedehi = allBedehis.get(i);
                if (bedehi.getFrom().getId() == fromID && bedehi.getTo().getId() == toID) {
                    ans += bedehi.getAmount();
                    peoplePurchasesID[toIndex].add(bedehi.getPurchase().getId());
                } else if (bedehi.getFrom().getId() == toID && bedehi.getTo().getId() == fromID) {
                    ans -= bedehi.getAmount();
                    peoplePurchasesID[toIndex].add(bedehi.getPurchase().getId());
                }
            }
            return ans;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bedehi_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tv_person_name.setText(people.get(position).toString());
            holder.tv_amount.setText(String.valueOf(bedehiAmount.get(position)));
            holder.btn_show_related_purchases.setTag(position);
            holder.btn_pay.setTag(position);
        }

        @Override
        public int getItemCount() {
            return people.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public Button btn_pay, btn_show_related_purchases;
            public TextView tv_person_name;
            public TextView tv_amount;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_person_name = view.findViewById(R.id.tv_person_name);
                tv_amount = view.findViewById(R.id.tv_amount);

                btn_show_related_purchases = view.findViewById(R.id.btn_show_related_purchases);
                btn_pay = view.findViewById(R.id.btn_pay);

                btn_show_related_purchases.setOnClickListener(listener);
                btn_pay.setOnClickListener(listener);
            }
        }
    }
}
