package fiveexceptions.com.m01sbcm.sync.model;

public class Comment {
    private int intID;
    private int intEventNewsID;
    private String strCommentText;
    private int intUserID;
    private String strLastUpdate;
    private int intDeviceID;

    public int getIntDeviceID() {
        return intDeviceID;
    }

    public void setIntDeviceID(int intDeviceID) {
        this.intDeviceID = intDeviceID;
    }

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
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

    public Comment() {
        // TODO Auto-generated constructor stub
    }

    public String toString() {
        return "intDeviceID == " + intDeviceID + " , " +
                "intID == " + intID + " , " +
                "intEventNewsID == " + intEventNewsID + " , " +
                "strCommentText == " + strCommentText + " , " +
                "intUserID == " + intUserID + " , "


                ;
    }
}
