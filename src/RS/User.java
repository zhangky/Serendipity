package RS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
	private ArrayList<Record> trainRecords;
	private HashSet<Integer> realItems;
	private HashSet<Integer> recItems;

	public User() {
		super();
		this.trainRecords = new ArrayList<Record>();
		this.realItems = new HashSet<Integer>();
		this.recItems = new HashSet<Integer>();
	}

	
	public void recommend(HashSet<Integer> subItemsID, int preHours) {
		this.getRecItems().clear();
		
//		for (Record record : this.trainRecords) {
//			if (record.getBehaviorType() == Info.BUY
//					&& subItemsID.contains(record.getItemId())) {
//				this.recItems.add(record.getItemId());
//			}
//		}
		HashSet<Integer> boughtItem = new HashSet<Integer>();
		for (Record record : this.trainRecords) {
			if (record.getTime().after(new Time(Info.END_TRAIN.getTime()-(long)preHours*3600*1000))
					&& subItemsID.contains(record.getItemId())
					&& record.getBehaviorType()==Info.BUY
					) {
//				this.recItems.add(record.getItemId());
				boughtItem.add(record.getItemId());
			}
		}
		
		for (Record record : this.trainRecords) {
			if (record.getTime().after(new Time(Info.END_TRAIN.getTime()-(long)preHours*3600*1000))
					&& subItemsID.contains(record.getItemId())
					&& record.getBehaviorType()==Info.CART
					&& !boughtItem.contains(record.getItemId())
					) {
				this.recItems.add(record.getItemId());
			}
		}
		
		
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
