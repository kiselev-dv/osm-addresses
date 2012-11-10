package ru.osm.dkiselev.geocode_index_builder.querries;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GetPlacesQuerry extends PageableQuerryTask {

	@Override
	public String getQuerryNotPaged() {
		return "SELECT osm_id, \"NAME\", \"NAME_EN\", \"NAME_RU\", \"PLACE\", \"A_CNTR\", \"A_RGN\", " +  
					"\"A_DSTRCT\", ST_ASText(geom) " + 
				"FROM layer.\"RU settlement-polygon\"";
	}

	@Override
	protected void handleRsRow(ResultSet rs) throws SQLException {
		
		BigDecimal osm_id = rs.getBigDecimal(1);
		String name = rs.getString(2);
		String nameEN = rs.getString(3);
		String nameRU = rs.getString(4);
		String place = rs.getString(5);
		String contry = rs.getString(6);
		String region = rs.getString(7);
		String district = rs.getString(8);
		String geometry = rs.getString(9);
		
	}

	@Override
	public void done() {
		
	}

}
