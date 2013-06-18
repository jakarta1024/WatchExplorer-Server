package application.dao;

import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.SessionFactory;

import application.model.Publication;
import application.model.StoreAdminHistory;
import application.model.Watch;
import commons.dao.BaseHibernateDao;

public class StoreAdminHistoryDao extends
		BaseHibernateDao<StoreAdminHistory, Long> {

	public StoreAdminHistoryDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public StoreAdminHistoryDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public StoreAdminHistory getCreationHistoryForWatch(Watch watch) {
		Set<StoreAdminHistory> histories = watch.getAdminHistories();
		Timestamp ts = null;
		StoreAdminHistory returnValue = null;
		for (StoreAdminHistory his : histories) {
			Timestamp ts1 = his.getTimestamp();
			if (returnValue == null || ts == null || ts1.before(ts)) {
				ts = ts1;
				returnValue = his;
			}
		}
		return returnValue;
	}
	
	public StoreAdminHistory getLastEditingHistoryForWatch(Watch watch) {
		Set<StoreAdminHistory> histories = watch.getAdminHistories();
		Timestamp ts = null;
		StoreAdminHistory returnValue = null;
		for (StoreAdminHistory his : histories) {
			Timestamp ts1 = his.getTimestamp();
			if (returnValue == null || ts == null || ts1.after(ts)) {
				ts = ts1;
				returnValue = his;
			}
		}
		return returnValue;
	}
	public StoreAdminHistory getLastEditedHistoryForPublication(Publication publication) {
		StoreAdminHistory returnValue = null;
		Timestamp ts = null;
		Set<StoreAdminHistory> hiss = publication.getHistories();
		for (StoreAdminHistory his : hiss) {
			Timestamp ts1 = his.getTimestamp();
			if (returnValue == null || ts == null || ts1.after(ts)) {
				ts = ts1;
				returnValue = his;
			}
		}
		return returnValue;
	}
}
