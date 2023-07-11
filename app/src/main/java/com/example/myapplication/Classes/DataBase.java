package com.example.myapplication.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    private SQLiteDatabase sqdb;

    public static final String
            db_name = "group",
            TABLE_PERSONS = "persons",
            TABLE_PURCHASES = "purchases",
            TABLE_BEDEHIES = "bedehies";

    public DataBase(@Nullable Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createTables(db);
    }

    public SQLiteDatabase openSqLiteDatabase() {
        return sqdb = getWritableDatabase();
    }

    public void createTables(SQLiteDatabase db) {
        sqdb = db;
        sqdb.execSQL("CREATE TABLE IF NOT EXISTS " + "'" + TABLE_BEDEHIES + "'" +
                "(" +
                "'" + Bedehi.KEY_ID + "' INTEGER," +
                "'" + Bedehi.KEY_AMOUNT + "' INTEGER," +
                "'" + Bedehi.KEY_FROM + "' INTEGER," +
                "'" + Bedehi.KEY_TO + "' INTEGER," +
                "'" + Bedehi.KEY_PURCHASE + "' INTEGER" +
                ")");

        sqdb.execSQL("CREATE TABLE IF NOT EXISTS " + "'" + TABLE_PERSONS + "'" +
                "(" +
                "'" + Person.KEY_ID + "' INTEGER," +
                "'" + Person.KEY_FIRST_NAME + "' TEXT," +
                "'" + Person.KEY_LAST_NAME + "' TEXT," +
                "'" + Person.KEY_USERNAME + "' TEXT," +
                "'" + Person.KEY_PASSWORD + "' TEXT" +
                ")");

        sqdb.execSQL("CREATE TABLE IF NOT EXISTS " + "'" + TABLE_PURCHASES + "'" +
                "(" +
                "'" + Purchase.KEY_ID + "' INTEGER," +
                "'" + Purchase.KEY_WHO + "' INTEGER," +
                "'" + Purchase.KEY_TITLE + "' INTEGER," +
                "'" + Purchase.KEY_PRICE + "' INTEGER," +
                "'" + Purchase.KEY_DATE + "' TEXT," +
                "'" + Purchase.KEY_IS_PUBLIC + "' BOOLEAN," +
                "'" + Purchase.KEY_DESC + "' TEXT" +
                ")");
    }

    public Person idToPerson(int person_id) {
        openSqLiteDatabase();
        Cursor person_cursor = sqdb.query(DataBase.TABLE_PERSONS, null,
                Person.KEY_ID + "=" + person_id, null, null, null, null);
        person_cursor.moveToFirst();
        return cursorToPerson(person_cursor);
    }

    public Purchase idToPurchase(int purchase_id) {
        openSqLiteDatabase();
        Cursor cursor = sqdb.query(DataBase.TABLE_PURCHASES, null,
                Purchase.KEY_ID + "=" + purchase_id, null, null, null, null);
        cursor.moveToFirst();
        return cursorToPurchase(cursor);
    }

    public ArrayList<Person> getUsers(int purchaseID) {
        openSqLiteDatabase();
        ArrayList<Bedehi> bedehis = getBedehiesAsList(Bedehi.KEY_PURCHASE + "=" + purchaseID);
        ArrayList<Person> users = new ArrayList<>();
        for (int i = 0; i < bedehis.size(); i++) {
            users.add(idToPerson(bedehis.get(i).getFrom().getId()));
        }
        return users;
    }


    public Person cursorToPerson(Cursor person_cursor) {
        Person person = new Person();
        if (person_cursor == null || person_cursor.getCount() == 0) {
            return person;
        }
        person.setId(person_cursor.getInt(person_cursor.getColumnIndex(Person.KEY_ID)));
        person.setFirstName(person_cursor.getString(person_cursor.getColumnIndex(Person.KEY_FIRST_NAME)));
        person.setLastName(person_cursor.getString(person_cursor.getColumnIndex(Person.KEY_LAST_NAME)));
        return person;
    }

    public Bedehi cursorToBedehi(Cursor bedehi_cursor) {
        Bedehi bedehi = new Bedehi();
        if (bedehi_cursor == null || bedehi_cursor.getCount() == 0) {
            return bedehi;
        }
        bedehi.setId(bedehi_cursor.getInt(bedehi_cursor.getColumnIndex(Bedehi.KEY_ID)));
        bedehi.setAmount(bedehi_cursor.getInt(bedehi_cursor.getColumnIndex(Bedehi.KEY_AMOUNT)));
        bedehi.setFrom(idToPerson(bedehi_cursor.getInt(bedehi_cursor.getColumnIndex(Bedehi.KEY_FROM))));
        bedehi.setTo(idToPerson(bedehi_cursor.getInt(bedehi_cursor.getColumnIndex(Bedehi.KEY_TO))));
        bedehi.setPurchase(idToPurchase(bedehi_cursor.getInt(bedehi_cursor.getColumnIndex(Bedehi.KEY_PURCHASE))));
        return bedehi;
    }

    public Purchase cursorToPurchase(Cursor purchase_cursor) {
        Purchase purchase = new Purchase();
        if (purchase_cursor == null || purchase_cursor.getCount() == 0) {
            return purchase;
        }
        purchase.setId(purchase_cursor.getInt(purchase_cursor.getColumnIndex(Purchase.KEY_ID)));
        purchase.setPrice(purchase_cursor.getInt(purchase_cursor.getColumnIndex(Purchase.KEY_PRICE)));
        purchase.setTitle(purchase_cursor.getString(purchase_cursor.getColumnIndex(Purchase.KEY_TITLE)));
        purchase.setDesc(purchase_cursor.getString(purchase_cursor.getColumnIndex(Purchase.KEY_DESC)));
        purchase.setDate(purchase_cursor.getString(purchase_cursor.getColumnIndex(Purchase.KEY_DATE)));
        purchase.setPublic(purchase_cursor.getInt(purchase_cursor.getColumnIndex(Purchase.KEY_IS_PUBLIC)) != 0);
        purchase.setWho(idToPerson(purchase_cursor.getInt(purchase_cursor.getColumnIndex(Purchase.KEY_WHO))));
        return purchase;
    }

    public ArrayList<Person> getPeople(String where, String sortBy) {
        openSqLiteDatabase();
        Cursor person_cursor = sqdb.query(DataBase.TABLE_PERSONS, null,
                where, null, null, null, sortBy);

        ArrayList<Person> people = new ArrayList<>(person_cursor.getCount() + 1);
        if (person_cursor.moveToFirst()) {
            do {
                people.add(cursorToPerson(person_cursor));
            } while (person_cursor.moveToNext());
        }

        person_cursor.close();
        return people;
    }

    public void pay(int from, int to) {
        openSqLiteDatabase();
        sqdb.execSQL("delete from " + DataBase.TABLE_BEDEHIES + " where " +
                Bedehi.KEY_FROM + " = " + from + " and " +
                Bedehi.KEY_TO + " = " + to);
    }

    public ArrayList<Purchase> getPurchases(String where, String sortBy) {
        openSqLiteDatabase();
        ArrayList<Purchase> purchases = new ArrayList<>();
        Cursor cursor = sqdb.query(DataBase.TABLE_PURCHASES, null,
                where, null, null, null, sortBy);

        if (cursor.moveToFirst()) {
            do {
                purchases.add(cursorToPurchase(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return purchases;
    }

    public Cursor getBedehiesAsCursor(String where) {
        openSqLiteDatabase();
        return sqdb.query(DataBase.TABLE_BEDEHIES, null,
                where, null, null, null, null);
    }

    public ArrayList<Bedehi> getBedehiesAsList(String where) {
        openSqLiteDatabase();
        ArrayList<Bedehi> bedehis = new ArrayList<>();
        Cursor cursor = getBedehiesAsCursor(where);
        if (cursor.moveToFirst()) {
            do {
                bedehis.add(cursorToBedehi(cursor));
            } while (cursor.moveToNext());
        }

        return bedehis;
    }

    public void getRelatedPurchases(int PersonID_1, int PersonID_2) {
        openSqLiteDatabase();

    }

    public int getPeopleCount(String where) {
        openSqLiteDatabase();
        Cursor cursor = sqdb.query(DataBase.TABLE_PERSONS, null,
                where, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void addPerson(ContentValues person) {
        openSqLiteDatabase();
        sqdb.insert(TABLE_PERSONS, null, person);
    }

    public int removePerson(int person_id) {
        openSqLiteDatabase();
        return sqdb.delete(TABLE_PERSONS, Person.KEY_ID + "=" + person_id, null);
    }

    public boolean addPurchaseAndBedehies(ContentValues purchase, LinearLayout switches, Context context) {
        int checkedSwitches = 0;
        for (int i = 0; i < switches.getChildCount(); i++) {
            Switch aswitch = (Switch) switches.getChildAt(i);
            if (aswitch.isChecked()) {
                checkedSwitches++;
            }
        }

        if (checkedSwitches == 0) {
            return false;
        }

        openSqLiteDatabase();
        sqdb.insert(TABLE_PURCHASES, null, purchase);

        for (int i = 0; i < switches.getChildCount(); i++) {
            Switch aswitch = (Switch) switches.getChildAt(i);
            if (aswitch.isChecked()) {
                Person person = (Person) aswitch.getTag();
                ContentValues bedehi = new ContentValues();
                bedehi.put(Bedehi.KEY_ID, SharedPreferences.getNextBedehiID(context));
                bedehi.put(Bedehi.KEY_AMOUNT, purchase.getAsInteger(Purchase.KEY_PRICE) / checkedSwitches);
                bedehi.put(Bedehi.KEY_FROM, person.getId());
                bedehi.put(Bedehi.KEY_TO, purchase.getAsInteger(Purchase.KEY_WHO));
                bedehi.put(Bedehi.KEY_PURCHASE, purchase.getAsInteger(Purchase.KEY_ID));
                sqdb.insert(TABLE_BEDEHIES, null, bedehi);
                bedehi.clear();
            }
        }
        return true;
    }

    public void dropAllTables() {
        openSqLiteDatabase();
        sqdb.execSQL("DROP TABLE IF EXISTS " + TABLE_BEDEHIES);
        sqdb.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASES);
        sqdb.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONS);
    }

}

