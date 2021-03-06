package ru.osm.dkiselev.geocode_index_builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class DBTaskManager {
	
	public static void execute(Collection<DBTask> tasks){
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Can't find psql driver", e);
		}
		
        Connection connection = null;
        
        try {
        	
        	connection = DriverManager.getConnection(
			   "jdbc:postgresql://gis-lab.info:5432/osm_shp","guest", "guest");
        	
		} catch (SQLException e) {
			throw new RuntimeException("Failed to connect to gis-lab.info:5432/osm_shp with user guest", e);
		}
        
        try{
        	if(connection != null){
        		for(DBTask task : tasks){
        			
        			try{
        				while(task.doAgain()){
        					
        					String querryString = task.getQuerry();
        					
        					try {
        						
        						Statement stm = connection.createStatement();
        						
        						ResultSet rs = stm.executeQuery(querryString);
        						
        						while(rs.next()){
        							task.handleRow(rs);
        						}
        						
        						rs.close();
        						
        					} catch (SQLException e) {
        						throw new RuntimeException("Failed to execute querry " + querryString, e);
        					}
        				}
        			}finally{
        				task.done();
        			}
        		}
        	}
        }
        catch (Exception t)
        {
        	if(t.getCause() instanceof SQLException){
        		t.printStackTrace();
        		((SQLException)t.getCause()).getNextException().printStackTrace();
        	}
        }
        finally{
        	if(connection != null){
        		try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Failed to close connection", e);
				}
        	}
        }
        
	}

}
