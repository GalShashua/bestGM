package com.example.newbestgm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity2 extends AppCompatActivity {
    private EditText userGroupID, userRole;
    private Button finishButton, button_newGroup;
    private ImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private StorageReference userProfileImgRef;
    private ProgressDialog loadingBar;
    private String curUID;
    private Spinner spinnerRole;
    private ArrayAdapter<CharSequence> adapter;
    private final static int galleryPic = 1;
    private final String USERS = "Users", IMAGE = "profile images", PROF_IMAGE = "profileimage", SELECT_IMAGE = "Please select profile image first...",
    PROF_IMAGE_MSG = "Profile Image",UPDATE_IMAGE = "Please wait, while we updating your profile image...", IMAGE_STORE = "Image Stored", ERROR = "Error:",
    IMAGE_ERROR = "Error Occured: Image can not be cropped. Try Again.",ENTER_GROUP_ID = "Please write your group number...", WRITE_ROLE = "Please write your user company role...", SAVE = "Saving information",
    WAIT_ACOUNT = "Please wait, while we are creating new account...", EMAIL = "email", FULL_NAME = "fullname", GROUP_ID = "groupid",ROLE = "role", PHONE = "phone", MAX_SALARY = "maxsalary",
    GROUP_TAG = "group dialog", CREATE_SUCC = "your account created successfully...", JPG = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mAuth = FirebaseAuth.getInstance();
        curUID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(curUID);
        userProfileImgRef = FirebaseStorage.getInstance().getReference().child(IMAGE);
        userGroupID = findViewById(R.id.register_group_id);
        userRole = findViewById(R.id.register_company_role);
        finishButton = findViewById(R.id.register_finish);
        button_newGroup = findViewById(R.id.button_get_id);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        loadingBar = new ProgressDialog(this);
        spinnerRole = (Spinner) findViewById(R.id.spinnerRole);
        adapter = ArrayAdapter.createFromResource(this, R.array.Roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String theRoleChoose= parent.getItemAtPosition(pos).toString();
                userRole.setText(theRoleChoose);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountInformation();
            }
        });
        button_newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        try {
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, galleryPic);
                }
            });
        } catch (Exception ex){

        }
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild(PROF_IMAGE))
                    {
                        String image = dataSnapshot.child(PROF_IMAGE).getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImage);
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity2.this, SELECT_IMAGE, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPic && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                loadingBar.setTitle(PROF_IMAGE_MSG);
                loadingBar.setMessage(UPDATE_IMAGE);
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);
                Uri resultUri = result.getUri();
                final StorageReference filePath = userProfileImgRef.child(curUID + JPG);
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                userRef.child(PROF_IMAGE).setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent selfIntent = new Intent(RegisterActivity2.this, RegisterActivity2.class);
                                            startActivity(selfIntent);
                                            Toast.makeText(RegisterActivity2.this,IMAGE_STORE , Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                        else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(RegisterActivity2.this, ERROR + message, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                            }

                        });

                    }

                });
            }
            else
            {
                Toast.makeText(this, IMAGE_ERROR, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private void saveAccountInformation() {
        String groupID = userGroupID.getText().toString();
        String companyRole = spinnerRole.getSelectedItem().toString();
        if(TextUtils.isEmpty(groupID))
        {
            Toast.makeText(this, ENTER_GROUP_ID, Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(companyRole))
        {
            Toast.makeText(this, WRITE_ROLE, Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle(SAVE);
            loadingBar.setMessage(WAIT_ACOUNT);
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            HashMap userMap = new HashMap();
            userMap.put(EMAIL, RegisterActivity.email);
            userMap.put(FULL_NAME, RegisterActivity.fullName);
            userMap.put(GROUP_ID, groupID);
            userMap.put(ROLE,companyRole);
            userMap.put(PHONE,"");
            userMap.put(MAX_SALARY,"");
            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        sendUserToMainActivity();
                        Toast.makeText(RegisterActivity2.this, CREATE_SUCC, Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterActivity2.this, ERROR + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void openDialog() {
        GroupNumDialog groupNumDialog = new GroupNumDialog();
        groupNumDialog.show(getSupportFragmentManager(), GROUP_TAG);
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity2.this, MainActivity.class);
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
        sendUserToRegisterActivity();
    }

    private void sendUserToRegisterActivity() {
        Intent intent = new Intent(RegisterActivity2.this, RegisterActivity.class);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity2.this, LoginActivity.class);
        //loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        //finish();
    }

}
