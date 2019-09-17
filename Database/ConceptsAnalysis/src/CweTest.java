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

public class CweTest {

    public static void main(String[] args) throws JDOMException, IOException {
        
        SAXBuilder saxBuilder = new SAXBuilder();
        //Document readDoc = saxBuilder.build("book.xml");
        InputStream in;
        try {
        	
        	in = new FileInputStream("xml/Architectural(1008).xml");
        	Document document = saxBuilder.build(in);
        	
        	Element root = document.getRootElement();
        	List<Element> childList = root.getChildren();
        	
        	//storage of founded and repeated CAPEC id
        	List<String> idCapstorage = new ArrayList<>();
        	List<String> idCaprepeated = new ArrayList<>();
        	HashSet<String> setCap = new HashSet<>();
        	
        	//number of the cwe id containing capec id
        	int capIncwe = 0 ;
        	
        	//Search and retrieve CAPEC id
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
            					if(wkn.getName().equals("Related_Attack_Patterns")) {
            						List<Element> atkList = wkn.getChildren();
            						capIncwe++;
            	            		for(Element atkpattern : atkList) {
            	            			List<org.jdom2.Attribute> atkAttr = atkpattern.getAttributes();
            	            			for(org.jdom2.Attribute attr :atkAttr) {
            	            				System.out.println("CAPEC Id: " + attr.getName() + " --> " + attr.getValue());
            	            				idCapstorage.add(attr.getValue());
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
        	for(String id : idCapstorage) {
        		boolean add = setCap.add(id);
        		if(!add) {
        			idCaprepeated.add(id);
        		}
        	}
        	
        	System.out.println("Total number of founded CAPEC id: "+ idCapstorage.size()+" Total number of Cwe id linked to CAPEC: "+capIncwe);
        	System.out.println("Number of repeated CAPEC id: "+ idCaprepeated.size() + " Number of unique CAPEC id: " +setCap.size());
        	
        }catch(FileNotFoundException e) {
        	e.printStackTrace();
        }catch(JDOMException e) {
        	e.printStackTrace();
        }catch(IOException e) {
        	e.printStackTrace();
        }
    }
}
