package de.dis2017.data;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Date {
    private static Map<String, Date> _dates;
    private static List<Date>        _datesList;
    
    private int _dateID;
    private int _day;
    private int _month;
    private int _quarter;
    private int _year;
    
    static {
        _dates = new HashMap<>();
        _datesList = new ArrayList<>();
    }
    
    public int get_dateID() {
        return _dateID;
    }
    
    public void set_dateID(int _dateID) {
        this._dateID = _dateID;
    }
    
    public int get_day() {
        return _day;
    }
    
    public void set_day(int _day) {
        this._day = _day;
    }
    
    public int get_month() {
        return _month;
    }
    
    public void set_month(int _month) {
        this._month = _month;
        switch (this._month) {
            case 1:
            case 2:
            case 3:
                _quarter = 1;
                break;
            case 4:
            case 5:
            case 6:
                _quarter = 2;
                break;
            case 7:
            case 8:
            case 9:
                _quarter = 3;
                break;
            case 10:
            case 11:
            case 12:
                _quarter = 4;
                break;
        }
    }
    
    public int get_quarter() {
        return _quarter;
    }
    
    public void set_quarter(int _quarter) {
        this._quarter = _quarter;
    }
    
    public int get_year() {
        return _year;
    }
    
    public void set_year(int _year) {
        this._year = _year;
    }
    
    public static Date parse(String date) {
        if (_dates.containsKey(date)) {
            return _dates.get(date);
        }
        
        String[] elements = date.split(".");
        Date newDate = new Date();
        newDate.set_dateID(Integer.parseInt(elements[2] + elements[1] + elements[0]));
        newDate.set_day(Integer.parseInt(elements[0]));
        newDate.set_month(Integer.parseInt(elements[1]));
        newDate.set_year(Integer.parseInt(elements[2]));
        
        _dates.put(date, newDate);
        _datesList.add(newDate);
        
        return newDate;
    }
    
    @Contract(pure = true)
    public static List<Date> getDates() {
        return _datesList;
    }
}
