package RS;
import java.sql.Timestamp;
import java.util.Date;

public class Time extends Timestamp {

	public Time(long time) {
		super(time);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("deprecation")
	public Time(String datetime) {
//		super(Integer.parseInt(datetime.split(" ")[0].split("-")[0]), 
//				Integer.parseInt(datetime.split(" ")[0].split("-")[1]), 
//				Integer.parseInt(datetime.split(" ")[0].split("-")[2]), 
//				Integer.parseInt(datetime.split(" ")[1].split(":")[0]), 
//				Integer.parseInt(datetime.split(" ")[1].split(":")[1]), 
//				Integer.parseInt(datetime.split(" ")[1].split(":")[2]), 0);
		
		super(Integer.parseInt(datetime.split(" ")[0].split("-")[0])-1900, 
				Integer.parseInt(datetime.split(" ")[0].split("-")[1])-1, 
				Integer.parseInt(datetime.split(" ")[0].split("-")[2]), 
				Integer.parseInt(datetime.split(" ")[1].split(":")[0]), 
				0, 0, 0);
	}

}
