package com.example.uscrecapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.navigation.ui.AppBarConfiguration;

public class map extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String id = intent.getStringExtra("msg");
        username = id;

        setContentView(R.layout.activity_map);
        ImageButton l = (ImageButton) findViewById(R.id.lyon);
        l.setX(250);
        l.setY(780);
        l.bringToFront();
        ImageButton s = (ImageButton) findViewById(R.id.swim);
        s.setX(280);
        s.setY(910);
        s.bringToFront();
        ImageButton v = (ImageButton) findViewById(R.id.village);
        v.setX(870);
        v.setY(750);
        v.bringToFront();

    }
    public void goGym(View v){
        Intent intent = new Intent(this, gymSlots.class);
        intent.putExtra("name", username);
        switch(v.getId())  //get the id of the view clicked. (in this case button)
        {
            case R.id.lyon : // if its button1
                //do something
                intent.putExtra( "gymFrom", "Lyon" );
                break;
            case R.id.swim :
                intent.putExtra( "gymFrom", "Uytengsu" );
                break;
            case R.id.village :
                intent.putExtra( "gymFrom", "Village" );
                break;
        }
        startActivity(intent);
    }

}