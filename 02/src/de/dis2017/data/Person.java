package de.dis2017.data;

public class Person {
    private int id = -1;
    private String firstName;
    private String name;
    private String address;
    
    public int getId() { return id; }
    
    public void setId(int id) { this.id = id; }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String adress) {
        this.address = adress;
    }
    
    
}
