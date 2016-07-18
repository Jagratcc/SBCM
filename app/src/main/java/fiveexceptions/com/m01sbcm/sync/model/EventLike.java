package fiveexceptions.com.m01sbcm.sync.model;

/**
 * Created by Vijesh Jat on 29/4/16.
 */
public class EventLike {
    private int intServerID;
    private int intNewsEventID;
    private int intUserID;
    private int intDeviceID;
    private String strLastUpdate;
    private String strUserName;

    public String getStrUserName() {
        return strUserName;
    }

    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    public int getIntServerID() {
        return intServerID;
    }

    public void setIntServerID(int intServerID) {
        this.intServerID = intServerID;
    }

    public int getIntNewsEventID() {
        return intNewsEventID;
    }

    public void setIntNewsEventID(int intNewsEventID) {
        this.intNewsEventID = intNewsEventID;
    }

    public int getIntUserID() {
        return intUserID;
    }

    public void setIntUserID(int intUserID) {
        this.intUserID = intUserID;
    }

    public int getIntDeviceID() {
        return intDeviceID;
    }

    public void setIntDeviceID(int intDeviceID) {
        this.intDeviceID = intDeviceID;
    }

    public String getStrLastUpdate() {
        return strLastUpdate;
    }

    public void setStrLastUpdate(String strLastUpdate) {
        this.strLastUpdate = strLastUpdate;
    }
}
