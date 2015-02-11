package com.produban.api.general;

public interface K {
	interface INDEXER {
		String TYPE_MONGO = "mongo";
		String TYPE_ELASTIC = "elasticsearch";
	}

	interface ELASTIC_SEARCH {
		String PROPERTY_INDEXER_CLUSTER_NAME = "produban.indexer.clusterName";
		String PROPERTY_INDEXER_NODES = "produban.indexer.nodes";
		String PROPERTY_INDEXER_INDEX_NAME = "produban.indexer.indexName";
	}

	interface MONGO {
		String PROPERTY_INDEXER_MONGO_CLUSTER_NAME = "produban.mongo.clusterName";
		String PROPERTY_INDEXER_MONGO_DB = "produban.mongo.db";
		String PROPERTY_INDEXER_MONGO_COLLECTION = "produban.mongo.collectionName";
	}

	interface KAFKA {
		String PROPERTY_ZOOKEEPER_NODES = "produban.zookeeper.nodes";
		String PROPERTY_KAFKA_TOPICS = "produban.kafka.topics";
		String PROPERTY_KAFKA_NODES = "produban.kafka.nodes";

	}

	interface SYSTEM {
		String CONFIG_FILE_WINDOWS = "C:\\configuration\\produban\\produban_config.properties";
		String CONFIG_FILE_LINUX_VIRT = "/user/local/etc/produban_config_VIRT.properties";
		String CONFIG_FILE_LINUX_PROD = "/user/local/etc/conf/produban_config_PROD.properties";

		String PROPERTY_INDEXER_TYPE = "produban.indexer.type";

		String PROPERTY_SPARKSTREAMING_MODE = "produban.sparkstreaming.mode";
		String PROPERTY_SPARKSTREAMING_PORT = "produban.sparkstreaming.port";
		String PROPERTY_SPARKSTREAMING_CHECKPOINT = "produban.sparkstreaming.checkpoint";

	}

	interface SPARK_STREAMING {
		String MASTER = "local[2]";
	}

}
