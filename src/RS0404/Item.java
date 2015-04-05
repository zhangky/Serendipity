package RS0404;

import java.util.ArrayList;
import java.util.HashSet;

class Item{
	private int itemId;
	private String itemGeohash;
	private int itemCategory;
	
	private double actionSum[] = null;
	private ArrayList<HashSet<Integer>> userSum = null;
	private ArrayList<HashSet<Integer>> actionDate = null;
	
	public Item(int itemId, String itemGeohash, int itemCategory) {
		super();
		this.itemId = itemId;
		this.itemGeohash = itemGeohash;
		this.itemCategory = itemCategory;
	}

	public Item(int itemId){
		this.itemId = itemId;
		actionSum = new double[4];
		userSum = new ArrayList<HashSet<Integer>>();
		actionDate = new ArrayList<HashSet<Integer>>();
		for(int i=0;i<4;++i){
			userSum.add(new HashSet<Integer>());
			actionDate.add(new HashSet<Integer>());
		}
	}
	
	public String toString() {
		String re = ""+itemId;
		for(int i=0;i<4;++i){
			re += "\t"+actionSum[i];
			re += "\t"+actionSum[i]/userSum.get(i).size();
		}
		re += "\n";
		return re;
	}
	
	public int getFirstDate(int actionType){
		int firstDate = Integer.MAX_VALUE;
		for(int date: actionDate.get(actionType)){
			if(date<firstDate){
				firstDate = date;
			}
		}
		return firstDate;
	}
	
	public int getLastDate(int actionType){
		int lastDate = 0;
		for(int date: actionDate.get(actionType)){
			if(date>lastDate){
				lastDate = date;
			}
		}
		return lastDate;
	}
	
	public double getActionSum(int actionType){
		return actionSum[actionType];
	}
	
	public double getUserSum(int actionType){
		return userSum.get(actionType).size();
	}
	
	public double getActionAver(int actionType){
		if(userSum.get(actionType).isEmpty()){
			return Double.MAX_VALUE;
		}
		else{
			return actionSum[actionType]/userSum.get(actionType).size();
		}
	}
	
	public void increActionSum(int actionType){
		++actionSum[actionType];
	}
	
	public void increUserSum(int actionType, int userId){
		userSum.get(actionType).add(userId);
	}
	
	public void addDate(int actionType, int date){
		actionDate.get(actionType).add(date);
	}
	
//	public double click2buy(){
//		if(getActionSum(firstRec.BUY)==0){
//			return Double.MAX_VALUE;
//		}
//		else{
//			return getActionSum(firstRec.CLICK)/getActionSum(firstRec.BUY);
//		}
	// }
}