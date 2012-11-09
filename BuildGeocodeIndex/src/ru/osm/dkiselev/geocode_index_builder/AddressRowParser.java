package ru.osm.dkiselev.geocode_index_builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AddressRowParser {
	
	public static List<IndexRow> parse(AddressRowWrapper addressRow){
		List<IndexRow> result = new ArrayList<IndexRow>();
		
		String number = addressRow.getNumber();
		
		Map<String, String> tags = addressRow.getAllBuildingTags();
		
		if(tags.containsKey("addr:street2") && tags.containsKey("addr:housenumber2")){
			
			addressRow.setType(AddressRowType.Karlsrue);
			result.add(new IndexRow(addressRow));
			
			addressRow.setType(AddressRowType.HN2);
			
			addressRow.setStreet(tags.get("addr:street2"));
			addressRow.setNumber(tags.get("addr:housenumber2"));
			
			result.add(new IndexRow(addressRow));
		}
		else if(tags.containsKey("addr:street2"))
		{
			addressRow.setType(AddressRowType.StreetNFirstRow);
			result.add(new IndexRow(addressRow));
			
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
			result.add(new IndexRow(addressRow));
			
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
				
				result.add(new IndexRow(addressRow));
			}
		}
		else{
			addressRow.setType(AddressRowType.Karlsrue);
			result.add(new IndexRow(addressRow));
		}
		
		return result;
	}
	
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

}
