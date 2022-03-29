package com.example.uscrecapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class SummaryPage extends AppCompatActivity {
    private static final String TAG = "SummaryPage";
    private Calendar cal = Calendar.getInstance();
    private Date currTime = new Date();
    public static String docName = "syuenSee";

    public static SummaryPage singleton;
    public static SummaryPage getInstance(){
        return singleton;
    }
    public String selectedGym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_page);

        singleton = this;

        Intent intent = getIntent();
        String message = intent.getStringExtra("msg");
        String selectedGym = intent.getStringExtra("gymFrom");
        String username = intent.getStringExtra("name");
        if(message == "display booking"){
            displayBookings();
        }else{
            // passed a user name
            // find documentPath
//            docName = toCamelCase(message);
//            Log.d(TAG, "docName: " + docName);
        }


        // make variables
        TextView studentName = findViewById(R.id.studentName);
        TextView studentID = findViewById(R.id.studentID);
        ImageView img = findViewById(R.id.myimage);

        // initialize db
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // retrieve contents from document
        DocumentReference docRef = db.collection("users").document(docName);
        String finalDocName = docName;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());


                        // populate user info
                        Map<String, Object> map = (Map<String, Object>) document.getData();
                        String name = (String) map.get("name");
                        String id = (String) map.get("id");
                        String imgUrl = (String) map.get("imgUrl");
                        ArrayList<String> group = (ArrayList<String>) document.get("reservations");
                        studentName.setText(name);
                        studentID.setText(id);

                        int myImg = getStudentImg(imgUrl);
                        img.setImageResource(myImg);




                        // if group.size() > 0, check if the timestamps for each of the bookings
                        // have passed yet
                        if(group.size() > 0){

                            Log.d(TAG, "starting timestamp comparison...");
                            // iterate through each time stamp and compare to current time
                            for(int i = 0; i < group.size(); i++){
                                int finalI = i;
                                db.collection("timeslots").whereEqualTo("slot", group.get(i))
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "success " + task.getResult().size());

                                            for (QueryDocumentSnapshot docu : task.getResult()) {
                                                Log.d(TAG, docu.getId() + " =>! " + docu.getData());
                                                Map<String, Object> map = docu.getData();
                                                Timestamp gymTime = (Timestamp) map.get("timestamp");
                                                Date gymDate = gymTime.toDate();
                                                Long gTime = gymDate.getTime();
                                                Long now = currTime.getTime();

                                                // if timestamp has passed add to previous bookings array
                                                if(currTime.after(gymDate)){
                                                    Log.d(TAG, "passed gym booking");

                                                    // add booking to map.get("previous")
                                                    db.collection("users").document(finalDocName)
                                                            .update("previous", FieldValue.arrayUnion(group.get(finalI)));

                                                    // delete booking from document.get("reservations")
                                                    db.collection("users").document(finalDocName)
                                                            .update("reservations", FieldValue.arrayRemove(group.get(finalI)));
//
                                                }

                                                Log.d(TAG, "ITERATION " + finalI + " " + docu.getId() + " --> " + document.getData());
                                            }

                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                            }
                        }

                    } else {
                        Toast.makeText(SummaryPage.this, "No such document",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SummaryPage.this, "get failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        displayBookings();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.person);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent gymNav = new Intent(SummaryPage.this, map.class);
                        gymNav.putExtra("gymFrom", selectedGym);
                        gymNav.putExtra("name", username);
                        startActivity(gymNav);
                        break;
                    case R.id.person:
                        Intent sumNav = new Intent(SummaryPage.this, gymSlots.class);
                        sumNav.putExtra("gymFrom", selectedGym);
                        sumNav.putExtra("name", username);
                        startActivity(sumNav);
                        break;
                }
                return false;
            }
        });
    }


    public void displayBookings(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference newDocRef = db.collection("users").document(docName);
        newDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    ArrayList<String> updatedRes = (ArrayList<String>) snapshot.getData().get("reservations");
                    BookingAdapter adapter = new BookingAdapter(getApplicationContext(), R.layout.upcoming_list_view, updatedRes);
                    ListView listView = (ListView) findViewById(R.id.upcomingList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    // display previous bookings via adapter
                    ArrayList<String> updatedPrev = (ArrayList<String>) snapshot.getData().get("previous");
                    Log.d(TAG, "updatedPrev size: " + updatedPrev.size());
                    PreviousAdapter adapter2 = new PreviousAdapter(getApplicationContext(), R.layout.prev_list_view, updatedPrev);
                    ListView listView2 = (ListView) findViewById(R.id.previousList);
                    listView2.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();


                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }

    public void updateName(String name){

    }
    public int getStudentImg(String imgName){
        switch (imgName)
        {
            case "syuen":
                return R.drawable.syuen;
            case "elle":
                return R.drawable.temp;
            case "kelly":
                return R.drawable.student;
        }
        return 0;
    }

    private static String toCamelCase(String s){
        String[] parts = s.split(" ");
        String camelCaseString = "";
        int counter = 0;
        for (String part : parts){
            camelCaseString = camelCaseString + toProperCase(part, counter);
            counter++;
        }
        return camelCaseString;
    }

    private static String toProperCase(String s, int count) {
        if(count == 0){
            return s.toLowerCase();
        }
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

}

