package application.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import application.dao.PlatformAdministratorDao;
import application.dao.StoreAdministratorDao;
import application.helper.PlatformServiceHelper;
import application.helper.StoreServiceHelper;
import application.model.PlatformAdministrator;
import application.model.StoreAdministrator;
import application.model.User;
import application.service.ServiceFactory;
import application.service.StoreServiceContext;

import commons.util.ConstantsProvider;
import commons.util.RemoteClientInfoUtil;

/**
 * Servlet implementation class WebServicesDispatcher
 */
@WebServlet("/services/*")
public class WebServicesDispatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unused")
	private static final String UTF8_STRINGTYPE = "text/html;charset=utf-8";
	private static final String UTF8_JSONTYPE  = "application/json;charset=utf-8";
	private static final Integer DEFAULT_COOKIE_LIFETIME = 60*60;
	
	private static final String ActivePlatformAdminKey = "active_platform_admin";
	private static final String ActiveStoreAdminKey = "active_store_admin";
	private static final String ActiveUserKey = "active_user_admin";
	
	@SuppressWarnings("unused")
	private static final String ActiveUserCookieIdKey = "active_user_cookie_id";
	private static final String ActivePlatformAdminCookieIdKey = "active_platform_admin_cookie_id";
	private static final String ActiveStoreAdminCookieIdKey = "active_store_admin_cookie_id";
	//For store parameters and upload file items
	private String requestParameters = null;
	private List<FileItem> uploadFileItems = null;
		
	private Map<String, String> serviceIdentifierMap = null;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebServicesDispatcher() {
        super();
        serviceIdentifierMap = new HashMap<String, String>();
        serviceIdentifierMap.put("mobile", ServiceFactory.MobileServiceContextIdentifier);
        serviceIdentifierMap.put("store", ServiceFactory.StoreServiceContextIdentifier);
        serviceIdentifierMap.put("platform", ServiceFactory.PlatformServiceContextIdentifier);
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		request.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		//Now we would invoke explicit services.
		String serviceName = getRequestServiceName(request);
		String serviceContextIdentifier = null;
		try {
			serviceContextIdentifier = getRequestServiceContextIdentifier(request);
			boolean ok = prepareRequestData(request);
			if (ok)
				doResponse(request, response, serviceContextIdentifier, serviceName);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void doResponse(HttpServletRequest request, HttpServletResponse response, String serviceContextIdentifier, String serviceName ) throws IOException {
		
		String hostServer = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String realPath = request.getServletContext().getRealPath("/");
		JSONObject responseData = null;
		
		if (serviceContextIdentifier.equals(ServiceFactory.MobileServiceContextIdentifier)) {
			User user = (User) request.getSession().getAttribute(ActiveUserKey);
			responseData = ServiceFactory.getServiceContext(user, serviceContextIdentifier,serviceName, hostServer, realPath).execute(this.requestParameters, this.uploadFileItems);
			if (serviceName.equalsIgnoreCase("login")) {
				try {
					//Do something about cookie and session 
					Boolean succeed = responseData.getJSONObject("outputData").getBoolean("succeed");
					if (succeed) {
						String adminname = responseData.getJSONObject("outputData").getString("username");
						request.getSession().setAttribute("username",adminname);
						Cookie nameCookie = new Cookie("username", adminname);
						nameCookie.setMaxAge(DEFAULT_COOKIE_LIFETIME);
						//sendRedirect
						String homeURL = hostServer+"/PlatformAdminHome.jsp";
						response.sendRedirect(homeURL);
					} else {
						response.setHeader("refresh","0;URL="+hostServer+"/PlatformAdminLogin.jsp");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (serviceName.equalsIgnoreCase("logout")) {
				request.getSession().invalidate();
			}
			//for now we do not do encrypt things.
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			try {
				System.out.println(responseData.toString());
				response.getWriter().print(responseData.toString());
				response.getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else if (serviceContextIdentifier.equals(ServiceFactory.StoreServiceContextIdentifier)) {
			
			StoreAdministrator currentAdmin = (StoreAdministrator) request.getSession().getAttribute(ActiveStoreAdminKey);
			String ip = RemoteClientInfoUtil.getRealRemoteIPAddress(request);
			if (currentAdmin == null) {
				//for register, login
				if (serviceName.equalsIgnoreCase("login") || serviceName.equalsIgnoreCase("approve") || serviceName.equalsIgnoreCase("register")) {
					StoreServiceContext service = (StoreServiceContext) ServiceFactory.getServiceContext(currentAdmin, serviceContextIdentifier,serviceName, hostServer, realPath);
					service.setIPAddress(ip);
					responseData = service.execute(this.requestParameters, this.uploadFileItems);
				} 
				//For login and logout, we need to handle extra things at this time
				if (serviceName.equalsIgnoreCase("login")) {
					try {
						Boolean successful = responseData.getJSONObject("outputData").getBoolean("successful");
						if (successful) {
							Long adminId = responseData.getJSONObject("outputData").getLong("adminId");
							StoreAdministrator admin = new StoreAdministratorDao().get(adminId);
							request.getSession().setAttribute(ActiveStoreAdminKey, admin);
							request.getSession().setAttribute(ActiveStoreAdminCookieIdKey, admin.getLoginName());
							Cookie nameCookie = new Cookie(ActiveStoreAdminCookieIdKey, admin.getLoginName());
							nameCookie.setMaxAge(DEFAULT_COOKIE_LIFETIME);
							//sendRedirect
							String homeURL = hostServer+"/StoreAdminHome.jsp";
							response.sendRedirect(homeURL);
						} else {
							response.setHeader("refresh","0;URL="+hostServer+"/StoreWelcome.jsp");
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return;
				} else {
					responseData = StoreServiceHelper.sharedResponseTemplate(-1, "Required to Login", "Request from unlogined User", new JSONObject());
				}
			} else {
				StoreServiceContext service = (StoreServiceContext) ServiceFactory.getServiceContext(currentAdmin, serviceName, realPath, hostServer);
				service.setIPAddress(ip);
				responseData = service.execute(this.requestParameters, this.uploadFileItems);
				if (serviceName.equalsIgnoreCase("logout")) {
					request.getSession().invalidate();
					response.setHeader("refresh","0;URL="+hostServer+"/StoreWelcome.jsp");
					return;
				} 
				if (request.getMethod().equalsIgnoreCase("POST")) {
					String anchor = "";
					if (!responseData.isNull("anchorTag")) {
						try {
							anchor = responseData.getString("anchorTag");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					response.setHeader("refresh","0;URL="+hostServer+"/StoreAdminHome.jsp"+anchor);
				} else {
					//for now we do not do encrypt things.
					response.setContentType("application/json");
					response.setHeader("Cache-Control", "no-store");
					try {
						response.getWriter().print(responseData.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (serviceContextIdentifier.equals(ServiceFactory.PlatformServiceContextIdentifier)) {
			PlatformAdministrator currentAdmin = (PlatformAdministrator) request.getSession().getAttribute(ActivePlatformAdminKey);
			if (currentAdmin == null) {
				//for register, login
				if (serviceName.equalsIgnoreCase("login") || serviceName.equalsIgnoreCase("register")) {
					responseData = ServiceFactory.getServiceContext(currentAdmin, serviceName, realPath, hostServer).execute(this.requestParameters, this.uploadFileItems);
				} 
				//for login and logout, we need to handle extra things at this time
				if (serviceName.equalsIgnoreCase("login")) {
					try {
						Boolean successful = responseData.getJSONObject("outputData").getBoolean("successful");
						if (successful) {
							String adminname = responseData.getJSONObject("outputData").getString("adminname");
							PlatformAdministrator admin = new PlatformAdministratorDao().getAdminByName(adminname);
							request.getSession().setAttribute(ActivePlatformAdminKey, admin);
							Cookie nameCookie = new Cookie(ActivePlatformAdminCookieIdKey, adminname);
							nameCookie.setMaxAge(DEFAULT_COOKIE_LIFETIME);
							//sendRedirect
							String homeURL = hostServer+"/PlatformAdminHome.jsp";
							response.sendRedirect(homeURL);
						} else {
							response.setHeader("refresh","0;URL="+hostServer+"/PlatformAdminLogin.jsp");
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return;
				} else {
					responseData = PlatformServiceHelper.sharedResponseTemplate(-1, "Required to Login", "Request from unlogined User", new JSONObject());
				}
			} else {
				try {
					responseData = ServiceFactory.getServiceContext(currentAdmin, serviceName, realPath, hostServer).execute(this.requestParameters, this.uploadFileItems);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (serviceName.equalsIgnoreCase("logout")) {
					request.getSession().invalidate();
					response.setHeader("refresh","0;URL="+hostServer+"/PlatformAdminLogin.jsp");
					return;
				}
			}
			//For now we do not do encrypt things.
			try {
				responseData = responseData.getJSONObject("outputData");
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-store");
				response.getWriter().print(responseData.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} else {
			
		}
	}
	
	private String getRequestServiceContextIdentifier(HttpServletRequest request) throws Exception {
		String serviceCtxtIdentifier = null;
		String pathInfo = request.getPathInfo().substring(1); //REmove first slash
	
		if (pathInfo != null && !pathInfo.isEmpty()) {
			String pieces[] = pathInfo.split("/");
			if (pieces.length >= 2) {
				int pos = pieces.length - 1;
				serviceCtxtIdentifier = pieces[pos - 1]; //start at 0
			} else {
				throw new Exception("URL path info has some issues!");
			}
		}
		if (serviceCtxtIdentifier != null) {
			serviceCtxtIdentifier = serviceIdentifierMap.get(serviceCtxtIdentifier);
		}
		return serviceCtxtIdentifier;
	}

	@SuppressWarnings("unchecked")
	private boolean prepareRequestData(HttpServletRequest request) throws IOException {
		boolean returnValue = true;
		if (ServletFileUpload.isMultipartContent(request)) {
			ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
			List<FileItem> items;
			try {
				items = uploadHandler.parseRequest(request);
				JSONObject formFields = new JSONObject();
				List<FileItem>  uploadFileItems = new ArrayList<FileItem>();
				for (FileItem item : items) {
					if (item.isFormField()) {
						String paramKey = item.getFieldName();
						String paramValue = new String(item.getString().getBytes("ISO8859-1"), "UTF-8");
						formFields.put(paramKey, paramValue);
					} else {
						uploadFileItems.add(item);
					}
				}
				JSONObject jsonInput = new JSONObject();
				jsonInput.put("fields", formFields);
				this.uploadFileItems = uploadFileItems;
				this.requestParameters = jsonInput.toString();
			} catch (FileUploadException e) {
				e.printStackTrace();
				returnValue = false;
			} catch (JSONException e) {
				e.printStackTrace();
				returnValue = false;
			}
		} else {
			String requestMethod = request.getMethod();
			String type = request.getContentType();
			if (requestMethod.equalsIgnoreCase("GET") || type.equalsIgnoreCase("application/x-www-form-urlencoded")) {
				Map<String, String[]> maps = request.getParameterMap();
				java.util.Iterator<Entry<String, String[]>> it = maps.entrySet().iterator();
				JSONObject json = new JSONObject();
				while (it.hasNext()) {
					Map.Entry<String, String[]> entry = it.next();
					String key = entry.getKey();
					String[] value = entry.getValue();
					try {
						json.put(key, value[0]);
					} catch (JSONException e) {
						e.printStackTrace();
						returnValue = false;
					}
				}
				this.requestParameters = json.toString();
			} else if (requestMethod.equalsIgnoreCase("POST")) {
				int length = request.getContentLength();
				byte buffer[] = new byte[length];
				InputStream ins = request.getInputStream();
				int total = 0;
				int once  = 0;
				while ((total < length) && (once >= 0)) {
					once = ins.read(buffer, total, length);
					total += once;
				}
				if (type.equals(UTF8_JSONTYPE)) {
					String rawData = new String(buffer, "utf-8");
					try {
						JSONObject jo = new JSONObject(rawData);
						this.requestParameters = jo.toString();
					} catch (JSONException e) {
						e.printStackTrace();
						returnValue = false;
					} 
				} else {
					this.requestParameters = new String(buffer, "utf-8");
				}
			} else {
				returnValue = false;
			}
		}
		return returnValue;
	}
	private String getRequestServiceName (HttpServletRequest request) {
		String serviceName = null;
		String pathInfo = request.getPathInfo();
		if (pathInfo != null && !pathInfo.isEmpty()) {
			int i = pathInfo.lastIndexOf("/");
			serviceName = pathInfo.substring(i+1, pathInfo.length());
		}
		if (serviceName == null) {
			String contextPath = request.getContextPath();
			System.out.println(this.getClass().toString() + contextPath);
		}
		System.out.println("service name: " + serviceName);
		return serviceName;
	}

}
