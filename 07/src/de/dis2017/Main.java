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
        extract();
        load();
    }
    
    private static void extract()
    {
        _articles = _orm.getArticles();
        Map<String, Integer> articleNames = new HashMap<>();
        for (Article article : _articles) {
            articleNames.put(article.get_name(), article.get_articleID());
        }
    
        _shops        = _orm.getShops();
        Map<String, Integer> shopNames    = new HashMap<>();
        for (Shop shop : _shops) {
            shopNames.put(shop.get_name(), shop.get_shopID());
        }
        
        try {
            List<List<String>> csvEntries = CSVScanner.scan("sales.csv");
            List<Sale>         sales      = new ArrayList<>();
            for (List<String> row : csvEntries) {
                Sale sale = new Sale();
                String date = row.get(0);
                String shop = row.get(1);
                String article = row.get(2);
                int sold = Integer.valueOf(row.get(3));
                int earned = Integer.valueOf(row.get(4));
    
                sale.set_dateID(Date.parse(date).get_dateID());
                sale.set_shopID(shopNames.get(shop));
                sale.set_articleID(articleNames.get(article));
                sale.set_sold(sold);
                sale.set_earnings(earned);
                sales.add(sale);
            }
            _sales = sales;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        _dates = Date.getDates();
    }
    
    private static void load()
    {
    
    }
}
