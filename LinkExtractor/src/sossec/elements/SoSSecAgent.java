package sossec.elements;
import java.util.HashMap;

public class SoSSecAgent extends SoSSecObject implements SoSSecElement{
	private HashMap<String, SoSSecBehaviour> behaviour = new HashMap<String, SoSSecBehaviour>();
	
	public SoSSecAgent() {
		super();
	}
	
	public SoSSecAgent(String name) {
		super();
		this.setName(name);
	}

	public HashMap<String, SoSSecBehaviour> getBehaviours() {
		return this.behaviour;
	}

	public SoSSecBehaviour getBehaviour(String name) {
		return this.behaviour.get(name);
	}

	public void addBehaviour(HashMap<String, SoSSecBehaviour> behaviour) {
		this.behaviour = behaviour;
	}

	public void addBehaviour(SoSSecBehaviour behaviour) {

		if (this.behaviour == null || !this.behaviour.containsKey(behaviour.getName())) {
			this.behaviour.put(behaviour.getName(), behaviour);
		}

	}
}