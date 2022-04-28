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

public class Map extends AppCompatActivity {
    private static final String TAG = "MapPage";
    public static String docName;
    private static String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
//        message = intent.getStringExtra("name");
        FileIO file = new FileIO();
        String message = file.read(this);
        // passed a user name
        // find documentPath
        if(message != null){
            docName = message;
            Log.d(TAG, "docName: " + docName);
        }
        else{
            Log.e(null, "message was null in home");
        }

        ListView listView = (ListView) findViewById(R.id.upcomingList);
        listView.bringToFront();
        TextView title = (TextView)findViewById(R.id.upcomingTitle);
        title.bringToFront();

        displayBookings();

        ImageButton village = (ImageButton) findViewById(R.id.villageBtn);
        ImageButton lyon = (ImageButton) findViewById(R.id.lyonBtn);
        ImageButton uy = (ImageButton) findViewById(R.id.uyBtn);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("clicked",  "the small window");
                Intent sumNav = new Intent(view.getContext(),  SummaryPage.class);
                sumNav.putExtra("msg", docName);
                startActivity(sumNav);
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clicked",  "the small window");
                Intent sumNav = new Intent(view.getContext(),  SummaryPage.class);
                sumNav.putExtra("msg", docName);
                startActivity(sumNav);
            }
        });
        village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gymNav = new Intent(Map.this, gymSlots.class);
                gymSlots.selectedGym = "village";
                gymNav.putExtra("msg", docName);
                startActivity(gymNav);
            }
        });

        lyon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gymNav = new Intent(Map.this, gymSlots.class);
                gymSlots.selectedGym = "lyon";
                gymNav.putExtra("msg", docName);
                startActivity(gymNav);
            }
        });

        uy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gymNav = new Intent(Map.this, gymSlots.class);
                gymSlots.selectedGym = "uy";
                gymNav.putExtra("msg", docName);
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
                        Intent homeNav = new Intent(Map.this, Map.class);
//                        homeNav.putExtra("msg", message);
                        startActivity(homeNav);
                        break;
                    case R.id.person:
                        Intent sumNav = new Intent(Map.this, SummaryPage.class);
                        sumNav.putExtra("msg", docName);
                        Log.d(TAG, "Sent intent with message: " + docName);
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
                if (snapshot.exists()) {
                    Log.d(TAG, "Current data.txt: " + snapshot.getData());
                    ArrayList<String> updatedRes = (ArrayList<String>) snapshot.getData().get("reservations");
                    WindowAdapter adapter = new WindowAdapter(getApplicationContext(), R.layout.window_view, updatedRes);
                    ListView listView = (ListView) findViewById(R.id.upcomingList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {

                    Log.d(TAG, "Current data.txt: null");
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
