package ru.osm.dkiselev.geocode_index_builder;

import java.math.BigDecimal;

public class IndexRow {

	private double lon;
	private double lat;

	private String country;
	private String province;
	private String district;
	private String placeName;
	private String quarter;
	private String street;
	private String number;

	private String placePopulation;
	private String placeNameEN;
	private String placeNameRU;
	
	private AddressRowType addressRowType;
	
	private BigDecimal buildingOSMid;
	private BigDecimal placeOSMid;
	
	public IndexRow(){
		
	}
	
	public IndexRow(AddressRowWrapper addressRow){
		
		lon = addressRow.getLonlat()[0];
		lat = addressRow.getLonlat()[1];
		
		country = addressRow.getCountry();
		province = addressRow.getProvince();
		district = addressRow.getDistrict();
		placeName = addressRow.getPlaceName();
		quarter = addressRow.getQuarter();
		street = addressRow.getStreet();
		number = addressRow.getNumber();
		
		placePopulation = addressRow.getPlacePopulation();
		placeNameEN = addressRow.getPlaceNameEN();
		placeNameRU = addressRow.getPlaceNameRU();
		addressRowType = addressRow.getType();
		
		buildingOSMid = addressRow.getBuildingOSMid();
		placeOSMid = addressRow.getPlaceOSMid();
	}
	
	public String getFullAddressRU(){
		
		StringBuilder result = new StringBuilder();
		
		if(country != null){
			result.append(country);
		}
		
		if(province != null){
			result.append(", ").append(province);
		}

		if(district != null && !district.equals(province)){
			result.append(", ").append(district);
		}

		if(placeName != null && !placeName.equals(district)){
			result.append(", ").append(placeName);
		}
		
		if(quarter != null){
			result.append(", ").append(quarter);
		}

		if(street != null){
			result.append(", ").append(street);
		}

		if(street != null){
			result.append(", ").append(number);
		}
		
		return result.toString();
	}

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}

	public String getCountry() {
		return country;
	}

	public String getProvince() {
		return province;
	}

	public String getDistrict() {
		return district;
	}

	public String getPlaceName() {
		return placeName;
	}

	public String getQuarter() {
		return quarter;
	}

	public String getStreet() {
		return street;
	}

	public String getNumber() {
		return number;
	}

	public String getPlacePopulation() {
		return placePopulation;
	}

	public String getPlaceNameEN() {
		return placeNameEN;
	}

	public String getPlaceNameRU() {
		return placeNameRU;
	}

	public AddressRowType getAddressRowType() {
		return addressRowType;
	}

	public BigDecimal getBuildingOSMid() {
		return buildingOSMid;
	}

	public BigDecimal getPlaceOSMid() {
		return placeOSMid;
	} 
	
}
