package com.produban.sparkstreaming

import java.util.Properties
import com.produban.util.FilePropertiesUtil
import org.apache.commons.lang.SystemUtils
import com.produban.api.general.K
import com.produban.indexer.elastic.ElasticIndexer
import scala.collection.JavaConverters._
import com.produban.sparkstreaming._
import com.produban.indexer.mongo.MongoIndexer
import com.produban.indexer.api.Indexer

object AkamaiExecutor {
  def main(args: Array[String]) {

    val configuration = manageInput(args)

    val indexer = configIndexer(configuration)
    val akamai = new AkamaiKafkaStreaming(indexer)
    akamai.executeRules(configuration)

  }

  private def configIndexer(configuration: Properties): Indexer = {
    val typeIndexer = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_TYPE)

    if (typeIndexer.equals(K.INDEXER.TYPE_ELASTIC)) {
      val nodesIndexer = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_NODES)
      val clusterName = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_CLUSTER_NAME)
      val indexName = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_INDEX_NAME)
      return new ElasticIndexer(clusterName, indexName, "alerts", nodesIndexer)

    } else {
      val clusterName = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_MONGO_CLUSTER_NAME)
      val mongoDB = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_MONGO_DB)
      val collection = configuration.getProperty(K.SYSTEM.PROPERTY_INDEXER_MONGO_COLLECTION)
      return new MongoIndexer(clusterName, mongoDB, collection)
    }

  }

  private def manageInput(args: Array[String]): Properties = {
    var configuration: Properties = new Properties()
    if (SystemUtils.IS_OS_WINDOWS) {
      configuration = FilePropertiesUtil.getProperties(K.SYSTEM.CONFIG_FILE_WINDOWS)
    } else {
            if (args(0).equals("VIRT")) {
              configuration = FilePropertiesUtil.getProperties(K.SYSTEM.CONFIG_FILE_LINUX_VIRT)
            } else {
      configuration = FilePropertiesUtil.getProperties(K.SYSTEM.CONFIG_FILE_LINUX_PROD)
      }

    }
    return configuration
  }
}