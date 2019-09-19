import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;


public class VulandCapec {
	public static void main(String[] args) {
		InputStream input1;
		InputStream input2;
		InputStream input3;
		String cveid = null;
    	
		//storage of total cve and capec Id which have direct links
		List<String> totalCve = new ArrayList<>();
		List<String> totalCapec = new ArrayList<>();
		
		//filter storage of cve and capec id
		List<String> rcveId = new ArrayList<>();
		List<String> rcapecId = new ArrayList<>();
		HashSet<String> setCve = new HashSet<>();
		HashSet<String> setCapec = new HashSet<>();
		
		System.out.println("Please enter the CVE id to begin: ");
		
    	try {
    		Scanner scan = new Scanner(System.in);
    		if(scan.hasNext()) {
    			cveid = scan.next();
    		}
    		scan.close();
    		
    		SearchDirectlinks dl;
    		FilteredCveCapec setCvecapec1;
    		FilteredCveCapec setCvecapec2;
    		FilteredCveCapec setCvecapec3;
    		HashSet<String> dCve;
    		HashSet<String> dCapec;
    		
			//capture cve and capec ids which have direct links
    		input1 = new FileInputStream("xml/Development(699).xml");
			dl = new SearchDirectlinks();
			dl.directLinks(cveid,input1);
			setCvecapec1 = new FilteredCveCapec();			
			dCve = new HashSet<>();
			dCapec = new HashSet<>();			
			dCve = setCvecapec1.getFilteredcve();
			dCapec = setCvecapec1.getFilteredcapec();
			for(String id : dCve) {
				totalCve.add(id);
			}
			for(String id : dCapec) {
				totalCapec.add(id);
			}
			
			//capture cve and capec ids which have direct links
			input2 = new FileInputStream("xml/Research(1000).xml");
			dl = new SearchDirectlinks();
			dl.directLinks(cveid,input2);
			setCvecapec2 = new FilteredCveCapec();
			dCve = new HashSet<>();
			dCapec = new HashSet<>();			
			dCve = setCvecapec2.getFilteredcve();
			dCapec = setCvecapec2.getFilteredcapec();
			for(String id : dCve) {
				totalCve.add(id);
			}
			for(String id : dCapec) {
				totalCapec.add(id);
			}

			//capture cve and capec ids which have direct links
			input3 = new FileInputStream("xml/Architectural(1008).xml");
			dl = new SearchDirectlinks();
			dl.directLinks(cveid,input3);
			setCvecapec3 = new FilteredCveCapec();
			dCve = new HashSet<>();
			dCapec = new HashSet<>();			
			dCve = setCvecapec3.getFilteredcve();
			dCapec = setCvecapec3.getFilteredcapec();
			for(String id : dCve) {
				totalCve.add(id);
			}
			for(String id : dCapec) {
				totalCapec.add(id);
			}
			
			//filter the total cve and capec ids
        	for(String id : totalCve) {
        		boolean add = setCve.add(id);
        		if(!add) {
        			rcveId.add(id);
        		}
        	}
        	for(String id : totalCapec) {
        		boolean add = setCapec.add(id);
        		if(!add) {
        			rcapecId.add(id);
        		}
        	}
        	
        	System.out.println("<--Print final results-->");
        	System.out.println("└─Final filtered CAPEC: " + setCapec.toString());
        	System.out.println("	└─Coverage of direct links in CVE: "+ setCve.size()+ "/153347");
        	System.out.println("	└─Coverage of direct links in CAPEC: "+ setCapec.size()+ "/577");
        	System.out.println("<--End of print-->");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
