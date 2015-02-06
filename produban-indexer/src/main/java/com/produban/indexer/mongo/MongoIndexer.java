package com.produban.indexer.mongo;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import com.produban.indexer.api.Indexer;
import com.produban.util.JsonUtil;
import com.produban.util.exception.ProdubanException;

/**
 * Class to insert json in 
 * @author ortizg1
 *
 */
public class MongoIndexer implements Indexer, Serializable {
	private static final Logger LOG = Logger.getLogger(MongoIndexer.class);
	String clusterName;
	String mongoDb;
	String collectionMongo;

	public MongoIndexer(String clusterName, String mongoDb,
			String collectionMongo) {
		super();
		this.clusterName = clusterName;
		this.mongoDb = mongoDb;
		this.collectionMongo = collectionMongo;
	}

	@Override
	public void indexJson(String jsonToIndex) {

		DBCollection collection = getClient();
		DBObject dbObject = (DBObject) JSON.parse(jsonToIndex);
		collection.insert(dbObject);

	}

	@Override
	public void indexJsons(String[] jsonsToIndex) {
		DBCollection collection = getClient();
		List<DBObject> objectsToInsert = new ArrayList<DBObject>();
		for (String json : jsonsToIndex) {
			objectsToInsert.add((DBObject) JSON.parse(json));
			
		}
		collection.insert(objectsToInsert);

	}

	private DBCollection getClient() {

		MongoClient mongoClient;
		DB db;
		DBCollection collection;
		try {

			String[] cluster = clusterName.split(":");
			mongoClient = new MongoClient(cluster[0],
					Integer.parseInt(cluster[1]));
			db = mongoClient.getDB(mongoDb);
			collection = db.getCollection(collectionMongo);
			
			if (LOG.isDebugEnabled()){
				LOG.debug("Created connection with MongoDB to " + cluster[0] + ":" + cluster[1] );
			}
			
			
		} catch (UnknownHostException e) {
			throw new ProdubanException("Error connecting with MongoDB", e);
		}
		
		
		return collection;
	}
}
