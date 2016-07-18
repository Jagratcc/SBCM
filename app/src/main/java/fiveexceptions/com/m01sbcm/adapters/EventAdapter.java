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
import fiveexceptions.com.m01sbcm.activities.EventDetail;
import fiveexceptions.com.m01sbcm.model.Event;
import fiveexceptions.com.m01sbcm.sync.VolleySingleton;
import fiveexceptions.com.m01sbcm.utility.Constants;
import fiveexceptions.com.m01sbcm.utility.Utility;

/**
 * Created by amit on 11/4/16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventObjectHolder> {

    Activity activity;

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList, Activity activity) {

        this.eventList = eventList;
        this.activity = activity;

    }


    public class EventObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mheading, mdate, mplace, mnolike, mnocomment, source;
        ImageButton like, comment;
        NetworkImageView imageView;

        public EventObjectHolder(View itemView) {
            super(itemView);

            mheading = (TextView) itemView.findViewById(R.id.heading);
            source = (TextView) itemView.findViewById(R.id.textView28);
            mdate = (TextView) itemView.findViewById(R.id.date);
            mplace = (TextView) itemView.findViewById(R.id.place);
            mnolike = (TextView) itemView.findViewById(R.id.nolike);
            mnocomment = (TextView) itemView.findViewById(R.id.nocomment1);
            like = (ImageButton) itemView.findViewById(R.id.like);
            comment = (ImageButton) itemView.findViewById(R.id.comment);
            imageView = (NetworkImageView) itemView.findViewById(R.id.event_img_view);

            itemView.setOnClickListener(this);


        }

//Here is an example of using ImageRequest. It retrieves the image specified by the URL and displays it in the app. Note that this snippet interacts with the RequestQueue through a singleton class (see Setting Up a RequestQueue for more discussion of this topic):



        @Override
        public void onClick(View v) {
            // itemListener.recyclerViewListClicked(v, this.getPosition());
            int pos = getLayoutPosition();
            Log.d("Tag....Item position", pos + "");
            Event event = eventList.get(pos);
            int eventId = event.getEventId();
            Log.d("Tag....Item profileId", eventId + "");

            // viewHolder.getItemId()
            Intent i = new Intent(activity, EventDetail.class);
            // int id = getItemId();
            // viewHolder.getItemId()
            i.putExtra("eventid", eventId + "");
            // activity.startActivity(i);
            activity.startActivityForResult(i,1);
        }
    }


    @Override
    public EventObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_row, null);

        EventObjectHolder viewHolder = new EventObjectHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(EventObjectHolder holder, int position) {

        Event event = eventList.get(position);

        holder.mheading.setText(event.getEventHeading());

        try {

            holder.mdate.setText(Utility.getDate(event.getEventDate()));

            holder.mplace.setText("  @  " + event.getEventPlace());
            holder.mnolike.setText("" + event.getNoOfLike());
            holder.mnocomment.setText("" + event.getNoOfComment());
            // holder.imageView.setBackground(event.getEventImage());
            Log.d("TAG", "onBindViewHolder: " + event.getEventsource());
            Log.d("TAG", "onBindViewHolder: " + event.getEventPlace());

            holder.source.setText(event.getEventsource());

            if (event.getThumbnailImgName() != null && event.getThumbnailImgName().length() > 2) {
                String imageUrl = Constants.URL_EVENT_IMG + event.getThumbnailImgName();
                holder.imageView.setImageUrl(imageUrl, VolleySingleton.getsInstance().getImageLoader());
                holder.imageView.setErrorImageResId(R.drawable.img_not_found);
            } else {
                Log.d("thumbnail", " either null or empty " + event.getEventId());
                // holder.imageView.setDefaultImageResId();ErrorImageResId(R.drawable.img_not_found);
                holder.imageView.setDefaultImageResId(R.drawable.img_not_found);
            }

            //Set animation on each view
            // setScaleAnimation(holder.itemView);

        } catch (Exception e) {

        }
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // For animation on RecyclerView
    private final static int FADE_DURATION = 1000 ; // in milliseconds
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
}
