package fiveexceptions.com.m01sbcm.sync.model;

public class Profession {
	private int id;
	private String profName;
	
	public Profession(int id, String name){
		this.id=id;
		this.profName = name;
	}

	public Profession(){

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProfName() {
		return profName;
	}

	public void setProfName(String profName) {
		this.profName = profName;
	}
	
	
}
