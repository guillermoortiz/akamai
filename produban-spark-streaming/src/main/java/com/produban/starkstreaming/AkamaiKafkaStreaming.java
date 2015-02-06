package com.produban.starkstreaming;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.serializer.JavaDeserializationStream;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.stringtemplate.v4.compiler.CodeGenerator.conditional_return;

import scala.Tuple2;
import scala.concurrent.duration.Duration;

import com.produban.akamai.entity.Akamai;
import com.produban.api.data.Rule;
import com.produban.api.general.K;
import com.produban.api.manager.ConfigurationManager;
import com.produban.api.manager.RulesManager;



public class AkamaiKafkaStreaming {
	Map<String, String> configurations;
	List<Rule> rules;

	
	private void config(){
		
	}
	
	public void executeRules(final JavaPairReceiverInputDStream<String,String> pair){
		
		for (Rule rule : rules){
			pair.map(new Function<Tuple2<String,String>, JavaDStream<Akamai>>() {

				@Override
				public JavaDStream<Akamai> call(Tuple2<String, String> v1)
						throws Exception {
					// TODO Auto-generated method stub
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

	public String getZookeeperNodes(){
		return configurations.get(K.SYSTEM.PROPERTY_ZOOKEEPER_NODES);
	}
	
	public Map<String, Integer> getKafkaTopics(){
		return new HashMap<String, Integer>();
	}
	

	public static void main(String[] args) {
		AkamaiKafkaStreaming akamai = new AkamaiKafkaStreaming();
		
		SparkConf conf = new SparkConf().setAppName("");
		JavaStreamingContext ssc = null;
		JavaPairReceiverInputDStream<String, String> pair = KafkaUtils.createStream(ssc, akamai.getZookeeperNodes(), "", akamai.getKafkaTopics());
		akamai.executeRules(pair);
		
		
	}
}	

