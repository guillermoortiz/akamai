package com.produban.indexer.elastic.factory;

import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticSearchFactory {

	/**
	 * 
	 * Get an Client to connect with the ElasticSearch cluster.
	 * 
	 * @param clusterName
	 *            name of the cluster
	 * @param nodesElastic
	 *            nodes in the elastic cluster
	 *            host1:port1,host2:port2,host3:port3,...
	 * @return instance for connect to the elastic cluster.
	 */
	public static Client getClient(final String clusterName,
			final List<String> nodesElastic) {

		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", clusterName).build();
		TransportClient transportClient = new TransportClient(settings);

		for (String esNode : nodesElastic) {
			String[] hostAndPort = esNode.split(":");
			transportClient.addTransportAddress(new InetSocketTransportAddress(
					hostAndPort[0], Integer.parseInt(hostAndPort[1])));
		}
		return transportClient;
	}
}
