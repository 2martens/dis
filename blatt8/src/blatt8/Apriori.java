package blatt8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Apriori {
    
    
    private static List<int[]> itemsets        = new ArrayList<int[]>();
    private static float       minSup          = 0.01f;
    private static String      path            = "transactions.txt";
    private static int         numItems        = 0;
    private static int         numTransactions = 0;
    
    public static void main(String[] args) {
        analyzeTransactionFile();
        AprioriAlgorithm();
    }
    
    private static void analyzeTransactionFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                numTransactions++;
                String[] products = line.split("\\s+");
                for (String product : products) {
                    if (Integer.parseInt(product) + 1 > numItems) {
                        numItems = Integer.parseInt(product) + 1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Num Transactions:" + numTransactions);
        System.out.println("Num Items:" + numItems);
    }
    
    
    private static void AprioriAlgorithm() {
        int k = 1;
        //Calculate L1
        System.out.println(k + "-Itemsets");
        find_frequent_1_itemsets();
        for (k = 2; itemsets.size() > 0; k++) {
            System.out.println("\n" + k + "-Itemsets");
            generateCandidates();
            
            calculateFrequentItemsets();
        }
    }
    
    
    private static void generateCandidates() {
        int         k          = itemsets.get(0).length + 1;
        List<int[]> candidates = new ArrayList<>();
        for (int i = 0; i < itemsets.size(); i++) {
            for (int[] itemset : itemsets) {
                int[]   I1    = itemsets.get(i);
                boolean match = true;
                for (int l = 0; l < k - 2; l++) {
                    if (I1[l] != itemset[l]) {
                        match = false;
                    }
                }
                if (match) {
                    if (I1[k - 2] < itemset[k - 2]) {
                        int[] c = new int[k];
                        System.arraycopy(I1, 0, c, 0, k - 1);
                        c[k - 1] = itemset[k - 2];
                        if (!prune()) {
                            candidates.add(c);
                        }
                    }
                }
            }
        }
        itemsets = new ArrayList<int[]>(candidates);
        if (itemsets.size() > 0) {
            System.out.println(itemsets.size() + " Candidates for k=" + itemsets.get(0).length);
        } else {
            System.out.println("0 Candidates");
        }
    }
    
    private static boolean prune() {
        return false;
    }
    
    private static boolean contains(int[] a, int[] b) {
        for (int aB : b) {
            boolean match = false;
            for (int anA : a) {
                if (aB == anA) {
                    match = true;
                }
            }
            if (!match) return false;
        }
        return true;
    }
    
    private static boolean compare(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
    
    private static void find_frequent_1_itemsets() {
        itemsets = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            int[] cand = {i};
            itemsets.add(cand);
        }
        System.out.println(itemsets.size() + " Candidates for k=" + itemsets.get(0).length);
        calculateFrequentItemsets();
    }
    
    private static void calculateFrequentItemsets() {
        List<int[]> frequent = new ArrayList<>();
        int[]       counter  = new int[itemsets.size()];
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] products = line.split("\\s+");
                int      trans[]  = new int[products.length];
                for (int i = 0; i < trans.length; i++) {
                    trans[i] = Integer.parseInt(products[i]);
                }
                for (int cand = 0; cand < itemsets.size(); cand++) {
                    if (contains(trans, itemsets.get(cand))) {
                        counter[cand]++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < counter.length; i++) {
            
            if ((float) counter[i] / (float) numTransactions >= minSup) {
                frequent.add(itemsets.get(i));
                printItemset(itemsets.get(i));
            }
        }
        itemsets = frequent;
        if (itemsets.size() > 0) {
            System.out.println("Total Number Frequent " + itemsets.get(0).length + "-Itemsets: " + itemsets.size());
        } else {
            System.out.println("0 Frequent");
        }
        
    }
    
    private static void printItemset(int[] is) {
        StringBuilder print = new StringBuilder();
        for (int i1 : is) {
            print.append(i1).append(" - ");
        }
        System.out.println(print);
    }
    
}
