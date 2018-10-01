package com.passport.venkatgonuguntala.passportapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.passport.venkatgonuguntala.passportapp.ui.ProfileViewActivity;
import com.passport.venkatgonuguntala.passportapp.R;
import com.passport.venkatgonuguntala.passportapp.util.Constant;
import com.passport.venkatgonuguntala.passportapp.util.SortByAgeAscending;
import com.passport.venkatgonuguntala.passportapp.util.SortByAgeDescending;
import com.passport.venkatgonuguntala.passportapp.util.SortByNameAscending;
import com.passport.venkatgonuguntala.passportapp.util.SortByNameDescending;
import com.passport.venkatgonuguntala.passportapp.model.PersonProfile;
import com.squareup.picasso.Picasso;

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
        public ImageView imageView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            nameLabel = rootView.findViewById(R.id.textViewName);
            ageLabel = rootView.findViewById(R.id.textViewAge);
            imageView = rootView.findViewById(R.id.listImageView);
        }

        public void bindHolder(final PersonProfile profile, final int position) {
            final int color;

            if(profile.getGender().equalsIgnoreCase(context.getResources().getString(R.string.male))) {
                color = ContextCompat.getColor(context, R.color.colorBlue);
                rootView.setBackgroundColor(color);
            } else {
                color = ContextCompat.getColor(context, R.color.colorPink);
                rootView.setBackgroundColor(color);
            }

            nameLabel.setText(profile.getName());
            ageLabel.setText(profile.getAge());
            Picasso.get().load(profile.getImage()).into(imageView);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileViewActivity.class);
                    intent.putExtra(Constant.NAME, profile.getName());
                    intent.putExtra(Constant.AGE, profile.getAge());
                    intent.putExtra(Constant.GENDER, profile.getGender());
                    intent.putExtra(Constant.HOBBIES, profile.getHobie());
                    intent.putExtra(Constant.ID, profile.getId());
                    intent.putExtra(Constant.IMAGE, profile.getImage());
                    intent.putExtra(Constant.COLOR, color);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
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

    public void clearSort() {
        profiles = defaultProfiles;
        notifyDataSetChanged();
    }
}
