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

object SparkStreaminTest {
  def main(args: Array[String]) {
    //	    val sparkConf = new SparkConf().setMaster("local").setAppName("Test")
    //		val sc = new SparkContext(sparkConf)
    //	    
    //	    val inputFile = sc.textFile("c:\\A.txt")
    //	    val lineFilter = inputFile.filter(line => line.contains("hola"))
    //	    val output = lineFilter.collect()
    //	    lineFilter.saveAsTextFile("c:\\B.txt");

    val nodesElastic = List("node1", "node2");
    //val indexer: ElasticIndexer = new ElasticIndexer("elasticCluster", "akamai", "logs", nodesElastic.asJava)

    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("Test");
    // Create a StreamingContext with a 1-second batch size from a SparkConf
    //val sc = new SparkContext(sparkConf)
    //val rdd = sc.textFile("")
    //rdd.map(x => (x,1)).reduceByKey(....)

    val ssc = new StreamingContext(sparkConf, Seconds(4));
    // Create a DStream using data received after connecting to port 7777 on the local machine
    val lines = ssc.socketTextStream("localhost", 15000);
    ssc.checkpoint("c:\\Users\\x100516")

    //Ejemplo1 (clave, [h1,h2,h3,..])    
    //    val errorLines = lines.filter(_.contains("h"))
    //    val errorLinesValue = errorLines.map(line => ("key", line))
    //    val errorLinesValueReduce = errorLinesValue.groupByKeyAndWindow(Seconds(8), Seconds(4))
    //

    //Ejemplo2 como sacar el entero de una RDD
    //    var count: Long = 0
    //    val errorLines = lines.filter(_.contains("h"))
    //    errorLines.foreachRDD {
    //      rdd =>
    //        count += rdd.count();
    //        println(count + "hola");
    //    }

    //Ejemplo3 como sacar la lista y poder hacer cosas con ella.
    val errorLines = lines.filter(_.contains("h"))
    val errorLinesValue = errorLines.map(line => ("key", line))
    val errorLinesValueReduce = errorLinesValue.groupByKeyAndWindow(Seconds(8), Seconds(4))
    errorLinesValueReduce.foreachRDD {
      rdd =>
        val elem1 = rdd.take(1)

        if (elem1.size > 0) {
          val alertsAkamai = elem1(0)._2
          if (alertsAkamai.size > 3) {
            println("indexar con ES " + alertsAkamai)
            println(errorLinesValueReduce)
          }
        }

    }

    //indexer.indexJson(x$1)

    // println(linesWindow.count())
    // Filter our DStream for lines with "error"
    //val errorLines = lines.filter(_.contains("hola"))
    // Print out the lines with errors
    //errorLines.print()

    ssc.start()
    ssc.awaitTermination()
    println("fin");

  }
}