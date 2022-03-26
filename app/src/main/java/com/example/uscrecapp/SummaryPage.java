package com.example.uscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class SummaryPage extends AppCompatActivity {
    private static final String TAG = "SummaryPage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_page);

        // make variables
        TextView studentName = findViewById(R.id.studentName);
        TextView studentID = findViewById(R.id.studentID);
        TextView gymName = findViewById(R.id.gymName);
        TextView dateAndTime = findViewById((R.id.dateAndTime));


        // initialize db
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // retrieve contents from document
        DocumentReference docRef = db.collection("users").document("syuenSee");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> map = (Map<String, Object>) document.getData();
                        String name = (String) map.get("name");
                        String id = (String) map.get("id");
                        List<String> group = (List<String>) document.get("reservations");
                        studentName.setText(name);
                        studentID.setText(id);

                        // display timeslot data
                        // if multiple timeslots, create for loop and iterate through group list
                        db.collection("timeslots").whereEqualTo("slot", group.get(0))
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "success " + task.getResult().size());

                                    for (QueryDocumentSnapshot docu : task.getResult()) {
                                        Log.d(TAG, docu.getId() + " => " + docu.getData());
                                        Map<String, Object> map = docu.getData();
                                        String gym = (String) map.get("gymName");
                                        String day = (String) map.get("day");
                                        String time = (String) map.get("time");
                                        gymName.setText(gym);
                                        dateAndTime.setText(day + " " + time);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });






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



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.person);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent gymNav = new Intent(SummaryPage.this, MainActivity.class);
                        startActivity(gymNav);
                        break;
                    case R.id.person:
                        Intent sumNav = new Intent(SummaryPage.this, SummaryPage.class);
                        startActivity(sumNav);
                        break;
                }
                return false;
            }
        });
    }
}