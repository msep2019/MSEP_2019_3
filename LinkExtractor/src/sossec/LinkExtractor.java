package sossec;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.log4j.BasicConfigurator;
import gate.util.GateException;

import sossec.cve.CVEHelper;
import sossec.cve.CVEItem;
import sossec.cwe.CWEItem;
import sossec.capec.CAPECHelper;
import sossec.capec.CAPECItem;
import sossec.keyword.Keyword;
import sossec.keywordmatching.Item;
import sossec.keywordmatching.KeywordMatching;

public class LinkExtractor {
	public static void main(String[] args) throws GateException, IOException, URISyntaxException {
		File fileCVEKeywordDef = null;
		File fileCWEKeywordDef = null;
		ArrayList<Item> indirectCWE = new ArrayList<>();
		ArrayList<Item> indirectCAPEC = new ArrayList<>();
		
		CVEHelper cveHelper = new CVEHelper();
		CAPECHelper capecHelper = new CAPECHelper();

		String cveDesc = cveHelper.getItemContent(args[0]);

		if (cveDesc.isEmpty()) {
			System.exit(1);
		}
		
		CVEItem cveItem = new CVEItem(args[0]);

		
		BasicConfigurator.configure();
		
		// Get CVE keywords
		if (!cveDesc.isEmpty()) {
			cveItem.keywords = Keyword.processDocs(cveDesc);
		}
		
		// Generate CVE keywords Gazetteer list and definition
		if (cveItem.keywords.size() > 0) {
			System.out.println("CVE Desc: " + cveDesc);
			System.out.println("CVE Keywords: " + cveItem.keywords.toString());
			fileCVEKeywordDef = Keyword.generateGazetteer("CVE", cveItem.keywords);
		}
		
		if (fileCVEKeywordDef != null) {
			System.out.println("\n===Found CWE for CVE " + cveItem.id + " : ");
			// Get weaknesses which have high matching
			indirectCWE = KeywordMatching.processDocs(CWEItem.xmlFiles, fileCVEKeywordDef, "src/gate/jape/get-CWE.jape");
			
			for (Item itemCWE : indirectCWE) {
				CWEItem cweItem = new CWEItem(itemCWE.id);
				cveItem.indirectCWE.add(cweItem);
				
				cweItem.getContent();
				
				// Get CWE keywords
				if (!cweItem.description.isEmpty()) {
					cweItem.keywords = Keyword.processDocs(cweItem.description);
				}
				
				System.out.println("cweItem.keywords: " + cweItem.keywords.toString());
				
				// Generate CWE keywords Gazetteer list and definition
				if (cweItem.keywords.size() > 0) {
					System.out.println("CWE Desc: " + cweItem.description);
					System.out.println("CwE Keywords: " + cweItem.keywords.toString());
					fileCWEKeywordDef = Keyword.generateGazetteer("CWE", cweItem.keywords);
				}
				
				if (fileCWEKeywordDef != null) {
					System.out.println("\n==Found CAPEC for CWE " + cweItem.id + " : ");
					// Get attack pattern which have high matching
					indirectCAPEC = KeywordMatching.processDocs(capecHelper.xmlFiles, fileCWEKeywordDef, "src/gate/jape/get-CAPEC.jape");
					
					// Add found CAPEC to list
					for (Item itemCAPEC : indirectCAPEC) {
						CAPECItem capecItem = new CAPECItem(itemCAPEC.id);
						cweItem.indirectCAPEC.add(capecItem);
					}
				}
		    }
		}
		System.out.println("\n========FINAL RESULT========\n");
		System.out.println("CVE: " + cveItem.id);
		for (CWEItem cweItem : cveItem.indirectCWE) {
			System.out.println("└─CWE: " + cweItem.id);
			
			for (CAPECItem capecItem : cweItem.indirectCAPEC) {
				System.out.println("  └─CAPEC: " + capecItem.id);
			}
		}
	}
}
