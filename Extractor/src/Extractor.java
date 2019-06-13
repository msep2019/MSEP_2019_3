import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
		
		//identify structs for annotation types
		ConditionRule[] conditionRules = new ConditionRule[352];
		MessageRule[] messageRules = new MessageRule[348];
		BehaviourRule[] behaviourRules = new BehaviourRule[874];
		AgentRule[] agentRules = new AgentRule[1402];
		
		//call document analysing
		DocumentAnalyser.processDocs(sac,log,conditionRules,messageRules,behaviourRules,agentRules,1);
		
		//call file writer to write the XML file
		FileOutput.atWriter(conditionRules,messageRules,behaviourRules,agentRules);
		ExcelReaderXmlWriter.writeXml();
		
		long endTime = System.currentTimeMillis();
		System.out.println("Total time in processing"+" SoSSec is"+(endTime-startTime)+"ms.");
	}
}
