package ru.osm.dkiselev.geocode_index_builder;

import java.math.BigDecimal;
import java.util.Map;

public class AddressRowWrapper {

	private BigDecimal buildingOSMid;
	private BigDecimal placeOSMid;
	private String country;
	private String province;
	private String district;
	private String placePopulation;
	private String placeName;
	private String placeNameEN;
	private String placeNameRU;
	private String suburb;
	private String quarter;
	private String street;
	private String number;
	
	private Map<String, String> allBuildingTags;
	private double[] lonlat;

	private AddressRowType type; 
	
	public AddressRowWrapper(BigDecimal buildingOSMid, BigDecimal placeOSMid,
			String country, String province, String district,
			String placePopulation, String placeName, String placeNameEN,
			String placeNameRU, String suburb, String quarter, String street, String number,
			 Map<String, String> allBuildingTags, double[] lonlat) {
				
		this.buildingOSMid = buildingOSMid;
		this.placeOSMid = placeOSMid;
		this.country = country;
		this.province = province;
		this.district = district;
		this.placePopulation = placePopulation;
		this.placeName = placeName;
		this.placeNameEN = placeNameEN;
		this.placeNameRU = placeNameRU;
		this.quarter = quarter;
		this.street = street;
		this.number = number;
		this.allBuildingTags = allBuildingTags;
		this.lonlat = lonlat;
	}

	public BigDecimal getBuildingOSMid() {
		return buildingOSMid;
	}

	public void setBuildingOSMid(BigDecimal buildingOSMid) {
		this.buildingOSMid = buildingOSMid;
	}

	public BigDecimal getPlaceOSMid() {
		return placeOSMid;
	}

	public void setPlaceOSMid(BigDecimal placeOSMid) {
		this.placeOSMid = placeOSMid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPlacePopulation() {
		return placePopulation;
	}

	public void setPlacePopulation(String placePopulation) {
		this.placePopulation = placePopulation;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceNameEN() {
		return placeNameEN;
	}

	public void setPlaceNameEN(String placeNameEN) {
		this.placeNameEN = placeNameEN;
	}

	public String getPlaceNameRU() {
		return placeNameRU;
	}

	public void setPlaceNameRU(String placeNameRU) {
		this.placeNameRU = placeNameRU;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Map<String, String> getAllBuildingTags() {
		return allBuildingTags;
	}

	public void setAllBuildingTags(Map<String, String> allBuildingTags) {
		this.allBuildingTags = allBuildingTags;
	}

	public double[] getLonlat() {
		return lonlat;
	}

	public void setLonlat(double[] lonlat) {
		this.lonlat = lonlat;
	}

	public AddressRowType getType() {
		return type;
	}

	public void setType(AddressRowType type) {
		this.type = type;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

}
