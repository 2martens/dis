package de.dis2017.data;

public class Article {
    private int _articleID;
    private int _productGroupID;
    private String _name;
    private float _price;
    
    public int get_articleID() {
        return _articleID;
    }
    
    public void set_articleID(int articleID) {
        _articleID = articleID;
    }
    
    public int get_productGroupID() {
        return _productGroupID;
    }
    
    public void set_productGroupID(int productGroupID) {
        _productGroupID = productGroupID;
    }
    
    public String get_name() {
        return _name;
    }
    
    public void set_name(String name) {
        _name = name;
    }
    
    public float get_price() {
        return _price;
    }
    
    public void set_price(float price) {
        _price = price;
    }
}
