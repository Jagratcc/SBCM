package fiveexceptions.com.m01sbcm.sync.model;

public class ProfessionField 
{
	private int intID;
	private String strProfessionField;
	private String strLastUpdate;
	private int intProfessionID;

	public ProfessionField(int intID, String strProfessionField, String strLastUpdate) 
	{
		super();
		this.intID = intID;
		this.strProfessionField = strProfessionField;
		this.strLastUpdate = strLastUpdate;
	}



	public int getIntProfessionID() {
		return intProfessionID;
	}

	public void setIntProfessionID(int intProfessionID) {
		this.intProfessionID = intProfessionID;
	}

	public int getIntID()
	{
		return intID;
	}

	public void setIntID(int intID) 
	{
		this.intID = intID;
	}

	public String getStrProfessionField() 
	{
		return strProfessionField;
	}

	public void setStrProfessionField(String strProfessionField) 
	{
		this.strProfessionField = strProfessionField;
	}

	public String getStrLastUpdate() 
	{
		return strLastUpdate;
	}

	public void setStrLastUpdate(String strLastUpdate) 
	{
		this.strLastUpdate = strLastUpdate;
	}

	public ProfessionField() 
	{
		// TODO Auto-generated constructor stub
	}

}
