package fiveexceptions.com.m01sbcm.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.activities.ProfileDetail;
import fiveexceptions.com.m01sbcm.model.Profile;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;

/**
 * Created by amit on 12/4/16.
 */
public class ProfileAdapter extends RecyclerView.Adapter <ProfileAdapter.ProfileObjectHolder>{

    private List<Profile> profileList;
    private Activity activity;

    /*
    int[] imgArray = {R.drawable.p1_t, R.drawable.p2_t, R.drawable.p3_t, R.drawable.p4_t, R.drawable.p5_t,
            R.drawable.p1_t, R.drawable.p2_t, R.drawable.p3_t, R.drawable.p4_t, R.drawable.p5_t };  */

    public ProfileAdapter(List<Profile> profileList, Activity activity) {

        this.profileList = profileList;
        this.activity = activity;
    }


    public class  ProfileObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView mid,mname,mage,mprofession,mmobileno,mmanglik,mcomplex,mhight,mincome,mcity;
        NetworkImageView imageView;


        public ProfileObjectHolder(View itemView) {
            super(itemView);

            mid=(TextView)itemView.findViewById(R.id.id);
            mname=(TextView)itemView.findViewById(R.id.name);
            mage=(TextView)itemView.findViewById(R.id.age);
            mprofession=(TextView)itemView.findViewById(R.id.profession);
            mmanglik=(TextView)itemView.findViewById(R.id.mangalik);
            mcomplex=(TextView)itemView.findViewById(R.id.compl);
            mhight=(TextView)itemView.findViewById(R.id.hight);
            mincome=(TextView)itemView.findViewById(R.id.mincome);
            mcity=(TextView)itemView.findViewById(R.id.address);
            mmobileno=(TextView)itemView.findViewById(R.id.mobileno);

            imageView=(NetworkImageView)itemView.findViewById(R.id.profile_img_view);
            // imageView.setDefaultImageResId(R.drawable.default_image); // image for loading...

            itemView.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
            // itemListener.recyclerViewListClicked(v, this.getPosition());
            int pos = getLayoutPosition();
            Log.d("Tag....Item position", pos + "");
            Profile profile=profileList.get(pos);
            int profileId = profile.getProfileId();
            Log.d("Tag....Item profileId", profileId + "");

            // viewHolder.getItemId()
            Intent i = new Intent(activity,ProfileDetail.class);
            // int id = getItemId();
            // viewHolder.getItemId()
            i.putExtra("profileid", profileId + "");
            // i.putExtra("imgId", imgArray[pos]);
            activity.startActivityForResult(i,1);
            Log.d("Tag....Item profileId", "  i am after start activity for result ");


        }
    }




    @Override
    public ProfileAdapter.ProfileObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_row, null);
        ProfileObjectHolder viewHolder = new ProfileObjectHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.ProfileObjectHolder holder, int position) {

       Profile profile=profileList.get(position);
        if(profile==null)
        {
            Log.d("TAG", "onBindViewHolder: "+"yes i am null");

        }

        holder.mid.setText("" + profile.getProfileId());
        holder.mname.setText(profile.getCandidateName());
        holder.mage.setText(""+profile.getAge()+ " yrs");
        holder.mprofession.setText(profile.getProfession());

        if(profile.getManaglik() != null){
            if(profile.getManaglik().equals("Yes"))
            {
                holder.mmanglik.setText("Manglic");
            }
            if(profile.getManaglik().equals("No"))
            {
                holder.mmanglik.setText("");
            }
            if(profile.getManaglik().equals("Partial"))
            {
                holder.mmanglik.setText("Partial Manglic");
            }
        }

        holder.mcomplex.setText(profile.getColor());
        holder.mhight.setText(""+profile.getHight());
        holder.mincome.setText(""+profile.getIncome()+"/m");
        holder.mcity.setText(profile.getCityRes());
        holder.mmobileno.setText("" + profile.getContectNo());

        // holder.imageView.setBackground(profile.getImage());
        // holder.imageView.setBackground(profile.getImage());

        // niv.setDefaultImageResId(R.drawable._default);
        // niv.setErrorImageResId(R.drawable.error);

        if(profile.getThumbnailImgName() != null && profile.getThumbnailImgName().length() > 2){
            String imageUrl = Constants.URL_PROFILE_IMG + profile.getThumbnailImgName();
            holder.imageView.setImageUrl(imageUrl, VolleySingleton.getsInstance().getImageLoader());
            holder.imageView.setErrorImageResId(R.drawable.img_not_found);
        }else{
            Log.d("thumbnail", " either null or empty " + profile.getProfileId());
            // holder.imageView.setDefaultImageResId();ErrorImageResId(R.drawable.img_not_found);
            holder.imageView.setDefaultImageResId(R.drawable.img_not_found);
        }

        // int idx = position%10;
        // holder.imageView.setBackgroundResource(imgArray[idx]);

        /*
        if(position == 0){

        }else if(position == 1){
            holder.     imageView.setBackgroundResource(R.drawable.indexvv2);
        }else {
            holder.     imageView.setBackgroundResource(R.drawable.indexvv3);
        }
        */
        //Set animation on each view
       // setScaleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }


    @Override
    public long getItemId(int position) {

        // Profile profile=profileList.get(position);
        // int profileId = profile.getProfileId();
        return super.getItemId(position);
        // return profileId;

    }


    // For animation on RecyclerView
    private final static int FADE_DURATION = 1000 ; // in milliseconds
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }




}

