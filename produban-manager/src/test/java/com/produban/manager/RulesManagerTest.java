package com.produban.manager;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import com.produban.api.data.Configuration;
import com.produban.api.data.Rule;
import com.produban.api.general.Factory;
import com.produban.api.manager.ConfigurationManager;
import com.produban.api.manager.RulesManager;

public class RulesManagerTest {
	@Before
    public void setUp() throws Exception {
        System.out.println(this.getClass().getName() + ".setUp");	        
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(this.getClass().getName() + ".tearDown");	        
    }

    @Test
    public void listRules() {
        System.out.println(this.getClass().getName() + ".listConfiguration");
        
        RulesManager manager = Factory.getRulesManager();
        List<Rule> rules = manager.getRules();	        
        
        assertTrue("Incorrect number rules", rules.size() > 0);
    }
	 
}
