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
import fiveexceptions.com.m01sbcm.model.Event;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullSizeImageEvent extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_size_image_event);


        Intent intent = getIntent();
        String id = intent.getStringExtra("eventid");
        // int   imgId = intent.getIntExtra("imgId", 0);

        int j = Integer.parseInt(id);
        Log.d("TAG", "onCreate: " + j);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        final DataBaseManager dataBaseManager = new DataBaseManager();
        String profid =  " and e.event_id = " + j;
        List<Event> list = dataBaseManager.getEvent(dataBaseHelper, profid);

        Event event = list.get(0);










        NetworkImageView imageView= (NetworkImageView)findViewById(R.id.imageViewevent);
        // imageView.setBackground(profile.getImage());

        String imageUrl = Constants.URL_EVENT_IMG + event.getFullImgName();
        Log.d("full img", imageUrl);

        // imageView.setBackgroundResource(imgId);
        if(event.getFullImgName() != null && event.getFullImgName().length() > 2){
            imageUrl = Constants.URL_EVENT_IMG + event.getFullImgName();
            imageView.setImageUrl(imageUrl, VolleySingleton.getsInstance().getImageLoader());
            imageView.setErrorImageResId(R.drawable.img_not_found);
        }else{
            Log.d("thumbnail", " either null or empty " + event.getEventId());
            // holder.imageView.setDefaultImageResId();ErrorImageResId(R.drawable.img_not_found);
            imageView.setDefaultImageResId(R.drawable.img_not_found);
        }
















    }

















}
