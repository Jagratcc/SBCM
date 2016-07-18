package fiveexceptions.com.m01sbcm.sync.model;

public class State{
	
	private int id;
	private String stateName;
	private String strLastUpdate;
	
	public State(int id, String stateName, String strLastUpdate) {
		super();
		this.id = id;
		this.stateName = stateName;
		this.strLastUpdate = strLastUpdate;
	}
	public String getStrLastUpdate() {
		return strLastUpdate;
	}
	public void setStrLastUpdate(String strLastUpdate) {
		this.strLastUpdate = strLastUpdate;
	}
	public State(){
	}
	public State(int id, String name){
		this.id=id;
		this.stateName = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String toString(){
		return "stateId- "+id +",stateName- "+stateName +", strLastUpdate -"+strLastUpdate;
	}


}