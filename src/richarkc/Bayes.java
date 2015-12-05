package richarkc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;

public class Bayes {

	@SuppressWarnings("unchecked")
	public HashMap<String, Double>[] classifiers = new HashMap[5];
	public static final double ENTRIES = 10000;
	private File data;
	private double[] c = new double[5];
	public int classNum;

	/**
	 * Initializes and parses data in the given data set into conditional
	 * probabilites for Bayes classifier
	 * 
	 * @param data
	 *            text file containing classifier data
	 */
	public Bayes(File data) {
		this.data = data;
		try {
			parseData();
		} catch (FileNotFoundException e) {
			System.err
					.println("Error: Data set was not found. Make sure data is named appropriately and in the proper directory.");
			System.exit(1);
		}
	}

	private void parseData() throws FileNotFoundException {
		for (int i = 0; i < 5; i++) {
			// initialize a hashmap for every classifier
			classifiers[i] = new HashMap<String, Double>();
		}

		Scanner sc = new Scanner(data);
		HashMap<String, Double> tmp = null;

		while (sc.hasNext()) {
			// Split the line into individual attributes
			String temp[] = sc.nextLine().split(" ");
			// get classifier and hashmap for that classifier
			int cl = Integer.parseInt(temp[temp.length - 1]);
			tmp = classifiers[cl];
			// increment occurence of classifier
			c[cl]++;
			for (int i = 0; i < temp.length - 1; i++) {
				if (tmp.containsKey(temp[i])) {
					// increment attribute given the classifier
					double k = tmp.get(temp[i]);
					k++;
					tmp.put(temp[i], k);
				} else {
					tmp.put(temp[i], 1.0);
				}
			}

		}
		sc.close();
		for (int i = 0; i < 5; i++) {
			// figure out how many classifiers there are
			if (c[i] == 0){
				classNum = i;
				break;
			}
		}

		// change numbers into probabilities
		for (int i = 0; i < classNum; i++) {
			// iterating through map used from previous work, originally from
			// StackOverflow source:
			// http://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
			Iterator<Entry<String, Double>> it = classifiers[i].entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, Double> pair = (Map.Entry<String, Double>) it
						.next();
				double prob = (double) pair.getValue();
				pair.setValue((prob / ENTRIES) / (c[i] / ENTRIES));
				
			}
		}

	}

	public double classifyData() throws FileNotFoundException {
		Scanner sc = new Scanner(data);
		double[] probs = new double[classNum];
		double highestProb = Double.NEGATIVE_INFINITY;
		double highestProbClassifier = -1;
		double success = 0;

		while (sc.hasNext()) {
			highestProbClassifier = -1;
			highestProb = Double.NEGATIVE_INFINITY;
			String[] temp = sc.nextLine().split(" ");
			for (int i = 0; i < classNum; i++) {
				// probability of given classifier
				probs[i] = c[i] / ENTRIES;
				// iterate through all but classifier
				for (int j = 0; j < temp.length - 1; j++) {
					// bayes classifier calculations
					probs[i] = probs[i] * classifiers[i].get(temp[j]);
				}
				if (probs[i] > highestProb) {
					// take highest probability and set entry as that classifier
					highestProb = probs[i];
					highestProbClassifier = i;
				}
			}
			if (highestProbClassifier == Double
					.parseDouble(temp[temp.length - 1])) {
				// if bayes classifier matches given one, increment success
				success++;
			}
		}
		sc.close();
		// calculate success rate
		return success / ENTRIES;
	}

	public static void main(String[] args) {
		Bayes test = new Bayes(new File("src/data3.txt"));
		try {
			System.out.println(test.classifyData());
		} catch (FileNotFoundException e) {

		}

	}

}
