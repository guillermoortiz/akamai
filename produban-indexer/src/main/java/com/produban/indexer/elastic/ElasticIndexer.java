package com.produban.indexer.elastic;

import java.io.Serializable;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.produban.indexer.api.Indexer;

/**
 * Class to do operations with ElasticSearch.
 */
public class ElasticIndexer implements Indexer, Serializable {

	String indexName;
	String docType;
	String clusterName;
	String nodesElastic;

	private static final Logger LOG = LoggerFactory
			.getLogger(ElasticIndexer.class);

	/**
	 * Constructor
	 * 
	 * @param clusterName
	 *            name of the ES cluster.
	 * @param indexName
	 *            index where we are executing
	 * @param docType
	 * @param elasticSearchNodes
	 *            nodes of elastic search
	 */
	public ElasticIndexer(String clusterName, String indexName, String docType,
			String elasticSearchNodes) {
		this.indexName = indexName;
		this.docType = docType;
		this.clusterName = clusterName;
		this.nodesElastic = elasticSearchNodes;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.produban.elastic.Indexer#indexJson(java.lang.String)
	 */
	@Override
	public void indexJson(final String jsonToIndex) {

		Client client = getClient();
		IndexResponse response = client.prepareIndex(indexName, docType)
				.setSource(jsonToIndex).execute().actionGet();

		if (LOG.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("JSON:" + jsonToIndex);
			sb.append(" Index:");
			sb.append(response.getIndex());
			sb.append(" Id:");
			sb.append(response.getId());
			sb.append(" Type:");
			sb.append(response.getType());
			sb.append(" Index:");
			LOG.debug(sb.toString());
		}

		LOG.info("Document indexed");
		cleanup(client);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.produban.elastic.Indexer#indexJsons(java.lang.String[])
	 */
	@Override
	public void indexJsons(final String[] jsonsToIndex) {
		try {
			Client client = getClient();
			BulkRequestBuilder bulkRequest = client.prepareBulk();

			for (String json : jsonsToIndex) {
				bulkRequest.add(client.prepareIndex(indexName, docType)
						.setSource(json));
			}

			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if (bulkResponse.hasFailures()) {
				LOG.error("Error indexing JSON to ElasticSearch "
						+ bulkResponse.buildFailureMessage());
			}

			LOG.info("Document indexed");
			cleanup(client);
		} catch (Exception e) {
			LOG.info("Error " + e);
			e.printStackTrace();
		}
	}

	private void cleanup(Client client) {
		client.close();
	}

	private Client getClient() {

		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", clusterName).build();
		TransportClient transportClient = new TransportClient(settings);
		LOG.info("cluster:" + clusterName);

		for (String esNode : nodesElastic.split(",")) {
			String[] hostAndPort = esNode.split(":");

			LOG.info("Maq:" + hostAndPort[0] + ":" + hostAndPort[1]);

			transportClient.addTransportAddress(new InetSocketTransportAddress(
					hostAndPort[0], Integer.parseInt(hostAndPort[1])));
		}
		return transportClient;
	}

}
