package de.dis2017.data.db;

import de.dis2017.data.Estate;
import de.dis2017.data.EstateAgent;
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
     * Loads the estate agent with the given ID from database and returns the corresponding object.
     *
     * @param ID the ID of the agent to load
     * @return the EstateAgent or null if there is no such agent
     */
    public EstateAgent get(int ID) {
        if (_agents.containsKey(ID)) {
            return _agents.get(ID);
        }
        
        try {
            // create query
            String            selectSQL = "SELECT * FROM ESTATEAGENT WHERE ID = ?";
            PreparedStatement pstmt     = _connection.prepareStatement(selectSQL);
            pstmt.setInt(1, ID);
        
            return get(pstmt);
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
    public EstateAgent get(String username) {
        if (_agentsUsername.containsKey(username)) {
            return _agentsUsername.get(username);
        }
        
        try {
            // create query
            String            selectSQL = "SELECT * FROM ESTATEAGENT WHERE login = ?";
            PreparedStatement pstmt     = _connection.prepareStatement(selectSQL);
            pstmt.setString(1, username);
            
            return get(pstmt);
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
    private EstateAgent get(PreparedStatement pstmt)
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
