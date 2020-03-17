package com.example.newbestgm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;

public class SalaryNavigation extends AppCompatActivity {
    private EditText max_salary;
    private Button max_salary_btn, addMoneyShift;
    private ProgressDialog loadingBar;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog pd;
    private final String USERS = "Users", MAX_SALARY = "maxsalary", UPDATED = "Updated...", ENTER_MAX = "Please enter max salary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_navigation);
        max_salary = findViewById(R.id.edit_text_max_salary);
        max_salary_btn = findViewById(R.id.apply_max_salary);
        addMoneyShift = (Button)findViewById(R.id.buttonAddMoney);
        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);
        storageReference = FirebaseStorage.getInstance().getReference();
        max_salary_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMaxSalary();
                sendUserToMainActivity();
            }
        });
        addMoneyShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalaryNavigation.this, AddMoneyToShift.class);
                startActivity(intent);
            }
        });
    }

    private void saveMaxSalary() {
        String value = max_salary.getText().toString().trim();
        if (!TextUtils.isEmpty(value)) {
            HashMap<String, Object> result = new HashMap<>();
            result.put(MAX_SALARY, value);
            databaseReference.child(user.getUid()).updateChildren(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SalaryNavigation.this, UPDATED, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(SalaryNavigation.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(SalaryNavigation.this, ENTER_MAX , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        sendUserToMainActivity();
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(SalaryNavigation.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

}
