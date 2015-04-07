package RS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import DB.MySQL;
import Weka.RSClassifier;

import com.sun.corba.se.impl.util.*;

public class Test {

	public static void main(String[] args) throws Exception {
		Data data = new Data();
		data.initUsingFile();

		RSClassifier classifier;
		// classifier = (RSClassifier)
		// SerializationHelper.read(Info.MODEL_PATH);
		classifier = new RSClassifier();

		for (User user : data.getUsers().values()) {
			HashSet<Integer> itemsID = (HashSet<Integer>) user.getItemsID()
					.clone();
			itemsID.retainAll(data.getSubItemsID());
			for (int p : itemsID) {
				Item item = new Item();
				item.setItemId(p);
				classifier.updateData(user, item, user.isBuy(item.getItemId()));
			}
		}

		for (User user : data.getUsers().values()) {
			// for (int p:data.getSubItemsID()){
			int p = 42338659;
			Item item = new Item();
			item.setItemId(p);
			classifier.classifyUP(user, item);
			// }
		}

	}
}
