package sossec.directlinksmatching;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;


public class LinkVulCapec {
	public static void main(String[] args) {
		InputStream input;
		String cveid = null;
    	
		//storage of total cve and capec Id which have direct links
		List<String> totalCve = new ArrayList<>();
		List<String> totalCapec = new ArrayList<>();
		
		//filter storage of cve and capec id
		List<String> rcveId = new ArrayList<>();
		List<String> rcapecId = new ArrayList<>();
		HashSet<String> setCve = new HashSet<>();
		HashSet<String> setCapec = new HashSet<>();

		System.out.println("Please enter the CVE ID to begin: ");
		
    	try {
    		Scanner scan = new Scanner(System.in);
    		if(scan.hasNext()) {
    			cveid = scan.next();
    		}
    		scan.close();
    		
    		SearchDirectlinks dl;
    		FilteredCveCapec setCvecapec;

    		HashSet<String> dCve;
    		HashSet<String> dCapec;
    		
			//Go through 3 CWE and 2 CAPEC datasets to capture cve and capec ids which have direct links
    		for(int i=0; i < args.length; i++){
    			System.out.println("<--Results of matching "+args[i]+" dataset-->");
			    input = new FileInputStream(args[i]);
				dl = new SearchDirectlinks();
				dl.directLinks(cveid,input);
				setCvecapec = new FilteredCveCapec();			
				dCve = new HashSet<>();
				dCapec = new HashSet<>();			
				dCve = setCvecapec.getFilteredcve();
				dCapec = setCvecapec.getFilteredcapec();
				for(String id : dCve) {
					totalCve.add(id);
				}
				for(String id : dCapec) {
					totalCapec.add(id);
				}
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
        	
        	/*System.out.println("<--Print the results of coverage calculation-->");
        	System.out.println("└─Final filtered CAPEC: " + setCapec.toString());
        	System.out.println("	└─Coverage of direct links in CVE: "+ setCve.size()+ "/153347");
        	System.out.println("	└─Coverage of direct links in CAPEC: "+ setCapec.size()+ "/519");
        	System.out.println("<--End of print-->");*/
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
