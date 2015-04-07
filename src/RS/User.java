package RS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
	private int userID;
	private ArrayList<Record> trainRecords;
	private HashSet<Integer> realItems;
	private HashSet<Integer> recItems;
	// private HashSet<Integer> subItems;
	private HashSet<Integer> itemsID;

	public User() {
		super();
		this.trainRecords = new ArrayList<Record>();
		this.realItems = new HashSet<Integer>();
		this.recItems = new HashSet<Integer>();
		this.itemsID = new HashSet<Integer>();
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public double getFeatureValue(String feature, Item item) {
		if (feature.equals("buyNum")){
			return this.getBuyNum(item);
		}
		return 0;
	}
	
	public double getBuyNum(Item item){
		double buyNum = 0;
		for (Record record:this.trainRecords){
			if (record.getBehaviorType()==Info.BUY){
				buyNum++;
			}
		}
		return buyNum;
	}

	public String isBuy(int itemID) {
		if (this.realItems.contains(itemID)){
			return "1";
		}
		else{
			return "0";
		}
	}

	public void updateItemsID() {
		for (Record record : this.trainRecords) {
			if (!this.itemsID.contains(record.getItemId())) {
				this.itemsID.add(record.getItemId());
			}
		}
	}

	public HashSet<Integer> getItemsID() {
		return itemsID;
	}

	public void setItemsID(HashSet<Integer> itemsID) {
		this.itemsID = itemsID;
	}

	public void recommend(HashSet<Integer> subItemsID, int preHours) {
		this.getRecItems().clear();

		// for (Record record : this.trainRecords) {
		// if (record.getBehaviorType() == Info.BUY
		// && subItemsID.contains(record.getItemId())) {
		// this.recItems.add(record.getItemId());
		// }
		// }

		// HashSet<Integer> boughtItem = new HashSet<Integer>();
		// HashMap<Integer, Boolean> can = new HashMap<Integer, Boolean>();
		// for (Record record : this.trainRecords) {
		// if (record.getTime().after(new
		// Time(Info.END_TRAIN.getTime()-(long)preHours*3600*1000))
		// && subItemsID.contains(record.getItemId())
		// && record.getBehaviorType()==Info.BUY
		// ) {
		// // this.recItems.add(record.getItemId());
		// boughtItem.add(record.getItemId());
		// }
		// }
		//
		// for (Record record : this.trainRecords) {
		// if (record.getTime().after(new
		// Time(Info.END_TRAIN.getTime()-(long)preHours*3600*1000))
		// && subItemsID.contains(record.getItemId())
		// && (record.getBehaviorType()==Info.CART
		// || record.getBehaviorType()==Info.COLLECT)
		// && !boughtItem.contains(record.getItemId())
		// ) {
		// this.recItems.add(record.getItemId());
		// }
		// }

	}

	public HashSet<Integer> getHit() {
		HashSet<Integer> hit = (HashSet<Integer>) this.realItems.clone();
		hit.retainAll(this.recItems);
		return hit;
	}

	public ArrayList<Record> getTrainRecords() {
		return trainRecords;
	}

	public void setTrainRecords(ArrayList<Record> trainRecords) {
		this.trainRecords = trainRecords;
	}

	public HashSet<Integer> getRealItems() {
		return realItems;
	}

	public void setRealItems(HashSet<Integer> realItems) {
		this.realItems = realItems;
	}

	public HashSet<Integer> getRecItems() {
		return recItems;
	}

	public void setRecItems(HashSet<Integer> recItems) {
		this.recItems = recItems;
	}

}
