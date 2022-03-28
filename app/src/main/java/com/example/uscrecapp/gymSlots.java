package com.example.uscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.List;
import java.util.Map;

public class gymSlots extends AppCompatActivity{
    private static final String TAG = "gymSlots";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int signedUpTemp = 0;
    private long capacityTemp = 0;
    private String selectedDay = "";
    private String selectedGym = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_slots);


        // initialize variables
        //Spinner
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(gymSlots.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.days));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        selectedDay = mySpinner.getSelectedItem().toString();

        //button
        Button reserve8 = findViewById(R.id.reserve8);

        //creating button click listeners
        reserve8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCapacity("lyonMon8-10");
                if(checkCapacity("lyonMon8-10")){
                    addUsertoSlot(SummaryPage.docName, "lyonMon8-10");
                }
            }
        });

        Button remind8 = findViewById(R.id.remind8);
        remind8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkCapacity("lyonMon8-10")){
                    addUsertoWaitlist(SummaryPage.docName, "lyonMon8-10");
                }
            }
        });

        Button reserve10 = findViewById(R.id.reserve10);
        reserve10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCapacity("lyonMon10-12")){
                    addUsertoSlot(SummaryPage.docName, "lyonMon10-12");
                }
            }
        });

        Button reserve12 = findViewById(R.id.reserve12);
        reserve12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCapacity("lyonMon12-2")){
                    addUsertoSlot(SummaryPage.docName, "lyonMon12-2");
                }
            }
        });



        //getting capacities from firestore and displaying
        db.collection("timeslots").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        Map<String, Object> map = (Map<String, Object>) document.getData();
                        String time = (String)map.get("time");
                        String slot = (String)map.get("slot");

                        //TO DO: need to also check which day the user selects to see (whenever a fragment is clicked)
                        changeCapacityText(slot);

                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent gymNav = new Intent(gymSlots.this, MainActivity.class);
                        startActivity(gymNav);
                        break;
                    case R.id.person:
                        Intent sumNav = new Intent(gymSlots.this, SummaryPage.class);
                        startActivity(sumNav);
                        break;
                }
                return false;
            }
        });
    }

    public void setGymTitle(String gym){
        TextView gymTitle = findViewById(R.id.gymTitle);
        gymTitle.setText(gym +" Gym");
        selectedGym = gym;
    }

    public void changeCapacityText(String timeslot){
        DocumentReference docRef = db.collection("timeslots").document(timeslot);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = (Map<String, Object>) document.getData();
                        List<String> signedUp = (List<String>)map.get("signedUp");
                        long capacity = (long) map.get("capacity");
                        String time = (String)map.get("time");
                        String day = (String)map.get("day");

                        boolean full = false;
                        if(signedUp.size() >= capacity){
                            full = true;
                        }

                        if(day.equals(selectedDay)){
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

    boolean checkCapacity(String timeslot){
        DocumentReference docRef = db.collection("timeslots").document(timeslot);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = (Map<String, Object>) document.getData();
                        List<String> signedUp = (List<String>)map.get("signedUp");
                        signedUpTemp = signedUp.size();
                        capacityTemp = (long) map.get("capacity");
                        Log.d(TAG, " firestore "+ signedUpTemp + " " + capacityTemp);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        Log.d(TAG, " hi"+ signedUpTemp + " " + capacityTemp);
        if(signedUpTemp < capacityTemp){
            return true;
        }
        else{
            return false;
        }

    }

    void addUsertoSlot(String name, String timeslot){
        changeCapacityText(timeslot);

        DocumentReference docRef = db.collection("timeslots").document(timeslot);
        docRef.update("signedUp", FieldValue.arrayUnion(name))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        db.collection("users").document(name).update("reservations", FieldValue.arrayUnion(timeslot));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });


    }

    void addUsertoWaitlist(String name, String timeslot){
        DocumentReference docRef = db.collection("timeslots").document(timeslot);
        docRef.update("waitlist", FieldValue.arrayUnion(name))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }






}