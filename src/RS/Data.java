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
	private ArrayList<Record> records;
	private HashSet<Item> subItems;
	private HashMap<Integer, User> users;
	private HashSet<Integer> subItemsID;

	public void init() throws Exception {
		// mysql load in version, User and Item class provide sql query
		// function, or implement many functions in sql query
		// so need mysql init setting, there maybe some mysql variables need to
		// be used in latter class
		// load in the records, sub items, and all the users

		// file load in and if mysql speed and check consistince

	}
	
	public void initUsingSQL() throws Exception{
		
	}

	public void initUsingFile() throws Exception {
		records = new ArrayList<Record>();
		users = new HashMap<Integer, User>();
		subItems = new HashSet<Item>();// can't use hashmap ()
		subItemsID = new HashSet<Integer>();

		String line = null;
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				new File(Info.SUBITEM_PATH)), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		bufferedReader.readLine();// title

		while ((line = bufferedReader.readLine()) != null) {
			String[] lineParts = line.split(",");
			if (lineParts.length < 3)
				throw new Exception("error item table record " + line);
			subItems.add(new Item(Integer.parseInt(lineParts[0]), lineParts[1],
					Integer.parseInt(lineParts[2])));
			subItemsID.add(Integer.parseInt(lineParts[0]));
		}
		System.out.println("P size " + subItems.size() + " "
				+ subItemsID.size());
		bufferedReader.close();
		reader.close();

		reader = new InputStreamReader(new FileInputStream(new File(
				Info.USER_PATH)), "UTF-8");
		bufferedReader = new BufferedReader(reader);
		bufferedReader.readLine();
		int i = 0;
		
//		int tmp1=0;
//		int tmp2=0;
		
		while ((line = bufferedReader.readLine()) != null) {
			String[] lineParts = line.split(",");

//			if (i % 1000000 == 0) {
//				System.out.println(i);
//			}

			if (lineParts.length < 6)
				throw new Exception("error user record " + line);

			Record record = new Record(Integer.parseInt(lineParts[0]),
					Integer.parseInt(lineParts[1]),
					Integer.parseInt(lineParts[2]), lineParts[3],
					Integer.parseInt(lineParts[4]), new Time(lineParts[5]), i++);
			
//			System.out.println(line);
//			System.out.println(lineParts[5]+"\n"+lineParts[5].split(" ")[0].split("-")[0]);
//			System.out.println(record.getTime().toString()+"\n");
			
			if (record.getTime().after(Info.START_TRAIN)
					&& record.getTime().before(Info.END_TEST)) {
				records.add(record);
			}

			
			if (record.getTime().after(Info.START_TRAIN)
					&& record.getTime().before(Info.END_TRAIN)) {
				if (users.containsKey(record.getUserId())) {
					users.get(record.getUserId()).getTrainRecords().add(record);
				} else {
					User newUser = new User();
					newUser.getTrainRecords().add(record);
					users.put(record.getUserId(), newUser);
				}
//				tmp1++;
//				System.out.println(record.getTime().toString());
			}

			if (record.getTime().after(Info.START_TEST)
					&& record.getTime().before(Info.END_TEST)) {
				if (users.containsKey(record.getUserId())
						&& record.getBehaviorType() == Info.BUY
						&& subItemsID.contains(record.getItemId())) {
					users.get(record.getUserId()).getRealItems()
							.add(record.getItemId());
				}
//				tmp2++;
			}
		}

		System.out.println("i " + i + "\nrecords size "
				+ records.size() + "\ntotal users " + users.size());
//		System.out.println("tmp1 "+tmp1+" tmp2 "+tmp2);
	}

	public ArrayList<Record> getRecords() {
		return records;
	}

	public void setRecords(ArrayList<Record> records) {
		this.records = records;
	}

	public HashSet<Item> getSubItems() {
		return subItems;
	}

	public HashSet<Integer> getSubItemsID() {
		return subItemsID;
	}

	public void setSubItemsID(HashSet<Integer> subItemsID) {
		this.subItemsID = subItemsID;
	}

	public void setSubItems(HashSet<Item> subItems) {
		this.subItems = subItems;
	}

	public HashMap<Integer, User> getUsers() {
		return users;
	}

	public void setUsers(HashMap<Integer, User> users) {
		this.users = users;
	}

}
