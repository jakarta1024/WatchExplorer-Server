package application.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import application.model.WatchBrand;
import commons.dao.BaseHibernateDao;

public class WatchBrandDao extends BaseHibernateDao<WatchBrand, Long> {
	
	public WatchBrandDao() {
		super();
	}

	@SuppressWarnings("deprecation")
	public WatchBrandDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	//TODO: implement these 2 functions.
	public List<WatchBrand> getBrandsByName(String bname, boolean isEnglish) {
		Criteria c = currentSession().createCriteria(WatchBrand.class);
		if (isEnglish) {
			c.add(Restrictions.eq("engName", bname));
		} else {
			c.add(Restrictions.eq("chnName", bname));
		}
		List<WatchBrand> brands = list(c);
		return brands;
	} 
	
}
