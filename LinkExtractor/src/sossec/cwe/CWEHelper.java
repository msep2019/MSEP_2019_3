package sossec.cwe;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import sossec.XMLHelper;

public class CWEHelper {

	public String[] xmlFiles = { "src/databases/CWE_desc_architectural.xml", "src/databases/CWE_desc_development.xml",
			"src/databases/CWE_desc_research.xml" };

	public String getItemContent(String Id) {
		String result = "";

		for (String file : xmlFiles) {
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
	
	public List<CWEItem> findWeakness() {
		List<CWEItem> list = new ArrayList<CWEItem>();
		
		
		
		return list;
	}
}
