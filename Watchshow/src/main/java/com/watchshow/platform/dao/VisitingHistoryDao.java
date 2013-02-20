package com.watchshow.platform.dao;

import org.hibernate.SessionFactory;

import com.watchshow.common.dao.BaseHibernateDao;
import com.watchshow.platform.domain.VisitingHistory;

public class VisitingHistoryDao extends BaseHibernateDao<VisitingHistory, Long> {

	public VisitingHistoryDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public VisitingHistoryDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

}
