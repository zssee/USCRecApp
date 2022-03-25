package com.example.uscrecapp;
import androidx.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String name;
    private Integer id;
    private HashMap<Gym, ArrayList<TimeSlot>> reservations;

    // we'll store images for each user in the drawable folder
    // since we can pre-load the users
    @DrawableRes
    private int img;

    public User(String name, Integer id, HashMap<Gym, ArrayList<TimeSlot>> reservations, @DrawableRes int img) {
        this.name = name;
        this.id = id;
        this.reservations = reservations;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HashMap<Gym, ArrayList<TimeSlot>> getReservations() {
        return reservations;
    }

    public void setReservations(HashMap<Gym, ArrayList<TimeSlot>> reservations) {
        this.reservations = reservations;
    }


    public void setImg(int img) {
        this.img = img;
    }

    @DrawableRes
    public int getImage() {
        return img;
    }
}
