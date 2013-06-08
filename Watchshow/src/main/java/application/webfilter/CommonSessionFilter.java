package application.webfilter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class PlatformAdminSessionFilter
 */
@WebFilter("/*")
public class CommonSessionFilter implements Filter {

	 /**
     * Default constructor. 
     */
    public CommonSessionFilter() {
    
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// place your code here
		HttpServletRequest req = (HttpServletRequest) request;
		String servletPath = req.getServletPath();
		System.out.println("common webfilter meets serlvet path:" + servletPath);
		if (servletPath.equals("/PlatformAdminHome.jsp")) {
			String username = (String) req.getSession().getAttribute("active_platform_admin_name");
			if(username!=null) {
	            chain.doFilter(request, response);
	            System.out.println(username + "---- logined : CommonSessionFilter");            
	        } else {
	            req.getRequestDispatcher("PlatformAdminLogin.jsp").forward(request, response);
	        }
		} else if (servletPath.equals("/PlatformAdminLogin.jsp")) {
			String username = (String) req.getSession().getAttribute("active_platform_admin_name");
			if(username!=null) {
				req.getRequestDispatcher("PlatformAdminHome.jsp").forward(request, response);
	            System.out.println(username + "---- logined : CommonSessionFilter");            
	        } else {
	        	chain.doFilter(request, response);
	        }
		} else if (servletPath.equals("/StoreAdminHome.jsp")) {
			String username = (String) req.getSession().getAttribute("active_store_admin_name");
			if(username!=null) {
	            chain.doFilter(request, response);
	            System.out.println(username + "---- logined : CommonSessionFilter");            
	        } else {
	            req.getRequestDispatcher("StoreWelcome.jsp").forward(request, response);
	        }
		} else if (servletPath.equals("/StoreWelcome.jsp") || servletPath.equals("//StoreWelcome.jsp")) {
			String username = (String) req.getSession().getAttribute("active_store_admin_name");
			if (username != null) {
				 req.getRequestDispatcher("StoreAdminHome.jsp").forward(request, response);
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}
	

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
