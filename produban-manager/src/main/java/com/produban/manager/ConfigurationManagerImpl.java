package com.produban.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.produban.api.data.Configuration;
import com.produban.api.manager.ConfigurationManager;
import com.produban.data.dao.ConfigurationDAO;

public class ConfigurationManagerImpl implements ConfigurationManager{
	ConfigurationDAO configurationDAO;
	
	@Override
	public List<Configuration> getListConfigurations() {
		return configurationDAO.getListConfigurations();
	}

	@Override
	public Map<String, String> getMapConfigurations() {
		Map<String, String> mapConfigurations = new HashMap<>();
		List<Configuration> configurations = configurationDAO.getListConfigurations();
		for (Configuration configuration : configurations){
			mapConfigurations.put(configuration.getKey(), configuration.getValue());
		}
		return mapConfigurations;
	}

	@Override
	public Configuration getConfiguration(String key) {
		return configurationDAO.getConfiguration(key);
	}

}
