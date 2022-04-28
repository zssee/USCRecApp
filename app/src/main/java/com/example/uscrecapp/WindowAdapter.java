package com.example.uscrecapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class WindowAdapter extends ArrayAdapter<String> {
    private ArrayList<String> bookings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "WindowAdapter";
    private Context context;

    public WindowAdapter(@NonNull Context context, int resource, ArrayList<String> bookings) {
        super(context, resource, bookings);
        this.bookings = bookings;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(TAG, String.valueOf(position));
        int phraseIndex = position;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.window_view, parent, false);
        }

        TextView gymName = convertView.findViewById(R.id.gymName);
        TextView dateAndTime = convertView.findViewById(R.id.dateAndTime);

//        final String[] currGym = new String[1];
        if (bookings.size() == 0){
            gymName.setText("No bookings to show");
            dateAndTime.setText("");
            return convertView;
        }
        // set gym name and day/time
        db.collection("timeslots").whereEqualTo("slot", bookings.get(position))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Integer size = task.getResult().size();
                    Log.d(TAG, "success " + size);
                    for (QueryDocumentSnapshot docu : task.getResult()) {
                        Log.d(TAG, docu.getId() + " => " + docu.getData());
                        Map<String, Object> map = docu.getData();
                        String gym = (String) map.get("gymName");
                        String day = (String) map.get("day");
                        String time = (String) map.get("time");
                        gymName.setText(gym);
                        dateAndTime.setText(day + " " + time);
//                        currGym[0] = gym;
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


        return convertView;
    }
}