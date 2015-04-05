package RS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Data {
	public ArrayList<Record> RECORDS;
	public ArrayList<Item> ITEMS;
	public HashMap<Integer, User> USERS;
	public HashSet<Integer> ITEMSID;

	public void init() throws Exception {
		String line = null;
		RECORDS = new ArrayList<Record>();
		ITEMS = new ArrayList<Item>();
		USERS = new HashMap<Integer, User>();
		ITEMSID = new HashSet<Integer>();
		
		InputStreamReader readerItem = new InputStreamReader(new FileInputStream(
				new File(Info.ITEM_PATH)), "UTF-8");
		BufferedReader bufferedReaderItem = new BufferedReader(readerItem);
		bufferedReaderItem.readLine();
		String lineItem = null;
		while ((lineItem = bufferedReaderItem.readLine()) != null){
			String[] lineParts = lineItem.split(",");
			if (lineParts.length<3)
				throw new Exception("error record "+lineItem);
			ITEMSID.add(Integer.parseInt(lineParts[0]));
		}
		
		
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				new File(Info.USER_PATH)), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		bufferedReader.readLine();
		int i=0;
		while ((line = bufferedReader.readLine()) != null) {
			String[] lineParts = line.split(",");
			
			if (lineParts.length<6)
				throw new Exception("error record "+line);
			
			Record record = new Record(
					Integer.parseInt(lineParts[0]),
					Integer.parseInt(lineParts[1]),
					Integer.parseInt(lineParts[2]), 
					lineParts[3],
					Integer.parseInt(lineParts[4]), 
					new Time(lineParts[5]),
					i++);
			
			if (i%1000000==0){
				System.out.println(i);
			}
			
			if (!ITEMSID.contains(record.getItemId()))
				continue;
			
			if (record.getTime().after(Info.START_TRAIN)
					&& record.getTime().before(Info.END_TEST)) {
				RECORDS.add(record);
			}
			
			if (record.getTime().after(Info.START_TRAIN)
					&& record.getTime().before(Info.END_TRAIN)) {
				if (USERS.containsKey(record.getUserId())){
					USERS.get(record.getUserId()).addTrainRecords(record);
				}
				else{
					User newUser = new User();
					newUser.addTrainRecords(record);
					USERS.put(record.getUserId(), newUser);
				}
			}
			
			if (record.getTime().after(Info.START_TEST)
					&& record.getTime().before(Info.END_TEST)) {
				if (USERS.containsKey(record.getUserId())
						&& record.getBehaviorType()==Info.BUY){
					USERS.get(record.getUserId()).addRealItems(record.getItemId());
				}
			}
			
		}
		
	}

}
