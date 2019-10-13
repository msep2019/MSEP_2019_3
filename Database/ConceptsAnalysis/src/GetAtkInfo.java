
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class GetAtkInfo {

    public static void main(String[] args) throws JDOMException, IOException {
        
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
        
        try {
        	
        	in = new FileInputStream("xml/Mechanisms of Attack(1000).xml");
        	Document document = saxBuilder.build(in);
        	
        	//get root node of the xml file
        	Element root = document.getRootElement();
        	List<Element> childList = root.getChildren();
        	
        	//storage of founded CAPEC description and taxonomy
        	HashMap<String,String> idCapecdescription = new HashMap<String,String>();
        	HashMap<String,ArrayList<String>> idCapectaxonomy = new HashMap<String,ArrayList<String>>();
        	HashMap<String,ArrayList<String>> idPatternstore = new HashMap<String,ArrayList<String>>();
        	
        	//filter storage of taxonomy entry id
        	List<String> repTaxonomy = new ArrayList<>();
        	HashSet<String> setTaxonomy = new HashSet<>();
        	
        	//Calculate number of the CAPEC id containing mitigations
        	int capecNum = 0 ;
        	int mitIncwe = 0 ;
        	
        	//Search and retrieve CVE id
        	for(Element child : childList) {
        		//List<org.jdom2.Attribute> attrList = child.getAttributes();
        		//System.out.println("executed");
    			if(child.getName().equals("Attack_Patterns")) {
    				List<Element> apsList = child.getChildren();
            		for(Element ap : apsList) {
            			if(ap.getName().equals("Attack_Pattern")) {
            				capecNum++;
            				System.out.println("<--Start reading info number of Pattern: " + (apsList.indexOf(ap) + 1 ) + "-->");
            				System.out.println("Node ID: " + ap.getName() + " --> " + ap.getAttributeValue("ID"));
            				List<Element> patternList = ap.getChildren();
            				
            				for(Element pattern : patternList) {            					

            					//search information about description
            					if(pattern.getName().equals("Description")) {
            						System.out.println("└─Description: " + pattern.getText());
            						idCapecdescription.put(ap.getAttributeValue("ID"), pattern.getText());
            					}
            					
            					//search information about related capec ids
            					if(pattern.getName().equals("Related_Attack_Patterns")) {
            						List<Element> capecList = pattern.getChildren();           
            						for(Element idList : capecList) {
            							if(idList.getName().equals("Related_Attack_Pattern")) {
            								ArrayList<String> idPattern;
            								if(idPatternstore.containsKey(ap.getAttributeValue("ID"))) {
            									idPattern = idPatternstore.get(ap.getAttributeValue("ID"));
            									idPattern.add(idList.getAttributeValue("Nature")+idList.getAttributeValue("CAPEC_ID"));
            								}else {
            									idPattern = new ArrayList<String>();
            									idPattern.add(idList.getAttributeValue("Nature")+idList.getAttributeValue("CAPEC_ID"));
            									idPatternstore.put(ap.getAttributeValue("ID"),idPattern);
            								}
            								System.out.println("	└─Related CAPEC id: " + idList.getAttributeValue("CAPEC_ID"));
            								System.out.println("		└─Related Nature: " + idList.getAttributeValue("Nature"));
            							}
            						}
            					}           					
            					
            					// search information about taxonomy
            					if(pattern.getName().equals("Taxonomy_Mappings")) {
            						List<Element> txmysList = pattern.getChildren();
            						for(Element subList : txmysList) {
            							if(subList.getName().equals("Taxonomy_Mapping")) {
            								System.out.println(" Taxonomy Name: "+subList.getAttributeValue("Taxonomy_Name"));
            								List<Element> txmyList = subList.getChildren();
            								for(Element entry : txmyList) {
            									if(entry.getName().equals("Entry_ID")) {
                        	            			System.out.println(" Entry ID:  -->  " + entry.getText());
                									ArrayList<String> entryId;
                	            					if(idCapectaxonomy.containsKey(ap.getAttributeValue("ID"))) {
                	            						entryId = idCapectaxonomy.get(ap.getAttributeValue("ID"));
                	            						entryId.add(entry.getText());
                	            					}else {
                	            						entryId = new ArrayList<String>();
                	            						entryId.add(entry.getText());
                	            						idCapectaxonomy.put(ap.getAttributeValue("ID"),entryId);
                	            					}
            									}
            								}
            							}
            						}
            					}
            					
            					//search information about mitigations
            					if(pattern.getName().equals("Mitigations")) {
            						mitIncwe++;
            					}
            				}
            				System.out.println("<--End reading info-->");
            			}
            			
            		}
    			}
        	}
        	
        	//filter the repeated taxonomy
        	for(Map.Entry<String,ArrayList<String>> entryTxmy : idCapectaxonomy.entrySet()) {	            	
            	for(String id : entryTxmy.getValue()) {
            		boolean add = setTaxonomy.add(id);
            		if(!add) {
            			repTaxonomy.add(id);
            		}
            	}
        	}
        	
        	System.out.println("Unique Taxonomy Entry Id:" + setTaxonomy.toString());       	
        	System.out.println("Total number of founded Attack patterns: " + idCapecdescription.size()+", Proportion of mitigations: " + mitIncwe + "/" + capecNum);
        	System.out.println("Number of repeated Taxonomy Entry Id: " + idCapectaxonomy.size() + ", Number of unique Taxonomy Entry Id: " + setTaxonomy.size());
        	
        }catch(FileNotFoundException e) {
        	e.printStackTrace();
        }catch(JDOMException e) {
        	e.printStackTrace();
        }catch(IOException e) {
        	e.printStackTrace();
        }
    }
}