package sossec;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Element;

import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import sossec.capec.CAPECHelper;
import sossec.capec.CAPECItem;
import sossec.keyword.Keyword;
import sossec.mitigation.Mitigation;

public class GetMitigations {

	public static void main(String[] args) {
		// BasicConfigurator.configure();
		// ArrayList<CAPECMitigation> getIndirectCAPECList = new
		// ArrayList<CAPECMitigation>();
		// CAPECMitigation capec = new CAPECMitigation("ID 100");
		// String log4jConfPath = "../Path/to/log4j.properties";
		// PropertyConfigurator.configure(log4jConfPath);
		// org.apache.log4j.BasicConfigurator.configure();
		CAPECHelper helper = new CAPECHelper();
		Scanner sc = new Scanner(System.in);
		sc.useDelimiter("/n");
		System.out.println("Please input CAPEC_ID(example:100):");
		String capecID = sc.nextLine();
		// String capecID = "100";
		CAPECItem capec = new CAPECItem(capecID);
		List<Mitigation> mitigations = capec.getMitigations();
		System.out.println("CAPEC_ID:" + capecID + "\n");
//		for (Element mitigation : mitigations) {
//			ArrayList<String> keywords = new ArrayList<>();
//			try {
//				keywords = Keyword.processDocs(mitigation.getValue());
//			} catch (ResourceInstantiationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			System.out.println("\n\nCAPEC mitigations: " + mitigation.getValue() + "\n" + keywords);
//
//			// System.out.println("CAPEC mitigation: " + keywords);
//		}
		
		sc.close();
	}
}
