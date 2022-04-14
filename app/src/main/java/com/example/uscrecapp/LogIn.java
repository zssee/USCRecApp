package com.example.uscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.Map;

public class LogIn extends AppCompatActivity {
    private static final String TAG = "LogIn";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        EditText userName = findViewById(R.id.userName);


        Button logInBtn = findViewById(R.id.logIn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString(); //gets you the contents of edit text
                Log.d(TAG, username);

                // initialize db
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                if(username.length() != 0){
                    db.collection("users").whereEqualTo("name", username)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "valid user: " + task.getResult().size());

                                if(task.getResult().size() != 0){
                                    // let user login
                                    String msg = username;
                                    Intent sumNav = new Intent(view.getContext(),  home.class);
                                    sumNav.putExtra("msg", msg);
                                    startActivity(sumNav);
                                }
                                else{
                                    Toast.makeText(LogIn.this, "Cannot Find User",
                                            Toast.LENGTH_LONG).show();
                                }


                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LogIn.this, "Please enter your name",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}