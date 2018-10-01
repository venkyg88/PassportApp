package com.passport.venkatgonuguntala.passportapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.passport.venkatgonuguntala.passportapp.R;
import com.passport.venkatgonuguntala.passportapp.util.AlertDialogForErrorAndConnectivity;
import com.passport.venkatgonuguntala.passportapp.util.Constant;
import com.passport.venkatgonuguntala.passportapp.adapter.ProfileAdapter;
import com.passport.venkatgonuguntala.passportapp.listeners.AppCloseListener;
import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AppCloseListener {

    private RecyclerView profileRecyclerView;
    private DatabaseReference profileDatabaseReference;
    private ArrayList<PersonProfile> personProfileList;
    private ProfileAdapter profileAdapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolBar();
        setFloatingButtonForCreatingProfile();
        profileRecyclerView = findViewById(R.id.recycle_view_profiles);
        profileDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);
        personProfileList = new ArrayList<>();
        emptyView = findViewById(R.id.empty_view);
    }

    private void setFloatingButtonForCreatingProfile() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
                    startActivity(intent);
                } else {
                    alertUserOrConnectivityError();
                }
            }
        });
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkAvailable()) {

            profileDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    personProfileList.clear(); //clearing if already present
                    for (DataSnapshot profilesSnapshot : dataSnapshot.getChildren()) {
                        PersonProfile personProfile = profilesSnapshot.getValue(PersonProfile.class);

                        personProfileList.add(personProfile);
                    }
                    checkAvailableData(personProfileList);
                    updateRecyclerViewAdapter();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    alertUserOrConnectivityError();
                }
            });
        } else {
            alertUserOrConnectivityError();
            checkAvailableData(personProfileList);
        }
    }

    private void alertUserOrConnectivityError() {
        AlertDialogForErrorAndConnectivity dialog = new AlertDialogForErrorAndConnectivity();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void updateRecyclerViewAdapter() {
        profileAdapter = new ProfileAdapter(personProfileList, getApplicationContext());
        profileRecyclerView.setAdapter(profileAdapter);

        RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(MainActivity.this);
        profileRecyclerView.setLayoutManager(layoutManger);

        profileRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (profileAdapter != null) {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_filter_male:
                    filter(getString(R.string.male));
                    break;
                case R.id.action_filter_female:
                    filter(getString(R.string.female));
                    break;
                case R.id.action_sort_ascending_name:
                    profileAdapter.sortByNameAscending();
                    break;
                case R.id.action_sort_descending_name:
                    profileAdapter.sortByNameDescending();
                    break;
                case R.id.action_sort_ascending_age:
                    profileAdapter.sortByAgeAscending();
                    break;
                case R.id.action_sort_descending_age:
                    profileAdapter.sortByAgeDescending();
                    break;
                case R.id.action_sort_clear:
                case R.id.action_filter_clear:
                    checkAvailableData(personProfileList);
                    profileAdapter.clearSort();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void filter(String gender) {

        ArrayList<PersonProfile> profilesFiltered = new ArrayList<>();
        for (PersonProfile profiles : personProfileList) {
            if (gender.equalsIgnoreCase(profiles.getGender()))
                profilesFiltered.add(profiles);
        }
        checkAvailableData(profilesFiltered);
        profileAdapter.filterList(profilesFiltered);
    }

    private void checkAvailableData(ArrayList<PersonProfile> personProfileList) {
        if (personProfileList.isEmpty()) {
            profileRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            profileRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void close() {
        finish();
    }
}
