package com.produban.api.factory;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public final class OpenSessionInView {

	/**
	 * Private constructor to avoid developers instantiate a useless object.
	 */
	private OpenSessionInView() {
	}

	private static SessionFactory sessionFactory;

	/**
	 * Open a hibernate session and put it on the actual execution thread.
	 */
	public static void openSession() {
		SessionFactory sf = getSessionFactory();

		Session session = sf.openSession();
		session.setFlushMode(FlushMode.COMMIT);
		TransactionSynchronizationManager.bindResource(sf, new SessionHolder(
				session));
	}

	/**
	 * Close the hibernate session in the actual execution thread and removes
	 * the session from it.
	 */
	public static void closeSession() {
		SessionFactory sf = getSessionFactory();
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
				.unbindResource(sf);
		SessionFactoryUtils.closeSession(sessionHolder.getSession());
	}

	/**
	 * @param pSessionFactory
	 *            the sessionFactory to set
	 */
	public static void setSessionFactory(final SessionFactory pSessionFactory) {
		sessionFactory = pSessionFactory;
	}

	private static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = Factory.getSessionFactory();
		}
		return sessionFactory;
	}
}
