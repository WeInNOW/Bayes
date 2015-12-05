package richarkc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;

public class Bayes {

	@SuppressWarnings("unchecked")
	public HashMap<String, Double>[] classifiers = new HashMap[5];
	private File data;
	private int[] c = new int[5];
	private int classNum;
	
	public Bayes(File data){
		this.data = data;
	}
	
	private void parseData() throws FileNotFoundException{
		for (int i = 0; i < 5; i++){
			classifiers[i] = new HashMap<String, Double>();
		}
		
		Scanner sc = new Scanner(data);
		HashMap<String, Double> tmp = null;
		while (sc.hasNext()){
			//Split the line into individual attributes
			String temp[] = sc.nextLine().split(" ");
			//get classifier and hashmap for that classifier
			int cl = Integer.parseInt(temp[temp.length - 1]);
			tmp = classifiers[cl];
			//increment occurence of classifier
			c[cl]++;
			for (int i = 0; i < temp.length - 1; i++){
				//increment attribute given the classifier
				double k = tmp.get(temp[i]);
				k++;
				tmp.put(temp[i], k);
			}
			
			
		}
		sc.close();
		for (int i = 0; i < 5; i++){
			//figure out how many classifiers there are
			if (c[i] == 0)
				classNum = i;
		}
		
		//change numbers into probabilities
		for (int i = 0; i < classNum; i++){
			//iterating through map used from previous work, originally from StackOverflow source: http://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
			Iterator<Entry<String, Double>> it = classifiers[i].entrySet().iterator();
			while (it.hasNext()){
				Map.Entry<String, Double> pair = (Map.Entry<String, Double>)it.next();
				double prob = (double)pair.getValue();
				pair.setValue(prob / c[i]);
				it.remove();
			}
		}
		
		
		
	}
	
	public static void main(String[] args){
		
	}
	
	
	
}
