package sossec.cve;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;

import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import sossec.cwe.CWEItem;
import sossec.keyword.Keyword;
import sossec.keywordmatching.Item;
import sossec.keywordmatching.KeywordMatching;

public class CVEItem {
	CVEHelper cveHelper = new CVEHelper();

	public String id;
	public String description = "";
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<String> disabledKeywords = new ArrayList<>();
	public ArrayList<CWEItem> directCWE = new ArrayList<>();
	public ArrayList<CWEItem> indirectCWE = new ArrayList<>();
	public int maxMatching = -1;
	public int minMatching = -1;
	public boolean isChangedKeywords = true;
	public HashMap<String, String> metadata = new HashMap<String, String>();

	public CVEItem() {

	}

	public CVEItem(String id) {
		this.id = id;
	}
	
	public String toString() {
		return "<html><font color=\"green\">" + this.id + "</font></html>";
	}
	
	public ArrayList<CWEItem> getDirectCWEList() {
		
		InputStream input;
		
		//define database location in a string
		String[] argsDb = {"xml/Research(1000).xml","xml/Architectural(1008).xml","xml/Development(699).xml"}; 
		//receive cveCweName
		HashMap<ArrayList<String>,String> cveCweName = new HashMap<ArrayList<String>,String>();
		
		
		//Go through 3 CWE and to capture CWE idS connecting CVE and CAPEC id groups
		for(int i=0; i < argsDb.length; i++){
			System.out.println("<--Results of matching direct CWE info in "+argsDb[i]+" dataset-->");
		    try {
				//Initialize SearchDirectlinks
				SearchDirectlinks dl = new SearchDirectlinks();;				
				input = new FileInputStream(argsDb[i]);
				cveCweName = dl.directLinks(input);
				
				ArrayList<String> cveIdList;
				
				for(Map.Entry<ArrayList<String>,String> entryDirectcwe : cveCweName.entrySet()) {					
					cveIdList = new ArrayList<String>();
					cveIdList = entryDirectcwe.getKey();
					for(String cveId : cveIdList){
						if(cveId.equals(id)){
							System.out.println(entryDirectcwe.getValue().split("--")[0]+"-->"+entryDirectcwe.getValue().split("--")[1]);
							boolean isExist = false;
							CWEItem cweItem = new CWEItem(entryDirectcwe.getValue().split("--")[0],entryDirectcwe.getValue().split("--")[1]);
							for(CWEItem foundItem : directCWE){
								if(foundItem.id.equals(cweItem.id)){
									isExist = true;
								}								
							}
							if(!isExist){
								directCWE.add(cweItem);
							}
							
						}
					}

				}
			    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}			
		return directCWE;
	}

	public ArrayList<CWEItem> getIndirectCWEList() {
		File fileCVEKeywordDef = null;
		ArrayList<Item> listCWE = new ArrayList<>();

		BasicConfigurator.configure();

		this.indirectCWE = new ArrayList<>();
		
		if (keywords.size() <= 0 && disabledKeywords.size() <= 0) {
			String cveDesc = cveHelper.getItemContent(this.id);
	
			if (cveDesc.isEmpty()) {
				return indirectCWE;
			} else {
				System.out.println("CVE Desc: " + cveDesc);
				try {
					keywords = Keyword.processDocs(cveDesc);
					this.isChangedKeywords = true;
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
		
		// Generate CVE keywords Gazetteer list and definition
		if (keywords.size() > 0) {
			//System.out.println("CVE Desc: " + cveDesc);
			System.out.println("CVE Keywords: " + keywords.toString());
			fileCVEKeywordDef = Keyword.generateGazetteer("CVE", keywords);
		}
		

		if (fileCVEKeywordDef != null) {
			System.out.println("\n==Found CWE for CVE " + this.id + " : ");
			// Get weaknesses which have high matching
			try {
				listCWE = KeywordMatching.processDocs(CWEItem.xmlFiles, fileCVEKeywordDef, "src/gate/jape/get-CWE.jape");
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
			
			for (Item item : listCWE) {
				CWEItem cweItem = new CWEItem(item.id, item.name);
				cweItem.matching = item.matchingCount;
				this.indirectCWE.add(cweItem);
			}
		}
		
		return indirectCWE;
	}
}