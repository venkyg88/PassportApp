package com.passport.venkatgonuguntala.passportapp;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.passport.venkatgonuguntala.passportapp.Util.Constant;
import com.squareup.picasso.Picasso;

public class ProfileViewActivity extends AppCompatActivity {

    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //layout = findViewById(R.id.rootLayout);


        getIncomingIntents();
    }

    public void getIncomingIntents() {
        /*if(getIntent().hasExtra(getString(R.string.profile_name)) &&
                getIntent().hasExtra(getString(R.string.profile_age)) &&
                getIntent().hasExtra(getString(R.string.profile_gender)) &&
                getIntent().hasExtra(getString(R.string.profile_hobbies)) &&
                getIntent().hasExtra(getString(R.string.profile_images))){*/

            String id = getIntent().getStringExtra(getString(R.string.profile_id));
            String name = getIntent().getStringExtra(getString(R.string.profile_name));
            String age = getIntent().getStringExtra(getString(R.string.profile_age));
            String gender = getIntent().getStringExtra(getString(R.string.profile_gender));
            String hobbies = getIntent().getStringExtra(getString(R.string.profile_hobbies));
            String image = getIntent().getStringExtra("image");
            String color = getIntent().getStringExtra("color");

            setData(name, age, gender, hobbies, id, image, color);
        //}
    }

    private void setData(String name, String age, String gender, String hobbies, final String id, String image, String color) {
        TextView nameView = findViewById(R.id.name);
        TextView ageView = findViewById(R.id.age);
        TextView genderView =findViewById(R.id.gender);
        ImageView imageView = findViewById(R.id.imageContainer);
        final EditText hobbiesEditText = findViewById(R.id.editTextHobbies);
        Button saveButton = findViewById(R.id.save);
        Button deleteButton = findViewById(R.id.delete);

        //TODO: use the "color" to set the backgound layout
        //layout.setBackgroundColor(Integer.parseInt(color));
        nameView.setText(name);
        ageView.setText(age);
        genderView.setText(gender);
        hobbiesEditText.setText(hobbies);
        //TODO: to set image to profile view
        Picasso.get().load(image).into(imageView);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update hobbies to database.
                String hobbiesUpdate = hobbiesEditText.getText().toString();
                FirebaseDatabase.getInstance()
                        .getReference(Constant.DATABASE_PATH_UPLOADS)
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
                        .getReference(getString(R.string.dbnode_users))
                        .child(id)
                        .removeValue();
                Toast.makeText(getApplicationContext(),"Profile deleted", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
