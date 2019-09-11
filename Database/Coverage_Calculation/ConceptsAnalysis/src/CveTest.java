import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

//import com.sun.org.apache.bcel.internal.classfile.Attribute;

public class CveTest {

    public static void main(String[] args) throws JDOMException, IOException {
        
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
        
        try {
        	
        	in = new FileInputStream("xml/Research(1000).xml");
        	Document document = saxBuilder.build(in);
        	
        	Element root = document.getRootElement();
        	List<Element> childList = root.getChildren();
        	
        	//storage of founded and repeated CVE id
        	List<String> idCvestorage = new ArrayList<>();
        	List<String> idCverepeated = new ArrayList<>();
        	HashSet<String> setCve = new HashSet<>();
        	
        	//number of the CWE id containing CAPEC id
        	int cveIncwe = 0 ;
        	
        	//Search and retrieve CVE id
        	for(Element child : childList) {
        		//List<org.jdom2.Attribute> attrList = child.getAttributes();
        		System.out.println("executed");
    			if(child.getName().equals("Weaknesses")) {
    				List<Element> wknsList = child.getChildren();
            		for(Element wkns : wknsList) {
            			if(wkns.getName().equals("Weakness")) {
            				System.out.println("<--Start reading info : number of Weakness " + (wknsList.indexOf(wkns) + 1 ) + "-->");
            				System.out.println("Weakness Node ID: " + wkns.getName() + " --> " + wkns.getAttributeValue("ID"));
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
                        	            			System.out.println("CVE Id:  -->  " + cve.getText());
                        	            			idCvestorage.add(cve.getText());
            									}
            								}
            							}
            						}
            					}
            				}
            				System.out.println("<--End reading info-->");
            			}
            			
            		}
    			}
        	}
        	
        	//filter the repeated element
        	for(String id : idCvestorage) {
        		boolean add = setCve.add(id);
        		if(!add) {
        			idCverepeated.add(id);
        		}
        	}
        	
        	System.out.println("Total number of founded CVE id: "+ idCvestorage.size()+" Total number of Cwe id linked to CVE: "+cveIncwe);
        	System.out.println("Number of repeated CVE id: "+ idCverepeated.size() + " Number of unique CVE id: " +setCve.size());
        	
        }catch(FileNotFoundException e) {
        	e.printStackTrace();
        }catch(JDOMException e) {
        	e.printStackTrace();
        }catch(IOException e) {
        	e.printStackTrace();
        }
    }
}
