package report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import sossec.capec.CAPECItem;
import sossec.cve.CVEItem;
import sossec.cwe.CWEItem;
import sossec.mitigation.Mitigation;
import sossec.mitigation.SecurityPattern;

public class Report {
	public void printCVEReport(CVEItem cve) {
		if (cve.id.isEmpty()) {
			return;
		}
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		String reportDirectory = "report";
		File directory = new File(reportDirectory);
		String fileName = cve.id + " " + sdf.format(timestamp) + ".xml";
		
		if (!directory.exists()){
	        directory.mkdir();
	    }
		
		Element elCVE = new Element("cve");
		Document doc = new Document(elCVE);
		
		elCVE.setAttribute("id", cve.id);
		
		if (cve.metadata.get("processing-time") != null) {
			elCVE.setAttribute("processing_time", cve.metadata.get("processing-time"));
		}
		
		elCVE.addContent(new Element("description").setText(cve.description));
		elCVE.addContent(new Element("keywords").setText(cve.keywords.toString()));
		elCVE.addContent(new Element("disabled_keywords").setText(cve.disabledKeywords.toString()));
		
		if (cve.directCWE.size() > 0) {
			Element elDirectCWEList = new Element("direct_cwe");
			elCVE.addContent(elDirectCWEList);
			printCWE(elDirectCWEList, cve.directCWE);
		}
		
		if (cve.indirectCWE.size() > 0) {
			Element elIndirectCWEList = new Element("indirect_cwe");
			elCVE.addContent(elIndirectCWEList);
			printCWE(elIndirectCWEList, cve.indirectCWE);
		}
        
        
		
		File file = new File(reportDirectory + "/" + fileName);
	    try{
	        XMLOutputter xmlOutput = new XMLOutputter();
			
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter(file.getAbsoluteFile()));
			
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.exit(-1);
	    }
		
