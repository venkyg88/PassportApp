package com.passport.venkatgonuguntala.passportapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;

import java.util.List;

/**
 * Created by venkatgonuguntala on 9/19/18.
 */

//TODO: Delete unused class
public class ProfilesListAdapter extends ArrayAdapter<PersonProfile> {

    private Activity context;
    private List<PersonProfile> personProfileList;


    public ProfilesListAdapter(Activity context, List<PersonProfile> personProfilesList) {
        super(context, R.layout.profile_list_item, personProfilesList);
        this.context = context;
        this.personProfileList = personProfilesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.profile_list_item, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewAge = (TextView) listViewItem.findViewById(R.id.textViewAge);

        PersonProfile personProfile = personProfileList.get(position);
        textViewName.setText(personProfile.getName());
        textViewAge.setText(personProfile.getAge());

        return listViewItem;
    }
}
