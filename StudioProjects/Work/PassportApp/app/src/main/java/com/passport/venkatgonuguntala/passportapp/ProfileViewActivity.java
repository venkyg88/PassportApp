package com.passport.venkatgonuguntala.passportapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getIncomingIntents();
    }

    public void getIncomingIntents() {
        if(getIntent().hasExtra(getString(R.string.profile_name)) &&
                getIntent().hasExtra(getString(R.string.profile_age)) &&
                getIntent().hasExtra(getString(R.string.profile_gender))){

            String id = getIntent().getStringExtra(getString(R.string.profile_id));
            String name = getIntent().getStringExtra(getString(R.string.profile_name));
            String age = getIntent().getStringExtra(getString(R.string.profile_age));
            String gender = getIntent().getStringExtra(getString(R.string.profile_gender));
            String hobbies = getIntent().getStringExtra(getString(R.string.profile_hobbies));

            setData(name, age, gender, hobbies, id);
        }
    }

    private void setData(String name, String age, String gender, String hobbies, final String id) {
        TextView nameView = findViewById(R.id.name);
        TextView ageView = findViewById(R.id.age);
        TextView genderView =findViewById(R.id.gender);
        ImageView imageView = findViewById(R.id.imageContainer);
        final EditText hobbiesEditText = findViewById(R.id.editTextHobbies);
        Button saveButton = findViewById(R.id.save);
        Button deleteButton = findViewById(R.id.delete);

        nameView.setText(name);
        ageView.setText(age);
        genderView.setText(gender);
        hobbiesEditText.setText(hobbies);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update hobbies to database.
                String hobbiesUpdate = hobbiesEditText.getText().toString();
                FirebaseDatabase.getInstance()
                        .getReference(getString(R.string.dbnode_profiles))
                        .child(id)
                        .child("hobie")
                        .setValue(hobbiesUpdate);
                Toast.makeText(getApplicationContext(),"Hobbies Updated", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance()
                        .getReference(getString(R.string.dbnode_profiles))
                        .child(id)
                        .removeValue();
                Toast.makeText(getApplicationContext(),"Profile deleted", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
