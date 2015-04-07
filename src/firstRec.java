
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SPegasos;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

class SortedBrand implements Comparable<SortedBrand>{
	int brandId;
	double weight;
	
	SortedBrand(int brandId,double weight){
		this.brandId = brandId;
		this.weight = weight;
	}
	
	public String toString(){
		return weight+"\t"+brandId+"\n";
	}

	@Override
	public int compareTo(SortedBrand other) {
		if(this.weight<other.weight) return 1;
		if(this.weight>other.weight) return -1;
		return 0;
	}
}

class User {
	private ArrayList<Record> trainRecords;
	private HashMap<Integer, Double> brandWeights;
	private HashMap<Integer, Double> brandWeights2;
	private HashMap<Integer, Double> userSimi;
	private HashSet<Integer> realBrands;
	private HashSet<Integer> recBrands;
	
	public User(){
		trainRecords = new ArrayList<Record>();
		brandWeights = new HashMap<Integer, Double>();
		brandWeights2 = new HashMap<Integer, Double>();
		userSimi = new HashMap<Integer, Double>();
		realBrands = new HashSet<Integer>();
		recBrands = new HashSet<Integer>();
	}
	
	public int getActionSum(int actionType){
		int count = 0;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType){
				++count;
			}
		}
		return count;
	}
	
	public int getUniqueBrands(int actionType){
		int count = 0;
		for(int brandId:getWeightedBrands()){
			if(hasBrandAction(actionType, brandId)){
				++count;
			}
		}
		return count;
	}
	
	public int getBrandActionSum(int actionType, int brandId){
		int count = 0;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType && record.getBrandId()==brandId){
				++count;
			}
		}
		return count;
	}
	
	public double getActionTimedSum(int actionType){
		double sum = 0;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType){
				sum += 1.0/(firstRec.endTrain - record.getActionDate() + 11);
			}
		}
		return sum;
	}
	
	public double getBrandActionTimedSum(int actionType, int brandId){
		double sum = 0;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType && record.getBrandId()==brandId){
				sum += 1.0/(firstRec.endTrain - record.getActionDate() + 11);
			}
		}
		return sum;
	}
	
	public int getSpanActionSum(int actionType, int span){
		int count = 0;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType && record.getActionDate()>firstRec.endTrain-span){
				++count;
			}
		}
		return count;
	}
	
	public int getSpanBrandActionSum(int actionType, int brandId, int span){
		int count = 0;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType && record.getBrandId()==brandId&& record.getActionDate()>firstRec.endTrain-span){
				++count;
			}
		}
		return count;
	}
	
	public int getRealSize(){
		return realBrands.size();
	}
	
	public int getRecSize(){
		return recBrands.size();
	}
	
	public int getHitSize(){
		HashSet<Integer> hitBrands = new HashSet<Integer>();
		hitBrands.addAll(realBrands);
		hitBrands.retainAll(recBrands);
		return hitBrands.size();
	}
	
	public HashSet<Integer> getRecBrands(){
		return recBrands;
	}
	
	public HashSet<Integer> getRealBrands(){
		return realBrands;
	}
	
	public Set<Integer> getWeightedBrands(){
		return brandWeights.keySet();
	}
	
	public Set<Integer> getWeightedBrands2(){
		return brandWeights2.keySet();
	}
	
	public Set<Integer> getTopBrands(int k){
		HashSet<Integer> topBrands = new HashSet<Integer>();
		ArrayList<SortedBrand> sortedBrands = new ArrayList<SortedBrand>();
		for(int brandId:brandWeights.keySet()){
			sortedBrands.add(new SortedBrand(brandId,brandWeights.get(brandId)));
		}
		Collections.sort(sortedBrands);
		int count = 0;
		for(SortedBrand sortedBrand:sortedBrands){
			if(count==k){
				break;
			}
			++count;
			topBrands.add(sortedBrand.brandId);
		}
		return topBrands;
	}
	
	public Set<Integer> getTopBrands2(int k){
		HashSet<Integer> topBrands = new HashSet<Integer>();
		ArrayList<SortedBrand> sortedBrands = new ArrayList<SortedBrand>();
		for(int brandId:brandWeights2.keySet()){
			sortedBrands.add(new SortedBrand(brandId,brandWeights2.get(brandId)));
		}
		Collections.sort(sortedBrands);
		int count = 0;
		for(SortedBrand sortedBrand:sortedBrands){
			++count;
			topBrands.add(sortedBrand.brandId);
			if(count==k){
				break;
			}
		}
		return topBrands;
	}
	
	public double getBrandWeight(int brandId){
		if(brandWeights.containsKey(brandId)){
			return brandWeights.get(brandId);
		}
		else{
			return (double) 0;
		}
	}
	
	public double getBrandWeight2(int brandId){
		if(brandWeights2.containsKey(brandId)){
			return brandWeights2.get(brandId);
		}
		else{
			return (double) 0;
		}
	}
	
	public void increBrandWeight(int brandId, double increment){
		if(!brandWeights.containsKey(brandId)){
			brandWeights.put(brandId, (double) 0);
		}
		brandWeights.put(brandId, brandWeights.get(brandId)+increment);
	}
	
	public void increBrandWeight2(int brandId, double increment){
		if(!brandWeights2.containsKey(brandId)){
			brandWeights2.put(brandId, (double) 0);
		}
		brandWeights2.put(brandId, brandWeights2.get(brandId)+increment);
	}
	
	public double getuserSimi(int userId){
		return userSimi.get(userId);
	}
	
	public void setUserSimi(int userId, double value){
		userSimi.put(userId, value);
	}
	
	public ArrayList<Record> getTrainRecords(){
		return trainRecords;
	}
	
	public boolean hasBrandAction(int actionType, int brandId){
		for(Record record:trainRecords){
			if(record.getActionType()==actionType && record.getBrandId()==brandId){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasAction(int actionType){
		for(Record record:trainRecords){
			if(record.getActionType()==actionType){
				return true;
			}
		}
		return false;
	}
	
	public int firstActionDate(int actionType){
		int firstDate = Integer.MAX_VALUE;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType && record.getActionDate()<firstDate){
				firstDate = record.getActionDate();
			}
		}
		return firstDate;
	}
	
	public int firstBrandActionDate(int actionType, int brandId){
		int firstDate = Integer.MAX_VALUE;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType && record.getActionDate()<firstDate && record.getBrandId()==brandId){
				firstDate = record.getActionDate();
			}
		}
		return firstDate;
	}
	
	public int lastActionDate(int actionType){
		int lastDate = 0;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType && record.getActionDate()>lastDate){
				lastDate = record.getActionDate();
			}
		}
		return lastDate;
	}
	
	public int lastBrandActionDate(int actionType, int brandId){
		int lastDate = 0;
		for(Record record:trainRecords){
			if(record.getActionType()==actionType && record.getActionDate()>lastDate && record.getBrandId()==brandId){
				lastDate = record.getActionDate();
			}
		}
		return lastDate;
	}
	
	public double averClickBuySpan(){
		double span = 0;
		int count = 0;
		for(int brandId:getWeightedBrands()){
			int clickDate = firstBrandActionDate(firstRec.CLICK,brandId);
			int buyDate = firstBrandActionDate(firstRec.BUY,brandId);
			if(buyDate<Integer.MAX_VALUE && clickDate<=buyDate){
				span += buyDate-clickDate;
				++count;
			}
		}
		return span/count;
	}
	
	public void addTrainRecord(Record record){
		trainRecords.add(record);
	}
	
	public void addRealBrand(int brandId){
		realBrands.add(brandId);
	}
	
	public void addRecBrand(int brandId){
		recBrands.add(brandId);
	}
	
	public double click2buy(){
		if(getActionSum(firstRec.BUY)==0){
			return Double.MAX_VALUE;
		}
		else{
			return getActionSum(firstRec.CLICK)/getActionSum(firstRec.BUY);
		}
	}
}

