package fiveexceptions.com.m01sbcm.sync.model;

public class Like {
	private int intID;
	private int intNewsEventID;
	private int intUserID;
	private int intDeviceID;
	private String strLastUpdate;

	public Like(int intID, int intNewsEventID, int intUserID, String strLastUpdate) {
		super();
		this.intID = intID;
		this.intNewsEventID = intNewsEventID;
		this.intUserID = intUserID;
		this.strLastUpdate = strLastUpdate;
	}

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

	public String getStrLastUpdate() {
		return strLastUpdate;
	}

	public void setStrLastUpdate(String strLastUpdate) {
		this.strLastUpdate = strLastUpdate;
	}

	public Like() {
		// TODO Auto-generated constructor stub
	}

}
