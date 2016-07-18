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
import fiveexceptions.com.m01sbcm.model.Event;
import fiveexceptions.com.m01sbcm.model.User;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;

public class EventDetail extends AppCompatActivity {
    ImageView v;
    TextView title,date,city,source,hading,nooflike,noofcomment,likeit;
    ImageButton  like,comment;
    NetworkImageView image;
    List<Event> eventlist;
    Event event;
    String id;
    Intent i;
    // int cnt;
    int noofcnt;
    boolean isLikeCountIncreased = false;
    boolean isCommentIncreased = false;
    ImageButton likeclick;
    LinearLayout likelayout,commentlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);


        User user = Utility.getuser();
        final int userid = user.getUserId();



        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.textView20);
        city = (TextView) findViewById(R.id.city);
        source = (TextView) findViewById(R.id.source);
        noofcomment = (TextView) findViewById(R.id.noofcomment);
        nooflike = (TextView) findViewById(R.id.nooflike);
        hading = (TextView) findViewById(R.id.dis);
        image = (NetworkImageView) findViewById(R.id.imageView2);
        comment = (ImageButton) findViewById(R.id.imageButton3);
        like = (ImageButton) findViewById(R.id.imageButton2);
        likeclick = (ImageButton) findViewById(R.id.pinButton);
        likeit=(TextView) findViewById(R.id.likeit);
        likelayout=(LinearLayout)findViewById(R.id.eventlikelayout);
        commentlayout=(LinearLayout)findViewById(R.id.eventcommentlayout);

        Intent intent = getIntent();
        id = intent.getStringExtra("eventid");
        final int eventid = Integer.parseInt(id);


             image.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Log.d("TAG", "onClick: i am at image 2");
                     Intent i = new Intent(EventDetail.this, FullSizeImageEvent.class);
                     i.putExtra("eventid", id + "");
                     startActivity(i);
                 }
             });


        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        final DataBaseManager dataBaseManager = new DataBaseManager();
        //  String query="select e.*, case when temp.comment_count is null then 0 else temp.comment_count end c_count, case when temp2.like_count is null then 0 else temp2.like_count end l_count from event e  left join (SELECT count(1) comment_count, event_id from commentevent group by event_id ) temp on  e.event_id = temp.event_id left join (SELECT count(1) like_count, event_id from like_event group by event_id ) temp2 on  e.event_id = temp2.event_id";
        // String ehrtr codition

        eventlist = dataBaseManager.getEvent(dataBaseHelper, " and e.event_id = " + eventid);


        for (int i = 0; i < eventlist.size(); i++) {
            event = eventlist.get(i);
            break;
            // Log.d("tagmayank",""+id);
        }


        commentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = new Intent(getBaseContext(), CommentDetail.class);
                i.putExtra("flag", "event");
                // int id = getItemId();
                // viewHolder.getItemId()
                i.putExtra("id", event.getEventId() + "");
                Log.d("tagmayank", "" + event.getEventId());
                startActivityForResult(i, 1);

            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = new Intent(getBaseContext(), CommentDetail.class);
                i.putExtra("flag", "event");
                // int id = getItemId();
                // viewHolder.getItemId()
                i.putExtra("id", event.getEventId() + "");
                Log.d("tagmayank", "" + event.getEventId());
                startActivityForResult(i, 1);

            }
        });

        noofcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = new Intent(getBaseContext(), CommentDetail.class);
                i.putExtra("flag", "event");
                // int id = getItemId();
                // viewHolder.getItemId()
                i.putExtra("id", event.getEventId() + "");
                Log.d("tagmayank", "" + event.getEventId());
                startActivityForResult(i, 1);

            }
        });


        boolean C = dataBaseManager.checkEventLike(EventDetail.this, eventid, userid);
        if (C) {
            likeit.setTextColor(getResources().getColor(R.color.black_overlay));
        }


        likeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // int c = 0;

                boolean alreadyLike = dataBaseManager.checkEventLike(EventDetail.this, eventid, userid);


                if (!alreadyLike) {
                    likeit.setTextColor(getResources().getColor(R.color.black_overlay));
                    boolean result = dataBaseManager.insertEventLike(EventDetail.this, eventid, userid);
                    int cnt = dataBaseManager.noOfEventLike(EventDetail.this, eventid);
                    nooflike.setText("" + cnt);
                    Log.d("TAG", "onClick: " + result);
                    isLikeCountIncreased = true;
                }


            }
        });

        likelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = new Intent(getBaseContext(), LikeList.class);
                // int id = getItemId();
                // viewHolder.getItemId()i.
                i.putExtra("flag", "event");
                i.putExtra("eventid", event.getEventId() + "");
                Log.d("tagmayank", "" + event.getEventId());

                startActivity(i);


            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = new Intent(getBaseContext(), LikeList.class);
                // int id = getItemId();
                // viewHolder.getItemId()i.
                i.putExtra("flag", "event");
                i.putExtra("eventid", event.getEventId() + "");
                Log.d("tagmayank", "" + event.getEventId());

                startActivity(i);


            }
        });

        nooflike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = new Intent(getBaseContext(), LikeList.class);
                // int id = getItemId();
                // viewHolder.getItemId()i.
                i.putExtra("flag", "event");
                i.putExtra("eventid", event.getEventId() + "");
                Log.d("tagmayank", "" + event.getEventId());

                startActivity(i);


            }
        });




        title.setText(event.getEventHeading());
        // date.setText(event.getEventDate() + "    @");
        date.setText(Utility.getDateTime(event.getEventDate())+"    @");
        city.setText(event.getEventPlace());

        // String name=dataBaseManager.getsource1(dataBaseHelper,event.getEventsource());
        String name = event.getEventsource();
        source.setText(event.getEventsource());

        noofcomment.setText("" + event.getNoOfComment());
        nooflike.setText("" + event.getNoOfLike());
        hading.setText(event.getEventDiscription());

        // image.setBackground(event.getEventImage());
        if (event.getThumbnailImgName() != null && event.getThumbnailImgName().length() > 2) {
            String imageUrl = Constants.URL_EVENT_IMG + event.getFullImgName();
            image.setImageUrl(imageUrl, VolleySingleton.getsInstance().getImageLoader());
            image.setErrorImageResId(R.drawable.img_not_found);
        } else {
            Log.d("thumbnail", " either null or empty " + event.getEventId());
            // holder.imageView.setDefaultImageResId();ErrorImageResId(R.drawable.img_not_found);
            image.setDefaultImageResId(R.drawable.img_not_found);
        }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Intent intent = getIntent();

        Log.d("Result", "data=" + data);
        String eventid = data.getStringExtra("id");
        Log.d("Result ", "eventid=" + eventid);


        int   meventid=Integer.parseInt(eventid);
        boolean isCommentAdded = data.getBooleanExtra("is_comment_added", false);

        Log.d("Detail", "is_comment_added=" + isCommentAdded);

        if(isCommentAdded)
        {

            isCommentIncreased = true;

            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            final DataBaseManager dataBaseManager = new DataBaseManager();
            //  String query="select e.*, case when temp.comment_count is null then 0 else temp.comment_count end c_count, case when temp2.like_count is null then 0 else temp2.like_count end l_count from event e  left join (SELECT count(1) comment_count, event_id from commentevent group by event_id ) temp on  e.event_id = temp.event_id left join (SELECT count(1) like_count, event_id from like_event group by event_id ) temp2 on  e.event_id = temp2.event_id";
            // String ehrtr codition
            eventlist = dataBaseManager.getEvent(dataBaseHelper," and e.event_id = " + eventid);
            for(int i=0;i<eventlist.size();i++)
            {
                event=eventlist.get(i);
                break;
                // Log.d("tagmayank",""+id);
            }


            title.setText(event.getEventHeading());
            date.setText(Utility.getDateTime(event.getEventDate())+"    @");
            city.setText(event.getEventPlace());

            // String name=dataBaseManager.getsource1(dataBaseHelper,event.getEventsource());
            String name = event.getEventsource();
            source.setText(event.getEventsource());



            noofcomment.setText(""+event.getNoOfComment());
             noofcnt=event.getNoOfComment();
            nooflike.setText(""+event.getNoOfLike());
            hading.setText(event.getEventDiscription());
            // image.setBackground(event.getEventImage());


        }






    }
}
