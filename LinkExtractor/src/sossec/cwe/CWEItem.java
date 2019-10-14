package sossec.cwe;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

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

	public CWEItem(String id) {
		this.id = id;
	}

	public CWEItem(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		return "<html> CWE <font color=\"green\">" + this.id + "</font> " + this.name + "</html>";
	}

	public ArrayList<CAPECItem> getDirectCAPECList() {
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
				indirectCAPEC.add(capecItem);
			}
		}

		return indirectCAPEC;
	}
}
