package de.dis2017;

import de.dis2017.data.db.ORM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MainAnalysis {
    private static ORM _orm;
    private static int _year;
    private static String _productDimension;
    private static String _locationDimension;
    private static String _timeDimension;
    
    /**
     * Starts the application.
     */
    public static void main(String[] args) {
        _orm = new ORM();
        _year = 2017;
        _productDimension = "PRODUCTFAMILY";
        _locationDimension = "NAME";
        _timeDimension = "MONTH";
        
        while (true) {
            Map<String, Map<String, Map<String, Integer>>> sales = _orm.getSalesCrossTable(_year,
                                                                                           _productDimension,
                                                                                           _locationDimension,
                                                                                           _timeDimension);
            printTable(sales, _timeDimension);
            boolean continueLoop = interact();
            if (!continueLoop) {
                break;
            }
        }
    }
    
    private static boolean interact() {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Re-Query [Yes, No]?");
            String reQuery = stdin.readLine();
            if (reQuery.equals("No")) {
                return false;
            }
            
            System.out.println("Product dimension (Article, Productgroup, Productfamily, Productcategory):");
            String productDimension = stdin.readLine();
            if (productDimension.equals("Article") || productDimension.equals("article")) {
                productDimension = "NAME";
            }
            else {
                productDimension = productDimension.toUpperCase();
            }
    
            System.out.println("Location dimension (Shop, City, Region, Country):");
            String locationDimension = stdin.readLine();
            if (locationDimension.equals("Shop") || locationDimension.equals("shop")) {
                locationDimension = "NAME";
            }
            else {
                locationDimension = locationDimension.toUpperCase();
            }
    
            System.out.println("Time dimension (Month, Quarter, Year):");
            String timeDimension = stdin.readLine();
            timeDimension = timeDimension.toUpperCase();
            
            // overwrite
            _productDimension = productDimension;
            _locationDimension = locationDimension;
            _timeDimension = timeDimension;
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private static String getMonth(int monthNr) {
        Map<Integer, String> months = new HashMap<>();
        months.put(1, "January");
        months.put(2, "February");
        months.put(3, "March");
        months.put(4, "April");
        months.put(5, "May");
        months.put(6, "June");
        months.put(7, "July");
        months.put(8, "August");
        months.put(9, "September");
        months.put(10, "October");
        months.put(11, "November");
        months.put(12, "December");
        
        return months.get(monthNr);
    }
    
    private static String getTimeCell(String timeDimension, String time) {
        String returnValue = "";
        switch (timeDimension) {
            case "QUARTER":
                returnValue = "quarter " + time + ", 2017";
                break;
            case "MONTH":
                returnValue = getMonth(Integer.valueOf(time)) + ", 2017";
                break;
            case "YEAR":
                returnValue = time;
                break;
        }
        
        return returnValue;
    }
    
    private static void printTable(Map<String, Map<String, Map<String, Integer>>> crossTable, String timeDimension) {
        List<String> articleNames = new ArrayList<>(crossTable.get("total").get("total").keySet());
        Collections.sort(articleNames);
        StringBuilder columnHeaderEdge = new StringBuilder("+------------------------+-----------------+");
        StringBuilder columnHeader     = new StringBuilder("| Location               | Time            |");
        StringBuilder        leftAlignFormat  = new StringBuilder("| %-22s | %15s |");
        
        for (String article : articleNames) {
            if (article.equals("total")) continue;
            int colSize = article.length();
            int fillUp = 0;
            if (colSize < 7) {
                fillUp = 7 - colSize;
                colSize = 7;
            }
            columnHeaderEdge.append(new String(new char[colSize + 2]).replace('\0', '-')).append("+");
            leftAlignFormat.append(" %").append(colSize).append("d |");
            columnHeader.append(" ").append(article).append(new String(new char[fillUp]).replace('\0', ' '))
                        .append(" |");
        }
        columnHeaderEdge.append("---------+%n");
        leftAlignFormat.append(" %7d |%n");
        columnHeader.append(" total   |%n");
        
        System.out.format(columnHeaderEdge.toString());
        System.out.format(columnHeader.toString());
        System.out.format(columnHeaderEdge.toString());
    
        // cities
        List<String> cityNames = new ArrayList<>(crossTable.keySet());
        Collections.sort(cityNames);
        boolean firstRun = true;
        for (String city : cityNames) {
            if (firstRun) {
                firstRun = false;
            }
            else {
                System.out.format(columnHeaderEdge.toString());
            }
            if (city.equals("total")) continue;
            Map<String, Map<String, Integer>> timeMap = crossTable.get(city);
            List<String>                      times   = new ArrayList<>(timeMap.keySet());
            Collections.sort(times);
            
            for (String time : times) {
                if (time.equals("total")) continue;
                Map<String, Integer> productMap = timeMap.get(time);
                List<Object> values = new ArrayList<>();
                values.add(city);
                values.add(getTimeCell(timeDimension, time));
                for (String article : articleNames) {
                    if (article.equals("total")) continue;
                    values.add(productMap.get(article));
                }
                values.add(productMap.get("total"));
                System.out.format(leftAlignFormat.toString(), unpack(values.toArray()));
            }
            Map<String, Integer> productMap = timeMap.get("total");
            List<Object> totalValues = new ArrayList<>();
            totalValues.add(city);
            totalValues.add("total");
            for (String article : articleNames) {
                if (article.equals("total")) continue;
                totalValues.add(productMap.get(article));
            }
            totalValues.add(productMap.get("total"));
            System.out.format(leftAlignFormat.toString(), unpack(totalValues.toArray()));
        }
        Map<String, Map<String, Integer>> timeMap = crossTable.get("total");
        List<String>                      times   = new ArrayList<>(timeMap.keySet());
        Collections.sort(times);
        for (String time : times) {
            if (time.equals("total")) continue;
            Map<String, Integer> productMap = timeMap.get(time);
            List<Object> values = new ArrayList<>();
            values.add("total");
            values.add(getTimeCell(timeDimension, time));
            for (String article : articleNames) {
                if (article.equals("total")) continue;
                values.add(productMap.get(article));
            }
            values.add(productMap.get("total"));
            System.out.format(leftAlignFormat.toString(), unpack(values.toArray()));
        }
        
        Map<String, Integer> productMap = timeMap.get("total");
        List<Object> globalTotalValues = new ArrayList<>();
        globalTotalValues.add("total");
        globalTotalValues.add("total");
        for (String article : articleNames) {
            if (article.equals("total")) continue;
            globalTotalValues.add(productMap.get(article));
        }
        globalTotalValues.add(productMap.get("total"));
        System.out.format(leftAlignFormat.toString(), unpack(globalTotalValues.toArray()));
        System.out.format(columnHeaderEdge.toString());
    }
    
    @SafeVarargs
    private static <E> Object[] unpack(E... objects) {
        List<Object> list = new ArrayList<Object>();
        for (Object object : objects) {
            if (object instanceof Object[]) {
                list.addAll(Arrays.asList((Object[]) object));
            }
            else{
                list.add(object);
            }
        }
        
        return list.toArray(new Object[list.size()]);
    }
}
