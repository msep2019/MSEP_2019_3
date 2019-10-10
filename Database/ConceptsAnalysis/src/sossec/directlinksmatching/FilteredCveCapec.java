package sossec.directlinksmatching;
import java.util.HashSet;

class FilteredCveCapec {
	
	static private HashSet<String> setCve = new HashSet<String>();
	static private HashSet<String> setCapec = new HashSet<String>();
	
	public FilteredCveCapec() {
		super();
	}
	
	public HashSet<String> getFilteredcve(){
		//System.out.println("└─Filtered CVE: "+setCve.toString());
		return setCve;
	}
	
	public HashSet<String> getFilteredcapec(){
		//System.out.println("└─Filtered CAPEC: "+setCapec.toString());
		return setCapec;
	}
	
	public void setFilteredcve(HashSet<String> setCve) {
		FilteredCveCapec.setCve = setCve;
	}
	
	public void setFilteredcapec(HashSet<String> setCapec) {
		FilteredCveCapec.setCapec = setCapec;
	}
}
