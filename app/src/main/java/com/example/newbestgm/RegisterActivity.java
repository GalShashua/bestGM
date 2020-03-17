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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText userFullName, userEmail, userPassword, userConfirmPassword;
    private Button continueButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    protected static String fullName, email;
    private final String ENTER_NAME = "Please write your full name...", ENTER_MAIL = "Please write your email...",
    ENTER_PASS = "Please write your password...", CONFIRM_PASS = "Please confirm your password...", NO_MATCH = "your password didn't match",
    NEW_ACOUNT = "Creating new account", WAIT_ACOUNT = "Please wait, while we are creating your new account...", SUCCESS_MSG = "You are authenticated successfully...",
    ERROR_MSG = "Error occured: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth= FirebaseAuth.getInstance();
        userFullName = findViewById(R.id.register_full_name);
        userEmail = findViewById(R.id.register_email);
        userPassword = findViewById(R.id.register_password);
        userConfirmPassword = findViewById(R.id.register_confirm_password);
        continueButton = findViewById(R.id.register_create_account);
        loadingBar = new ProgressDialog(this);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {
        fullName = userFullName.getText().toString();
        email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confirmPassword = userConfirmPassword.getText().toString();
        if(TextUtils.isEmpty(fullName))
        {
            Toast.makeText(this,ENTER_NAME, Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, ENTER_MAIL, Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, ENTER_PASS, Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this, CONFIRM_PASS, Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword))
        {
            Toast.makeText(this, NO_MATCH, Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle(NEW_ACOUNT);
            loadingBar.setMessage(WAIT_ACOUNT);
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                sendUserToRegister2Activity();
                                Toast.makeText(RegisterActivity.this, SUCCESS_MSG, Toast.LENGTH_SHORT);
                                loadingBar.dismiss();
                            }

                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, ERROR_MSG + message, Toast.LENGTH_SHORT);
                                loadingBar.dismiss();
                            }
                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            sendUserToMainActivity();
        }

    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void sendUserToRegister2Activity() {
        Intent mainIntent = new Intent(RegisterActivity.this, RegisterActivity2.class);
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

    @Override
    public void onBackPressed() {
        sendUserToLoginActivity();
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        //finish();
    }

}
