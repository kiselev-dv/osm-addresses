package ru.osm.dkiselev.geocode_index_builder.writers;

import java.math.BigDecimal;

import ru.osm.dkiselev.geocode_index_builder.AddressRowType;

public class Building {
	
	private BigDecimal buildingOSMid;
	private BigDecimal placeOSMid;
	private String suburb;
	private String quarter;
	private String street;
	private String number;
	private double[] lonlat;
	private AddressRowType type;
	
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
	public String getSuburb() {
		return suburb;
	}
	public void setSuburb(String suburb) {
		this.suburb = suburb;
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
	public double[] getLonlat() {
		return lonlat;
	}
	public void setLonlat(double[] lonlat) {
		this.lonlat = lonlat;
	}
	public Building(BigDecimal buildingOSMid, BigDecimal placeOSMid,
			String suburb, String quarter, String street, String number,
			double[] lonlat, AddressRowType type) {
		super();
		this.buildingOSMid = buildingOSMid;
		this.placeOSMid = placeOSMid;
		this.suburb = suburb;
		this.quarter = quarter;
		this.street = street;
		this.number = number;
		this.lonlat = lonlat;
		this.type = type;
	}
	public AddressRowType getType() {
		return type;
	}
	public void setType(AddressRowType type) {
		this.type = type;
	}
}
