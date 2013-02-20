package com.watchshow.platform.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.watchshow.common.dao.BaseHibernateDao;
import com.watchshow.common.util.Assert;
import com.watchshow.platform.domain.StoreAdministrator;
import com.watchshow.platform.domain.WatchStore;

public class WatchStoreDao extends BaseHibernateDao<WatchStore, Long> {

	public WatchStoreDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public WatchStoreDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public WatchStore getStoreByName(String name) {
		WatchStore retStore = null;
		Session sess = currentSession();
		List<WatchStore> stores =  list(sess.createCriteria(WatchStore.class).add(Restrictions.eq("name", name)));
		 Assert.isTrue(stores.size() <= 1, "store with given name must be one at most!");
		if (stores.size()==1) {
		    retStore = stores.get(0);
		}
		return retStore;
	}
	
	public StoreAdministrator getFounder(Long storeId) {
		WatchStore store = get(storeId);
		StoreAdministrator founder = null;
		Set<StoreAdministrator> admins = store.getAdmins();
		for (StoreAdministrator admin : admins) {
			if (admin.getRole().equalsIgnoreCase("founder")) {
				founder = admin;
				break;
			}
		}
		return founder;
	}

}
