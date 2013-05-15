package com.watchshow.platform.webservlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.watchshow.common.util.ConstantsProvider;
import com.watchshow.platform.service.MobileServiceContext;

/**
 * Servlet implementation class MobileUserRequestHandler
 */
@WebServlet("/mobileuser/services/*")
public class MobileUserActivitiesHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final String UTF8_STRINGTYPE = "text/html;charset=utf-8";
	private static final String UTF8_JSONTYPE  = "application/json;charset=utf-8";
	private static final Integer DEFAULT_COOKIE_LIFETIME = 60*60;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileUserActivitiesHandler() {
        super();
    }
    
    /**
	 * @see HttpServlet#service(ServletRequest request, ServletResponse response);
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Set character encoding
		response.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		request.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		//Now we would invoke explicit services.
		String serviceName = getRequestServiceName(request);
		String requestMethod = request.getMethod();
		
		String inputData = null;
		
		if (requestMethod.equalsIgnoreCase("GET")) {
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
			
		} else if (requestMethod.equalsIgnoreCase("POST")) {
			
			//TODO: Get input data and then pass it to corresponding service.
			int length = request.getContentLength();
			String type = request.getContentType();
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
					inputData = jo.toString();
				} catch (JSONException e) {
					e.printStackTrace();
				} 
			} else {
				inputData = new String(buffer, "utf-8");
			}
		} else if (requestMethod.equalsIgnoreCase("PUT")){
			
		} else if (requestMethod.equalsIgnoreCase("DELETE")) {
			
		} else {
			System.out.println("Other Method <"+requestMethod+"> is not supported now!");
		}
		String hostServer = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String realPath = request.getServletContext().getRealPath("/");
		JSONObject responseData = MobileServiceContext.createServiceContext(serviceName, hostServer, realPath).execute(inputData);
		//cookie things goes here
		if (serviceName.equalsIgnoreCase("login")) {
			try {
				Boolean successful = responseData.getJSONObject("outputData").getBoolean("successful");
				if (successful) {
					String adminname = responseData.getJSONObject("outputData").getString("username");
					Long adminId = responseData.getJSONObject("outputData").getLong("userId");
					String password = responseData.getJSONObject("outputData").getString("password");
					request.getSession().setAttribute("username",adminname);
					request.getSession().setAttribute("userId", adminId);
					request.getSession().setAttribute("password", password);
					Cookie nameCookie = new Cookie("username", adminname);
					Cookie passwordCookie = new Cookie("password", password);
					Cookie idCookie = new Cookie("userId", adminId.toString());
					nameCookie.setMaxAge(DEFAULT_COOKIE_LIFETIME);
					passwordCookie.setMaxAge(DEFAULT_COOKIE_LIFETIME);
					idCookie.setMaxAge(DEFAULT_COOKIE_LIFETIME);
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
