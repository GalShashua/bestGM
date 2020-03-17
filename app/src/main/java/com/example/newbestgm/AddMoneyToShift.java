package com.example.newbestgm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AddMoneyToShift extends AppCompatActivity implements MoneyDialog.MoneyDialogListener{
    private String myName, currentUID, pressedDate;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, shiftRefence;
    private ArrayList<String> datesArray;
    private LinearLayout linearLayout;
    private final String USERS = "Users", SHIFT = "Shift", FULL_NAME = "fullname", DATE = "date", USER = "user", MONEY = "money",
    ADD_MONEY_MSG = "Add Money - ", EDIT_MONEY_MSG = "Edit Money - ", EXAMPLE_DIALOG = "example dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_shift);
        shiftRefence = FirebaseDatabase.getInstance().getReference().child(SHIFT);
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        datesArray = new ArrayList<>();
        checkMyName();
    }

    public void checkMyName(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child(USERS);
        databaseReference.child(currentUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myName = dataSnapshot.child(FULL_NAME).getValue().toString();
                }
                checkMyShifts(myName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void checkMyShifts(String userName){
        shiftRefence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int index = 0;
                String dateDataSnapshot = "";
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    String userDataSnapshot = dataSnapshot2.child(USER).getValue(String.class);
                    if (userDataSnapshot.equals(myName)) {
                        dateDataSnapshot = dataSnapshot2.child(DATE).getValue(String.class);
                        datesArray.add(dateDataSnapshot);
                        index++;
                    }
                }
                Collections.sort(datesArray);
                for (int j = 0; j < datesArray.size(); j++) {
                    addButtonToDate(datesArray.get(j));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void addButtonToDate(final String date) {
        final Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(btn);
        shiftRefence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    if (dataSnapshot2.child(USER).getValue().toString().equals(myName) && dataSnapshot2.child(DATE).getValue().toString().equals(date)) {
                        int haveMoney = dataSnapshot2.child(MONEY).getValue(int.class);
                        if(haveMoney==0){
                            btn.setText(ADD_MONEY_MSG + date);
                            btn.setBackgroundColor(Color.parseColor("#FF8080"));
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    createMoneyDialog();
                                    pressedDate = date;
                                    btn.setBackgroundColor(Color.parseColor("#30FF29"));
                                    btn.setText(EDIT_MONEY_MSG + date);
                                }
                            });
                        }else{
                            btn.setText(EDIT_MONEY_MSG+ date);
                            btn.setBackgroundColor(Color.parseColor("#30FF29"));
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    createMoneyDialog();
                                    pressedDate = date;
                                    btn.setBackgroundColor(Color.parseColor("#30FF29"));
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void saveMoneyChange(final int moneyCount){
        shiftRefence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    if (dataSnapshot2.child(USER).getValue().toString().equals(myName)) {
                        String dateStr = dataSnapshot2.child(DATE).getValue(String.class);
                        if (dateStr.equals(pressedDate)) {
                            String key = dataSnapshot2.getKey();
                            HashMap shiftsMap = new HashMap();
                            shiftsMap.put(MONEY, moneyCount);
                            shiftRefence.child(key).updateChildren(shiftsMap)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                sendUserToMainActivity();
                                            } else {
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createMoneyDialog() {
        MoneyDialog moneyDialog = new MoneyDialog();
        moneyDialog.show(getSupportFragmentManager(), EXAMPLE_DIALOG);
    }

    @Override
    public void applyTexts(String money) {
        int moneyCount = Integer.parseInt(money);
        saveMoneyChange(moneyCount);
    }

        private void sendUserToMainActivity() {
            Intent mainIntent = new Intent(AddMoneyToShift.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();

    }

    @Override
    public void onBackPressed() {
        sendUserToMainActivity();
    }
}