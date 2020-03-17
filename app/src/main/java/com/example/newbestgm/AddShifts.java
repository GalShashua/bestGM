package com.example.newbestgm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class AddShifts extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerTime, spinnerUsers;
    private TextView title;
    private DatabaseReference userRef, reff;
    private FirebaseAuth mAuth;
    private Shift shift;
    private Button saveButton;
    private long max_id = 0;
    private String dateStr,myGroup,currentUID;
    private final String DATE_STR = "dateStr", DATE_MSG = "Selected date: ", USERS = "Users", SHIFT = "Shift",
    SHIFT_MSG = "Shift was inserd succesfully", GROUP_ID = "groupid", FULL_NAME = "fullname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shifts);

        title = (TextView)findViewById(R.id.theDate);
        dateStr = getIntent().getStringExtra(DATE_STR);
        if (dateStr != null) {
            title.setText(DATE_MSG + dateStr);
        }
        reff = FirebaseDatabase.getInstance().getReference().child(USERS);
        spinnerUsers = (Spinner)findViewById(R.id.spinnerWorkers);
        userRef = FirebaseDatabase.getInstance().getReference().child(USERS);
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        selectTimeShift();
        selectUserForShift();
        saveButton = (Button)findViewById(R.id.saveButton);
        shift = new Shift();
        reff = FirebaseDatabase.getInstance().getReference().child(SHIFT);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    max_id = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shift.setMoney(0);
                shift.setDate(dateStr.trim());
                shift.setTime(spinnerTime.getSelectedItem().toString());
                shift.setUser(spinnerUsers.getSelectedItem().toString());
                reff.child(String.valueOf(max_id + 1)).setValue(shift);
                Toast.makeText(AddShifts.this, SHIFT_MSG, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddShifts.this, CalenderActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        String text = parent.getItemAtPosition(pos).toString();
    }

    public void selectTimeShift(){
        spinnerTime = (Spinner)findViewById(R.id.spinnerTime);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Shifts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter);
        spinnerTime.setOnItemSelectedListener(this);
    }

    public void selectUserForShift(){

        userRef.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myGroup = dataSnapshot.child(GROUP_ID).getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> users = new ArrayList<String>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String userDataSnapshot = dataSnapshot1.child(GROUP_ID).getValue(String.class);
                    if (userDataSnapshot.equals(myGroup)) {
                        String name = dataSnapshot1.child(FULL_NAME).getValue().toString();
                        users.add(name);
                    }
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AddShifts.this, android.R.layout.simple_spinner_dropdown_item, users);
                    spinnerUsers.setAdapter(adapter1);
                    spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            String text = parent.getItemAtPosition(pos).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddShifts.this, CalenderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
