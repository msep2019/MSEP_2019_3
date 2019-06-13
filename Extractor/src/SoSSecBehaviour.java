import java.util.ArrayList;

class SoSSecBehaviour {
	private String name;
	private ArrayList<SoSSecVulnerability> vulnerability;
	
	public SoSSecBehaviour() {
		super();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<SoSSecVulnerability> getVulnerabilities() {
		return this.vulnerability;
	}
	
	public SoSSecVulnerability getBehaviour(String name) {
		
		for (SoSSecVulnerability el : this.vulnerability){
	        if (el.getName().equals(name)) {
	        	return el;
	        }
	           
	    }
		return null;
	}
	
	public void setVulnerability(ArrayList<SoSSecVulnerability> vulnerability) {
		this.vulnerability = vulnerability;
	}
	
	public void setVulnerability(SoSSecVulnerability vulnerability) {
			  
		if (!this.vulnerability.contains(vulnerability)) { 
			this.vulnerability.add(vulnerability); 
		} 
       
	}
}