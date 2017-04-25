package de.dis2017.data;

/**
 * EstateAgent data class
 */
public class Estate {
    private int id = -1;
    private String city;
    private String postalCode;
    private String street;
    private int    streetNumber;
    private int    squareArea;
    private int    agent;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public int getStreetNumber() {
        return streetNumber;
    }
    
    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }
    
    public int getSquareArea() {
        return squareArea;
    }
    
    public void setSquareArea(int squareArea) {
        this.squareArea = squareArea;
    }
    
    public int getAgent() { return agent; }
    
    public void setAgent(int agent) { this.agent = agent; }
}
