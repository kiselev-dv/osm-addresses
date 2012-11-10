package ru.osm.dkiselev.geocode_index_builder.writers;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;


public class BuildingPSQLWriter {
	
	public static String asSQLInsert(Building building)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO (osmid, city, suburb, quarter, street, number, centroid) buildings VALUES(");
		
		//building id
		sql.append(building.getBuildingOSMid()).append(", ");
		
		//place id
		sql.append(building.getPlaceOSMid()).append(", ");
		
		//suburb
		if(StringUtils.stripToNull(building.getSuburb()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(building.getSuburb())).append("', ");
		}

		//quarter
		if(StringUtils.stripToNull(building.getQuarter()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(building.getQuarter())).append("', ");
		}

		//street
		if(StringUtils.stripToNull(building.getStreet()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(StringEscapeUtils.escapeSql(building.getStreet())).append("', ");
		}
		
		//number
		if(StringUtils.stripToNull(building.getNumber()) == null){
			sql.append("NULL, ");
		}
		else{
			sql.append("'").append(building.getNumber()).append("', ");
		}

		sql.append("'").append(building.getType().name()).append("', ");

		double[] lonlat = building.getLonlat();
		sql.append("st_point (").append(lonlat[0]).append(", ").append(lonlat[1]).append(", 4326)");
		
		sql.append(")");
		
		return sql.toString();
	}

}
