package RS;

import java.util.ArrayList;
import java.util.HashSet;

public class Item{
	private int itemId;
	private String itemGeohash;
	private int itemCategory;
	
	public Item(){
		
	}
	
	public Item(int itemId, String itemGeohash, int itemCategory) {
		super();
		this.itemId = itemId;
		this.itemGeohash = itemGeohash;
		this.itemCategory = itemCategory;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemGeohash() {
		return itemGeohash;
	}

	public void setItemGeohash(String itemGeohash) {
		this.itemGeohash = itemGeohash;
	}

	public int getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(int itemCategory) {
		this.itemCategory = itemCategory;
	}

	
}