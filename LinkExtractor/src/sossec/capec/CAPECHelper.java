package sossec.capec;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import sossec.XMLHelper;

public class CAPECHelper {

	public String[] xmlFiles = { "src/databases/CAPEC_desc_domains.xml", "src/databases/CAPEC_desc_mechanisms.xml" };


	public String getItemContent(String Id) {
		String result = "";

		for (String file : xmlFiles) {
			Document document = XMLHelper.getSAXParsedDocument(file);

			String query = "/Attack_Pattern_Catalog/Attack_Patterns/Attack_Pattern[@ID= '" + Id + "']";
			XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());
			
			List<Element> xPathResult = xpe.evaluate(document);
	        
			if (xPathResult.size() > 0) {
				result = xPathResult.get(0).getChild("Description").getValue();
			}

			if (!result.isEmpty()) {
				break;
			}
		}

		return result;
	}
	
	public String getCAPECName(String Id) {
		String result = "";
	
		for (String file : xmlFiles) {
			Document document = XMLHelper.getSAXParsedDocument(file);
		
			String query = "/Attack_Pattern_Catalog/Attack_Patterns/Attack_Pattern[@ID='" + Id + "']";
			XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());
		
			List<Element> xPathResult = xpe.evaluate(document);
		
			if (xPathResult.size() > 0) {
				result = xPathResult.get(0).getAttribute("Name").getValue();
			}
		
			if (!result.isEmpty()) {
				break;
			}
		}
	
		return result;		
	}
}
