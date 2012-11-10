package ru.osm.dkiselev.geocode_index_builder.querries;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.osm.dkiselev.geocode_index_builder.writers.Building;
import ru.osm.dkiselev.geocode_index_builder.writers.BuildingPSQLWriter;
import ru.osm.dkiselev.geocode_index_builder.writers.BuildingParser;
import ru.osm.dkiselev.geocode_index_builder.writers.PSQLWriter;

public class GetBuildingsQuerry extends PageableQuerryTask {

	@Override
	public String getQuerryNotPaged() {
		return 
			"select " +
                "bldng.osm_id, " +
                "bldng.\"A_SBRB\", " +
                "bldng.\"A_STRT\", " +
                "bldng.\"A_HSNMBR\", " +
                "%%bldngOSM.tags, " +
                "ST_AsText(ST_Centroid(bldng.geom)) " +
            "from layer.\"RU building-polygon\" bldng " +
                "join osm_polygon bldngOSM on bldngOSM.osm_id = bldng.osm_id " + 
            "where bldng.\"A_HSNMBR\" is not null";
	}

	@Override
	public void handleRsRow(ResultSet rs) throws SQLException {
		
		BigDecimal building_id = rs.getBigDecimal(1);
		String suburb = rs.getString(3);
		String street = rs.getString(4);
		String number = rs.getString(5);
		Map<String, String> tags = parseTags(rs.getArray(6));
		double[] lonlat = getLonLat(rs.getString(7));
		
		List<Building> parse = BuildingParser.parse(building_id, null, suburb, tags.get("addr:quarter"), street, number, tags, lonlat);
		
		for(Building row : parse){
			String asSQLInsert = BuildingPSQLWriter.asSQLInsert(row);
			PSQLWriter.getInstance().writeString(asSQLInsert);
		}
	}

	private double[] getLonLat(String string) {
		String[] split = string.substring(6, string.length() - 2).split(" ");
		return new double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1])};
	}

	private Map<String, String> parseTags(Array sqlArray) throws SQLException {
		
		String[] array = (String[]) sqlArray.getArray();
		Map<String, String> result = new HashMap<String, String>();
		
		for(int i = 0; i < array.length; ){
			result.put(array[i++], array[i++]);
		}
		
		return result;
	}

	@Override
	public void done() {
		PSQLWriter.getInstance().close();
	}

}
