package com.watchshow.platform.dao;

import org.hibernate.SessionFactory;

import com.watchshow.common.dao.BaseHibernateDao;
import com.watchshow.platform.domain.UserHistory;

public class UserHistoryDao extends BaseHibernateDao<UserHistory, Long> {

	public UserHistoryDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public UserHistoryDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	

}
