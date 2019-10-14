package sossec;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.log4j.BasicConfigurator;
import gate.util.GateException;

import sossec.cve.CVEHelper;
import sossec.cve.CVEItem;
import sossec.cwe.CWEHelper;
import sossec.cwe.CWEItem;
import sossec.capec.CAPECHelper;
import sossec.capec.CAPECHelper2;
import sossec.capec.CAPECItem;
import sossec.capec.CAPECMitigation;
import sossec.keyword.Keyword;
import sossec.keywordmatching.Item;
import sossec.keywordmatching.KeywordMatching;

public class LinkExtractor {
	public static void main(String[] args) throws GateException, IOException, URISyntaxException {
		File fileCVEKeywordDef = null;
		File fileCWEKeywordDef = null;
		ArrayList<Item> listCWE = new ArrayList<>();
		ArrayList<Item> listCAPEC = new ArrayList<>();
		//mitigation
		ArrayList<Item> listCAPEC2 = new ArrayList<>();
		
		CVEHelper cveHelper = new CVEHelper();
		CWEHelper cweHelper = new CWEHelper();
		CAPECHelper capecHelper = new CAPECHelper();
		//mitigation
		CAPECHelper2 capecHelper2 = new CAPECHelper2();

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
			System.out.println("\n==Found CWE for CVE " + cveItem.id + " : ");
			// Get weaknesses which have high matching
			listCWE = KeywordMatching.processDocs(cweHelper.xmlFiles, fileCVEKeywordDef, "src/gate/jape/get-CWE.jape");
			
			for (Item itemCWE : listCWE) {
				CWEItem cweItem = new CWEItem(itemCWE.id);
				cveItem.listCWE.add(cweItem);
				
				String cweDesc = cweHelper.getItemContent(cweItem.id);
				
				// Get CWE keywords
				if (!cweDesc.isEmpty()) {
					cweItem.keywords = Keyword.processDocs(cweDesc);
				}
				
				System.out.println("cweItem.keywords: " + cweItem.keywords.toString());
				
				// Generate CWE keywords Gazetteer list and definition
				if (cweItem.keywords.size() > 0) {
					System.out.println("CWE Desc: " + cweDesc);
					System.out.println("CwE Keywords: " + cweItem.keywords.toString());
					fileCWEKeywordDef = Keyword.generateGazetteer("CWE", cweItem.keywords);
				}
				
				if (fileCWEKeywordDef != null) {
					System.out.println("\n==Found CAPEC for CWE " + cweItem.id + " : ");
					// Get attack pattern which have high matching
					listCAPEC = KeywordMatching.processDocs(capecHelper.xmlFiles, fileCWEKeywordDef, "src/gate/jape/get-CAPEC.jape");
					
					// Add found CAPEC to list
					for (Item itemCAPEC : listCAPEC) {
						CAPECItem capecItem = new CAPECItem(itemCAPEC.id);
						cweItem.listCAPEC.add(capecItem);
						//mitigation
						//CAPECMitigation capecMitigation = new CAPECMitigation(itemCAPEC.id);
						//cweItem.listCAPEC.add(capecMitigation);
					}
				}
		    }
		}
		System.out.println("\n========FINAL RESULT========\n");
		System.out.println("CVE: " + cveItem.id);
		for (CWEItem cweItem : cveItem.listCWE) {
			System.out.println("©¸©¤CWE: " + cweItem.id);
			
			for (CAPECItem capecItem : cweItem.listCAPEC) {
				System.out.println("  ©¸©¤CAPEC: " + capecItem.id);
			}
			for (CAPECMitigation capecMitigation : cweItem.listCAPEC) {
				System.out.println("  ©¸©¤CAPEC: " + capecMitigation.id);
			}
		}
	}
}
