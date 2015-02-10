package com.produban.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.produban.api.data.Configuration;
import com.produban.api.data.Rule;

public class RulesDAOImpl implements RulesDAO{
	private static final String MESSAGE = "message";
	private static final String SLIDE_WINDOW= "slideWindow";
	private static final String WINDOW = "window";
	private static final String NUMBER_TIMES = "numberTimes";
	private static final String REGEX = "regex";
	private final static String QUERY_LIST_RULES = "SELECT * FROM rules";
	
	private DataSource dataSource;	
	
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public List<Rule> getRules() {
		Connection conn = null; 
		Rule rule = null;
		List<Rule> rules = new ArrayList<>();
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(QUERY_LIST_RULES);						
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {	
				String regex = rs.getString(REGEX);
				int numberTimes = rs.getInt(NUMBER_TIMES);
				int totalTime = rs.getInt(WINDOW);
				int windowTime = rs.getInt(SLIDE_WINDOW);
				String message = rs.getString(MESSAGE);
				rule = new Rule(regex, numberTimes, totalTime, windowTime, message);
				rules.add(rule);
			}
			rs.close();
			ps.close();
			return rules;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
				conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
