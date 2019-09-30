package sossec.elements;
import java.util.HashMap;

public class SoSSecBehaviour extends SoSSecObject implements SoSSecElement{
	private HashMap<String, SoSSecVulnerability> vulnerability = new HashMap<String, SoSSecVulnerability>();
	private HashMap<String, SoSSecBehaviour> receiver = new HashMap<String, SoSSecBehaviour>();
	
	public SoSSecBehaviour() {
		super();
	}
	
	public SoSSecBehaviour(String name) {
		super();
		this.setName(name);
	}

	public HashMap<String, SoSSecVulnerability> getVulnerabilities() {
		return this.vulnerability;
	}

	public SoSSecVulnerability getVulnerability(String name) {
		return this.vulnerability.get(name);
	}

	public void addVulnerability(HashMap<String, SoSSecVulnerability> vulnerability) {
		this.vulnerability = vulnerability;
	}

	public void addVulnerability(SoSSecVulnerability vulnerability) {

		if (this.vulnerability == null || !this.vulnerability.containsKey(vulnerability.getName())) {
			this.vulnerability.put(vulnerability.getName(), vulnerability);
		}

	}
	
	public HashMap<String, SoSSecBehaviour> getReceivers() {
		return this.receiver;
	}

	public SoSSecBehaviour getReceiver(String name) {
		return this.receiver.get(name);
	}

	public void addReceiver(HashMap<String, SoSSecBehaviour> behaviour) {
		this.receiver = behaviour;
	}

	public void addReceiver(SoSSecBehaviour receiver) {

		if (this.receiver == null || !this.receiver.containsKey(receiver.getName())) {
			this.receiver.put(receiver.getName(), receiver);
		}

	}
}