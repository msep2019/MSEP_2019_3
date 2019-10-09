package sossec.cwe;
import java.util.ArrayList;

import sossec.capec.CAPECItem;
//for mitigation
public class CWEItem2 {
	public String id;
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<CAPECItem> listCAPEC = new ArrayList<>();

	public CWEItem2(String id) {
		this.id = id;
	}
}
