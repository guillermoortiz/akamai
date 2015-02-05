package com.produban.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class FilePropertiesUtil {
	private static final Logger LOG = Logger
			.getLogger(FilePropertiesUtil.class);

	public static Properties getProperties(final String path) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(path);
			prop.load(input);
		} catch (IOException ex) {
			LOG.error("Error opening file " + path, ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOG.error("Error closing file " + path, e);
				}
			}
		}
		return prop;
	}
}
