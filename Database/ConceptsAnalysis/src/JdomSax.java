import java.io.File;
import java.util.List;
 
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
 
public class JdomSax {
 
	public static void main(String[] args) {
		//saxbuilder
		SAXBuilder sax = new SAXBuilder();
		Document doc;
		try {
			//get xml file
			doc = sax.build(new File("D:/book.xml"));
			//get root node
			Element bookstore = doc.getRootElement();
			//get data under root tree
			List<Element> books = bookstore.getChildren();
			for(Element book: books) {
				//get attributes
				book.getAttributeValue("id");
				List<Element> bookitems = book.getChildren();
				for(Element item:bookitems) {
					System.out.println(item.getName()); 
					System.out.println(item.getText());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
 
}