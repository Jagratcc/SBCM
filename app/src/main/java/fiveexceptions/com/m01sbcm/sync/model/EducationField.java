package fiveexceptions.com.m01sbcm.sync.model;

public class EducationField {
	
	private int id;
	private String education;
	
	public EducationField(int id, String name){
		this.id=id;
		this.education = name;
	}
	public EducationField(){

	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	
	
}
