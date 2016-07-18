package fiveexceptions.com.m01sbcm.sync.model;

public class MotherTongue {
	
	private int id;
	private String language;
	
	public MotherTongue(int id, String name){
		this.id=id;
		this.language = name;
	}
	public MotherTongue(){

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	
}