class Record{
	private int userId;
	private int brandId;
	private int actionType;
	private int actionDate;
	
	public Record(int userId, int brandId, int actionType, int actionDate){
		this.userId = userId;
		this.brandId = brandId;
		this.actionType = actionType;
		this.actionDate = actionDate;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public int getBrandId(){
		return brandId;
	}
	
	public int getActionType(){
		return actionType;
	}
	
	public int getActionDate(){
		return actionDate;
	}
	
	public String toString() {
		return userId+"\t"+brandId+"\t"+actionType+"\t"+actionDate+"\n";
	}
}

class Brand{
	private int brandId;
	private double actionSum[] = null;
	private ArrayList<HashSet<Integer>> userSum = null;
	private ArrayList<HashSet<Integer>> actionDate = null;
	
	public Brand(int brandId){
		this.brandId = brandId;
		actionSum = new double[4];
		userSum = new ArrayList<HashSet<Integer>>();
		actionDate = new ArrayList<HashSet<Integer>>();
		for(int i=0;i<4;++i){
			userSum.add(new HashSet<Integer>());
			actionDate.add(new HashSet<Integer>());
		}
	}
	
	public String toString() {
		String re = ""+brandId;
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
	
	public double click2buy(){
		if(getActionSum(firstRec.BUY)==0){
			return Double.MAX_VALUE;
		}
		else{
			return getActionSum(firstRec.CLICK)/getActionSum(firstRec.BUY);
		}
	}
}

class UBpair implements Comparable<UBpair>{
	User user;
	int brandId;
	double score;
	
	UBpair(User user,int brandId,double score){
		this.user = user; 
		this.brandId = brandId; 
		this.score = score; 
	}

	public int compareTo(UBpair other) {
		if(this.score<other.score){
			return 1;
		}
		if(this.score>other.score){
			return -1;
		}
		return 0;
	}
}

public class firstRec{
	static final int buyUpper = 3;
	static final int CLICK = 0, BUY = 1, COLLECT = 2, CART = 3;
	static int startTrain, endTrain, endTest;
	static int features[] = {1,3,4,6,12};
	
//	static final String commonPath = "C:/Users/k/Desktop/��è�㷨����/";
	static final String commonPath = "/home/keyang/RS/";
	static final String recordsPath=commonPath+"���/processed.txt";
	static final String resultPath=commonPath+"���/result.txt";
	static final String brandsPath=commonPath+"���/brands.txt";
	static final String trainPath=commonPath+"���/resample-feature/4/train3.arff";
	static final String modelPath=commonPath+"model/2.model";
	static final String modelsPath=commonPath+"models/";
	static ArrayList<Record> records = new ArrayList<Record>();
	static HashMap<Integer,User> users = new HashMap<Integer,User>();
	static HashMap<Integer,Brand> brands = new HashMap<Integer,Brand>();
	static ArrayList<UBpair> UBpairs = new ArrayList<UBpair>();
	static HashMap<Integer,Classifier> classifiers = new HashMap<Integer,Classifier>();
	
