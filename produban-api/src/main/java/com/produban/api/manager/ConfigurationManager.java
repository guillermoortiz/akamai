package com.produban.api.manager;

import java.util.List;

import com.produban.api.entities.Configuration;

public interface ConfigurationManager {

	Configuration getConfiguration(String key);

	String get(String key);

	List<Configuration> list();

}
