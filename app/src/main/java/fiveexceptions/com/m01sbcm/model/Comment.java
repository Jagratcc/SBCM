package fiveexceptions.com.m01sbcm.model;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by amit on 23/4/16.
 */
public class Comment {

    int id;
    int serverId;
    String discription;
    int userid;
    String userDisplayName;
    int eventid;
    int newsid;
    long secound;



    public void setEventComment(int id, int serverId, String dis, int userid,int eventid,long time, String userDisplayName)

    {
        this.id=id;
        this.serverId=serverId;
        this.discription=dis;
        this.userid=userid;
        this.eventid=eventid;
        Calendar C=Calendar.getInstance();
        long time1=C.getTimeInMillis();
        secound=(time1-time)/1000;
        this.userDisplayName = userDisplayName;
        Log.d("TAG","time"+time+"time1"+time1+"secound"+secound);
    }


    public void setNewsComment(int id, int serverId, String dis, int userid, int newsid, long time, String userDisplayName)

    {
        this.userDisplayName = userDisplayName;
        this.id=id;
        this.serverId=serverId;
        this.discription=dis;
        this.userid=userid;
        this.newsid=newsid;
        Calendar C=Calendar.getInstance();
        long time1=C.getTimeInMillis();
        secound=(time1-time)/1000;
        this.userDisplayName = userDisplayName;
        Log.d("TAG","time"+time+"time1"+time1+"secound"+secound);


    }


    public long getSecound() {
        return secound;
    }

    public void setSecound(int secound) {
        this.secound = secound;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewsid() {
        return newsid;
    }

    public void setNewsid(int newsid) {
        this.newsid = newsid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
