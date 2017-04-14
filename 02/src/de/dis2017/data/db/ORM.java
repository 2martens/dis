package de.dis2017.data.db;

import de.dis2017.data.EstateAgent;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps the data objects to the database and vice versa.
 */
public class ORM {
    private Connection _connection;
    
    private Map<Integer, EstateAgent> _agents;
    
    /**
     * Initializes the ORM.
     */
    public ORM() {
        DB2ConnectionManager _dbManager = DB2ConnectionManager.getInstance();
        _connection = _dbManager.getConnection();
        _agents = new HashMap<>();
    }
    
    /**
     * Loads the estate agent with the given ID from database and returns the corresponding object.
     *
     * @param ID the ID of the agent to load
     * @return returns the EstateAgent or null if there is no such agent
     */
    public EstateAgent getEstateAgent(int ID) {
        if (_agents.containsKey(ID)) {
            return _agents.get(ID);
        }
        
        try {
            // create query
            String            selectSQL = "SELECT * FROM ESTATEAGENT WHERE ID = ?";
            PreparedStatement pstmt     = _connection.prepareStatement(selectSQL);
            pstmt.setInt(1, ID);
        
            // execute query
            ResultSet rs = pstmt.executeQuery();
            EstateAgent agent;
            if (rs.next()) {
                agent = new EstateAgent();
                agent.setId(ID);
                agent.setName(rs.getString("name"));
                agent.setAddress(rs.getString("address"));
                agent.setLogin(rs.getString("login"));
                agent.setPassword(rs.getString("password"));
    
                rs.close();
                pstmt.close();
                
                _agents.put(ID, agent);
                return agent;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
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
    }
}
