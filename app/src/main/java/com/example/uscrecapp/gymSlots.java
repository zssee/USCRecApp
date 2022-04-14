package com.example.uscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class gymSlots extends AppCompatActivity{
    private static final String TAG = "gymSlots";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int signedUpTemp = 0;
    private long capacityTemp = 0;
    private String selectedDay = "";
    public static String selectedGym = "Lyon";
    public static String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_slots);

        setGymTitle();
        Intent intent = getIntent();
        message = intent.getStringExtra("msg");
        SummaryPage.docName = message;
        if (message == null){
            Log.d(null, "message was null in gym slots");
        }
        // initialize variables
        //Spinner
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(gymSlots.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.days));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        selectedDay = mySpinner.getSelectedItem().toString();

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDay = mySpinner.getSelectedItem().toString();
                Log.d(TAG, selectedDay);
                db.collection("timeslots").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                String time = (String)map.get("time");
                                String slot = (String)map.get("slot");

                                changeCapacityText(slot);

                            }

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //buttons
        setUpButtons();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent gymNav = new Intent(gymSlots.this, home.class);
                        gymNav.putExtra("msg", message);
                        startActivity(gymNav);
                        break;
                    case R.id.person:
                        Intent sumNav = new Intent(gymSlots.this, SummaryPage.class);
                        sumNav.putExtra("msg", message);
                        startActivity(sumNav);
                        break;
                }
                return false;
            }
        });
    }

    public void setGymTitle(){
        TextView gymTitle = findViewById(R.id.gymTitle);
        if(selectedGym.equals("lyon")){
            gymTitle.setText("Lyon Gym");
        }
        else if(selectedGym.equals("village")){
            gymTitle.setText("Village Gym");
        }
        else{
            gymTitle.setText("Uytengsu Gym");
        }
    }

    public void changeCapacityText(String timeslot){
        DocumentReference docRef = db.collection("timeslots").document(timeslot);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = (Map<String, Object>) document.getData();
                        List<String> signedUp = (List<String>)map.get("signedUp");
                        Long capacity = (Long) map.get("capacity");
                        String time = (String)map.get("time");
                        String day = (String)map.get("day");
                        String gym = (String)map.get("gymName");
                        if(gym.equals("Uytengsu")){
                            gym = "uy";
                        }

                        Log.d(TAG, " " + capacity + " "+ day + " " + time + " "+ gym + " selected: " + selectedGym);

                        boolean full = false;
                        if(signedUp.size() >= capacity.intValue()){
                            full = true;
                        }

                        if(day.equals(selectedDay) && gym.toLowerCase(Locale.ROOT).equals(selectedGym.toLowerCase(Locale.ROOT))){
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if (time.equals("8:00am-10:00am")) {
                                TextView capacityView = findViewById(R.id.capText8);
                                if(full){
                                    capacityView.setText("Capacity: FULL");
                                }
                                else{
                                    capacityView.setText("Capacity: " + Integer.toString(signedUp.size()) + "/" + Long.toString(capacity));
                                }

                            }
                            else if (time.equals("10:00am-12:00pm")) {
                                TextView capacityView = findViewById(R.id.capText10);
                                if(full){
                                    capacityView.setText("Capacity: FULL");
                                }
                                else {
                                    capacityView.setText("Capacity: " + Integer.toString(signedUp.size()) + "/" + Long.toString(capacity));
                                }
                            }
                            else if (time.equals("12:00pm-2:00pm")) {
                                TextView capacityView = findViewById(R.id.capText12);
                                if(full){
                                    capacityView.setText("Capacity: FULL");
                                }
                                else {
                                    capacityView.setText("Capacity: " + Integer.toString(signedUp.size()) + "/" + Long.toString(capacity));
                                }
                            }
                            else if (time.equals("2:00pm-4:00pm")) {
                                TextView capacityView = findViewById(R.id.capText2);
                                if(full){
                                    capacityView.setText("Capacity: FULL");
                                }
                                else {
                                    capacityView.setText("Capacity: " + Integer.toString(signedUp.size()) + "/" + Long.toString(capacity));
                                }

                            }
                            else if (time.equals("4:00pm-6:00pm")) {
                                TextView capacityView = findViewById(R.id.capText4);
                                if(full){
                                    capacityView.setText("Capacity: FULL");
                                }
                                else {
                                    capacityView.setText("Capacity: " + Integer.toString(signedUp.size()) + "/" + Long.toString(capacity));
                                }
                            }

                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void addUsertoSlot(String name, String timeslot){
        DocumentReference docRef = db.collection("timeslots").document(timeslot);
        docRef.update("signedUp", FieldValue.arrayUnion(name))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, name + " added to the list!");
                        db.collection("users").document(name).update("reservations", FieldValue.arrayUnion(timeslot));

                        Toast.makeText(gymSlots.this, "You reserved the timeslot!",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        changeCapacityText(timeslot);

    }

    public void addUsertoWaitlist(String name, String timeslot){
        DocumentReference docRef = db.collection("timeslots").document(timeslot);
        docRef.update("waitlist", FieldValue.arrayUnion(name))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, name + " added to the waitlist!");
                        Toast.makeText(gymSlots.this, "You are on the waitlist!",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }

    public void removeUserfromSlot(String name, String timeslot){
        DocumentReference docRef = db.collection("timeslots").document(timeslot);
        docRef.update("signedUp", FieldValue.arrayRemove(name))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully removed " + name + "!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error removing user", e);
                    }
                });
    }

    private String shortenDay(String day){
        if(day.equals("Monday")){
            return day.substring(0,3);
        }
        else if(day.equals("Tuesday")){
            return day.substring(0,4);
        }
        else{
            return day.substring(0,3);
        }
    }

    private void setUpButtons() {
        //buttons
        Button reserve8 = findViewById(R.id.reserve8);
        Button reserve10 = findViewById(R.id.reserve10);
        Button reserve12 = findViewById(R.id.reserve12);
        Button reserve2 = findViewById(R.id.reserve2);
        Button reserve4 = findViewById(R.id.reserve4);

        Button remind8 = findViewById(R.id.remind8);
        Button remind10 = findViewById(R.id.remind10);
        Button remind12 = findViewById(R.id.remind12);
        Button remind2 = findViewById(R.id.remind2);
        Button remind4 = findViewById(R.id.remind4);

        // ----- 8AM-10AM BUTTONS
        reserve8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "8-10");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() < capacity) {
                                    addUsertoSlot(message, gymDay + "8-10");
                                }
                                else{
                                    Toast.makeText(gymSlots.this, "Timeslot is at capacity!",
                                            Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        remind8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "8-10");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() >= capacity) {
                                    addUsertoWaitlist(message, gymDay + "8-10");
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });


        // ----- 10AM-12PM BUTTONS
        reserve10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "10-12");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() < capacity) {
                                    addUsertoSlot(message, gymDay + "10-12");
                                }
                                else{
                                    Toast.makeText(gymSlots.this, "Timeslot is at capacity!",
                                            Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        remind10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "10-12");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() >= capacity) {
                                    addUsertoWaitlist(message, gymDay + "10-12");
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });


        // ----- 12PM-2PM BUTTONS
        reserve12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "12-2");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() < capacity) {
                                    addUsertoSlot(message, gymDay + "12-2");
                                }
                                else{
                                    Toast.makeText(gymSlots.this, "Timeslot is at capacity!",
                                            Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        remind12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "12-2");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() >= capacity) {
                                    addUsertoWaitlist(message, gymDay + "12-2");
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        // ----- 2PM-4PM BUTTONS
        reserve2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "2-4");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() < capacity) {
                                    addUsertoSlot(message, gymDay + "2-4");
                                }
                                else{
                                    Toast.makeText(gymSlots.this, "Timeslot is at capacity!",
                                            Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        remind2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "2-4");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() >= capacity) {
                                    addUsertoWaitlist(message, gymDay + "2-4");
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        // ----- 4PM-6PM BUTTONS
        reserve4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "4-6");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() < capacity) {
                                    addUsertoSlot(message, gymDay + "4-6");
                                }
                                else{
                                    Toast.makeText(gymSlots.this, "Timeslot is at capacity!",
                                            Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        remind4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gymDay = selectedGym.toLowerCase(Locale.ROOT) + shortenDay(selectedDay);
                Log.d(TAG, "DAY!!! " + gymDay);
                DocumentReference docRef = db.collection("timeslots").document(gymDay + "4-6");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Map<String, Object> map = (Map<String, Object>) document.getData();
                                List<String> signedUp = (List<String>) map.get("signedUp");
                                long capacity = (long) map.get("capacity");
                                if (signedUp.size() >= capacity) {
                                    addUsertoWaitlist(message, gymDay + "4-6");
                                }

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });


    }



}