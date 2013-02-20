package com.watchshow.platform.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.watchshow.common.dao.BaseHibernateDao;
import com.watchshow.common.util.HibernateUtil;
import com.watchshow.platform.domain.User;
import com.watchshow.platform.domain.UserHistory;

public class UserDao extends BaseHibernateDao<User, Long> {
	public UserDao() {
		super();
	}
	private Log logger = LogFactory.getLog(BaseHibernateDao.class);
	@SuppressWarnings("deprecation")
	public UserDao(SessionFactory sessionFactory) {
		super(sessionFactory);
		if (logger == null) {
			logger = LogFactory.getLog(this.getClass());
		}
		
	}
	public User getUserByName(String username) {
		User user = null;
		Criteria criteria = HibernateUtil.currentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("userName", username));
		user = unique(criteria);
		return user;
	} 
	public User getUserByEmail(String email) {
		User user = null;
		Criteria criteria = HibernateUtil.currentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("verifyEmail", email));
		user = unique(criteria);
		return user;
	}
	/**
	 * This method is not safe, because users on this device are not unique.
	 * So, to use this method, we assume one user use this device only. 
	 * @param deviceSN
	 * @return
	 */
	public User getUserByDeviceSerialsNumber(String deviceSN) {
	    System.out.println("passed device sn: " + deviceSN);
		User user = null;
		Criteria criteria = HibernateUtil.currentSession().createCriteria(UserHistory.class);
		criteria.add(Restrictions.eq("deviceSN", deviceSN));
		criteria.addOrder(Order.desc("timestamp"));
		List<UserHistory> userHistories = list(criteria);
		if (!userHistories.isEmpty()) {
			user = userHistories.get(0).getOwner();
		}
		return user;
	}
	
}
