package RS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Test {
	public static void output(Data data) throws Exception {
		File outputFile = new File(Info.OUTPUT_PATH);
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(outputFile), "UTF-8");
		BufferedWriter bufferedWriter = new BufferedWriter(writer);

		bufferedWriter.write("user_id,item_id");
		bufferedWriter.newLine();
		
		for (int userId : data.USERS.keySet()) {
			User user = data.USERS.get(userId);
			for (Integer brandId : user.getRecItems()) {
				bufferedWriter.write(userId+","+brandId);
				bufferedWriter.newLine();
			}
		}
		bufferedWriter.flush();
		bufferedWriter.close();
	}

	public static void main(String argv[]) throws Exception{
		Data data = new Data();
		data.init();
		System.out.println("start");
		int hit = 0;
		int totalRec = 0;
		int totalReal = 0;
		System.out.println(data.USERS.size());
		for (User user:data.USERS.values()){
			user.recommend();
			hit += user.result().get(2);
			totalRec += user.result().get(0);
			totalReal += user.result().get(1);
		}
		System.out.println("hit "+hit+" rec "+totalRec+" real "+totalReal);
		if (totalRec != 0 && totalReal != 0)
			System.out.println("pre "+(float)hit/totalRec+" recal "+(float)hit/totalReal);
		output(data);
	}
}
