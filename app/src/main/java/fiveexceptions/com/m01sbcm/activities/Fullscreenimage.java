package fiveexceptions.com.m01sbcm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.Profile;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;

public class Fullscreenimage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreenimage);


        Intent intent = getIntent();
        String id = intent.getStringExtra("profileid");
        // int   imgId = intent.getIntExtra("imgId", 0);

    int j = Integer.parseInt(id);
        Log.d("TAG", "onCreate: " + j);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        final DataBaseManager dataBaseManager = new DataBaseManager();
        String profid = " and profile_id = " + j;
        List<Profile> list = dataBaseManager.getprofile(dataBaseHelper, profid);

        Profile profile = list.get(0);










        NetworkImageView imageView= (NetworkImageView)findViewById(R.id.imageView3);
        // imageView.setBackground(profile.getImage());

        String imageUrl = Constants.URL_PROFILE_IMG + profile.getFullImgName();
        Log.d("full img", imageUrl);

        // imageView.setBackgroundResource(imgId);
        if(profile.getFullImgName() != null && profile.getFullImgName().length() > 2){
            imageUrl = Constants.URL_PROFILE_IMG + profile.getFullImgName();
            imageView.setImageUrl(imageUrl, VolleySingleton.getsInstance().getImageLoader());
            imageView.setErrorImageResId(R.drawable.img_not_found);
        }else{
            Log.d("thumbnail", " either null or empty " + profile.getProfileId());
            // holder.imageView.setDefaultImageResId();ErrorImageResId(R.drawable.img_not_found);
            imageView.setDefaultImageResId(R.drawable.img_not_found);
        }
















    }
}
