package com.example.uscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
        Button cancelBtn = findViewById(R.id.cancelButton);
        ListView upcomingBookings = findViewById((R.id.upcomingList));




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
                        ArrayList<String> group = (ArrayList<String>) document.get("reservations");
                        studentName.setText(name);
                        studentID.setText(id);

                        Log.d(TAG, "success " + group.get(0) + " " + group.get(1) + " " + group.get(2));
                        BookingAdapter adapter = new BookingAdapter(getApplicationContext(), R.layout.upcoming_list_view, group);
                        ListView listView = (ListView) findViewById(R.id.upcomingList);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        // display timeslot data
                        // if multiple timeslots, create for loop and iterate through group list
//                        for(int i = 0; i < group.size(); i++){

//                            // dynamically create new rows (vert)
//                            LinearLayout parent = new LinearLayout(SummaryPage.this);
//                            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                            parent.setOrientation(LinearLayout.VERTICAL);
//
//                            //children of parent linearlayout (horizontal)
//                            LinearLayout row = new LinearLayout(SummaryPage.this);
//                            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//                            row.setOrientation(LinearLayout.HORIZONTAL);
//                            parent.addView(row);
//
//                            // linearlayout (vertical)
//                            LinearLayout vertText = new LinearLayout(SummaryPage.this);
//                            vertText.setLayoutParams(new LinearLayout.LayoutParams(227, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));                            vertText.setOrientation(LinearLayout.VERTICAL);
//                            row.addView(vertText);
//
//                            // text views on left of row
//                            TextView gymName = new TextView(SummaryPage.this);
//                            TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 32);
//                            gymName.setGravity(Gravity.CENTER_VERTICAL);
//                            gymName.setPadding(10, 0, 0, 0);
//                            gymName.setTextColor(getResources().getColor(android.R.color.black));
//                            gymName.setTextSize(17);
//                            gymName.setLayoutParams(params1);
//
//                            TextView dateAndTime = new TextView(SummaryPage.this);
//                            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 31);
//                            dateAndTime.setGravity(Gravity.CENTER_VERTICAL);
//                            dateAndTime.setPadding(10, 0, 0, 0);
//                            dateAndTime.setTextColor(getResources().getColor(android.R.color.black));
//                            dateAndTime.setTextSize(15);
//                            dateAndTime.setLayoutParams(params2);
//
//                            vertText.addView(gymName);
//                            vertText.addView(dateAndTime);
//
//                            // button on right of row
//                            Button cancelBtn = new Button(SummaryPage.this);
//                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(151,
//                                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
//                            cancelBtn.setLayoutParams(params);
//                            cancelBtn.setScaleX(0.6f);
//                            cancelBtn.setScaleY(0.6f);
//
//                            row.addView(cancelBtn);


//                            db.collection("timeslots").whereEqualTo("slot", group.get(i))
//                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d(TAG, "success " + task.getResult().size());
//
//                                        for (QueryDocumentSnapshot docu : task.getResult()) {
//                                            Log.d(TAG, docu.getId() + " => " + docu.getData());
//                                            Map<String, Object> map = docu.getData();
//                                            String gym = (String) map.get("gymName");
//                                            String day = (String) map.get("day");
//                                            String time = (String) map.get("time");
//                                            gymName.setText(gym);
//                                            dateAndTime.setText(day + " " + time);
//                                        }
//                                    } else {
//                                        Log.d(TAG, "Error getting documents: ", task.getException());
//                                    }
//                                }
//                            });
//                        }







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