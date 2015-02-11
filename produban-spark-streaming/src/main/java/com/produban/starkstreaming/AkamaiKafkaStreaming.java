package com.produban.starkstreaming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

import com.produban.akamai.api.Alert;
import com.produban.akamai.entity.Akamai;
import com.produban.api.data.Rule;
import com.produban.api.general.K;
import com.produban.indexer.api.Indexer;
import com.produban.indexer.elastic.ElasticIndexer;
import com.produban.indexer.mongo.MongoIndexer;
import com.produban.util.JsonUtil;

public class AkamaiKafkaStreaming implements Serializable {
	HashMap<String, String> configurations;
	ArrayList<Rule> rules;
	ArrayList<Indexer> indexers;

	public AkamaiKafkaStreaming(HashMap<String, String> configurations,
			ArrayList<Rule> rules) {
		super();
		this.configurations = configurations;
		this.rules = rules;
		initIndexers();
	}
	
	private void initIndexers() {
		indexers = new ArrayList<>();
		String typeIndexer = configurations.get(K.SYSTEM.PROPERTY_INDEXER_TYPE);
		if (typeIndexer.contains("elastic")){
			indexers.add(initElasticSearch());
		}
		
		if (typeIndexer.contains("mongo")){
			indexers.add(initMongo());
		}		
	}

	private Indexer initElasticSearch(){
		Indexer indexer;
		String clusterName = configurations.get(K.ELASTIC_SEARCH.PROPERTY_INDEXER_CLUSTER_NAME);
		String indexName = configurations.get(K.ELASTIC_SEARCH.PROPERTY_INDEXER_INDEX_NAME);
		String elasticNodes = configurations.get(K.ELASTIC_SEARCH.PROPERTY_INDEXER_NODES);
		indexer = new ElasticIndexer(clusterName, indexName, "logs", elasticNodes);
		return indexer;
	}
	
	private Indexer initMongo(){
		Indexer indexer;
		String clusterName = configurations.get(K.MONGO.PROPERTY_INDEXER_MONGO_CLUSTER_NAME);
		String mongoDb = configurations.get(K.MONGO.PROPERTY_INDEXER_MONGO_DB);
		String collectionMongo = configurations.get(K.MONGO.PROPERTY_INDEXER_MONGO_COLLECTION);
		indexer = new MongoIndexer(clusterName, mongoDb, collectionMongo);
		return indexer;
	}

	public AkamaiKafkaStreaming() {
		super();
	}

	public void startStreaming() {
		Duration batchDuration = new Duration(5000);
		SparkConf conf = new SparkConf().setMaster("local[2]")
				.setAppName("App");
		JavaStreamingContext ssc = new JavaStreamingContext(conf, batchDuration);

		JavaPairReceiverInputDStream<String, String> pair = KafkaUtils
				.createStream(ssc, getZookeeperNodes(), "myGroup",
						getKafkaTopics());
		executeRules(pair);

		ssc.start();
		ssc.awaitTermination();
	}

	private void executeRules(
			final JavaPairReceiverInputDStream<String, String> streaming) {

		for (final Rule rule : rules) {
			JavaDStream<String> bodyKafka = streaming
					.map(new Function<Tuple2<String, String>, String>() {
						@Override
						public String call(Tuple2<String, String> v1)
								throws Exception {
							return v1._2;
						}
					});

			// bodyKafka.print();
			JavaDStream<String> filteredLines = bodyKafka
					.filter(new Function<String, Boolean>() {
						@Override
						public Boolean call(String v1) throws Exception {
							return v1.contains(rule.getRegex());
						}
					});

			JavaDStream<Akamai> filteredAkamai = filteredLines
					.map(new Function<String, Akamai>() {
						@Override
						public Akamai call(String v1) throws Exception {
							return JsonUtil.read(v1.getBytes(), Akamai.class);
						}
					});

			// Parse to a list of tuples<String(ip_url), AkamaiObj>
			JavaPairDStream<String, Akamai> tuplesAkamai = filteredAkamai
					.mapToPair(new PairFunction<Akamai, String, Akamai>() {
						@Override
						public Tuple2<String, Akamai> call(Akamai jsonAkamai)
								throws Exception {

							String srcIp = jsonAkamai.getMessage().getCliIP();
							String srcURL = jsonAkamai.getMessage()
									.getReqHost();
							Tuple2<String, Akamai> tuple = new Tuple2<String, Akamai>(
									srcIp + "_" + srcURL, jsonAkamai);
							return tuple;
						}
					});

			Duration window = new Duration(rule.getWindow());
			Duration slideDuration = new Duration(rule.getSlideWindow());
			JavaPairDStream<String, Iterable<Akamai>> tupleStreaming = tuplesAkamai
					.groupByKeyAndWindow(window, slideDuration);

			tupleStreaming
					.foreachRDD(new Function<JavaPairRDD<String, Iterable<Akamai>>, Void>() {
						@Override
						public Void call(
								JavaPairRDD<String, Iterable<Akamai>> rdd)
								throws Exception {

							List<Tuple2<String, Iterable<Akamai>>> elem = rdd
									.take(1);

							if (elem.size() > 0) {
								System.out.println("XXX elem " + elem.size());
								List<Akamai> elements = IteratorUtils.toList(elem
										.get(0)._2.iterator());
								System.out.println("XXX elements " + elements.size());
								if (rule.getNumberTimes() <= elements.size()) {
									System.out.println("XXX insertando.. " + elements.size());
									createAlert(rule.getMessage(),elements, indexers);
								}
							}

							return null;
						}
					});
		}
	}

	private void createAlert(final String message,
			final List<Akamai> jsonsDetected, final List<Indexer> Indexer) {
		Akamai[] akamayArray = new Akamai[jsonsDetected.size()];
		jsonsDetected.toArray(akamayArray);		
		Alert alert = new Alert(message, akamayArray);
		String jsonAlert = JsonUtil.write(alert);
		
		for (Indexer indexer : indexers){
			indexer.indexJson(jsonAlert);
		}
	}


	public Map<String, String> getConfigurations() {
		return configurations;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public String getZookeeperNodes() {
		return configurations.get(K.KAFKA.PROPERTY_ZOOKEEPER_NODES);
	}

	public Map<String, Integer> getKafkaTopics() {
		Map<String, Integer> map = new HashMap<>();
		map.put("flume-topic", 1);
		return map;
	}
}
