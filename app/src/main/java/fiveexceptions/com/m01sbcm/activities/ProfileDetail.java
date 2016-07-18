package fiveexceptions.com.m01sbcm.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.Profile;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;

public class ProfileDetail extends AppCompatActivity {

    List<Profile> list;
    android.support.v7.widget.Toolbar ntoolbar;
    Profile profile;
    TextView  Fathername, Mothername, Birthdate, Birthcity, hight, complex, subcast, manglik, contect, education, income, profession, Address, expectation, source,likeit;
    ImageButton search;
    int flagforfavirouite;

    int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        Intent intent = getIntent();
        String id = intent.getStringExtra("profileid");
        // int   imgId = intent.getIntExtra("imgId", 0);

        j = Integer.parseInt(id);
        Log.d("TAG", "onCreate: " + j);


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        final DataBaseManager dataBaseManager = new DataBaseManager();
        String profid = " and profile_id = " + j;
        list = dataBaseManager.getprofile(dataBaseHelper, profid);

        profile = list.get(0);


        Log.d("TAG", "onCreate:i am not nukll okkkk  ");

        // set title
        TextView tv = (TextView) findViewById(R.id.profile_toolbar_title);

        Log.d("TAG", "onCreate: " + profile.getFlag());
        tv.setText(profile.getCandidateName());

        Fathername = (TextView) findViewById(R.id.fathername);
        Mothername = (TextView) findViewById(R.id.mincome);
        Birthdate = (TextView) findViewById(R.id.DATEOFBIRTH);
        Birthcity = (TextView) findViewById(R.id.CITY);
        hight = (TextView) findViewById(R.id.hight);
        complex = (TextView) findViewById(R.id.COMPLEX);
        subcast = (TextView) findViewById(R.id.cast);
        manglik = (TextView) findViewById(R.id.manglik);
        contect = (TextView) findViewById(R.id.contect);
        education = (TextView) findViewById(R.id.education);
        income = (TextView) findViewById(R.id.income);
        profession = (TextView) findViewById(R.id.profession);
        ntoolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        Address = (TextView) findViewById(R.id.address);
        expectation = (TextView) findViewById(R.id.exception);
        search = (ImageButton) findViewById(R.id.pinButton);
        source=(TextView) findViewById(R.id.sourceid);
        likeit=(TextView) findViewById(R.id.textView20);


        search.setOnClickListener(view);


        Fathername.setText(profile.getFatherName() + "");
        Mothername.setText(profile.getMotherName() + "");
        Birthcity.setText(profile.getCityBirth() + "");


        String  date= Utility.getDateTime(profile.getDateBirth());
        Birthdate.setText(date);

        //String time=Utility.getDateTime(profile.getDateBirth()).substring(10);
       // Birthdate.setText(profile.getBirthdisplaydate()+ " "  +time);
       // Log.d("TAG", "onCreate: "+profile.getBirthdisplaydate() +time);
        hight.setText(profile.getHight() + "");
        complex.setText(profile.getColor() + "");
        contect.setText(profile.getContectNo() + "");
        subcast.setText("" + profile.getCast());
        education.setText("" + profile.getEducation());
        income.setText("" + profile.getIncome() + " per month");
        profession.setText("" + profile.getProfession());
        Address.setText("" + profile.getAddress());
        expectation.setText("" + profile.getExpectation());
        manglik.setText("" + profile.getManaglik());
        source.setText(""+profile.getSource());
        Log.d("TAG", "onCreatesource: " + profile.getSource());
        Log.d("TAG", "onCreateexpectation:"+profile.getExpectation());
        contect.setTextColor(getResources().getColor(R.color.colorPrimary));

        contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+" + contect.getText().toString().trim()));
                if (ActivityCompat.checkSelfPermission(ProfileDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);


            }
        });


        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.imageView3);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ProfileDetail.this, Fullscreenimage.class);
                i.putExtra("profileid", j + "");
                startActivity(i);
            }
        });
        // imageView.setBackground(profile.getImage());

        String imageUrl = Constants.URL_PROFILE_IMG + profile.getFullImgName();
        Log.d("full img", imageUrl);

        // imageView.setBackgroundResource(imgId);
        if (profile.getFullImgName() != null && profile.getThumbnailImgName().length() > 2) {
            imageUrl = Constants.URL_PROFILE_IMG + profile.getThumbnailImgName();
            imageView.setImageUrl(imageUrl, VolleySingleton.getsInstance().getImageLoader());
            imageView.setErrorImageResId(R.drawable.img_not_found);
        } else {
            Log.d("thumbnail", " either null or empty " + profile.getProfileId());
            // holder.imageView.setDefaultImageResId();ErrorImageResId(R.drawable.img_not_found);
            imageView.setDefaultImageResId(R.drawable.img_not_found);
        }
        Log.d("flag on i am in view", "" + profile.getFlag());

        if (profile.getFlag() == 1) {
            search.setBackground(getResources().getDrawable(R.mipmap.ahrt));
        }

    }


    View.OnClickListener view = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            flagforfavirouite = profile.getFlag();

            Log.d("flag in mayank", "" + profile.getFlag());
            boolean result = false;
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
            final DataBaseManager dataBaseManager = new DataBaseManager();

            if (profile.getFlag() == 0) {

                profile.setFlag(1);
                search.setBackground(getResources().getDrawable(R.mipmap.ahrt));
                boolean b = dataBaseManager.setProfileFavFlag(ProfileDetail.this, 1, j);


            } else {

                profile.setFlag(0);
                boolean b = dataBaseManager.setProfileFavFlag(ProfileDetail.this, 0, j);
                search.setBackground(getResources().getDrawable(R.mipmap.hrt));


            }


        }
    };


    @Override
    public void onBackPressed() {


        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);
        Log.d("Tag....Item profileId", "  i am after start activity for result " + profile.getFlag());


        if (profile.getFlag() == 0) {
            returnIntent.putExtra("flag", true);
        }

        else if(profile.getFlag() == 1) {
            returnIntent.putExtra("flag", false);
        }

        returnIntent.putExtra("profile",true);

        super.onBackPressed();

    }
}
