package de.dis2017;

import de.dis2017.data.Article;
import de.dis2017.data.Date;
import de.dis2017.data.Sale;
import de.dis2017.data.Shop;
import de.dis2017.data.db.CSVScanner;
import de.dis2017.data.db.ORM;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static ORM _orm;
    private static List<Article> _articles;
    private static List<Shop> _shops;
    private static List<Date> _dates;
    private static List<Sale> _sales;
    
    /**
     * Starts the application.
     */
    public static void main(String[] args) {
        _orm = new ORM();
        System.out.println("Begin Extract process..");
        extract();
        load();
    }
    
    private static void extract()
    {
        System.out.println("Load product dimension data from database..");
        _articles = _orm.getArticles();
        Map<String, Integer> articleNames = new HashMap<>();
        for (Article article : _articles) {
            articleNames.put(article.get_name(), article.get_articleID());
        }
    
        System.out.println("Load location dimension data from database..");
        _shops        = _orm.getShops();
        Map<String, Integer> shopNames    = new HashMap<>();
        for (Shop shop : _shops) {
            shopNames.put(shop.get_name(), shop.get_shopID());
        }
        
        try {
            System.out.println("Scan the CSV file..");
            List<List<String>> csvEntries = CSVScanner.scan("sales.csv");
            System.out.println("Finish Extract process");
            List<Sale>         sales      = new ArrayList<>();
            System.out.println("Begin Transform process..");
            System.out.println("Transform each CSV row into one Sale object..");
            System.out.println("..and create one Date object for each date (multiple Sale objects share one such " +
                               "object)..");
            for (List<String> row : csvEntries) {
                Sale   sale    = new Sale();
                String date    = row.get(0);
                String shop    = row.get(1);
                String article = row.get(2);
                int    sold    = Integer.valueOf(row.get(3));
                float  earned  = Float.valueOf(row.get(4).replace(',', '.'));
    
                sale.set_dateID(Date.parse(date).get_dateID());
                sale.set_shopID(shopNames.get(shop));
                sale.set_articleID(articleNames.get(article));
                sale.set_sold(sold);
                sale.set_earnings(earned);
                sales.add(sale);
            }
            _sales = sales;
            System.out.println("Finish Transform process.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        System.out.println("Retrieve all Date objects which were created during Transform");
        _dates = Date.getDates();
    }
    
    private static void load()
    {
        System.out.println("Begin Load process..");
        _orm.setAutoCommit(false);
        System.out.println("Write product dimension data to database..");
        _orm.createArticles(_articles);
        System.out.println("Write date dimension data to database..");
        _orm.createDates(_dates);
        System.out.println("Write location dimension data to database..");
        _orm.createShops(_shops);
        System.out.println("Write sales (fact) data to database..");
        _orm.createSales(_sales);
        _orm.commit();
        _orm.setAutoCommit(true);
        System.out.println("Finish Load process");
    }
}
