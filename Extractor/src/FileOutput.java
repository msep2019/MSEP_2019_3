import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileOutput {
	public static void atWriter(ConditionRule[] conditionRules, MessageRule[] messageRules, BehaviourRule[] behaviourRules, AgentRule[] agentRules) throws IOException{
		System.out.println("Writing output file...");
		
		FileOutputStream xlsFileStream = new FileOutputStream(new File("log/output.xlsx"));
		
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Outputsheet");
		sheet.setDefaultColumnWidth(30);

		sheet.createRow(0).createCell(0).setCellValue("Agent");
		sheet.getRow(0).createCell(1).setCellValue("aBehaviour");
		sheet.getRow(0).createCell(2).setCellValue("Behaviour");
		sheet.getRow(0).createCell(3).setCellValue("bVulnerability");	
		sheet.getRow(0).createCell(4).setCellValue("Receiver");
		sheet.getRow(0).createCell(5).setCellValue("Sender");
		sheet.getRow(0).createCell(6).setCellValue("Vulnerability");
		sheet.getRow(0).createCell(7).setCellValue("vType");
		
		for(int x = 0; x < agentRules.length; x++){
			XSSFRow row = sheet.createRow(x+1);
			row.createCell(0).setCellValue(agentRules[x].getAgent());
			row.createCell(1).setCellValue(agentRules[x].getBehaviour());
		}

		for(int x = 0; x < behaviourRules.length; x++){
			XSSFRow row = sheet.getRow(x+1);
			row.createCell(2).setCellValue(behaviourRules[x].getBehaviour());
			row.createCell(3).setCellValue(behaviourRules[x].getVulnerability());
		}

		for(int x = 0; x < messageRules.length; x++){
			XSSFRow row = sheet.getRow(x+1);
			row.createCell(4).setCellValue(messageRules[x].getReceiver());
			row.createCell(5).setCellValue(messageRules[x].getSender());
		}

		for(int x = 0; x < conditionRules.length; x++){
			XSSFRow row = sheet.getRow(x+1);
			row.createCell(6).setCellValue(conditionRules[x].getVulnerability());
			row.createCell(7).setCellValue(conditionRules[x].getType());
		}
		
		// write sheet in file
		workbook.write(xlsFileStream);
		xlsFileStream.close();
	}
}

