package fiveexceptions.com.m01sbcm.model;

import android.util.Log;

/**
 * Created by amit on 9/4/16.
 */
public class Event {

    int    eventId;
    String eventHeading;
    int    eventType;
    String  eventPlace;
    String  eventDiscription;
    String  eventDate;
    String  eventaddon;
    // Drawable eventImage;
    String eventsource;
    int noOfLike;
    int noOfComment;
    String thumbnailImgName;
    String fullImgName;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventHeading() {
        return eventHeading;
    }

    public void setEventHeading(String eventHeading) {
        this.eventHeading = eventHeading;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getEventDiscription() {
        return eventDiscription;
    }

    public void setEventDiscription(String eventDiscription) {
        this.eventDiscription = eventDiscription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventaddon() {
        return eventaddon;
    }

    public void setEventaddon(String eventaddon) {
        this.eventaddon = eventaddon;
    }

    /*
    public Drawable getEventImage() {
        return eventImage;
    }

    public void setEventImage(Drawable eventImage) {
        this.eventImage = eventImage;
    }
    */
    public int getNoOfLike() {
        return noOfLike;
    }

    public void setNoOfLike(int noOfLike) {
        this.noOfLike = noOfLike;
    }

    public int getNoOfComment() {
        return noOfComment;
    }

    public void setNoOfComment(int noOfComment) {
        this.noOfComment = noOfComment;
    }

    public String getEventsource() {
        return eventsource;
    }

    public void setEventsource(String eventsource) {
        this.eventsource = eventsource;
    }

    public String getThumbnailImgName() {
        return thumbnailImgName;
    }

    public void setThumbnailImgName(String thumbnailImgName) {
        this.thumbnailImgName = thumbnailImgName;
    }

    public String getFullImgName() {
        return fullImgName;
    }

    public void setFullImgName(String fullImgName) {
        this.fullImgName = fullImgName;
    }

    public Event(int eventId, String eventHeading, int eventType, String eventDate,
                 String eventPlace, String eventDiscription, String thumbnailImgName, String fullImgName,
                 String eventaddon, int noOfComment, int noOfLike,String eventsource)

 {
   this.eventId=eventId;
   this.eventHeading=eventHeading;
   this.eventType=eventType;
   this.eventDate=eventDate;
   this.eventPlace=eventPlace;
   this.eventDiscription=eventDiscription;
     Log.d("TAG", "i am in event place in constructor " +eventPlace);


   // this.eventImage=eventImage;
   this.eventaddon=eventaddon;
   this.noOfComment=noOfComment;
   this.noOfLike=noOfLike;
     this.eventsource=eventsource;
     this.thumbnailImgName = thumbnailImgName;
     this.fullImgName = fullImgName;
 }


}