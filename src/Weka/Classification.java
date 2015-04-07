package Weka;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import RS.User;
import RS.Data;
import RS.Info;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.core.*;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Classification {

	private ArrayList<String> features;
	// private Data testData;
	private Classifier classifier;
	private Instances format;

	public Classification() {
		features = new ArrayList<String>();
	}
	
	public void train(Data data){
		classifier = new Logistic();
		Instances trainInstances = null;
		
	    String nameOfDataset = "RS-Serendipity";

	    // Create vector of attributes.
	    FastVector attributes = new FastVector(2);

	    // Add attribute for holding messages.
	    attributes.addElement(new Attribute("Buy", (FastVector) null));

	    // Add class attribute.
	    FastVector classValues = new FastVector(2);
	    classValues.addElement("miss");
	    classValues.addElement("hit");
	    attributes.addElement(new Attribute("Class", classValues));

	    // Create dataset with initial capacity of 100, and set index of class.
	    trainInstances = new Instances(nameOfDataset, attributes, 100);
	    trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
		
		for (User user:data.getUsers().values()){
//			for(int pID:data.getSubItemsID()){
			for (int pID:user.getSubItems()){
				
			}
		}
			
			
			
//			Instances trainInstances = new Instances(new BufferedReader(new FileReader(modelsPath+userId+".arff")));
//			Instances format = new Instances(new BufferedReader(new FileReader(modelsPath+userId+".arff")));
			Instances trainInstances = new Instances(new BufferedReader(new FileReader(modelsPath+userId+".arff")));
			Instances format = new Instances(new BufferedReader(new FileReader(modelsPath+userId+".arff")));
			trainInstances.setClassIndex(trainInstances.numAttributes() - 1);
			format.setClassIndex(format.numAttributes() - 1);
			
//			NaiveBayes classifier = new NaiveBayes();
			classifier.buildClassifier(trainInstances);
			
		}
		
	}

	public void loadInFeature() throws Exception {
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				new File(Info.FEATURES_PATH)), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			features.add(line);
		}
		System.out.println("num of features " + features.size());
	}

	public void loadInExistingM() throws Exception {
		classifier = (Classifier) weka.core.SerializationHelper
				.read(Info.MODEL_PATH);
		format = new Instances(new BufferedReader(new FileReader(
				Info.FORMAT_PATH)));
		format.setClassIndex(format.numAttributes() - 1);
	}

	public double classify(String sentence) throws Exception {

		Unigram ug = new Unigram();
		HashMap<String, Integer> tokens = ug.calSenFrequency(sentence);

		Instance testInstance = new Instance(format.numAttributes());
		int i = 0;
		for (String word : features) {
			if (tokens.containsKey(word)) {
				testInstance.setValue(format.attribute(i++), tokens.get(word));
			} else {
				testInstance.setValue(format.attribute(i++), 0);
			}
		}

		testInstance.setDataset(format);
		double score = classifier.classifyInstance(testInstance);

		return score;
	}

}
