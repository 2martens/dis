package de.dis2017.data;

import java.sql.Date;

/**
 * Contract data class
 */
public class Contract {
    private int contractNo = -1;
    private Date   date;
    private String place;
    private int    person;
    private int    estate;
    
    int getEstate() {
        return estate;
    }
    
    void setEstate(int estate) {
        this.estate = estate;
    }
    
    public int getContractNo() {
        return contractNo;
    }
    
    public void setContractNo(int contractNo) {
        this.contractNo = contractNo;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getPlace() {
        return place;
    }
    
    public void setPlace(String place) {
        this.place = place;
    }
    
    public int getPerson() {
        return person;
    }
    
    public void setPerson(int person) {
        this.person = person;
    }
}
