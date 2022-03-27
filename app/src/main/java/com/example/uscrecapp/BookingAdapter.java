package com.example.uscrecapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookingAdapter extends ArrayAdapter<String> {
    private ArrayList<String> bookings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "BookingAdapter";

    public BookingAdapter(@NonNull Context context, int resource, ArrayList<String> bookings) {
        super(context, resource, bookings);
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(TAG, String.valueOf(position));
        int phraseIndex = position;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.upcoming_list_view, parent, false);
        }

        TextView gymName = convertView.findViewById(R.id.gymName);
        TextView dateAndTime = convertView.findViewById(R.id.dateAndTime);
        Button cancelBtn = convertView.findViewById(R.id.cancelButton);

        // set gym name and day/time
        db.collection("timeslots").whereEqualTo("slot", bookings.get(position))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "success " + task.getResult().size());

                    for (QueryDocumentSnapshot docu : task.getResult()) {
                        Log.d(TAG, docu.getId() + " => " + docu.getData());
                        Map<String, Object> map = docu.getData();
                        String gym = (String) map.get("gymName");
                        String day = (String) map.get("day");
                        String time = (String) map.get("time");
                        gymName.setText(gym);
                        dateAndTime.setText(day + " " + time);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users").document("syuenSee")
                        .update("reservations", FieldValue.arrayRemove(bookings.get(position)));

                String msg = "display booking";
                Intent i = new Intent(view.getContext(), SummaryPage.class);
                i.putExtra("msg", msg);
                view.getContext().startActivity(i);

            }
        });

        return convertView;
    }
}