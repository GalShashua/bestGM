package com.example.newbestgm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;

public class ProfileNavigation extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference  storageReference;
    private final String storagePath= "Users_Profile_Imgs/", USERS = "Users", EMAIL = "email", FULL_NAME = "fullname", PHONE = "phone",
    ROLE = "role", IMAGE = "profileimage",EDIT_PROF_PIC =  "Edit profile picture", EDIT_NAME = "Edit name", EDIT_COMPANY = "Edit company role", EDIT_PHONE = "Edit phone number",
     ACTION = "Choose Action", UPDATE_PIC = "Updating Profile Picture",UPTADE_NAME = "Updating Name", UPDATE_COMPANY = "Updating Company Role",CANCEL = "Cancel",
    UPDATE_PHONE = "Updating Phone Number", UPDATE = "Update", ENTER = "Enter ", PLEACE_ENTER = "Pleace enter ", CAMARA = "Camera", GALLERY = "Gallery", PICK_IMAGE = "Pick Image From",
    CAMERA_STORAGE = "Please enable camera & storage permission", ENABLE_STRORAGE = "Please enable storage permission";
    private ImageView profileImg;
    private TextView pro_fullname, pro_email, pro_phone, pro_role;
    private FloatingActionButton fab;
    private ProgressDialog pd;
    private static final int CAMERA_REQUEST_CODE = 100, STORAGE_REQUEST_CODE = 200, IMAGE_PIC_CAMERA_CODE = 300, IMAGE_PICK_GALLERY_CODE = 400;
    private String cameraPermissions[], storagePermissions[];
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_navigation);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);
        storageReference = FirebaseStorage.getInstance().getReference();
        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        profileImg = findViewById(R.id.pro_img);
        pro_fullname = findViewById(R.id.profile_full_name);
        pro_email = findViewById(R.id.profile_email);
        pro_phone = findViewById(R.id.profile_phone);
        pro_role = findViewById(R.id.profile_role);
        fab = findViewById(R.id.fab);
        pd = new ProgressDialog(this);

        Query query = databaseReference.orderByChild(EMAIL).equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = "" + ds.child(FULL_NAME).getValue();
                    String email = "" + ds.child(EMAIL).getValue();
                    String phone = "" + ds.child(PHONE).getValue();
                    String role = "" + ds.child(ROLE).getValue();
                    String image = "" + ds.child(IMAGE).getValue();
                    pro_fullname.setText(name);
                    pro_email.setText(email);
                    pro_phone.setText(phone);
                    pro_role.setText(role);
                    try {
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImg);
                    }
                    catch (Exception e)
                    {
                        Picasso.get().load(R.drawable.ic_person_black_24dp).into(profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);


        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog() {
        String options[] = {EDIT_PROF_PIC, EDIT_NAME, EDIT_COMPANY, EDIT_PHONE};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ACTION);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    pd.setMessage(UPDATE_PIC);
                    ShowImagePicDialog();
                }
                else if (which == 1) {
                    pd.setMessage(UPTADE_NAME);
                    showNamePhoneUpdateDialog(FULL_NAME);
                }
                else if (which == 2) {
                    pd.setMessage(UPDATE_COMPANY);
                    showNamePhoneUpdateDialog(ROLE);
                }
                else if (which == 3) {
                    pd.setMessage(UPDATE_PHONE);
                    showNamePhoneUpdateDialog(PHONE);
                }
            }
        });
        builder.create().show();
    }

    private void showNamePhoneUpdateDialog(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(UPDATE + key);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        final EditText editText = new EditText(this);
        editText.setHint(ENTER + key);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setPositiveButton(UPDATE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(ProfileNavigation.this,UPDATE + "....", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(ProfileNavigation.this,"" +e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(ProfileNavigation.this, PLEACE_ENTER + key, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void ShowImagePicDialog() {
        String options[]={CAMARA, GALLERY};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(PICK_IMAGE);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else
                    {
                        pickFromCamera();
                    }
                }
                else if (which==1) {
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE: {
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, CAMERA_STORAGE, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE : {
                if(grantResults.length >0 ){
                    boolean writeStorageAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted) {
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, ENABLE_STRORAGE, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode ==IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                uploadProfilePhoto(image_uri);
            }
            if( requestCode == IMAGE_PIC_CAMERA_CODE) {
                uploadProfilePhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePhoto(Uri uri) {
        pd.show();
        String filePathAndName = storagePath+"_"+user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();

                if (uriTask.isSuccessful()){
                    HashMap<String, Object> results = new HashMap<>();
                    results.put(IMAGE, downloadUri.toString());
                    databaseReference.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                        }
                    });
                }
                else {
                    pd.dismiss();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                    }
                });
    }

    private void pickFromGallery() {
        Intent gallertIntent = new Intent(Intent.ACTION_PICK);
        gallertIntent.setType("image/*");
        startActivityForResult(gallertIntent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PIC_CAMERA_CODE);
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(ProfileNavigation.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }


}
