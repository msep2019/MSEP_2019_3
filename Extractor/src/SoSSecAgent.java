import java.util.ArrayList;

class SoSSecAgent extends SoSSecObject {
	private ArrayList<SoSSecBehaviour> behaviour;
	
	public SoSSecAgent() {
		super();
	}
	
	public SoSSecAgent(String name) {
		super();
		this.setName(name);
	}

	public ArrayList<SoSSecBehaviour> getBehaviours() {
		return this.behaviour;
	}

	public SoSSecBehaviour getBehaviour(String name) {

		for (SoSSecBehaviour el : this.behaviour) {
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