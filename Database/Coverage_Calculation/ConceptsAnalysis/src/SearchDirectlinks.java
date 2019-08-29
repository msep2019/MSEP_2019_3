import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.sun.org.apache.bcel.internal.classfile.Attribute;

public class SearchDirectlinks {

    public static void directLinks(String cveid, InputStream input){
       
    	SAXBuilder saxBuilder = new SAXBuilder();
        try {
        	//build SAX parser
        	Document document = saxBuilder.build(input);
        	
        	//get the root node of the xml file
        	Element root = document.getRootElement();
        	List<Element> childList = root.getChildren();
        	
        	//storage of founded and repeated CVE id
        	HashMap<String,ArrayList<String>> vul = new HashMap<String,ArrayList<String>>();
        	
        	//storage of founded and repeated CAPEC id     	
        	HashMap<String,ArrayList<String>> pattern = new HashMap<String,ArrayList<String>>();
        	
        	//number of the cwe id containing capec id
        	int cveIncwe = 0 ;
        	//number of the cwe id containing capec id
        	int capIncwe = 0 ;
        	
        	//the returned HashMap
        	HashMap<ArrayList<String>,ArrayList<String>> vpLink = new HashMap<ArrayList<String>,ArrayList<String>>();
        	
        	//Search and retrieve CVE id
        	for(Element child : childList) {
    			if(child.getName().equals("Weaknesses")) {
    				List<Element> wknsList = child.getChildren();
            		for(Element wkns : wknsList) {
            			if(wkns.getName().equals("Weakness")) {   
            				List<Element> wknList = wkns.getChildren();           				
            				for(Element wkn : wknList) {
            					if(wkn.getName().equals("Observed_Examples")) {
            						List<Element> egList = wkn.getChildren();
            						cveIncwe++;
            						for(Element subList : egList) {
            							if(subList.getName().equals("Observed_Example")) {
            								List<Element> cveList = subList.getChildren();
            								for(Element cve : cveList) {
            									if(cve.getName().equals("Reference")) {
                	            					ArrayList<String> idCvestorage;
                	            					if(vul.containsKey(wkns.getAttributeValue("ID"))) {
                	            						idCvestorage = vul.get(wkns.getAttributeValue("ID"));
                	            						idCvestorage.add(cve.getText());
                	            					}else {
                	            						idCvestorage = new ArrayList<String>();
                	            						idCvestorage.add(cve.getText());
                	            						vul.put(wkns.getAttributeValue("ID"),idCvestorage);
                	            					}
            									}
            								}
            							}
            						}
            					}
            				}
            			}        			
            		}
    			}
        	}

        	//Search and retrieve CAPEC id
        	for(Element child : childList) {
    			if(child.getName().equals("Weaknesses")) {
    				List<Element> wknsList = child.getChildren();
            		for(Element wkns : wknsList) {
            			if(wkns.getName().equals("Weakness")) {
            				List<Element> wknList = wkns.getChildren();            				
            				for(Element wkn : wknList) {
            					if(wkn.getName().equals("Related_Attack_Patterns")) {
            						List<Element> atkList = wkn.getChildren();
            						capIncwe++;
            	            		for(Element atkpattern : atkList) {
            	            			if(atkpattern.getName().equals("Related_Attack_Pattern")) {
                	            			List<org.jdom2.Attribute> atkAttr = atkpattern.getAttributes();
                	            			for(org.jdom2.Attribute attr :atkAttr) {
                	            				if(attr.getName().equals("CAPEC_ID")) {
                	            					ArrayList<String> idCapstorage;
                	            					if(pattern.containsKey(wkns.getAttributeValue("ID"))) {
                	            						idCapstorage = pattern.get(wkns.getAttributeValue("ID"));
                	            						idCapstorage.add(attr.getValue());
                	            					}else {
                	            						idCapstorage = new ArrayList<String>();
                	            						idCapstorage.add(attr.getValue());
                	            						pattern.put(wkns.getAttributeValue("ID"),idCapstorage);
                	            					}
                	            				}
                	            			} 
            	            			}
            	            		}
            					}
            				}
            			}           			
            		}
    			}
        	}     	
        	
        	/*for(java.util.Map.Entry<String,ArrayList<String>> entryVul : vul.entrySet()) {
        		System.out.println("Cwe_ID:" + entryVul.getKey() + " --> Cve_ID:"+ entryVul.getValue());
        	}*/
        	       	
        	// output the CVE_ID and CAPEC_ID which have direct links
        	System.out.println("<--Direct links between CVE_ID and CAPEC_ID-->");
        	for(Map.Entry<String,ArrayList<String>> entryVul : vul.entrySet()) {
            	for(Map.Entry<String,ArrayList<String>> entryCap : pattern.entrySet()) {	
            		if( entryVul.getKey().equals(entryCap.getKey())) {
            			System.out.println("CVE_ID:" + entryVul.getValue() + " --> CAPEC_ID:"+ entryCap.getValue());
            			vpLink.put(entryVul.getValue(), entryCap.getValue());
            		}
            	}
        	}   
        	
        	System.out.println("<--End of Print-->");
        	System.out.println("<--The results of marching-->");
        	
        	// search for the provided cveid
    		Set<Entry<ArrayList<String>, ArrayList<String>>> entrySet = vpLink.entrySet();
    		Iterator<Map.Entry<ArrayList<String>, ArrayList<String>>> iterator = entrySet.iterator();
        	
        	while(iterator.hasNext()) {
        		ArrayList<String> match = new ArrayList<String>();
        		Map.Entry<ArrayList<String>, ArrayList<String>> entryMatch = iterator.next();
        		match = entryMatch.getKey();
        		for(int i = 0; i < match.size(); i++) {
            		if( cveid.equals(match.get(i)) ) {
            			System.out.println( "CVE_ID:" + cveid + " --> CAPEC_ID(Group):"+ entryMatch.getValue() );
            		}
        		}

        	}
        	
        	System.out.println("<--End of Search-->");

        }catch(FileNotFoundException e) {
        	e.printStackTrace();
        }catch(JDOMException e) {
        	e.printStackTrace();
        }catch(IOException e) {
        	e.printStackTrace();
        }
    }
}
