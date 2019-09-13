//import java.io.File;
import java.io.FileOutputStream;
 
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
 
public class JDOMCreate {
 
	public static void main(String[] args) {
		Element bookstore = new Element("bookstore");
		
		Element book = new Element("book");
		book.setAttribute("id","1");
		book.addContent(new Element("name").setText("HarryPotter"));
		book.addContent(new Element("author").setText("Rowling"));
		book.addContent(new Element("year").setText("2018"));
		bookstore.addContent(book);
		
		//create new file
		Document doc = new Document(bookstore);
		//read format
		Format format = Format.getCompactFormat();
		format.setEncoding("UTF-8");
		//set indent into 4
		format.setIndent("    ");
		//build working factory
		XMLOutputter xmlout = new XMLOutputter(format);
		try {
			//send wrote file to factory
			xmlout.output(doc, new FileOutputStream("book2.xml"));
		} catch (Exception e) {
			// TODO: handle exception
		}
 
	}
}
 
