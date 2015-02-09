package com.produban.manager;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import com.produban.api.data.Configuration;
import com.produban.api.general.Factory;
import com.produban.api.manager.ConfigurationManager;

public class ConfigurationManagerTest {
	 	@Before
	    public void setUp() throws Exception {
	        System.out.println(this.getClass().getName() + ".setUp");	        
	    }

	    @After
	    public void tearDown() throws Exception {
	        System.out.println(this.getClass().getName() + ".tearDown");	        
	    }

	    @Test
	    public void listConfiguration() {
	        System.out.println(this.getClass().getName() + ".listConfiguration");
	        
	        ConfigurationManager manager = Factory.getConfigurationManager();
	        List<Configuration> configurations = manager.getListConfigurations();	        
	        
	        assertTrue("Incorrect number configurations", configurations.size() > 0);
	    }
	 
}
