package com.watchshow.platform.webservlet;

import java.io.IOException;
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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.watchshow.common.util.ConstantsProvider;
import com.watchshow.platform.dao.PlatformAdministratorDao;
import com.watchshow.platform.domain.PlatformAdministrator;
import com.watchshow.platform.helper.PlatformServiceHelper;
import com.watchshow.platform.service.PlatformServiceContext;

/**
 * Servlet implementation class PlatformApplyRequestDataSource
 */
@WebServlet("/platform/admin/*")
public class PlatformAdminActivitiesHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final Integer DEFAULT_COOKIE_LIFETIME = 60*60; //1 hour
    
    private static final String activeAdminAttribute = "active_platform_admin";
    private static final String activeAdminCookieId  = "active_platform_admin_name";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlatformAdminActivitiesHandler() {
        super();
    }

	@SuppressWarnings("unchecked")
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		request.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		String serviceName = getRequestServiceName(request);
		String inputData = null;
		if (ServletFileUpload.isMultipartContent(request)) {
			ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
			List<FileItem> items;
			try {
				items = uploadHandler.parseRequest(request);
				JSONObject formFields = new JSONObject();
				JSONArray  uploadFileItems = new JSONArray();
				for (FileItem item : items) {
					if (item.isFormField()) {
						String paramKey = item.getFieldName();
						String paramValue = new String(item.getString().getBytes("ISO8859-1"), "UTF-8");
						System.out.println(paramKey + " : " + paramValue);
						formFields.put(paramKey, paramValue);
					} else {
						uploadFileItems.put(item);
					}
				}
				JSONObject jsonInput = new JSONObject();
				jsonInput.put("fields", formFields);
				jsonInput.put("fileitem", uploadFileItems);
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
		String realPath = request.getServletContext().getRealPath("/");
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		PlatformAdministrator currentAdmin = (PlatformAdministrator) request.getSession().getAttribute(activeAdminAttribute);
		JSONObject responseData = null;
		if (currentAdmin == null) {
			//for register, login
			if (serviceName.equalsIgnoreCase("login") || serviceName.equalsIgnoreCase("register")) {
				responseData = PlatformServiceContext.createServiceContext(currentAdmin, serviceName, realPath, basePath).execute(inputData);
			} 
			//for login and logout, we need to handle extra things at this time
			if (serviceName.equalsIgnoreCase("login")) {
				try {
					Boolean successful = responseData.getJSONObject("outputData").getBoolean("successful");
					if (successful) {
						String adminname = responseData.getJSONObject("outputData").getString("adminname");
						PlatformAdministrator admin = new PlatformAdministratorDao().getAdminByName(adminname);
						request.getSession().setAttribute(activeAdminAttribute, admin);
						request.getSession().setAttribute(activeAdminCookieId, adminname);
						Cookie nameCookie = new Cookie(activeAdminCookieId, adminname);
						nameCookie.setMaxAge(DEFAULT_COOKIE_LIFETIME);
						//sendRedirect
						String homeURL = basePath+"/PlatformAdminHome.jsp";
						response.sendRedirect(homeURL);
					} else {
						response.setHeader("refresh","0;URL="+basePath+"/PlatformAdminLogin.jsp");
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return;
			} else {
				responseData = PlatformServiceHelper.sharedResponseTemplate(-1, "Required to Login", "Request from unlogined User", new JSONObject());
			}
		} else {
			responseData = PlatformServiceContext.createServiceContext(currentAdmin, serviceName, realPath, basePath).execute(inputData);
			if (serviceName.equalsIgnoreCase("logout")) {
				request.getSession().invalidate();
				response.setHeader("refresh","0;URL="+basePath+"/PlatformAdminLogin.jsp");
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
