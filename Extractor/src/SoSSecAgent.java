import java.util.ArrayList;

class SoSSecAgent {
	private String name;
	private ArrayList<SoSSecBehaviour> behaviour;
	public SoSSecAgent() {
		super();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<SoSSecBehaviour> getBehaviours() {
		return this.behaviour;
	}
	
	public SoSSecBehaviour getBehaviour(String name) {
		
		for (SoSSecBehaviour el : this.behaviour){
	        if (el.getName().equals(name)) {
	        	return el;
	        }
	           
	    }
		return null;
	}
	
	public void setBehaviour(ArrayList<SoSSecBehaviour> behaviour) {
		this.behaviour = behaviour;
	}
	
	public void setBehaviour(SoSSecBehaviour behaviour) {
			  
		if (!this.behaviour.contains(behaviour)) { 
			this.behaviour.add(behaviour); 
		} 
       
	}
}