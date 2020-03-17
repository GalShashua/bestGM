package com.example.newbestgm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class CalenderActivity extends AppCompatActivity {
    private String myRole, currentUID, userName;
    private DatabaseReference userRef, reff;
    private FirebaseAuth mAuth;
    ArrayList<String> shifts;
    private TextView showText, explain_text;
    private final String USERS = "Users", ROLE = "role", MANAGER = "Manager" , SELECT_DATE_MSG = "Please select a date to see if you work",
    UPDATE_DATE = "Please select date for update\n      a new shift for worker", DATE_STR = "dateStr", BACKSLASH = "/", FULL_NAME = "fullname",
    DATE = "date", USER = "user", TIME = "time";

    @Override
    public void onCreate(Bundle savedInstanced) {
        super.onCreate(savedInstanced);
        setContentView(R.layout.activity_calender);
        explain_text = (TextView) findViewById(R.id.explain);
        userRef = FirebaseDatabase.getInstance().getReference().child(USERS);
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        userRef.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myRole = dataSnapshot.child(ROLE).getValue().toString();
                }
                displayBoardShift(myRole);
            }

            public void displayBoardShift(String role) {
                if (!role.equalsIgnoreCase(MANAGER)) {
                    CalendarView calendarView = findViewById(R.id.calendarView);
                    explain_text.setText(SELECT_DATE_MSG);
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            String dateStr = dayOfMonth + BACKSLASH + (month + 1) + BACKSLASH + year;
                            showShiftForEmployees(dateStr);
                        }
                    });
                } else {
                    CalendarView calendarView = findViewById(R.id.calendarView);
                    explain_text.setText(UPDATE_DATE);
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            String dateStr = dayOfMonth + BACKSLASH + (month + 1) + BACKSLASH + year;
                            Intent intent = new Intent(CalenderActivity.this, AddShifts.class);
                            intent.putExtra(DATE_STR, dateStr);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void showShiftForEmployees(final String dateString) {
        final String dateSt = dateString;
        //read the shifts
        showText = (TextView) findViewById(R.id.showShifts);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        reff = FirebaseDatabase.getInstance().getReference().child("Shift");
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        shifts = new ArrayList<String>();
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
                int i = 0;
                ArrayList<String> datesArray = new ArrayList<String>();
                String dateDataSnapshot = "";
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    String userDataSnapshot = dataSnapshot2.child("user").getValue(String.class);
                    if (userDataSnapshot.equals(userName)) {
                        dateDataSnapshot = dataSnapshot2.child("date").getValue(String.class);
                        datesArray.add(dateDataSnapshot);
                        i++;
                    }
                    if (datesArray.contains(dateSt.trim())) {
                        checkShiftTime(dateSt, userName);
                    } else {
                        showText.setText("No shift at date: " + dateSt);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkShiftTime(final String theDate,final String theUser) {
        String timeToReturn;
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String theTime = "";
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String dateStr = dataSnapshot1.child(DATE).getValue(String.class);
                    String userStr = dataSnapshot1.child(USER).getValue(String.class);
                    if (dateStr.equals(theDate) && userStr.equals(theUser)) {
                        theTime = dataSnapshot1.child(TIME).getValue(String.class);
                        showText.setText("Shift at date: " + theDate + " in " + theTime);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
            Intent mainIntent = new Intent(CalenderActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
        }
    }


