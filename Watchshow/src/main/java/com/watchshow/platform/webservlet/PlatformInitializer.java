package com.watchshow.platform.webservlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet implementation class Log4jInitializer
 */
@WebServlet("/PlatformInitializer")
public class PlatformInitializer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(PlatformInitializer.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlatformInitializer() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		String prefix = config.getServletContext().getRealPath("/");
		String file = config.getInitParameter("log4j");
		String filePath = prefix + file;
		Properties props = new Properties();
		try {
			FileInputStream istream = new FileInputStream(filePath);
			props.load(istream);
			istream.close();
			String logfilePath = props.getProperty("log4j.appender.FILE.File");
			logfilePath = logfilePath.replace('/', File.separatorChar);
			String logFile = prefix + logfilePath;// 设置路径
			props.setProperty("log4j.appender.FILE.File", logFile);
			PropertyConfigurator.configure(props);// 装入log4j配置信息
			//create file if it does not exists
			//FileManagerUtil.createFile(logFile);
			//logger.info(logFile);
		} catch (IOException e) {
			logger.debug("Could not read configuration file [" + filePath
					+ "].");
			logger.debug("Ignoring configuration file [" + filePath + "].");
			return;
		}
	}

}
