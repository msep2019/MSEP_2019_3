package sossec.mitigation;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;

import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import sossec.capec.CAPECItem;
import sossec.cwe.CWEItem;
import sossec.keyword.Keyword;
import sossec.keywordmatching.Item;
import sossec.keywordmatching.KeywordMatching;

public class Mitigation {
	MitigationHelper mitigationHelper = new MitigationHelper();
	
	public String description;
	public String sourceDB;
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<String> disabledKeywords = new ArrayList<>();
	public ArrayList<SecurityPattern> securityPatterns = new ArrayList<>();
	public HashMap<String, String> metadata = new HashMap<String, String>();
	
	public CWEItem cwe;
	public CAPECItem capec;
	public int maxMatching;
	public int minMatching;
	
	public boolean isChangedKeywords = true;
	
	public Mitigation() {
		
	}
	
	public Mitigation(String description) {
		this.description = description;
	}
	
	public String toString() {
		String result = "<html>";
		
		if (capec != null) {
			result += "<font color=\"orange\">[From CAPEC " + capec.id + "]</font> ";
		} else if (cwe != null) {
			result += "<font color=\"orange\">[From CWE " + cwe.id + "]</font> ";
		}
		
		result += description.trim();
		result += "</html>";
		
		return result;
	}
	
	public ArrayList<SecurityPattern> getSecurityPatterns() {
		File fileMitigationKeywordDef = null;
		ArrayList<Item> listPattern = new ArrayList<>();

		BasicConfigurator.configure();

		this.securityPatterns = new ArrayList<>();
		
		if (keywords.size() <= 0 && disabledKeywords.size() <= 0) {
			String mitigationDesc = this.description;
	
			if (mitigationDesc.isEmpty()) {
				return securityPatterns;
			} else {
				System.out.println("Mitigation Desc: " + mitigationDesc);
				try {
					keywords = Keyword.processDocs(mitigationDesc);
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
		
		// Generate Mitigation keywords Gazetteer list and definition
		if (keywords.size() > 0) {
			//System.out.println("Mitigation Desc: " + mitigationDesc);
			System.out.println("Mitigation Keywords: " + keywords.toString());
			fileMitigationKeywordDef = Keyword.generateGazetteer("Mitigation", keywords);
		}
		

		if (fileMitigationKeywordDef != null) {
			System.out.println("\n==Found Security for Mitigation: " + this.description + " : ");
			// Get weaknesses which have high matching
			try {
				listPattern = KeywordMatching.processDocs(mitigationHelper.xmlFiles, fileMitigationKeywordDef, "src/gate/jape/get-SecurityPattern.jape");
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
			
			for (Item item : listPattern) {
				SecurityPattern patternItem = new SecurityPattern(item.name);
				patternItem.matching = item.matchingCount;
				this.securityPatterns.add(patternItem);
			}
		}
		
		return securityPatterns;
	}
}
