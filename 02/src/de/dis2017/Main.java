package de.dis2017;

import de.dis2017.data.EstateAgent;
import de.dis2017.data.db.ORM;

/**
 * Main class
 */
public class Main {
	/**
	 * Starts the application.
	 */
	public static void main(String[] args) {
		showMainMenu();
	}
	
	/**
	 * Shows the main menu.
	 */
	private static void showMainMenu() {
		// menu options
		final int MENU_AGENT = 0;
		final int QUIT = 1;
		
		// create menu
		Menu mainMenu = new Menu("Main menu");
		mainMenu.addEntry("EstateAgent management", MENU_AGENT);
		mainMenu.addEntry("Quit", QUIT);
		
		// process input
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_AGENT:
					showEstateAgentMenu();
					break;
				case QUIT:
					return;
			}
		}
	}
	
	/**
	 * Shows the estate management.
	 */
	private static void showEstateAgentMenu() {
		// menu options
		final int NEW_AGENT = 0;
		final int BACK = 1;
		
		// estate management menu
		Menu estateAgentMenu = new Menu("EstateAgent management");
		estateAgentMenu.addEntry("Create EstateAgent", NEW_AGENT);
		estateAgentMenu.addEntry("Back to the main menu", BACK);
		
		// process input
		while(true) {
			int response = estateAgentMenu.show();
			
			switch(response) {
				case NEW_AGENT:
					newEstateAgent();
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
		
		ORM orm = new ORM();
		orm.persist(agent);
		
		System.out.println("EstateAgent with the ID " + agent.getId() + " was created.");
	}
}
