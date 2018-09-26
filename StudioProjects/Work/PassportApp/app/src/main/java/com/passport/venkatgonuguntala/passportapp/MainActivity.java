package com.passport.venkatgonuguntala.passportapp;

import android.content.Intent;
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
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.passport.venkatgonuguntala.passportapp.adapter.ProfileAdapter;
import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    //private ListView listViewProfiles;
    private RecyclerView recyclerViewProfiles;
    private DatabaseReference databaseProfiles;
    private List<PersonProfile> personProfileList;
    private ProfileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        recyclerViewProfiles = (RecyclerView) findViewById(R.id.recycle_view_profiles);
        databaseProfiles = FirebaseDatabase.getInstance().getReference(getString(R.string.dbnode_profiles));
        personProfileList = new ArrayList<>();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseProfiles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                personProfileList.clear(); //clearing if already present
                for(DataSnapshot profilesSnapshot: dataSnapshot.getChildren()) {
                    PersonProfile personProfile = profilesSnapshot.getValue(PersonProfile.class);

                    personProfileList.add(personProfile);
                }

                adapter = new ProfileAdapter(personProfileList, getApplicationContext());
                recyclerViewProfiles.setAdapter(adapter);

                RecyclerView.LayoutManager layoutManger = new LinearLayoutManager(MainActivity.this);
                recyclerViewProfiles.setLayoutManager(layoutManger);

                recyclerViewProfiles.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_filter_male: filter(getString(R.string.male));
                                break;
            case R.id.action_filter_female: filter(getString(R.string.female));
                                break;
            case R.id.action_sort_ascending_name: sortByNameAscending();
                                break;
            case R.id.action_sort_descending_name: sortByNameDescending();
                                break;
            case R.id.action_sort_ascending_age: sortByAgeAscending();
                                break;
            case R.id.action_sort_descending_age: sortByAgeDescending();
                Toast.makeText(getApplicationContext(), "filter pressed", Toast.LENGTH_LONG).show();
                                break;
            case R.id.action_sort_clear:
            case R.id.action_filter_clear:
                adapter.clearSort();break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void filter(String gender){
        ArrayList<PersonProfile> profilesFiltered = new ArrayList<>();
        for (PersonProfile profiles : personProfileList) {
            if (gender.equalsIgnoreCase(profiles.getGender()))
                profilesFiltered.add(profiles);
        }
        Toast.makeText(getApplicationContext(), profilesFiltered.get(1).getName(),Toast.LENGTH_LONG).show();
        adapter.filterList(profilesFiltered);
    }

    private void sortByNameAscending() {
        adapter.sortByNameAscending();
    }

    private void sortByNameDescending() {
        adapter.sortByNameDescending();
    }

    private void sortByAgeAscending() {
        adapter.sortByAgeAscending();
    }

    private void sortByAgeDescending() {
        adapter.sortByAgeDescending();
    }

}
