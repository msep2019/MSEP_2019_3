package sossec.cve;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class SearchDirectlinks {

    public HashMap<ArrayList<String>,String> directLinks(InputStream input){
       
    	SAXBuilder saxBuilder = new SAXBuilder();
    	
    	//storage of direct cwe id and name
    	HashMap<ArrayList<String>,String> cveCweName = new HashMap<ArrayList<String>,String>();
    	
        try {
        	//build SAX parser
        	Document document = saxBuilder.build(input);
        	
        	//get the root node of the xml file
        	Element root = document.getRootElement();
        	List<Element> childList = root.getChildren();
        	
        	//storage of cwe id and name
        	HashMap<String,String> cweName = new HashMap<String,String>();
        	
        	//storage of founded and repeated CVE id
        	HashMap<String,ArrayList<String>> vul = new HashMap<String,ArrayList<String>>();
        	
        	//storage of founded and repeated CAPEC id     	
        	HashMap<String,ArrayList<String>> pattern = new HashMap<String,ArrayList<String>>();
        	
        	//the returned HashMap and ArrayList containing direct link ids
         	HashMap<ArrayList<String>,ArrayList<String>> vpLink = new HashMap<ArrayList<String>,ArrayList<String>>();

        	
        	//Search and retrieve CVE id AND CWE ID and description
        	for(Element child : childList) {
    			if(child.getName().equals("Weaknesses")) {
    				List<Element> wknsList = child.getChildren();
            		for(Element wkns : wknsList) {
            			if(wkns.getName().equals("Weakness")) {   
            				List<Element> wknList = wkns.getChildren();
            				cweName.put(wkns.getAttributeValue("ID"),wkns.getAttributeValue("Name"));
            				for(Element wkn : wknList) {          					
            					if(wkn.getName().equals("Observed_Examples")) {
            						List<Element> egList = wkn.getChildren();
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
        	
        	//Associate direct link groups with cwe id
        	for(Map.Entry<String,ArrayList<String>> entryVul : vul.entrySet()) {
            	for(Map.Entry<String,ArrayList<String>> entryCap : pattern.entrySet()) {	
            		if(entryVul.getKey().equals(entryCap.getKey())) {
            			vpLink.put(entryVul.getValue(), entryCap.getValue());
            			for(Map.Entry<String, String> entryDes : cweName.entrySet()){           				
            				if(entryDes.getKey().equals(entryVul.getKey())){           					
            					cveCweName.put(entryVul.getValue(),entryDes.getKey()+"--"+entryDes.getValue());
            				}
            			}
            		}
            	}
        	}      	
        	
        	System.out.println("<--End of Match-->");
        	        	
        }catch(FileNotFoundException e) {
        	e.printStackTrace();
        }catch(JDOMException e) {
        	e.printStackTrace();
        }catch(IOException e) {
        	e.printStackTrace();
        }
        
    	//save the direct CWE id with name
        return cveCweName;
    }
}
