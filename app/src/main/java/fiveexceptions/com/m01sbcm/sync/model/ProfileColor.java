package fiveexceptions.com.m01sbcm.sync.model;

public class ProfileColor {
	
	private int id;
	private String colorName;
	
	public ProfileColor(int id, String name){
		this.id=id;
		this.colorName = name;
	}
	public ProfileColor(){

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	
}