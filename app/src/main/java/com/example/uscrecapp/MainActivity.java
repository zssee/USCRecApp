package com.example.uscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{

    public static boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (!isLoggedIn){
        // temporarily changing this to home

        Intent toLogIn = new Intent(this, home.class);
        startActivity(toLogIn);
//        }
//        else{
//            Intent intent = new Intent(this, SummaryPage.class);
//            startActivity(intent);
//        }


//        Button button = findViewById(R.id.summaryNav);
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                Intent sumNav = new Intent(view.getContext(), SummaryPage.class);
//                startActivity(sumNav);
//
//            }
//        });
//
//        Button button2 = findViewById(R.id.gymNav);
//        button2.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                Intent gymNav = new Intent(view.getContext(), gymSlots.class);
//                startActivity(gymNav);
//
//            }
//        });

    }


}