package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.Classes.Bedehi;
import com.example.myapplication.Classes.DataBase;
import com.example.myapplication.Classes.Person;
import com.example.myapplication.Classes.Purchase;
import com.example.myapplication.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    TextView tv_no_purchase;
    RecyclerView recyclerView;
    PurchaseListAdapter adapter;
    ArrayList<Integer> purchasesID;
    boolean showAllPurchasesFromDB = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
*/

        purchasesID = getIntent().getIntegerArrayListExtra("purchases");
        if (purchasesID != null) {
            showAllPurchasesFromDB = false;
        }

        tv_no_purchase = findViewById(R.id.tv_no_purchase);
        recyclerView = findViewById(R.id.recycler_view_purchases);
        adapter = new PurchaseListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (showAllPurchasesFromDB) {
            adapter.refresh();
        }
    }

    MenuItem personList;
    MenuItem addPurchase;
    MenuItem privatePage;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showAllPurchasesFromDB) {
            personList = menu.add("لیست اشخاص");
            addPurchase = menu.add("خرید جدید");
            privatePage = menu.add("صفحه شخصی");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (showAllPurchasesFromDB) {
            if (item == personList) {
                startActivity(new Intent(MainActivity.this, PersonListActivity.class));
            } else if (item == addPurchase) {
                startActivity(new Intent(MainActivity.this, AddPurchaseActivity.class));
            } else if (item == privatePage) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    class PurchaseListAdapter extends RecyclerView.Adapter<PurchaseListAdapter.MyViewHolder> {

        public ArrayList<Purchase> purchases;
        public Context context;
        public DataBase dataBase;

        public PurchaseListAdapter(Context context) {
            this.context = context;
            dataBase = new DataBase(context);
            refresh();
        }

        public void refresh() {
            if (MainActivity.this.showAllPurchasesFromDB) {
                purchases = dataBase.getPurchases("", Purchase.KEY_ID + " DESC");
            } else {
                purchases = new ArrayList<>(purchasesID.size() + 1);
                for (int i = 0; i < purchasesID.size(); i++) {
                    purchases.add(dataBase.idToPurchase(purchasesID.get(i)));
                }
            }
            if (purchases.size() == 0) {
                tv_no_purchase.setVisibility(View.VISIBLE);
            } else {
                tv_no_purchase.setVisibility(View.GONE);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PurchaseListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchases_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PurchaseListAdapter.MyViewHolder holder, int position) {
            Purchase purchase = purchases.get(position);
            holder.tv_title.setText(purchase.getTitle());
            if (purchase.getDesc().isEmpty()) {
                holder.tv_desc.setVisibility(View.GONE);
            } else {
                holder.tv_desc.setText(purchase.getDesc());
            }
            holder.tv_price.setText(String.valueOf(purchase.getPrice()));
            holder.tv_date.setText(purchase.getDate());
            ArrayList<Bedehi> bedehis = dataBase.getBedehiesAsList(Bedehi.KEY_PURCHASE + "=" + purchase.getId());
            holder.tv_users_count.setText(String.valueOf(bedehis.size()));

            StringBuilder users_text = new StringBuilder();
            for (int i = 0; i < bedehis.size(); i++) {
                Bedehi bedehi = bedehis.get(i);
                Person person = dataBase.idToPerson(bedehi.getFrom().getId());

                users_text.append(person.getFirstName()).append(" ").append(person.getLastName()).
                        append("(").append(bedehi.getAmount()).append(")").append("\n");
            }
            if (users_text.length() != 0) {
                users_text.deleteCharAt(users_text.length() - 1);
            }
            holder.users_list_layout.removeAllViews();
            TextView tv;

            tv = new TextView(context);
            tv.setText(users_text);
            holder.users_list_layout.addView(tv);

            tv = new TextView(context);
            tv.setText(String.format("purchaser : %s %s", purchase.getWho().getFirstName(), purchase.getWho().getLastName()));
            holder.users_list_layout.addView(tv);
        }

        @Override
        public int getItemCount() {
            return purchases.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_title, tv_price, tv_date, tv_users_count, tv_desc;
            public LinearLayout users_list_layout;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_title = view.findViewById(R.id.tv_title);
                tv_price = view.findViewById(R.id.tv_price);
                tv_date = view.findViewById(R.id.tv_date);
                tv_users_count = view.findViewById(R.id.tv_users_count);
                tv_desc = view.findViewById(R.id.tv_desc);
                users_list_layout = view.findViewById(R.id.users_list_layout);
            }
        }
    }

}

