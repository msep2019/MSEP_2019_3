package sossec;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

public class XMLHelper {
	public static Document getSAXParsedDocument(final String fileName)
    {
		SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try
        {
            document = builder.build(fileName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return document;
    }
}
