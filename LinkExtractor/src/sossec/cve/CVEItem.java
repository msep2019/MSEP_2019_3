package sossec.cve;

import java.util.ArrayList;

import sossec.cwe.CWEItem;

public class CVEItem {
	public String id;
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<CWEItem> listCWE = new ArrayList<>();

	public CVEItem(String id) {
		this.id = id;
	}
}