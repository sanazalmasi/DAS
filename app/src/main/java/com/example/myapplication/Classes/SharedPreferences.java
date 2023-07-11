package com.example.myapplication.Classes;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.myapplication.Activities.AddPersonActivity;

public class SharedPreferences {

    public static int getNextPersonID(Context context) {
        android.content.SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int result = pref.getInt(Person.KEY_ID, 1);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Person.KEY_ID, result + 1);
        editor.apply();
        return result;
    }

    public static int getNextPurchaseID(Context context) {
        android.content.SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int result = pref.getInt(Purchase.KEY_ID, 1);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Purchase.KEY_ID, result + 1);
        editor.apply();
        return result;
    }

    public static int getNextBedehiID(Context context) {
        android.content.SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int result = pref.getInt(Bedehi.KEY_ID, 1);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Bedehi.KEY_ID, result + 1);
        editor.apply();
        return result;
    }
}
