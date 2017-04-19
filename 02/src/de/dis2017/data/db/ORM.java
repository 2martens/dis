package de.dis2017.data.db;

import de.dis2017.data.Apartment;
import de.dis2017.data.Estate;
import de.dis2017.data.EstateAgent;
import de.dis2017.data.House;
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
    private Map<String, EstateAgent> _agentsUsername;
    private Map<Integer, Estate> _estates;
    
    /**
     * Initializes the ORM.
     */
    public ORM() {
        DB2ConnectionManager _dbManager = DB2ConnectionManager.getInstance();
        _connection = _dbManager.getConnection();
        _agents = new HashMap<>();
        _agentsUsername = new HashMap<>();
        _estates = new HashMap<>();
    }
    
    /**
     * Loads all objects from the database and returns a list of them.
     *
     * @param objectType the type of objects to load
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
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return objects;
    }
    
    /**
     * Process a select all query for estates.
     *
     * @param rs the result set of such a query
     * @return a list of estates
     * @throws SQLException when an error occurs during the rs.next call
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
        
            _estates.put(estate.getId(), estate);
            estates.add(estate);
        }
        
        return estates;
    }
    
    /**
     * Processes a select all query for estate agents.
     *
     * @param rs the result set of such a query
     * @return a list of agents
     * @throws SQLException when an error occurs during the rs.next call
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
     * Loads the estate with the given ID from database and returns the corresponding object.
     *
     * @param ID the id of the estate to load
     * @return the Estate or null if there is no such object
     */
    public Estate getEstate(int ID) {
        if (_estates.containsKey(ID)) {
            return _estates.get(ID);
        }
    
        String            selectSQLHouse = "SELECT * FROM HOUSE LEFT JOIN ESTATE ON HOUSE.ID = ESTATE.ID " +
                                           "WHERE HOUSE.ID = ?";
        String selectSQLApartment = "SELECT * FROM APARTMENT LEFT JOIN ESTATE ON APARTMENT.ID = ESTATE.ID " +
                                    "WHERE APARTMENT.ID = ?";
    
        String countHouse = "SELECT COUNT(ID) AS count FROM HOUSE WHERE ID = ?";
        String countApartment = "SELECT COUNT(ID) AS count FROM APARTMENT WHERE ID = ?";
        try {
            // try house first
            PreparedStatement preparedStatementCount = _connection.prepareStatement(countHouse);
            preparedStatementCount.setInt(1, ID);
            ResultSet rs = preparedStatementCount.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            String type = "None";
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
            }
            else {
                type = "House";
            }
            rs.close();
            preparedStatementCount.close();
            
            PreparedStatement pstmt;
            Estate estate;
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
     * @param ID the ID of the agent to load
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
     * Loads the estate agent with the given username from database and returns the corresponding object,
     *
     * @param username the username of the estate agent
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
     * @param pstmt the prepared statement with parameters already set
     * @return the EstateAgent or null
     */
    @Nullable
    private EstateAgent getAgent(PreparedStatement pstmt)
    {
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
     * @param agent the agent that should be deleted
     */
    public void delete(EstateAgent agent)
    {
        if (agent.getId() == -1) {
            System.err.println("This agent is not yet persisted to the dabase and cannot be deleted.");
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
     * Deletes an object from the database.
     *
     * @param sql the sql used for deletion
     * @param id the id of the object to be deleted
     */
    private void delete(String sql, int id)
    {
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
     * @param agent the agent that should be persisted
     */
    public void persist(EstateAgent agent)
    {
        try {
            if (agent.getId() == -1) {
                String insertSQL = "INSERT INTO ESTATEAGENT (name, address, login, password) VALUES (?, ?, ?, ?)";
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
                String updateSQL = "UPDATE ESTATEAGENT SET name = ?, address = ?, password = ? WHERE ID = ?";
                PreparedStatement pstmt = _connection.prepareStatement(updateSQL);
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
}
