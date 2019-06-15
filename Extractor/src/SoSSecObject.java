import org.eclipse.emf.ecore.util.EcoreUtil;

public class SoSSecObject {
	private String name;
	private String uuid;
	
	public SoSSecObject() {
		super();
		init();
	}
	
	public SoSSecObject(String name) {
		super();
		this.name = name;
		init();
	}
	
	private void init() {
		this.uuid = EcoreUtil.generateUUID();
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUUID() {
		return this.uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
}
