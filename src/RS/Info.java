package RS;

public class Info {
//	public static final String USER_PATH = "/home/jinyi/Serendipity/data/tianchi_mobile_recommend_train_user.csv";
//	public static final String SUBITEM_PATH = "/home/jinyi/Serendipity/data/tianchi_mobile_recommend_train_item.csv";
//	public static final String OUTPUT_PATH = "/home/jinyi/Serendipity/data/tmp_result_0406.csv";
	public static final String USER_PATH = "/Users/jinyi/Documents/College/TianchiRS_Serendipity/data/tmp.csv";
	public static final String SUBITEM_PATH = "/Users/jinyi/Documents/College/TianchiRS_Serendipity/data/tianchi_mobile_recommend_train_item.csv";
	public static final String OUTPUT_PATH = "/Users/jinyi/Documents/College/TianchiRS_Serendipity/data/tmp_result.csv";
	
	public static final Time START_TRAIN = new Time("2014-11-17 23");//11-17 23< < 12-19 0
	public static final Time END_TRAIN = new Time("2014-12-18 00");
	public static final Time START_TEST = new Time("2014-12-17 23");
	public static final Time END_TEST = new Time("2014-12-19 00");
	
	public static final String DB_URL = "jdbc:mysql://202.120.38.146:3306/jinyi";
	public static final String DB_USER = "jinyi";
	public static final String DB_PASSWORD = "enwiki";
	
	//classify
	public static final String FEATURES_PATH = "/Users/jinyi/Documents/College/TianchiRS_Serendipity/data/rs.feature";
//	public static final String FEATURES_PATH = "/home/jinyi/Serendipity/data/rs.feature";
	public static final String MODEL_PATH = "/home/jinyi/Serendipity/data/rs.model";
	public static final String FORMAT_PATH = "/home/jinyi/Serendipity/data/rs.format";
	
//	public static final Time START_TRAIN = new Time("2014-11-17 23");//11-17 23< < 12-19 0
//	public static final Time END_TRAIN = new Time("2014-12-19 00");
//	public static final Time START_TEST = new Time("2014-12-19 23");
//	public static final Time END_TEST = new Time("2014-12-21 00");
	
//	public static final int TOTALBUY = 100;
//	public static final double SUGGESTBUY = 100000000;//5.7 1000
	
	public static final int BUY = 4;
	public static final int CART = 3;
	public static final int COLLECT = 2;
	public static final int CLICK = 1;
}
