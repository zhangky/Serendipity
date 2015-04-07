package Weka;

import RS.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import DB.*;
import RS.*;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.filters.AllFilter;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class RSClassifier {

	/** for serialization. */
	private static final long serialVersionUID = -123455813150452885L;

	/** The training data gathered so far. */
	private Instances m_Data = null;

	/** The filter used to generate the word counts. */
//	private StringToWordVector m_Filter = new StringToWordVector();
	private Filter m_Filter = new AllFilter();

	/** The actual classifier. */
//	private Classifier m_Classifier = new J48();
	private Classifier m_Classifier = new Logistic();
	
	private ArrayList<String> features = new ArrayList<String>();
	

	/** Whether the model is up to date. */
	private boolean m_UpToDate;

	/**
	 * Constructs empty training dataset.
	 * @throws Exception 
	 */
	public RSClassifier() throws Exception {
		this.loadInFeature();
		
		String nameOfDataset = "RS-Serendipity";

		// Create vector of attributes.
		FastVector attributes = new FastVector(this.features.size()+1);

		// Add attribute for holding messages.
		for (String feature:this.features){
			attributes.addElement(new Attribute(feature));
//			attributes.addElement(new Attribute(feature, (FastVector) null));
		}

		// Add class attribute.
		FastVector classValues = new FastVector(2);
		classValues.addElement("0");
		classValues.addElement("1");
		attributes.addElement(new Attribute("Class", classValues));

		// Create dataset with initial capacity of 100, and set index of class.
		m_Data = new Instances(nameOfDataset, attributes, 100);
		m_Data.setClassIndex(m_Data.numAttributes() - 1);
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

	/**
	 * Updates model using the given training message.
	 * 
	 * @param message
	 *            the message content
	 * @param classValue
	 *            the class label
	 */
	
	public void updateData(User user, Item item, String classValue) {
		// Make message into instance.
		Instance instance = makeInstance(user, item, m_Data);

		// Set class value for instance.
		instance.setClassValue(classValue);

		// Add instance to training data.
		m_Data.add(instance);

		m_UpToDate = false;
	}	
	

	/**
	 * Classifies a given message.
	 * 
	 * @param message
	 *            the message content
	 * @throws Exception
	 *             if classification fails
	 */
	public void classifyUP(User user, Item item) throws Exception {
		// Check whether classifier has been built.
		if (m_Data.numInstances() == 0)
			throw new Exception("No classifier available.");

		// Check whether classifier and filter are up to date.
		if (!m_UpToDate) {
			// Initialize filter and tell it about the input format.
			m_Filter.setInputFormat(m_Data);

			// Generate word counts from the training data.
			Instances filteredData = Filter.useFilter(m_Data, m_Filter);

			// Rebuild classifier.
			m_Classifier.buildClassifier(filteredData);

			m_UpToDate = true;
		}

		// Make separate little test set so that message
		// does not get added to string attribute in m_Data.
		//doubt
		Instances testset = m_Data.stringFreeStructure();

		// Make message into test instance.
		Instance instance = makeInstance(user, item, testset);

		// Filter instance.
		m_Filter.input(instance);
		Instance filteredInstance = m_Filter.output();

		// Get index of predicted class value.
		double predicted = m_Classifier.classifyInstance(filteredInstance);

		// Output class value.
		System.err.println("U: "+ user.getUserID() +" P: "+ item.getItemId() +" classified as : "
				+ m_Data.classAttribute().value((int) predicted));
	}

	/**
	 * Method that converts a text message into an instance.
	 * 
	 * @param text
	 *            the message content to convert
	 * @param data
	 *            the header information
	 * @return the generated Instance
	 */
	private Instance makeInstance(User user, Item item, Instances data) {
		// Create instance of length two.
		Instance instance = new Instance(this.features.size()+1);

		// Set value for message attribute
		for (String feature:this.features){
			Attribute upAtt = data.attribute(feature);
			instance.setValue(upAtt, user.getFeatureValue(feature, item));
		}

		// Give instance access to attribute information from the dataset.
		instance.setDataset(data);

		return instance;
	}
	

	/**
	 * Main method. The following parameters are recognized:
	 * <ul>
	 * <li>
	 * <code>-m messagefile</code><br/>
	 * Points to the file containing the message to classify or use for updating
	 * the model.</li>
	 * <li>
	 * <code>-c classlabel</code><br/>
	 * The class label of the message if model is to be updated. Omit for
	 * classification of a message.</li>
	 * <li>
	 * <code>-t modelfile</code><br/>
	 * The file containing the model. If it doesn't exist, it will be created
	 * automatically.</li>
	 * </ul>
	 * 
	 * @param args
	 *            the commandline options
	 */
	
//	public void test(){
//		RSClassifier classifier;
////		classifier = (RSClassifier) SerializationHelper.read(Info.MODEL_PATH);
//		classifier = new RSClassifier();
//		
//		classifier.updateData(user, item, '0');
//		classifier.classifyUP(user, item);
//	}

}
