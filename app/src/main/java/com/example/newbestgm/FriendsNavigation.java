package com.example.newbestgm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FriendsNavigation extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ArrayList<User> list;
    private MyAdapter adapter;
    private String currentUID;
    private final String USERS = "Users", GROUP_ID = "groupid", FULL_NAME = "fullname", ROLE = "role", PROF_IMAGE = "profileimage";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_navigation);
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list= new ArrayList<User>();

        reference = FirebaseDatabase.getInstance().getReference().child(USERS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    String myGroup = dataSnapshot.child(currentUID).child(GROUP_ID).getValue().toString();
                    if(dataSnapshot1.child(GROUP_ID).getValue().toString().equals(myGroup)) {
                        User u = new User();
                        u.setUser_full_name((String) dataSnapshot1.child(FULL_NAME).getValue());
                        u.setUser_company_role(dataSnapshot1.child(ROLE).getValue().toString());
                        if(dataSnapshot1.child(PROF_IMAGE).exists()){
                            u.setImage(dataSnapshot1.child(PROF_IMAGE).getValue().toString());
                        }
                        list.add(u);
                    }
                }
                adapter= new MyAdapter(FriendsNavigation.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendsNavigation.this, "Opss..", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
