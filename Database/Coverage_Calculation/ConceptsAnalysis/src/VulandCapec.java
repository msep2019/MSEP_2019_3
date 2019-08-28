import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class VulandCapec {
	public static void main(String[] args) {
		InputStream input;
		String cveid = null;
		System.out.println("Please enter the CVE id to begin: ");
		
    	try {
    		Scanner scan = new Scanner(System.in);
    		if(scan.hasNext()) {
    			cveid = scan.next();
    		}
    		scan.close();
			input = new FileInputStream("xml/Research(1000).xml");
			SearchDirectlinks.directLinks(cveid,input);			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
