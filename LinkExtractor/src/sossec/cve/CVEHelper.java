package sossec.cve;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import sossec.XMLHelper;

public class CVEHelper {

	public String[] xmlFiles = { "src/databases/CVE_desc.xml" };

	public String getItemContent(String Id) {
		String result = "";

		for (String file : xmlFiles) {
			Document document = XMLHelper.getSAXParsedDocument(file);

			String query = "/cve/item[@name= '" + Id + "']";
			XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());
			
			List<Element> xPathResult = xpe.evaluate(document);
	        
			if (xPathResult.size() > 0) {
				result = xPathResult.get(0).getChild("desc").getValue();
			}

			if (!result.isEmpty()) {
				break;
			}
		}

		return result;
	}
}
