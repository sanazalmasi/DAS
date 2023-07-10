package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Classes.DataBase;
import com.example.myapplication.Classes.Person;
import com.example.myapplication.Classes.SharedPreferences;
import com.example.myapplication.R;

public class AddPersonActivity extends AppCompatActivity {

    EditText et_firstName, et_lastName, et_user_name, et_password;
    Button btn_submit, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        et_firstName = findViewById(R.id.et_first_name);
        et_lastName = findViewById(R.id.et_last_name);
        et_user_name = findViewById(R.id.et_user_name);
        et_password = findViewById(R.id.et_password);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_submit) {
                    DataBase dataBase = new DataBase(AddPersonActivity.this);
                    String firstName = et_firstName.getText().toString().trim();
                    String lastName = et_lastName.getText().toString().trim();
                    String userName = et_user_name.getText().toString();
                    String password = et_password.getText().toString();

                    boolean error = false;
                    if (firstName.length() == 0) {
                        et_firstName.setError("نمی تواند خالی باشد");
                        error = true;
                    }
                    if (lastName.length() == 0) {
                        et_lastName.setError("نمی تواند خالی باشد");
                        error = true;
                    }
                    if (userName.length() == 0) {
                        et_user_name.setError("نمی تواند خالی باشد");
                        error = true;
                    }
                    if (password.length() == 0) {
                        et_password.setError("نمی تواند خالی باشد");
                        error = true;
                    }
                    if (error) {
                        return;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Person.KEY_ID, SharedPreferences.getNextPersonID(AddPersonActivity.this));
                    contentValues.put(Person.KEY_FIRST_NAME, firstName);
                    contentValues.put(Person.KEY_LAST_NAME, lastName);
                    contentValues.put(Person.KEY_USERNAME, userName);
                    contentValues.put(Person.KEY_PASSWORD, password);
                    dataBase.addPerson(contentValues);
                    String msg = "شخص جدید با موفقیت اضافه شد";
                    Toast.makeText(AddPersonActivity.this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (v == btn_cancel) {
                    finish();
                }
            }
        };

        btn_cancel.setOnClickListener(onClickListener);
        btn_submit.setOnClickListener(onClickListener);

    }
}
