package com.example.myapplication.Classes;

import androidx.annotation.NonNull;

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;

    public static final String
            KEY_ID = "person_id",
            KEY_FIRST_NAME = "first_name",
            KEY_LAST_NAME = "last_name",
            KEY_USERNAME = "user_name",
            KEY_PASSWORD = "password";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
