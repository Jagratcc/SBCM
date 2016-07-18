package fiveexceptions.com.m01sbcm.sync.model;

/**
 * Created by Vijesh Jat on 30/4/16.
 */
public class News {
    private int id;
    private String strTitle;

    private String strDescription;
    private int intEventCityID;
    private int intEventStateID;
    private int intEventRegionID;
    private String strDateAddedOn;
    private String strEventDate;
    private int intLikeCount;
    private int intCommentCount;
    private String strEffectiveDate;
    private String strExpirationDate;
    private int intAuthCode;
    private int intPaymentRefNo;
    private String strLastUpdate;
    private int intSourceID;
    private String strThumbnailURL;
    private String strPhotoURL;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrEventDate() {
        return strEventDate;
    }

    public void setStrEventDate(String strEventDate) {
        this.strEventDate = strEventDate;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrPhotoURL() {
        return strPhotoURL;
    }

    public void setStrPhotoURL(String strPhotoURL) {
        this.strPhotoURL = strPhotoURL;
    }

    public int getIntEventCityID() {
        return intEventCityID;
    }

    public void setIntEventCityID(int intEventCityID) {
        this.intEventCityID = intEventCityID;
    }

    public int getIntEventStateID() {
        return intEventStateID;
    }

    public void setIntEventStateID(int intEventStateID) {
        this.intEventStateID = intEventStateID;
    }


    public int getIntEventRegionID() {
        return intEventRegionID;
    }

    public void setIntEventRegionID(int intEventRegionID) {
        this.intEventRegionID = intEventRegionID;
    }

    public String getStrDateAddedOn() {
        return strDateAddedOn;
    }

    public void setStrDateAddedOn(String strDateAddedOn) {
        this.strDateAddedOn = strDateAddedOn;
    }

    public int getIntLikeCount() {
        return intLikeCount;
    }

    public void setIntLikeCount(int intLikeCount) {
        this.intLikeCount = intLikeCount;
    }

    public int getIntCommentCount() {
        return intCommentCount;
    }

    public void setIntCommentCount(int intCommentCount) {
        this.intCommentCount = intCommentCount;
    }

    public String getStrEffectiveDate() {
        return strEffectiveDate;
    }

    public void setStrEffectiveDate(String strEffectiveDate) {
        this.strEffectiveDate = strEffectiveDate;
    }

    public String getStrExpirationDate() {
        return strExpirationDate;
    }

    public void setStrExpirationDate(String strExpirationDate) {
        this.strExpirationDate = strExpirationDate;
    }

    public int getIntAuthCode() {
        return intAuthCode;
    }

    public void setIntAuthCode(int intAuthCode) {
        this.intAuthCode = intAuthCode;
    }

    public int getIntPaymentRefNo() {
        return intPaymentRefNo;
    }

    public void setIntPaymentRefNo(int intPaymentRefNo) {
        this.intPaymentRefNo = intPaymentRefNo;
    }

    public String getStrLastUpdate() {
        return strLastUpdate;
    }

    public void setStrLastUpdate(String strLastUpdate) {
        this.strLastUpdate = strLastUpdate;
    }

    public int getIntSourceID() {
        return intSourceID;
    }

    public void setIntSourceID(int intSourceID) {
        this.intSourceID = intSourceID;
    }

    public String getStrThumbnailURL() {
        return strThumbnailURL;
    }

    public void setStrThumbnailURL(String strThumbnailURL) {
        this.strThumbnailURL = strThumbnailURL;
    }
}
