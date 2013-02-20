package com.watchshow.platform.dao;

import java.sql.Timestamp;

import com.watchshow.common.dao.BaseHibernateDao;
import com.watchshow.platform.domain.PlatformBulletin;

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
