package de.dis2017;

import de.dis2017.data.db.ORM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainAnalysis {
    private static ORM _orm;
    
    /**
     * Starts the application.
     */
    public static void main(String[] args) {
        _orm = new ORM();
        Map<String,Map<String, Map<String, Integer>>> sales = _orm.getSalesCrossTable(2017);
        List<String>                                  articleNames = new ArrayList<>(sales.get("total").get("total").keySet());
        Collections.sort(articleNames);
        System.out.print("City | Time | Sales | ");
        int chars = 27;
        for (String article : articleNames) {
            if (article.equals("total")) continue;
            System.out.print(article + " | ");
            chars += article.length() + 3;
        }
        System.out.println("total");
        System.out.println(new String(new char[chars]).replace('\0', '-'));
        
        // cities
        List<String> cityNames = new ArrayList<>(sales.keySet());
        Collections.sort(cityNames);
        for (String city : cityNames) {
            if (city.equals("total")) continue;
            Map<String, Map<String, Integer>> timeMap = sales.get(city);
            List<String> times = new ArrayList<>(timeMap.keySet());
            Collections.sort(times);
            
            for (String time : times) {
                if (time.equals("total")) continue;
                Map<String, Integer> productMap = timeMap.get(time);
                System.out.print(city + " | " + time + " | " );
                for (String article : articleNames) {
                    if (article.equals("total")) continue;
                    System.out.print(productMap.get(article) + " | ");
                }
                System.out.println(productMap.get("total"));
                System.out.println(new String(new char[chars]).replace('\0', '-'));
            }
            Map<String, Integer> productMap = timeMap.get("total");
            System.out.print(city + " | total | " );
            for (String article : articleNames) {
                if (article.equals("total")) continue;
                System.out.print(productMap.get(article) + " | ");
            }
            System.out.println(productMap.get("total"));
            System.out.println(new String(new char[chars]).replace('\0', '-'));
        }
    
        System.out.println(new String(new char[chars]).replace('\0', '-'));
    
        Map<String, Map<String, Integer>> timeMap = sales.get("total");
        Map<String, Integer> productMap = timeMap.get("total");
        System.out.print("total | total | ");
        for (String article : articleNames) {
            if (article.equals("total")) continue;
            System.out.print(productMap.get(article) + " | ");
        }
        System.out.println(productMap.get("total"));
    }
}
