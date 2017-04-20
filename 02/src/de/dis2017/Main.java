package de.dis2017;

import de.dis2017.data.Apartment;
import de.dis2017.data.Contract;
import de.dis2017.data.Estate;
import de.dis2017.data.EstateAgent;
import de.dis2017.data.House;
import de.dis2017.data.Person;
import de.dis2017.data.PurchaseContract;
import de.dis2017.data.TenancyContract;
import de.dis2017.data.db.ORM;
import de.dis2017.data.db.Type;

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
		final int MENU_CONTRACTS = 2;
		final int QUIT = 3;
		
		// create menu
		Menu mainMenu = new Menu("Main menu");
		mainMenu.addEntry("EstateAgent management", MENU_AGENT);
		mainMenu.addEntry("Estate management", MENU_ESTATES);
		mainMenu.addEntry("Contract management", MENU_CONTRACTS);
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
				    if (loginEstateAgent()) {
                        showEstateMenu();
                    }
                    else {
				        System.out.println("The username or password was wrong.");
                    }
					break;
				case MENU_CONTRACTS:
				    showContractMenu();
				    break;
				case QUIT:
					return;
			}
		}
	}
    
    private static void showContractMenu() {
    	// menu options
		final int INSERT_PERSON = 0;
		final int CREATE_CONTRACT = 1;
		final int OVERVIEW_CONTRACTS = 2;
		final int BACK = 3;
		
		// create menu
		Menu mainMenu = new Menu("Contract Menu");
		mainMenu.addEntry("Insert person",INSERT_PERSON );
		mainMenu.addEntry("Create/Sign contract", CREATE_CONTRACT);
		mainMenu.addEntry("Contracts overview", OVERVIEW_CONTRACTS);
		mainMenu.addEntry("Back", BACK);
		
		// process input
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case INSERT_PERSON:
				    newPerson();
					break;
				case CREATE_CONTRACT:
				    newContract();
					break;
				case OVERVIEW_CONTRACTS:
				    showContractsOverview();
				    break;
				case BACK:
					return;
			}
		}
	}

	private static void showContractsOverview() {
		List<?> contracts = _orm.getAll(Type.CONTRACT);
		Menu listContracts = new Menu("Contracts Overview");
        
        final int BACK = 0;
        
        for (Object o : contracts) {
            Contract contract = (Contract) o;
            	listContracts.addEntry("Contract No: " + contract.getContractNo() + " Place: "+contract.getPlace()
            	+ "Date: "+contract.getDate(),contract.getContractNo());
        }
        listContracts.addEntry("Back to the Contract management menu", BACK);
        
        // process input
        while(true) {
            int response = listContracts.show();
            
            switch (response) {
                case BACK:
                    return;
                default:
                    break;
            }
        }
	}

	private static void newContract() {
		Contract contract = new Contract();
		//choose Estate from List
		printListOfEstates();
		contract.setEstate(FormUtil.readInt("Estate"));
		//choose Person from List
		printListOfPersons();
		contract.setPerson(FormUtil.readInt("Person"));
		contract.setPlace(FormUtil.readString("Place"));
		contract.setDate(FormUtil.readString("Date"));
		if(_orm.isApartment(contract.getEstate())){
			TenancyContract tenContract = new TenancyContract(contract);
			tenContract.setStartDate(FormUtil.readString("Start Date"));
			tenContract.setDuration(FormUtil.readInt("Duration"));
			tenContract.setAdditionalCost(FormUtil.readInt("Additional Costs"));
			
			//_orm.persist(tenContract);
		}
		else{
			PurchaseContract purContract = new PurchaseContract(contract);
			purContract.setNoOfInstallments(FormUtil.readInt("No of Installments"));
			purContract.setInterestRate(FormUtil.readInt("Interest Rate"));
			
			//_orm.persist(purContract);
		}
	}

	private static void newPerson() {
        Person person = new Person();
        person.setFirstName(FormUtil.readString("First Name"));
        person.setName(FormUtil.readString("Name"));
        person.setAddress(FormUtil.readString("Address"));
        
        //_orm.persist(person);
        System.out.println("Person with the Name " + person.getFirstName() +" "+person.getName() + " was created.");
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
     * Performs a login for an estate agent.
     *
     * @return true if the login is successful, false otherwise
     */
	private static boolean loginEstateAgent() {
        System.out.println("Please insert the username and password of a valid estate agent.");
        String      username      = FormUtil.readString("Username");
        String      passwordInput = FormUtil.readPassword();
        EstateAgent agent         = _orm.getAgent(username);
        return agent != null && agent.getPassword().equals(passwordInput);
    }
	
	/**
	 * Shows the estate management menu.
	 */
	private static void showEstateMenu() {
		// menu options
		final int NEW_ESTATE = 0;
		final int LIST_ESTATES = 1;
		final int BACK = 2;
		
		// estate management menu
		Menu estateMenu = new Menu("Estate management");
		estateMenu.addEntry("Create Estate", NEW_ESTATE);
		estateMenu.addEntry("List Estates", LIST_ESTATES);
		estateMenu.addEntry("Back to the main menu", BACK);
		
		// process input
		while(true) {
			int response = estateMenu.show();
			
			switch(response) {
				case NEW_ESTATE:
					newEstate();
					break;
				case LIST_ESTATES:
					listEstates();
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
        printListOfAgents();
        
        String input = FormUtil.readString("Apartment(A)/House(H)");
        boolean isApartment = input.equals("A") || input.equals("a");
        Estate estate = new Estate();
        estate.setStreet(FormUtil.readString("Street"));
        estate.setStreetNumber(FormUtil.readInt("Street Number"));
        estate.setPostalCode(FormUtil.readString("Postal Code"));
        estate.setCity(FormUtil.readString("City"));
        estate.setSquareArea(FormUtil.readInt("Square Area"));
        estate.setAgent(FormUtil.readInt("EstateAgent ID"));
        if(isApartment){
            Apartment apartment = new Apartment(estate);
            apartment.setFloor(FormUtil.readInt("Floor"));
            apartment.setRooms(FormUtil.readInt("Rooms"));
            apartment.setRent(FormUtil.readInt("Rent"));
            input = FormUtil.readString("Balcony(Y/N)");
            apartment.setBalcony(input.equals("Y") || input.equals("y"));
            input = FormUtil.readString("Built-in Kitchen(Y/N)");
            apartment.setBuiltinKitchen(input.equals("Y") || input.equals("y"));
        }
        else{
            House house = new House(estate);
            house.setPrice(FormUtil.readInt("Price"));
            house.setFloors(FormUtil.readInt("Floors"));
            input = FormUtil.readString("Garden(Y/N)");
            house.setGarden(input.equals("Y") || input.equals("y"));
        }
        
        //_orm.persist(estate);
        System.out.println("Estate with the ID " + estate.getId() + " was created.");
    }
    
    /**
     * Lists estates.
     */
	private static void listEstates() {
	    List<?> estates = _orm.getAll(Type.ESTATE);
        Menu listEstates = new Menu("Please select the estate you want to modify or delete");
        System.out.println("List of Estates");
        
        final int BACK = 0;
        
        for (Object o : estates) {
            Estate estate = (Estate) o;
            listEstates.addEntry("ID: " + estate.getId() + ", Address: " + estate.getStreet() + " "
                                 + estate.getStreetNumber() + ", " + estate.getPostalCode() + " " + estate.getCity(),
                                 estate.getId());
        }
        listEstates.addEntry("Back to the Estate management menu", BACK);
        
        // process input
        while(true) {
            int response = listEstates.show();
            
            switch (response) {
                case BACK:
                    return;
                default:
                    showEstate(response);
                    break;
            }
        }
	    
    }
    
    /**
     * Shows a selected estate.
     *
     * @param id the id of the selected estate
     */
    private static void showEstate(int id) {
        Estate estate = _orm.getEstate(id);
        
        System.out.println("Estate");
        printEstateDetails(estate);
        
        final int MODIFY = 1;
        final int DELETE = 2;
        final int BACK = 3;
        
        Menu showEstateMenu = new Menu("Do you want to modify or delete the estate?");
        showEstateMenu.addEntry("Modify", MODIFY);
        showEstateMenu.addEntry("Delete", DELETE);
        showEstateMenu.addEntry("Back to the list of estates", BACK);
        
        while(true) {
            int response = showEstateMenu.show();
            
            switch (response) {
                case MODIFY:
                    modifyEstate(estate);
                    break;
                case DELETE:
                    deleteEstate(estate);
                    break;
                case BACK:
                    return;
            }
        }
    }
    
    /**
     * Prints the estate details to command line.
     *
     * @param estate the estate from which the details should be printed to commandline
     */
    private static void printEstateDetails(Estate estate) {
        System.out.println("------------------");
        System.out.println("ID: " + estate.getId());
        System.out.println("Street: " + estate.getStreet() + " " + estate.getStreetNumber());
        System.out.println("PostalCode: " + estate.getPostalCode());
        System.out.println("City: " + estate.getCity());
        System.out.println("SquareArea: " + estate.getSquareArea());
        EstateAgent agent = _orm.getAgent(estate.getAgent());
        System.out.println("Agent: " + agent.getName());
        if (estate instanceof House) {
            House house = (House) estate;
            System.out.println("Price: " + house.getPrice());
            System.out.println("Floors: " + house.getFloors());
            System.out.println("Garden: " + (house.hasGarden() ? "yes" : "false"));
        }
        else if (estate instanceof Apartment) {
            Apartment apartment = (Apartment) estate;
            System.out.println("Floor: " + apartment.getFloor());
            System.out.println("Rooms: " + apartment.getRooms());
            System.out.println("Rent: " + apartment.getRent());
            System.out.println("Balcony: " + (apartment.hasBalcony() ? "yes" : "no"));
            System.out.println("Built-in Kitchen: " + (apartment.hasBuiltinKitchen() ? "yes" : "no"));
        }
        System.out.println("------------------");
    }
    
    /**
     * Print a list of persons.
     */
    private static void printListOfPersons() {
        List<?> agents = _orm.getAll(Type.PERSON);
        System.out.println("List of Persons");
        System.out.println("------------------");
    
        for (Object o : agents) {
            Person person = (Person) o;
            System.out.println("ID: "+person.getId()+" Name: " + person.getFirstName()+" "+person.getName() +", Address: " + person.getAddress());
        }
        System.out.println("------------------");
    }
    /**
     * Print a list of estates.
     */
    private static void printListOfEstates() {
        List<?> agents = _orm.getAll(Type.ESTATE);
        System.out.println("List of Estates");
        System.out.println("------------------");
    
        for (Object o : agents) {
            Estate estate = (Estate) o;
            System.out.println("ID: " + estate.getId() + ", Address: " + estate.getStreet()+" "+estate.getStreetNumber()+", "+estate.getCity());
        }
        System.out.println("------------------");
    }
    
    /**
     * Print a list of agents.
     */
    private static void printListOfAgents() {
        List<?> agents = _orm.getAll(Type.ESTATEAGENT);
        System.out.println("List of EstateAgents");
        System.out.println("------------------");
    
        for (Object o : agents) {
            EstateAgent agent = (EstateAgent) o;
            System.out.println("ID: " + agent.getId() + ", Name: " + agent.getName());
        }
        System.out.println("------------------");
    }
    
    /**
     * Modify estate.
     *
     * @param estate the modified estate
     */
    private static void modifyEstate(Estate estate) {
        System.out.println("Modify Estate");
        printEstateDetails(estate);
        printListOfAgents();
    
        estate.setStreet(FormUtil.readString("Street", estate.getStreet()));
        estate.setStreetNumber(FormUtil.readInt("Street Number", estate.getStreetNumber()));
        estate.setPostalCode(FormUtil.readString("Postal Code", estate.getPostalCode()));
        estate.setCity(FormUtil.readString("City", estate.getCity()));
        estate.setSquareArea(FormUtil.readInt("Square Area", estate.getSquareArea()));
        estate.setAgent(FormUtil.readInt("EstateAgent ID", estate.getAgent()));
    
        if (estate instanceof Apartment) {
            Apartment apartment = (Apartment) estate;
            apartment.setFloor(FormUtil.readInt("Floor", apartment.getFloor()));
            apartment.setRooms(FormUtil.readInt("Rooms", apartment.getRooms()));
            apartment.setRent(FormUtil.readInt("Rent", apartment.getRent()));
            String input = FormUtil.readString("Balcony(Y/N)", apartment.hasBalcony()?"Y":"N");
            apartment.setBalcony(input.equals("Y") || input.equals("y"));
            input = FormUtil.readString("Built-in Kitchen(Y/N)", apartment.hasBuiltinKitchen()?"Y":"N");
            apartment.setBuiltinKitchen(input.equals("Y") || input.equals("y"));
        }
        else if (estate instanceof House){
            House house = (House) estate;
            house.setFloors(FormUtil.readInt("Floors", house.getFloors()));
            house.setPrice(FormUtil.readInt("Price", house.getPrice()));
            String input = FormUtil.readString("Garden(Y/N)", house.hasGarden()?"Y":"N");
            house.setGarden(input.equals("Y") || input.equals("y"));
        }
        
        _orm.persist(estate);
        
        System.out.println("------------------");
        System.out.println("Estate with the ID " + estate.getId() + " was modified.");
    }
    
    /**
     * Deletes an estate.
     *
     * @param estate the estate that should be deleted
     */
    private static void deleteEstate(Estate estate) {
        _orm.delete(estate);
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
		agent.setPassword(FormUtil.readPassword());
		
		_orm.persist(agent);
		
		System.out.println("EstateAgent with the ID " + agent.getId() + " was created.");
	}
    
    /**
     * List estate agents.
     */
	private static void listEstateAgents() {
	    // get all agents
        List<?> agents = _orm.getAll(Type.ESTATEAGENT);
        Menu listEstateAgents = new Menu("Please select the estate agent you want to modify or delete");
        System.out.println("List of EstateAgents");
        
        final int BACK = 0;
        
        for (Object o : agents) {
            EstateAgent agent = (EstateAgent) o;
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
        EstateAgent agent = _orm.getAgent(id);
    
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
	    
        agent.setName(FormUtil.readString("Name", agent.getName()));
	    agent.setAddress(FormUtil.readString("Address", agent.getAddress()));
	    agent.setLogin(FormUtil.readString("Username", agent.getLogin()));
	    agent.setPassword(FormUtil.readPassword(agent.getPassword()));
	    
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
