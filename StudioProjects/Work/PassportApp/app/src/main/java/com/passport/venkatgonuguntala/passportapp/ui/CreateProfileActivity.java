package com.passport.venkatgonuguntala.passportapp.ui;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.passport.venkatgonuguntala.passportapp.R;
import com.passport.venkatgonuguntala.passportapp.util.AlertDialogForBlankFields;
import com.passport.venkatgonuguntala.passportapp.util.Constant;
import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;

import java.io.IOException;


public class CreateProfileActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 234;
    private int STORAGE_PERMISSION_CODE = 1;

    private static final String TAG = CreateProfileActivity.class.getSimpleName();
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextHobbies;
    private Button submitButton;
    private Spinner genderSpinner;
    private ImageView profileImage;
    private Button chooseImageButton;

    private Uri filePath;
    private String name;
    private String age;
    private String hobby;
    private String gender;
    private String image;
    private String id;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.passport.venkatgonuguntala.passportapp.R.layout.activity_create_profile);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);

        chooseImageButton =  findViewById(R.id.chooseImage);
        editTextName =  findViewById(R.id.editTextName);
        editTextAge =  findViewById(R.id.editTextAge);
        editTextHobbies =  findViewById(R.id.editTextHobbies);
        submitButton =  findViewById(R.id.addProfile);
        profileImage =  findViewById(R.id.imageViewProfile);
        genderSpinner =  findViewById(R.id.spinner);
        genderSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserDetails();
            }
        });
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(CreateProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    showFileChooser();
                } else {
                    requestStoragePermission();
                }
            }
        });

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is required to choose an image from photos")
                    .setPositiveButton("OK", null).create();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gender = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    public void uploadUserDetails() {
        name = editTextName.getText().toString().trim();
        age = editTextAge.getText().toString();
        hobby = editTextHobbies.getText().toString();

        if (filePath != null && !name.isEmpty() && !age.isEmpty() && !hobby.isEmpty()) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference storageReference = this.storageReference.child(Constant.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            image = taskSnapshot.getDownloadUrl(). toString();
                            addProfile();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });

        } else {
            AlertDialogForBlankFields alertDialogFragment = new AlertDialogForBlankFields();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.TITLE, Constant.BLANK_FIELDS_TITLE);
            bundle.putString(Constant.MESSAGE, Constant.BLANK_FIELDS_MESSAGE);
            alertDialogFragment.setArguments(bundle);
            alertDialogFragment.show(getFragmentManager(), "blank_fields");
        }
    }

    private void addProfile() {
        id = databaseReference.push().getKey();
        PersonProfile profile = new PersonProfile(id, name, age, hobby, gender, image);
        databaseReference.child(id).setValue(profile);
        Toast.makeText(getApplicationContext(), "profile added", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                showFileChooser();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
