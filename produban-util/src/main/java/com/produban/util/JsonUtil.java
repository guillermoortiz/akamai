package com.produban.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import com.produban.util.exception.ProdubanException;

/**
 * Utility class for JSON purpouses.
 */
public final class JsonUtil {
	private static final Logger LOG = Logger.getLogger(JsonUtil.class);

	/**
	 * Constructor class.
	 */
	private JsonUtil() {
	}

	/**
	 * Instance of Jackson class to parse and format objects to JSON.
	 */
	private static ObjectMapper om = new ObjectMapper();

	/**
	 * Decode an object from his JSON representation.
	 * 
	 * @param <T>
	 *            Class of the Object to parse.
	 * 
	 * @param aObject
	 *            byte[] with the JSON representation of the object.
	 * @param clazz
	 *            Class of the object represented.
	 * @return Object Object decoded.
	 */
	public static <T> T read(final byte[] aObject, final Class<T> clazz) {
		T oObject;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(aObject);
			oObject = om.readValue(bis, clazz);
			LOG.debug("Object read");
		} catch (IOException ex) {
			throw new ProdubanException("Error transforming Json to object", ex);
		}
		return oObject;
	}

	public static String write(final Object aObject) {
		String json;
		try {
			ObjectWriter ow = new ObjectMapper().writer()
					.withDefaultPrettyPrinter();
			json = ow.writeValueAsString(aObject);
			LOG.debug("Object parsed, json is: " + json);
		} catch (IOException ex) {
			throw new ProdubanException("Error transforming object to String",
					ex);
		}
		return json;
	}
}
