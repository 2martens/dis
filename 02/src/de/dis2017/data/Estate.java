package de.dis2017.data;

/**
 * EstateAgent data class
 */
public abstract class Estate {
	private int id = -1;
	private String city;
	private int postalcode;
	private String street;
	private int streetnumber;
	private int squarearea;
	
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
	
	public int getPostalcode() {
		return postalcode;
	}
	
	public void setPostalcode(int postalcode) {
		this.postalcode = postalcode;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public int getStreetNumber() {
		return streetnumber;
	}
	
	public void setStreetnumber(int streetnumber) {
		this.streetnumber = streetnumber;
	}
	
	public int getSquareArea() {
		return streetnumber;
	}
	
	public void setSquareArea(int squarearea) {
		this.squarearea = squarearea;
	}
}
