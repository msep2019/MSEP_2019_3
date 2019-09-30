package sossec.cve;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import sossec.XMLHelper;

public class CVEHelper {
	
	String xmlFile = "src/databases/CVE_desc.xml";
	
	Document document = XMLHelper.getSAXParsedDocument(xmlFile);
	
	public String getCVEContent(String cveId) {
		String result = "";
		
		Element rootNode = document.getRootElement();
		
		String query = "//*[@name= '" + cveId + "']/desc";
		XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());
		for (Element el : xpe.evaluate(document)) 
		{
			result = el.getValue();
		}
		
		
		return result;
	}
}
