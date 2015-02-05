package com.produban.akamai.api;

import java.io.Serializable;
import java.util.Date;

import com.produban.akamai.entity.Akamai;

public class Alert implements Serializable {
	String type;
	Date date;
	Akamai[] logs;

	public Alert() {
		super();
		date = new Date();
	}

	public Alert(String type, Akamai[] logs) {
		super();
		this.type = type;
		this.logs = logs;
		date = new Date();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Akamai[] getLogs() {
		return logs;
	}

	public void setLogs(Akamai[] logs) {
		this.logs = logs;
	}

	@Override
	public String toString() {
		return "Alert [type=" + type + ", date=" + date + ", logs=" + logs
				+ "]";
	}

}
