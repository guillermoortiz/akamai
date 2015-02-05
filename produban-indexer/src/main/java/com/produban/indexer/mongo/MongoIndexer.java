package com.produban.indexer.mongo;

import java.io.Serializable;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import com.produban.indexer.api.Indexer;
import com.produban.util.exception.ProdubanException;

public class MongoIndexer implements Indexer, Serializable {
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

		for (String json : jsonsToIndex) {
			DBObject dbObject = (DBObject) JSON.parse(json);
			collection.insert(dbObject);
		}

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
		} catch (UnknownHostException e) {
			throw new ProdubanException("Error connecting with MongoDB", e);
		}

		return collection;
	}
}
