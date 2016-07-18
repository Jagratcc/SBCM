package fiveexceptions.com.m01sbcm.sync.model;

public class Cast{
	
	private int id;
	private String castName;
	
	public Cast(int id, String name){
		this.id=id;
		this.castName = name;
	}
	public Cast(){

	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCastName() {
		return castName;
	}

	public void setCastName(String castName) {
		this.castName = castName;
	}

	public String toString(){
		return "costId- "+id +",castName- "+castName;
	}
	
	
}