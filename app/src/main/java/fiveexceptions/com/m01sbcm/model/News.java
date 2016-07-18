package fiveexceptions.com.m01sbcm.model;

/**
 * Created by amit on 9/4/16.
 */
public class News {
    int newsId;
    String newsHeading;
    int newsType;
    String newsPlace;
    String newsDiscription;
    String newsDate;
    String  newsaddon;
    // Drawable newsImage;
    int noOfLike;
    int noOfComment;
    String newssource;
    String thumbnailImgName;
    String fullImgName;

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    public String getNewsPlace() {
        return newsPlace;
    }

    public void setNewsPlace(String newsPlace) {
        this.newsPlace = newsPlace;
    }

    public String getNewsDiscription() {
        return newsDiscription;
    }

    public void setNewsDiscription(String newsDiscription) {
        this.newsDiscription = newsDiscription;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsaddon() {
        return newsaddon;
    }

    public void setNewsaddon(String newsaddon) {
        this.newsaddon = newsaddon;
    }

    /*
    public Drawable getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(Drawable newsImage) {
        this.newsImage = newsImage;
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

    public String getNewssource() {
        return newssource;
    }

    public void setNewssource(String newssource) {
        this.newssource = newssource;
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

    public News(int newsId, String newsHeading, int newsType, String newsDate,
                String newsPlace, String newsDiscription, String thumbnailImgName, String fullImgName,
                String newsaddon, int noOfComment, int noOfLike, String newssource)

    {
        this.newsId=newsId;
        this.newsHeading=newsHeading;
        this.newsType=newsType;
        this.newsDate=newsDate;
        this.newsPlace=newsPlace;
        this.newsDiscription=newsDiscription;
        // this.newsImage=newsImage;
        this.newsaddon=newsaddon;
        this.noOfComment=noOfComment;
        this.noOfLike=noOfLike;
        this.newssource=newssource;
        this.thumbnailImgName = thumbnailImgName;
        this.fullImgName = fullImgName;


    }




}
