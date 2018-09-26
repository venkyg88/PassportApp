package com.passport.venkatgonuguntala.passportapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.passport.venkatgonuguntala.passportapp.CreateProfileActivity;
import com.passport.venkatgonuguntala.passportapp.ProfileViewActivity;
import com.passport.venkatgonuguntala.passportapp.R;
import com.passport.venkatgonuguntala.passportapp.Util.SortByAgeAscending;
import com.passport.venkatgonuguntala.passportapp.Util.SortByAgeDescending;
import com.passport.venkatgonuguntala.passportapp.Util.SortByNameAscending;
import com.passport.venkatgonuguntala.passportapp.Util.SortByNameDescending;
import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by venkatgonuguntala on 9/20/18.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private List<PersonProfile> profiles;
    private List<PersonProfile> defaultProfiles;
    private Context context;

    public ProfileAdapter(List<PersonProfile> profiles, Context context) {
        this.defaultProfiles = profiles;
        this.profiles = profiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item, parent, false);
        ProfileViewHolder viewHolder = new ProfileViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.bindHolder(profiles.get(position), position);

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder{

        public TextView nameLabel;
        public TextView ageLabel;
        public View rootView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            nameLabel = rootView.findViewById(R.id.textViewName);
            ageLabel = rootView.findViewById(R.id.textViewAge);
        }

        public void bindHolder(final PersonProfile profile, final int position) {
            //TODO: need to check the gender and handle the color condition

            nameLabel.setText(profile.getName());
            ageLabel.setText(profile.getAge());
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, position+"",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, ProfileViewActivity.class);
                    intent.putExtra("name", profile.getName());
                    intent.putExtra("age", profile.getAge());
                    intent.putExtra("gender", profile.getGender());
                    intent.putExtra("hobbies", profile.getHobie());
                    intent.putExtra("id", profile.getId());
                    context.startActivity(intent);
                }
            });
            //TODO: sample code to set the image
            /*Glide.with(context)
                    .load(contact.getImage())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.thumbnail);*/
        }
    }

    public void filterList(ArrayList<PersonProfile> filteredList) {
        profiles = filteredList;
        notifyDataSetChanged();
    }

    public void sortByNameAscending() {
        Collections.sort(profiles, new SortByNameAscending());
        notifyDataSetChanged();
    }

    public void sortByNameDescending() {
        Collections.sort(profiles, new SortByNameDescending());
        notifyDataSetChanged();
    }

    public void sortByAgeAscending() {
        Collections.sort(profiles, new SortByAgeAscending());
        notifyDataSetChanged();
    }

    public void sortByAgeDescending() {
        Collections.sort(profiles, new SortByAgeDescending());
        notifyDataSetChanged();
    }

    //TODO: not working
    public void clearSort() {
        profiles = defaultProfiles;
        notifyDataSetChanged();
    }
}
