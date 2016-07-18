package fiveexceptions.com.m01sbcm.sync.model;

public class Manglic {

	private int id;
	private String manglicType;
	
	public Manglic(int id, String type){
		this.id=id;
		this.manglicType = type;
	}
	public Manglic(){

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getManglicType() {
		return manglicType;
	}

	public void setManglicType(String manglicType) {
		this.manglicType = manglicType;
	}

	public String toString(){
		return "manglicId- "+id +",manglicType- "+manglicType;
	}
	
}
