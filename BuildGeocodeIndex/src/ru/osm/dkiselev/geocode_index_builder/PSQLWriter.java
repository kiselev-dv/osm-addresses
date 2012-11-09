package ru.osm.dkiselev.geocode_index_builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class PSQLWriter {
	
	private static final PSQLWriter instance = new PSQLWriter();
	private int counter = 0;

	private Connection connection = null;
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

	public void write(IndexRow row) {
		if(counter++ % 1000 == 0){
			flushStmnt();
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO addresses VALUES(");
		sql.append(row.getBuildingOSMid()).append(", ");
		sql.append(row.getPlaceOSMid()).append(", ");
		sql.append("'").append(row.getFullAddressRU()).append("', ");
		
		if(StringUtils.stripToNull(row.getCountry()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(row.getCountry())).append("', ");
		}
		
		if(StringUtils.stripToNull(row.getProvince()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(row.getProvince())).append("', ");
		}
		
		if(StringUtils.stripToNull(row.getDistrict()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(row.getDistrict())).append("', ");
		}

		if(StringUtils.stripToNull(row.getPlaceName()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(row.getPlaceName())).append("', ");
		}

		if(StringUtils.stripToNull(row.getQuarter()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(row.getQuarter())).append("', ");
		}

		if(StringUtils.stripToNull(row.getStreet()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(row.getStreet())).append("', ");
		}
		
		if(StringUtils.stripToNull(row.getNumber()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(row.getNumber()).append("', ");
		}

		int population = 0;
		try{
			population = Integer.getInteger(row.getPlacePopulation().replace(" ", "").replace(",", "").replace(".", ""));
		}catch(Exception e )
		{
			//do nothing
		}
		
		sql.append(population).append(", ");
		
		if(StringUtils.stripToNull(row.getPlaceNameEN()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(row.getPlaceNameEN())).append("', ");
		}

		if(StringUtils.stripToNull(row.getPlaceNameRU()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(row.getPlaceNameRU())).append("', ");
		}

		sql.append("'").append(row.getAddressRowType().name()).append("', ");

		sql.append(row.getLon()).append(", ");
		sql.append(row.getLat());
		
		sql.append(")");
		
		try {
			stmt.addBatch(sql.toString());
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
