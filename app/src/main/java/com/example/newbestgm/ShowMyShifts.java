package com.example.newbestgm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowMyShifts extends AppCompatActivity {
    private TextView showText;
    private String dateStr ,currentUID, shiftTime, userName, shiftDate;
    private DatabaseReference userRef, reff;
    private FirebaseAuth mAuth;
    private final String DATE_STR = "dateStr", USERS = "Users", SHIFT = "Shift", FULL_NAME = "fullname", USER = "user",
    DATE = "date", TIME = "time", SHIFT_DATE_MSG = "My Shift at date: ", IS_IN = "is in the: ", NO_SHIFT = "No shift for this date";

    @Override
    public void onCreate(Bundle savedInstanced) {
        super.onCreate(savedInstanced);
        setContentView(R.layout.activity_calender);

        showText = (TextView)findViewById(R.id.showShifts);
        dateStr = getIntent().getStringExtra(DATE_STR);
        userRef = FirebaseDatabase.getInstance().getReference().child(USERS);
        reff = FirebaseDatabase.getInstance().getReference().child(SHIFT);
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        userRef.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userName = dataSnapshot.child(FULL_NAME).getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child(USER).getValue().toString().equals(userName)) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            shiftDate = dataSnapshot2.child(DATE).getValue().toString();
                            shiftTime = dataSnapshot2.child(TIME).getValue().toString();
                            if(shiftDate.equals(dateStr)){
                                showText.setText(SHIFT_DATE_MSG + shiftDate + IS_IN + shiftTime);
                            }else{
                                showText.setText(NO_SHIFT + shiftDate);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShowMyShifts.this, MainActivity.class);
        startActivity(intent);
    }

}