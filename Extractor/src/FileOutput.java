import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileOutput {
	public static void atWriter(ArrayList<String> anntTypes, ArrayList<String> checkColumn) throws IOException{
		System.out.println("Writing output file..");
		
		FileOutputStream xlsFileStream = new FileOutputStream(new File("log/output.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Outputsheet");
		sheet.setDefaultColumnWidth(30);
		
		for(int x=0; x<anntTypes.size(); x++){
			Row row = sheet.createRow(x);
			Cell cell = row.createCell(0);
			Cell tag = row.createCell(1);
			cell.setCellValue(anntTypes.get(x));
			tag.setCellValue(checkColumn.get(x));
			if (anntTypes.get(x) == "")
				cell.setCellValue("null");
			if (checkColumn.get(x) == "")
				cell.setCellValue("null");
		}
		
		// write sheet in file
		workbook.write(xlsFileStream);
		xlsFileStream.close();
	}
}

