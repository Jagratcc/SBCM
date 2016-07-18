package fiveexceptions.com.m01sbcm.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fiveexceptions.com.m01sbcm.R;
import fiveexceptions.com.m01sbcm.model.Comment;

/**
 * Created by amit on 23/4/16.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentObjectHolder> {

     List<Comment>  list;
    Activity activity;

    public CommentAdapter(List<Comment> list,Activity activity)

    {
         this.list=list;
         this.activity=activity;
    }

    @Override
    public CommentObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, null);

        CommentObjectHolder viewHolder = new CommentObjectHolder(view);
        return viewHolder;
    }

    public class  CommentObjectHolder extends RecyclerView.ViewHolder
    {

        TextView commentTitle,time,disciption;


        public CommentObjectHolder(View itemView)

        {
            super(itemView);

            commentTitle=(TextView)itemView.findViewById(R.id.comment_title);
            time=(TextView)itemView.findViewById(R.id.time);
            disciption=(TextView)itemView.findViewById(R.id.discription);
        }

    }






    @Override
    public void onBindViewHolder(CommentObjectHolder holder, int position)
    {

        Comment cc= list.get(position);
        // DataBaseHelper dataBaseHelper=new DataBaseHelper(activity);
        // DataBaseManager dataBaseManager=new DataBaseManager();
          // String USERNAME=dataBaseManager.getComment(dataBaseHelper,"select username f")

                // +":" + cc.getServerId()

                holder. commentTitle.setText(cc.getUserDisplayName());
                Log.d("TAG", "onBindViewHolder" + cc.getUserDisplayName());
                Log.d("TAG", "onBindViewHoldersecond:"+cc.getSecound() );

                if(cc.getSecound()<60)
                {

                    Log.d("TAG", "in secound: ");
                    holder.time.setText("Just now");
                }
        if(cc.getSecound()>60 && cc.getSecound()<3600)
                {
                      long p=cc.getSecound();
                    Log.d("TAG", "in minute: ");
                      p=p/60;
                      holder. time.setText(p + " minute");

                  }
        if(cc.getSecound()>3600 && cc.getSecound()<86400)
                  {

                      long p=cc.getSecound();
                      p=p/3600;
                      holder.    time.setText(p + " hour");




                  }
        if(cc.getSecound()>86400 && cc.getSecound()<2592000)
                 {
                     Log.d("TAG", "in days: ");
                    long p=cc.getSecound();
                    p=p/86400;




                    if(p == 1)

                    {
                        holder.time.setText(p + " day");
                    }

                    else
                    {
                        holder.time.setText(p + " days");
                    }



                 }
        if(cc.getSecound()>2592000  && cc.getSecound()<31104000)
                {

                     long p=cc.getSecound();
                     p=p/2592000;
                     holder.time.setText(p + " month");

                }
        Log.d("TAG", "onBindViewHolder: " + cc.getDiscription());
        holder.       disciption.setText(cc.getDiscription());




    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
