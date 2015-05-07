package com.produban.sparkstreaming

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
import org.apache.spark.streaming.kafka.KafkaUtils
import com.produban.akamai.api.AlertElastic
import java.util.UUID
import com.produban.api.data.Rule

class AkamaiKafkaStreaming(indexer: Indexer) extends Serializable {

  def executeRules(configuration: Properties) {

    val sparkPort = configuration.getProperty(K.SYSTEM.PROPERTY_SPARKSTREAMING_PORT)
    val sparkMode = configuration.getProperty(K.SYSTEM.PROPERTY_SPARKSTREAMING_MODE)
    val sparkCheckPoint = configuration.getProperty(K.SYSTEM.PROPERTY_SPARKSTREAMING_CHECKPOINT)
    val zookeeperNodes = configuration.getProperty(K.KAFKA.PROPERTY_ZOOKEEPER_NODES)
    val topicsKafka = Map("flume-topic" -> 1)

    //If we want to configure mode and name with code, bad idea... better pass like configuration
    //val sparkConf = new SparkConf().setMaster(sparkMode).setAppName("MiApp");
    val sparkConf = new SparkConf().setMaster(sparkMode);
    val ssc = new StreamingContext(sparkConf, Seconds(5));
    val lines = KafkaUtils.createStream(ssc, zookeeperNodes, "myGroup", topicsKafka)

    var rule = new Rule()
    rule.setMessage("Alert SQL Injection")
    rule.setNumberTimes(3)
    rule.setRegex("SQL")
    rule.setWindow(15)
    rule.setSlideWindow(5)
    val rules = Array(rule)
    
    executeRules(lines, rules)

    // ssc.checkpoint(sparkCheckPoint)
    ssc.start()
    ssc.awaitTermination()

  }

  def executeRules(lines: ReceiverInputDStream[(String, String)], rules: Array[Rule]) {
    for (rule <- rules) {
      val filterSql = lines.map(line => line._2).
        filter(line => line.contains(rule.getRegex())).
        map {
          line =>
            JsonUtil.read(line.getBytes(), classOf[Akamai])
        }

      val groupSql = filterSql.map {
        json =>
          val srcIp = json.getMessage().getCliIP()
          val srcURL = json.getMessage().getReqHost()
          (srcIp + "_" + srcURL, json)
      }

      val errorLinesValueReduce = groupSql.groupByKeyAndWindow(Seconds(rule.getWindow()), Seconds(rule.getSlideWindow()));
      errorLinesValueReduce.foreachRDD {
        rdd =>
          val elem1 = rdd.take(1)

          if (elem1.size > 0) {

            val alertsAkamai = elem1(0)._2
            if (alertsAkamai.size > rule.getNumberTimes()) {
              //Todos los logs en un array de elementos dentro de AlertS   
              val jsonsToIndex = createAlert(rule.getMessage(), alertsAkamai.toList)
              indexer.indexJsons(jsonsToIndex)
            }
          }
      }
    }
  }

  private def createAlert(message: String, jsonsDetected: List[Akamai]): Array[String] = {
    val idUUID = UUID.randomUUID().toString()
    var alertJsons: Array[String] = new Array[String](jsonsDetected.size)
    for (index <- 0 until jsonsDetected.size) {
      val alert: AlertElastic = new AlertElastic(idUUID, message, jsonsDetected(index))
      alertJsons(index) = JsonUtil.write(alert)
    }

    return alertJsons
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