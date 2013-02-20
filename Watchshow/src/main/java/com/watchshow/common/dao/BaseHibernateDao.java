package com.watchshow.common.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;


import com.watchshow.common.pagination.PageUtil;
import com.watchshow.common.util.Assert;
import com.watchshow.common.util.HibernateUtil;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.Map.Entry;

/**
 * Basic Hibernate DAO methods here for convenience.
 * @author Kipp Li
 * @version 1.0
 */
public abstract class BaseHibernateDao<M extends java.io.Serializable, PK extends java.io.Serializable> implements IBaseDao<M, PK> {

    protected static final Log LOGGER = LogFactory.getLog(BaseHibernateDao.class);

    private final Class<M> entityClass;
    private final String HQL_LIST_ALL;
    private final String HQL_COUNT_ALL;
    private final String HQL_OPTIMIZE_PRE_LIST_ALL;
    private final String HQL_OPTIMIZE_NEXT_LIST_ALL;
    private String pkName = null;
    private SessionFactory sessionFactory;
    
    @SuppressWarnings("unchecked")
	public BaseHibernateDao() {
    	 //Make sure current sessionFactory is not null
        if (this.sessionFactory == null) {
        	this.sessionFactory = HibernateUtil.getSessionFactory();
        }
        this.entityClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Field[] fields = this.entityClass.getDeclaredFields();
        //In this case, assume we just use annotation way to map a pojo to hibernate object
        for(Field f : fields) {
            if(f.isAnnotationPresent(Id.class)) {
                this.pkName = f.getName();
            }
        }
        // And, in this case, we just use hbm mapping file to implement pojo maps to hibernate object
        if (this.pkName == null) {
        	ClassMetadata meta = sessionFactory.getClassMetadata(this.entityClass);
        	this.pkName = meta.getIdentifierPropertyName();
        }
        Assert.notNull(pkName);
        //TODO @Entity name not null
        HQL_LIST_ALL = "from " + this.entityClass.getSimpleName() + " order by " + pkName + " desc";
        HQL_OPTIMIZE_PRE_LIST_ALL = "from " + this.entityClass.getSimpleName() + " where " + pkName + " > ? order by " + pkName + " asc";
        HQL_OPTIMIZE_NEXT_LIST_ALL = "from " + this.entityClass.getSimpleName() + " where " + pkName + " < ? order by " + pkName + " desc";
        HQL_COUNT_ALL = " select count(*) from " + this.entityClass.getSimpleName();
    }

    /**
     * 
     * @param sessionFactory
     * @deprecated
     */
    @SuppressWarnings("unchecked")
    public BaseHibernateDao(SessionFactory sessionFactory) {
        this.entityClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Field[] fields = this.entityClass.getDeclaredFields();
        for(Field f : fields) {
        	//TODO:
            if(f.isAnnotationPresent(Id.class)) {
                this.pkName = f.getName();
            }
        }
        if (this.pkName == null) {
        	ClassMetadata meta = sessionFactory.getClassMetadata(this.entityClass);
        	this.pkName = meta.getIdentifierPropertyName();
        }
        Assert.notNull(pkName);
        //TODO @Entity name not null
        HQL_LIST_ALL = "from " + this.entityClass.getSimpleName() + " order by " + pkName + " desc";
        HQL_OPTIMIZE_PRE_LIST_ALL = "from " + this.entityClass.getSimpleName() + " where " + pkName + " > ? order by " + pkName + " asc";
        HQL_OPTIMIZE_NEXT_LIST_ALL = "from " + this.entityClass.getSimpleName() + " where " + pkName + " < ? order by " + pkName + " desc";
        HQL_COUNT_ALL = " select count(*) from " + this.entityClass.getSimpleName();
        //Make sure current sessionFactory is not null
        if (this.sessionFactory == null) {
        	Assert.notNull(sessionFactory, "Hibernate sessionFactory must be setup before DAO construction!");
        	setSessionFactory(sessionFactory);
        }
    }
        
    private Session useDefaultCurrentSession(boolean use) {
    	if(use == true) {
    		return this.sessionFactory.getCurrentSession();
    	} else {
    		return HibernateUtil.currentSession();
    	}
    }

