package application.dao;

import org.hibernate.SessionFactory;

import application.model.VisitingHistory;
import commons.dao.BaseHibernateDao;

public class VisitingHistoryDao extends BaseHibernateDao<VisitingHistory, Long> {

	public VisitingHistoryDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public VisitingHistoryDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

}
