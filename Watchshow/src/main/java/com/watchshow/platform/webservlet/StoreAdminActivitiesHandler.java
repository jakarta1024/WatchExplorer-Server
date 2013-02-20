package com.watchshow.platform.webservlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
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

import com.watchshow.common.util.ConstantsProvider;
import com.watchshow.common.util.RemoteClientInfoUtil;
import com.watchshow.platform.dao.StoreAdministratorDao;
import com.watchshow.platform.domain.StoreAdministrator;
import com.watchshow.platform.service.StoreAdminActivitiesService;
import com.watchshow.platform.service.StoreAdminHelper;

/**
 * Servlet implementation class StoreAdminWatchListHandler
 */
@WebServlet(urlPatterns = { "/store/admin/*" },
			initParams = { @WebInitParam(name = "uploadRootPath", value = "", description = "") })
public class StoreAdminActivitiesHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String hostRealPath = null;
	private String basePath = null;
	private static final Integer DEFAULT_COOKIE_LIFETIME = 60*60; //1 hour
	
	private static final String activeAdminAttrKey = "active_store_admin";
	private static final String activeAdminCookieId = "active_store_admin_name";
	
	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		request.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		this.basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		String serviceName = getRequestServiceName(request);
		String inputData = null;
		List<FileItem> uploadFiles = null;
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
				uploadFiles = uploadFileItems;
				inputData = jsonInput.toString();
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			String requestMethod = request.getMethod();
			if (requestMethod.equalsIgnoreCase("GET") || requestMethod.equals("POST")) {
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
					}
				}
				inputData = json.toString();
			} else {
				response.getWriter().println("HTTP Method ["+requestMethod+"] does not be supported at this release!");
			}
		}
		
		StoreAdministrator currentAdmin = (StoreAdministrator) request.getSession().getAttribute(activeAdminAttrKey);
		JSONObject responseData = null;
		String ip = RemoteClientInfoUtil.getRealRemoteIPAddress(request);
		if (currentAdmin == null) {
			//for register, login
			if (serviceName.equalsIgnoreCase("login") || serviceName.equalsIgnoreCase("approve") || serviceName.equalsIgnoreCase("register")) {
				StoreAdminActivitiesService service = StoreAdminActivitiesService.getService(currentAdmin, serviceName, hostRealPath, basePath);
				service.setIPAddress(ip);
				responseData = service.execute(inputData, uploadFiles);
			} 
			//For login and logout, we need to handle extra things at this time
			if (serviceName.equalsIgnoreCase("login")) {
				try {
					Boolean successful = responseData.getJSONObject("outputData").getBoolean("successful");
					if (successful) {
						Long adminId = responseData.getJSONObject("outputData").getLong("adminId");
						StoreAdministrator admin = new StoreAdministratorDao().get(adminId);
						request.getSession().setAttribute(activeAdminAttrKey, admin);
						request.getSession().setAttribute(activeAdminCookieId, admin.getLoginName());
						Cookie nameCookie = new Cookie(activeAdminCookieId, admin.getLoginName());
						nameCookie.setMaxAge(DEFAULT_COOKIE_LIFETIME);
						//sendRedirect
						String homeURL = basePath+"/StoreAdminHome.jsp";
						response.sendRedirect(homeURL);
					} else {
						response.setHeader("refresh","0;URL="+basePath+"/StoreWelcome.jsp");
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return;
			} else {
				responseData = StoreAdminHelper.sharedResponseTemplate(-1, "Required to Login", "Request from unlogined User", new JSONObject());
			}
		} else {
			StoreAdminActivitiesService service = StoreAdminActivitiesService.getService(currentAdmin, serviceName, hostRealPath, basePath);
			service.setIPAddress(ip);
			responseData = service.execute(inputData, uploadFiles);
			if (serviceName.equalsIgnoreCase("logout")) {
				request.getSession().invalidate();
				response.setHeader("refresh","0;URL="+basePath+"/StoreWelcome.jsp");
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
				response.setHeader("refresh","0;URL="+basePath+"/StoreAdminHome.jsp"+anchor);
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
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.hostRealPath = config.getServletContext().getRealPath("/");
	}
}
