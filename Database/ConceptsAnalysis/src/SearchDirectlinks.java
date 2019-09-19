import java.io.FileInputStream;
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

public class SearchDirectlinks {

    public void directLinks(String cveid, InputStream input){
       
    	SAXBuilder saxBuilder = new SAXBuilder();
     	
        try {
        	//build SAX parser
        	Document document = saxBuilder.build(input);
        	
        	//get the root node of the xml file
        	Element root = document.getRootElement();
        	List<Element> childList = root.getChildren();
        	
        	//storage of cwe id and description
        	HashMap<String,String> cweDescription = new HashMap<String,String>();
        	HashMap<String,String> cweIdDescription = new HashMap<String,String>();
        	
        	//storage of founded and repeated CVE id
        	HashMap<String,ArrayList<String>> vul = new HashMap<String,ArrayList<String>>();
        	
        	//storage of founded and repeated CAPEC id     	
        	HashMap<String,ArrayList<String>> pattern = new HashMap<String,ArrayList<String>>();
        	
        	//the returned HashMap and ArrayList containing direct link ids
         	HashMap<ArrayList<String>,ArrayList<String>> vpLink = new HashMap<ArrayList<String>,ArrayList<String>>();
        	ArrayList<String> dcveId = new ArrayList<String>();
        	ArrayList<String> dcapecId = new ArrayList<String>();
        	
        	//filter storage of cve and capec id
        	List<String> rcveId = new ArrayList<>();
        	List<String> rcapecId = new ArrayList<>();
        	HashSet<String> setCve = new HashSet<>();
        	HashSet<String> setCapec = new HashSet<>();
        	
        	//Search and retrieve CVE id AND CWE ID and description
        	for(Element child : childList) {
    			if(child.getName().equals("Weaknesses")) {
    				List<Element> wknsList = child.getChildren();
            		for(Element wkns : wknsList) {
            			if(wkns.getName().equals("Weakness")) {   
            				List<Element> wknList = wkns.getChildren();           				
            				for(Element wkn : wknList) {
            					if(wkn.getName().equals("Description")){
            						cweDescription.put(wkns.getAttributeValue("ID"),wkn.getText());
            					}            					
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
        	
        	/*System.out.println("<--Direct links between CWE_ID and CVE_ID-->");
        	for(java.util.Map.Entry<String,ArrayList<String>> entryVul : vul.entrySet()) {
        		System.out.println("CWE_ID:" + entryVul.getKey() + " --> CVE_ID(Group):"+ entryVul.getValue());
        	}
        	System.out.println("<--End of Print-->");
        	
        	System.out.println("<--Direct links between CWE_ID and CAPEC_ID-->");
        	for(java.util.Map.Entry<String,ArrayList<String>> entryVul : pattern.entrySet()) {
        		System.out.println("CWE_ID:" + entryVul.getKey() + " --> CAPEC_ID(Group):"+ entryVul.getValue());
        	}
        	System.out.println("<--End of Print-->");*/
        	
        	// output the CVE_ID and CAPEC_ID which have direct links
        	//System.out.println("<--Print of direct links between CVE_ID and CAPEC_ID-->");
        	//Associate direct link groups with description
        	for(Map.Entry<String,ArrayList<String>> entryVul : vul.entrySet()) {
            	for(Map.Entry<String,ArrayList<String>> entryCap : pattern.entrySet()) {	
            		if(entryVul.getKey().equals(entryCap.getKey())) {
            			//System.out.println("CVE_ID:" + entryVul.getValue() + " --> CAPEC_ID:"+ entryCap.getValue());
            			vpLink.put(entryVul.getValue(), entryCap.getValue());
            			for(Map.Entry<String, String> entryDes : cweDescription.entrySet()){
            				if(entryDes.getKey().equals(entryVul.getKey())){
            					cweIdDescription.put(entryDes.getKey(),entryDes.getValue());
            				}
            			}
            		}
            	}
        	}   
        	
        	//System.out.println("<--End of Print-->");    		
    		//match attack patterns by using capec id
    		InputStream input4;
    		InputStream input5;
    		SearchAtkInfo sai;
    		FilteredAtkCapec fi;
    		HashMap<String,ArrayList<String>> idPatternstore = new HashMap<String,ArrayList<String>>();
    		//HashMap<String,ArrayList<String>> idPatternstore = new HashMap<String,ArrayList<String>>();
    		
        	System.out.println("<--The results of matching-->");
        	System.out.println("└─Number of direct matches between groups(CVE-CAPEC): " + vpLink.size());
        	// search for the provided cveid and calculate the coverage of direct links
    		Set<Entry<ArrayList<String>, ArrayList<String>>> entrySet = vpLink.entrySet();
    		Iterator<Map.Entry<ArrayList<String>, ArrayList<String>>> iterator = entrySet.iterator();
        	
        	while(iterator.hasNext()) {
        		ArrayList<String> match = new ArrayList<String>();
        		Map.Entry<ArrayList<String>, ArrayList<String>> entryMatch = iterator.next();
        		match = entryMatch.getValue();
        		for(int i = 0; i < match.size(); i++) {
        			dcapecId.add(match.get(i));
        		}
        		match = new ArrayList<String>();
        		match = entryMatch.getKey();
        		for(int i = 0; i < match.size(); i++) {
        			dcveId.add(match.get(i));
        			if( cveid.equals(match.get(i)) ) {  
        				System.out.println( "	└─>Matched direct link: " + cveid + " --> CAPEC_ID(Group): "+ entryMatch.getValue() );	
        				for(Map.Entry<String,ArrayList<String>> entryCap : pattern.entrySet()){
        					if(entryCap.getValue().equals(entryMatch.getValue())){
        						for(Map.Entry<String, String> entryDes : cweIdDescription.entrySet()){
        							if(entryDes.getKey().equals(entryCap.getKey())){
        								System.out.println("	└─>Matched Weakness ID: " + entryDes.getKey() + " --Description-- " + entryDes.getValue());
        							}
        						}
        					}
        				}
											
						//initialize related classes for input1 of capec datasets
						input4 = new FileInputStream("xml/Domains of Attack(3000).xml");
						sai = new SearchAtkInfo();
        	    		sai.searchPattern(input4);
        	    		fi = new FilteredAtkCapec();
        	    		idPatternstore = fi.getIdPattern();
        	    		
        	    		//match capec id with attack patterns

    	    			for(String eCapec : entryMatch.getValue()) {
            	    		Set<Entry<String, ArrayList<String>>> entryPattern1 = idPatternstore.entrySet();
            	    		Iterator<Map.Entry<String, ArrayList<String>>> iPattern1 = entryPattern1.iterator();
    	    				while(iPattern1.hasNext()) {
    	    					Map.Entry<String, ArrayList<String>> ePattern1 = iPattern1.next();
    	    					ArrayList<String> capecPattern = new ArrayList<String>();
    	    					capecPattern = ePattern1.getValue();
        	    				for(String natureId : capecPattern) {
        	    					if(natureId.replaceAll("[a-zA-Z]","").equals(eCapec)) {
        	    						System.out.println( "		└─Related attack pattern ID: " + ePattern1.getKey() + " --" + natureId.replaceAll("\\d","") + "-- CAPEC_ID: " + eCapec);
        	    					}
        	    				}
        	    			}
        	    		}
        	    		
        	    		//initialize related classes for input2 of capec datasets
        	    		input5 = new FileInputStream("xml/Mechanisms of Attack(1000).xml");
        	    		sai = new SearchAtkInfo();
        	    		sai.searchPattern(input5);
        	    		fi = new FilteredAtkCapec();
        	    		idPatternstore = fi.getIdPattern();	
        	    		
        	    		//match capec id with attack patterns
        	    		for(String eCapec : entryMatch.getValue()) {
            	    		Set<Entry<String, ArrayList<String>>> entryPattern2 = idPatternstore.entrySet();
            	    		Iterator<Map.Entry<String, ArrayList<String>>> iPattern2 = entryPattern2.iterator();
        	    			while(iPattern2.hasNext()) {
        	    				Map.Entry<String, ArrayList<String>> ePattern2 = iPattern2.next();
        	    				ArrayList<String> capecPattern = new ArrayList<String>();
        	    				capecPattern = ePattern2.getValue();       	    			
        	    				for(String natureId : capecPattern) {
        	    					if(natureId.replaceAll("[a-zA-Z]","").equals(eCapec)) {
        	    						System.out.println( "		└─Related attack pattern ID: " + ePattern2.getKey() + " --" + natureId.replaceAll("\\d","") + "-- CAPEC_ID: " + eCapec);
        	    					}
        	    				}
        	    			}
        	    		}
            		}
        		}
        	}
        	
        	//filter the repeated cve and capec ID
        	for(String id : dcveId) {
        		boolean add = setCve.add(id);
        		if(!add) {
        			rcveId.add(id);
        		}
        	}
        	for(String id : dcapecId) {
        		boolean add = setCapec.add(id);
        		if(!add) {
        			rcapecId.add(id);
        		}
        	}
        	
        	//save the filtered CVE and CAPEC in hashsets
        	FilteredCveCapec setCvecapec = new FilteredCveCapec();
        	setCvecapec.setFilteredcve(setCve);
        	setCvecapec.setFilteredcapec(setCapec);

        	//System.out.println(" Coverage of direct links in CVE: "+ setCve.size()+ "/153347");
        	//System.out.println(" Coverage of direct links in CAPEC: "+ setCapec.size()+ "/577");
        	System.out.println("<--End of Match-->");
        	
        	
        }catch(FileNotFoundException e) {
        	e.printStackTrace();
        }catch(JDOMException e) {
        	e.printStackTrace();
        }catch(IOException e) {
        	e.printStackTrace();
        }
    }
}
