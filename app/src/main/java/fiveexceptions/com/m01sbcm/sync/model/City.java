package fiveexceptions.com.m01sbcm.sync.model;

public class City {
	
	private int id;
	private String cityName;
	private int regionId;
	private int stateId;	
	private String strLastUpdate;
	
	public String getStrLastUpdate() {
		return strLastUpdate;
	}
	public void setStrLastUpdate(String strLastUpdate) {
		this.strLastUpdate = strLastUpdate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public String toString(){
		return "cityId- "+id +",cityName- "+cityName+
		"regionId- "+regionId +",stateId- "+stateId+
		"strLastUpdate- "+strLastUpdate ;
	}

}