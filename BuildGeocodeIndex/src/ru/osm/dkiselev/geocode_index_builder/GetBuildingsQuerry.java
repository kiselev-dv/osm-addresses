package ru.osm.dkiselev.geocode_index_builder;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GetBuildingsQuerry implements DBTask {

	@Override
	public String getQuerry() {
		return 
			"select " +
                "bldng.osm_id, " +
                "settle.osm_id, " +
                "settle.\"A_CNTR\", " +
                "settle.\"A_RGN\", " +
                "settle.\"A_DSTRCT\", " +
                "settle.\"POPULATION\", " +
                "settle.\"NAME\", " +
                "settle.\"NAME_EN\", " +
                "settle.\"NAME_RU\", " +
                "bldng.\"A_SBRB\", " +
                "bldng.\"A_STRT\", " +
                "bldng.\"A_HSNMBR\", " +
                "%%bldngOSM.tags, " +
                "ST_AsText(ST_Centroid(bldng.geom)) " +
            "from layer.\"RU building-polygon\" bldng " +
                "join layer.\"RU settlement-polygon\" settle on ST_Within(bldng.geom, settle.geom) " +
                "join osm_polygon bldngOSM on bldngOSM.osm_id = bldng.osm_id " + 
            "where bldng.\"A_HSNMBR\" is not null and settle.\"NAME\" is not null limit 10000";
	}

	@Override
	public void handleRow(ResultSet rs) throws SQLException {
		
		BigDecimal building_id = rs.getBigDecimal(1);
		BigDecimal place_id = rs.getBigDecimal(2);
		String country = rs.getString(3);
		String region = rs.getString(4);
		String district = rs.getString(5);
		String population = rs.getString(6);
		String name = rs.getString(7);
		String nameEN = rs.getString(8);
		String nameRU = rs.getString(9);
		String suburb = rs.getString(10);
		String street = rs.getString(11);
		String number = rs.getString(12);
		Map<String, String> tags = parseTags(rs.getArray(13));
		double[] lonlat = getLonLat(rs.getString(14));
		
		processRow(new AddressRowWrapper(building_id, place_id, country, region, district, population, 
				name, nameEN, nameRU, suburb, tags.get("quarter"), street, number, tags, lonlat));
	}

	private void processRow(AddressRowWrapper addressRow) {

		List<IndexRow> parse = AddressRowParser.parse(addressRow);
		
		for(IndexRow row : parse){
			PSQLWriter.getInstance().write(row);
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

}
