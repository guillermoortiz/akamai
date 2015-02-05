package com.produban.api.entities;

/**
 * Model bean with a configuration table row.
 */
public class Configuration {

	/**
	 * Identifier of the configuration row.
	 */
	private String key;

	/**
	 * Value of the configuration.
	 */
	private String value;

	/**
	 * @return the identifier of the configuration row.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param pKey
	 *            the identifier of the configuration row to set.
	 */
	public void setKey(final String pKey) {
		key = pKey;
	}

	/**
	 * @return the value of the configuration.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param pValue
	 *            the value of the configuration to set.
	 */
	public void setValue(final String pValue) {
		value = pValue;
	}

	@Override
	public int hashCode() {
		// CHECKSTYLE OFF AvoidInlineConditionals
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
		// CHECKSTYLE ON AvoidInlineConditionals
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Configuration)) {
			return false;
		}
		final Configuration other = (Configuration) obj;
		other.key = other.getKey(); // Javassist not initialize workaround
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}
}