	static final String collect_whitePath=commonPath+"lists/collect_white.txt";
	static final String collect_blackPath=commonPath+"lists/collect_black.txt";
	static HashSet<Integer> collect_white = new HashSet<Integer>();
	static HashSet<Integer> collect_black = new HashSet<Integer>();
	static HashSet<Integer> blackBrands = new HashSet<Integer>();
	
	public static void main(String args[]) throws Exception{
		for(int i=0;i<8;i+=1){
			int tmp = features[i];
			features[i] = 4;
			for(int j=0;j<41;j+=20){
				System.out.print(i+"\t"+j+"\t");
				timeInit(31);
				config();
				genArffs(j);
				train();
				records.clear();
				users.clear();
				brands.clear();
//				
				timeInit(0);
				config();
//				CF(j);
				classify();
				evaluation();
				
				output();	
				records.clear();
				users.clear();
				brands.clear();
				UBpairs.clear();
				classifiers.clear();
			}
			features[i] = tmp;
		}
	}
	
	public static void loadLists() throws Exception{
		String line = null;
		InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(collect_whitePath)), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		while ((line = bufferedReader.readLine()) != null) {
			collect_white.add(Integer.parseInt(line));
		}
		
		reader = new InputStreamReader(new FileInputStream(new File(collect_blackPath)), "UTF-8");
		bufferedReader = new BufferedReader(reader);
		while ((line = bufferedReader.readLine()) != null) {
			collect_black.add(Integer.parseInt(line));
		}
	}
	
	public static void config() throws Exception{
		loadRecords();
		analyseBrand();
		setUsers();
		userBrandPref();
//		for(int userId:users.keySet()){
//			User user = users.get(userId);
//			System.out.println(userId+"\t"+user.getUniqueBrands(BUY)+"\t"+user.averClickBuySpan()); 
//		}
//		Out:for(int userId:users.keySet()){
//			User user = users.get(userId);
//			for(int brandId:user.getWeightedBrands()){
//				Brand brand = brands.get(brandId);
//				int collectBuySpan = user.firstBrandActionDate(BUY, brandId)-user.firstBrandActionDate(COLLECT, brandId);
//				if(user.hasBrandAction(BUY, brandId) && collectBuySpan>0){
//					continue Out;
//				}
//			}
//			System.out.println(userId+"\t"+user.getActionSum(COLLECT));
//		}
	}
	
