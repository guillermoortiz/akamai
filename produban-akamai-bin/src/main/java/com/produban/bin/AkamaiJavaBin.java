package com.produban.bin;

import java.util.ArrayList;
import java.util.HashMap;

import com.produban.api.data.Rule;
import com.produban.api.general.Factory;
import com.produban.api.manager.ConfigurationManager;
import com.produban.api.manager.RulesManager;
import com.produban.starkstreaming.AkamaiKafkaStreaming;

public class AkamaiJavaBin {
	public static void main(String[] args) {
		ConfigurationManager configurationManager = Factory
				.getConfigurationManager();
		RulesManager rulesManager = Factory.getRulesManager();
		HashMap<String, String> configurations = (HashMap<String, String>) configurationManager
				.getMapConfigurations();
		ArrayList<Rule> rules = (ArrayList<Rule>) rulesManager.getRules();

		System.out.println("Starting Streaming..");
		AkamaiKafkaStreaming akamaiStreaming = new AkamaiKafkaStreaming(
				configurations, rules);
		akamaiStreaming.startStreaming();
		System.out.println("Ending Streaming..");
	}
}
