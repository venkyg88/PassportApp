package com.passport.venkatgonuguntala.passportapp;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.passport.venkatgonuguntala.passportapp.Util.Constant;
import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;

import java.io.IOException;


public class CreateProfileActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    private static final String TAG = CreateProfileActivity.class.getSimpleName();
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextHobbies;
    private Button submitButton;
    private Spinner genderSpinner;
    private String gender;
    private ImageView profileImage;
    private Button chooseImageButton;

    //URI to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);

        chooseImageButton = (Button) findViewById(R.id.chooseImage);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextHobbies = (EditText) findViewById(R.id.editTextHobbies);
        submitButton = (Button) findViewById(R.id.addProfile);
        profileImage = (ImageView) findViewById(R.id.imageViewProfile);
        genderSpinner = (Spinner) findViewById(R.id.spinner);
        genderSpinner.setOnItemSelectedListener(this);

        submitButton.setOnClickListener(this);
        chooseImageButton.setOnClickListener(this);
        //TODO: move to a method - set adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

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



    private void addProfile(){
        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString();
        String hobby = editTextHobbies.getText().toString();
        String image = "temp";//profileImage.toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(hobby)){
            String id = databaseReference.push().getKey();
            PersonProfile profile = new PersonProfile(id, name, age, hobby, gender, image);
            databaseReference.child(id).setValue(profile);
            Toast.makeText(this, "profile added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "You should enter a name, age & hobie", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gender = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        if (view == chooseImageButton) {
            showFileChooser();
        } else if (view == submitButton) {
            uploadFile();
            //addProfile();
            finish();
        }
    }

    public void uploadFile() {
        //checking if file is available
        if(filePath != null) {
        //displaying progress dialog while image is uploading
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        //getting the storage reference
        StorageReference sRef = storageReference.child(Constant.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

        //adding the file to reference
        sRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();

                        //displaying success toast
                        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                        String name = editTextName.getText().toString().trim();
                        String age = editTextAge.getText().toString();
                        String hobby = editTextHobbies.getText().toString();
                        String image = taskSnapshot.getDownloadUrl().toString();
                        //creating the upload object to store uploaded image details
                        /*if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(hobby)){*/
                            String id = databaseReference.push().getKey();
                            PersonProfile profile = new PersonProfile(id, name, age, hobby, gender, image);
                            databaseReference.child(id).setValue(profile);
                            Toast.makeText(getApplicationContext(), "profile added", Toast.LENGTH_LONG).show();
                        /*} else {
                            //TODO: to be handled later
                            Toast.makeText(getApplicationContext(), "You should enter a name, age & hobie", Toast.LENGTH_LONG).show();
                        }*/
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
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
        } else {
            //TODO: display an error if no file is selected
        }

    }
}
