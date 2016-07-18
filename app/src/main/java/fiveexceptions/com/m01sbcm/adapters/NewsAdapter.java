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
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.activities.NewsDetail;
import fiveexceptions.com.m01sbcm.model.News;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;

/**
 * Created by amit on 12/4/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsObjectHolder> {

    Activity activity;

    private List<News> newsList;


    public NewsAdapter(List<News> newsList, Activity activity) {
        this.activity = activity;
        this.newsList = newsList;


    }


    public class NewsObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mheading, mdate, mplace, mnolike, mnocomment, msource;
        ImageButton like, comment;
        NetworkImageView imageView;


        public NewsObjectHolder(View itemView) {
            super(itemView);

            mheading = (TextView) itemView.findViewById(R.id.heading);
            mdate = (TextView) itemView.findViewById(R.id.date);
            mplace = (TextView) itemView.findViewById(R.id.place);
            mnolike = (TextView) itemView.findViewById(R.id.nolike);
            msource = (TextView) itemView.findViewById(R.id.source);
            mnocomment = (TextView) itemView.findViewById(R.id.nocomment1);
            like = (ImageButton) itemView.findViewById(R.id.like);
            comment = (ImageButton) itemView.findViewById(R.id.comment);
            imageView = (NetworkImageView) itemView.findViewById(R.id.news_img_view);
            itemView.setOnClickListener(this);
            ;


        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            Log.d("Tag....Item position", pos + "");
            News profile = newsList.get(pos);
            int newsId = profile.getNewsId();
            Log.d("Tag....Item profileId", newsId + "");

            // viewHolder.getItemId()
            Intent i = new Intent(activity, NewsDetail.class);
            // int id = getItemId();
            // viewHolder.getItemId()
            i.putExtra("newsid", newsId + "");
            // activity.startActivity(i);
            activity.startActivityForResult(i,1);

        }
    }


    @Override
    public NewsAdapter.NewsObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_row, null);

        NewsAdapter.NewsObjectHolder viewHolder = new NewsAdapter.NewsObjectHolder(view);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(NewsAdapter.NewsObjectHolder holder, int position) {

        News news = newsList.get(position);

        holder.mheading.setText(news.getNewsHeading());
        String newsDate =  news.getNewsDate();
        if(newsDate != null && newsDate.length() > 16 ){
            newsDate = newsDate.substring(0,16);
        }
        //holder.mdate.setText(newsDate );
        holder.mdate.setText(Utility.getDateTime(news.getNewsDate()));
        holder.mplace.setText("  @  "+news.getNewsPlace());
        holder.mnolike.setText("" + news.getNoOfLike());
        holder.mnocomment.setText("" + news.getNoOfComment());
        holder.msource.setText(news.getNewssource());

        // holder.imageView.setBackground(news.getNewsImage());
        if(news.getThumbnailImgName() != null && news.getThumbnailImgName().length() > 2){
            String imageUrl = Constants.URL_NEWS_IMG + news.getThumbnailImgName();
            holder.imageView.setImageUrl(imageUrl, VolleySingleton.getsInstance().getImageLoader());
            holder.imageView.setErrorImageResId(R.drawable.img_not_found);
        }else{
            Log.d("thumbnail", " either null or empty " + news.getNewsId());
            // holder.imageView.setDefaultImageResId();ErrorImageResId(R.drawable.img_not_found);
            holder.imageView.setDefaultImageResId(R.drawable.img_not_found);
        }
        //Set animation on each view
        // setScaleAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // For animation on RecyclerView
    private final static int FADE_DURATION = 1000; // in milliseconds

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
}
