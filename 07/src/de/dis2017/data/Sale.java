package de.dis2017.data;


import java.util.Date;

public class Sale {
    private Date _date;
    private int _shopID;
    private int _articleID;
    private int _sold;
    private int _earnings;
    
    
    public Date get_date() {
        return _date;
    }
    
    public void set_date(Date date) {
        _date = date;
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
    
    public int get_earnings() {
        return _earnings;
    }
    
    public void set_earnings(int earnings) {
        _earnings = earnings;
    }
}
