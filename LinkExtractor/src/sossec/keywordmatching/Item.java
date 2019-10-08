package sossec.keywordmatching;

import java.util.Comparator;

public class Item {
	public String id;
	public String name;
	public int matchingCount;
	
	Item(String id, String name, int matchingCount) {
		this.id = id;
		this.name = name;
		this.matchingCount = matchingCount;
	}
}

class SortByMatchingCount implements Comparator<Item> 
{ 
	@Override
	public int compare(Item o1, Item o2) {
		// TODO Auto-generated method stub
		return o2.matchingCount - o1.matchingCount; 
	} 
} 