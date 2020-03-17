package com.example.newbestgm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressBar progressBar;
    private ImageView navProfileImage, mainProfileImage;
    private TextView textView, textView2, navProfileUserName, navProfileRole, myNextShift;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,shiftRef;
    private String role, currentUID, myUserName, userName;
    private ImageView btn_salary, btn_block;
    private int sumSalary = 0, randNum, max = 9999, min = 1000;
    private Random rand;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private final String TAG_MASSAGE = "message", TITLE_HOME = "Home", MAX_SALARY = "maxsalary", FULL_NAME = "fullname", ROLE = "role",
            PROFILE_IMAGE = "profileimage", SHIFT = "Shift", USER = "user", MONEY = "money", USERS = "Users", DATE = "date",HYPHEN =  "-" , BACKSLASH = "/",
            PROFILE_MESSAGE = "Profile name do not exist...", TEXT_GOAL = "/ enter goal", NEXT_SHIFT = " My next shift: ",NO_FUTURE_SHIFT = "No future shifts",
            EXAMPLE_DIALOG = "example dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_block = findViewById(R.id.button_main_blockade);
        btn_salary =findViewById(R.id.button_main_salary);
        userRef = FirebaseDatabase.getInstance().getReference().child(USERS);

        btn_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, BlockadeNavigation.class);
                intent1.putExtra(TAG_MASSAGE, role);
                startActivity(intent1);
            }
        });
        btn_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, AddMoneyToShift.class);
                startActivity(intent2);
            }
        });
        rand = new Random();
        randNum = rand.nextInt(max - min) + max;
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        showMyNextShift();
        mainProfileImage = findViewById(R.id.image_for_main_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(TITLE_HOME);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        progressBar = findViewById(R.id.progress);
        textView = findViewById(R.id.how_much_money);
        textView2 = findViewById(R.id.max_salary_text);
        goalSalary();
        calculateSum();
        updateShift();

    }

    public void updateShift() {
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalaryNavigation.class);
                startActivity(intent);
            }
        });
    }

    public void goalSalary() {
        View navView = navigationView.inflateHeaderView(R.layout.header);
        navProfileImage = navView.findViewById(R.id.header_profile_img);
        navProfileUserName = navView.findViewById(R.id.nav_user_full_name);
        navProfileRole = navView.findViewById(R.id.nav_user_role);
        userRef.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(MAX_SALARY)) {
                    if (dataSnapshot.child(MAX_SALARY).getValue().toString().equals("")) {
                        textView2.setText(TEXT_GOAL);
                        progressBar.setMax(1000);
                    } else {
                        String maxsalary = dataSnapshot.child(MAX_SALARY).getValue().toString();
                        int int_max = Integer.parseInt(maxsalary);
                        textView2.setText(BACKSLASH + maxsalary);
                        progressBar.setMax(int_max);
                    }
                }
                if (dataSnapshot.hasChild(FULL_NAME)) {
                    myUserName = dataSnapshot.child(FULL_NAME).getValue().toString();
                    navProfileUserName.setText(myUserName);
                }
                if (dataSnapshot.hasChild(ROLE)) {
                    role = dataSnapshot.child(ROLE).getValue().toString();
                    navProfileRole.setText(role);
                }
                if (dataSnapshot.hasChild(PROFILE_IMAGE)) {
                    String image = dataSnapshot.child(PROFILE_IMAGE).getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(navProfileImage);
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(mainProfileImage);
                } else {
                    Toast.makeText(MainActivity.this,PROFILE_MESSAGE , Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void calculateSum() {
        userRef = FirebaseDatabase.getInstance().getReference().child(USERS);
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        userRef.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userName = dataSnapshot.child(FULL_NAME).getValue().toString();
                    shiftRef = FirebaseDatabase.getInstance().getReference().child(SHIFT);
                    shiftRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if(dataSnapshot1.child(USER).getValue().toString().equals(myUserName)){
                                    double doubleValue = Double.parseDouble(dataSnapshot1.child(MONEY).getValue().toString().trim());
                                    int intValue = (int) doubleValue;
                                    sumSalary = sumSalary + intValue;
                                }
                            }
                            progressBar.setProgress(sumSalary);
                            textView.setText(sumSalary + " ");
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void createNewShift() {
        ShiftDialog shiftDialog = new ShiftDialog();
        shiftDialog.show(getSupportFragmentManager(),EXAMPLE_DIALOG);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            sendUserToLoginActivity();
        }
        else
        {
            checkUserExistence();
        }
    }

    public void checkUserExistence(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(currentUID)) {
//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void sendUserToRegisterActivity2() {
        Intent setupIntent = new Intent(MainActivity.this, RegisterActivity2.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_salary:
                Intent intent = new Intent(MainActivity.this, SalaryNavigation.class);
                startActivity(intent);
                break;
            case R.id.nav_blockade:
                Intent intent1 = new Intent(MainActivity.this, BlockadeNavigation.class);
                intent1.putExtra(TAG_MASSAGE, role);
                startActivity(intent1);
                break;
            case R.id.nav_friends:
                Intent intent2 = new Intent(MainActivity.this, FriendsNavigation.class);
                startActivity(intent2);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(MainActivity.this, ProfileNavigation.class);
                startActivity(intent3);
                break;
            case R.id.nav_shift_board:
                Intent intent4 = new Intent(MainActivity.this, CalenderActivity.class);
                startActivity(intent4);
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                sendUserToLoginActivity();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToMainActivity() {
    }


    public void showMyNextShift() {
        userRef = FirebaseDatabase.getInstance().getReference().child(USERS);
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
            userRef.child(currentUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            myUserName = dataSnapshot.child(FULL_NAME).getValue().toString();
                            checkUserName(myUserName);
                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

    public void checkUserName(String name){
        myNextShift = (TextView)findViewById(R.id.myNextShift);
        shiftRef = FirebaseDatabase.getInstance().getReference().child(SHIFT);
        shiftRef.addValueEventListener(new ValueEventListener() {
            int index = 0;
            ArrayList<String> datesArray = new ArrayList<String>();
            String dateDataSnapshot = "";
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String userDataSnapshot = dataSnapshot1.child(USER).getValue(String.class);
                    if (userDataSnapshot.equals(myUserName)) {
                        dateDataSnapshot = dataSnapshot1.child(DATE).getValue(String.class);
                        datesArray.add(dateDataSnapshot);
                        index++;
                    }
                }
                checkUserDate(datesArray);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void checkUserDate(ArrayList<String> datesArray){
        LocalDate today = LocalDate.now();
        String replaceCharToday;
        replaceCharToday = today.toString().replace(HYPHEN, BACKSLASH);
        String[] replaceCharTodaySplit = replaceCharToday.split(BACKSLASH);
        String todayYear = replaceCharTodaySplit[0];
        String todayMonth = replaceCharTodaySplit[1];
        String todayDay = replaceCharTodaySplit[2];
        Collections.sort(datesArray);
        String theYear, theMonth, theDay;
        String[] splitTheDateArray;
        for(int j=0; j<datesArray.size(); j++){
            splitTheDateArray = datesArray.get(j).split(BACKSLASH);
            theYear = splitTheDateArray[2];
            theMonth = splitTheDateArray[1];
            theDay = splitTheDateArray[0];
            if(theYear.compareTo(todayYear)>=0 && theMonth.compareTo(todayMonth)>=0 && theDay.compareTo(todayDay)>=0){
                myNextShift.setText(NEXT_SHIFT + datesArray.get(j));
                return;
            }else{
                myNextShift.setText(NO_FUTURE_SHIFT);
            }
        }
    }




}
