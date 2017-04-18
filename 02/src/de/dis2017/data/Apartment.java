package de.dis2017.data;

public class Apartment extends Estate{
	int floor;
	int rent;
	int rooms;
	boolean balcony;
	boolean builtinKitchen;
	
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
	public boolean isBalcony() {
		return balcony;
	}
	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}
	public boolean isBuiltinKitchen() {
		return builtinKitchen;
	}
	public void setBuiltinKitchen(boolean builtinKitchen) {
		this.builtinKitchen = builtinKitchen;
	}
}
