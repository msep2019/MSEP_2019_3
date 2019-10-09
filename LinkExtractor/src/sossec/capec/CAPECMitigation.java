package sossec.capec;

import java.util.ArrayList;

public class CAPECMitigation {
	public String id;
	public ArrayList<String> keywords = new ArrayList<>();

	public CAPECMitigation(String id) {
		this.id = id;
	}
}