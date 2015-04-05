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

	public User(){
		super();
		this.trainRecords = new ArrayList<Record>();
		this.realItems = new HashSet<Integer>();
		this.recItems = new HashSet<Integer>();
	}
	
	public User(ArrayList<Record> trainRecords, HashSet<Integer> realItems,
			HashSet<Integer> recItems) {
		super();
		this.trainRecords = trainRecords;
		this.realItems = realItems;
		this.recItems = recItems;
	}
	
	public int countBuy(){
		int i=0;
		for (Record record:this.trainRecords){
			if (record.getBehaviorType()==Info.BUY)
				i++;
		}
		return i;
	}
	
	public List<SortedItem> topBuyItem(){
		HashMap<Integer, SortedItem> itemBuy = new HashMap<Integer, SortedItem>();
		for (Record record:this.trainRecords){
			if (record.getBehaviorType()==Info.BUY||
					record.getBehaviorType()==Info.CART||
					record.getBehaviorType()==Info.FAVORITE||
					record.getBehaviorType()==Info.CLICK){
				if (itemBuy.containsKey(record.getItemId())){
					itemBuy.get(record.getItemId()).weight +=1;
				}
				else{
					SortedItem item = new SortedItem(record.getItemId(),1);
					itemBuy.put(record.getItemId(), item);
				}
			}
		}
		List<SortedItem> buyItem = new ArrayList<SortedItem>(itemBuy.values());
		
		Collections.sort(buyItem);
		
		return buyItem;
	}
	
	public void recommend(){
		int num = (int) (this.countBuy()/(Info.TOTALBUY+0.0)*Info.SUGGESTBUY);
//		List<SortedItem> buyItem = this.topBuyItem();
//		for (SortedItem st:buyItem){
//			this.recItems.add(st.itemId);
//		}
		
		
		if (num>0){
			List<SortedItem> buyItem = this.topBuyItem();
			for (int i=0; i<num; i++){
				if (i<buyItem.size()){
//					System.out.println(buyItem.get(i).itemId);
					this.recItems.add(buyItem.get(i).itemId);
				}
			}
		}
	}
	
	public void addTrainRecords(Record record){
		this.trainRecords.add(record);
	}
	
	public void addRealItems(int itemId){
		this.realItems.add(itemId);
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
	
	public ArrayList<Integer> result(){
		ArrayList<Integer> re = new ArrayList<Integer>();
		re.add(this.recItems.size());
		re.add(this.realItems.size());
		HashSet<Integer> tmp = (HashSet<Integer>) this.realItems.clone();
		tmp.retainAll(this.recItems);
		re.add(tmp.size());
		return re;
	}
	
	// private HashMap<Integer, Double> brandWeights;
	// private HashMap<Integer, Double> brandWeights2;
	// private HashMap<Integer, Double> userSimi;

	// public User(){
	// trainRecords = new ArrayList<Record>();
	// brandWeights = new HashMap<Integer, Double>();
	// brandWeights2 = new HashMap<Integer, Double>();
	// userSimi = new HashMap<Integer, Double>();
	// realItems = new HashSet<Integer>();
	// recItems = new HashSet<Integer>();
	// }
	//
	// public int getActionSum(int actionType){
	// int count = 0;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType){
	// ++count;
	// }
	// }
	// return count;
	// }
	//
	// public int getUniqueBrands(int actionType){
	// int count = 0;
	// for(int brandId:getWeightedBrands()){
	// if(hasBrandAction(actionType, brandId)){
	// ++count;
	// }
	// }
	// return count;
	// }
	//
	// public int getBrandActionSum(int actionType, int brandId){
	// int count = 0;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType && record.getBrandId()==brandId){
	// ++count;
	// }
	// }
	// return count;
	// }
	//
	// public double getActionTimedSum(int actionType){
	// double sum = 0;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType){
	// sum += 1.0/(firstRec.endTrain - record.getActionDate() + 11);
	// }
	// }
	// return sum;
	// }
	//
	// public double getBrandActionTimedSum(int actionType, int brandId){
	// double sum = 0;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType && record.getBrandId()==brandId){
	// sum += 1.0/(firstRec.endTrain - record.getActionDate() + 11);
	// }
	// }
	// return sum;
	// }
	//
	// public int getSpanActionSum(int actionType, int span){
	// int count = 0;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType &&
	// record.getActionDate()>firstRec.endTrain-span){
	// ++count;
	// }
	// }
	// return count;
	// }
	//
	// public int getSpanBrandActionSum(int actionType, int brandId, int span){
	// int count = 0;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType && record.getBrandId()==brandId&&
	// record.getActionDate()>firstRec.endTrain-span){
	// ++count;
	// }
	// }
	// return count;
	// }
	//
	// public int getRealSize(){
	// return realItems.size();
	// }
	//
	// public int getRecSize(){
	// return recItems.size();
	// }
	//
	// public int getHitSize(){
	// HashSet<Integer> hitBrands = new HashSet<Integer>();
	// hitBrands.addAll(realItems);
	// hitBrands.retainAll(recItems);
	// return hitBrands.size();
	// }
	//
	// public HashSet<Integer> getRecBrands(){
	// return recItems;
	// }
	//
	// public HashSet<Integer> getRealBrands(){
	// return realItems;
	// }
	//
	// public Set<Integer> getWeightedBrands(){
	// return brandWeights.keySet();
	// }
	//
	// public Set<Integer> getWeightedBrands2(){
	// return brandWeights2.keySet();
	// }
	//
	// public Set<Integer> getTopBrands(int k){
	// HashSet<Integer> topBrands = new HashSet<Integer>();
	// ArrayList<SortedBrand> sortedBrands = new ArrayList<SortedBrand>();
	// for(int brandId:brandWeights.keySet()){
	// sortedBrands.add(new SortedBrand(brandId,brandWeights.get(brandId)));
	// }
	// Collections.sort(sortedBrands);
	// int count = 0;
	// for(SortedBrand sortedBrand:sortedBrands){
	// if(count==k){
	// break;
	// }
	// ++count;
	// topBrands.add(sortedBrand.brandId);
	// }
	// return topBrands;
	// }
	//
	// public Set<Integer> getTopBrands2(int k){
	// HashSet<Integer> topBrands = new HashSet<Integer>();
	// ArrayList<SortedBrand> sortedBrands = new ArrayList<SortedBrand>();
	// for(int brandId:brandWeights2.keySet()){
	// sortedBrands.add(new SortedBrand(brandId,brandWeights2.get(brandId)));
	// }
	// Collections.sort(sortedBrands);
	// int count = 0;
	// for(SortedBrand sortedBrand:sortedBrands){
	// ++count;
	// topBrands.add(sortedBrand.brandId);
	// if(count==k){
	// break;
	// }
	// }
	// return topBrands;
	// }
	//
	// public double getBrandWeight(int brandId){
	// if(brandWeights.containsKey(brandId)){
	// return brandWeights.get(brandId);
	// }
	// else{
	// return (double) 0;
	// }
	// }
	//
	// public double getBrandWeight2(int brandId){
	// if(brandWeights2.containsKey(brandId)){
	// return brandWeights2.get(brandId);
	// }
	// else{
	// return (double) 0;
	// }
	// }
	//
	// public void increBrandWeight(int brandId, double increment){
	// if(!brandWeights.containsKey(brandId)){
	// brandWeights.put(brandId, (double) 0);
	// }
	// brandWeights.put(brandId, brandWeights.get(brandId)+increment);
	// }
	//
	// public void increBrandWeight2(int brandId, double increment){
	// if(!brandWeights2.containsKey(brandId)){
	// brandWeights2.put(brandId, (double) 0);
	// }
	// brandWeights2.put(brandId, brandWeights2.get(brandId)+increment);
	// }
	//
	// public double getuserSimi(int userId){
	// return userSimi.get(userId);
	// }
	//
	// public void setUserSimi(int userId, double value){
	// userSimi.put(userId, value);
	// }
	//
	// public ArrayList<Record> getTrainRecords(){
	// return trainRecords;
	// }
	//
	// public boolean hasBrandAction(int actionType, int brandId){
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType && record.getBrandId()==brandId){
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public boolean hasAction(int actionType){
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType){
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// public int firstActionDate(int actionType){
	// int firstDate = Integer.MAX_VALUE;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType &&
	// record.getActionDate()<firstDate){
	// firstDate = record.getActionDate();
	// }
	// }
	// return firstDate;
	// }
	//
	// public int firstBrandActionDate(int actionType, int brandId){
	// int firstDate = Integer.MAX_VALUE;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType && record.getActionDate()<firstDate
	// && record.getBrandId()==brandId){
	// firstDate = record.getActionDate();
	// }
	// }
	// return firstDate;
	// }
	//
	// public int lastActionDate(int actionType){
	// int lastDate = 0;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType &&
	// record.getActionDate()>lastDate){
	// lastDate = record.getActionDate();
	// }
	// }
	// return lastDate;
	// }
	//
	// public int lastBrandActionDate(int actionType, int brandId){
	// int lastDate = 0;
	// for(Record record:trainRecords){
	// if(record.getActionType()==actionType && record.getActionDate()>lastDate
	// && record.getBrandId()==brandId){
	// lastDate = record.getActionDate();
	// }
	// }
	// return lastDate;
	// }
	//
	// public double averClickBuySpan(){
	// double span = 0;
	// int count = 0;
	// for(int brandId:getWeightedBrands()){
	// int clickDate = firstBrandActionDate(firstRec.CLICK,brandId);
	// int buyDate = firstBrandActionDate(firstRec.BUY,brandId);
	// if(buyDate<Integer.MAX_VALUE && clickDate<=buyDate){
	// span += buyDate-clickDate;
	// ++count;
	// }
	// }
	// return span/count;
	// }
	//
	// public void addTrainRecord(Record record){
	// trainRecords.add(record);
	// }
	//
	// public void addRealBrand(int brandId){
	// realItems.add(brandId);
	// }
	//
	// public void addRecBrand(int brandId){
	// recItems.add(brandId);
	// }
	//
	// public double click2buy(){
	// if(getActionSum(firstRec.BUY)==0){
	// return Double.MAX_VALUE;
	// }
	// else{
	// return getActionSum(firstRec.CLICK)/getActionSum(firstRec.BUY);
	// }
	// }
}