		return;
	}	
	
	private void printCWE(Element el, ArrayList<CWEItem> listCWE) {
		int cweCount = 0;
		
		
		for (CWEItem cwe : listCWE) {
			// Print CWE has CAPEC first
			if (cwe.directCAPEC.size() + cwe.indirectCAPEC.size() > 0) {
				
				
				cweCount++;
			
				Element elCWE = new Element("cwe");
				elCWE.setAttribute("id", cwe.id);
				if (cwe.matching > 0) {
					elCWE.setAttribute("type", "indirect");
					elCWE.setAttribute("matching", Integer.toString(cwe.matching));
				} else {
					elCWE.setAttribute("type", "direct");
				}
				elCWE.setAttribute("name", cwe.name);
				
				if (cwe.metadata.get("processing-time") != null) {
					elCWE.setAttribute("processing_time", cwe.metadata.get("processing-time"));
				}
				
				elCWE.addContent(new Element("description").setText(cwe.description));
				elCWE.addContent(new Element("keywords").setText(cwe.keywords.toString()));
				elCWE.addContent(new Element("disabled_keywords").setText(cwe.disabledKeywords.toString()));
				
				el.addContent(elCWE);
				
				if (cwe.directCAPEC.size() > 0) {
					Element elDirectCAPECList = new Element("direct_capec");
					elCWE.addContent(elDirectCAPECList);
					
					printCAPEC(elDirectCAPECList, cwe.directCAPEC);
		    	}
		    	
		    	if (cwe.indirectCAPEC.size() > 0) {
		    		Element elIndirectCAPECList = new Element("indirect_capec");
		    		elCWE.addContent(elIndirectCAPECList);
		    		
		    		printCAPEC(elIndirectCAPECList, cwe.indirectCAPEC);
		    	}
		    	
		    	if (cwe.mitigations != null && cwe.mitigations.size() > 0) {
		    		Element elMitigationList = new Element("mitigations");
		    		elCWE.addContent(elMitigationList);
					
		    		printMitigation(elMitigationList, cwe.mitigations);
		    	}
			}
		}
		
		// If CWE count less than 5, the print CWE has no CAPEC
		if (cweCount < 5) {
			for (CWEItem cwe : listCWE) {
				// Print CWE has CAPEC first
				if (cwe.directCAPEC.size() + cwe.indirectCAPEC.size() <= 0) {
					
					cweCount++;
					
					Element elCWE = new Element("cwe");
					elCWE.setAttribute("id", cwe.id);
					if (cwe.matching > 0) {
						elCWE.setAttribute("type", "indirect");
						elCWE.setAttribute("matching", Integer.toString(cwe.matching));
					} else {
						elCWE.setAttribute("type", "direct");
					}
					elCWE.setAttribute("name", cwe.name);
					
					if (cwe.metadata.get("processing-time") != null) {
						elCWE.setAttribute("processing_time", cwe.metadata.get("processing-time"));
					}
					
					elCWE.addContent(new Element("description").setText(cwe.description));
					elCWE.addContent(new Element("keywords").setText(cwe.keywords.toString()));
					elCWE.addContent(new Element("disabled_keywords").setText(cwe.disabledKeywords.toString()));
					
					el.addContent(elCWE);
					
					if (cwe.mitigations != null && cwe.mitigations.size() > 0) {
						Element elMitigationList = new Element("mitigations");
			    		elCWE.addContent(elMitigationList);
						
			    		printMitigation(elMitigationList, cwe.mitigations);
		        	}
		        	
		        	if (cweCount == 5) {
		        		break;
		        	}
				}
			}
		}
	}
	
	private void printCAPEC(Element el, ArrayList<CAPECItem> listCAPEC) {
		int capecCount = 0;
		
		for (CAPECItem capec : listCAPEC) {
			capecCount++;
			
			Element elCAPEC = new Element("capec");
			
			elCAPEC.setAttribute("id", capec.id);
			
			if (capec.matching > 0) {
				elCAPEC.setAttribute("type", "indirect");
				elCAPEC.setAttribute("matching", Integer.toString(capec.matching));
			} else {
				elCAPEC.setAttribute("type", "direct");
			}
			elCAPEC.setAttribute("name", capec.name);
			
			el.addContent(elCAPEC);
			
			
			if (capec.mitigations != null && capec.mitigations.size() > 0) {
				Element elMitigationList = new Element("mitigations");
				elCAPEC.addContent(elMitigationList);
				
				printMitigation(elMitigationList, capec.mitigations);
			}
			
			if (capecCount == 5) {
				break;
			}
		}
	}
	
	private void printMitigation(Element el, ArrayList<Mitigation> mitigations) {
		int mitigationCount = 0;
		
		for (Mitigation mitigation : mitigations) {
			mitigationCount++;
			
			Element elMitigation = new Element("mitigation");
			
			if (mitigation.metadata.get("processing-time") != null) {
				elMitigation.setAttribute("processing_time", mitigation.metadata.get("processing-time"));
			}
			
			elMitigation.addContent(new Element("description").setText(mitigation.description));
			elMitigation.addContent(new Element("keywords").setText(mitigation.keywords.toString()));
			elMitigation.addContent(new Element("disabled_keywords").setText(mitigation.disabledKeywords.toString()));
			
			el.addContent(elMitigation);
			
			if (mitigation.securityPatterns != null && mitigation.securityPatterns.size() > 0) {
				Element elSecurityPatternList = new Element("security_patterns");
				elMitigation.addContent(elSecurityPatternList);
				
				printSecurityPattern(elSecurityPatternList, mitigation.securityPatterns);
			}
			
			if (mitigationCount == 5) {
				break;
			}
		}
	}
	
	private void printSecurityPattern(Element el, ArrayList<SecurityPattern> patterns) {
		int patternCount = 0;
		
		for (SecurityPattern pattern : patterns) {
			patternCount++;
			
			Element elSecurityPattern = new Element("security_pattern");
			elSecurityPattern.setAttribute("name", pattern.name);
			
			el.addContent(elSecurityPattern);
			
			if (patternCount == 5) {
				break;
			}
		}
	}
}
