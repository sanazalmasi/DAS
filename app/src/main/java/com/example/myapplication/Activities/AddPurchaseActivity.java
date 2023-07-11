package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myapplication.Classes.DataBase;
import com.example.myapplication.Classes.Person;
import com.example.myapplication.Classes.Purchase;
import com.example.myapplication.Classes.SharedPreferences;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddPurchaseActivity extends AppCompatActivity {

    Spinner spinner;
    Calendar calendar;
    Switch aSwitch;
    ConstraintLayout date_time_layout;
    View.OnClickListener listener;
    Button btn_cancel, btn_submit;
    Button btn_year_plus, btn_year_minus,
            btn_month_plus, btn_month_minus,
            btn_day_plus, btn_day_minus,
            btn_hour_plus, btn_hour_minus,
            btn_minute_plus, btn_minute_minus;

    TextView tv_year, tv_month, tv_day, tv_hour, tv_minute;
    EditText et_price, et_title, et_desc;
    ArrayList<Person> people;
    DataBase dataBase;
    LinearLayout layout_users;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);

        date_time_layout = findViewById(R.id.data_time_layout);

        btn_year_plus = findViewById(R.id.btn_year_plus);
        btn_year_minus = findViewById(R.id.btn_year_minus);
        btn_month_plus = findViewById(R.id.btn_month_plus);
        btn_month_minus = findViewById(R.id.btn_month_minus);
        btn_day_plus = findViewById(R.id.btn_day_plus);
        btn_day_minus = findViewById(R.id.btn_day_minus);
        btn_hour_plus = findViewById(R.id.btn_hour_plus);
        btn_hour_minus = findViewById(R.id.btn_hour_minus);
        btn_minute_plus = findViewById(R.id.btn_minute_plus);
        btn_minute_minus = findViewById(R.id.btn_minute_minus);

        tv_year = findViewById(R.id.tv_year);
        tv_month = findViewById(R.id.tv_month);
        tv_day = findViewById(R.id.tv_day);
        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);

        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);

        et_title = findViewById(R.id.et_title);
        et_price = findViewById(R.id.et_price);
        et_desc = findViewById(R.id.et_desc);

        aSwitch = findViewById(R.id.switch_is_public);
        spinner = findViewById(R.id.purchaser_spinner);

        layout_users = findViewById(R.id.layout_users);

        dataBase = new DataBase(this);
        people = dataBase.getPeople("", Person.KEY_FIRST_NAME);
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, people);
        spinner.setAdapter(adapter);


        for (int i = 0; i < people.size(); i++) {
            Switch aswitch = new Switch(AddPurchaseActivity.this);
            Person person = people.get(i);
            aswitch.setTag(person);
            aswitch.setChecked(true);
            aswitch.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));
            layout_users.addView(aswitch);
        }

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_year_plus) {
                    plus(tv_year, 2000, 2050);
                } else if (v == btn_year_minus) {
                    minus(tv_year, 2000, 2050);
                } else if (v == btn_month_plus) {
                    plus(tv_month, 1, 12);
                } else if (v == btn_month_minus) {
                    minus(tv_month, 1, 12);
                } else if (v == btn_day_plus) {
                    plus(tv_day, 1, 31);
                } else if (v == btn_day_minus) {
                    minus(tv_day, 1, 31);
                } else if (v == btn_hour_plus) {
                    plus(tv_hour, 0, 23);
                } else if (v == btn_hour_minus) {
                    minus(tv_hour, 0, 23);
                } else if (v == btn_minute_plus) {
                    plus(tv_minute, 0, 59);
                } else if (v == btn_minute_minus) {
                    minus(tv_minute, 0, 59);
                } else if (v == btn_cancel) {
                    finish();
                } else if (v == btn_submit) {
                    if (dataBase.addPurchaseAndBedehies(getPurchaseContentValues(), layout_users, AddPurchaseActivity.this)) {
                        finish();
                    }
                }
            }
        };

        btn_year_plus.setOnClickListener(listener);
        btn_year_minus.setOnClickListener(listener);
        btn_month_plus.setOnClickListener(listener);
        btn_month_minus.setOnClickListener(listener);
        btn_day_plus.setOnClickListener(listener);
        btn_day_minus.setOnClickListener(listener);
        btn_hour_plus.setOnClickListener(listener);
        btn_hour_minus.setOnClickListener(listener);
        btn_minute_plus.setOnClickListener(listener);
        btn_minute_minus.setOnClickListener(listener);

        btn_submit.setOnClickListener(listener);
        btn_cancel.setOnClickListener(listener);

        calendar = new GregorianCalendar();

        tv_year.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        tv_month.setText(String.valueOf(1 + calendar.get(Calendar.MONTH)));
        tv_day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        tv_hour.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        tv_minute.setText(String.valueOf(calendar.get(Calendar.MINUTE)));
    }

    ContentValues getPurchaseContentValues() {
        ContentValues purchase = new ContentValues();
        purchase.put(Purchase.KEY_ID, SharedPreferences.getNextPurchaseID(AddPurchaseActivity.this));
        purchase.put(Purchase.KEY_WHO, people.get(spinner.getSelectedItemPosition()).getId());
        purchase.put(Purchase.KEY_PRICE, et_price.getText().toString().trim());
        purchase.put(Purchase.KEY_TITLE, et_title.getText().toString().trim());
        purchase.put(Purchase.KEY_DATE,
                tv_year.getText().toString() + ":" +
                        tv_month.getText().toString() + ":" +
                        tv_day.getText().toString() + ":" +
                        tv_hour.getText().toString() + ":" +
                        tv_minute.getText().toString());
        purchase.put(Purchase.KEY_DESC, et_desc.getText().toString().trim());
        purchase.put(Purchase.KEY_IS_PUBLIC, aSwitch.isChecked());
        return purchase;
    }

    void plus(TextView tv, int min, int max) {
        int result = Integer.parseInt(tv.getText().toString());
        if (result == max) {
            result = min;
        } else {
            result++;
        }
        tv.setText(String.valueOf(result));
    }

    void minus(TextView tv, int min, int max) {
        int result = Integer.parseInt(tv.getText().toString());
        if (result == min) {
            result = max;
        } else {
            result--;
        }
        tv.setText(String.valueOf(result));
    }
}