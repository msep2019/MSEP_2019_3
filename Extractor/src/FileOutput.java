import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.*;

public class FileOutput {
	public static void atWriter(ArrayList<String> anntTypes, ArrayList<String> checkColumn) throws IOException{
		System.out.println("Writing output file..");
		String url = "jdbc:mysql://localhost:3306/sossec?serverTimezone=UTC" ;
		Connection con ;
		Statement stmt ;
		String query = "select id, feature from sossec_tbl1" ;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection(url,"root","crazyThomas21");
			stmt = con.createStatement();
			stmt.executeUpdate("delete from sossec_tbl1");
			for(int x=0; x<=anntTypes.size(); x++) {					
				stmt.executeUpdate("INSERT INTO sossec_tbl1(sossec_id,sossec_feature) VALUES" +"("+x+","+anntTypes.get(x)+");");				
			}
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				int n = rs.getInt("sossec_id");
				String t = rs.getString("sossec_feature");
				System.out.println(n+" "+t);
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

