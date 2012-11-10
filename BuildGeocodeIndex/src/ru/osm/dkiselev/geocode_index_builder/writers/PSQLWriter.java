package ru.osm.dkiselev.geocode_index_builder.writers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class PSQLWriter {
	
	private static final PSQLWriter instance = new PSQLWriter();
	private int counter = 0;

	protected Connection connection = null;
	private Statement stmt = null;

	private PSQLWriter(){
		try {
        	
        	connection = DriverManager.getConnection(
			   "jdbc:postgresql://localhost:5432/addresses","test", "test");
        	
		} catch (SQLException e) {
			throw new RuntimeException("Failed to connect to localhost:5432/addresses with user test", e);
		}
	}

	public static PSQLWriter getInstance() {
		return instance;
	}

	public void writeString(String sql) {
		if(counter++ % 1000 == 0){
			flushStmnt();
		}
		
		try {
			stmt.addBatch(sql);
		} catch (SQLException e) {
			throw new RuntimeException("cant add insert to batch: " + sql.toString(), e);
		}
	}

	private void flushStmnt() {
		if(stmt != null){
			try {
				stmt.executeBatch();
			} catch (SQLException e) {
				throw new RuntimeException("Failed to write batch to addresses db", e);
			}finally{
				try {
					stmt.close();
				} catch (SQLException e) {
					throw new RuntimeException("Failed to close statement fro addresses db", e);
				}
			}
		}
		
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create statement", e);
		}
		
		System.out.println(counter - 1);
	}

	public void close() {
		if(stmt != null){
			try {
				if(!stmt.isClosed()){
					stmt.executeBatch();
				}
			} catch (SQLException e) {
				throw new RuntimeException("Failed to write batch to addresses db", e);
			}finally{
				try {
					stmt.close();
				} catch (SQLException e) {
					throw new RuntimeException("Failed to close statement fro addresses db", e);
				}
			}
		}
		
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException("can't close connection to addresses db", e);
		}
		
	}
	
}
