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

                        // display scrolling view of upcoming bookings
                        Log.d(TAG, "success " + group.get(0) + " " + group.get(1) + " " + group.get(2));
                        BookingAdapter adapter = new BookingAdapter(getApplicationContext(), R.layout.upcoming_list_view, group);
                        ListView listView = (ListView) findViewById(R.id.upcomingList);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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