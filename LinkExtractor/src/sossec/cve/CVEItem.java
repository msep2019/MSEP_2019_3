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
import sossec.cwe.CWEHelper;
import sossec.cwe.CWEItem;
import sossec.keyword.Keyword;
import sossec.keywordmatching.Item;
import sossec.keywordmatching.KeywordMatching;

public class CVEItem {
	CVEHelper cveHelper = new CVEHelper();
	CWEHelper cweHelper = new CWEHelper();

	public String id;
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<String> disabledKeywords = new ArrayList<>();
	public ArrayList<CWEItem> directCWE = new ArrayList<>();
	public ArrayList<CWEItem> indirectCWE = new ArrayList<>();

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
		HashMap<String,String> cweIdDescription = new HashMap<String,String>();
				
		//Go through 3 CWE and to capture CWE idS connecting CVE and CAPEC id groups
		for(int i=0; i < argsDb.length; i++){
			System.out.println("<--Results of matching "+argsDb[i]+" dataset-->");
		    try {
				//Initialize SearchDirectlinks
				SearchDirectlinks dl;				
				input = new FileInputStream(argsDb[i]);
				dl = new SearchDirectlinks();
				cweIdDescription = dl.directLinks(input);
				
				for(Map.Entry<String,String> entryDirectcwe : cweIdDescription.entrySet()) {					
					CWEItem cweItem = new CWEItem(entryDirectcwe.getKey(),entryDirectcwe.getValue());
					directCWE.add(cweItem);
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
			}
			
			// Get CVE keywords
			if (!cveDesc.isEmpty()) {
				System.out.println("CVE Desc: " + cveDesc);
				try {
					keywords = Keyword.processDocs(cveDesc);
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
				listCWE = KeywordMatching.processDocs(cweHelper.xmlFiles, fileCVEKeywordDef, "src/gate/jape/get-CWE.jape");
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
				this.indirectCWE.add(cweItem);
			}
		}
		
		return indirectCWE;
	}
}