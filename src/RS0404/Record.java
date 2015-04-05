package RS0404;

class Record {
	private int userId;
	private int itemId;
	private int behaviorType;
	private String userGeohash;
	private int itemCategory;
	private Time time;
	private int i;

	public Record(int userId, int itemId, int behaviorType, String userGeohash,
			int itemCategory, Time time, int i) {
		super();
		this.userId = userId;
		this.itemId = itemId;
		this.behaviorType = behaviorType;
		this.userGeohash = userGeohash;
		this.itemCategory = itemCategory;
		this.time = time;
		this.i = i;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getBehaviorType() {
		return behaviorType;
	}

	public void setBehaviorType(int behaviorType) {
		this.behaviorType = behaviorType;
	}

	public String getUserGeohash() {
		return userGeohash;
	}

	public void setUserGeohash(String userGeohash) {
		this.userGeohash = userGeohash;
	}

	public int getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(int itemCategory) {
		this.itemCategory = itemCategory;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

}