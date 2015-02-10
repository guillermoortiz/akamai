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

import com.produban.akamai.entity.Akamai;
import com.produban.api.data.Rule;
import com.produban.api.general.K;
import com.produban.util.JsonUtil;

public class AkamaiKafkaStreaming implements Serializable {
	HashMap<String, String> configurations;
	ArrayList<Rule> rules;

	public AkamaiKafkaStreaming(HashMap<String, String> configurations,
			ArrayList<Rule> rules) {
		super();
		this.configurations = configurations;
		this.rules = rules;
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
							Tuple2<String, Akamai> tuple = new Tuple2<String, Akamai>(srcIp
									+ "_" + srcURL, jsonAkamai);
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
								List elements =  IteratorUtils.toList(elem.get(0)._2.iterator());														
								if (2 <= elements.size()) {
									System.out.println("XXX ALERTA "
											+ rule.getMessage() + " GENERADA");
								}
							}

							return null;
						}
					});			
		}
	}

	public Map<String, String> getConfigurations() {
		return configurations;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public String getZookeeperNodes() {
		return configurations.get(K.SYSTEM.PROPERTY_ZOOKEEPER_NODES);
	}

	public Map<String, Integer> getKafkaTopics() {
		Map<String, Integer> map = new HashMap<>();
		map.put("flume-topic", 1);
		return map;
	}
}
