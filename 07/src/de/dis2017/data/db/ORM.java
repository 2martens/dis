package de.dis2017.data.db;

import de.dis2017.data.Article;
import de.dis2017.data.Shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ORM {
    private Connection _connection;
    
    /**
     * Initializes the ORM.
     */
    public ORM() {
        DB2ConnectionManager _dbManager = DB2ConnectionManager.getInstance();
        _connection = _dbManager.getConnection();
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
}
