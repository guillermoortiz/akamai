package com.produban.api.data;

import java.io.Serializable;

public class Configuration implements Serializable{
	String key;
	String value;	
	
	public Configuration() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Configuration(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
