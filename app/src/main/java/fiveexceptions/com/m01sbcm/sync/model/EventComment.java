package fiveexceptions.com.m01sbcm.sync.model;

/**
 * Created by Vijesh Jat on 28/4/16.
 */
public class EventComment {
    private int ServerID;
    private int intEventNewsID;
    private String strCommentText;
    private int intUserID;
    private String strLastUpdate;
    private String strUserName;

    public String getStrUserName() {
        return strUserName;
    }

    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    public int getServerID() {
        return ServerID;
    }

    public void setServerID(int serverID) {
        ServerID = serverID;
    }

    public int getIntEventNewsID() {
        return intEventNewsID;
    }

    public void setIntEventNewsID(int intEventNewsID) {
        this.intEventNewsID = intEventNewsID;
    }

    public String getStrCommentText() {
        return strCommentText;
    }

    public void setStrCommentText(String strCommentText) {
        this.strCommentText = strCommentText;
    }

    public int getIntUserID() {
        return intUserID;
    }

    public void setIntUserID(int intUserID) {
        this.intUserID = intUserID;
    }

    public String getStrLastUpdate() {
        return strLastUpdate;
    }

    public void setStrLastUpdate(String strLastUpdate) {
        this.strLastUpdate = strLastUpdate;
    }
}
