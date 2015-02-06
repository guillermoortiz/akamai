package com.produban.data.dao;

import java.util.List;

import com.produban.api.data.Configuration;


public interface ConfigurationDAO {
	public List<Configuration> getListConfigurations();
	public Configuration getConfiguration(final String key);
}
