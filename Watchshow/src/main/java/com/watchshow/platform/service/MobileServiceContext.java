package com.watchshow.platform.service;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.watchshow.common.dao.OrderBy;
import com.watchshow.common.util.Assert;
import com.watchshow.common.util.EncryptUtil;
import com.watchshow.common.util.HibernateUtil;
import com.watchshow.platform.dao.PublicationDao;
import com.watchshow.platform.dao.UserDao;
import com.watchshow.platform.dao.UserHistoryDao;
import com.watchshow.platform.dao.WatchDao;
import com.watchshow.platform.domain.Publication;
import com.watchshow.platform.domain.User;
import com.watchshow.platform.domain.UserHistory;
import com.watchshow.platform.domain.Watch;
import com.watchshow.platform.helper.MobileServiceHelper;
import com.watchshow.platform.helper.PlatformServiceHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * URL Pattern: /mobileuser/services/$ServiceName
 * @author Kipp Li
 */
public class MobileServiceContext extends AbstractServiceContext {

	private static final String Internal_Error_Reason = "Internal errors";
	private static final String Internal_Error_Message = "Failed at Requested Server"; 
	
	public static Float ServiceVersion = new Float(1.0);
	public static String ServiceIdentifier = "MobileUserService";
	private String webappRealPath;
	@SuppressWarnings("unused")
	private String appHostURL;
	private Method currentMethod;
	private String passedServiceName;
	
	public MobileServiceContext(String serviceName, String appURL, String realpath) {
		super(serviceName, appURL, realpath);
	}
	
