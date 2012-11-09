package ru.osm.dkiselev.geocode_index_builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PSQLWriter {
	
	private static final PSQLWriter instance = new PSQLWriter();

	private Connection connection;

	private PSQLWriter(){
		try {
        	
        	connection = DriverManager.getConnection(
			   "jdbc:postgresql://localhost:5432/addresses","test", "test");
        	
		} catch (SQLException e) {
			throw new RuntimeException("Failed to connect to gis-lab.info:5432/osm_shp with user guest", e);
		}
	}

	public static PSQLWriter getInstance() {
		return instance;
	}

	public void write(IndexRow row) {
		
	}
	
}
