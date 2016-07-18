package fiveexceptions.com.m01sbcm.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.database.DataBaseHelper;
import fiveexceptions.com.m01sbcm.database.DataBaseManager;
import fiveexceptions.com.m01sbcm.model.News;
import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;

public class NewsDetail extends AppCompatActivity {

    ImageView v;
    TextView title,date,city,source,hading,nooflike,noofcomment,likeit;
    ImageButton  like,comment;
    NetworkImageView image;
    List<News> newslist;
    News news;
    String newsid;
    int mnewsid;
    boolean isLikeCountIncreased = false;
    boolean isCommentIncreased = false;
    LinearLayout likelayout,commentlayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        User user=  Utility.getuser();
        final int userid =    user.getUserId();


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.textView17);
        city = (TextView) findViewById(R.id.city);
        source = (TextView) findViewById(R.id.sourcenews);
        noofcomment = (TextView) findViewById(R.id.noofcomment);
        nooflike = (TextView) findViewById(R.id.nooflike);
        hading = (TextView) findViewById(R.id.dis);
        image = (NetworkImageView) findViewById(R.id.imageView2);
        comment = (ImageButton) findViewById(R.id.imageButton3);
        like = (ImageButton) findViewById(R.id.imageButton2);
        likeit=(TextView) findViewById(R.id.likeitnews);
        Intent intent = getIntent();
        newsid = intent.getStringExtra("newsid");
        mnewsid=Integer.parseInt(newsid);
        likelayout=(LinearLayout)findViewById(R.id.likelayout);
        commentlayout=(LinearLayout)findViewById(R.id.commmentlayout);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        DataBaseManager dataBaseManager = new DataBaseManager();
        newslist = dataBaseManager.getNews(dataBaseHelper," and n.news_id = " + mnewsid);

        for(int i=0;i<newslist.size();i++)
        {
            news=newslist.get(i);
            break;
        }




        title.setText(news.getNewsHeading());
      //  date.setText(news.getNewsDate()+"  @");
        date.setText(Utility.getDateTime(news.getNewsDate())+"    @");
        city.setText(news.getNewsPlace());
        Log.d("TAG", "onCreate:i am in news. " + news.getNoOfComment());
        source.setText(news.getNewssource());
        noofcomment.setText(""+news.getNoOfComment());
        nooflike.setText(""+news.getNoOfLike());
        hading.setText(news.getNewsDiscription());

        Log.d("TAG", "onCreate city: " + news.getNewsPlace());
        Log.d("TAG", "onCreate: source"+news.getNewssource());
        Log.d("TAG", "onCreate: dare"+news.getNewsDate());

        // image.setBackground(news.getNewsImage());

        if(news.getThumbnailImgName() != null && news.getThumbnailImgName().length() > 2){
            String imageUrl = Constants.URL_NEWS_IMG + news.getFullImgName();
            image.setImageUrl(imageUrl, VolleySingleton.getsInstance().getImageLoader());
            image.setErrorImageResId(R.drawable.img_not_found);
        }else{
            Log.d("thumbnail", " either null or empty " + news.getNewsId());
            // holder.imageView.setDefaultImageResId();ErrorImageResId(R.drawable.img_not_found);
            image.setDefaultImageResId(R.drawable.img_not_found);
        }

        commentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  i = new Intent(getBaseContext(), CommentDetail.class);
                // int id = getItemId();
                // viewHolder.getItemId()
                i.putExtra("id", news.getNewsId() + "");
                i.putExtra("flag","news");
                Log.d("tagmayank", "" + news.getNewsId());
                startActivityForResult(i, 1);

            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  i = new Intent(getBaseContext(), CommentDetail.class);
                // int id = getItemId();
                // viewHolder.getItemId()
                i.putExtra("id", news.getNewsId() + "");
                i.putExtra("flag","news");
                Log.d("tagmayank", "" + news.getNewsId());
                startActivityForResult(i, 1);

            }
        });

        noofcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  i = new Intent(getBaseContext(), CommentDetail.class);
                // int id = getItemId();
                // viewHolder.getItemId()
                i.putExtra("id", news.getNewsId() + "");
                i.putExtra("flag","news");
                Log.d("tagmayank", "" + news.getNewsId());
                startActivityForResult(i, 1);

            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAG", "onClick: onclick");



                Intent i = new Intent(NewsDetail.this,NewsFullSIzeImage.class);
                i.putExtra("newsid", mnewsid + "");
                startActivity(i);
            }
        });






        boolean C= dataBaseManager.checkNewsLike(NewsDetail.this, mnewsid, userid);
        if(C)
        {
            likeit.setTextColor(getResources().getColor(R.color.black_overlay));
        }


        likeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DataBaseHelper dataBaseHelper = new DataBaseHelper(getBaseContext());
                DataBaseManager dataBaseManager = new DataBaseManager();

                // Log.d("news id=", mnewsid + " : " + userid);
                boolean alreadyLike = dataBaseManager.checkNewsLike(NewsDetail.this, mnewsid, userid);
                Log.d("alreadyLike=", alreadyLike + " ");
                if(!alreadyLike)
                {
                    likeit.setTextColor(getResources().getColor(R.color.black_overlay));
                    boolean result=  dataBaseManager.insertNewsLike(NewsDetail.this, mnewsid, userid);
                    int cnt=    dataBaseManager.noOfNewsLike(NewsDetail.this, mnewsid);
                    nooflike.setText(""+cnt);
                    Log.d("TAG", "onClick: " + result);
                    isLikeCountIncreased = true;
                }

            }
        });


        likelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  i = new Intent(getBaseContext(), LikeList.class);
                // int id = getItemId();
                // viewHolder.getItemId()
                i.putExtra("flag","news");
                i.putExtra("eventid", news.getNewsId() + "");
                // Log.d("tagmayank", "" +news.);

                startActivity(i);



            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  i = new Intent(getBaseContext(), LikeList.class);
                // int id = getItemId();
                // viewHolder.getItemId()
                i.putExtra("flag","news");
                i.putExtra("eventid", news.getNewsId() + "");
                // Log.d("tagmayank", "" +news.);

                startActivity(i);



            }
        });

        nooflike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  i = new Intent(getBaseContext(), LikeList.class);
                // int id = getItemId();
                // viewHolder.getItemId()
                i.putExtra("flag","news");
                i.putExtra("eventid", news.getNewsId() + "");
                // Log.d("tagmayank", "" +news.);

                startActivity(i);



            }
        });



    }


    @Override
    public void onBackPressed()
    {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);

        if(isLikeCountIncreased  || isCommentIncreased){
            returnIntent.putExtra("result",true);
        }
        super.onBackPressed();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        newsid = intent.getStringExtra("id");
        mnewsid=Integer.parseInt(newsid);
        // String isUpdate = intent.getStringExtra("result");
        boolean isCommentAdded = intent.getBooleanExtra("is_comment_added", false);
        Log.d("Detail", "is_comment_added=" + isCommentAdded);

        if(isCommentAdded)
        {

            isCommentIncreased = true;
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            DataBaseManager dataBaseManager = new DataBaseManager();
            newslist = dataBaseManager.getNews(dataBaseHelper," and n.news_id = " + mnewsid);

            for(int i=0;i<newslist.size();i++)
            {
                news=newslist.get(i);
                break;
            }


            title.setText(news.getNewsHeading());
            date.setText(news.getNewsDate()+"  @");
            city.setText(news.getNewsPlace());
            Log.d("TAG", "onCreate:i am in news. "+news.getNoOfComment());
            source.setText(news.getNewssource());
            noofcomment.setText(""+news.getNoOfComment());
            nooflike.setText(""+news.getNoOfLike());
            hading.setText(news.getNewsDiscription());
            // image.setBackground(news.getNewsImage());





        }



    }
}

