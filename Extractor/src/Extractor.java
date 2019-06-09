import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;

import gate.creole.SerialAnalyserController;
import gate.util.GateException;


public class Extractor {
	public static void main(String[] args) throws GateException, IOException, URISyntaxException{
		
		long startTime = System.currentTimeMillis();
		
		BasicConfigurator.configure();
		
		//get location of all resources
		File log = new File("log/txt/Original simulation Log Files.txt");
		System.out.println("Number of processing files:"+1);
		
		//load processing resources
		SerialAnalyserController sac = ResourcesLoader.getResources();
		
		//arraylist to contain annotation types
		ArrayList<String> anntTypes = new ArrayList<String>();
		//map to check if anntType list is already present
		ArrayList<String> checkColumn = new ArrayList<String>();
		
		//call document analysing
		DocumentAnalyser.processDocs(sac,log,anntTypes,checkColumn,1);
		
		//call file writer to write the XML file
		FileOutput.atWriter(anntTypes,checkColumn);
		
		long endTime = System.currentTimeMillis();
		System.out.println("Total time in processing"+" sossec is"+(endTime-startTime)+"ms.");
	}
}
