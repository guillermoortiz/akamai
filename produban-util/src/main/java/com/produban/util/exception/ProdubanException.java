package com.produban.util.exception;

/**
 * Generic exception.
 */
public class ProdubanException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructor class.
     * 
     * @param message
     *            the exception message to show.
     */
    public ProdubanException(final String message) {
        super(message);
    }

    /**
     * Constructor class.
     * 
     * @param message
     *           message to show.
     * @param cause
     *            the instance of Throwable.
     */
    public ProdubanException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
