package de.dis2017.data.db;

import de.dis2017.data.Apartment;
import de.dis2017.data.Contract;
import de.dis2017.data.Estate;
import de.dis2017.data.EstateAgent;
import de.dis2017.data.House;
import de.dis2017.data.Person;
import de.dis2017.data.PurchaseContract;
import de.dis2017.data.TenancyContract;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps the data objects to the database and vice versa.
 */
public class ORM {
    private Connection _connection;
    
    private Map<Integer, EstateAgent> _agents;
    private Map<String, EstateAgent>  _agentsUsername;
    private Map<Integer, Estate>      _estates;
    private Map<Integer, Contract>    _contracts;
    private Map<Integer, Person>      _persons;
    
    
    /**
     * Initializes the ORM.
     */
    public ORM() {
        DB2ConnectionManager _dbManager = DB2ConnectionManager.getInstance();
        _connection = _dbManager.getConnection();
        _agents = new HashMap<>();
        _agentsUsername = new HashMap<>();
        _estates = new HashMap<>();
        _contracts = new HashMap<>();
        _persons = new HashMap<>();
    }
    
    /**
     * Loads all objects from the database and returns a list of them.
     *
     * @param objectType
     *         the type of objects to load
     * @return a list of objects
     */
    public List<?> getAll(Type objectType) {
        List<?> objects = new ArrayList<>();
        try {
            // create query
            String            selectSQL = "SELECT * FROM " + objectType.name();
            PreparedStatement pstmt     = _connection.prepareStatement(selectSQL);
            
            // execute query
            ResultSet rs = pstmt.executeQuery();
            switch (objectType) {
                case ESTATEAGENT:
                    objects = processAgents(rs);
                    break;
                case ESTATE:
                    objects = processEstates(rs);
                    break;
                case CONTRACT:
                    objects = processContracts(rs);
                    break;
                case PERSON:
                    objects = processPersons(rs);
                    break;
                case HOUSE:
                    List<?> estates = getAll(Type.ESTATE);
                    objects = processHouses(rs, estates);
                    break;
                case APARTMENT:
                    estates = getAll(Type.ESTATE);
                    objects = processApartments(rs, estates);
                    break;
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return objects;
    }
    
    /**
     * Returns a list with the IDs of sold houses.
     *
     * @return a list of houseIDs
     */
    public List<Integer> getSoldHouses() {
        List<Integer> soldHouses = new ArrayList<>();
        
        try {
            String            selectSQL = "SELECT house FROM SALES";
            PreparedStatement pstmt     = _connection.prepareStatement(selectSQL);
            ResultSet         rs        = pstmt.executeQuery();
            while (rs.next()) {
                soldHouses.add(rs.getInt("house"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return soldHouses;
    }
    
    /**
     * Returns a list with the IDs of rented apartments.
     *
     * @return a list of apartmentIDs
     */
    public List<Integer> getRentedApartments() {
        List<Integer> rentedApartments = new ArrayList<>();
        
        try {
            String            selectSQL = "SELECT apartment FROM RENTALS";
            PreparedStatement pstmt     = _connection.prepareStatement(selectSQL);
            ResultSet         rs        = pstmt.executeQuery();
            while (rs.next()) {
                rentedApartments.add(rs.getInt("apartment"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rentedApartments;
    }
    
    /**
     * Process a select all query for estates.
     *
     * @param rs
     *         the result set of such a query
     * @return a list of estates
     *
     * @throws SQLException
     *         when an error occurs during the rs.next call
     */
    private List<Estate> processEstates(ResultSet rs) throws SQLException {
        List<Estate> estates = new ArrayList<>();
        
        while (rs.next()) {
            Estate estate = new Estate();
            estate.setId(rs.getInt("ID"));
            estate.setCity(rs.getString("city"));
            estate.setPostalCode(rs.getString("postalCode"));
            estate.setStreet(rs.getString("street"));
            estate.setStreetNumber(rs.getInt("streetNumber"));
            estate.setSquareArea(rs.getInt("squareArea"));
            estate.setAgent(rs.getInt("agent"));
            
            estates.add(estate);
        }
        
        return estates;
    }
    
    /**
     * Process a select all query for houses.
     *
     * @param rs
     *         the result set of such a query
     * @param estates
     *         a list of estates
     * @return a list of houses
     *
     * @throws SQLException
     *         when an error occurs during the rs.next call
     */
    private List<House> processHouses(ResultSet rs, List<?> estates) throws SQLException {
        List<House>         houses    = new ArrayList<>();
        Map<Integer, House> housesMap = new HashMap<>();
        
        while (rs.next()) {
            House house = new House();
            house.setId(rs.getInt("ID"));
            house.setPrice(rs.getInt("price"));
            house.setGarden(rs.getBoolean("garden"));
            house.setFloors(rs.getInt("floors"));
            
            houses.add(house);
            housesMap.put(house.getId(), house);
        }
        
        for (Object o : estates) {
            Estate estate = (Estate) o;
            if (!housesMap.containsKey(estate.getId())) {
                continue;
            }
            House _house = housesMap.get(estate.getId());
            _house.setCity(estate.getCity());
            _house.setPostalCode(estate.getPostalCode());
            _house.setStreet(estate.getStreet());
            _house.setStreetNumber(estate.getStreetNumber());
            _house.setSquareArea(estate.getSquareArea());
            _house.setAgent(estate.getAgent());
        }
        
        return houses;
    }
    
    /**
     * Process a select all query for houses.
     *
     * @param rs
     *         the result set of such a query
     * @param estates
     *         a list of estates
     * @return a list of houses
     *
     * @throws SQLException
     *         when an error occurs during the rs.next call
     */
    private List<Apartment> processApartments(ResultSet rs, List<?> estates) throws SQLException {
        List<Apartment>         apartments    = new ArrayList<>();
        Map<Integer, Apartment> apartmentsMap = new HashMap<>();
        
        while (rs.next()) {
            Apartment apartment = new Apartment();
            apartment.setId(rs.getInt("ID"));
            apartment.setFloor(rs.getInt("floor"));
            apartment.setRent(rs.getInt("rent"));
            apartment.setRooms(rs.getInt("rooms"));
            apartment.setBalcony(rs.getBoolean("balcony"));
            apartment.setBuiltinKitchen(rs.getBoolean("builtInKitchen"));
            
            apartments.add(apartment);
            apartmentsMap.put(apartment.getId(), apartment);
        }
        
        for (Object o : estates) {
            Estate estate = (Estate) o;
            if (!apartmentsMap.containsKey(estate.getId())) {
                continue;
            }
            Apartment _apartment = apartmentsMap.get(estate.getId());
            _apartment.setCity(estate.getCity());
            _apartment.setPostalCode(estate.getPostalCode());
            _apartment.setStreet(estate.getStreet());
            _apartment.setStreetNumber(estate.getStreetNumber());
            _apartment.setSquareArea(estate.getSquareArea());
            _apartment.setAgent(estate.getAgent());
        }
        
        return apartments;
    }
    
    /**
     * Processes a select all query for estate agents.
     *
     * @param rs
     *         the result set of such a query
     * @return a list of agents
     *
     * @throws SQLException
     *         when an error occurs during the rs.next call
     */
    private List<EstateAgent> processAgents(ResultSet rs) throws SQLException {
        List<EstateAgent> agents = new ArrayList<>();
        while (rs.next()) {
            EstateAgent agent = new EstateAgent();
            agent.setId(rs.getInt("ID"));
            agent.setName(rs.getString("name"));
            agent.setAddress(rs.getString("address"));
            agent.setLogin(rs.getString("login"));
            agent.setPassword(rs.getString("password"));
            
            _agents.put(agent.getId(), agent);
            _agentsUsername.put(agent.getLogin(), agent);
            agents.add(agent);
        }
        
        return agents;
    }
    
    /**
     * Process a select all query for contracts.
     *
     * @param rs
     *         the result set of such a query
     * @return a list of contracts
     *
     * @throws SQLException
     *         when an error occurs during the rs.next call
     */
    private List<Contract> processContracts(ResultSet rs) throws SQLException {
        List<Contract> contracts = new ArrayList<>();
        
        while (rs.next()) {
            Contract contract = new Contract();
            contract.setContractNo(rs.getInt("contractNumber"));
            contract.setPlace(rs.getString("place"));
            contract.setDate(rs.getDate("date"));
            
            contracts.add(contract);
        }
        
        return contracts;
    }
    
    /**
     * Process a select all query for persons.
     *
     * @param rs
     *         the result set of such a query
     * @return a list of persons
     *
     * @throws SQLException
     *         when an error occurs during the rs.next call
     */
    private List<Person> processPersons(ResultSet rs) throws SQLException {
        List<Person> persons = new ArrayList<>();
        
        while (rs.next()) {
            Person person = new Person();
            person.setId(rs.getInt("ID"));
            person.setFirstName(rs.getString("firstName"));
            person.setName(rs.getString("name"));
            person.setAddress(rs.getString("address"));
            
            persons.add(person);
        }
        
        return persons;
    }
    
    /**
     * Loads the contract with the given ID from database and returns the corresponding object.
     *
     * @param ID
     *         the id of the contract to load
     * @return the Contract or null if there is no such object
     */
    public Contract getContract(int ID) {
        if (_contracts.containsKey(ID)) {
            return _contracts.get(ID);
        }
        
        String selectSQLPurchase = "SELECT * FROM PURCHASECONTRACT " +
                                   "LEFT JOIN CONTRACT ON PURCHASECONTRACT.CONTRACTNUMBER = CONTRACT.CONTRACTNUMBER " +
                                   "LEFT JOIN SALES ON PURCHASECONTRACT.CONTRACTNUMBER = SALES.CONTRACTNUMBER " +
                                   "WHERE PURCHASECONTRACT.CONTRACTNUMBER = ?";
        String selectSQLTenancy = "SELECT * FROM TENANCYCONTRACT " +
                                  "LEFT JOIN CONTRACT ON TENANCYCONTRACT.CONTRACTNUMBER = CONTRACT.CONTRACTNUMBER " +
                                  "LEFT JOIN RENTALS ON TENANCYCONTRACT.CONTRACTNUMBER = RENTALS.CONTRACTNUMBER " +
                                  "WHERE TENANCYCONTRACT.CONTRACTNUMBER = ?";
        
        String countPurchase = "SELECT COUNT(contractNumber) AS count FROM PURCHASECONTRACT WHERE contractNumber = ?";
        String countTenancy  = "SELECT COUNT(contractNumber) AS count FROM TENANCYCONTRACT WHERE contractNumber = ?";
        try {
            // try purchase contract first
            PreparedStatement preparedStatementCount = _connection.prepareStatement(countPurchase);
            preparedStatementCount.setInt(1, ID);
            ResultSet rs = preparedStatementCount.executeQuery();
            rs.next();
            int    count = rs.getInt("count");
            String type  = "None";
            if (count == 0) {
                // try tenancy contract next
                preparedStatementCount = _connection.prepareStatement(countTenancy);
                preparedStatementCount.setInt(1, ID);
                rs = preparedStatementCount.executeQuery();
                rs.next();
                count = rs.getInt("count");
                if (count == 1) {
                    type = "TenancyContract";
                }
            } else {
                type = "PurchaseContract";
            }
            rs.close();
            preparedStatementCount.close();
            
            PreparedStatement pstmt;
            Contract          contract;
            switch (type) {
                case "PurchaseContract":
                    pstmt = _connection.prepareStatement(selectSQLPurchase);
                    pstmt.setInt(1, ID);
                    rs = pstmt.executeQuery();
                    contract = new PurchaseContract();
                    break;
                case "TenancyContract":
                    pstmt = _connection.prepareStatement(selectSQLTenancy);
                    pstmt.setInt(1, ID);
                    rs = pstmt.executeQuery();
                    contract = new TenancyContract();
                    break;
                default:
                    return null;
            }
            
            if (rs.next()) {
                contract.setContractNo(ID);
                contract.setPlace(rs.getString("place"));
                contract.setDate(rs.getDate("date"));
                contract.setPerson(rs.getInt("person"));
                
                if (contract instanceof PurchaseContract) {
                    ((PurchaseContract) contract).setInterestRate(rs.getInt("interestRate"));
                    ((PurchaseContract) contract).setNoOfInstallments(rs.getInt("numberOfInstallments"));
                    ((PurchaseContract) contract).setHouse(rs.getInt("house"));
                }
                if (contract instanceof TenancyContract) {
                    ((TenancyContract) contract).setStartDate(rs.getTimestamp("startDate"));
                    ((TenancyContract) contract).setDuration(rs.getTimestamp("duration"));
                    ((TenancyContract) contract).setAdditionalCost(rs.getInt("additionalCosts"));
                    ((TenancyContract) contract).setApartment(rs.getInt("apartment"));
                }
                
                rs.close();
                pstmt.close();
                _contracts.put(ID, contract);
                return contract;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Loads the estate with the given ID from database and returns the corresponding object.
     *
     * @param ID
     *         the id of the estate to load
     * @return the Estate or null if there is no such object
     */
    public Estate getEstate(int ID) {
        if (_estates.containsKey(ID)) {
            return _estates.get(ID);
        }
        
        String selectSQLHouse = "SELECT * FROM HOUSE LEFT JOIN ESTATE ON HOUSE.ID = ESTATE.ID " +
                                "WHERE HOUSE.ID = ?";
        String selectSQLApartment = "SELECT * FROM APARTMENT LEFT JOIN ESTATE ON APARTMENT.ID = ESTATE.ID " +
                                    "WHERE APARTMENT.ID = ?";
        
        String countHouse     = "SELECT COUNT(ID) AS count FROM HOUSE WHERE ID = ?";
        String countApartment = "SELECT COUNT(ID) AS count FROM APARTMENT WHERE ID = ?";
        try {
            // try house first
            PreparedStatement preparedStatementCount = _connection.prepareStatement(countHouse);
            preparedStatementCount.setInt(1, ID);
            ResultSet rs = preparedStatementCount.executeQuery();
            rs.next();
            int    count = rs.getInt("count");
            String type  = "None";
            if (count == 0) {
                // try apartment next
                preparedStatementCount = _connection.prepareStatement(countApartment);
                preparedStatementCount.setInt(1, ID);
                rs = preparedStatementCount.executeQuery();
                rs.next();
                count = rs.getInt("count");
                if (count == 1) {
                    type = "Apartment";
                }
            } else {
                type = "House";
            }
            rs.close();
            preparedStatementCount.close();
            
            PreparedStatement pstmt;
            Estate            estate;
            switch (type) {
                case "House":
                    pstmt = _connection.prepareStatement(selectSQLHouse);
                    pstmt.setInt(1, ID);
                    rs = pstmt.executeQuery();
                    estate = new House();
                    break;
                case "Apartment":
                    pstmt = _connection.prepareStatement(selectSQLApartment);
                    pstmt.setInt(1, ID);
                    rs = pstmt.executeQuery();
                    estate = new Apartment();
                    break;
                default:
                    return null;
            }
            
            if (rs.next()) {
                estate.setId(ID);
                estate.setCity(rs.getString("city"));
                estate.setPostalCode(rs.getString("postalCode"));
                estate.setStreet(rs.getString("street"));
                estate.setStreetNumber(rs.getInt("streetNumber"));
                estate.setSquareArea(rs.getInt("squareArea"));
                estate.setAgent(rs.getInt("agent"));
                
                if (estate instanceof House) {
                    ((House) estate).setFloors(rs.getInt("floors"));
                    ((House) estate).setGarden(rs.getBoolean("garden"));
                    ((House) estate).setPrice(rs.getInt("price"));
                }
                if (estate instanceof Apartment) {
                    ((Apartment) estate).setRent(rs.getInt("rent"));
                    ((Apartment) estate).setFloor(rs.getInt("floor"));
                    ((Apartment) estate).setRooms(rs.getInt("rooms"));
                    ((Apartment) estate).setBalcony(rs.getBoolean("balcony"));
                    ((Apartment) estate).setBuiltinKitchen(rs.getBoolean("builtInKitchen"));
                }
                
                rs.close();
                pstmt.close();
                _estates.put(ID, estate);
                return estate;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Loads the estate agent with the given ID from database and returns the corresponding object.
     *
     * @param ID
     *         the ID of the agent to load
     * @return the EstateAgent or null if there is no such agent
     */
    public EstateAgent getAgent(int ID) {
        if (_agents.containsKey(ID)) {
            return _agents.get(ID);
        }
        
        try {
            // create query
            String            selectSQL = "SELECT * FROM ESTATEAGENT WHERE ID = ?";
            PreparedStatement pstmt     = _connection.prepareStatement(selectSQL);
            pstmt.setInt(1, ID);
            
            return getAgent(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Loads the person with the given ID from database and returns the corresponding object.
     *
     * @param ID
     *         the ID of the person to load
     * @return the Person or null if there is no such agent
     */
    public Person getPerson(int ID) {
        if (_persons.containsKey(ID)) {
            return _persons.get(ID);
        }
        
        try {
            // create query
            String            selectSQL = "SELECT * FROM PERSON WHERE ID = ?";
            PreparedStatement pstmt     = _connection.prepareStatement(selectSQL);
            pstmt.setInt(1, ID);
            ResultSet rs = pstmt.executeQuery();
            Person    person;
            
            if (rs.next()) {
                person = new Person();
                person.setId(rs.getInt("ID"));
                person.setFirstName(rs.getString("firstName"));
                person.setName(rs.getString("name"));
                person.setAddress(rs.getString("address"));
                
                rs.close();
                pstmt.close();
                
                _persons.put(person.getId(), person);
                return person;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Loads the estate agent with the given username from database and returns the corresponding object,
     *
     * @param username
     *         the username of the estate agent
     * @return the EstateAgent or null if there is no such agent
     */
    public EstateAgent getAgent(String username) {
        if (_agentsUsername.containsKey(username)) {
            return _agentsUsername.get(username);
        }
        
        try {
            // create query
            String            selectSQL = "SELECT * FROM ESTATEAGENT WHERE login = ?";
            PreparedStatement pstmt     = _connection.prepareStatement(selectSQL);
            pstmt.setString(1, username);
            
            return getAgent(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Executes the given statement and returns an estate agent.
     *
     * @param pstmt
     *         the prepared statement with parameters already set
     * @return the EstateAgent or null
     */
    @Nullable
    private EstateAgent getAgent(PreparedStatement pstmt) {
        try {
            // execute query
            ResultSet   rs = pstmt.executeQuery();
            EstateAgent agent;
            if (rs.next()) {
                agent = new EstateAgent();
                agent.setId(rs.getInt("ID"));
                agent.setName(rs.getString("name"));
                agent.setAddress(rs.getString("address"));
                agent.setLogin(rs.getString("login"));
                agent.setPassword(rs.getString("password"));
                
                rs.close();
                pstmt.close();
                
                _agents.put(agent.getId(), agent);
                _agentsUsername.put(agent.getLogin(), agent);
                return agent;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Deletes the given agent from the database.
     *
     * @param agent
     *         the agent that should be deleted
     */
    public void delete(EstateAgent agent) {
        if (agent.getId() == -1) {
            System.err.println("This agent is not yet persisted to the database and cannot be deleted.");
            return;
        } else {
            // create query
            String deleteSQL = "DELETE FROM ESTATEAGENT WHERE ID = ?";
            delete(deleteSQL, agent.getId());
        }
        if (_agents.containsKey(agent.getId())) {
            _agents.remove(agent.getId(), agent);
        }
        if (_agentsUsername.containsKey(agent.getLogin())) {
            _agentsUsername.remove(agent.getLogin(), agent);
        }
    }
    
    /**
     * Deletes an estate from the database.
     *
     * @param estate
     *         the estate to be deleted
     */
    public void delete(Estate estate) {
        if (estate.getId() == -1) {
            System.err.println("This estate is not yet persisted to the database and cannot be deleted.");
            return;
        } else {
            // create query
            String deleteSQL = "DELETE FROM HOUSE WHERE ID = ?";
            delete(deleteSQL, estate.getId());
            deleteSQL = "DELETE FROM APARTMENT WHERE ID = ?";
            delete(deleteSQL, estate.getId());
            deleteSQL = "DELETE FROM ESTATE WHERE ID = ?";
            delete(deleteSQL, estate.getId());
        }
        if (_estates.containsKey(estate.getId())) {
            _estates.remove(estate.getId(), estate);
        }
    }
    
    /**
     * Deletes an object from the database.
     *
     * @param sql
     *         the sql used for deletion
     * @param id
     *         the id of the object to be deleted
     */
    private void delete(String sql, int id) {
        try {
            PreparedStatement pstmt = _connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            // execute query
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Persists the given agent.
     *
     * @param agent
     *         the agent that should be persisted
     */
    public void persist(EstateAgent agent) {
        try {
            if (agent.getId() == -1) {
                String            insertSQL
                                        = "INSERT INTO ESTATEAGENT (name, address, login, password) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = _connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                
                pstmt.setString(1, agent.getName());
                pstmt.setString(2, agent.getAddress());
                pstmt.setString(3, agent.getLogin());
                pstmt.setString(4, agent.getPassword());
                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    agent.setId(rs.getInt(1));
                }
                
                rs.close();
                pstmt.close();
            } else {
                // create query
                String            updateSQL = "UPDATE ESTATEAGENT SET name = ?, address = ?, password = ? WHERE ID = ?";
                PreparedStatement pstmt     = _connection.prepareStatement(updateSQL);
                pstmt.setString(1, agent.getName());
                pstmt.setString(2, agent.getAddress());
                pstmt.setString(3, agent.getPassword());
                pstmt.setInt(4, agent.getId());
                
                // execute query
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!_agents.containsKey(agent.getId())) {
            _agents.put(agent.getId(), agent);
        }
        if (!_agentsUsername.containsKey(agent.getLogin())) {
            _agentsUsername.put(agent.getLogin(), agent);
        }
    }
    
    /**
     * Persists the given estate.
     *
     * @param estate
     *         the estate that should be persisted
     */
    public void persist(Estate estate) {
        boolean changeFinished = false;
        try {
            _connection.setAutoCommit(false);
            if (estate.getId() == -1) {
                String insertSQL = "INSERT INTO ESTATE (city, postalCode, street, streetNumber, squareArea, agent) " +
                                   "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = _connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, estate.getCity());
                pstmt.setString(2, estate.getPostalCode());
                pstmt.setString(3, estate.getStreet());
                pstmt.setInt(4, estate.getStreetNumber());
                pstmt.setInt(5, estate.getSquareArea());
                pstmt.setInt(6, estate.getAgent());
                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    estate.setId(rs.getInt(1));
                }
                rs.close();
                pstmt.close();
                
                if (estate instanceof House) {
                    House             house          = (House) estate;
                    String            insertSQLHouse
                                                     = "INSERT INTO HOUSE (ID, price, garden, floors) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmtHouse     = _connection.prepareStatement(insertSQLHouse);
                    pstmtHouse.setInt(1, house.getId());
                    pstmtHouse.setInt(2, house.getPrice());
                    pstmtHouse.setInt(3, house.hasGarden() ? 1 : 0);
                    pstmtHouse.setInt(4, house.getFloors());
                    pstmtHouse.executeUpdate();
                    pstmtHouse.close();
                    changeFinished = true;
                } else if (estate instanceof Apartment) {
                    Apartment apartment = (Apartment) estate;
                    String insertSQLApartment =
                            "INSERT INTO APARTMENT (ID, floor, rent, rooms, balcony, builtInKitchen) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmtApartment = _connection.prepareStatement(insertSQLApartment);
                    pstmtApartment.setInt(1, apartment.getId());
                    pstmtApartment.setInt(2, apartment.getFloor());
                    pstmtApartment.setInt(3, apartment.getRent());
                    pstmtApartment.setInt(4, apartment.getRooms());
                    pstmtApartment.setInt(5, apartment.hasBalcony() ? 1 : 0);
                    pstmtApartment.setInt(6, apartment.hasBuiltinKitchen() ? 1 : 0);
                    pstmtApartment.executeUpdate();
                    pstmtApartment.close();
                    changeFinished = true;
                }
            } else {
                // create query
                String updateSQL = "UPDATE ESTATE SET city = ?, postalCode = ?, street = ?, streetNumber = ?, " +
                                   "squareArea = ?, agent = ? WHERE ID = ?";
                PreparedStatement pstmt = _connection.prepareStatement(updateSQL);
                pstmt.setString(1, estate.getCity());
                pstmt.setString(2, estate.getPostalCode());
                pstmt.setString(3, estate.getStreet());
                pstmt.setInt(4, estate.getStreetNumber());
                pstmt.setInt(5, estate.getSquareArea());
                pstmt.setInt(6, estate.getAgent());
                pstmt.setInt(7, estate.getId());
                
                if (estate instanceof House) {
                    House             house          = (House) estate;
                    String            updateSQLHouse
                                                     = "UPDATE HOUSE SET floors = ?, garden = ?, price = ? WHERE ID = ?";
                    PreparedStatement pstmtHouse     = _connection.prepareStatement(updateSQLHouse);
                    pstmtHouse.setInt(1, house.getFloors());
                    pstmtHouse.setInt(2, house.hasGarden() ? 1 : 0);
                    pstmtHouse.setInt(3, house.getPrice());
                    pstmtHouse.setInt(4, house.getId());
                    pstmt.executeUpdate();
                    pstmtHouse.executeUpdate();
                    pstmt.close();
                    pstmtHouse.close();
                    changeFinished = true;
                } else if (estate instanceof Apartment) {
                    Apartment apartment = (Apartment) estate;
                    String updateSQLApartment = "UPDATE APARTMENT SET floor = ?, rent = ?, rooms = ?, " +
                                                "balcony = ?, builtInKitchen = ? WHERE ID = ?";
                    PreparedStatement pstmtApartment = _connection.prepareStatement(updateSQLApartment);
                    pstmtApartment.setInt(1, apartment.getFloor());
                    pstmtApartment.setInt(2, apartment.getRent());
                    pstmtApartment.setInt(3, apartment.getRooms());
                    pstmtApartment.setInt(4, apartment.hasBalcony() ? 1 : 0);
                    pstmtApartment.setInt(5, apartment.hasBuiltinKitchen() ? 1 : 0);
                    pstmtApartment.setInt(6, apartment.getId());
                    pstmt.executeUpdate();
                    pstmtApartment.executeUpdate();
                    pstmt.close();
                    pstmtApartment.close();
                    changeFinished = true;
                }
            }
            if (changeFinished) {
                _connection.commit();
                if (!_estates.containsKey(estate.getId())) {
                    _estates.put(estate.getId(), estate);
                }
            }
            _connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                _connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    
    /**
     * Persists the given contract.
     *
     * @param contract
     *         the contract that should be persisted
     */
    public void persist(Contract contract) {
        boolean changeFinished = false;
        try {
            _connection.setAutoCommit(false);
            if (contract.getContractNo() == -1) {
                String insertSQL = "INSERT INTO CONTRACT (date, place) " +
                                   "VALUES (?, ?)";
                PreparedStatement pstmt = _connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                pstmt.setDate(1, contract.getDate());
                pstmt.setString(2, contract.getPlace());
                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    contract.setContractNo(rs.getInt(1));
                }
                rs.close();
                pstmt.close();
                
                if (contract instanceof PurchaseContract) {
                    PurchaseContract purchaseContract = (PurchaseContract) contract;
                    String insertSQLPurchaseContract = "INSERT INTO PURCHASECONTRACT " +
                                                       "(contractNumber, numberOfInstallments, interestRate) " +
                                                       "VALUES (?, ?, ?)";
                    PreparedStatement pstmtPurchase = _connection.prepareStatement(insertSQLPurchaseContract);
                    pstmtPurchase.setInt(1, purchaseContract.getContractNo());
                    pstmtPurchase.setInt(2, purchaseContract.getNoOfInstallments());
                    pstmtPurchase.setInt(3, purchaseContract.getInterestRate());
                    pstmtPurchase.executeUpdate();
                    pstmtPurchase.close();
                    
                    String            insertSQLSale
                                                = "INSERT INTO SALES (contractNumber, house, person) VALUES (?, ?, ?)";
                    PreparedStatement pstmtSale = _connection.prepareStatement(insertSQLSale);
                    pstmtSale.setInt(1, purchaseContract.getContractNo());
                    pstmtSale.setInt(2, purchaseContract.getHouse());
                    pstmtSale.setInt(3, purchaseContract.getPerson());
                    pstmtSale.executeUpdate();
                    pstmtSale.close();
                    changeFinished = true;
                } else if (contract instanceof TenancyContract) {
                    TenancyContract tenancyContract = (TenancyContract) contract;
                    String insertSQLTenancyContract = "INSERT INTO TENANCYCONTRACT " +
                                                      "(contractNumber, startDate, duration, additionalCosts) " +
                                                      "VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmtTenancy = _connection.prepareStatement(insertSQLTenancyContract);
                    pstmtTenancy.setInt(1, tenancyContract.getContractNo());
                    pstmtTenancy.setTimestamp(2, tenancyContract.getStartDate());
                    pstmtTenancy.setTimestamp(3, new Timestamp(tenancyContract.getDuration().toMillis()));
                    pstmtTenancy.setInt(4, tenancyContract.getAdditionalCost());
                    pstmtTenancy.executeUpdate();
                    pstmtTenancy.close();
                    
                    String            insertSQLRental
                                                  = "INSERT INTO RENTALS (contractNumber, apartment, person) VALUES (?, ?, ?)";
                    PreparedStatement pstmtRental = _connection.prepareStatement(insertSQLRental);
                    pstmtRental.setInt(1, tenancyContract.getContractNo());
                    pstmtRental.setInt(2, tenancyContract.getApartment());
                    pstmtRental.setInt(3, tenancyContract.getPerson());
                    pstmtRental.executeUpdate();
                    pstmtRental.close();
                    changeFinished = true;
                }
            } else {
                // create query
                String updateSQL = "UPDATE CONTRACT SET date = ?, place = ? WHERE ContractNumber = ?";
                PreparedStatement pstmt = _connection.prepareStatement(updateSQL);
                pstmt.setDate(1, contract.getDate());
                pstmt.setString(2, contract.getPlace());
                pstmt.setInt(3, contract.getContractNo());
    
                if (contract instanceof TenancyContract) {
                    TenancyContract tenancyContract = (TenancyContract) contract;
                    String updateSQLTenancy = "UPDATE TenancyContract SET startDate = ?, duration = ?, additionalcosts = ? " +
                                              "WHERE contractNumber = ?";
                    PreparedStatement pstmtTenancy = _connection.prepareStatement(updateSQLTenancy);
                    pstmtTenancy.setTimestamp(1, tenancyContract.getStartDate());
                    pstmtTenancy.setTimestamp(2, new Timestamp(tenancyContract.getDuration().toMillis()));
                    pstmtTenancy.setInt(3, tenancyContract.getAdditionalCost());
                    pstmtTenancy.setInt(4, tenancyContract.getContractNo());
                    pstmt.executeUpdate();
                    pstmtTenancy.executeUpdate();
                    pstmt.close();
                    pstmtTenancy.close();
                    changeFinished = true;
                }
                else if (contract instanceof PurchaseContract) {
                    PurchaseContract purContract = (PurchaseContract) contract;
                    String updateSQLPurchase =
                            "UPDATE PurchaseContract SET numberofinstallments = ?, interestrate = ? " +
                            "WHERE contractNumber = ?";
                    PreparedStatement pstmtPurchase = _connection.prepareStatement(updateSQLPurchase);
                    pstmtPurchase.setInt(1, purContract.getNoOfInstallments());
                    pstmtPurchase.setInt(2, purContract.getInterestRate());
                    pstmtPurchase.setInt(3, purContract.getContractNo());
                    pstmt.executeUpdate();
                    pstmtPurchase.executeUpdate();
                    pstmt.close();
                    pstmtPurchase.close();
                    changeFinished = true;
                }
            }
            if (changeFinished) {
                _connection.commit();
                if (!_contracts.containsKey(contract.getContractNo())) {
                    _contracts.put(contract.getContractNo(), contract);
                }
            }
            _connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                _connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    
    /**
     * Persists the given person.
     *
     * @param person
     *         the person that should be persisted
     */
    public void persist(Person person) {
        try {
            if (person.getId() == -1) {
                String            insertSQL = "INSERT INTO PERSON (firstName, name, address) VALUES (?, ?, ?)";
                PreparedStatement pstmt     = _connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                
                pstmt.setString(1, person.getFirstName());
                pstmt.setString(2, person.getName());
                pstmt.setString(3, person.getAddress());
                pstmt.executeUpdate();
                
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    person.setId(rs.getInt(1));
                }
                
                rs.close();
                pstmt.close();
            } else {
                // create query
                String            updateSQL = "UPDATE PERSON SET firstName = ?, name = ?, address = ? WHERE ID = ?";
                PreparedStatement pstmt     = _connection.prepareStatement(updateSQL);
                pstmt.setString(1, person.getFirstName());
                pstmt.setString(2, person.getName());
                pstmt.setString(3, person.getAddress());
                pstmt.setInt(4, person.getId());
                
                // execute query
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!_persons.containsKey(person.getId())) {
            _persons.put(person.getId(), person);
        }
    }
}
