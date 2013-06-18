package application.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import application.model.PlatformAdministrator;
import commons.dao.BaseHibernateDao;

public class PlatformAdministratorDao extends  BaseHibernateDao<PlatformAdministrator, Long> {
	public PlatformAdministratorDao() {
		super();
	}
	private Log logger = LogFactory.getLog(BaseHibernateDao.class);
	@SuppressWarnings("deprecation")
	public PlatformAdministratorDao(SessionFactory sessionFactory) {
		super(sessionFactory);
		if (logger == null) {
			logger = LogFactory.getLog(this.getClass());
		}
	}
	public PlatformAdministrator getAdminByName(String name) {
		PlatformAdministrator admin = null;
		Session s = currentSession();
		Criteria c = s.createCriteria(PlatformAdministrator.class).add(Restrictions.eq("loginName", name));
		admin = unique(c);
		return admin;
	}

}
