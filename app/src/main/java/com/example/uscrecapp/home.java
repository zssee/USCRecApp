package com.example.uscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton village = (ImageButton) findViewById(R.id.villageBtn);
        ImageButton lyon = (ImageButton) findViewById(R.id.lyonBtn);
        ImageButton uy = (ImageButton) findViewById(R.id.uyBtn);

        village.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gymNav = new Intent(home.this, gymSlots.class);
                gymSlots.selectedGym = "village";
                startActivity(gymNav);
            }
        });

        lyon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gymNav = new Intent(home.this, gymSlots.class);
                gymSlots.selectedGym = "lyon";
                startActivity(gymNav);
            }
        });

        uy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gymNav = new Intent(home.this, gymSlots.class);
                gymSlots.selectedGym = "uy";
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
                        startActivity(homeNav);
                        break;
                    case R.id.person:
                        Intent sumNav = new Intent(home.this, SummaryPage.class);
                        startActivity(sumNav);
                        break;
                }
                return false;
            }
        });
    }
}