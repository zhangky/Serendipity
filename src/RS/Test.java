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
		
		for (int userId : data.getUsers().keySet()) {
			User user = data.getUsers().get(userId);
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
		data.initUsingFile();

		
//		for (int i=0; i<=100; i++){
//			int hit = 0;
//			int totalRec = 0;
//			int totalReal = 0;
//			
//			for (User user:data.getUsers().values()){
//				user.getRecItems().clear();
//				
//				user.recommend(data.getSubItemsID(), i);
//				hit += user.getHit().size();
//				totalRec += user.getRecItems().size();
//				totalReal += user.getRealItems().size();
//				
//			}
//			
//			System.out.print(i+"\t"+hit+"\t"+totalRec+"\t"+totalReal+"\t");
////				System.out.println("Hit "+hit+" Rec "+totalRec+" Real "+totalReal);
//			if (totalRec != 0 && totalReal != 0){
//				float precision = (float)hit/totalRec;
//				float recall = (float)hit/totalReal;
//				float f1 = 2*precision*recall/(precision+recall);
//				System.out.println(f1+"\t"+precision+"\t"+recall);
////					System.out.println("F1 "+f1+" Precision "+precision+" Recall "+recall);
//			}
//			else{
//				System.out.println();
//			}
//			
//		}
//		
		int hit = 0;
		int totalRec = 0;
		int totalReal = 0;
		
		for (User user:data.getUsers().values()){
			
			user.recommend(data.getSubItemsID(), Integer.parseInt(argv[0]));
			hit += user.getHit().size();
			totalRec += user.getRecItems().size();
			totalReal += user.getRealItems().size();
			
		}
		
		System.out.print(argv[0]+"\t"+hit+"\t"+totalRec+"\t"+totalReal+"\t");
//			System.out.println("Hit "+hit+" Rec "+totalRec+" Real "+totalReal);
		if (totalRec != 0 && totalReal != 0){
			float precision = (float)hit/totalRec;
			float recall = (float)hit/totalReal;
			float f1 = 2*precision*recall/(precision+recall);
			System.out.println(f1+"\t"+precision+"\t"+recall);
//				System.out.println("F1 "+f1+" Precision "+precision+" Recall "+recall);
		}
		else{
			System.out.println();
		}
		output(data);
		
	}
	
	
	
}
