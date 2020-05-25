package com.example.mymoney;

public class Budget {
    String userId;
    String budget;

    public Budget() {
    }

    public Budget(String userId, String budget) {
        this.userId = userId;
        this.budget = budget;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "userId='" + userId + '\'' +
                ", budget=" + budget +
                '}';
    }
}
