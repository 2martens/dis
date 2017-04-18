package de.dis2017;

import de.dis2017.data.EstateAgent;
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
				    if (checkPassword()) {
                        showEstateAgentMenu();
                    }
                    else {
				    	System.out.println("The password was wrong.");
				    }
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
	 * Shows the estate management.
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
        Menu listEstateAgents = new Menu("Please select the estate agent you want to modify");
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
                    modifyEstateAgent(response);
                    break;
            }
        }
    }
    
    /**
     * Modify estate agent.
     *
     * @param id the id of the modified agent
     */
    private static void modifyEstateAgent(int id) {
	    EstateAgent agent = _orm.get(id);
	    
	    System.out.println("Modify EstateAgent");
	    System.out.println("------------------");
	    System.out.println("ID: " + id);
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
}
