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

object AkamiaSpark {

  def main(args: Array[String]) {
    val configuration = manageInput(args)
    executeRules(configuration)
  }

  def executeRules(configuration: Properties) {
    val nodesIndexer = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_NODES)
    val clusterName = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_CLUSTER_NAME)
    val indexName = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_INDEX_NAME)
    val sparkPort = configuration.getProperty(K.SYSTEM.PROPERTY_SPARKSTREAMING_PORT)
    val sparkMode = configuration.getProperty(K.SYSTEM.PROPERTY_SPARKSTREAMING_MODE)
    val sparkCheckPoint = configuration.getProperty(K.SYSTEM.PROPERTY_SPARKSTREAMING_CHECKPOINT)

    var indexer = new ElasticIndexer(clusterName, indexName, "logs", nodesIndexer)

    val sparkConf = new SparkConf().setMaster(sparkMode).setAppName("MiApp");
    val ssc = new StreamingContext(sparkConf, Seconds(15));
    val lines = ssc.socketTextStream("localhost", sparkPort.toInt);
    ssc.checkpoint(sparkCheckPoint)

    ruleDoS(lines, indexer);
    ruleSqlInjection(lines, indexer)

    ssc.start()
    ssc.awaitTermination()

  }

  def ruleSqlInjection(lines: ReceiverInputDStream[String], indexer: Indexer) = {
    val filterSql = lines.filter(line => line.contains("SQL"))
    val jsonSql = filterSql.map(line => JsonUtil.read(line.getBytes(), classOf[Akamai]))
    val groupSql = jsonSql.map {
      json =>
        val srcIp = json.getMessage().getCliIP()
        val srcURL = json.getMessage().getReqHost()
        (srcIp + "_" + srcURL, json)
    }
    val errorLinesValueReduce = groupSql.groupByKeyAndWindow(Seconds(60), Seconds(15))
    errorLinesValueReduce.foreachRDD {
      rdd =>
        val elem1 = rdd.take(1)
        if (elem1.size > 0) {
          val alertsAkamai = elem1(0)._2
          if (alertsAkamai.size > 2) {
            createAlert("Alert SQL Injection", alertsAkamai.toList, indexer)
          }
        }
    }
  }

  def ruleDoS(lines: ReceiverInputDStream[String], indexer: Indexer) = {
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
            createAlert("Alert DoS", alertsAkamai.toList, indexer)
          }
        }
    }
  }

  private def createAlert(message: String, jsonsDetected: List[Akamai], indexer: Indexer) = {
    val alert: Alert = new Alert("Alert SQL Injection", jsonsDetected.toArray)
    val jsonToElastic = JsonUtil.write(alert)
    println(jsonToElastic)
    indexer.indexJson(jsonToElastic)
    println("indexado")
  }

  private def manageInput(args: Array[String]): Properties = {
    var configuration: Properties = new Properties()
    //    if (args.length < 1) {
    //      println("Error parameter missing, we need to indicate LOCAL||VIRT||PROD")
    //      exit(-1)
    //    } else {
    if (SystemUtils.IS_OS_WINDOWS) {
      configuration = FilePropertiesUtil.getProperties(K.SYSTEM.CONFIG_FILE_WINDOWS)
    } else {
      if (args(0).equals("VIRT")) {
        configuration = FilePropertiesUtil.getProperties(K.SYSTEM.CONFIG_FILE_LINUX_VIRT)
      } else {
        configuration = FilePropertiesUtil.getProperties(K.SYSTEM.CONFIG_FILE_LINUX_PROD)
      }

    }
    //    }
    return configuration
  }

}