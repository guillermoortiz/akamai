package com.produban

import org.json4s._
import org.json4s.jackson.JsonMethods._
import com.produban.util.JsonUtil
import scala.collection.JavaConverters._
import com.produban.indexer.elastic.ElasticIndexer
import com.produban.api.general.K
import com.produban.akamai.entity.Akamai
import org.apache.commons.lang3.StringEscapeUtils

object Parseador {
  def main(args: Array[String]) {
    val json = "{" +
      "   \"type\":\"cloud_monitor\"," +
      "   \"format\":\"default\"," +
      "   \"version\":\"1.0\"," +
      "   \"id\":\"df652717542876ddca1e899-a\"," +
      "   \"start\":\"1411938013.555\"," +
      "   \"message\":{" +
      "      \"proto\":\"http\"," +
      "      \"protoVer\":\"1.1\"," +
      "      \"status\":\"200\",    " +
      "      \"fwdHost\":\"www.santanderpb.es\"" +
      "   }," +
      "   \"reqHdr\":{" +
      "      \"accEnc\":\"gzip\"," +
      "      \"conn\":\"Keep-Alive\" " +
      "   }" +
      "}"

    val json2 = "{" + "\"format\":\"default\"," + "\"version\":\"1.0\"," +
      "\"message\":{" + "\"proto\":\"http\"" + "}" + "}";

    val json3 = "{\"message\":{\"cliIP\":\"62.42.159.5\",\"reqHost\":\"www.santanderpb.es\",\"fwdHost\":\"www.santanderpb.es\"},\"waf\":{\"warnSlrs\":\"BURST\"}}";
    println(json3)
    val aa = StringEscapeUtils.escapeJson(json3)
    println(aa)
    val akamai = JsonUtil.read(json3.getBytes(), classOf[Akamai])

    //    val nodesElastic = List(K.ELASTIC_SEARCH.NODES)
    //    val indexer: ElasticIndexer = new ElasticIndexer(K.ELASTIC_SEARCH.CLUSTER_NAME, "akamai", "logs", "180.5.63.119:9300")
    //    indexer.indexJson(json3)
    //    val akamai = JsonUtil.read(json3.getBytes(), classOf[Akamai])
    //    print(akamai)
  }

}