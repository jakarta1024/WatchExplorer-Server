package com.watchshow.platform.service;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.watchshow.common.dao.OrderBy;
import com.watchshow.common.util.ConstantsProvider;
import com.watchshow.platform.dao.PlatformAdminLogDao;
import com.watchshow.platform.dao.PlatformAdministratorDao;
import com.watchshow.platform.dao.PlatformBulletinDao;
import com.watchshow.platform.dao.StoreAdministratorDao;
import com.watchshow.platform.dao.UserHistoryDao;
import com.watchshow.platform.dao.WatchStoreDao;
import com.watchshow.platform.domain.PlatformAdminLog;
import com.watchshow.platform.domain.PlatformAdministrator;
import com.watchshow.platform.domain.PlatformBulletin;
import com.watchshow.platform.domain.StoreAdministrator;
import com.watchshow.platform.domain.UserHistory;
import com.watchshow.platform.domain.Watch;
import com.watchshow.platform.domain.WatchBrand;
import com.watchshow.platform.domain.WatchStore;
import com.watchshow.platform.helper.PlatformServiceHelper;

public class PlatformServiceContext extends AbstractServiceContext {
	


	private static final String Internal_Error_Reason = "Internal errors";
	private static final String Internal_Error_Message = "Failed at Requested Server"; 
	public static Float ServiceVersion = new Float(1.0);
	
