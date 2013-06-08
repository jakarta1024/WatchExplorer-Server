package commons.util;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.apache.commons.logging.*;

@SuppressWarnings("deprecation")
public class HibernateUtil {
	private static Log log = LogFactory.getLog(HibernateUtil.class);
	private static Configuration configuration;
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	//addition for local thread safe
	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();

	/**
	 * Apply this static block, application will created a sessionFactory via
	 * default configuration context.
	 */
	static {

		try {
			configuration = new Configuration();
			//Initialize configuration with specified document location string
			configuration.configure("com/watchshow/config/hibernate.cfg.xml");
			//configuration.configure();

			// Setting service registry
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			// 在受管理环境中，如果为SessionFactory配置了JNDI，
			// 那么以下方法会自动把SessionFactory与JNDI绑定
			sessionFactory = configuration.buildSessionFactory();
			
			if(null == sessionFactory) {
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			}
			// sessionFactory = configuration.buildSessionFactory();
			// //Deprecated calling

		} catch (Throwable ex) {
			// 初始化阶段如果出现异常，必须终止整个应用，
			// 否则继续运行应用会导致其他异常
			System.err.println("Session factory initialization failed!");
			
			log.error(ex.getMessage());
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		/*
		 * 在受管理环境中，如果为SessionFactory配置了JNDI 应该通过JNDI来查找SessionFactory对象
		 * SessionFactory sessions = null; 
		 * try { Context ctx = new
		 * InitialContext(); String jndiName =
		 * "java:hibernate/HibernateFactory"; sessions =
		 * (SessionFactory)ctx.lookup(jndiName); }catch (NamingException ex) {
		 * throw new InfrastructureException(ex); }
		 * 
		 * return sessions;
		 */
		return sessionFactory;
	}

	/**
	 * @return Hibernate configuration
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Rebuild session factory, this means application will reload default configuration context to build factory
	 * 
	 */
	public static void rebuildSessionFactory() {
		synchronized (sessionFactory) {
			try {
				Configuration cfg = getConfiguration();
				serviceRegistry = new ServiceRegistryBuilder().applySettings(
						cfg.getProperties()).buildServiceRegistry();
				sessionFactory = cfg.buildSessionFactory(serviceRegistry);
			} catch (RuntimeException ex) {
				log.error(ex.getMessage());
				/*
				throw DatastoreException.datastoreError(ex);
				*/
			}
		}
	}

	/**
	 * Rebuild session factory via given configuration.
	 */
	public static void rebuildSessionFactory(Configuration cfg) {
		synchronized (sessionFactory) {
			try {
				serviceRegistry = new ServiceRegistryBuilder().applySettings(
						cfg.getProperties()).buildServiceRegistry();
				sessionFactory = cfg.buildSessionFactory(serviceRegistry);
				// sessionFactory = cfg.buildSessionFactory();
				configuration = cfg;
			} catch (RuntimeException ex) {
				log.error(ex.getMessage());
				//throw DatastoreException.datastoreError(ex);
			}
		}
	}
	/**
	 * @deprecated 
	 * @return
	 */
	public static Session currentSession2() {
		return sessionFactory.getCurrentSession();
//		try {
//			// 返回当前Session，Session的生命周期与当前线程绑定
//			return sessionFactory.getCurrentSession();
//		} catch (RuntimeException ex) {
//			log.error(ex.getMessage());
//			/*
//			throw DatastoreException.datastoreError(ex);
//			*/
//		}
	}
	/**
	 * Get a working session for current tread, it is thread-safe

	 * @see currentSession()
	 * @return Current thread session.
	 * @throws HibernateException
	 */
	public static Session currentSession() throws HibernateException {
		System.out.println("******--------------------->> wants to get session");
		Session s = session.get();
		if (s == null || s.isOpen() == false) {
			s = sessionFactory.openSession();
			session.set(s);
		}
		System.out.println("******-------------------->> got session:id="+s.toString());
		return s;
	}
	
	public static void closeSession() throws HibernateException {
		Session s = session.get();
		session.set(null);
		if (s != null) {
			s.close();
		}
	}

	public static void closeSessionFactory() {
		try {
			sessionFactory.close();
		} catch (RuntimeException ex) {
			log.error(ex.getMessage());
		}
	}

	public static void rollbackTransaction(Transaction transaction) {
		try {
			if (transaction != null)
				transaction.rollback();
		} catch (RuntimeException ex) {
			log.error(ex.getMessage());
			/*
			throw DatastoreException.datastoreError(ex);
			*/
		}
	}
}
