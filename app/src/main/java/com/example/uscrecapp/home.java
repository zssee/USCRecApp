package com.example.uscrecapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class home extends AppCompatActivity {
    private static final String TAG = "MapPage";
    public static String docName;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        message = intent.getStringExtra("msg");

        // passed a user name
        // find documentPath
        if(message != null){
            docName = toCamelCase(message);
            Log.d(TAG, "docName: " + docName);
        }
        else{
            Log.d(null, "message was null in home");
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(docName);

        ListView listView = (ListView) findViewById(R.id.upcomingList);
        listView.bringToFront();

        displayBookings();
        TextView title = (TextView) findViewById(R.id.upcomingTitle);
        title.bringToFront();
        ImageButton village = (ImageButton) findViewById(R.id.villageBtn);
        ImageButton lyon = (ImageButton) findViewById(R.id.lyonBtn);
        ImageButton uy = (ImageButton) findViewById(R.id.uyBtn);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("clicked",  "the small window");
                Intent sumNav = new Intent(view.getContext(),  SummaryPage.class);
                sumNav.putExtra("msg", message);
                SummaryPage.docName = message;
                startActivity(sumNav);
            }
        });
        village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gymNav = new Intent(home.this, gymSlots.class);
                gymSlots.selectedGym = "village";
                gymNav.putExtra("msg", message);
                startActivity(gymNav);
            }
        });

        lyon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gymNav = new Intent(home.this, gymSlots.class);
                gymSlots.selectedGym = "lyon";
                gymNav.putExtra("msg", message);
                startActivity(gymNav);
            }
        });

        uy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gymNav = new Intent(home.this, gymSlots.class);
                gymSlots.selectedGym = "uy";
                gymNav.putExtra("msg", message);
                startActivity(gymNav);
            }
        });



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent homeNav = new Intent(home.this, home.class);
                        homeNav.putExtra("msg", message);
                        startActivity(homeNav);
                        break;
                    case R.id.person:
                        Intent sumNav = new Intent(home.this, SummaryPage.class);
                        sumNav.putExtra("msg", message);
                        SummaryPage.docName = message;
                        startActivity(sumNav);
                        break;
                }
                return false;
            }
        });
    } //oncreate
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
                    WindowAdapter adapter = new WindowAdapter(getApplicationContext(), R.layout.window_view, updatedRes);
                    ListView listView = (ListView) findViewById(R.id.upcomingList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Current data: null");
                    Log.d(docName, "Name");
                }
            }
        });
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