	public static void genArff() throws Exception{
		File outputFile = new File(trainPath);
		if (!outputFile.exists()) { 
			outputFile.createNewFile();
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8");
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		
		bufferedWriter.write("@relation user-brand\n\n");
		
		bufferedWriter.write("@attribute user-getBrandActionSumCLICK numeric\n");
		bufferedWriter.write("@attribute user-getBrandActionSumBUY numeric\n");
		bufferedWriter.write("@attribute user-getBrandActionTimedSumCLICK numeric\n");
		bufferedWriter.write("@attribute user-getBrandActionTimedSumBUY numeric\n");
//		bufferedWriter.write("@attribute user-getActionSumCLICK numeric\n");
//		bufferedWriter.write("@attribute user-getActionSumBUY numeric\n");
//		bufferedWriter.write("@attribute user-getActionTimedSumCLICK numeric\n");
//		bufferedWriter.write("@attribute user-getActionTimedSumBUY numeric\n");
//		bufferedWriter.write("@attribute brand-getActionSumCLICK numeric\n");
//		bufferedWriter.write("@attribute brand-getActionSumBUY numeric\n");
//		bufferedWriter.write("@attribute brand-getUserSumCLICK numeric\n");
//		bufferedWriter.write("@attribute brand-getUserSumBUY numeric\n");
//		for(int i=0;i<=buyUpper;++i){
//			bufferedWriter.write("@attribute buyNum="+i+" numeric\n");
//		}
		bufferedWriter.write("@attribute buyNum>"+buyUpper+" numeric\n");
		bufferedWriter.write("@attribute user-getSpanBrandActionSumCLICK5 numeric\n");
		bufferedWriter.write("@attribute user-getSpanBrandActionSumBUY5 numeric\n");
		bufferedWriter.write("@attribute user-getSpanBrandActionSumCLICK10 numeric\n");
		bufferedWriter.write("@attribute user-getSpanBrandActionSumBUY10 numeric\n");
		bufferedWriter.write("@attribute user-getSpanBrandActionSumCLICK15 numeric\n");
		bufferedWriter.write("@attribute user-getSpanBrandActionSumBUY15 numeric\n");
		bufferedWriter.write("@attribute user-getSpanBrandActionSumCLICK30 numeric\n");
		bufferedWriter.write("@attribute user-getSpanBrandActionSumBUY30 numeric\n");
		
		bufferedWriter.write("@attribute buy {yes, no}\n");
		
		bufferedWriter.write("\n@data\n");
		for(int userId:users.keySet()){
			User user = users.get(userId);
			for(int brandId:user.getWeightedBrands()){
				Brand brand = brands.get(brandId);
				
				bufferedWriter.write(user.getBrandActionSum(CLICK, brandId)+",");
				bufferedWriter.write(user.getBrandActionSum(BUY, brandId)+",");
				bufferedWriter.write(user.getBrandActionTimedSum(CLICK, brandId)+",");
				bufferedWriter.write(user.getBrandActionTimedSum(BUY, brandId)+",");
//				bufferedWriter.write(user.getActionSum(CLICK)+",");
//				bufferedWriter.write(user.getActionSum(BUY)+",");
//				bufferedWriter.write(user.getActionTimedSum(CLICK)+",");
//				bufferedWriter.write(user.getActionTimedSum(BUY)+",");
//				bufferedWriter.write(brand.getActionSum(CLICK)+",");
//				bufferedWriter.write(brand.getActionSum(BUY)+",");
//				bufferedWriter.write(brand.getUserSum(CLICK)+",");
//				bufferedWriter.write(brand.getUserSum(BUY)+",");
				
				int buySum = user.getBrandActionSum(BUY, brandId);
//				for(int i=0;i<=buyUpper;++i){
//					if(buySum==i){
//						bufferedWriter.write("1,");
//					}
//					else{
//						bufferedWriter.write("0,");
//					}
//				}
				if(buySum>buyUpper){
					bufferedWriter.write("1,");
				}
				else{
					bufferedWriter.write("0,");
				}
				
				bufferedWriter.write(user.getSpanBrandActionSum(CLICK, brandId, 5)+",");
				bufferedWriter.write(user.getSpanBrandActionSum(BUY, brandId, 5)+",");
				bufferedWriter.write(user.getSpanBrandActionSum(CLICK, brandId, 10)+",");
				bufferedWriter.write(user.getSpanBrandActionSum(BUY, brandId, 10)+",");
				bufferedWriter.write(user.getSpanBrandActionSum(CLICK, brandId, 15)+",");
				bufferedWriter.write(user.getSpanBrandActionSum(BUY, brandId, 15)+",");
				bufferedWriter.write(user.getSpanBrandActionSum(CLICK, brandId, 30)+",");
				bufferedWriter.write(user.getSpanBrandActionSum(BUY, brandId, 30)+",");
				
				Set realBrands = user.getRealBrands();
				if(realBrands.contains(brandId)){
					bufferedWriter.write("yes");
				}
				else{
					bufferedWriter.write("no");
				}
				bufferedWriter.write("\n");
			}
		}
		bufferedWriter.flush();
		bufferedWriter.close();
	}
	
	public static void genArffs(int repeatTimes) throws Exception{
		String header = "";
		ArrayList<String> commonStrs = new ArrayList<String>();
		header = header.concat("@relation user-brand\n\n");
		header = header.concat("@attribute user-getBrandActionSumCLICK numeric\n");
		header = header.concat("@attribute user-getBrandActionSumBUY numeric\n");
		header = header.concat("@attribute user-getBrandActionTimedSumCLICK numeric\n");
		header = header.concat("@attribute user-getBrandActionTimedSumBUY numeric\n");
		header = header.concat("@attribute buyNum>"+buyUpper+" numeric\n");
		header = header.concat("@attribute user-getSpanBrandActionSumCLICK7 numeric\n");
		header = header.concat("@attribute user-getSpanBrandActionSumBUY7 numeric\n");
		header = header.concat("@attribute user-getSpanBrandActionSumCLICK15 numeric\n");
		header = header.concat("@attribute user-getSpanBrandActionSumBUY15 numeric\n");
		header = header.concat("@attribute user-getSpanBrandActionSumCLICK30 numeric\n");
		header = header.concat("@attribute user-getSpanBrandActionSumBUY30 numeric\n");
//		header = header.concat("@attribute clickAfterBuy numeric\n");
		header = header.concat("@attribute buy {yes, no}\n");
		header = header.concat("\n@data\n");
		
		for(int userId:users.keySet()){
			User user = users.get(userId);
			String commonStr = "";
			for(int brandId:user.getWeightedBrands()){
				Brand brand = brands.get(brandId);
				
				commonStr = commonStr.concat(user.getBrandActionSum(CLICK, brandId)+",");
				commonStr = commonStr.concat(user.getBrandActionSum(BUY, brandId)+",");
				commonStr = commonStr.concat(user.getBrandActionTimedSum(CLICK, brandId)+",");
				commonStr = commonStr.concat(user.getBrandActionTimedSum(BUY, brandId)+",");
				
				int buySum = user.getBrandActionSum(BUY, brandId);
				if(buySum>buyUpper){
					commonStr = commonStr.concat("1,");
				}
				else{
					commonStr = commonStr.concat("0,");
				}
				
				commonStr = commonStr.concat(user.getSpanBrandActionSum(CLICK, brandId, 7)+",");
				commonStr = commonStr.concat(user.getSpanBrandActionSum(BUY, brandId, 7)+",");
				commonStr = commonStr.concat(user.getSpanBrandActionSum(CLICK, brandId, 15)+",");
				commonStr = commonStr.concat(user.getSpanBrandActionSum(BUY, brandId, 15)+",");
				commonStr = commonStr.concat(user.getSpanBrandActionSum(CLICK, brandId, 30)+",");
				commonStr = commonStr.concat(user.getSpanBrandActionSum(BUY, brandId, 30)+",");
				
//				if(user.hasBrandAction(BUY, brandId) && user.lastBrandActionDate(CLICK, brandId)-user.lastBrandActionDate(BUY, brandId)>10){
//					commonStr = commonStr.concat("1,");
//				}
//				else{
//					commonStr = commonStr.concat("0,");
//				}
				
				Set realBrands = user.getRealBrands();
				if(realBrands.contains(brandId)){
					commonStr = commonStr.concat("yes");
				}
				else{
					commonStr = commonStr.concat("no");
				}
				commonStr = commonStr.concat("\n");
			}
			commonStrs.add(commonStr);
		}
		
		File outputFile = new File(trainPath);
		if (!outputFile.exists()) { 
			outputFile.createNewFile();
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8");
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		bufferedWriter.write(header);
		for(String commonStr:commonStrs){
			bufferedWriter.write(commonStr);
		}
		bufferedWriter.flush();
		bufferedWriter.close();
		
		for(int userId:users.keySet()){
			User user = users.get(userId);
			
			outputFile = new File(modelsPath+userId+".arff");
			if (!outputFile.exists()) { 
				outputFile.createNewFile();
			}
			writer = new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8");
			bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(header);
			for(String commonStr:commonStrs){
				bufferedWriter.write(commonStr);
			}
			
			for(int i=0;i<repeatTimes;++i){
				for(int brandId:user.getWeightedBrands()){
					Brand brand = brands.get(brandId);
					
					bufferedWriter.write(user.getBrandActionSum(CLICK, brandId)+",");
					bufferedWriter.write(user.getBrandActionSum(BUY, brandId)+",");
					bufferedWriter.write(user.getBrandActionTimedSum(CLICK, brandId)+",");
					bufferedWriter.write(user.getBrandActionTimedSum(BUY, brandId)+",");
					
					int buySum = user.getBrandActionSum(BUY, brandId);
					if(buySum>buyUpper){
						bufferedWriter.write("1,");
					}
					else{
						bufferedWriter.write("0,");
					}
					
					bufferedWriter.write(user.getSpanBrandActionSum(CLICK, brandId, 7)+",");
					bufferedWriter.write(user.getSpanBrandActionSum(BUY, brandId, 7)+",");
					bufferedWriter.write(user.getSpanBrandActionSum(CLICK, brandId, 15)+",");
					bufferedWriter.write(user.getSpanBrandActionSum(BUY, brandId, 15)+",");
					bufferedWriter.write(user.getSpanBrandActionSum(CLICK, brandId, 30)+",");
					bufferedWriter.write(user.getSpanBrandActionSum(BUY, brandId, 30)+",");
					
//					if(user.hasBrandAction(BUY, brandId) && user.lastBrandActionDate(CLICK, brandId)-user.lastBrandActionDate(BUY, brandId)>10 || user.lastBrandActionDate(BUY, brandId)>endTrain-10){
//						bufferedWriter.write("1,");
//					}
//					else{
//						bufferedWriter.write("0,");
//					}
					
					Set realBrands = user.getRealBrands();
					if(realBrands.contains(brandId)){
						bufferedWriter.write("yes");
					}
					else{
						bufferedWriter.write("no");
					}
					bufferedWriter.write("\n");
				}
			}
			bufferedWriter.flush();
			bufferedWriter.close();
		}
	}
	
	public static void train() throws Exception{
		for(int userId:users.keySet()){
			User user = users.get(userId);
			Instances trainInstances = new Instances(new BufferedReader(new FileReader(modelsPath+userId+".arff")));
			Instances format = new Instances(new BufferedReader(new FileReader(modelsPath+userId+".arff")));
			trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
			format.setClassIndex(format.numAttributes() - 1);
			
		Out:for(int j=format.numAttributes()-1;j>0;--j){
				for(int feature:features){
					if(feature==j){
						continue Out;
					}
				}
				trainInstances.deleteAttributeAt(j-1);
			}
			
			Logistic classifier = new Logistic();
//			NaiveBayes classifier = new NaiveBayes();
			classifier.buildClassifier(trainInstances);
			
			classifiers.put(userId, classifier);
		}
	}
	
	public static void classify() throws Exception{
		int recedNum = 0;
		Instances trainInstances = new Instances(new BufferedReader(new FileReader(trainPath)));
		Instances format = new Instances(new BufferedReader(new FileReader(trainPath)));
		trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
		format.setClassIndex(format.numAttributes() - 1);
		
//		Classifier classifier =(Classifier)weka.core.SerializationHelper.read(modelPath);
	Out:for(int j=format.numAttributes()-1;j>0;--j){
			for(int feature:features){
				if(feature==j){
					continue Out;
				}
			}
			trainInstances.deleteAttributeAt(j-1);
		}
		
//		SPegasos classifier = new SPegasos();
		Logistic classifier = new Logistic();
//		NaiveBayes classifier = new NaiveBayes();
		classifier.buildClassifier(trainInstances);
		
		for(int userId:users.keySet()){
			User user = users.get(userId);
			if(classifiers.containsKey(userId)){
				classifier = (Logistic) classifiers.get(userId);
			}
			
			int buyAver = (int) Math.round((user.getActionSum(BUY)/3.0));
			int topSize1 = Math.max((int) Math.round((user.getActionSum(BUY)/3.0)*0.1), 0);
			int topSize2 = Math.max((int) Math.round((user.getActionSum(BUY)/3.0)*1), 0);
			int topSize3 = Math.max((int) Math.round((user.getActionSum(BUY)/6.0)*1), 0);
			HashSet<Integer> topBrands1 = (HashSet<Integer>) user.getTopBrands(topSize1);
			HashSet<Integer> topBrands2 = (HashSet<Integer>) user.getTopBrands(topSize2);
			HashSet<Integer> topBrands3 = (HashSet<Integer>) user.getTopBrands2(topSize3);
			topBrands1.addAll(topBrands3);
			topBrands2.addAll(topBrands3);
			for(int brandId:user.getWeightedBrands()){
				Brand brand = brands.get(brandId);
				Instance instance = new Instance(format.numAttributes());
				int h = 0;
				
				instance.setValue(format.attribute(h++), user.getBrandActionSum(CLICK, brandId));      
				instance.setValue(format.attribute(h++), user.getBrandActionSum(BUY, brandId));
				instance.setValue(format.attribute(h++), user.getBrandActionTimedSum(CLICK, brandId));
				instance.setValue(format.attribute(h++), user.getBrandActionTimedSum(BUY, brandId));
//				instance.setValue(format.attribute(h++), user.getActionSum(CLICK));
//				instance.setValue(format.attribute(h++), user.getActionSum(BUY));
//				instance.setValue(format.attribute(h++), user.getActionTimedSum(CLICK));
//				instance.setValue(format.attribute(h++), user.getActionTimedSum(BUY));
//				instance.setValue(format.attribute(h++), brand.getActionSum(CLICK));
//				instance.setValue(format.attribute(h++), brand.getActionSum(BUY));
//				instance.setValue(format.attribute(h++), brand.getUserSum(CLICK));
//				instance.setValue(format.attribute(h++), brand.getUserSum(BUY));
				
				int buySum = user.getBrandActionSum(BUY, brandId);
//				for(int k=0;k<=buyUpper;++k){
//					if(buySum==k){
//						instance.setValue(format.attribute(h++), 1);
//					}
//					else{
//						instance.setValue(format.attribute(h++), 0);
//					}
//				}
				if(buySum>buyUpper){
					instance.setValue(format.attribute(h++), 1);
				}
				else{
					instance.setValue(format.attribute(h++), 0);
				}
				
				instance.setValue(format.attribute(h++), user.getSpanBrandActionSum(CLICK, brandId, 7));
				instance.setValue(format.attribute(h++), user.getSpanBrandActionSum(BUY, brandId, 7));
				instance.setValue(format.attribute(h++), user.getSpanBrandActionSum(CLICK, brandId, 15));
				instance.setValue(format.attribute(h++), user.getSpanBrandActionSum(BUY, brandId, 15));
				instance.setValue(format.attribute(h++), user.getSpanBrandActionSum(CLICK, brandId, 30));
				instance.setValue(format.attribute(h++), user.getSpanBrandActionSum(BUY, brandId, 30));
				
//				if(user.hasBrandAction(BUY, brandId) && user.lastBrandActionDate(CLICK, brandId)-user.lastBrandActionDate(BUY, brandId)>10 || user.lastBrandActionDate(BUY, brandId)>endTrain-10){
//					instance.setValue(format.attribute(h++), 1);
//				}
//				else{
//					instance.setValue(format.attribute(h++), 0);
//				}
				
			Out:for(int j=format.numAttributes()-1;j>0;--j){
					for(int feature:features){
						if(feature==j){
							continue Out;
						}
					}
					instance.deleteAttributeAt(j-1);
				}
				instance.setDataset(trainInstances);
				
//				if(classifier.distributionForInstance(instance)[0]>0.55){
//					user.addRecBrand(brandId);
//				}
				
//				if(classifier.classifyInstance(instance)==0){
//					user.addRecBrand(brandId);
//				}
				
				boolean b10 = user.hasBrandAction(BUY, brandId) && topBrands1.contains(brandId);
				boolean b11 = !user.hasBrandAction(BUY, brandId) && topBrands2.contains(brandId) && user.lastBrandActionDate(CLICK,brandId)>=endTrain-15;
				boolean b12 = user.hasBrandAction(BUY, brandId) && user.getBrandActionSum(BUY, brandId)<brand.getActionAver(BUY) && brand.getUserSum(BUY)>2;
				boolean b13 = user.getUniqueBrands(BUY)<=8 || user.averClickBuySpan()>0.1 || user.hasBrandAction(BUY, brandId);
				boolean b14 = user.getBrandActionSum(BUY, brandId)>2;
				boolean b15 = brand.getActionAver(BUY)>1.1||brand.getUserSum(BUY)<5;
				boolean b16 = user.lastBrandActionDate(CLICK, brandId)-user.firstBrandActionDate(BUY, brandId)>5;
				boolean b17 = user.lastActionDate(CLICK)>endTrain-50;
				boolean b18 = topBrands3.contains(brandId) && user.hasBrandAction(CLICK, brandId);
				boolean b19 = user.hasBrandAction(BUY, brandId) && (user.lastBrandActionDate(CLICK, brandId)-user.firstBrandActionDate(BUY, brandId)>15 || user.firstBrandActionDate(BUY, brandId)>endTrain-30);
				boolean b20 = user.hasBrandAction(COLLECT, brandId);
				boolean b1 = collect_white.contains(userId) && user.hasBrandAction(COLLECT, brandId);
				boolean b2 = !collect_black.contains(userId) && user.lastBrandActionDate(COLLECT, brandId)>endTrain-4;
				boolean b3 = b1 || b2;
				boolean b4 = !blackBrands.contains(brandId);
				
				if(b13 && b17 && b4){
					if(b3){
						user.addRecBrand(brandId);
						++recedNum;
					}
					else{
						double score = classifier.distributionForInstance(instance)[0];
						UBpairs.add(new UBpair(user,brandId,score));
					}
				}
				
			}
		}
		Collections.sort(UBpairs);
//		System.out.println(recedNum);
		
		for(int top=0;top<2000-recedNum;++top){
			UBpairs.get(top).user.addRecBrand(UBpairs.get(top).brandId);
		}
	}
	
	public static void timeInit(int i){
		endTest= 123-i;
		endTrain = endTest-31;
		startTrain = endTrain-61;
	}
	
	public static void loadRecords() throws Exception{
		String line = null;
		String date = "";
		int dateCnt = 0;
		InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(recordsPath)), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
    
		while ((line = bufferedReader.readLine()) != null) {
			String[] lineParts = line.split("\t");
			if(!date.equals(lineParts[3])){
				date = lineParts[3];
				++dateCnt;
			}
			Record record = new Record(Integer.parseInt(lineParts[0]), Integer.parseInt(lineParts[1]),Integer.parseInt(lineParts[2]),dateCnt);
			if(record.getActionDate()>=startTrain && record.getActionDate()<=endTest){
				records.add(record);
			}
		}
	}
	
	public static void analyseBrand() throws Exception{
//		File outputFile = new File(brandsPath);
//		if (!outputFile.exists()) { 
//			outputFile.createNewFile();
//		}
//		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8");
//		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		
		for(Record record:records){
			if(record.getActionDate()<=endTrain){
				int brandId = record.getBrandId();
				if(!brands.containsKey(brandId)){
					brands.put(brandId, new Brand(brandId));
				}
				brands.get(brandId).increActionSum(record.getActionType());
				brands.get(brandId).increUserSum( record.getActionType() , record.getUserId() );
				brands.get(brandId).addDate( record.getActionType() , record.getActionDate() );
			}
		}
		for(int brandId:brands.keySet()){
			Brand brand = brands.get(brandId);
			boolean b191 = brand.getUserSum(BUY)>1;
			boolean b192 = brand.getUserSum(CLICK)>1;
			boolean b201 = brand.getLastDate(BUY)-brand.getFirstDate(BUY)==0;
			boolean b202 = brand.getLastDate(CLICK)<endTrain-30;
			boolean b21 = brand.getLastDate(BUY)<endTrain-15;
			boolean b22 = b191 && b201 && b21;
			boolean b23 = b192 && b202;
			if(b22 || b23){
				blackBrands.add(brandId);
			}
		}
//		for(Brand brand:brands.values()){
//			bufferedWriter.write(brand.toString());
//		}
//		bufferedWriter.flush();
//		bufferedWriter.close();
	}
	
	public static void setUsers() throws Exception{
		for(Record record:records){
			int userId = record.getUserId();
			if(!users.containsKey(userId) && record.getActionDate()<=endTrain){
				users.put(userId, new User());
			}
		}
		for(Record record:records){
			int userId = record.getUserId();
			if(!users.containsKey(userId)){
				continue;
			}
			if(record.getActionDate()<=endTrain){
				users.get(userId).addTrainRecord(record);
			}
			else{
				if(record.getActionType()==BUY){
					users.get(userId).addRealBrand(record.getBrandId());
				}
			}
		}
	}
	
	public static void userBrandPref() throws Exception{
		double typeWeights[] = {1,1,0,0};
		
		for(User user:users.values()){
			for(Record trainRecord:user.getTrainRecords()){
				int brandId = trainRecord.getBrandId();
				int dateSpan = endTrain - trainRecord.getActionDate() + 1 + 10;
				double increment = typeWeights[trainRecord.getActionType()]/dateSpan;
				if(increment>0){
					user.increBrandWeight(brandId, increment);
				}
			}
		}
	}
	
	public static void CF(double Thresh) throws Exception{
		for(int userId1:users.keySet()){
			User user1 = users.get(userId1);
			user1.setUserSimi(userId1, 1);
			for(int userId2:users.keySet()){
				User user2 = users.get(userId2);
				if(userId1>userId2){
					HashSet<Integer> brands1 = new HashSet<Integer>();
					HashSet<Integer> brands2 = new HashSet<Integer>();
					brands1.addAll(user1.getWeightedBrands());
					brands1.addAll(user2.getWeightedBrands());
					brands2.addAll(user1.getWeightedBrands());
					brands2.retainAll(user2.getWeightedBrands());
					if(brands2.isEmpty()){
						user1.setUserSimi(userId2, 0);
						user2.setUserSimi(userId1, 0);
					}
					else{
						double sum_1=0, sum_2=0, sum_3=0;
						for(int brandId:brands1){
							double r1 = user1.getBrandWeight(brandId);
							double r2 = user2.getBrandWeight(brandId);
							sum_1 += r1*r2;
							sum_2 += r1*r1;
							sum_3 += r2*r2;
						}
						double coefficient = sum_1/Math.sqrt(sum_2*sum_3);
						user1.setUserSimi(userId2, coefficient);
						user2.setUserSimi(userId1, coefficient);
					}
				}
			}
		}
		for(Integer userId1:users.keySet()){
			User user1 = users.get(userId1);
			for(Integer userId2:users.keySet()){
				User user2 = users.get(userId2);
				double simi = user1.getuserSimi(userId2);
//				if(userId1>userId2 && simi>0){
//					System.out.println(userId1+"\t"+userId2+"\t"+simi);	
//				}
				if(simi>=0.2){
					for(int brandId:user2.getWeightedBrands()){
						if(simi==1){
							user1.increBrandWeight2(brandId, user2.getBrandWeight(brandId));
						}
						else{
							user1.increBrandWeight2(brandId, user2.getBrandWeight(brandId)*simi/2);
						}
					}
				}
			}
		}
	}
	
	public static void recommend() throws Exception{
		for(User user:users.values()){
			int buyAver = (int) Math.round((user.getActionSum(BUY)/3.0));
//			for(int brandId:user.getTopBrands(buyAver)){
//				Brand brand = brands.get(brandId);
//				boolean l1 = user.getActionSum(CLICK)>16;
//				if(l1){
//					user.addRecBrand(brandId);
//				}
//			}
			int topSize1 = Math.max((int) Math.round((user.getActionSum(BUY)/3.0)*0.1), 3);
			int topSize2 = Math.max((int) Math.round((user.getActionSum(BUY)/3.0)*2.2), 1);
			int topSize3 = Math.max((int) Math.round((user.getActionSum(BUY)/3.0)*0.7), 1);
			HashSet<Integer> topBrands1 = (HashSet<Integer>) user.getTopBrands(topSize1);
			HashSet<Integer> topBrands2 = (HashSet<Integer>) user.getTopBrands(topSize2);
			HashSet<Integer> topBrands3 = (HashSet<Integer>) user.getTopBrands2(topSize3);
			topBrands1.addAll(topBrands3);
			topBrands2.addAll(topBrands3);
			
			for(int brandId:user.getWeightedBrands()){
				Brand brand = brands.get(brandId);
//				int buySum = user.getBrandActionSum(BUY,brandId);
//				int buySpan = user.lastBrandActionDate(BUY, brandId) - user.firstBrandActionDate(BUY, brandId);
//				double Thresh = Math.max(user.click2buy(), brand.click2buy());
				
				boolean b10 = user.hasBrandAction(BUY, brandId) && topBrands1.contains(brandId);
//				boolean b10 = user.hasBrandAction(BUY, brandId) && user.getBrandWeight(brandId)>=1.76;
				boolean b11 = !user.hasBrandAction(BUY, brandId) && topBrands2.contains(brandId) && user.lastBrandActionDate(CLICK,brandId)>=endTrain-15;
//				boolean b11 = !user.hasBrandAction(BUY, brandId) && user.getBrandWeight(brandId)>=0.42 && user.lastBrandActionDate(CLICK,brandId)>=endTrain-10;
				boolean b12 = user.hasBrandAction(BUY, brandId) && user.getBrandActionSum(BUY, brandId)<brand.getActionAver(BUY);
				boolean b13 = user.getActionSum(BUY)>8 && user.averClickBuySpan()>1 || user.getActionSum(BUY)<=8;
				boolean b14 = user.getBrandActionSum(BUY, brandId)>3;
				boolean b15 = brand.getActionAver(BUY)>1.1||brand.getUserSum(BUY)<30;
				boolean b16 = user.lastBrandActionDate(CLICK, brandId)-user.firstBrandActionDate(BUY, brandId)>5;
				boolean b17 = user.lastActionDate(CLICK)>endTrain-50;
				
				if((b11&&b13 || b12&&b15 || b14 || b10) && b17){
					user.addRecBrand(brandId);
				}
			}
		}
	}
	
	public static void evaluation() throws Exception{
		int real_num=0, rec_num=0, hit_num=0;
		for(int userId:users.keySet()){
			User user = users.get(userId);
			if(user.getTrainRecords().size()==0){
				continue;
			}
			real_num += user.getRealSize();
			rec_num += user.getRecSize();
			hit_num += user.getHitSize();
//			if(user.getHitSize()!=0){
//				System.out.println(userId);
//			}
		}
		double recall = ((double)hit_num)/real_num;
		double precision = ((double)hit_num)/rec_num;
		double Fscore = 2*recall*precision/(recall+precision);
//		System.out.println("recall:\t"+recall);
//		System.out.print(rec_num+"\t");	
//		System.out.println(Fscore);	
		System.out.println(hit_num+"\t"+recall+"\t"+precision+"\t"+Fscore);	
	}
	
	public static void output() throws Exception{
		File outputFile = new File(resultPath);
		if (!outputFile.exists()) { 
			outputFile.createNewFile();
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8");
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		
		for(int userId:users.keySet()){
			String re = ""+userId+"\t";
			User user = users.get(userId);
			for(Integer brandId:user.getRecBrands()){
				re += brandId+",";
			}
			bufferedWriter.write(re.substring(0,re.length()-1)+"\n");
		}
		bufferedWriter.flush();
		bufferedWriter.close();
	}
}
