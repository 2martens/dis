package de.dis2017.data.db;

import de.dis2017.data.Article;
import de.dis2017.data.Date;
import de.dis2017.data.Sale;
import de.dis2017.data.Shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ORM {
    private Connection _connection;
    
    /**
     * Initializes the ORM.
     */
    public ORM() {
        DB2ConnectionManager _dbManager = DB2ConnectionManager.getInstance();
        _connection = _dbManager.getConnection();
    }
    
    public void setAutoCommit(boolean autoCommit) {
        try {
            _connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void commit() {
        try {
            _connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void rollback() {
        try {
            _connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Article> getArticles()
    {
        List<Article> articles = new ArrayList<>();
        String                selectSQL = "SELECT a.articleID, a.name AS name, pg.name AS productGroup, " +
                                          "pf.name AS productFamily, pc.name AS productCategory " +
                                          "FROM DB2INST1.ARTICLEID AS a, DB2INST1.PRODUCTGROUPID AS pg," +
                                          "DB2INST1.PRODUCTFAMILYID AS pf, DB2INST1.PRODUCTCATEGORYID AS pc " +
                                          "WHERE a.PRODUCTGROUPID = pg.PRODUCTGROUPID " +
                                          "AND pg.PRODUCTFAMILYID = pf.PRODUCTFAMILYID " +
                                          "AND pf.PRODUCTCATEGORYID = pc.PRODUCTCATEGORYID ";
        try {
            PreparedStatement pstmt = _connection.prepareStatement(selectSQL);
            ResultSet         rs    = pstmt.executeQuery();
    
            while (rs.next()) {
                Article article = new Article();
                article.set_articleID(rs.getInt("articleID"));
                article.set_name(rs.getString("name"));
                article.set_productGroup(rs.getString("productGroup"));
                article.set_productFamily(rs.getString("productFamily"));
                article.set_productCategory(rs.getString("productCategory"));
                articles.add(article);
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return articles;
    }
    
    public List<Shop> getShops()
    {
        List<Shop> shops = new ArrayList<>();
        String                selectSQL = "SELECT s.shopID, s.name AS name, c.name AS city, " +
                                          "r.name AS region, n.name AS country " +
                                          "FROM DB2INST1.SHOPID AS s, DB2INST1.STADTID AS c," +
                                          "DB2INST1.REGIONID r, DB2INST1.LANDID AS n " +
                                          "WHERE s.STADTID = c.STADTID " +
                                          "AND c.REGIONID = r.REGIONID " +
                                          "AND r.LANDID = n.LANDID";
        try {
            PreparedStatement pstmt = _connection.prepareStatement(selectSQL);
            ResultSet         rs    = pstmt.executeQuery();
        
            while (rs.next()) {
                Shop shop = new Shop();
                shop.set_shopID(rs.getInt("shopID"));
                shop.set_name(rs.getString("name"));
                shop.set_city(rs.getString("city"));
                shop.set_region(rs.getString("region"));
                shop.set_country(rs.getString("country"));
                shops.add(shop);
            }
        
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return shops;
    }
    
    public void createArticles(List<Article> articles) {
        String insertSQL = "INSERT INTO VSISP12.ARTICLE (ID, name, productgroup, productfamily, productcategory)" +
                           "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = _connection.prepareStatement(insertSQL);
            for (Article article : articles) {
                pstmt.setInt(1, article.get_articleID());
                pstmt.setString(2, article.get_name());
                pstmt.setString(3, article.get_productGroup());
                pstmt.setString(4, article.get_productFamily());
                pstmt.setString(5, article.get_productCategory());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void createShops(List<Shop> shops) {
        String insertSQL = "INSERT INTO VSISP12.SHOP (ID, name, city, region, country)" +
                           "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = _connection.prepareStatement(insertSQL);
            
            for (Shop shop : shops) {
                pstmt.setInt(1, shop.get_shopID());
                pstmt.setString(2, shop.get_name());
                pstmt.setString(3, shop.get_city());
                pstmt.setString(4, shop.get_region());
                pstmt.setString(5, shop.get_country());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void createDates(List<Date> dates) {
        String insertSQL = "INSERT INTO VSISP12.DATETABLE (ID, day, month, quarter, year)" +
                           "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = _connection.prepareStatement(insertSQL);
            
            for (Date date : dates) {
                pstmt.setInt(1, date.get_dateID());
                pstmt.setInt(2, date.get_day());
                pstmt.setInt(3, date.get_month());
                pstmt.setInt(4, date.get_quarter());
                pstmt.setInt(5, date.get_year());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void createSales(List<Sale> sales) {
        String insertSQL = "INSERT INTO VSISP12.SALES (dateID, storeID, articleID, soldunits, earnings)" +
                           "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = _connection.prepareStatement(insertSQL);
            
            for (Sale sale: sales) {
                pstmt.setInt(1, sale.get_dateID());
                pstmt.setInt(2, sale.get_shopID());
                pstmt.setInt(3, sale.get_articleID());
                pstmt.setInt(4, sale.get_sold());
                pstmt.setFloat(5, sale.get_earnings());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // --- analysis part starts here
    
    /**
     * Queries the cross table.
     *
     * @param year the year to be queried
     * @param productDimension the column of the productDimension to be used (name for article name, productgroup,
     *                         productfamily, productcategory)
     * @param locationDimension the column of the locationDimension to be used (name for shop name, city, region, country)
     * @param timeDimension the column of the timeDimension to be used (month, quarter, year)
     * @return the cross table
     */
    public Map<String,Map<String, Map<String, Integer>>> getSalesCrossTable(int year,
                                                                            String productDimension,
                                                                            String locationDimension,
                                                                            String timeDimension) {
        String querySQL = "SELECT SUM(s.SOLDUNITS) AS sales, a." + productDimension + " AS article, sh." +
                          locationDimension + " AS city, " +
                          "d." + timeDimension + " AS quarter " +
                          "FROM VSISP12.SALES AS s, " +
                          "VSISP12.DATETABLE AS d, " +
                          "VSISP12.SHOP AS sh, " +
                          "VSISP12.ARTICLE AS a " +
                          "WHERE s.DATEID = d.ID " +
                          "AND s.STOREID = sh.ID " +
                          "AND s.ARTICLEID = a.ID " +
                          "AND d.YEAR = " + year + " " +
                          "GROUP BY GROUPING SETS ( (), (sh." + locationDimension + "), (a." + productDimension + "), " +
                          "(d." + timeDimension + "), " +
                          "(sh." + locationDimension + ", a." + productDimension + "), " +
                          "(d." + timeDimension + ", sh." + locationDimension + "), " +
                          "(d." + timeDimension + ", a." + productDimension + "), " +
                          "(sh." + locationDimension + ", a." + productDimension + ", d." + timeDimension + ") )";
        Map<String,Map<String, Map<String, Integer>>> sales = new HashMap<>();
        
        try {
            PreparedStatement pstmt = _connection.prepareStatement(querySQL);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int salesnr = rs.getInt("sales");
                String article = rs.getString("article");
                String city = rs.getString("city");
                int quarter = rs.getInt("quarter");
    
                Map<String, Map<String, Integer>> timeMap;
                if (city == null) {
                    timeMap = sales.getOrDefault("total", new HashMap<>());
                }
                else {
                    timeMap = sales.getOrDefault(city, new HashMap<>());
                }
    
                Map<String, Integer> productMap;
                if (quarter == 0) {
                    productMap = timeMap.getOrDefault("total", new HashMap<>());
                }
                else {
                    productMap = timeMap.getOrDefault(String.valueOf(quarter), new HashMap<>());
                }
                
                // add specific sales number to product map
                if (article == null) {
                    productMap.put("total", salesnr);
                }
                else {
                    productMap.put(article, salesnr);
                }
                
                // add product map to time map
                if (quarter == 0) {
                    timeMap.put("total", productMap);
                }
                else {
                    timeMap.put(String.valueOf(quarter), productMap);
                }
                
                // add time map to sales map
                if (city == null) {
                    sales.put("total", timeMap);
                }
                else {
                    sales.put(city, timeMap);
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return sales;
    }
}
