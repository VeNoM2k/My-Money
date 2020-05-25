package com.example.mymoney;

import java.io.Serializable;

public class Group implements Serializable {
    String groupname;
    String userId;
    int no_members;

    public Group() {
    }

    public Group(String groupname, String userId, int no_members) {
        this.groupname = groupname;
        this.userId = userId;
        this.no_members = no_members;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNo_members() {
        return no_members;
    }

    public void setNo_members(int no_members) {
        this.no_members = no_members;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupname='" + groupname + '\'' +
                ", userId='" + userId + '\'' +
                ", no_members=" + no_members +
                '}';
    }
}
