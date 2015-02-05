package com.produban.akamai.entity;

import java.io.Serializable;

public class Geo implements Serializable {
	String country;
	String region;
	String city;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Geo [country=" + country + ", region=" + region + ", city="
				+ city + "]";
	}

}
