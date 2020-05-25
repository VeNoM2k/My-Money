package com.example.mymoney;

import com.google.firebase.Timestamp;

public class Bill {

    private String amount;
    private String date;
    private String showdate;
    private boolean imp;
    private String description;
    private Timestamp created;
    private boolean paid;
    private String userId;

    public Bill() {
    }

    public Bill(String amount, String date, String showdate, boolean imp, String description, Timestamp created, boolean paid, String userId) {
        this.amount = amount;
        this.date = date;
        this.showdate = showdate;
        this.imp = imp;
        this.description = description;
        this.created = created;
        this.paid = paid;
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

    public boolean isImp() {
        return imp;
    }

    public void setImp(boolean imp) {
        this.imp = imp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", showdate='" + showdate + '\'' +
                ", imp=" + imp +
                ", description='" + description + '\'' +
                ", created=" + created +
                ", paid=" + paid +
                ", userId='" + userId + '\'' +
                '}';
    }
}
