package com.example.uscrecapp;

import java.util.ArrayList;

public class Gym {
    private String name;
    private ArrayList<TimeSlot> timeSlots;

    public Gym(String name, ArrayList<TimeSlot> timeSlots) {
        this.name = name;
        this.timeSlots = timeSlots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(ArrayList<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