    public Session currentSession() {
    	return useDefaultCurrentSession(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PK save(M model) {
    	System.out.println(model.toString()+" : saved");
        return (PK) currentSession().save(model);
    }

    @Override
    public void saveOrUpdate(M model) {
    	System.out.println(model.toString()+" : saved or updated");
    	currentSession().saveOrUpdate(model);
    }

    @Override
    public void update(M model) {
    	currentSession().update(model);

    }

    @Override
    public void merge(M model) {
    	currentSession().merge(model);
    }

    @Override
    public void delete(PK id) {
    	currentSession().delete(this.get(id));

    }

    @Override
    public void deleteObject(M model) {
    	currentSession().delete(model);

    }

    @Override
    public boolean exists(PK id) {
        return get(id) != null;
    }

    @SuppressWarnings("unchecked")
	@Override
    public M get(PK id) {
        return (M) currentSession().get(this.entityClass, id);
    }

    @Override
    public int countAll() {
        Long total = aggregate(HQL_COUNT_ALL);
        return total.intValue();
    }
    
    public int count(String conditionHQL, Object...parameters) {
    	Long total = aggregate(conditionHQL, parameters);
    	return total.intValue();
    }
    
    @Override
    public List<M> listAll() {
        return list(HQL_LIST_ALL,-1,-1);
    }

    @Override
    public List<M> listAll(int pn, int pageSize) {
        return list(HQL_LIST_ALL, pn, pageSize);
    }
    
    @Override
    public List<M> pre(PK pk, int pn, int pageSize) {
        if(pk == null) {
            return list(HQL_LIST_ALL, pn, pageSize);
        }
        //倒序，重排
        List<M> result = list(HQL_OPTIMIZE_PRE_LIST_ALL, 1, pageSize, pk);
        Collections.reverse(result);
        return result;
    }
    @Override
    public List<M> next(PK pk, int pn, int pageSize) {
        if(pk == null) {
            return list(HQL_LIST_ALL, pn, pageSize);
        }
        return list(HQL_OPTIMIZE_NEXT_LIST_ALL, 1, pageSize, pk);
    }

    @Override
    public void flush() {
    	currentSession().flush();
    }

    @Override
    public void clear() {
    	currentSession().clear();
    }

    protected long getIdResult(String hql, Object... paramlist) {
        long result = -1;
        List<?> list = list(hql,-1,-1,paramlist);
        if (list != null && list.size() > 0) {
            return ((Number) list.get(0)).longValue();
        }
        return result;
    }

    protected List<M> listSelf(final String hql, final int pn, final int pageSize, final Object... paramlist) {
        return this.<M> list(hql, pn, pageSize, paramlist);
    }


    /**
     * for in
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> listWithIn(final String hql,final int start, final int length, final Map<String, Collection<?>> map, final Object... paramlist) {
        Query query = currentSession().createQuery(hql);
        setParameters(query, paramlist);
        for (Entry<String, Collection<?>> e : map.entrySet()) {
            query.setParameterList(e.getKey(), e.getValue());
        }
        if (start > -1 && length > -1) {
            query.setMaxResults(length);
            if (start != 0) {
                query.setFirstResult(start);
            }
        }
        List<T> results = query.list();
        return results;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> list(final String hql, final int pn, final int pageSize, final Object... paramlist) {
        Query query = currentSession().createQuery(hql);
        setParameters(query, paramlist);
        if (pn > -1 && pageSize > -1) {
            query.setMaxResults(pageSize);
            int start = PageUtil.getPageStart(pn, pageSize);
            if (start != 0) {
                query.setFirstResult(start);
            }
        }
        if (pn < 0) {
            query.setFirstResult(0);
        }
        List<T> results = query.list();
        return results;
    }

    /**
     * 根据查询条件返回唯一一条记录
     */
    @SuppressWarnings("unchecked")
    protected <T> T unique(final String hql, final Object... paramlist) {
        Query query = currentSession().createQuery(hql);
        setParameters(query, paramlist);
        return (T) query.setMaxResults(1).uniqueResult();
    }

       /**
        * 
        * for in
        */
    @SuppressWarnings("unchecked")
    protected <T> T aggregate(final String hql, final Map<String, Collection<?>> map, final Object... paramlist) {
        Query query = currentSession().createQuery(hql);
        if (paramlist != null) {
            setParameters(query, paramlist);
            for (Entry<String, Collection<?>> e : map.entrySet()) {
                query.setParameterList(e.getKey(), e.getValue());
            }
        }
        return (T) query.uniqueResult();
    }
        
    @SuppressWarnings("unchecked")
    protected <T> T aggregate(final String hql, final Object... paramlist) {
        Query query = currentSession().createQuery(hql);
        setParameters(query, paramlist);

        return (T) query.uniqueResult();

    }


    /**
     * 执行批处理语句.如 之间insert, update, delete 等.
     */
    protected int execteBulk(final String hql, final Object... paramlist) {
        Query query = currentSession().createQuery(hql);
        setParameters(query, paramlist);
        Object result = query.executeUpdate();
        return result == null ? 0 : ((Integer) result).intValue();
    }
    
    protected int execteNativeBulk(final String natvieSQL, final Object... paramlist) {
        Query query = currentSession().createSQLQuery(natvieSQL);
        setParameters(query, paramlist);
        Object result = query.executeUpdate();
        return result == null ? 0 : ((Integer) result).intValue();
    }

//    protected <T> List<T> list(final String sql, final Object... paramlist) {
//        return list(sql, -1, -1, paramlist);
//    }
        
    @SuppressWarnings("unchecked")
    public <T> List<T> listByNative(final String nativeSQL, final int pn, final int pageSize,
            final List<Entry<String, Class<?>>> entityList, 
            final List<Entry<String, Type>> scalarList, final Object... paramlist) {

        SQLQuery query = currentSession().createSQLQuery(nativeSQL);
        if (entityList != null) {
            for (Entry<String, Class<?>> entity : entityList) {
                query.addEntity(entity.getKey(), entity.getValue());
            }
        }
        if (scalarList != null) {
            for (Entry<String, Type> entity : scalarList) {
                query.addScalar(entity.getKey(), entity.getValue());
            }
        }

        setParameters(query, paramlist);

        if (pn > -1 && pageSize > -1) {
            query.setMaxResults(pageSize);
            int start = PageUtil.getPageStart(pn, pageSize);
            if (start != 0) {
                query.setFirstResult(start);
            }
        }
        List<T> result = query.list();
        return result;
    }
        
    @SuppressWarnings("unchecked")
    protected <T> T aggregateByNative(final String natvieSQL, final List<Entry<String, Type>> scalarList, final Object... paramlist) {
        SQLQuery query = currentSession().createSQLQuery(natvieSQL);
        if (scalarList != null) {
            for (Entry<String, Type> entity : scalarList) {
                query.addScalar(entity.getKey(), entity.getValue());
            }
        }

        setParameters(query, paramlist);

        Object result = query.uniqueResult();
        return (T) result;
    }
        
    @SuppressWarnings("unchecked")
    public <T> List<T> list(ConditionQuery query, OrderBy orderBy, final int pn, final int pageSize) {
        Criteria criteria = currentSession().createCriteria(this.entityClass);
        query.build(criteria);
        orderBy.build(criteria);
        int start = PageUtil.getPageStart(pn, pageSize);
        if(start != 0) {
            criteria.setFirstResult(start);
        }
        criteria.setMaxResults(pageSize);
        return criteria.list();
    }
    
    @SuppressWarnings("unchecked")
    public <T> List<T> list(Criteria criteria, OrderBy orderBy, final int pn, final int pageSize) {
        orderBy.build(criteria);
        int start = PageUtil.getPageStart(pn, pageSize);
        if(start != 0) {
            criteria.setFirstResult(start);
        }
        criteria.setMaxResults(pageSize);
        return criteria.list();
    }
    @SuppressWarnings("unchecked")
	public <T> List<T> list(Criteria criteria, final int pn, final int pageSize) {
    	int start = PageUtil.getPageStart(pn, pageSize);
    	if(start != 0) {
    		criteria.setFirstResult(start);
    	}
    	criteria.setMaxResults(pageSize);
    	return criteria.list();
    }
    @SuppressWarnings("unchecked")
    public <T> List<T> list(Criteria criteria) {
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public <T> T unique(Criteria criteria) {
        return (T) criteria.uniqueResult();
    }

    public <T> List<T> list(DetachedCriteria criteria) {
        return list(criteria.getExecutableCriteria(currentSession()));
    }

    @SuppressWarnings("unchecked")
    public <T> T unique(DetachedCriteria criteria) {
        return (T) unique(criteria.getExecutableCriteria(currentSession()));
    }

    protected void setParameters(Query query, Object[] paramlist) {
        if (paramlist != null) {
            for (int i = 0; i < paramlist.length; i++) {
                if(paramlist[i] instanceof Date) {
                    //TODO 难道这是bug 使用setParameter不行？？
                    query.setTimestamp(i, (Date)paramlist[i]);
                } else {
                    query.setParameter(i, paramlist[i]);
                }
            }
        }
    }



	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
