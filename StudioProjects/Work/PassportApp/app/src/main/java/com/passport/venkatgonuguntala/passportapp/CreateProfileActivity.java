package com.passport.venkatgonuguntala.passportapp;


import android.graphics.Bitmap;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;


public class CreateProfileActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private static final String TAG = CreateProfileActivity.class.getSimpleName();
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextHobbies;
    private Button submitButton;
    private DatabaseReference databaseProfiles;
    private Spinner genderSpinner;
    private String gender;
    private ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        databaseProfiles = FirebaseDatabase.getInstance().getReference(getString(R.string.dbnode_profiles));

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextHobbies = (EditText) findViewById(R.id.editTextHobbies);
        submitButton = (Button) findViewById(R.id.addProfile);
        profileImage = (ImageView) findViewById(R.id.imageContainer);
        genderSpinner = (Spinner) findViewById(R.id.spinner);
        genderSpinner.setOnItemSelectedListener(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfile();
                finish();
            }
        });
        //TODO: move to a method - set adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

    }



    private void addProfile(){
        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString();
        String hobby = editTextHobbies.getText().toString();
        String image = "temp";//profileImage.toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(hobby)){
            String id = databaseProfiles.push().getKey();
            PersonProfile profile = new PersonProfile(id, name, age, hobby, gender, image);
            databaseProfiles.child(id).setValue(profile);
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

}
