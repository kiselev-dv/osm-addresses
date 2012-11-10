package ru.osm.dkiselev.geocode_index_builder.writers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ru.osm.dkiselev.geocode_index_builder.AddressRowType;

public class BuildingParser {
	
	private static Map<String, String> getAddrNTags(Map<String, String> tags, int i) {
		Map<String, String> result = new HashMap<String, String>();
		
		for(Entry<String, String> entry : tags.entrySet()){
			if(entry.getKey().startsWith("addr" + i + ":")){
				result.put(entry.getKey().replace("addr" + i + ":", "addr:"), entry.getValue());
			}
		}
		
		return result;
	}

	private static boolean isAddrN(Map<String, String> tags) {
		for(String s : tags.keySet()){
			if(s.startsWith("addr2:")){
				return true;
			}
		}
		return false;
	}

	public static List<Building> parse(BigDecimal building_id,
			BigDecimal settle_id, String suburb, String quarter, String street, String nmbr,
			Map<String, String> tags, double[] lonlat) {
		
		List<Building> result = new ArrayList<Building>();
		
		String number = nmbr;
		
		if(tags.containsKey("addr:street2") && tags.containsKey("addr:housenumber2")){
			
			result.add(new Building(building_id, settle_id, suburb, quarter, street, number, lonlat, AddressRowType.HN2First));

			result.add(new Building(building_id, settle_id, suburb, quarter, tags.get("addr:street2"), tags.get("addr:housenumber2"), lonlat, AddressRowType.HN2));
			
		}
		else if(tags.containsKey("addr:street2"))
		{
			result.add(new Building(building_id, settle_id, suburb, quarter, street, number, lonlat, AddressRowType.StreetNFirstRow));
			
			String[] numbers = number.split("/");
			//addressRow.setType(AddressRowType.StreetNRow);
			for(int i = 0; i < numbers.length; i++)
			{
				String n = numbers[i];
				Building b = new Building(building_id, settle_id, suburb, quarter, street, n, lonlat, AddressRowType.StreetNRow);
				
				if(i > 0){
					String streetN = tags.get("addr:street" + (i + 1));
					b.setStreet(streetN);
				}
				result.add(b);
			}
			
		}
		else if(isAddrN(tags)){
			
			result.add(new Building(building_id, settle_id, suburb, quarter, street, number, lonlat, AddressRowType.AddrNFirstRow));
			
			for(int i = 2; i < 10; i++){
				
				Map<String, String> addrNTags = getAddrNTags(tags, i);
				if(addrNTags.isEmpty()){
					break;
				}
				
				Building b = new Building(building_id, settle_id, null, null, null, null, lonlat, AddressRowType.AddrNRow);
				
				b.setNumber(addrNTags.get("addr:housenumber"));
				b.setStreet(addrNTags.get("addr:street"));
				b.setQuarter(addrNTags.get("addr:quarter"));
				b.setSuburb(addrNTags.get("addr:suburb"));
				
				result.add(b);
			}
		}
		else{
			result.add(new Building(building_id, settle_id, suburb, quarter, street, number, lonlat, AddressRowType.Karlsrue));
		}
		
		return result;
	}

}