	private PlatformAdministrator currentAdmin = null;
    private Method currentMethod;
    @SuppressWarnings("unused")
	private String hostURL = null;
    @SuppressWarnings("unused")
	private String hostRealPath = null;
    private static final String COMPLAINT = "COMPLAIN";
    
		
	public PlatformServiceContext(String serviceName, String appURL, String realPath) {
		super(serviceName, appURL, realPath);
	}
	/**
    public static PlatformServiceContext createServiceContext(PlatformAdministrator admin,String serviceName, String appURL, String realPath) {
        PlatformServiceContext service = new PlatformServiceContext(serviceName, appURL, realPath);
        try {
        	service.currentAdmin = admin;
        	service.hostURL = appURL;
        	service.hostRealPath = realPath;
            service.currentMethod = service.getClass().getDeclaredMethod(serviceName, JSONObject.class);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.out.println("No such service implemented for " + serviceName);
            e.printStackTrace();
        }
        return service;
    }*/
    public JSONObject execute(String inputData) {
    	JSONObject responseData = null;
		try {
			JSONObject decInput = decodeInputData(inputData);
			responseData = (JSONObject) currentMethod.invoke(this, decInput);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return responseData;
	}
	///////////////////////////////////////////////////////////////////////////////
    protected JSONObject login(JSONObject params) {
    	JSONObject response = null;
    	JSONObject outputData = new JSONObject();
    	Integer returnCode = -1;
    	String reason = null, message = null;
    	try {
			String adminname = params.isNull("username") ? null : params.getString("username");
			String password  = params.isNull("password") ? null : params.getString("password");
			PlatformAdministrator admin = new PlatformAdministratorDao().getAdminByName(adminname);
			if (admin != null) {
				if (admin.getPassword().equals(password)) {
					outputData.put("successful", true);
					outputData.put("adminname", admin.getLoginName());
					returnCode = 1;
				} else {
					outputData.put("successful", false);
				}
			} else {
				returnCode = 0;
				outputData.put("successful", false);
				message = "Administrator named ["+adminname+"] does not exist";
				reason  = "Normal Warning";
			}
			
		} catch (Exception e) {
			reason = Internal_Error_Reason;
			message = Internal_Error_Message;
		} finally {
			response = PlatformServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
    	return response;
    }
    
    protected JSONObject logout(JSONObject params) {
    	System.out.println("does nothing!");
    	return null;
    }
	
	protected JSONObject getUserComplaints(JSONObject params) {
		JSONObject response = null;
		JSONObject outputData = new JSONObject();
		UserHistoryDao DAO = new UserHistoryDao();
		Criteria criteria = DAO.currentSession().createCriteria(UserHistory.class);
		criteria.add(Restrictions.eq("action", COMPLAINT));
		List<UserHistory> complaints = DAO.list(criteria);
		Integer returnCode = -1; String reason = null, message = null;
		try {
			JSONArray result = new JSONArray();
			for (UserHistory com : complaints) {
				JSONObject item = new JSONObject();
				item.put("type", "watch");
				Watch watch = com.getViewedWatch();
				WatchStore store = watch.getStore();
				item.put("storeId", store.getIdentifier().toString());
				item.put("watchId", watch.getIdentifier().toString());
				item.put("comment", com.getComment());
				item.put("fromIP", com.getIPAddress());
				item.put("time", com.getTimestamp().toString());
				result.put(item);
			}
			outputData.put("totalNum", new Long(result.length()).toString());
			outputData.put("complaints", result);
			returnCode = 1;
		} catch (JSONException e) {
			reason = Internal_Error_Reason;
			message = Internal_Error_Message;
		} finally {
			response = PlatformServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
		return response;
	}

	protected JSONObject getStores(JSONObject params) {
		JSONObject response = new JSONObject();
		JSONObject out = new JSONObject();
		StoreAdministratorDao DAO = new StoreAdministratorDao();
		Integer returnCode = -1; String reason = null, message = null;
		try {
			Integer totalNum = 0;
			Boolean page = params.getBoolean("page");
			JSONArray data = new JSONArray();
			if (params.isNull("approving")) {
				//means all
				List<WatchStore> stores = null;
				WatchStoreDao watchstoreDAO = new WatchStoreDao();
				if (page == true) {
					int pn = params.getInt("pn");
					int pageSize = params.getInt("ps");
					stores = watchstoreDAO.listAll(pn, pageSize);
				} else {
					stores = watchstoreDAO.listAll();
				}
				for (WatchStore s : stores) {
					JSONArray ja = new JSONArray();
					StoreAdministrator founder = watchstoreDAO.getFounder(s.getIdentifier());
					Boolean authorised = founder.getAuthorised();
					ja.put(s.getIdentifier());
					ja.put(s.getName());//name
					ja.put(founder.getLoginName());
					ja.put(authorised ? "Approved" : "Approving");
					SimpleDateFormat df = new SimpleDateFormat(ConstantsProvider.SIMPLE_DATE_FORMAT);
					ja.put(df.format(s.getRegisterTime()));
					ja.put(authorised ? df.format(s.getFoundTime()) : "---- -- -- --:--:--");
					ja.put(df.format(s.getRegisterTime()));
					ja.put(founder.getIdentifier());
					data.put(ja);
				}
				totalNum = stores.size();
			} else {
				List<StoreAdministrator>admins = null;
				Criteria criteria = DAO.currentSession().createCriteria(StoreAdministrator.class);
				Boolean approving = params.getBoolean("approving");
				if (approving) {
					criteria.add(Restrictions.eq("authorised", false)); //get unauthorised admins
				} else {
					criteria.add(Restrictions.eq("authorised", true)); 
				}
				criteria.add(Restrictions.eq("role", "founder"));
				if (page == false) {
					admins = DAO.list(criteria);
				} else {
					int pageNumber = params.isNull("pn") ? 0 : params.getInt("pn");
					int pageSize  = params.isNull("sz") ? 10 : params.getInt("sz");
					Order order = Order.desc("identifier");
					OrderBy orderBy = new OrderBy();
					orderBy.add(order);
					admins = DAO.list(criteria, orderBy, pageNumber, pageSize);
				}
				
				for (StoreAdministrator admin : admins) {
					JSONArray o = new JSONArray();
					o.put(admin.getStore().getIdentifier());
					o.put(admin.getStore().getName());
					WatchBrand brand = admin.getStore().getBrand();
					o.put(brand.getEngName()+":"+brand.getChnName());
					o.put(admin.getLoginName());
					SimpleDateFormat df = new SimpleDateFormat(ConstantsProvider.SIMPLE_DATE_FORMAT);
					o.put(df.format(admin.getStore().getRegisterTime())); //getFoundTime
					o.put(admin.getIdentifier());
					data.put(o);
				}
				totalNum = admins.size();
			}
			out.put("iTotalRecords",totalNum);
			out.put("iTotalDisplayRecords", totalNum);
			out.put("aaData", data);
			returnCode = 1;
		} catch (JSONException e) {
			e.printStackTrace();
			reason = Internal_Error_Reason;
			message = Internal_Error_Message;
		} finally {
			response = PlatformServiceHelper.sharedResponseTemplate(returnCode, reason, message, out);
		}
		return response;
	}
	protected JSONObject getAllStores(JSONObject params) {
		JSONObject response = new JSONObject();
		JSONObject out = new JSONObject();
		WatchStoreDao DAO = new WatchStoreDao();
		Integer returnCode = -1; String reason = null, message = null;
		try {
			Boolean page = params.getBoolean("page");
			List<WatchStore>stores = null;
			JSONArray a = new JSONArray();
			if (page == true) {
				int pn = params.getInt("pn");
				int pageSize = params.getInt("ps");
				stores = DAO.listAll(pn, pageSize);
			} else {
				stores = DAO.listAll();
			}
			for (WatchStore store : stores) {
				JSONObject o = new JSONObject();
				WatchStoreDao storeDAO = new WatchStoreDao();
				StoreAdministrator founder = storeDAO.getFounder(store.getIdentifier());
				o.put("storeId", store.getIdentifier().toString());
				o.put("storeName", store.getName());
				o.put("brand", store.getBrand().getEngName() +":"+ store.getBrand().getChnName());
				o.put("founder", founder.getLoginName());
				o.put("authorized", founder.getAuthorised().toString());
				SimpleDateFormat df = new SimpleDateFormat(ConstantsProvider.SIMPLE_DATE_FORMAT);
				String date = df.format(store.getFoundTime());
				o.put("time", date);
				a.put(o);
				out.put("data", a);
			}
			out.put("iTotalRecords",stores.size());
			out.put("iTotalDisplayRecords", stores.size());
			out.put("aaData", out);
		} catch (Exception ex) {
			ex.printStackTrace();
			reason = Internal_Error_Reason;
			message = Internal_Error_Message;
		} finally {
			response = PlatformServiceHelper.sharedResponseTemplate(returnCode, reason, message, out);
		}
		return response;
	}
	
	protected JSONObject getStoreInfo(JSONObject params) {
		JSONObject out = new JSONObject();
		try {
			Long id = params.isNull("storeId") ? -1 : params.getLong("storeId");
			WatchStoreDao DAO = new WatchStoreDao();
			WatchStore store = DAO.get(id);
			if (store != null) {
				out.put("storeId", store.getIdentifier());
				out.put("storeName", store.getName());
				out.put("brand_zh", store.getBrand().getChnName());
				out.put("brand_en", store.getBrand().getEngName());
				out.put("province", store.getProvince());
				out.put("city", store.getCity());
				out.put("district", store.getDistrict());
				out.put("address", store.getAddress());
				out.put("credits", store.getCredits().toString());
				out.put("desc", store.getDescription());
				out.put("phone", store.getPhoneNumber());
				out.put("fax", store.getFax());
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return out;
	}
	protected JSONObject addAnnounce( JSONObject params) {
		JSONObject data = addBulletin(PlatformBulletin.ANNOUCEMENT, params);
		return data;
	}
	protected JSONObject addNotice( JSONObject params) {
		JSONObject data = addBulletin(PlatformBulletin.NOTICE, params);
		return data;
	}
	protected JSONObject addBulletin(String kind, JSONObject params) {
		JSONObject response = new JSONObject();
		Boolean successful = false;
		JSONObject data = new JSONObject();
		Integer returnCode = -1; String reason = null, message = null;
		try {
			JSONObject formFields = params.getJSONObject("fields");
			PlatformBulletin bulletin = new PlatformBulletin();
			bulletin.setKind(kind);
			bulletin.setContent(formFields.getString("content"));
			bulletin.setTitle(formFields.getString("title"));
			bulletin.setSubtitle(formFields.isNull("subtitle")? null : formFields.getString("subtitle"));
			bulletin.setIsActive(true);//active this now
			Integer effectiveTime = formFields.getInt("interval");
			bulletin.setEffectiveTime(effectiveTime);
			//generate log
			//get current active platform administrator information.
			bulletin.setPublisher(currentAdmin);
			
			PlatformAdminLog log = new PlatformAdminLog();
			PlatformServiceHelper.ACTION actionKey;
			if (kind.equalsIgnoreCase("notice")) {
				actionKey = PlatformServiceHelper.ACTION.ADD_NOTICE;
			} else if (kind.equalsIgnoreCase("announce")) {
				actionKey = PlatformServiceHelper.ACTION.ADD_ANNOUNCE;
			} else {
				actionKey = PlatformServiceHelper.ACTION.UNKNOWN;
			}
			log.setAction(PlatformServiceHelper.ActionAttributesMap.get(actionKey));
			log.setComments(PlatformServiceHelper.createSimpleComment(currentAdmin, bulletin, actionKey));
			log.setAdmin(currentAdmin);
			log.setPublishedBulletin(bulletin);
			log.setTimestamp(new Timestamp(System.currentTimeMillis()));
			
			PlatformAdminLogDao logDAO = new PlatformAdminLogDao();
			Session session = logDAO.currentSession();
			Transaction tx = session.beginTransaction();
			try {
				logDAO.save(log);
				tx.commit();
				successful = true;
			} catch (Exception ex) {
				returnCode = 0;
				reason = Internal_Error_Reason;
				message = Internal_Error_Message;
				tx.rollback();
			} finally {
				session.close();
			}
			data.put("successful", successful);
		}catch(Exception ex) {
			reason = Internal_Error_Reason;
			message = Internal_Error_Message; 
		}
		response = PlatformServiceHelper.sharedResponseTemplate(returnCode, reason, message, data);
		return response;
	}
	
	protected JSONObject getBulletinList(JSONObject params) {
		JSONObject response = null;
		Integer returnCode = -1; String reason = null, message = null;
		JSONObject outputData = new JSONObject();
		try {
			String kind = params.getString("kind");
			PlatformBulletinDao DAO = new PlatformBulletinDao();
			Criteria criteria = DAO.currentSession().createCriteria(PlatformBulletin.class);
			criteria.add(Restrictions.eq("kind", kind));
			criteria.createCriteria("publisher").add(Restrictions.eq("identifier", currentAdmin.getIdentifier()));
			List<PlatformBulletin> bulletins = DAO.list(criteria);
			JSONArray aaData = new JSONArray();
			for (PlatformBulletin b : bulletins) {
				JSONArray ja = new JSONArray();
				ja.put(b.getIdentifier());
				ja.put(b.getTitle());
				String time = new SimpleDateFormat(ConstantsProvider.SIMPLE_DATE_FORMAT).format(b.getPublishTime());
				ja.put(time);
				ja.put(b.getPublisher().getLoginName());
				ja.put(b.getPublisher().getIdentifier());
				aaData.put(ja);
			}
			outputData.put("aaData", aaData);
			outputData.put("iTotalRecords",bulletins.size());
			outputData.put("iTotalDisplayRecords", bulletins.size());
		} catch (Exception ex) {
			reason = Internal_Error_Reason;
			message = Internal_Error_Message; 
		} finally {
			response = PlatformServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
		return response;
	}
	
	//protected getDataForStatisticGraph
	private JSONObject decodeInputData(String inputData) {
		JSONObject inputJson = null;
		try {
			inputJson = new JSONObject(inputData);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><");
		System.out.println("inputData: "+inputData);
		System.out.println("><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><");
		return inputJson;
	}

}
