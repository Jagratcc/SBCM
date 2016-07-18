package fiveexceptions.com.m01sbcm.sync.model;

/**
 * Created by Vijesh Jat on 30/4/16.
 */
public class ProfessionStreamMapping {
    private int intID;
    private int intProfessionID;
    private int intProfessionStreamID;
    private String strLastUpdate;

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

    public int getIntProfessionID() {
        return intProfessionID;
    }

    public void setIntProfessionID(int intProfessionID) {
        this.intProfessionID = intProfessionID;
    }

    public int getIntProfessionStreamID() {
        return intProfessionStreamID;
    }

    public void setIntProfessionStreamID(int intProfessionStreamID) {
        this.intProfessionStreamID = intProfessionStreamID;
    }

    public String getStrLastUpdate() {
        return strLastUpdate;
    }

    public void setStrLastUpdate(String strLastUpdate) {
        this.strLastUpdate = strLastUpdate;
    }
}
