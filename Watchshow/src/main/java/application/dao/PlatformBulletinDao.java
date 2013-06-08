package application.dao;

import java.sql.Timestamp;

import application.model.PlatformBulletin;
import commons.dao.BaseHibernateDao;

public class PlatformBulletinDao extends BaseHibernateDao<PlatformBulletin, Long> {
	private static final long oneDayMillseconds = 1 * 24 * 60 * 60 * 1000;
	public PlatformBulletinDao() {
		super();
	}
	
	public void updateIfInactive(PlatformBulletin bulletin) {
		if (bulletin.getIsActive() == true && checkIfExpired(bulletin)) {
			bulletin.setIsActive(false);
		}
	}
	
	public Boolean checkIfExpired(PlatformBulletin bulletin) {
		Timestamp startTime = bulletin.getPublishTime();
		Timestamp stopTime = new Timestamp(startTime.getTime() + oneDayMillseconds);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (now.after(stopTime) || now.equals(stopTime)) {
			return false;
		} else {
			return true;
		}
	}

}
