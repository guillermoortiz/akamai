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

class AkamaiStreaming2(indexer: Indexer) extends Serializable {

  def executeRules(configuration: Properties) {

    val sparkPort = configuration.getProperty(K.SYSTEM.PROPERTY_SPARKSTREAMING_PORT)
    val sparkMode = configuration.getProperty(K.SYSTEM.PROPERTY_SPARKSTREAMING_MODE)
    val sparkCheckPoint = configuration.getProperty(K.SYSTEM.PROPERTY_SPARKSTREAMING_CHECKPOINT)

    val sparkConf = new SparkConf().setMaster(sparkMode).setAppName("MiApp");
    //val ssc = new StreamingContext(sparkConf, Seconds(15));
    val ssc = new StreamingContext(sparkConf, Seconds(5));
    val lines = ssc.socketTextStream("localhost", sparkPort.toInt);
    // ssc.checkpoint(sparkCheckPoint)
    //ruleDoS(lines);
    ruleSqlInjection(lines)

    ssc.start()
    ssc.awaitTermination()

  }

  def ruleSqlInjection(lines: ReceiverInputDStream[String]) = {
    println("1");
    val filterSql = lines.filter(line => line.contains("SQL"))
    val jsonSql = filterSql.map(line => JsonUtil.read(line.getBytes(), classOf[Akamai]))
    val groupSql = jsonSql.map {
      json =>
        val srcIp = json.getMessage().getCliIP()
        val srcURL = json.getMessage().getReqHost()
        (srcIp + "_" + srcURL, json)
    }
    println("2");

    //val errorLinesValueReduce = groupSql.groupByKeyAndWindow(Seconds(60), Seconds(15))
    val errorLinesValueReduce = groupSql.groupByKeyAndWindow(Seconds(15), Seconds(5));
    println("3");
    errorLinesValueReduce.foreachRDD {
      rdd =>
        rdd.foreach { elem1 =>

          println("4 " + elem1._2.size);
          if (elem1._2.size > 0) {
            println("do something")
          }
        }
    }
    println("fin foreachRdd");

    //    errorLinesValueReduce.foreachRDD {
    //      rdd =>
    //        val elem1 = rdd.take(1)
    //
    //        println("adiossss");
    //        if (elem1.size > 0) {
    //
    //          println("take1 ->" + elem1(0)._1)
    //          println("take2 ->" + elem1(0)._2)
    //
    //          val alertsAkamai = elem1(0)._2
    //          if (alertsAkamai.size > 2) {
    //            indexer.indexJson(createAlert("Alert SQL Injection", alertsAkamai.toList))
    //            println("indexed sql")
    //          }
    //        }
    //    }
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
    //TODO optimizar esto para no abrir cada vez la conexion con Elastic
    errorLinesValueReduce.foreachRDD {
      rdd =>
        val elem1 = rdd.take(1)

        if (elem1.size > 0) {
          val alertsAkamai = elem1(0)._2
          if (alertsAkamai.size > 5) {
            indexer.indexJson(createAlert("Alert DoS", alertsAkamai.toList))
            println("indexed DoS")
          }
        }
    }
  }

  //  private def createAlert(message: String, jsonsDetected: List[Akamai]) = {
  //    val alert: Alert = new Alert("Alert SQL Injection", jsonsDetected.asJava)
  //    val jsonToElastic = JsonUtil.write(alert)
  //    println(jsonToElastic)
  //    indexer.indexJson(jsonToElastic)
  //    println("indexado")
  //  }
  //  

  private def createAlert(message: String, jsonsDetected: List[Akamai]): String = {
    val alert: Alert = new Alert("Alert SQL Injection", jsonsDetected.toArray)
    val jsonToElastic = JsonUtil.write(alert)
    return jsonToElastic
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