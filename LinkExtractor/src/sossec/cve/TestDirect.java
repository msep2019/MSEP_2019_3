package sossec.cve;

import java.util.ArrayList;

import sossec.cwe.CWEItem;

public class TestDirect {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<CWEItem> getDirectCWEList = new ArrayList<CWEItem>();
		CVEItem cwe = new CVEItem("CVE-2003-0981");

		getDirectCWEList = cwe.getDirectCWEList();

		System.out.println(getDirectCWEList.size());
	}
}
