package blatt8;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Apriori {

	
	static List<int[]> itemsets = new ArrayList<int[]>();
	static float minSup = 0.01f;
	static String path = "transactions.txt";
	static int numItems=0;
	static int numTransactions=0;
	
	private static void analyzeTransactionFile(){
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	numTransactions++;
		    	String[] products = line.split("\\s+");
		    	for(int i=0;i<products.length;i++){
		    		if(Integer.parseInt(products[i])+1>numItems){
		    			numItems = Integer.parseInt(products[i])+1;
		    		}
		    	}
		    }
		}
		catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Num Transactions:"+numTransactions);
		System.out.println("Num Items:"+numItems);
	}
	
	
	private static void AprioriAlgorithm(){
		int k=1;
		//Calculate L1
		System.out.println(k+"-Itemsets");
		find_frequent_1_itemsets();
        for (k=2;itemsets.size()>0;k++)
        {
    		System.out.println("\n"+k+"-Itemsets");
            generateCandidates();
            
            calculateFrequentItemsets();
        } 
	}
	

	private static void generateCandidates() {
		int k = itemsets.get(0).length+1;
		List<int[]> candidates = new ArrayList<int[]>();
		for(int i=0; i<itemsets.size(); i++)
        {
            for(int j=0; j<itemsets.size(); j++)
            {
                int[] I1 = itemsets.get(i);
                int[] I2 = itemsets.get(j);
                boolean match=true;
                for(int l=0;l<k-2;l++){
                	if(I1[l]!=I2[l]){
                		match = false;
                		continue;
                	}
                }
                if(match){
                	if(I1[k-2]<I2[k-2]){
                		int[] c = new int[k];
                		for(int l=0;l<k-1;l++){
                			c[l]=I1[l];
                    	}
                		c[k-1]=I2[k-2];
                		if(!prune(c)){
                			candidates.add(c);
                		}
                	}
                }
            }
        }
		itemsets=new ArrayList<int[]>(candidates);
		if(itemsets.size()>0)System.out.println(itemsets.size()+" Candidates for k="+itemsets.get(0).length);
		else System.out.println("0 Candidates");
	}


	private static boolean prune(int[] c) {
//		if(new Random().nextInt(50)==0)return false;
		return false;
//		for(int skip=0;skip<c.length;skip++){
//			int[] subset = new int[c.length-1];
//			for(int i=0;i<c.length-1;i++){
//				if(i>=skip)
//					subset[i]=c[i+1];
//				else
//					subset[i]=c[i];
//			}
//			boolean match=false;
//			for(int i=0;i<itemsets.size();i++){
//				if(compare(itemsets.get(i),subset)){
//					match=true;
//					break;
//				}
//			}
//			if(!match)
//				return true;
//			
//		}
//		return false;
		
	}
	
	private static boolean contains(int[] a, int[] b){
		for(int j=0;j<b.length;j++){
			boolean match=false;
			for(int i=0;i<a.length;i++){
				if(b[j]==a[i]){
					match = true;
				}
			}
			if(!match)return false;
		}
		return true;
	}

	private static boolean compare(int[] a, int[] b){
		for(int i=0;i<a.length;i++){
			if(a[i]!=b[i]){
				return false;
			}
		}
		return true;
	}

	private static void find_frequent_1_itemsets() {
		itemsets = new ArrayList<int[]>();
        for(int i=0; i<numItems; i++)
        {
        	int[] cand = {i};
        	itemsets.add(cand);
        }
		System.out.println(itemsets.size()+" Candidates for k="+itemsets.get(0).length);
        calculateFrequentItemsets();
	}

	private static void calculateFrequentItemsets()
    {
		List<int[]> frequent = new ArrayList<int[]>();
		int[] counter = new int[itemsets.size()];
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] products = line.split("\\s+");
		    	int trans[] = new int[products.length];
		    	for(int i=0;i<trans.length;i++){
		    		trans[i]=Integer.parseInt(products[i]);
		    	}
		    	for(int cand=0;cand<itemsets.size();cand++){
		    		if(contains(trans,itemsets.get(cand))){
		    			counter[cand]++;
		    		}
		    	}
		    }
		}
		catch(IOException e){
			e.printStackTrace();
		}
		for(int i=0;i<counter.length;i++){
			
			if((float)counter[i]/(float)numTransactions>=minSup){
				frequent.add(itemsets.get(i));
				printItemset(itemsets.get(i));
			}
		}
		itemsets = frequent;
		if(itemsets.size()>0)System.out.println("Total Number Frequent "+itemsets.get(0).length+"-Itemsets: "+itemsets.size());
		else System.out.println("0 Frequent");
		
    }

	private static void printItemset(int[] is) {
		String print="";
		for(int i=0;i<is.length;i++){
			print+=is[i]+" - ";
		}
		System.out.println(print);
	}


	public static void main(String[] args) {
		analyzeTransactionFile();
		AprioriAlgorithm();
		
		
	}

}
