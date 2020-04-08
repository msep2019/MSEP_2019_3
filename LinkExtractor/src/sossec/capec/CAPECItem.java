package sossec.capec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import sossec.XMLHelper;
import sossec.mitigation.Mitigation;

public class CAPECItem {
	public String id;
	public String name;
	public String description = "";
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<Mitigation> mitigations = new ArrayList<>();
	public int matching = -1;
	public HashMap<String, String> metadata = new HashMap<String, String>();
	
	public static String[] xmlMitigations = { "src/databases/CAPEC_mitigation_domains.xml",
			"src/databases/CAPEC_mitigation_mechanisms.xml", "src/databases/CWE_mitigation_research.xml" };

	public CAPECItem(String id) {
		this.id = id;
	}

	public CAPECItem(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		String result = "<html>";

		if (matching >= 1) {
			result += "[" + matching + "] ";
		}

		result += "CAPEC <font color=\"green\">" + this.id + "</font> " + this.name + "</html>";

		return result;

	}

	public List<Mitigation> getMitigations() {
		this.mitigations = new ArrayList<>();

		for (String file : xmlMitigations) {
			Document document = XMLHelper.getSAXParsedDocument(file);
			String query = "/Attack_Pattern_Catalog/Attack_Patterns/Attack_Pattern[@ID= '" + this.id + "']";
			XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());

			List<Element> xPathResult = xpe.evaluate(document);

			if (xPathResult.size() > 0) {
				List<Element> listMitigations = xPathResult.get(0).getChild("Mitigations").getChildren();
				
				for (Element el : listMitigations) {
					if (!el.getValue().trim().isEmpty()) {
						Mitigation mitigationItem = new Mitigation();
						mitigationItem.description = el.getValue();
						mitigationItem.capec = this;
						this.mitigations.add(mitigationItem);
					}
				}

				break;
			}
		}

		return this.mitigations;
	}
}