package sossec.cwe;

import java.util.ArrayList;

import sossec.capec.CAPECItem;

public class TestDirect {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<CAPECItem> getDirectCAPECList = new ArrayList<CAPECItem>();
		CWEItem cwe = new CWEItem("CVE-2003-0981");

		getDirectCAPECList = cwe.getDirectCAPECList();

		System.out.println(getDirectCAPECList.size());
	}

}
