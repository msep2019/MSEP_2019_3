import java.util.ArrayList;
import java.util.HashMap;

public class FilteredAtkCapec {
	static private HashMap<String,ArrayList<String>> idPatternstore = new HashMap<String,ArrayList<String>>();
	
	public FilteredAtkCapec() {
		super();
	}
	
	public HashMap<String,ArrayList<String>> getIdPattern(){
		return idPatternstore;
	}
	
	public void setIdPattern(HashMap<String,ArrayList<String>> idPatternstore){
		FilteredAtkCapec.idPatternstore = idPatternstore;
	}
}
