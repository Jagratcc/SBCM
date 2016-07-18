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
import fiveexceptions.com.m01sbcm.model.News;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class NewsFullSIzeImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_full_size_image);


        Intent intent = getIntent();
        String id = intent.getStringExtra("newsid");
        // int   imgId = intent.getIntExtra("imgId", 0);

        int j = Integer.parseInt(id);
        Log.d("TAG", "onCreate: " + j);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
        final DataBaseManager dataBaseManager = new DataBaseManager();
        String newsid =  " and n.news_id = " + j;
        List<News> list = dataBaseManager.getNews(dataBaseHelper, newsid);

        News news = list.get(0);










        NetworkImageView imageView= (NetworkImageView)findViewById(R.id.imageViewnews);
        // imageView.setBackground(profile.getImage());

        String imageUrl = Constants.URL_NEWS_IMG + news.getFullImgName();
        Log.d("full img", imageUrl);

        // imageView.setBackgroundResource(imgId);
        if(news.getFullImgName() != null && news.getFullImgName().length() > 2){
            imageUrl = Constants.URL_NEWS_IMG + news.getFullImgName();
            imageView.setImageUrl(imageUrl, VolleySingleton.getsInstance().getImageLoader());
            imageView.setErrorImageResId(R.drawable.img_not_found);
        }else{
            Log.d("thumbnail", " either null or empty " + news.getNewsId());
            // holder.imageView.setDefaultImageResId();ErrorImageResId(R.drawable.img_not_found);
            imageView.setDefaultImageResId(R.drawable.img_not_found);
        }
















    }












}
