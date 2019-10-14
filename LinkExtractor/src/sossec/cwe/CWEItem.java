package sossec.cwe;
import java.util.ArrayList;

import sossec.capec.CAPECItem;

public class CWEItem {
	public String id;
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<CAPECItem> listCAPEC = new ArrayList<>();

	public CWEItem(String id) {
		this.id = id;
	}
}
