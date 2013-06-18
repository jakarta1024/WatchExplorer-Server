package application.helper;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class ResponseRender {

	private static final Logger logger = Logger.getLogger(ResponseRender.class);
	// header 常量定义
	private static final String ENCODING_PREFIX = "encoding";
	private static final String NOCACHE_PREFIX = "no-cache";
	private static final String ENCODING_DEFAULT = "UTF-8";
	private static final boolean NOCACHE_DEFAULT = true;
	// "application/json" 是最规范的写法，但是旧浏览器支持的不好，所以暂用老写法，各种浏览器都支持
	private static final String JSON_CONTENT_TYPE = "text/javascript";
	private static int Expires = 100;

	/**
	 * 直接输出JSON.
	 * 
	 * @param string
	 *            json字符串.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(HttpServletResponse response,
			final String string, final String... headers) {
		// render(response, "text/html", string, headers);
		// TODO: 用这种方式时，用IE请求接口时会提示下载文件
		render(response, JSON_CONTENT_TYPE, string, headers);
	}

	/**
	 * 直接输出内容的简便函数.
	 * 
	 * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain",
	 * "hello", "no-cache:false"); render("text/plain", "hello", "encoding:GBK",
	 * "no-cache:false");
	 * 
	 * @param headers
	 *            可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true .
	 */
	private static void render(HttpServletResponse response,
			final String contentType, final String content,
			final String... headers) {
		try {
			// 分析headers参数
			String encoding = ENCODING_DEFAULT;
			boolean noCache = NOCACHE_DEFAULT;
			for (String header : headers) {
				String headerName = StringUtils.substringBefore(header, ":");
				String headerValue = StringUtils.substringAfter(header, ":");

				if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
					encoding = headerValue;
				} else if (StringUtils.equalsIgnoreCase(headerName,
						NOCACHE_PREFIX)) {
					noCache = Boolean.parseBoolean(headerValue);
				} else
					throw new IllegalArgumentException(headerName
							+ "不是一个合法的header类型");
			}

			// 设置headers参数
			String fullContentType = contentType + ";charset=" + encoding;
			response.setContentType(fullContentType);
			if (noCache) {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", Expires);
			}

			response.getWriter().write(content);

		} catch (IOException e) {
			logger.error("render==" + e.getMessage(), e);
		}
	}
}
