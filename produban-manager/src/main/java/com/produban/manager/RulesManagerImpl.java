/**
 * 
 */
package com.produban.manager;

import java.util.List;

import com.produban.api.data.Rule;
import com.produban.api.manager.RulesManager;
import com.produban.data.dao.RulesDAO;

/**
 * @author ortizg1
 * 
 */
public class RulesManagerImpl implements RulesManager {
	RulesDAO rulesDAO;

	@Override
	public List<Rule> getRules() {
		return rulesDAO.getRules();
	}

}
