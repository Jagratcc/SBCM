package fiveexceptions.com.m01sbcm.sync.model;

public class EducationStatus {
	
	private int id;
	private String status;
	
	public EducationStatus(int id, String name){
		this.id=id;
		this.status = name;
	}
	public EducationStatus(){

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString(){
		return "educationId- "+id +",educationstatus- "+status;
	}
	
}
