package de.dis2017.data;


import java.util.Date;

public class Sale {
    private int _dateID;
    private int _shopID;
    private int _articleID;
    private int _sold;
    private float _earnings;
    
    
    public int get_dateID() {
        return _dateID;
    }
    
    public void set_dateID(int dateID) {
        _dateID = dateID;
    }
    
    public int get_shopID() {
        return _shopID;
    }
    
    public void set_shopID(int shopID) {
        _shopID = shopID;
    }
    
    public int get_articleID() {
        return _articleID;
    }
    
    public void set_articleID(int articleID) {
        _articleID = articleID;
    }
    
    public int get_sold() {
        return _sold;
    }
    
    public void set_sold(int sold) {
        _sold = sold;
    }
    
    public float get_earnings() {
        return _earnings;
    }
    
    public void set_earnings(float earnings) {
        _earnings = earnings;
    }
}
