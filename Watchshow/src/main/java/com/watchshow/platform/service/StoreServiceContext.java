package com.watchshow.platform.service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.apache.commons.fileupload.FileItem;
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
import com.watchshow.common.util.FileManagerUtil;
import com.watchshow.common.util.HibernateUtil;
import com.watchshow.platform.dao.PublicationDao;
import com.watchshow.platform.dao.StoreAdminHistoryDao;
import com.watchshow.platform.dao.StoreAdministratorDao;
import com.watchshow.platform.dao.WatchDao;
import com.watchshow.platform.dao.WatchStoreDao;
import com.watchshow.platform.domain.Publication;
import com.watchshow.platform.domain.StoreAdminHistory;
import com.watchshow.platform.domain.StoreAdministrator;
import com.watchshow.platform.domain.Watch;
import com.watchshow.platform.domain.WatchBrand;
import com.watchshow.platform.domain.WatchStore;
import com.watchshow.platform.helper.ServerResourcePathHelper;
import com.watchshow.platform.helper.StoreServiceHelper;

import eu.medsea.util.StringUtil;

public class StoreServiceContext extends AbstractServiceContext {

	private static final String Internal_Error_Reason = "Internal errors";
	private static final String Internal_Error_Message = "Failed at Requested Server"; 
	public static Float ServiceVersion = new Float(1.0);
	private String hostRealPath = null;
	private Method currentMethod;
    private String IPAddress = null;
    private StoreAdministrator currentAdmin = null;
    
    public void setIPAddress(String IPAddress) {
    	this.IPAddress = IPAddress;
    }
    
	public StoreServiceContext(String serviceName, String appURL,
			String realpath) {
		super(serviceName, appURL, realpath);
	}
    