	public static MobileServiceContext createServiceContext(String serviceName, String appURL ,String realpath) {
		MobileServiceContext service = new MobileServiceContext(serviceName, appURL, realpath);
		try {
			service.webappRealPath = realpath;
			service.appHostURL = appURL;
		    service.passedServiceName = serviceName;
			service.currentMethod = service.getClass().getDeclaredMethod(serviceName, String.class);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("No such service implemented for " + serviceName);
			try {
                service.currentMethod = service.getClass().getDeclaredMethod("serviceNotFound", String.class);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (SecurityException e1) {
                e1.printStackTrace();
            }
		}
		return service;
	}
	/**
	 * Execute service corresponding to your call
	 * @param inputData - passed data which will be used by corresponding service method
	 * @return response data
	 */
	public JSONObject execute(String inputData) {
		JSONObject responseData = null;
		try {
			responseData = (JSONObject) currentMethod.invoke(this, inputData);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return responseData;
	}
	public JSONObject serviceNotFound(String inputData) throws JSONException {
	    Integer returnCode = 0;
	    String message = "Service["+ passedServiceName +"] is Not Found!";
    	String reason  = "Service Not Found";
    	JSONObject response = MobileServiceHelper.sharedResponseTemplate(returnCode, reason, message, new JSONObject());
	    return response;
	}
	/**
	 * Register a mobile user using input data. The parameter
	 * <code>method</code> represents register method, there are 3 ways to
	 * finished registration.
	 * <ul>
	 * <li>"auto" - which means system will register a raw user using device
	 * information.</li>
	 * <li>"signup" - which means user should provide some required
	 * information to finish registering.</li>
	 * <li>"oauth" - which implies user can connect 3rd party account to get
	 * access to this system, i.e. Weibo, QQ, etc.</li>
	 * </ul>
	 * <ul>
	 * Common parameters:
	 * <li>deviceSN - device serial number;
	 * <li>os - device os identifier;
	 * <li>MAC - device MAC address;
	 * <li>IP - device IP address;
	 * <li>uuid - device uuid;
	 * </ul>
	 * <ul>
	 * For <code>auto</code> method, following parameters are required:
	 * <li> reserved - null
	 * </ul>
	 * <ul>
	 * For <code>ordinary</code> method, following parameters are required:
	 * <li>username - login username
	 * <li>password - user password
	 * <li>email - verify email address
	 * </ul>
	 * <p>
	 * 
	 * @param inputData
	 * @return response data represented by a instance of {@link JSONObject}.
	 * @throws JSONException
	 */
	protected JSONObject registerMobileUser(String inputData) {
		JSONObject response = null;
		JSONObject decInputData = decodeInputData(inputData);
		JSONObject outputData = new JSONObject();
		String reason = null, message = null;
		int returnCode = -1;
		
		//get common informations
		String method = "undefined", deviceSN = null, deviceOS = null , deviceMacAddr = null, deviceIPAddr = null, deviceUUID = null;
		try {
			method = decInputData.isNull("method") ? "undefined" : decInputData.getString("method");
			deviceSN = decInputData.isNull("deviceSN") ? null : decInputData.getString("deviceSN");
			deviceOS = decInputData.isNull("os") ? null : decInputData.getString("os");
			deviceMacAddr = decInputData.isNull("MAC") ? null : decInputData.getString("MAC");
			deviceIPAddr = decInputData.isNull("IP") ? null : decInputData.getString("IP");
			deviceUUID = decInputData.isNull("guid") ? null : decInputData.getString("guid");
		} catch (JSONException ex) {
			
		}
		
		UserDao userDAO = new UserDao();
		Long userid = new Long(-1);
		User existingUser = userDAO.getUserByDeviceSerialsNumber(deviceSN);
	
		if (method.equalsIgnoreCase("auto")) {
		    if (existingUser != null) {
		        userid = existingUser.getIdentifier();
		        returnCode = 1;
		    } else {
                Date now = new Date();
                Timestamp d = new Timestamp(now.getTime());
                User user = new User();
                user.setUserName("auto_"+d);
                user.setVerifyEmail(MobileServiceHelper.generatePlaceholderEmailForUser(user));
                user.setPassword(MobileServiceHelper.generateAutoPassword());
                UserHistory log = new UserHistory();
                log.setDeviceOS(deviceOS);
                log.setDeviceMacAddress(deviceMacAddr);
                log.setIPAddress(deviceIPAddr);
                log.setDeviceUUID(deviceUUID);
                log.setAction(MobileServiceHelper.ActionAttributesMap.get(MobileServiceHelper.ACTION.REGISTER));
                log.setDeviceSN(deviceSN);
                log.setComment(MobileServiceHelper.createSimpleComment(user, null, MobileServiceHelper.ACTION.REGISTER));
                log.setOwner(user);
                log.setTimestamp(d);
                Transaction tx = HibernateUtil.currentSession().beginTransaction();
                try {
                	userDAO.save(user);
                    user.setUserName("auto_"+user.getIdentifier());
                    new UserHistoryDao().save(log);
                    userid = user.getIdentifier();
                    tx.commit();
                    returnCode = 1;
                } catch (Exception ex) {
                	tx.rollback();
                } finally {
                	HibernateUtil.currentSession().close();
                }
                
            }
        } else if (method.equalsIgnoreCase("signup")) {
        	String userName = null, password = null, email = null;
        	try {
        		userName = decInputData.getString("username");
                password = decInputData.getString("password");
                email = decInputData.getString("email");
        	} catch (JSONException ex) {
        		
        	}
            
            User user = existingUser;
            Timestamp registerTime = new Timestamp(new Date().getTime());
            if (user != null) {
                user.setUserName(userName);
                user.setPassword(password);
                user.setPasswordMD5(EncryptUtil.MD5(password));
                user.setVerifyEmail(email);
                user.setRegisterTime(registerTime);
                returnCode = 1;
            } else {
                existingUser = userDAO.getUserByEmail(email);
                if (existingUser == null) {
                    user = new User();
                    user.setUserName(userName);
                    user.setPassword(password);
                    user.setVerifyEmail(email);
                    user.setRegisterTime(registerTime);
                    returnCode = 1;
                } else {
                    reason = "Email has been used for others regiestration!";
                    returnCode = 0;
                }
            } 
            if (returnCode == 1) {
                Assert.isTrue(user != null, "User can not be null!");
                UserHistory log = new UserHistory();
                log.setAction(MobileServiceHelper.ActionAttributesMap.get(MobileServiceHelper.ACTION.SIGNUP));
                log.setDeviceOS(deviceOS);
                log.setDeviceMacAddress(deviceMacAddr);
                log.setIPAddress(deviceIPAddr);
                log.setDeviceUUID(deviceUUID);
                log.setTimestamp(user.getRegisterTime());
                log.setComment(MobileServiceHelper.createSimpleComment(user, null, MobileServiceHelper.ACTION.SIGNUP));
                log.setOwner(user);
                Session s = HibernateUtil.currentSession();
                Transaction tx = s.beginTransaction();
                try {
                	userDAO.save(user);
                    tx.commit();
                    userid = user.getIdentifier();
                } catch (Exception e) {
                	tx.rollback();
                } finally {
                	s.close();
                }
            }
        } else if (method.equalsIgnoreCase("oauth")) {
            
        } else {
            System.out.println("Registration "+method+" does not supported!");
        }
		
		//* get response things
		if (returnCode == 1) {
			try {
				outputData.put("userid", userid.toString());
				outputData.put("reserved", new Boolean(true).toString());
			} catch (JSONException e) {
				e.printStackTrace();
				reason = Internal_Error_Reason;
				message = Internal_Error_Message;
				returnCode = -1;
			}
			
		}
		response = MobileServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		return response;
	}
	
	protected JSONObject login(String inputData) {
		JSONObject res = null;
		JSONObject decInputData = decodeInputData(inputData);
		String reason = null, message = null;
		Integer returnCode = -1;
		JSONObject outputData = new JSONObject();
		try {
			String username = decInputData.getString("username");
			String password = decInputData.getString("password");
			String email = null;
			if (decInputData.isNull("email")) {
				email = MobileServiceHelper.isEmailAddress(username) ? username : null;
			} else {
				email = decInputData.getString("email");
			}
			
			UserDao DAO = new UserDao();
			User user = DAO.getUserByName(username);
			if (user == null) {
				user = DAO.getUserByEmail(email);
			}
			Boolean sucessed = false;
			if (user != null) {
				if (user.getPassword().equals(password)) {
					sucessed = true;
				} else {
					reason = "Wrong password!";
					message = "Login Failed";
					returnCode = 0;
				}
			} else {
				message = "Login Failed";
				reason = "User with Name: <"+username+"> or Email: <"+email+"> does not exist!";
				returnCode = 0;
			}
			outputData.put("successful", sucessed.toString());
			if (sucessed) {
				outputData.put("userId", user.getIdentifier().toString());
				outputData.put("username", user.getUserName());
				outputData.put("avatarURL", user.getAvatarURL());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			message = Internal_Error_Message;
			reason = Internal_Error_Reason;
			returnCode = -1;
		} finally {
			HibernateUtil.currentSession().close();
			res = MobileServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
		
		return res;
	}
	
	protected JSONObject logout(String inputData) {
		JSONObject res = null;
		JSONObject outputData = new JSONObject();
		String reason = null, message = null;
		Integer returnCode = -1;
		try {
			outputData.put("successful", new Boolean(true).toString());
			returnCode = 1;
		} catch (JSONException e) {
			e.printStackTrace();
			message = Internal_Error_Message;
			reason = Internal_Error_Reason;
		} finally {
			res = MobileServiceHelper.sharedResponseTemplate(returnCode,reason, message, outputData);
		}
		return res;
	}
	/**
	 * Return all news headers
	 * @return
	 */
	protected JSONObject getNewsList(String inputData) {
		JSONObject res = null;
		JSONObject outputData = new JSONObject();
		@SuppressWarnings("unused")
		JSONObject decInputData = decodeInputData(inputData);
		String reason = null, message = null;
		Integer returnCode = -1;
		try {
			PublicationDao dao = new PublicationDao();
			List<Publication>newslist = dao.lastNews();
			JSONArray ja = new JSONArray();
			Integer totalNum = newslist.size();
			for (Publication news : newslist) {
				String title = news.getTitle();
				String subtitle = news.getSubtitle();
				String id = news.getIdentifier().toString();
				JSONObject jo = new JSONObject();
				jo.put("id", id);
				jo.put("title", title);
				jo.put("subtitle", subtitle);
				jo.put("thumbnailURL", dao.getThumbnailURLForPub(news.getIdentifier(), webappRealPath));
				ja.put(jo);
			}
			outputData.put("newslist", ja);
			outputData.put("totalNum", totalNum.toString());
			returnCode = 1;
		}catch(Exception ex) {
			ex.printStackTrace();
			reason = Internal_Error_Reason;
			message = Internal_Error_Message;
		} finally {
			HibernateUtil.currentSession().close();
			res = MobileServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
		return res;
	}
	/**
	 * 
	 * @param inputData
	 * @return
	 */
	protected JSONObject getNewsItem(String inputData) {
		JSONObject res = null;
		JSONObject decInputData = decodeInputData(inputData);
		String reason = null, message = null;
		Integer returnCode = -1;
		JSONObject outputData = new JSONObject();
		try {
			Long id = decInputData.getLong("id");
			PublicationDao dao = new PublicationDao();
			Publication pub = dao.get(id);
			if (pub != null) {
				outputData.put("number", pub.getIdentifier());
				outputData.put("title", pub.getTitle());
				outputData.put("subtitle", pub.getSubtitle());
				outputData.put("kind", pub.getKind());
				outputData.put("externalURL", pub.getExternalURL());
				outputData.put("imageURLs", new JSONArray(dao.getResourcePathWithKind(pub.getIdentifier(), PublicationDao.SRC_KIND.IMAGE, webappRealPath)));
				outputData.put("videoURLs", new JSONArray(dao.getResourcePathWithKind(pub.getIdentifier(), PublicationDao.SRC_KIND.VIDEO, webappRealPath)));
				outputData.put("audioURLs", new JSONArray(dao.getResourcePathWithKind(pub.getIdentifier(), PublicationDao.SRC_KIND.AUDIO, webappRealPath)));
				outputData.put("plainURLs", new JSONArray(dao.getResourcePathWithKind(pub.getIdentifier(), PublicationDao.SRC_KIND.PLAIN, webappRealPath)));
				returnCode = 1;
			} else {
				returnCode = 0;
				message = "Resource Not Found";
				reason = "No Information for This NewsItem with ID: ["+id+"]";
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
			reason = Internal_Error_Reason;
			message = Internal_Error_Message;
		} finally {
			HibernateUtil.currentSession().close();
			res = MobileServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
		return res;
	}
	/**
	 * mode: search|all
	 * URL example: =www.watchshow.com:8080/user/mobile/getWatchList?mode=search&page=true&item=name&query=input&orderby=id&ps=20&pn=1
	 * @param queryData
	 * @return
	 */
	protected JSONObject getWatchList(String queryData) {
	    JSONObject res = null;
	    JSONObject decQuery = decodeInputData(queryData);
	    JSONObject outputData = new JSONObject();
	    Integer returnCode = -1; //initially to be failed.
	    String reason = null, message = null;
	    try {
	    	WatchDao DAO = new WatchDao();
	        String mode = decQuery.getString("mode");
	        List<Watch>watches = null;
	        if (mode.equalsIgnoreCase("all")) {
	        	Boolean page = decQuery.getBoolean("page");
	        	if (page == true) {
	        		int count = decQuery.getInt("ps");
		        	Integer pageNumber = decQuery.getInt("pn");
		        	watches = DAO.listAll(pageNumber, count);
	        	} else {
	        		watches = DAO.listAll();
	        	}
	            returnCode = 1;
	        } else if (mode.equalsIgnoreCase("search")) {
		        String queryItem = decQuery.getString("sterm");
		        String query = decQuery.getString("query");
		        Boolean page = decQuery.getBoolean("page");
		        String orderby = "timestamp";
		        if (!decQuery.isNull("orderby")) {
		        	orderby = decQuery.getString("orderby");
		        }
		        Boolean asc = false;
		        if (!decQuery.isNull("asc")) {
		        	asc = decQuery.getBoolean("asc");
		        }
		        Criteria criteria = DAO.currentSession().createCriteria(Watch.class).add(Restrictions.like(queryItem, query));
	        	if (page == true) {
	        		int count = decQuery.getInt("ps");
		        	Integer pageNumber = decQuery.getInt("pn");
		        	OrderBy ob = new OrderBy();
		        	ob.add(asc ? Order.asc(orderby) : Order.desc(orderby));
		        	watches = DAO.list(criteria, new OrderBy(), pageNumber, count);
	        	} else {
	        		watches = DAO.list(criteria);
	        	}
		        returnCode = 1;
	        } else {
	        	throw new Exception();
	        }
	        //now, put all watches to json data
	        JSONArray ja = new JSONArray();
	        for (Watch watch : watches) {
	        	JSONObject jo = new JSONObject();
	        	jo.put("id", watch.getIdentifier().toString());
	        	jo.put("name", watch.getName());
	        	jo.put("simdesc", watch.getSimpleDescription());
	        	jo.put("desc", watch.getDescription());
	        	jo.put("price", watch.getPrice());
	        	jo.put("brand", watch.getBrand().getEngName() +":"+watch.getBrand().getChnName());
	        	jo.put("storeId", watch.getStore().getIdentifier().toString());
	        	List<String> pathset = DAO.getResourcePathWithKind(watch.getIdentifier(), WatchDao.SRC_KIND.IMAGE, webappRealPath);
	        	List<String> urls = new ArrayList<String>();
	        	for (String tpath : pathset) {
					String url = "/dl?client=mobile&path="+tpath;
					urls.add(url);
				}
	        	jo.put("thumbnails", new JSONArray(urls));
	        	ja.put(jo);
			}
	        outputData.put("totalNum", new Integer(watches.size()).toString());
	        outputData.put("results", ja);
	    }catch (Exception ex) {
	        ex.printStackTrace();
	        reason = Internal_Error_Reason;
	        message = Internal_Error_Message;
	    } finally {
	    	HibernateUtil.currentSession().close();
	    }
	    res = MobileServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
	    return res;
	}
	protected JSONObject getWatch(String queryData) {
		JSONObject res = null;
		JSONObject decQuery = decodeInputData(queryData);
		JSONObject outputData = new JSONObject();
		String reason = null, message = null;
		Integer returnCode = -1;
		try {
			Long id = decQuery.getLong("watchId");
			WatchDao wDAO = new WatchDao();
			Watch watch = wDAO.get(id);
			if (watch != null) {
				outputData.put("watchId", watch.getIdentifier());
				outputData.put("watchname", watch.getName());
				outputData.put("brand", watch.getBrand().getChnName() + ":" + watch.getBrand().getEngName());
				outputData.put("abstract", watch.getSimpleDescription());
				outputData.put("introduction", watch.getDescription());
				outputData.put("arch", watch.getArchitecture());
				outputData.put("function", watch.getFunction());
				outputData.put("material", watch.getMaterial());
				outputData.put("bandDesc", watch.getWatchband());
				outputData.put("movement", watch.getMovement());
				outputData.put("price", watch.getPrice());
				outputData.put("discount", watch.getDiscount());
				outputData.put("discount", watch.getDiscount());
				outputData.put("psn", watch.getProductSerialNumber());
				outputData.put("style", watch.getStyle());
				outputData.put("marketTime", watch.getMarketTime());
				outputData.put("totalCommentsNum", "\""+watch.getUserHistories().size()+"\"");
				{
					JSONObject rates = new JSONObject();
					rates.put("movement", watch.getRateMovement());
					rates.put("look", watch.getRateLook());
					rates.put("idea", watch.getRateIdea());
					rates.put("price", watch.getRatePrice());
					rates.put("prospect", watch.getRateProspect());
					outputData.put("rates", rates);
				}
				JSONObject attach = new JSONObject();
				attach.put("imageURLs", new JSONArray(MobileServiceHelper.composeDownloadableURLs(wDAO.getResourcePathWithKind(id, WatchDao.SRC_KIND.IMAGE, webappRealPath))));
				attach.put("videoURLs", new JSONArray(MobileServiceHelper.composeDownloadableURLs(wDAO.getResourcePathWithKind(id, WatchDao.SRC_KIND.VIDEO, webappRealPath))));
				attach.put("audioURLs", new JSONArray(MobileServiceHelper.composeDownloadableURLs(wDAO.getResourcePathWithKind(id, WatchDao.SRC_KIND.AUDIO, webappRealPath))));
				attach.put("plainURLs", new JSONArray(MobileServiceHelper.composeDownloadableURLs(wDAO.getResourcePathWithKind(id, WatchDao.SRC_KIND.PLAIN, webappRealPath))));
				outputData.put("attachments", attach);
				
				returnCode = 1;
			} else {
				returnCode = 0;
				reason = "No Information about This Watch with ID: [" + id +"]";
				message  = "Resource Not Found";
			}
		} catch (Exception e) {
			message = Internal_Error_Message;
			reason = Internal_Error_Reason;
		} finally {
			HibernateUtil.currentSession().close();
			res = MobileServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
		return res;
	}
	//TODO: page?
	protected JSONObject getComments(String queryData) {
		JSONObject response = null;
		JSONObject decQuery = decodeInputData(queryData);
		JSONObject outputData = new JSONObject();
		String reason = null, message = null;
		Integer returnCode = -1;
		try {
			Long watchId = decQuery.getLong("watchId");
			WatchDao DAO = new WatchDao();
			String mode = decQuery.getString("mode");
			Watch watch = DAO.get(watchId);
			if (watch != null) {
				List<UserHistory> comments = null;
				UserHistoryDao HDAO = new UserHistoryDao();
				String orderBy = "timestamp";
				if (!decQuery.isNull("orderby")) {
					orderBy = decQuery.getString("orderby");
				}
				Boolean asc = false;
				if (!decQuery.isNull("asc")) {
					asc = decQuery.getBoolean("asc");
				}
				Criteria criteria = HDAO.currentSession().createCriteria(UserHistory.class);
				criteria.createCriteria("viewedWatch").add(Restrictions.eq("identifier", watchId));
				criteria.add(Restrictions.like("action", MobileServiceHelper.ActionAttributesMap.get(MobileServiceHelper.ACTION.COMMENT)));
				criteria.addOrder(asc ? Order.asc(orderBy) : Order.desc(orderBy));
				if (!mode.equalsIgnoreCase("all")) { //"search"
					String term = "author";
					if (!decQuery.isNull("sterm")) {
						term = decQuery.getString("sterm");
					}
					if (term.equalsIgnoreCase("author")) {
						term = "userName";
					} 
					String search = decQuery.getString("sq");
					criteria.add(Restrictions.like(term, search));
				}

				Boolean page = decQuery.getBoolean("page");
				if (page) {
					int pageSize = decQuery.getInt("ps");
					int pageNumber = decQuery.getInt("pn");
					comments = HDAO.list(criteria, pageNumber, pageSize);
				} else {
					comments = HDAO.list(criteria);
				}
				JSONArray allCom = new JSONArray();
				for (UserHistory com : comments) {
					JSONObject jo = new JSONObject();
					jo.put("author", com.getOwner().getUserName());
					jo.put("subject", com.getSubject());
					jo.put("comment", com.getComment());
					jo.put("sumRate", com.getRateSum());
					jo.put("timestamp", com.getTimestamp());
					allCom.put(jo);
				}
				outputData.put("totalNum", new Integer(comments.size()).toString());
				outputData.put("comments", allCom);
			} else {
				outputData.put("totalNum", 0);
				outputData.put("comments", new JSONArray());
			}
			returnCode = 1;
		} catch (JSONException e) {
			reason = Internal_Error_Reason;
			message = Internal_Error_Message;
		} finally {
			HibernateUtil.currentSession().close();
			response = MobileServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
		return response;
	}
	/**
	 *	POST localhost:8080/Watchshow/user/mobile/addComment
	 * @param queryData
	 * @return
	 */
	protected JSONObject addComment(String queryData) {
		JSONObject response = null;
		JSONObject decQuery = decodeInputData(queryData);
		JSONObject outputData = new JSONObject();
		String reason = null, message = null;
		Integer returnCode = -1;

		UserHistoryDao DAO = new UserHistoryDao();
		Long commentId = new Long(-1);
		Long userId = -1L, watchId = -1L;
		try {
			userId = decQuery.getLong("authorId");
			watchId = decQuery.getJSONObject("comment").getLong("watchId");
		} catch (Exception ex) {
			
		}
		
		User user = new UserDao().get(userId);
		Watch watch = new WatchDao().get(watchId);
		if (user != null && watch!=null) {
			UserHistory userComment = new UserHistory();
			userComment.setAction(MobileServiceHelper.ActionAttributesMap.get(MobileServiceHelper.ACTION.COMMENT));
			try {
				JSONObject commentOb = decQuery.getJSONObject("comment");
				JSONObject ratesOb = commentOb.getJSONObject("rates");
				
				userComment.setRateIdea(ratesOb.isNull("ideaRate") ? null : ratesOb.getInt("ideaRate"));
				userComment.setRateLook(ratesOb.isNull("lookRate") ? null : ratesOb.getInt("lookRate"));
				userComment.setRateMovement(ratesOb.isNull("movementRate") ? null : ratesOb.getInt("movementRate"));
				userComment.setRatePrice(ratesOb.isNull("priceRate") ? null : ratesOb.getInt("priceRate"));
				userComment.setRateProspect(ratesOb.isNull("prospectRate") ? null : ratesOb.getInt("prospectRate"));
				userComment.setRateSum(ratesOb.isNull("sumRate") ? null : ratesOb.getInt("sumRate"));
				
				userComment.setSubject(commentOb.isNull("subject") ? null : commentOb.getString("subject"));
				userComment.setComment(commentOb.isNull("text") ? null : commentOb.getString("text"));
				userComment.setTimestamp(new Timestamp(System.currentTimeMillis()));
				userComment.setViewedWatch(watch);
				userComment.setOwner(user);
				JSONObject deviceOb = decQuery.getJSONObject("device");
				userComment.setDeviceMacAddress(deviceOb.isNull("deviceMAC") ? null : deviceOb.getString("deviceMAC"));
				userComment.setDeviceOS(deviceOb.isNull("deviceOS") ? null : deviceOb.getString("deviceOS"));
				userComment.setDeviceSN(deviceOb.isNull("deviceSN") ? null : deviceOb.getString("deviceSN"));
				userComment.setDeviceUUID(deviceOb.isNull("deviceGUID") ? null : deviceOb.getString("deviceGUID"));
				userComment.setIPAddress(deviceOb.isNull("deviceIP") ? null : deviceOb.getString("deviceIP"));
				JSONObject geoOb = decQuery.getJSONObject("location");
				userComment.setLongitude(geoOb.isNull("longitude") ? null : geoOb.getString("longitude"));
				userComment.setLatitude(geoOb.isNull("latitude") ? null : geoOb.getString("latitude"));
				userComment.setLocation(geoOb.isNull("location") ? null : geoOb.getString("location"));
				
			} catch (Exception ex) {
				returnCode = -1;
				reason = Internal_Error_Reason;
				message = Internal_Error_Message;
			}
			
			UserHistoryDao hisDAO = new UserHistoryDao();
			Session session = HibernateUtil.currentSession();
			Transaction tx = session.beginTransaction();
			try {
				hisDAO.saveOrUpdate(userComment);
				commentId = userComment.getIdentifier();
				tx.commit();
				returnCode = 1;
			} catch (Exception ex) {
				tx.rollback();
				returnCode = 0;
				reason = "Save or Update database Failed!";
				message = "Save or Update Comment[" +userComment.getSubject()+ "] Failed!"; 
			} finally {
				session.close();
			}
		} else {
			returnCode = 0;
			reason = "Input Data is not Vailable!";
			message = "AddComment Failed, because target Watch [id="+watchId+"]"+"or User [id="+userId+"] does not exist";
		}
		//prepare output content
		JSONArray attachInfos = new JSONArray();
		UserHistory addedComment = DAO.get(commentId);
		if (addedComment != null) {
			try {
				outputData.put("author", addedComment.getOwner().getUserName());
				outputData.put("id", addedComment.getIdentifier().toString());
				outputData.put("parentId", "-1");
				outputData.put("watchId", addedComment.getViewedWatch());
				outputData.put("subject", addedComment.getSubject());
				outputData.put("text", addedComment.getComment());
				outputData.put("attachments", attachInfos);
			} catch(Exception ex) {
				ex.printStackTrace();
				reason = Internal_Error_Reason;
				message = Internal_Error_Message;
			}
			
		}
		response = MobileServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		return response;
	}
	
	protected JSONObject getComment(String queryData) {
		JSONObject response = null;
		JSONObject decInput = decodeInputData(queryData);
		JSONObject outputData = new JSONObject();
		Integer returnCode = -1; String reason = null, message = null;
		try {
			Long commentId = decInput.getLong("commentId");
			UserHistoryDao DAO = new UserHistoryDao();
			UserHistory comment = DAO.get(commentId);
			if (comment != null) {
				outputData.put("commentId", comment.getIdentifier());
				outputData.put("title", comment.getComment());
				JSONObject loc = new JSONObject();
				loc.put("longitude", comment.getLongitude());
				loc.put("latitude", comment.getLatitude());
				outputData.put("location", loc);
				JSONObject device = new JSONObject();
				device.put("UUID", comment.getDeviceUUID());
				device.put("OS", comment.getDeviceOS());
				device.put("SN", comment.getDeviceSN());
				device.put("IP", comment.getIPAddress());
				outputData.put("device", device);
				JSONObject rates = new JSONObject();
				rates.put("sumRate", comment.getRateSum());
				rates.put("ideaRate", comment.getRateIdea());
				rates.put("lookRate", comment.getRateLook());
				rates.put("movementRate", comment.getRateMovement());
				rates.put("priceRate", comment.getRatePrice());
				rates.put("prospectRate", comment.getRateProspect());
				outputData.put("rates", rates);
				returnCode = 1;
			} else {
				returnCode = 0;
				reason = "Could not Find Request Comment ["+commentId+"]";
				message = "Comment Not Found";
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			reason = Internal_Error_Reason;
			message = Internal_Error_Message;
		} finally {
			HibernateUtil.currentSession().close();
			response = PlatformServiceHelper.sharedResponseTemplate(returnCode, reason, message, outputData);
		}
		
		return response;
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

}
