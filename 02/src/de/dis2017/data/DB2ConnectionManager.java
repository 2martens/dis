package de.dis2017.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton for management of database connections
 * 
 * @author Michael von Riegen
 * @version April 2009
 */
class DB2ConnectionManager {

	// instance of Driver Manager
	private static DB2ConnectionManager _instance = null;

	// DB2 connection
	private Connection _con;

	/**
	 * Initializes database connection
	 */
	private DB2ConnectionManager() {
		try {
			// load properties from db2.properties file
			Properties properties = new Properties();
			URL url = ClassLoader.getSystemResource("db2.properties");
			FileInputStream stream = new FileInputStream(new File(url.toURI()));
			properties.load(stream);
			stream.close();

			String jdbcUser = properties.getProperty("jdbc_user");
			String jdbcPass = properties.getProperty("jdbc_pass");
			String jdbcUrl = properties.getProperty("jdbc_url");

			// created connection to DB2 database
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			_con = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPass);

		} catch (IOException | ClassNotFoundException | SQLException | URISyntaxException e){
			e.printStackTrace();
		}

	}

	/**
	 * Returns the instance of the connection manager
	 * 
	 * @return DB2ConnectionManager
	 */
	static DB2ConnectionManager getInstance() {
		if (_instance == null) {
			_instance = new DB2ConnectionManager();
		}
		return _instance;
	}

	/**
	 * Returns the connection to the DB2 database
	 * 
	 * @return Connection
	 */
	Connection getConnection() {
		return _con;
	}
}
