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
	private long window;
	private long slideWindow;
	private String message;

	public Rule(String regex, int numberTimes, long window, long slideWindow,
			String message) {
		super();
		this.regex = regex;
		this.numberTimes = numberTimes;
		this.window = window;
		this.slideWindow = slideWindow;
		this.message = message;
	}

	public Rule() {
		super();		
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

	public long getWindow() {
		return window;
	}

	public void setWindow(long window) {
		this.window = window;
	}

	public long getSlideWindow() {
		return slideWindow;
	}

	public void setSlideWindow(long slideWindow) {
		this.slideWindow = slideWindow;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
