package sossec.capec;

import java.util.ArrayList;

public class CAPECItem {
	public String id;
	public ArrayList<String> keywords = new ArrayList<>();

	public CAPECItem(String id) {
		this.id = id;
	}
}