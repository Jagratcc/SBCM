package fiveexceptions.com.m01sbcm.sync.model;

public class NewsEventSource {
	private int id;
	private String strTitle;
	private String strLastUpdate;

	public int getId() {
		return id;
	}

	public NewsEventSource(int id, String strTitle, String strLastUpdate) {
		super();
		this.id = id;
		this.strTitle = strTitle;
		this.strLastUpdate = strLastUpdate;
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

	public String getStrLastUpdate() {
		return strLastUpdate;
	}

	public void setStrLastUpdate(String strLastUpdate) {
		this.strLastUpdate = strLastUpdate;
	}

	public NewsEventSource() {
		// TODO Auto-generated constructor stub
	}

}
