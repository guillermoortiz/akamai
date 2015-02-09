package com.produban.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.produban.api.data.Configuration;



public class ConfigurationDAOImpl implements ConfigurationDAO{

	private DataSource dataSource;
	private final static String QUERY_GET_CONFIGURATION = "SELECT * FROM CONFIGURATION WHERE KEY = ?";
	private final static String QUERY_LIST_CONFIGURATION = "SELECT * FROM CONFIGURATION";
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public List<Configuration> getListConfigurations() {
		Connection conn = null; 
		Configuration configuration = null;
		List<Configuration> configurations = new ArrayList<>();
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(QUERY_LIST_CONFIGURATION);						
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {				
				configuration = new Configuration(
					rs.getString("key"),
					rs.getString("value")
				);
				configurations.add(configuration);
			}
			rs.close();
			ps.close();
			return configurations;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
				conn.close();
				} catch (SQLException e) {}
			}
		}
	}

	@Override
	public Configuration getConfiguration(String key) {		
		Connection conn = null; 
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(QUERY_GET_CONFIGURATION);
			ps.setString(1, key);
			Configuration configuration = null;
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				configuration = new Configuration(
					rs.getString("key"),
					rs.getString("value")
				);
			}
			rs.close();
			ps.close();
			return configuration;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
				conn.close();
				} catch (SQLException e) {}
			}
		}
	}

}
