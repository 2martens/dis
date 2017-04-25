package de.dis2017.data;

public class Apartment extends Estate {
    private int     floor;
    private int     rent;
    private int     rooms;
    private boolean balcony;
    private boolean builtinKitchen;
    
    public Apartment() {
        super();
    }
    
    public Apartment(Estate estate) {
        this.setId(estate.getId());
        this.setCity(estate.getCity());
        this.setPostalCode(estate.getPostalCode());
        this.setStreet(estate.getStreet());
        this.setStreetNumber(estate.getStreetNumber());
        this.setSquareArea(estate.getSquareArea());
        this.setAgent(estate.getAgent());
    }
    
    public int getFloor() {
        return floor;
    }
    
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    public int getRent() {
        return rent;
    }
    
    public void setRent(int rent) {
        this.rent = rent;
    }
    
    public int getRooms() {
        return rooms;
    }
    
    public void setRooms(int rooms) {
        this.rooms = rooms;
    }
    
    public boolean hasBalcony() {
        return balcony;
    }
    
    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }
    
    public boolean hasBuiltinKitchen() {
        return builtinKitchen;
    }
    
    public void setBuiltinKitchen(boolean builtinKitchen) {
        this.builtinKitchen = builtinKitchen;
    }
}
