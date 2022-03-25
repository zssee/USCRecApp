package com.example.uscrecapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.summaryNav);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent sumNav = new Intent(view.getContext(), SummaryPage.class);
                startActivity(sumNav);

            }
        });

        Button button2 = findViewById(R.id.gymNav);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent gymNav = new Intent(view.getContext(), gymSlots.class);
                startActivity(gymNav);

            }
        });




    }
}