package de.dis2017.data;

public class House extends Estate {
    private int     floors;
    private int     price;
    private boolean garden;
    
    public House() {
        super();
    }
    
    public House(Estate estate) {
        this.setId(estate.getId());
        this.setCity(estate.getCity());
        this.setPostalCode(estate.getPostalCode());
        this.setStreet(estate.getStreet());
        this.setStreetNumber(estate.getStreetNumber());
        this.setSquareArea(estate.getSquareArea());
        this.setAgent(estate.getAgent());
    }
    
    public int getFloors() {
        return floors;
    }
    
    public void setFloors(int floors) {
        this.floors = floors;
    }
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public boolean hasGarden() {
        return garden;
    }
    
    public void setGarden(boolean garden) {
        this.garden = garden;
    }
    
    
}
