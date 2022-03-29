package com.example.uscrecapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.CameraUpdate;

// Implement OnMapReadyCallback.
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    LatLng center = new LatLng(34.022350,-118.285118);
    LatLng lyons = new LatLng(34.022970,-118.284580);
    LatLng swim = new LatLng(34.022970,-118.284580);
    LatLng village = new LatLng(34.024550,-118.285630);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout file as the content view.
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.my_container, mapFragment)
                .commit();
    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        Log.d("idk", "executed");
//        googleMap.addMarker(new MarkerOptions()
//                .position(lyons)
//                .title("lyons"));
    }
}