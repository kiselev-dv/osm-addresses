package ru.osm.dkiselev.geocode_index_builder;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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

		String number = addressRow.getNumber();
		
		Map<String, String> tags = addressRow.getAllBuildingTags();
		
		if(tags.containsKey("addr:street2") && tags.containsKey("addr:housenumber2")){
			
			addressRow.setType(AddressRowType.Karlsrue);
			handleAddressRow(addressRow);
			
			addressRow.setType(AddressRowType.HN2);
			
			addressRow.setStreet(tags.get("addr:street2"));
			addressRow.setNumber(tags.get("addr:housenumber2"));
			
			handleAddressRow(addressRow);
		}
		else if(tags.containsKey("addr:street2"))
		{
			addressRow.setType(AddressRowType.StreetNFirstRow);
			handleAddressRow(addressRow);
			
			String[] numbers = number.split("/");
			addressRow.setType(AddressRowType.StreetNRow);
			for(int i = 0; i < numbers.length; i++)
			{
				String n = numbers[i];
				addressRow.setNumber(n);
				
				if(i > 0){
					String streetN = tags.get("addr:street" + (i + 1));
					addressRow.setStreet(streetN);
				}
			}
		}
		else if(isAddrN(tags)){
			
			addressRow.setType(AddressRowType.AddrNFirstRow);
			handleAddressRow(addressRow);
			
			for(int i = 2; i < 10; i++){
				
				Map<String, String> addrNTags = getAddrNTags(tags, i);
				if(addrNTags.isEmpty()){
					break;
				}
				
				addressRow.setType(AddressRowType.AddrNRow);
				
				addressRow.setNumber(addrNTags.get("addr:housenumber"));
				addressRow.setStreet(addrNTags.get("addr:street"));
				addressRow.setQuarter(addrNTags.get("addr:quarter"));
				
				if(addrNTags.size() > 3){
					//System.out.println("Something interested");
				}
				
				handleAddressRow(addressRow);
			}
		}
		else{
			addressRow.setType(AddressRowType.Karlsrue);
			handleAddressRow(addressRow);
		}
		
	}

	private Map<String, String> getAddrNTags(Map<String, String> tags, int i) {
		Map<String, String> result = new HashMap<String, String>();
		
		for(Entry<String, String> entry : tags.entrySet()){
			if(entry.getKey().startsWith("addr" + i + ":")){
				result.put(entry.getKey().replace("addr" + i + ":", "addr:"), entry.getValue());
			}
		}
		
		return result;
	}

	private boolean isAddrN(Map<String, String> tags) {
		for(String s : tags.keySet()){
			if(s.startsWith("addr2:")){
				return true;
			}
		}
		return false;
	}

	private void handleAddressRow(AddressRowWrapper addressRow) {
		String fullAddressRU = (new IndexRow(addressRow)).getFullAddressRU();
		System.out.println(fullAddressRU);
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
