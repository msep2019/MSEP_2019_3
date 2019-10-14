package sossec.cve;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

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
		return directCWE;
	}

	public ArrayList<CWEItem> getIndirectCWEList() {
		File fileCVEKeywordDef = null;
		String cveDesc = cveHelper.getItemContent(this.id);
		ArrayList<Item> listCWE = new ArrayList<>();

		if (cveDesc.isEmpty()) {
			return indirectCWE;
		}

		BasicConfigurator.configure();

		// Get CVE keywords
		if (!cveDesc.isEmpty()) {
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
		
		// Generate CVE keywords Gazetteer list and definition
		if (keywords.size() > 0) {
			System.out.println("CVE Desc: " + cveDesc);
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