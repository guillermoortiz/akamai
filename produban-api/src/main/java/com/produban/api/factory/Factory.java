package com.produban.api.factory;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.produban.api.manager.ConfigurationManager;

public final class Factory {

	private static final Logger LOG = Logger.getLogger(Factory.class);
	private static ApplicationContext context;

	private Factory() {
	}

	static synchronized ApplicationContext getContext() {

		if (context == null) {
			String[] locations = { "produban-manager.xml", "produban-api.xml" };

			ArrayList<String> available = new ArrayList<String>();
			ClassLoader loader = Factory.class.getClassLoader();
			for (String location : locations) {
				if (loader.getResource(location) != null) {
					available.add(location);
				}
			}

			if (LOG.isInfoEnabled()) {
				LOG.info("Loading spring context with resources: " + available);
			}

			String[] availableLocations = new String[available.size()];
			for (int i = 0; i < available.size(); i++) {
				availableLocations[i] = available.get(i);
			}

			context = new ClassPathXmlApplicationContext(availableLocations);
		}

		return context;
	}

	/**
	 * Método utilizado para obtener el objeto encargado de las operaciones
	 * sobre la configuración.
	 * 
	 * @return Objeto configurado para realizar operaciones sobre la
	 *         configuración.
	 */
	public static ConfigurationManager getConfigurationManager() {
		final ConfigurationManager bean = (ConfigurationManager) getContext()
				.getBean("configurationManager");

		return bean;
	}

	/**
	 * Método utilizado para obtener la factoria de sesiones de hibernate.
	 * 
	 * @return Objeto configurado para realizar operaciones sobre el catálogo
	 *         de servidores.
	 */
	static SessionFactory getSessionFactory() {
		final SessionFactory bean = (SessionFactory) getContext().getBean(
				"sessionFactory");

		return bean;
	}

}
