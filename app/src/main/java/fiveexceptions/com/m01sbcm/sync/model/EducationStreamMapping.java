package fiveexceptions.com.m01sbcm.sync.model;

/**
 * Created by Vijesh Jat on 30/4/16.
 */
public class EducationStreamMapping {
    //    "intID": 1,"intEducationID": 2, "intEducationStreamID": 1,
    //       "strLastUpdate": "2016-04-30 17:16:13.0"
    private int intID;
    private int intEducationID;
    private int intEducationStreamID;
    private String strLastUpdate;

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

    public int getIntEducationID() {
        return intEducationID;
    }

    public void setIntEducationID(int intEducationID) {
        this.intEducationID = intEducationID;
    }

    public int getIntEducationStreamID() {
        return intEducationStreamID;
    }

    public void setIntEducationStreamID(int intEducationStreamID) {
        this.intEducationStreamID = intEducationStreamID;
    }

    public String getStrLastUpdate() {
        return strLastUpdate;
    }

    public void setStrLastUpdate(String strLastUpdate) {
        this.strLastUpdate = strLastUpdate;
    }
}
