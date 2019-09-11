import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ExcelReaderXmlWriter {
    public static void writeXml() {
        try {
            InputStream inputStream = new FileInputStream("log/output.xlsx");
            File file = new File("log/annotations.xml");
            
            FileOutputStream foS = new FileOutputStream(file);// get input stream
            Document doc = readExcel(inputStream);
            Format format = Format.getCompactFormat().setEncoding("UTF-8").setIndent("");
            XMLOutputter XMLOut = new XMLOutputter(format);// changing line after each element,set indentation
            XMLOut.output(doc, foS);
            foS.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Selected file can't be found!");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private static Document readExcel(InputStream inputStream) {
    	Element root = new Element("profileApplication");
    	Document doc = new Document(root);
    	
    	//add level 0 tags
    	Element opaqueAction = new Element("OpaqueAction");
    	Element controlFlow = new Element("ControlFlow");
    	Element constraint = new Element("Constraint");
    	Element dataStorenode = new Element("DataStoreNode");   
    	
    	try {
    		
    		@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
    		XSSFSheet sheet = workbook.getSheet("Outputsheet");
    		
    		//get number of rows
    		System.out.println("Numbers of Row:"+sheet.getLastRowNum());
    		
    		// add level 1 tags
    		for(int x = 1; x < 1403; x++) {
    			XSSFRow row = sheet.getRow(x);
    			if (row == null) {
    				continue;
    			}

    			String tagAgent = getTag(0); 
    			String tagaBehaviour = getTag(1);
				Element agent = new Element(tagAgent);
				Element aBehaviour = new Element(tagaBehaviour);
				XSSFCell cellAgent = row.getCell(0);
				XSSFCell cellaBehaviour = row.getCell(1);
				
				if(cellAgent == null) {
					agent.setText("");
					continue;
				}else {
					String cellValue = "";
					cellValue = cellAgent.getStringCellValue();
					agent.setText(cellValue);
					cellValue = cellaBehaviour.getStringCellValue();
					aBehaviour.setText(cellValue);
				}			
				opaqueAction.addContent(agent);
				opaqueAction.addContent(aBehaviour);
    		}
    		
    		// add level 1 tags
    		for(int x = 1; x < 349; x++) {
    			XSSFRow row = sheet.getRow(x);
    			if (row == null) {
    				continue;
    			}

    			String tagReceiver = getTag(4);
    			String tagSender = getTag(5);
				Element receiver = new Element(tagReceiver);
				Element sender = new Element(tagSender);
				XSSFCell cellReceiver = row.getCell(4);
				XSSFCell cellSender = row.getCell(5);
				if(cellReceiver == null) {
					receiver.setText("");
					continue;
				}else {
					String cellValue = "";
					cellValue = cellReceiver.getStringCellValue();
					receiver.setText(cellValue);
					cellValue = cellSender.getStringCellValue();
					sender.setText(cellValue);
				}
				controlFlow.addContent(receiver);
				controlFlow.addContent(sender); 
    		}
    		
    		// add level 1 tags
    		for(int x = 1; x < 353; x++) {
    			XSSFRow row = sheet.getRow(x);
    			if (row == null) {
    				continue;
    			}	

    			String tagVulnerability = getTag(6);
    			String tagType = getTag(7);
				Element vulnerability = new Element(tagVulnerability);
				Element type = new Element(tagType);
				XSSFCell cellVulnerability = row.getCell(6);
				XSSFCell cellType = row.getCell(7);
				if(cellVulnerability  == null) {
					vulnerability.setText("");
					continue;
				}else {
					String cellValue = "";
					cellValue = cellVulnerability .getStringCellValue();
					vulnerability.setText(cellValue);
					cellValue = cellType.getStringCellValue();
					type.setText(cellValue);
				}
				constraint.addContent(vulnerability); 
				constraint.addContent(type); 
    		}
    		
    		// add level 1 tags
    		for(int x = 1; x < 874; x++) {
    			XSSFRow row = sheet.getRow(x);
    			if (row == null) {
    				continue;
    			}	

    			String tagBehaviour = getTag(2);
    			String tagbVulnerability = getTag(3);
				Element behaviour = new Element(tagBehaviour);
				Element bVulnerability = new Element(tagbVulnerability);
				XSSFCell cellBehaviour = row.getCell(6);
				XSSFCell cellbVulnerability = row.getCell(7);
				//System.out.println(row.getCell(6));
				if(cellBehaviour  == null) {
					behaviour.setText("");
					continue;
				}else {
					String cellValue = "";
					cellValue = cellBehaviour .getStringCellValue();
					behaviour.setText(cellValue);
					cellValue = cellbVulnerability.getStringCellValue();
					bVulnerability.setText(cellValue);
				}
				dataStorenode.addContent(behaviour);
				dataStorenode.addContent(bVulnerability);
    		}
    		
    		root.addContent(opaqueAction);
    		root.addContent(controlFlow);
    		root.addContent(constraint);
    		root.addContent(dataStorenode);
    		
    	}catch(Exception e){
    		
    	}
        try {
            inputStream.close();
        } catch (IOException e) {
        
        }
        return doc;
    }
    
    private static String getTag(int key) {
    	String tag ="";
    	switch(key) {
    	case 0:
    		tag = "Agent";
    		break;
    	case 1:
    		tag = "Behaviour";
    		break;
    	case 2:
    		tag = "Behaviour";
    		break;
    	case 3:
    		tag = "Vulnerability";
    		break;
    	case 4:
    		tag = "Receiver";
    		break;
    	case 5:
    		tag = "Sender";
    		break;
    	case 6:
    		tag = "Vulnerability";
    		break;
    	case 7:
    		tag = "Type";
    		break;
    	default:
    	}
    	return tag;
    }
}
