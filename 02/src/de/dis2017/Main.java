package de.dis2017;

import de.dis2017.data.Apartment;
import de.dis2017.data.Estate;
import de.dis2017.data.EstateAgent;
import de.dis2017.data.House;
import de.dis2017.data.db.ORM;

import java.util.List;

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
				    if (checkPassword()) {
                        showEstateAgentMenu();
                    }
                    else {
				    	System.out.println("The password was wrong.");
				    }
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
     * Checks the password for sudo-like menu areas.
     */
	private static boolean checkPassword() {
	    System.out.println("Please insert the sudo password. You are entering dangerous territory.");
        String passwordInput = FormUtil.readPassword();
        String sudoPassword = "ea-sudo";
        return sudoPassword.equals(passwordInput);
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
		boolean apartment = input.equals("A") || input.equals("a");
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
			estate.setBalcony(input.equals("Y") || input.equals("y"));
			input = FormUtil.readString("Built-in Kitchen(Y/N)");
			estate.setBuiltinKitchen(input.equals("Y") || input.equals("y"));
			
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
	 * TODO Change an estate agent after the user has entered the necessary data.
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
			apartment.setBalcony(input.equals("Y") || input.equals("y"));
			input = FormUtil.readString("Built-in Kitchen(Y/N)");
			apartment.setBuiltinKitchen(input.equals("Y") || input.equals("y"));
			
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
			house.setGarden(input.equals("Y") || input.equals("y"));
			
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
        final int LIST_AGENTS = 1;
		final int BACK = 2;

		// estate management menu
		Menu estateAgentMenu = new Menu("EstateAgent management");
		estateAgentMenu.addEntry("Create EstateAgent", NEW_AGENT);
        estateAgentMenu.addEntry("List EstateAgents", LIST_AGENTS);
		estateAgentMenu.addEntry("Back to the main menu", BACK);
		
		// process input
		while(true) {
			int response = estateAgentMenu.show();
			
			switch(response) {
				case NEW_AGENT:
					newEstateAgent();
					break;
                case LIST_AGENTS:
                    listEstateAgents();
                    break;
				case BACK:
					return;
			}
		}
	}
	
	/**
	 * Creates a new estate agent after the user has entered the necessary data.
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
     * List estate agents.
     */
	private static void listEstateAgents() {
	    // get all agents
        List<EstateAgent> agents = _orm.getAll();
        Menu listEstateAgents = new Menu("Please select the estate agent you want to modify or delete");
        System.out.println("List of EstateAgents");
        
        final int BACK = 0;
        
        for (EstateAgent agent : agents) {
            listEstateAgents.addEntry("ID: " + agent.getId() + ", Name: " + agent.getName(), agent.getId());
        }
        listEstateAgents.addEntry("Back to the EstateAgent management menu", BACK);
        
        // process input
        while(true) {
            int response = listEstateAgents.show();
            
            switch (response) {
                case BACK:
                    return;
                default:
                    showEstateAgent(response);
                    break;
            }
        }
    }
    
    /**
     * Shows a selected estate agent.
     *
     * @param id the id of the selected agent
     */
    private static void showEstateAgent(int id) {
        EstateAgent agent = _orm.get(id);
    
        System.out.println("EstateAgent");
        System.out.println("------------------");
        System.out.println("ID: " + id);
        System.out.println("Name: " + agent.getName());
        System.out.println("Address: " + agent.getAddress());
        System.out.println("Username: " + agent.getLogin());
        System.out.println("------------------");
        
        final int MODIFY = 1;
        final int DELETE = 2;
        final int BACK = 3;
        
        Menu showEstateAgentMenu = new Menu("Do you want to modify or delete the agent?");
        showEstateAgentMenu.addEntry("Modify", MODIFY);
        showEstateAgentMenu.addEntry("Delete", DELETE);
        showEstateAgentMenu.addEntry("Back to the list of agents", BACK);
        
        while(true) {
            int response = showEstateAgentMenu.show();
            
            switch (response) {
                case MODIFY:
                    modifyEstateAgent(agent);
                    break;
                case DELETE:
                    deleteEstateAgent(agent);
                    break;
                case BACK:
                    return;
            }
        }
    }
    
    /**
     * Modify estate agent.
     *
     * @param agent the modified agent
     */
    private static void modifyEstateAgent(EstateAgent agent) {
	    System.out.println("Modify EstateAgent");
	    System.out.println("------------------");
	    System.out.println("ID: " + agent.getId());
	    System.out.println("Name: " + agent.getName());
	    System.out.println("Address: " + agent.getAddress());
        System.out.println("Username: " + agent.getLogin());
        System.out.println("------------------");
	    
        agent.setName(FormUtil.readString("Name"));
	    agent.setAddress(FormUtil.readString("Address"));
	    agent.setLogin(FormUtil.readString("Username"));
	    agent.setPassword(FormUtil.readPassword());
	    
	    _orm.persist(agent);
    
        System.out.println("------------------");
        System.out.println("Agent was modified.");
    }
    
    /**
     * Deletes an estate agent.
     *
     * @param agent the agent that should be deleted
     */
    private static void deleteEstateAgent(EstateAgent agent) {
        _orm.delete(agent);
        System.out.println("EstateAgent with the ID " + agent.getId() + " was deleted.");
    }
}
