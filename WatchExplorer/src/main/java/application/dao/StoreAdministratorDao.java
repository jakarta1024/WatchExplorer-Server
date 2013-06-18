package application.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import application.model.StoreAdministrator;
import commons.dao.BaseHibernateDao;

public class StoreAdministratorDao extends BaseHibernateDao<StoreAdministrator, Long> {

	@SuppressWarnings("deprecation")
	public StoreAdministratorDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public StoreAdministratorDao() {
		super();
	}
	public StoreAdministrator getStoreAdminByName(String adminname, Long storeId) {
		StoreAdministrator admin = null;
		Session sess = currentSession();
		try {
			Criteria criteria = sess.createCriteria(StoreAdministrator.class);
			criteria.add(Restrictions.eq("loginName", adminname))
			 		.createCriteria("store")
			 		.add(Restrictions.eq("identifier", storeId));
			admin = unique(criteria);
		} catch (Exception ex) {
			
		} finally {
			if (sess != null) {
				sess.close();
			}
		}
		return admin;
	}
	public StoreAdministrator getStoreAdminByName(String adminname) {
		StoreAdministrator admin = null;
		Session sess = currentSession();
		try {
			Criteria c = sess.createCriteria(StoreAdministrator.class);
			c.add(Restrictions.eq("loginName", adminname));
			admin = unique(c);
		} catch (Exception ex) {
			
		} finally {
			if (sess != null) {
				sess.close();
			}
		}
		return admin;
	}
	
	public StoreAdministrator getStoreAdminByEmail(String adminEmail) {
		StoreAdministrator admin = null;
		Session sess = currentSession();
		try {
			Criteria c = sess.createCriteria(StoreAdministrator.class);
			c.add(Restrictions.eq("verifyEmail", adminEmail));
			admin = unique(c);
		} catch (Exception ex) {
			
		} finally {
			if (sess != null) {
				sess.close();
			}
		}
		return admin;
	}
	
	public StoreAdministrator getWatchCommiter(Long watchId) {
		StoreAdministrator admin = null;
		
		return admin;
	}

}
