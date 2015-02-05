package com.produban.api.data;

import java.util.List;

import com.produban.api.entities.Configuration;

public interface ConfigurationDAO {

	List<Configuration> list();

	Configuration getConfiguration(String id);
}