	public static StoreServiceContext getService(StoreAdministrator admin, String serviceName, String realPath, String host) {
		StoreServiceContext service = new StoreServiceContext(serviceName, host, realPath);
        try {
        	service.hostRealPath = realPath;
        	service.currentAdmin = admin;
            service.currentMethod = service.getClass().getDeclaredMethod(serviceName, JSONObject.class, List.class);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.out.println("No such service implemented for " + serviceName);
            e.printStackTrace();
        }
        return service;
    }
    public JSONObject execute(String inputData, List<FileItem> uploadItems) {
    	JSONObject responseData = null;
		try {
			JSONObject decInput = decodeInputData(inputData);
			responseData = (JSONObject) currentMethod.invoke(this, decInput, uploadItems);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return responseData;
	}
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
    //////////////////////////////////////////////////////////////////////////////////////////
    // services
    //////////////////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("unused")
	protected JSONObject storeRegister(JSONObject params, List<FileItem> uploadItems) {
    	JSONObject response = null;
    	String reason =null, message = null;
    	Integer returnCode = -1;
    	JSONObject outputData = new JSONObject();
    	try {
    		//Get Founder Info and Store Info
    		Long storeId = new Long(-1);
    		JSONObject fields = params.getJSONObject("fields");
    		String founderName = params.isNull("founder") ? null : params.getString("founder");
    		String password = params.isNull("password") ? null : params.getString("password");
    		String repassword = params.isNull("repassword") ? null : params.getString("repassword");
    		String verifyEmail = params.isNull("email") ? null : params.getString("email");
    		
    		if (!password.equals(repassword)) {
    			returnCode = 0;
    			reason = "password not match";
    			message = reason;
    		} else {
    			StoreAdministrator founder = new StoreAdministrator();
        		founder.setLoginName(founderName);
        		founder.setPassword(password);
        		founder.setVerifyEmail(verifyEmail);
        		//Get form field and prepare to fill them into pojo, and then commit to database.
        		String storename = params.isNull("storename") ? null : params.getString("storename"); 
        		String brandChname = params.isNull("cnbrand") ? null : params.getString("cnbrand"); 
        		String brandEnname = params.isNull("enbrand") ? null : params.getString("enbrand"); 
        		String telephone = params.isNull("telephone") ? null : params.getString("telephone"); 
        		String province = params.isNull("province") ? null : params.getString("province"); 
        		String city = params.isNull("city") ? null : params.getString("city"); 
        		String district = params.isNull("district") ? null : params.getString("district"); 
        		String address = params.isNull("address") ? null : params.getString("address"); 
        		Integer postcode = params.isNull("postcode") ? null : params.getInt("postcode"); 
        		Long fax = params.isNull("fax") ? null : params.getLong("fax"); 
        		String website = params.isNull("website") ? null : params.getString("website"); 
        		String description = params.isNull("storedesc") ? null : params.getString("storedesc"); 
        		
        		WatchBrand brand = new WatchBrand();
        		brand.setChnName(brandChname);
        		brand.setEngName(brandEnname);
        		WatchStore store = new WatchStore();
        		store.setName(storename);
        		store.setPhoneNumber(telephone);
        		store.setProvince(province);
        		store.setCity(city);
        		store.setDistrict(district);
        		store.setAddress(address);
        		store.setPostcode(postcode);
        		store.setFax(fax);
        		store.setWebsite(website);
        		store.setDescription(description);
        		store.setBrand(brand);
        		founder.setStore(store);
        		
        		founder.setRole(StoreServiceHelper.RoleAttributeMap.get(StoreServiceHelper.ROLE.FOUNDER));

    			StoreAdminHistory log = new StoreAdminHistory();
    			Timestamp now = new Timestamp(System.currentTimeMillis());
    			log.setComments("Reigster store: " + store.getName() + " at " + now + ".");
    			log.setTimestamp(now);
    			log.setIPAddress(IPAddress);
    			log.setOperatedWatch(null);
    			log.setOwner(founder);
        		
        		WatchStoreDao DAO = new WatchStoreDao();
        		Session session = DAO.currentSession();
        		Transaction tx = session.beginTransaction();
        		try {
        			DAO.save(store);
        			tx.commit();
        			storeId = store.getIdentifier();
        			String storeDescSrcPath = ServerResourcePathHelper.getServerFolderForStoreDescSource(storeId);
        			System.out.println("path = "+storeDescSrcPath);
        			store.setDescResourceURL(storeDescSrcPath);
        			DAO.saveOrUpdate(store);
        		} catch (Exception ex) {
        			tx.rollback();
        		} finally {
        			session.close();
        		}
    		}
    		//Upload Files
    		for (int i=0;i<uploadItems.size();i++) {
				FileItem item = (FileItem) uploadItems.get(i);
				if (!item.isFormField()) {
					String itemName = item.getName();
					if (itemName == null || itemName =="") {
						continue;
					}
					String destFilename = ServerResourcePathHelper.generateServerPathForStoringStoreFile(itemName, storeId);
					String path = hostRealPath + File.separator + destFilename;
					File file = FileManagerUtil.createFile(path, itemName);
					item.write(file);
				} 
			}
    		
    	} catch (Exception ex) {
    		reason = Internal_Error_Reason;
    		message = Internal_Error_Message;
    	} finally {
    		response = StoreServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
    	}
    	return response;
    }

    protected JSONObject login(JSONObject params,List<FileItem> uploadItems) {
    	JSONObject response = null;
    	String reason =null, message = null;
    	Integer returnCode = -1;
    	JSONObject outputData = new JSONObject();
    	Boolean successful = false;
    	try {
    		String adminname = params.isNull("username") ? null : params.getString("username");
    		Long storeId = params.isNull("storenumber") ? null : params.getLong("storenumber");
    		String password = params.isNull("password") ? null : params.getString("password");
    		StoreAdministratorDao DAO = new StoreAdministratorDao();
    		StoreAdministrator admin = DAO.getStoreAdminByName(adminname, storeId);
    		Long adminId = new Long(-1);
    		if (admin != null) {
    			if (admin.getPassword().equals(password)) {
    				successful = true;
    				adminId = admin.getIdentifier();
    				returnCode = 1;
    			} else {
    				reason = "Incorrect Password";
    				message = "Login Failed";
    				returnCode = 0;
    			}
    			
    		} else {
    			message = "Login Failed";
    			reason = "Administrator:["+adminname+"] does not exist";
    			returnCode = 0;
    		}
    		outputData.put("successful", successful);
    		outputData.put("adminId", adminId);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		message = Internal_Error_Message;
    		reason = Internal_Error_Reason;
    	} finally {
    		HibernateUtil.currentSession().close();
    		response = StoreServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
    	}
    	return response;
    }
    protected JSONObject logout(JSONObject params, List<FileItem> uploadItems) {
    	return null;
    }
    protected JSONObject getWatchList(JSONObject params, List<FileItem> uploadItems) {
    	JSONObject response = null;
    	JSONObject outputData = new JSONObject();
		try {
			Long said = currentAdmin.getIdentifier();

			String[] COLUMNS = { "name", "watchband", "modifyDate" };
			JSONArray aaData = new JSONArray();
			@SuppressWarnings("unused")
			Integer istart = params.isNull("istart") ? 0 : params.getInt("istart");
			Integer iamount = params.isNull("ps") ? 10 : params.getInt("ps");
			if (iamount < 10 || iamount > 100) {
				iamount = 10;
			}
			Integer iecho = params.isNull("secho") ? 0 : params.getInt("secho");
			Integer icol  = params.isNull("icol") ? 0 : params.getInt("icol");
			Boolean isASCOrder = params.isNull("asc") ? false : params.getBoolean("asc");
			Integer ipage = params.isNull("ipage") ? 0 : params.getInt("ipage");
			String searchTerm = params.isNull("searchTerm") ? "" : params.getString("searchTerm");
			String columnName = COLUMNS[icol];
			StoreAdministratorDao adminDAO = new StoreAdministratorDao();
			StoreAdministrator admin = adminDAO.get(said);
			Long storeId = admin.getStore().getIdentifier();
			WatchDao watchDAO = new WatchDao();
			Session session2 = watchDAO.currentSession();
			Criteria c = session2.createCriteria(Watch.class);
			c.createCriteria("store").add(Restrictions.eq("identifier", storeId));
			
			OrderBy orderby = new OrderBy();
			Order curOrder = isASCOrder ? Order.asc(columnName) : Order
					.desc(columnName);
			orderby.add(curOrder);

			//TODO: correct logic here
			List<Watch> watches = watchDAO.list(c, orderby, ipage, iamount);
			int total = watches.size(); //watchDAO.countAll();
			int totalAfterFilter = total;
			if (searchTerm != "") {
				c.add(Restrictions.like(columnName, searchTerm));
				String conditionHQL = "select count(*) from "
						+ Watch.class.getSimpleName() + "where (" + columnName
						+ " like '%" + searchTerm + "%')";
				totalAfterFilter = watchDAO.count(conditionHQL);
			}
			watches = watchDAO.list(c, orderby, ipage, iamount);
			
			StoreAdminHistoryDao hisDAO = new StoreAdminHistoryDao();
			for (Iterator<Watch> it = watches.iterator(); it.hasNext();) {
				Watch w = (Watch) it.next();
				JSONArray ja = new JSONArray();
				ja.put(w.getIdentifier()); //id
				ja.put(w.getName()); //watchname
				// format date
				SimpleDateFormat df = new SimpleDateFormat(ConstantsProvider.SIMPLE_DATE_FORMAT);
				StoreAdminHistory his = hisDAO.getCreationHistoryForWatch(w);
				Timestamp creationTime = his.getTimestamp();
				String timestamp = df.format(creationTime);
				ja.put(timestamp); //createdtime
				his = hisDAO.getLastEditingHistoryForWatch(w);
				timestamp = df.format(his.getTimestamp());
				ja.put(timestamp); //modifytime
				String commiter = his.getOwner().getLoginName();
				StoreAdministratorDao d = new StoreAdministratorDao();
				StoreAdministrator cmter = d.getWatchCommiter(w.getIdentifier());
				if (cmter != null)
					commiter = cmter.getLoginName();
				ja.put(commiter); //owner
				ja.put(false);
				aaData.put(ja);
			}
			outputData.put("iTotalRecords", total);
			outputData.put("iTotalDisplayRecords", totalAfterFilter);
			outputData.put("aaData", aaData);
			outputData.put("sEcho", iecho);
			//returnCode = 1;
		} catch (JSONException e1) {
			e1.printStackTrace();
			//reason = Internal_Error_Reason;
			//message = Internal_Error_Message;
		} finally {
			HibernateUtil.currentSession().close();
			response = outputData;//StoreAdminHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
		return response;
	}
    
    protected JSONObject getNewsList(JSONObject params, List<FileItem> uploadItems) {
    	return getPublications(params, Publication.NEWS);
    }
    protected JSONObject getBulletinList(JSONObject params, List<FileItem> uploadItems) {
    	return getPublications(params, Publication.ANNOUNCEMENT);
    }
    protected JSONObject getActivityList(JSONObject params, List<FileItem> uploadItems) {
    	return getPublications(params, Publication.ACTIVITY);
    }
    private JSONObject getPublications(JSONObject params, String pubKind) {
    	JSONObject outputData = new JSONObject();
    	try {
    		//just for demo
    		List<Publication> newsset = null;
    		PublicationDao DAO = new PublicationDao();
    		Session session = DAO.currentSession();
    		Criteria criteria = session.createCriteria(Publication.class);
    		criteria.createCriteria("publisher").add(Restrictions.eq("identifier", currentAdmin.getIdentifier()));
    		criteria.add(Restrictions.eq("kind", pubKind));
    		newsset = DAO.list(criteria);
    		JSONArray aaData = new JSONArray();
    		StoreAdminHistoryDao d = new StoreAdminHistoryDao();
    		for (Publication news : newsset) {
    			SimpleDateFormat df = new SimpleDateFormat(ConstantsProvider.SIMPLE_DATE_FORMAT);
				JSONArray ja = new JSONArray();
				ja.put(news.getIdentifier());
				ja.put(news.getTitle());
				
				StoreAdminHistory h = d.getLastEditedHistoryForPublication(news);
				if (h != null) {
					ja.put(df.format(h.getTimestamp()));
				} else {
					ja.put("N/A");
				}
				ja.put(news.getPublisher().getLoginName());
				ja.put("0");
				aaData.put(ja);
			}
    		
    		outputData.put("iTotalRecords", newsset.size());
			outputData.put("iTotalDisplayRecords", newsset.size());
			outputData.put("aaData", aaData);
			outputData.put("sEcho", 0);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    		
    	} finally {
    		HibernateUtil.currentSession().close();
    	}
    	return outputData;
    }
    
    protected JSONObject getAdminLogList(JSONObject params, List<FileItem> uploadItems) {
    	JSONObject outputData = new JSONObject();
    	try {
    		//just for demo
    		List<StoreAdminHistory> logs = null;
    		if (params.isNull("storeId")) {
    			throw new Exception();
    		}
    		Long storeId = params.getLong("storeId");
    		StoreAdminHistoryDao DAO = new StoreAdminHistoryDao();
    		Criteria criteria = DAO.currentSession().createCriteria(StoreAdminHistory.class);
    		criteria.createCriteria("owner").createCriteria("store").add(Restrictions.eq("identifier", storeId));
    		criteria.addOrder(Order.desc("timestamp"));
    		logs = DAO.list(criteria);
    		DAO.currentSession().close();
    		JSONArray aaData = new JSONArray();
    		for (StoreAdminHistory log : logs) {
				JSONArray ja = new JSONArray();
				ja.put(log.getOwner().getLoginName());
				ja.put(log.getComments());
				ja.put(new SimpleDateFormat(ConstantsProvider.SIMPLE_DATE_FORMAT).format(log.getTimestamp()));
				aaData.put(ja);
			}
    		outputData.put("iTotalRecords", logs.size());
			outputData.put("iTotalDisplayRecords", logs.size());
			outputData.put("aaData", aaData);
			outputData.put("sEcho", 0);
    	} catch (Exception ex) {
    		
    	}
    	
    	return outputData;
    }
    
    protected JSONObject addWatch(JSONObject params, List<FileItem> uploadItems) {
    	Integer returnCode = -1;
    	JSONObject outputData = new JSONObject();
    	try {
    		JSONObject formFields = params.getJSONObject("fields");
			String watchname = formFields.getString("watchname");
			
			Float discount = formFields.isNull("discount")? null : formFields.getInt("discount")/100.f;
			Float price = formFields.isNull("price")? null : (float) formFields.getDouble("price");
			String simDesc = formFields.isNull("simpledesc")? null : formFields.getString("simpledesc");
			String desc = formFields.isNull("desc")? null : formFields.getString("desc");
			String movement = formFields.isNull("movement")? null : formFields.getString("movement");
			String material = formFields.isNull("material")? null : formFields.getString("material");
			String size = formFields.isNull("size")? null : formFields.getString("size");
			String arch = formFields.isNull("architecture")? null : formFields.getString("architecture");
			String style = formFields.isNull("style")? null : formFields.getString("style");
			String band = formFields.isNull("band")? null : formFields.getString("band");
			String functions = formFields.isNull("function")? null : formFields.getString("function");
			String DateString = formFields.isNull("marketTime")? null : formFields.getString("marketTime");
			Date marketTime = new SimpleDateFormat(ConstantsProvider.YMD_DATE_FORMAT).parse(DateString);
			
			Watch watch = new Watch();
			WatchStore curstore = currentAdmin.getStore();
			WatchBrand curbrand = curstore.getBrand();
			String brand =null;//TODO:selector
			if (formFields.isNull("brand")) {
				brand = curbrand.getEngName()+":"+curbrand.getChnName();
			}
			String bnames = curbrand.getEngName()+":"+ curbrand.getChnName();
			if (StringUtil.contains(bnames, brand) || brand.equalsIgnoreCase(bnames)) {
				watch.setBrand(curbrand);
			} else {
				returnCode = 0;
				throw new Exception();
			}
			watch.setName(watchname);
			watch.setDiscount(discount);
			watch.setPrice(price);
			watch.setSimpleDescription(simDesc);
			watch.setDescription(desc);
			watch.setMovement(movement);
			watch.setMarketTime(marketTime);
			watch.setMaterial(material);
			watch.setSize(size);
			watch.setArchitecture(arch);
			watch.setStyle(style);
			watch.setWatchband(band);
			watch.setFunction(functions);
			watch.setStore(curstore);

			StoreAdminHistory log = new StoreAdminHistory();
			log.setAction(StoreServiceHelper.ActionAttributeMap.get(StoreServiceHelper.ACTION.CREATE));
			log.setTimestamp(new Timestamp(System.currentTimeMillis()));
			log.setOperatedWatch(watch);
			log.setIPAddress(IPAddress);
			log.setComments("$place_holder$ needs to update!");
			log.setOwner(currentAdmin);
			
			StoreAdminHistoryDao DAO = new StoreAdminHistoryDao();
			Session session = DAO.currentSession();
			Transaction tx = session.beginTransaction();
			try {
				DAO.save(log); //cascade all linked pojos
				log.setComments(StoreServiceHelper.createSimpleComment(currentAdmin, watch, StoreServiceHelper.ACTION.CREATE));
				String srcpath = ServerResourcePathHelper.getServerFolderForWatchDescSource(curstore.getIdentifier(), watch.getIdentifier());
				watch.setDescResourceURL(srcpath);
				DAO.saveOrUpdate(log);
				tx.commit();
				returnCode = 1;
			} catch (Exception ex) {
				ex.printStackTrace();
				tx.rollback();
			} finally {
				session.close();
			}
			if (returnCode == 1 && uploadItems != null) {
				returnCode = 0;
        		for (int i=0;i<uploadItems.size();i++) {
    				FileItem item = (FileItem) uploadItems.get(i);
    				if (!item.isFormField()) {
    					String itemName = item.getName();
    					if (itemName == null || itemName.isEmpty()) {
    						continue;
    					}
    					String destFilename = ServerResourcePathHelper.generateServerPathForStoringWatchFile(itemName, curstore.getIdentifier(), watch.getIdentifier());
    					String path = hostRealPath + File.separator + destFilename;
    					File file = FileManagerUtil.createFile(path, itemName);
    					item.write(file);
    				} 
    			}
        		returnCode = 1;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	return outputData;
    }
    protected JSONObject addNews(JSONObject params, List<FileItem> uploadItems) {
    	JSONObject tempData = new JSONObject();
    	try {
			tempData.put("response", addPublication(Publication.NEWS, params, uploadItems));
			tempData.put("anchorTag", "#news");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return tempData;
    }
    protected JSONObject addAnnouncement(JSONObject params, List<FileItem> uploadItems) {
    	JSONObject tempData = new JSONObject();
    	try {
			tempData.put("response",  addPublication(Publication.ANNOUNCEMENT, params, uploadItems));
			tempData.put("anchorTag", "#pubs");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return tempData;
    }
    protected JSONObject addActivity(JSONObject params, List<FileItem> uploadItems) {
    	JSONObject tempData = new JSONObject();
    	try {
			tempData.put("response",  addPublication(Publication.ACTIVITY, params, uploadItems));
			tempData.put("anchorTag", "#activity");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return tempData;
    }
    protected JSONObject addArticle(JSONObject params, List<FileItem> uploadItems) {
    	JSONObject tempData = new JSONObject();
    	try {
			tempData.put("response", addPublication(Publication.ARTICLE, params, uploadItems));
			tempData.put("anchorTag", "#article");
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return tempData;
    }
    private JSONObject addPublication( String kind, JSONObject params, List<FileItem> uploadItems) {
    	Integer returnCode = -1;
    	JSONObject outputData = new JSONObject();
    	try {
    		Long adminId = currentAdmin.getIdentifier();
    		if (params.isNull("fields")) {
    			throw new Exception();
    		}
    		JSONObject formFields = params.getJSONObject("fields");
    		String title = formFields.isNull("publication_title") ? null : formFields.getString("publication_title");
    		String subtitle = formFields.isNull("subtitle") ? null : formFields.getString("subtitle");
    		String content = formFields.isNull("content") ? null : formFields.getString("content");
    		String externalURL = null;
    		if (!formFields.isNull("external_url")) {
    			externalURL = formFields.getString("external_url");
    		}
    		Publication publication = new Publication();
    		publication.setTitle(title);
    		publication.setSubtitle(subtitle);
    		publication.setExternalURL(externalURL);
    		publication.setContent(content);
    		publication.setKind(kind);
    		currentAdmin = new StoreAdministratorDao().get(adminId);
    		publication.setPublisher(currentAdmin);
    		//news.setReferredWatches(); //FIXME: ????
    		StoreAdminHistory log = new StoreAdminHistory();
			log.setAction(StoreServiceHelper.ActionAttributeMap.get(StoreServiceHelper.ACTION.CREATE));
			log.setTimestamp(new Timestamp(System.currentTimeMillis()));
			log.setPublication(publication);
			log.setIPAddress(IPAddress);
			log.setComments("$place_holder$ needs to update!");
			log.setOwner(currentAdmin);
    		
    		StoreAdminHistoryDao DAO = new StoreAdminHistoryDao();
    		
    		Session session = DAO.currentSession();
    		Transaction tx = session.beginTransaction();
    		try {
    			DAO.save(log); //cascade to save all related pojos
        		String srcPath = ServerResourcePathHelper.getServerFolderForStorePubSource(currentAdmin.getStore().getIdentifier(), publication.getIdentifier());
        		publication.setResourcesURL(srcPath);
        		log.setComments(StoreServiceHelper.createSimpleComment(currentAdmin, publication, StoreServiceHelper.ACTION.CREATE));
        		DAO.saveOrUpdate(log);
    			tx.commit();
    			returnCode = 1;
    		} catch(Exception ex) {
    			ex.printStackTrace();
    			tx.rollback();
    		} finally {
    			session.close();
    		}
    		if (returnCode == 1 && uploadItems != null) {
    			returnCode = 0;
        		for (int i=0;i<uploadItems.size();i++) {
    				FileItem item = (FileItem) uploadItems.get(i);
    				if (!item.isFormField()) {
    					String itemName = item.getName();
    					if (itemName == null || itemName.isEmpty()) {
    						continue;
    					}
    					String destFilename = ServerResourcePathHelper.generateServerPathForStoringPubFile(itemName, currentAdmin.getStore().getIdentifier(), publication.getIdentifier());
    					String path = hostRealPath + File.separator + destFilename;
    					File file = FileManagerUtil.createFile(path, itemName);
    					item.write(file);
    				} 
    			}
        		returnCode = 1;
    		}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	} finally {
    	}
    	return outputData;
    }
    
}
