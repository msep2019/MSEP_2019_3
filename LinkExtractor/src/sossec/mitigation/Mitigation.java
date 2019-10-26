package sossec.mitigation;

import java.util.ArrayList;

import sossec.capec.CAPECItem;

public class Mitigation {
	public String description;
	public String sourceDB;
	public ArrayList<String> keywords = new ArrayList<>();
	public ArrayList<String> disabledKeywords = new ArrayList<>();
	public ArrayList<SecurityPattern> securityPattern = new ArrayList<>();
}
