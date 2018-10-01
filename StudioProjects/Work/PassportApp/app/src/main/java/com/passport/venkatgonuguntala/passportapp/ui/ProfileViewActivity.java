package com.passport.venkatgonuguntala.passportapp.ui;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.passport.venkatgonuguntala.passportapp.R;
import com.passport.venkatgonuguntala.passportapp.util.AlertDialogForDeleteProfile;
import com.passport.venkatgonuguntala.passportapp.util.Constant;
import com.passport.venkatgonuguntala.passportapp.listeners.ProfileDeleteListener;
import com.squareup.picasso.Picasso;

public class ProfileViewActivity extends AppCompatActivity implements ProfileDeleteListener {

    private ConstraintLayout layout;
    private Button deleteButton;
    private Button saveButton;
    private TextView nameView;
    private TextView ageView;
    private TextView genderView;
    private ImageView imageView;
    private EditText hobbiesEditText;

    String id;
    String name;
    String age;
    String gender;
    String hobbies;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = findViewById(R.id.rootLayout);


        getIncomingIntents();
    }

    public void getIncomingIntents() {

        id = getIntent().getStringExtra(getString(R.string.profile_id));
        name = getIntent().getStringExtra(getString(R.string.profile_name));
        age = getIntent().getStringExtra(getString(R.string.profile_age));
        gender = getIntent().getStringExtra(getString(R.string.profile_gender));
        hobbies = getIntent().getStringExtra(getString(R.string.profile_hobbies));
        image = getIntent().getStringExtra("image");
        int color = getIntent().getIntExtra("color",0);

        setData(color);

    }

    private void setData( int color) {
        nameView = findViewById(R.id.name);
        ageView = findViewById(R.id.age);
        genderView =findViewById(R.id.gender);
        imageView = findViewById(R.id.imageContainer);
        hobbiesEditText = findViewById(R.id.editTextHobbies);
        saveButton = findViewById(R.id.save);
        deleteButton = findViewById(R.id.delete);


        layout.setBackgroundColor(color);
        nameView.setText(name);
        ageView.setText(age);
        genderView.setText(gender);
        hobbiesEditText.setText(hobbies);
        Picasso.get().load(image).into(imageView);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update hobbies to database.
                updateHobbiesToDatabase(id);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogForDeleteProfile alertDialogFragment = new AlertDialogForDeleteProfile();
                alertDialogFragment.show(getFragmentManager(), "delete");
            }
        });
    }

    private void updateHobbiesToDatabase(String id) {
        String hobbiesUpdate = hobbiesEditText.getText().toString();
        FirebaseDatabase.getInstance()
                .getReference(Constant.DATABASE_PATH_UPLOADS)
                .child(id)
                .child("hobie")
                .setValue(hobbiesUpdate);
        Toast.makeText(getApplicationContext(),"Hobbies Updated", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void deleteProfile() {
        FirebaseDatabase.getInstance()
                .getReference(getString(R.string.dbnode_users))
                .child(id)
                .removeValue();
        Toast.makeText(getApplicationContext(),"Profile deleted", Toast.LENGTH_LONG).show();
        finish();
    }
}
