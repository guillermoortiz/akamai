package com.produban.indexer.api;

/**
 * 
 * Interface that any class has to have to index documents.
 * 
 */
public interface Indexer {

	/**
	 * Method to index an JSON to a indexer.
	 * 
	 * @param jsonToIndex
	 *            json to index.
	 */
	public abstract void indexJson(String jsonToIndex);

	/**
	 * Method to index a set of JSON in indexer.
	 * 
	 * @param jsonsToIndex
	 *            list of JSONs
	 */
	public abstract void indexJsons(String[] jsonsToIndex);

}