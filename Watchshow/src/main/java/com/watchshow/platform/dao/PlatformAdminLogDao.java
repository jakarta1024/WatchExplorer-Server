package com.watchshow.platform.dao;

import org.hibernate.SessionFactory;

import com.watchshow.common.dao.BaseHibernateDao;
import com.watchshow.platform.domain.PlatformAdminLog;

public class PlatformAdminLogDao extends BaseHibernateDao<PlatformAdminLog, Long> {

	public PlatformAdminLogDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public PlatformAdminLogDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	

}
