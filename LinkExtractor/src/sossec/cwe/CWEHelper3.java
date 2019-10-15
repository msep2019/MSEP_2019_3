package sossec.cwe;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import sossec.XMLHelper;

public class CWEHelper3 {
	
	public String[] cweFiles = { "src/databases/CWE_desc_research.xml", "src/databases/CWE_desc_architectural.xml", "src/databases/CWE_desc_development.xml" };
	public String[] capecFiles = { "src/databases/CAPEC_desc_domains.xml", "src/databases/CAPEC_desc_mechanisms.xml" };
	
	public String getCweIdContent(String Id) {
		String result = "";
	
		for (String file : cweFiles) {
			Document document = XMLHelper.getSAXParsedDocument(file);
		
			Element rootNode = document.getRootElement();
		
			String query = "/Weakness_Catalog/Weaknesses/Weakness[@ID= '" + Id + "']";
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
	
	public String getCapecIdContent(String Id) {
		String result = "";
	
		for (String file : capecFiles) {
			Document document = XMLHelper.getSAXParsedDocument(file);
		
			Element rootNode = document.getRootElement();
		
			String query = "/Attack_Pattern_Catalog/Attack_Patterns/Attack_Pattern[@ID='" + Id + "']";
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
}
