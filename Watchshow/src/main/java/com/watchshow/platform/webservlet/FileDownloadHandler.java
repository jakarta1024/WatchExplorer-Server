package com.watchshow.platform.webservlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.watchshow.common.util.ConstantsProvider;
import com.watchshow.platform.dao.PublicationDao;
import com.watchshow.platform.dao.WatchBrandDao;
import com.watchshow.platform.dao.WatchDao;
import com.watchshow.platform.dao.WatchStoreDao;
import com.watchshow.platform.domain.Publication;
import com.watchshow.platform.domain.Watch;
import com.watchshow.platform.domain.WatchBrand;
import com.watchshow.platform.domain.WatchStore;
import com.watchshow.platform.helper.ServerResourcePathHelper;

/**
 * Servlet implementation class FileDownloadHandler
 */
@WebServlet("/dl")
public class FileDownloadHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// parameters will be used to determine download contents.
	private static final String paramClientType = "client"; // optional "mobile" | "browser"
	private static final String paramSourcePath = "path"; // required
	private static final String paramMimeType = "mimetype"; // optional
	private static final String paramEncoding = "enc"; // optional
	private static final String paramThumbnail = "thumb";
	private static final String DEFAULT_MIMETYPE = "application/zip";
	private static final String DEFAULT_ENCODING = "GBK";
	
	private String client = "mobile";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileDownloadHandler() {
		super();
	}
	/**
	 * @see HttpServlet#service(ServletRequest request, ServletResponse response);
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		request.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		super.service(request, response);
		
	}
	
	protected String makeReachablePathFromRequestParameters(HttpServletRequest request) {
		
		String srcOwner = request.getParameter("src_owner");
		String srcName = request.getParameter("name");
		try {
			srcName = java.net.URLDecoder.decode(srcName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String srcType = request.getParameter("src_type");
		String classFolder = "Others";
		if (srcType.equalsIgnoreCase("image")) {
			classFolder = "images";
		} else if (srcType.equalsIgnoreCase("audio")) {
			classFolder = "audios";
		} else if (srcType.equalsIgnoreCase("video")) {
			classFolder = "videos";
		} else {
			classFolder = "others";
		}
		String srcPath = "";
		if (srcOwner.equalsIgnoreCase("watch")) {
			 Long watchId = Long.parseLong(request.getParameter("id"));
			 WatchDao dao = new WatchDao();
			 Watch watch = dao.get(watchId);
			 if (watch != null) {
				 srcPath = watch.getDescResourceURL();
			 }
		} else if (srcOwner.equalsIgnoreCase("store")) {
			 Long storeId = Long.parseLong(request.getParameter("id")); 
			 WatchStoreDao dao = new WatchStoreDao();
			 WatchStore store = dao.get(storeId);
			 if (store != null) {
				 srcPath = store.getDescResourceURL();
			 }
		} else if (srcOwner.equalsIgnoreCase("brand")) {
			 Long brandId = Long.parseLong(request.getParameter("id"));
			 WatchBrandDao dao = new WatchBrandDao();
			 WatchBrand brand = dao.get(brandId);
			 if (brand != null) {
				 srcPath = brand.getIntroResourcesURL();
			 }
		} else if (srcOwner.equalsIgnoreCase("news")) {
			 Long newsId = Long.parseLong(request.getParameter("id"));
			 PublicationDao dao = new PublicationDao();
			 Publication pub = dao.get(newsId);
			 if (pub != null) {
				 srcPath = pub.getResourcesURL();
			 }
		} else {
			return null;
		}
		String reachablePath = getServletContext().getRealPath("/") + srcPath + File.separator + classFolder + File.separator + srcName;
		System.out.println("file path: " + reachablePath);
		File src = new File(reachablePath);
		if (src.exists()) {
			return reachablePath;
		} else {
			return null;
		}
	}
	
	private String retrieveRealServerFilePath(String paramPath) {
		String realServerFilePath = null;
		try {
			java.net.URLDecoder.decode(paramPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//conversion start:
		
		
		//end
		
		
		realServerFilePath = getServletContext().getRealPath("/") + paramPath;
		if (new File(realServerFilePath).exists() == false) {
			//TODO: retrieve file
			System.out.println("request download url: " + realServerFilePath);
		}
		return realServerFilePath;
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ct = request.getParameter(paramClientType);
		if (ct != null && !ct.isEmpty()) {
			client = ct;
		}
		System.out.println("Requesting client: "+client);
		// 服务器相对路径
		String path = new String(request.getParameter(paramSourcePath).getBytes("ISO8859-1"),"UTF-8");
		System.out.println("download url: "+ path);
		String mimetype = request.getParameter(paramMimeType);
		if (mimetype == null || mimetype.isEmpty()) {
			mimetype = DEFAULT_MIMETYPE;
			if (path != null && !path.isEmpty()) {
				String filename = path.substring(path.lastIndexOf('/'), path.length());
				mimetype = ServerResourcePathHelper.getMimeType(filename);
			} 
		}
		String encoding = request.getParameter(paramEncoding);
		if (encoding == null|| !encoding.isEmpty()) {
			encoding = DEFAULT_ENCODING;
		}
		encoding = "charset=" + encoding;
		
		// 服务器绝对路径
		path = retrieveRealServerFilePath(path);

		// 检查文件是否存在
		File obj = new File(path);
		if (!obj.exists()) {
			mimetype = "application/json";
			encoding = "charset="+"UTF-8";
			response.setContentType(mimetype + encoding);
			response.getWriter().print("{\"error\":\"ture\", \"message\":\"指定文件不存在！\"}");
			return;
		}
		
		Long fileLen = obj.length();
		// 读取文件名：用于设置客户端保存时指定默认文件名
		int index = path.lastIndexOf("/"); // 前提：传入的path字符串以“\”表示目录分隔符
		String fileName = path.substring(index + 1);

		// 写流文件到客户端
		ServletOutputStream out = response.getOutputStream();
		response.setContentType(mimetype + encoding);
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		response.setHeader("Content-length", fileLen.toString());
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(path));
			bos = new BufferedOutputStream(out);
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}
}
