package com.produban.api.manager;

import java.util.List;
import java.util.Map;

import com.produban.api.data.Configuration;

public interface ConfigurationManager {
	public List<Configuration> getConfigurations();
	public Map<String, String> getMapConfigurations();
	public Configuration getConfiguration(String key);
}
