package application.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import application.model.StoreAdministrator;
import application.model.WatchStore;
import commons.dao.BaseHibernateDao;
import commons.util.Assert;

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
