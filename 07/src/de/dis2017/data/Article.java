package de.dis2017.data;

public class Article {
    private int _articleID;
    private String _name;
    private String _productGroup;
    private String _productFamily;
    private String _productCategory;
    
    public int get_articleID() {
        return _articleID;
    }
    
    public void set_articleID(int articleID) {
        _articleID = articleID;
    }
    
    public String get_name() {
        return _name;
    }
    
    public void set_name(String name) {
        _name = name;
    }
    
    public String get_productGroup() {
        return _productGroup;
    }
    
    public void set_productGroup(String productGroup) {
        _productGroup = productGroup;
    }
    
    
    public String get_productFamily() {
        return _productFamily;
    }
    
    public void set_productFamily(String _productFamily) {
        this._productFamily = _productFamily;
    }
    
    public String get_productCategory() {
        return _productCategory;
    }
    
    public void set_productCategory(String _productCategory) {
        this._productCategory = _productCategory;
    }
}
