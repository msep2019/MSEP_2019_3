package sossec.capec;

import java.util.ArrayList;

public class CAPECItem {
	public String id;
	public String name;
	public ArrayList<String> keywords = new ArrayList<>();

	public CAPECItem(String id) {
		this.id = id;
	}
	
	public CAPECItem(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return "<html> CAPEC <font color=\"green\">" + this.id + "</font> " + this.name + "</html>";
	}
}