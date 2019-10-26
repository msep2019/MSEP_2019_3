package sossec.capec;

import java.util.ArrayList;

import sossec.mitigation.Mitigation;

public class CAPECItem {
	public String id;
	public String name;
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<Mitigation> mitigations = new ArrayList<>();
	public int matching = -1;

	public CAPECItem(String id) {
		this.id = id;
	}
	
	public CAPECItem(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		String result = "<html>";
		
		if (matching >= 1) {
			result += "[" + matching + "] "; 
		}
		
		result += "CAPEC <font color=\"green\">" + this.id + "</font> " + this.name + "</html>";
		
		return result;
		
	}
}