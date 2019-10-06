package sossec.cve;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import com.jpetrak.gate.stringannotation.extendedgazetteer.ExtendedGazetteer;

import gate.Factory;
import gate.FeatureMap;
import sossec.XMLHelper;

public class CVEHelper {

	public String[] xmlFiles = { "src/databases/CVE_desc.xml" };

	public String getItemContent(String Id) {
		String result = "";

		for (String file : xmlFiles) {
			Document document = XMLHelper.getSAXParsedDocument(file);

			Element rootNode = document.getRootElement();

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
	
	public File generateCVEGazetteer(ArrayList<String> keywords) {
		
		File tempDir = new File("tmp");
        if (!tempDir.exists()) {
            if (tempDir.mkdir()) {
                System.out.println("Temp directory is created!");
            } else {
                System.out.println("Failed to create temp directory!");
                System.exit(1);
            }
        }
        
		File fileList = null;
		File fileDef = null;
		
		try {
			String fileListName = "cve_keywords" + UUID.randomUUID().toString() + ".lst";
			fileList = new File("tmp/" + fileListName);
			
			PrintStream fileListStream = new PrintStream(fileList);
			for (String keyword : keywords) { 		      
				fileListStream.println(keyword); 		
		    }
			
			fileListStream.close();
			
			//System.out.println(fileList.getAbsolutePath());
			
			/*
			FileReader fr = new FileReader(fileList);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
			    //process the line
			    System.out.println(line);
			}
			*/
			
			String fileDefName = "cve_keywords" + UUID.randomUUID().toString() + ".def"; 
			fileDef = new File("tmp/" + fileDefName);
			
			PrintStream fileDefStream = new PrintStream(fileDef);
			
			fileDefStream.print(fileListName + ":CVE:Keyword:en");
			
			fileDefStream.close();
			
			//fileList.deleteOnExit();
			//fileDef.deleteOnExit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileDef;
	}
}
