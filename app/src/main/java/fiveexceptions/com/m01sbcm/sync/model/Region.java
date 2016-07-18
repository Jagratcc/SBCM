package fiveexceptions.com.m01sbcm.sync.model;

public class Region {

	private int id;
	private String regionName;
	private String strLastUpdate;
	
	public Region(int id, String regionName, String strLastUpdate) {
		super();
		this.id = id;
		this.regionName = regionName;
		this.strLastUpdate = strLastUpdate;
	}

	public Region() {
		super();

	}
	public String getStrLastUpdate() {
		return strLastUpdate;
	}

	public void setStrLastUpdate(String strLastUpdate) {
		this.strLastUpdate = strLastUpdate;
	}

	public Region(int id, String name){
		this.id=id;
		this.regionName = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String toString(){
		return "regionId- "+id +",regionName- "+regionName+", strLastUpdate- "+strLastUpdate;
	}





}
