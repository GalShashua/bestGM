package com.example.newbestgm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Random;

public class BlockadeNavigation extends AppCompatActivity {
    private ImageView im1,im2,im3,im4,im5,im6,im7,im8,im9,im10,im11,im12,im13,im14,im15,im16,im17,im18,im19,im20,im21;
    private Button finish;
    private DatabaseReference blockRef,userRef;
    private LinearLayout mainView,x1;
    private FirebaseAuth mAuth;
    private Random r;
    private int[] counters= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private int x, max = 9999, min = 1000;
    private StringBuilder forBlocks;
    private char charB = 'b',charO = 'o';
    private String forBlocksStr, myGroup, currentUID;
    private final String BLOCKS = "Blocks", USERS = "Users", MESSAGE = "message", MANAGER = "Manager", GROUP_ID = "groupid",
    GROUP = "group", FULL_NAME = "fullname", BLOCK_ARR = "blockarray", UID = "uid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        blockRef = FirebaseDatabase.getInstance().getReference().child(BLOCKS);
        userRef = FirebaseDatabase.getInstance().getReference().child(USERS);
        currentUID = mAuth.getCurrentUser().getUid();
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString(MESSAGE);

        //-------------------------manager ----------------------------------
        if(message.equals(MANAGER)) {
            setContentView(R.layout.activity_blockade_navigation_manager);
            mainView = findViewById(R.id.main_layout_for_tables);
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

            blockRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        if(dataSnapshot1.child(GROUP).getValue().toString().equals(myGroup)) {
                            String p= dataSnapshot1.child(BLOCK_ARR).getValue().toString();
                            TextView space = new TextView(BlockadeNavigation.this);
                            space.setText("              ");
                            mainView.addView(space);
                            TextView name = new TextView(BlockadeNavigation.this);
                            name.setText("    "+dataSnapshot1.child(FULL_NAME).getValue().toString());
                            name.setTextSize(20);
                            name.setTextColor(Color.BLACK);
                            mainView.addView(name);
                            LayoutInflater inflater;
                            inflater = (LayoutInflater) BlockadeNavigation.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.table_block , null);
                            layout.setGravity(Gravity.CENTER);
                            TextView t1 = (TextView) layout.findViewById(R.id.t1);
                            TextView t2 = (TextView) layout.findViewById(R.id.t2);
                            TextView t3= (TextView) layout.findViewById(R.id.t3);
                            TextView t4 = (TextView) layout.findViewById(R.id.t4);
                            TextView t5 = (TextView) layout.findViewById(R.id.t5);
                            TextView t6 = (TextView) layout.findViewById(R.id.t6);
                            TextView t7 = (TextView) layout.findViewById(R.id.t7);
                            TextView t8 = (TextView) layout.findViewById(R.id.t8);
                            TextView t9 = (TextView) layout.findViewById(R.id.t9);
                            TextView t10 = (TextView) layout.findViewById(R.id.t10);
                            TextView t11 = (TextView) layout.findViewById(R.id.t11);
                            TextView t12 = (TextView) layout.findViewById(R.id.t12);
                            TextView t13 = (TextView) layout.findViewById(R.id.t13);
                            TextView t14 = (TextView) layout.findViewById(R.id.t14);
                            TextView t15 = (TextView) layout.findViewById(R.id.t15);
                            TextView t16 = (TextView) layout.findViewById(R.id.t16);
                            TextView t17 = (TextView) layout.findViewById(R.id.t17);
                            TextView t18 = (TextView) layout.findViewById(R.id.t18);
                            TextView t19 = (TextView) layout.findViewById(R.id.t19);
                            TextView t20 = (TextView) layout.findViewById(R.id.t20);
                            TextView t21 = (TextView) layout.findViewById(R.id.t21);
                            if(p.charAt(0)== charB)
                                t1.setBackgroundColor(Color.RED);
                            if(p.charAt(1)== charB)
                                t2.setBackgroundColor(Color.RED);
                            if(p.charAt(2)== charB)
                                t3.setBackgroundColor(Color.RED);
                            if(p.charAt(3)== charB)
                                t4.setBackgroundColor(Color.RED);
                            if(p.charAt(4)== charB)
                                t5.setBackgroundColor(Color.RED);
                            if(p.charAt(5)== charB)
                                t6.setBackgroundColor(Color.RED);
                            if(p.charAt(6)== charB)
                                t7.setBackgroundColor(Color.RED);
                            if(p.charAt(7)== charB)
                                t8.setBackgroundColor(Color.RED);
                            if(p.charAt(8)== charB)
                                t9.setBackgroundColor(Color.RED);
                            if(p.charAt(9)== charB)
                                t10.setBackgroundColor(Color.RED);
                            if(p.charAt(10)== charB)
                                t11.setBackgroundColor(Color.RED);
                            if(p.charAt(11)== charB)
                                t12.setBackgroundColor(Color.RED);
                            if(p.charAt(12)== charB)
                                t13.setBackgroundColor(Color.RED);
                            if(p.charAt(13)== charB)
                                t14.setBackgroundColor(Color.RED);
                            if(p.charAt(14)== charB)
                                t15.setBackgroundColor(Color.RED);
                            if(p.charAt(15)== charB)
                                t16.setBackgroundColor(Color.RED);
                            if(p.charAt(16)== charB)
                                t17.setBackgroundColor(Color.RED);
                            if(p.charAt(17)== charB)
                                t18.setBackgroundColor(Color.RED);
                            if(p.charAt(18)== charB)
                                t19.setBackgroundColor(Color.RED);
                            if(p.charAt(19)== charB)
                                t20.setBackgroundColor(Color.RED);
                            if(p.charAt(20)== charB)
                                t21.setBackgroundColor(Color.RED);
                            mainView.addView(layout);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            setContentView(R.layout.activity_blockade_navigation);
            forBlocks = new StringBuilder("ooooooooooooooooooooo");
            r = new Random();
            x = r.nextInt(max - min) + max;
            finish = findViewById(R.id.send_block);
            im1 = findViewById(R.id.im1);
            im2 = findViewById(R.id.im2);
            im3 = findViewById(R.id.im3);
            im4 = findViewById(R.id.im4);
            im5 = findViewById(R.id.im5);
            im6 = findViewById(R.id.im6);
            im7 = findViewById(R.id.im7);
            im8 = findViewById(R.id.im8);
            im9 = findViewById(R.id.im9);
            im10 = findViewById(R.id.im10);
            im11 = findViewById(R.id.im11);
            im12 = findViewById(R.id.im12);
            im13 = findViewById(R.id.im13);
            im14 = findViewById(R.id.im14);
            im15 = findViewById(R.id.im15);
            im16 = findViewById(R.id.im16);
            im17 = findViewById(R.id.im17);
            im18 = findViewById(R.id.im18);
            im19 = findViewById(R.id.im19);
            im20 = findViewById(R.id.im20);
            im21 = findViewById(R.id.im21);
            im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(0,im1);
                }
            });
            im2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(1,im2);
                }
            });
            im3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(2,im3);
                }
            });
            im4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(3,im4);
                }
            });
            im5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(4,im5);
                }
            });
            im6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(5,im6);
                }
            });
            im7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(6,im7);
                }
            });
            im8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(7,im8);
                }
            });
            im9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(8,im9);
                }
            });
            im10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(9,im10);
                }
            });
            im11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(10,im11);
                }
            });
            im12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(11,im12);
                }
            });
            im13.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(12,im13);
                }
            });
            im14.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(13,im14);
                }
            });
            im15.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(14,im15);
                }
            });
            im16.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(15,im16);
                }
            });
            im17.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(16,im17);
                }
            });
            im18.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(17,im18);
                }
            });
            im19.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(18,im19);
                }
            });
            im20.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(19,im20);
                }
            });
            im21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickEvent(20,im21);
                }
            });
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forBlocksStr = forBlocks.toString();
                    userRef.child(currentUID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String userFullName = dataSnapshot.child(FULL_NAME).getValue().toString();
                                String group = dataSnapshot.child(GROUP_ID).getValue().toString();
                                HashMap blocakMap = new HashMap();
                                blocakMap.put(UID, currentUID);
                                blocakMap.put(FULL_NAME, userFullName);
                                blocakMap.put(GROUP, group);
                                blocakMap.put(BLOCK_ARR, forBlocksStr);
                                blockRef.child(currentUID + x).updateChildren(blocakMap)
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
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }

    private void onClickEvent (int x, ImageView view) {
        if (counters[x] % 2 ==0) {
            view.setColorFilter(Color.parseColor("#DA1D27"));
            forBlocks.setCharAt(x, charB);
            counters[x]++;
        } else {
            view.setColorFilter(Color.parseColor("#939393"));
            forBlocks.setCharAt(x, charO);
            counters[x]++;
        }
    }
    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(BlockadeNavigation.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
