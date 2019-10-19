package sossec;
import java.net.MalformedURLException;
import java.util.ArrayList;

import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import sossec.capec.CAPECHelper2;
import sossec.capec.CAPECMitigation;
import sossec.cwe.CWEItem;
import sossec.keyword.Keyword;

public class testMitigations {

	public static void main(String[] args) {
		
		//ArrayList<CAPECMitigation> getIndirectCAPECList = new ArrayList<CAPECMitigation>();
		//CAPECMitigation capec = new CAPECMitigation("ID 100");
		
		CAPECHelper2 helper = new CAPECHelper2();
		
		String content = helper.getItemContent("100");
		ArrayList<String> keywords = new ArrayList<>();
		try {
			keywords = Keyword.processDocs(content);
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("CAPEC mitigations: "+ keywords);
	}

}
