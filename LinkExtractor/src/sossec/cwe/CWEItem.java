package sossec.cwe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import sossec.capec.CAPECHelper;
import sossec.capec.CAPECItem;
import sossec.keyword.Keyword;
import sossec.keywordmatching.Item;
import sossec.keywordmatching.KeywordMatching;

public class CWEItem {
	public String id;
	public String name;
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<String> disabledKeywords = new ArrayList<>();
	public ArrayList<CAPECItem> directCAPEC = new ArrayList<>();
	public ArrayList<CAPECItem> indirectCAPEC = new ArrayList<>();
	public int matching = -1;

	public CWEItem() {
		
	}
	public CWEItem(String id) {
		this.id = id;
	}

	public CWEItem(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		String result = "<html>";
		
		if (matching >= 1) {
			result += "[" + matching + "] ";
		}
		
		result += "CWE <font color=\"green\">" + this.id + "</font> " + this.name + "</html>";
		
		return result;
		
	}

	public ArrayList<CAPECItem> getDirectCAPECList() {
		
		InputStream inputCwe;
		
		//define database location in a string
		String[] argsDb = {"xml/Research(1000).xml","xml/Development(699).xml","xml/Architectural(1008).xml"};
		
		//storage of direct capec id and info
		HashMap<ArrayList<String>, ArrayList<String>> directCveCapec= new HashMap<ArrayList<String>, ArrayList<String>>();
		
		//Go through 3 CWE and 2 CAPEC datasets to capture cve and capec ids which have direct links
		for(int i=0 ; i < argsDb.length ; i++){
			System.out.println("<--Results of matching "+argsDb[i]+" CWE dataset-->");
		    try {
				
		    	//Initialize SearchDirectlinks and Helper3
		    	EstablishDirectlinks edl = new EstablishDirectlinks();;
		    	CWEHelper3 helper3 = new CWEHelper3();
		    	
				inputCwe = new FileInputStream(argsDb[i]);			
				directCveCapec = edl.directLinks(inputCwe);
				
				System.out.println(directCveCapec.size());
				ArrayList<String> directCapec;
				String result = "";
				
				for(Entry<ArrayList<String>, ArrayList<String>> entryCapeclist : directCveCapec.entrySet()) {
					directCapec = new ArrayList<String>();
					directCapec = entryCapeclist.getValue();
					
					for(String capecId : directCapec){
						result = helper3.getCapecIdContent(capecId);
						CAPECItem capecItem= new CAPECItem(capecId,result);
						directCAPEC.add(capecItem);
						//System.out.println(capecId+"--"+result);

					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return directCAPEC;
	}

	public ArrayList<CAPECItem> getIndirectCAPECList() {
		CWEHelper cweHelper = new CWEHelper();
		CAPECHelper capecHelper = new CAPECHelper();
		ArrayList<Item> listCWE = new ArrayList<>();

		File fileCWEKeywordDef = null;

		if (keywords.size() <= 0) {

			String cweDesc = cweHelper.getItemContent(id);
			System.out.println("CWE Desc: " + cweDesc);

			// Get CWE keywords
			if (!cweDesc.isEmpty()) {
				try {
					keywords = Keyword.processDocs(cweDesc);
				} catch (ResourceInstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		System.out.println("cweItem.keywords: " + keywords.toString());

		// Generate CWE keywords Gazetteer list and definition
		if (keywords.size() > 0) {
			
			System.out.println("CwE Keywords: " + keywords.toString());
			fileCWEKeywordDef = Keyword.generateGazetteer("CWE", keywords);
		}

		if (fileCWEKeywordDef != null) {
			System.out.println("\n==Found CAPEC for CWE " + id + " : ");
			// Get attack pattern which have high matching
			try {
				listCWE = KeywordMatching.processDocs(capecHelper.xmlFiles, fileCWEKeywordDef,
						"src/gate/jape/get-CAPEC.jape");
			} catch (ResourceInstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Add found CAPEC to list
			for (Item itemCAPEC : listCWE) {
				CAPECItem capecItem = new CAPECItem(itemCAPEC.id, itemCAPEC.name);
				capecItem.matching = itemCAPEC.matchingCount;
				indirectCAPEC.add(capecItem);
			}
		}

		return indirectCAPEC;
	}
}
