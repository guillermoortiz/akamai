package com.produban.api.data;

import java.io.Serializable;

/**
 * Class to store data with a business process. It has information about the
 * regex we want to look for, number of times that it's to happen in a time and
 * the message to save
 * 
 * @author ortizg1
 * 
 */
public class Rule implements Serializable {
	private String regex;
	private int numberTimes;
	private long totalTime;
	private long windowTime;
	private String message;

	
	
	
	public Rule(String regex, int numberTimes, long totalTime, long windowTime,
			String message) {
		super();
		this.regex = regex;
		this.numberTimes = numberTimes;
		this.totalTime = totalTime;
		this.windowTime = windowTime;
		this.message = message;
	}

	public Rule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public int getNumberTimes() {
		return numberTimes;
	}

	public void setNumberTimes(int numberTimes) {
		this.numberTimes = numberTimes;
	}

	
	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public long getWindowTime() {
		return windowTime;
	}

	public void setWindowTime(long windowTime) {
		this.windowTime = windowTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
