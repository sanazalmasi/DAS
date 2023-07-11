package com.example.myapplication.Classes;

public class Bedehi {
    private int id;
    private Person from;
    private Person to;
    private Purchase Purchase;
    private int amount;

    public static final String
            KEY_ID = "bedehi_id",
            KEY_FROM = "from_who",
            KEY_TO = "to_who",
            KEY_PURCHASE = "purchase",
            KEY_AMOUNT = "amount";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getFrom() {
        return from;
    }

    public void setFrom(Person from) {
        this.from = from;
    }

    public Person getTo() {
        return to;
    }

    public void setTo(Person to) {
        this.to = to;
    }

    public com.example.myapplication.Classes.Purchase getPurchase() {
        return Purchase;
    }

    public void setPurchase(com.example.myapplication.Classes.Purchase purchase) {
        Purchase = purchase;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}