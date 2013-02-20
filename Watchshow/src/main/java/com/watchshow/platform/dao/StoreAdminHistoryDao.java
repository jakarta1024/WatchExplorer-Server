package com.watchshow.platform.dao;

import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.SessionFactory;

import com.watchshow.common.dao.BaseHibernateDao;
import com.watchshow.platform.domain.Publication;
import com.watchshow.platform.domain.StoreAdminHistory;
import com.watchshow.platform.domain.Watch;

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
