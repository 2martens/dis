package de.dis2017.data;

public class Shop {
    private int _shopID;
    private String _name;
    private String _city;
    private String _region;
    private String _country;
    
    public int get_shopID() {
        return _shopID;
    }
    
    public void set_shopID(int shopID) {
        _shopID = shopID;
    }
    
    public String get_name() {
        return _name;
    }
    
    public void set_name(String name) {
        _name = name;
    }
    
    public String get_city() {
        return _city;
    }
    
    public void set_city(String _city) {
        this._city = _city;
    }
    
    public String get_region() {
        return _region;
    }
    
    public void set_region(String _region) {
        this._region = _region;
    }
    
    public String get_country() {
        return _country;
    }
    
    public void set_country(String _country) {
        this._country = _country;
    }
}
