package com.produban.injector.flume.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.http.HTTPBadRequestException;
import org.apache.flume.source.http.HTTPSourceHandler;
import org.apache.log4j.Logger;

/**
 * 
 * Handler for Flume which just generates an event for each line we got from
 * HTTP POST called. Events don't have header.
 * 
 * @author x100516
 * 
 */
public class StringHandler implements HTTPSourceHandler {
	private static final Logger LOG = Logger.getLogger(StringHandler.class);

	@Override
	public void configure(Context context) {
		// Not configuration
	}

	@Override
	public List<Event> getEvents(HttpServletRequest request)
			throws HTTPBadRequestException, Exception {

		BufferedReader reader = request.getReader();
		return getSimpleEvents(reader);

	}

	List<Event> getSimpleEvents(final BufferedReader reader) {
		List<Event> newEvents = new ArrayList<Event>();
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println("XXX " + line);
				if (LOG.isDebugEnabled()) {
					LOG.debug("JSON read:" + line);
				}
				newEvents.add(EventBuilder.withBody(line.getBytes()));
			}
		} catch (IOException ioe) {
			System.out.println("XXX error " + ioe);
			throw new FlumeException(
					"Error reading BufferedReader from HTTP POST", ioe);
		}
		return newEvents;
	}
}
