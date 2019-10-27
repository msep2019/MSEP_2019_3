package sossec.mitigation;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import sossec.XMLHelper;

public class SecurityPattern {
	public String name;
	
	public int matching;
	
	public String[] xmlFiles = { "src/databases/securitypatterns/security_patterns.xml" };
	
	public SecurityPattern(String name) {
		this.name = name;
	}
	
	public String toString() {
		String result = "<html>";
		
		if (matching >= 1) {
			result += "[" + matching + "] ";
		}
		
		result += this.name + "</html>";
		
		return result;
		
	}
	
	public String getContent() {
		String result = "";

		for (String file : xmlFiles) {
			Document document = XMLHelper.getSAXParsedDocument(file);

			String query = "/Security_Patterns/Security_Pattern[@name= '" + this.name + "']";
			XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());

			List<Element> xPathResult = xpe.evaluate(document);
			
			if (xPathResult.size() > 0) {
				
				Element elPattern = xPathResult.get(0);
				
				result += "<html>";
				
				result += "<h1>Quick Info</h1>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Quick_info").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h1>Problem</h1>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Problem").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h1>Forces</h1>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Forces").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h1>Example</h1>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Example").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h1>Solution</h1>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Solution").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h2>Structure</h2>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Solution").getChild("Structure").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h2>Dynamics</h2>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Solution").getChild("Dynamics").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h2>Participants</h2>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Solution").getChild("Participants").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h2>Collaborations</h2>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Solution").getChild("Collaborations").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h1>Implementation</h1>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Implementation").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h1>Pitfalls</h1>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Pitfalls").getChild("Content").getContent()) + "</div>";
				
				result += "<br/><br/><h1>Consequences</h1>";
				result += "<div>" + new XMLOutputter().outputString(elPattern.getChild("Consequences").getChild("Content").getContent()) + "</div>";
				
				//result += "<h1>Known_uses</h1>";
				//result += "<div>" + elPattern.getChild("Known_uses").getChild("Content").getValue() + "</div>";
				
				result += "</html>";
				
				System.out.println(result);
			}

			if (!result.isEmpty()) {
				break;
			}
		}

		return result;
	}
}
