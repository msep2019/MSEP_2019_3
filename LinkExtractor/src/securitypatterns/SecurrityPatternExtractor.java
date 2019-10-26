package securitypatterns;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import sossec.XMLHelper;

public class SecurrityPatternExtractor {

	public String[] xmlFiles = { "src/securitypatterns/res/src_security_pattern_catalog.xml" };

	List<Element> bookmark;

	Document srcDoc;
	Document desDoc;

	public SecurrityPatternExtractor() {
		init();
	}

	private void init() {
		srcDoc = XMLHelper.getSAXParsedDocument(xmlFiles[0]);

		Element patterns = new Element("Security_Patterns");
		desDoc = new Document(patterns);
		// desDoc.setRootElement(patterns);

		bookmark = getBookmark();

		System.out.println("Bookmark size: " + bookmark.size());

		for (Element srcPattern : bookmark) {
			if (!srcPattern.getAttributeValue("title").equals("Security Pattern Catalog")) {
				Element pattern = new Element("Security_Pattern");
				pattern.setAttribute("name", srcPattern.getAttributeValue("title"));

				List<Element> srcSections = srcPattern.getChild("bookmark").getChildren("bookmark");
				for (Element srcSection : srcSections) {
					String sectionTitle = srcSection.getAttributeValue("title");
					System.out.println(sectionTitle);
					if (sectionTitle.equals("Known uses")) {
						pattern.addContent(getKnownUses(srcSection));
					} if (sectionTitle.equals("Solution")) {
						Element section = getSection(srcSection);
						List<Element> srcSolutionSections = srcSection.getChildren("bookmark");
						
						for (Element srcSolutionSection : srcSolutionSections) {
							String solutionSectionTitle = srcSolutionSection.getAttributeValue("title");
							System.out.println("  " + solutionSectionTitle);
							
							section.addContent(getSection(srcSolutionSection));
						}
						pattern.addContent(section);
					} else {
						pattern.addContent(getSection(srcSection));
					}
				}

				desDoc.getRootElement().addContent(pattern);

			}
		}

		XMLOutputter xmlOutput = new XMLOutputter();

		// display nice nice
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(desDoc, new FileWriter("src/securitypatterns/res/security_patterns.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("File Saved!");
	}

	private List<Element> getBookmark() {
		List<Element> bookmark;

		String query = "/TaggedPDF-doc/bookmark-tree/bookmark";
		XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());

		bookmark = xpe.evaluate(srcDoc);

		return bookmark;
	}

	private Element getSection(Element srcSection) {
		Element el = new Element(srcSection.getAttributeValue("title").replace(" ", "_"));

		int sectionId = Integer
				.parseInt(srcSection.getChild("destination").getAttributeValue("structID").split("_")[1]);

		String query = "//*[preceding-sibling::Figure[@id='LinkTarget_" + sectionId
				+ "'] and following-sibling::Figure[@id='LinkTarget_" + (sectionId + 1) + "']]";
		XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());

		List<Element> srcEls = xpe.evaluate(srcDoc);

		Element content = new Element("Content");

		for (Element srcEl : srcEls) {
			addElement(content, srcEl);
		}

		el.addContent(content);

		return el;
	}

	private Element getKnownUses(Element srcSection) {
		Element el = new Element(srcSection.getAttributeValue("title").replace(" ", "_"));

		int sectionId = Integer
				.parseInt(srcSection.getChild("destination").getAttributeValue("structID").split("_")[1]);

		String query = "//*[preceding-sibling::Figure[@id='LinkTarget_" + sectionId + "']]";
		XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());

		List<Element> srcEls = xpe.evaluate(srcDoc);

		Element content = new Element("Content");

		for (Element srcEl : srcEls) {
			addElement(content, srcEl);
		}

		el.addContent(content);

		return el;
	}

	private void addElement(Element el, Element srcEl) {
		switch (srcEl.getName()) {
			case "P":
				el.addContent(new Element("p").setText(srcEl.getValue()));
				break;
			case "L":
				Element ul = new Element("ul");
	
				List<Element> list = srcEl.getChildren("LI");
	
				for (Element li : list) {
					ul.addContent(new Element("li").setText(li.getValue()));
				}
				el.addContent(ul);
				break;
			case "ImageData":
				Element img = new Element("img");
				img.setAttribute(srcEl.getAttribute("src"));
				el.addContent(img);
				break;
		}

	}

	public static void main(String[] args) {
		new SecurrityPatternExtractor();
	}
}
