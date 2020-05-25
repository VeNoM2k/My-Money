package com.example.mymoney;

import com.google.firebase.Timestamp;

public class Expense {

    private String amount;
    private String date;
    private String showdate;
    private String des;
    private Timestamp created;
    private String userId;

    public Expense() {
    }

    public Expense(String amount, String date, String showdate, String des, Timestamp created, String userId) {
        this.amount = amount;
        this.date = date;
        this.showdate = showdate;
        this.des = des;
        this.created = created;
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShowdate() {
        return showdate;
    }

    public void setShowdate(String showdate) {
        this.showdate = showdate;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", showdate='" + showdate + '\'' +
                ", des='" + des + '\'' +
                ", created=" + created +
                ", userId='" + userId + '\'' +
                '}';
    }
}
