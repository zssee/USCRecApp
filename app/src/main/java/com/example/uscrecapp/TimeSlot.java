package com.example.uscrecapp;

import java.util.ArrayList;

public class TimeSlot {
    private ArrayList<User> users;
    private ArrayList<User> waitlist;
    private String date;
    private String time;
    private Integer capacity;

    public TimeSlot(ArrayList<User> users, ArrayList<User> waitlist, String date, String time, Integer capacity) {
        this.users = users;
        this.waitlist = waitlist;
        this.date = date;
        this.time = time;
        this.capacity = capacity;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getWaitlist() {
        return waitlist;
    }

    public void setWaitlist(ArrayList<User> waitlist) {
        this.waitlist = waitlist;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
