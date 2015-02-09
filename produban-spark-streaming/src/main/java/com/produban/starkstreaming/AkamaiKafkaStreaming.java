package com.produban.starkstreaming;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.serializer.JavaDeserializationStream;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.stringtemplate.v4.compiler.CodeGenerator.conditional_return;

import scala.Tuple2;

import com.produban.akamai.entity.Akamai;
import com.produban.api.data.Rule;
import com.produban.api.general.K;
import com.produban.api.manager.ConfigurationManager;
import com.produban.api.manager.RulesManager;
import com.produban.util.JsonUtil;

public class AkamaiKafkaStreaming {
	Map<String, String> configurations;
	List<Rule> rules;

	private void config() {

	}

	public void executeRules(
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
							Tuple2 tuple = new Tuple2<String, Akamai>(srcIp
									+ "_" + srcURL, jsonAkamai);
							return tuple;
						}
					});
			org.apache.spark.streaming.Duration window = new org.apache.spark.streaming.Duration(
					rule.getTotalTime());
			org.apache.spark.streaming.Duration slideDuration = new org.apache.spark.streaming.Duration(
					rule.getWindowTime());
			JavaPairDStream<String, Iterable<Akamai>> tupleStreaming = tuplesAkamai
					.groupByKeyAndWindow(window, slideDuration);

			tuplesAkamai.foreachRDD(new Function<JavaPairRDD<String,Akamai>, Void>() {

				@Override
				public Void call(JavaPairRDD<String, Akamai> v1)
						throws Exception {
					// TODO Auto-generated method stub
					return null;
				}
			});
			
			// val errorLinesValueReduce =
			// groupSql.groupByKeyAndWindow(Seconds(rule.getTotalTime()),
			// Seconds(rule.getWindowTime()));
			// errorLinesValueReduce.foreachRDD {
			// rdd =>
			// val elem1 = rdd.take(1)
			//
			// if (elem1.size > 0) {
			//
			// val alertsAkamai = elem1(0)._2
			// if (alertsAkamai.size > rule.getNumberTimes()) {
			// //Todos los logs en un array de elementos dentro de AlertS
			// val jsonsToIndex = createAlert(rule.getMessage(),
			// alertsAkamai.toList)
			// indexer.indexJsons(jsonsToIndex)
			// }
			// }
			// }
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
		return new HashMap<String, Integer>();
	}

	public static void main(String[] args) {
		AkamaiKafkaStreaming akamai = new AkamaiKafkaStreaming();

		SparkConf conf = new SparkConf().setAppName("");
		JavaStreamingContext ssc = null;
		JavaPairReceiverInputDStream<String, String> pair = KafkaUtils
				.createStream(ssc, akamai.getZookeeperNodes(), "",
						akamai.getKafkaTopics());
		akamai.executeRules(pair);

	}
}
