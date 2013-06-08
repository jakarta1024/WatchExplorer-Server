package application.dao;

import org.hibernate.SessionFactory;

import application.model.PlatformAdminLog;
import commons.dao.BaseHibernateDao;

public class PlatformAdminLogDao extends BaseHibernateDao<PlatformAdminLog, Long> {

	public PlatformAdminLogDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public PlatformAdminLogDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	

}
