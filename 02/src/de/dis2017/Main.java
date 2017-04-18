package de.dis2017;

import de.dis2017.data.Apartment;
import de.dis2017.data.Estate;
import de.dis2017.data.EstateAgent;
import de.dis2017.data.House;
import de.dis2017.data.db.ORM;

/**
 * Main class
 */
public class Main {
    private static ORM _orm;
    
	/**
	 * Starts the application.
	 */
	public static void main(String[] args) {
		_orm = new ORM();
	    showMainMenu();
	}
	
	/**
	 * Shows the main menu.
	 */
	private static void showMainMenu() {
		// menu options
		final int MENU_AGENT = 0;
		final int MENU_ESTATES = 1;
		final int QUIT = 2;
		
		// create menu
		Menu mainMenu = new Menu("Main menu");
		mainMenu.addEntry("EstateAgent management", MENU_AGENT);
		mainMenu.addEntry("Estate management", MENU_ESTATES);
		mainMenu.addEntry("Quit", QUIT);
		
		// process input
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_AGENT:
					showEstateAgentMenu();
					break;
				case MENU_ESTATES:
					showEstateMenu();
					break;
				case QUIT:
					return;
			}
		}
	}
	
	/**
	 * TODO Shows the estate management.
	 */
	private static void showEstateMenu() {
		String username = FormUtil.readString("Username");
		String password = FormUtil.readString("Password");
		// TODO Check username password combination else back to MainMenu
		System.out.println("EstateAgent "+username+" logged in succesfully.");
		
		// menu options
		final int NEW_ESTATE = 0;
		final int CHANGE_ESTATE = 1;
		final int DELETE_ESTATE = 2;
		final int BACK = 3;
		
		// estate management menu
		Menu estateMenu = new Menu("Estate management");
		estateMenu.addEntry("Create Estate", NEW_ESTATE);
		estateMenu.addEntry("Change Estate", CHANGE_ESTATE);
		estateMenu.addEntry("Delete Estate", DELETE_ESTATE);
		estateMenu.addEntry("Back to the main menu", BACK);
		
		// process input
		while(true) {
			int response = estateMenu.show();
			
			switch(response) {
				case NEW_ESTATE:
					newEstate();
					break;
				case CHANGE_ESTATE:
					changeEstate();
					break;
				case DELETE_ESTATE:
					deleteEstate();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Creates a new estate agent after the usesr has entered the necessary data.
	 */
	private static void newEstate() {
		String input = FormUtil.readString("Apartmnet(A)/House(H)");
		boolean apartment = input=="A"||input=="a";
		if(apartment){
			Apartment estate = new Apartment();
			
			estate.setCity(FormUtil.readString("Name"));
			estate.setPostalcode(FormUtil.readInt("Postal Code"));
			estate.setStreet(FormUtil.readString("Street"));
			estate.setStreetnumber(FormUtil.readInt("Street Number"));
			estate.setSquareArea(FormUtil.readInt("Square Area"));

			estate.setFloor(FormUtil.readInt("Floor"));
			estate.setRent(FormUtil.readInt("Rent"));
			estate.setRooms(FormUtil.readInt("Rooms"));
			input = FormUtil.readString("Balcony(Y/N)");
			estate.setBalcony(input=="Y"||input=="y");
			input = FormUtil.readString("Built-in Kitchen(Y/N)");
			estate.setBuiltinKitchen(input=="Y"||input=="y");
			
			//_orm.persist(estate);
			
			System.out.println("Estate with the ID " + estate.getId() + " was created.");
		}
		else{
			House estate = new House();
			
			estate.setCity(FormUtil.readString("Name"));
			estate.setPostalcode(FormUtil.readInt("Postal Code"));
			estate.setStreet(FormUtil.readString("Street"));
			estate.setStreetnumber(FormUtil.readInt("Street Number"));
			estate.setSquareArea(FormUtil.readInt("Square Area"));

			estate.setFloors(FormUtil.readInt("Floors"));
			estate.setPrice(FormUtil.readInt("Price"));
			input = FormUtil.readString("Garden(Y/N)");
			estate.setGarden(input=="Y"||input=="y");
			
			//_orm.persist(estate);
			
			System.out.println("Estate with the ID " + estate.getId() + " was created.");
		}
	}
	
	/**
	 * TODO Change an estate agent after the usesr has entered the necessary data.
	 */
	private static void changeEstate() {
		//Choose Estate from List
		Estate estate = new Apartment();//choosen Estate
		
		estate.setCity(FormUtil.readString("Name"));
		estate.setPostalcode(FormUtil.readInt("Postal Code"));
		estate.setStreet(FormUtil.readString("Street"));
		estate.setStreetnumber(FormUtil.readInt("Street Number"));
		estate.setSquareArea(FormUtil.readInt("Square Area"));
		
		if(estate instanceof Apartment){
			Apartment apartment = (Apartment) estate;
			

			apartment.setFloor(FormUtil.readInt("Floor"));
			apartment.setRent(FormUtil.readInt("Rent"));
			apartment.setRooms(FormUtil.readInt("Rooms"));
			String input = FormUtil.readString("Balcony(Y/N)");
			apartment.setBalcony(input=="Y"||input=="y");
			input = FormUtil.readString("Built-in Kitchen(Y/N)");
			apartment.setBuiltinKitchen(input=="Y"||input=="y");
			
			//_orm.persist(apartment);
			
			System.out.println("Estate with the ID " + estate.getId() + " was updated.");
		}
		else{
			House house = (House)estate;
			
			estate.setCity(FormUtil.readString("Name"));
			estate.setPostalcode(FormUtil.readInt("Postal Code"));
			estate.setStreet(FormUtil.readString("Street"));
			estate.setStreetnumber(FormUtil.readInt("Street Number"));
			estate.setSquareArea(FormUtil.readInt("Square Area"));

			house.setFloors(FormUtil.readInt("Floors"));
			house.setPrice(FormUtil.readInt("Price"));
			String input = FormUtil.readString("Garden(Y/N)");
			house.setGarden(input=="Y"||input=="y");
			
			//_orm.persist(house);
			
			System.out.println("Estate with the ID " + estate.getId() + " was updated.");
		}
	}
	
	/**
	 * TODO Deletes an estate.
	 */
	private static void deleteEstate() {
		//Choose estate from list.
		Estate estate = new Apartment();//choosen estate.
		//Delete Agent
		System.out.println("Estate with the ID " + estate.getId() + " was deleted.");
	}
	
	/**
	 * Shows the estate agent management.
	 */
	private static void showEstateAgentMenu() {
		// menu options
		final int NEW_AGENT = 0;
		final int CHANGE_AGENT = 1;
		final int DELETE_AGENT = 2;
		final int BACK = 3;
		
		// estate management menu
		Menu estateAgentMenu = new Menu("EstateAgent management");
		estateAgentMenu.addEntry("Create EstateAgent", NEW_AGENT);
		estateAgentMenu.addEntry("Change EstateAgent", CHANGE_AGENT);
		estateAgentMenu.addEntry("Delete EstateAgent", DELETE_AGENT);
		estateAgentMenu.addEntry("Back to the main menu", BACK);
		
		// process input
		while(true) {
			int response = estateAgentMenu.show();
			
			switch(response) {
				case NEW_AGENT:
					newEstateAgent();
					break;
				case CHANGE_AGENT:
					changeEstateAgent();
					break;
				case DELETE_AGENT:
					deleteEstateAgent();
					break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Creates a new estate agent after the usesr has entered the necessary data.
	 */
	private static void newEstateAgent() {
		EstateAgent agent = new EstateAgent();
		
		agent.setName(FormUtil.readString("Name"));
		agent.setAddress(FormUtil.readString("Address"));
		agent.setLogin(FormUtil.readString("Login"));
		agent.setPassword(FormUtil.readString("Password"));
		
		_orm.persist(agent);
		
		System.out.println("EstateAgent with the ID " + agent.getId() + " was created.");
	}
	
	/**
	 * TODO Change an estate agent after the usesr has entered the necessary data.
	 */
	private static void changeEstateAgent() {
		EstateAgent agent = new EstateAgent();//logged in Agent
		
		agent.setName(FormUtil.readString("Name"));
		agent.setAddress(FormUtil.readString("Address"));
		agent.setLogin(FormUtil.readString("Login"));
		agent.setPassword(FormUtil.readString("Password"));
		
		//_orm.persist(agent);
		
		System.out.println("EstateAgent with the ID " + agent.getId() + " was updated.");
	}
	
	/**
	 * TODO Deletes an estate agent.
	 */
	private static void deleteEstateAgent() {
		//Delete Agent
		EstateAgent agent = new EstateAgent();//logged in Agent
		System.out.println("EstateAgent with the ID " + agent.getId() + " was deleted.");
	}
}
