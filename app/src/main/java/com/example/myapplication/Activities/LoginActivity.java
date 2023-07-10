package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.A;
import com.example.myapplication.Classes.DataBase;
import com.example.myapplication.Classes.Person;
import com.example.myapplication.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    TextView tv_wrong_user_pass;
    EditText et_user_name, et_password;
    Button btn_cancel, btn_submit;
    View.OnClickListener listener;
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_wrong_user_pass = findViewById(R.id.tv_wrong_user_pass);
        et_user_name = findViewById(R.id.et_user_name);
        et_password = findViewById(R.id.et_password);
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);

        dataBase = new DataBase(LoginActivity.this);

        tv_wrong_user_pass.setText("");
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_submit) {
                    String userName = et_user_name.getText().toString();
                    String password = et_password.getText().toString();
                    ArrayList<Person> people = dataBase.getPeople(Person.KEY_USERNAME + "='" + userName + "' AND " + Person.KEY_PASSWORD + "='" + password+"'", "");
                    if (people.size() == 1) {
                        Intent intent = new Intent(LoginActivity.this, PrivatePageActivity.class);
                        intent.putExtra(Person.KEY_ID, people.get(0).getId());
                        startActivity(intent);
                    } else if(people.size() == 0) {
                        tv_wrong_user_pass.setText("نام کاربری یا رمز اشتباه است");
                    }
                } else if (v == btn_cancel) {
                    finish();
                }
            }
        };

        btn_submit.setOnClickListener(listener);
        btn_cancel.setOnClickListener(listener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        et_user_name.setText("");
        et_password.setText("");
        tv_wrong_user_pass.setText("");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(A.finish_login_activity) {
            A.finish_login_activity = false;
            finish();
        }
    }
}
