package RS0404;

public class Info {
	public static final String USER_PATH = "/home/jinyi/Serendipity/data/tianchi_mobile_recommend_train_user.csv";
	public static final String ITEM_PATH = "/home/jinyi/Serendipity/data/tianchi_mobile_recommend_train_item.csv";
	public static final String OUTPUT_PATH = "/home/jinyi/Serendipity/data/tmp_result_0405.csv";
//	public static final String USER_PATH = "/Users/jinyi/Documents/College/TianchiRS_Serendipity/data/tmp.csv";
//	public static final String ITEM_PATH = "/Users/jinyi/Documents/College/TianchiRS_Serendipity/data/tianchi_mobile_recommend_train_item.csv";
//	public static final String OUTPUT_PATH = "/Users/jinyi/Documents/College/TianchiRS_Serendipity/data/tmp_result.csv";
	
//	public static final Time START_TRAIN = new Time("2014-11-17 23:10:00");
//	public static final Time END_TRAIN = new Time("2014-12-17 23:10:00");
//	public static final Time START_TEST = new Time("2014-12-17 23:10:00");
//	public static final Time END_TEST = new Time("2014-12-18 23:10:00");
	
	public static final Time START_TRAIN = new Time("2014-12-17 23:10:00");
	public static final Time END_TRAIN = new Time("2014-12-18 23:10:00");
	public static final Time START_TEST = new Time("2014-12-20 23:10:00");
	public static final Time END_TEST = new Time("2014-12-21 00:10:00");
	
	public static final int TOTALBUY = 100;
	public static final double SUGGESTBUY = 100000000;//5.7 1000
	public static final int BUY = 4;
	public static final int CART = 3;
	public static final int FAVORITE = 2;
	public static final int CLICK = 1;
}
