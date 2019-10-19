package sossec.elements;

public class SoSSecCondition extends SoSSecObject implements SoSSecElement{
	private String type;
	
	public SoSSecCondition() {
		super();
	}
	
	public SoSSecCondition(String name) {
		super();
		this.setName(name);
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
}