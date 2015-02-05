package com.produban.injector.flume.handler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import junit.framework.Assert;

import org.apache.flume.Event;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * JUnit test for StringHandler
 * 
 */
public class StringHandlerTest {
	private static final Logger LOG = Logger.getLogger(StringHandlerTest.class);

	StringHandler stringHandler;

	@Before
	public void setUp() throws Exception {
		stringHandler = new StringHandler();
	}

	@Test
	public void checkOneEvent() {
		String str = "Ex1";
		List<Event> events = callGetEvents(str);
		Event event = events.get(0);
		String body = new String(event.getBody());
		Assert.assertEquals("Ex1", body);
	}

	@Test
	public void getOneEvent() {
		String str = "Ex1";
		List<Event> events = callGetEvents(str);
		Assert.assertEquals(1, events.size());
	}

	@Test
	public void getNEvents() {
		String str = "Ex1;\nEx2";
		List<Event> events = callGetEvents(str);
		Assert.assertEquals(2, events.size());
	}

	@Test
	public void getNotEvents() {
		String str = "";
		List<Event> events = callGetEvents(str);
		Assert.assertEquals(0, events.size());
	}

	private BufferedReader createInput(final String input) {
		InputStream is = new ByteArrayInputStream(input.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return br;
	}

	private List<Event> callGetEvents(final String input) {
		BufferedReader reader = createInput(input);
		List<Event> events = stringHandler.getSimpleEvents(reader);
		return events;

	}
}
