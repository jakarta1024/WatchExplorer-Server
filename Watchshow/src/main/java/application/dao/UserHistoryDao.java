package application.dao;

import org.hibernate.SessionFactory;

import application.model.UserHistory;
import commons.dao.BaseHibernateDao;

public class UserHistoryDao extends BaseHibernateDao<UserHistory, Long> {

	public UserHistoryDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public UserHistoryDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	

}
