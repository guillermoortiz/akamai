package com.produban

import scala.collection.JavaConverters._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import akka.dispatch.Foreach
import org.apache.spark.streaming.StreamingContext
import StreamingContext._
import org.apache.spark.streaming.Seconds
import org.apache.spark.rdd.PairRDDFunctions
import com.produban.util.JsonUtil
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.commons.collections.IteratorUtils
import com.produban.indexer.elastic.ElasticIndexer
import com.produban.akamai.api.Alert
import com.produban.akamai.entity.Akamai
import com.produban.api.general.K
import com.produban.util.FilePropertiesUtil
import org.apache.commons.lang.SystemUtils
import java.util.Properties
import com.produban.indexer.api.Indexer

object AkamaiRules {
  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("AkamaiRules");
    val ssc = new StreamingContext(sparkConf, Seconds(15));
    val lines = ssc.socketTextStream("localhost", 15000);
    ssc.checkpoint("c:\\Users\\x100516")
    ruleDoS(lines);
    //    val filterDoS = lines.filter(line => line.contains("BURST") || line.contains("SUMMARY"))
    //    val jsonDoS = filterDoS.map(line => JsonUtil.read(line.getBytes(), classOf[AkamaiJson]))
    //    val groupDoS = jsonDoS.map {
    //      json =>
    //        val srcIp = json.getMessage().getCliIP()
    //        val srcURL = json.getMessage().getReqHost()
    //        println(srcIp + "_" + srcURL)
    //        (srcIp + "_" + srcURL, json)
    //    }
    //    val errorLinesValueReduce = groupDoS.groupByKeyAndWindow(Seconds(8), Seconds(4))
    //    errorLinesValueReduce.foreachRDD {
    //      rdd =>
    //        val elem1 = rdd.take(1)
    //
    //        if (elem1.size > 0) {
    //          val alertsAkamai = elem1(0)._2
    //          if (alertsAkamai.size > 2) {
    //            println("indexar con ES " + alertsAkamai)
    //            println(errorLinesValueReduce)
    //          }
    //        }
    //    }

    ssc.start()
    ssc.awaitTermination()
    println("fin");
  }

  def ruleDoS(lines: ReceiverInputDStream[String]) = {
    val filterDoS = lines.filter(line => line.contains("BURST") || line.contains("SUMMARY"))
    val jsonDoS = filterDoS.map(line => JsonUtil.read(line.getBytes(), classOf[Akamai]))
    val groupDoS = jsonDoS.map {
      json =>
        val srcIp = json.getMessage().getCliIP()
        val srcURL = json.getMessage().getReqHost()
        (srcIp + "_" + srcURL, json)
    }
    val errorLinesValueReduce = groupDoS.groupByKeyAndWindow(Seconds(180), Seconds(15))
    errorLinesValueReduce.foreachRDD {
      rdd =>
        val elem1 = rdd.take(1)

        if (elem1.size > 0) {
          val alertsAkamai = elem1(0)._2
          if (alertsAkamai.size > 5) {
            val indexer: ElasticIndexer = new ElasticIndexer("elasticsearch", "akamai", "logs", "180.5.63.119:9300")

            val alert: Alert = new Alert("Alert DoS", alertsAkamai.toArray)
            val jsonToElastic = JsonUtil.write(alert)
            println(alertsAkamai.size + " indexar DOS **** " + alertsAkamai)
            println("Alert DOS-->" + jsonToElastic)
            println(errorLinesValueReduce)

            indexer.indexJson(jsonToElastic)
          }
        }
    }
  }

}