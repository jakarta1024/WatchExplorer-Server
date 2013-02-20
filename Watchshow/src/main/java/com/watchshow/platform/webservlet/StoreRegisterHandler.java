package com.watchshow.platform.webservlet;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.imgscalr.Scalr;

import com.watchshow.common.util.Assert;
import com.watchshow.common.util.ConstantsProvider;
import com.watchshow.common.util.EncryptUtil;
import com.watchshow.common.util.FileManagerUtil;
import com.watchshow.common.util.HibernateUtil;
import com.watchshow.common.util.RemoteClientInfoUtil;
import com.watchshow.platform.dao.StoreAdministratorDao;
import com.watchshow.platform.dao.WatchBrandDao;
import com.watchshow.platform.dao.WatchStoreDao;
import com.watchshow.platform.domain.StoreAdminHistory;
import com.watchshow.platform.domain.StoreAdministrator;
import com.watchshow.platform.domain.WatchBrand;
import com.watchshow.platform.domain.WatchStore;
import com.watchshow.platform.service.ResourcePathHelper;
import com.watchshow.platform.service.StoreAdminHelper;

/**
 * Servlet implementation class FileUploadServlet
 * 
 * @note Servlet parameters: fileType = [text, image, vedio, audio, other].
 */
@WebServlet(description = "To address forms submitted for regitering a watchstore", 
			urlPatterns = { "/store/register" }, 
			initParams = { @WebInitParam(name = "uploadRootPath", value = "", description = "") })
public class StoreRegisterHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String fileUploadRootPath;

	// These keys indicates to retrieve parameters for founder admin
	private static final String paramFounder = "founder";
	private static final String paramFounderPassword = "password";
	private static final String paramFounderRepasswd = "repassword";
	private static final String paramFounderEmail = "email";

	// These keys indicate to retrieve parameters for store info
	private static final String paramStoreName = "storename";
	private static final String paramEnBrand = "enbrand";
	private static final String paramZhBrand = "cnbrand";
	private static final String paramStoreTel = "telephone";
	private static final String paramStoreProvince = "province";
	private static final String paramStoreCity = "city";
	private static final String paramStoreDist = "district";
	private static final String paramStoreAddress = "address";

	private static final String paramStorePost = "postcode";
	private static final String paramStoreFax = "fax";
	private static final String paramWebsite = "website";
	private static final String paramStoreDesc = "storedesc";
	// validator key
	//private static final String paramValidator = "validator";

	@Override
	public void init(ServletConfig config) {
		fileUploadRootPath = config.getInitParameter("uploadRootPath");
		if (fileUploadRootPath == null || fileUploadRootPath.isEmpty()) {
			fileUploadRootPath = config.getServletContext().getRealPath("/");
			System.out.println("Watchshow Resource Path: " + fileUploadRootPath);
		}
		Assert.isTrue(fileUploadRootPath != null && !fileUploadRootPath.isEmpty(),
				"Cannot be null or blank string!");
	}
    /**
	 * @see HttpServlet#service(ServletRequest request, ServletResponse response);
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Set charactor encoding
		super.service(request, response);
		response.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
		request.setCharacterEncoding(ConstantsProvider.DEFAULT_ENCODING);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("getfile") != null
				&& !request.getParameter("getfile").isEmpty()) {

			File file = new File(fileUploadRootPath,
					request.getParameter("getfile"));
			if (file.exists()) {
				int bytes = 0;
				ServletOutputStream op = response.getOutputStream();

				response.setContentType(getMimeType(file));
				response.setContentLength((int) file.length());
				response.setHeader("Content-Disposition", "inline; filename=\""
						+ file.getName() + "\"");

				byte[] bbuf = new byte[1024];
				DataInputStream in = new DataInputStream(new FileInputStream(
						file));

				while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, bytes);
				}

				in.close();
				op.flush();
				op.close();
			}
		} else if (request.getParameter("delfile") != null
				&& !request.getParameter("delfile").isEmpty()) {
			File file = new File(fileUploadRootPath,
					request.getParameter("delfile"));
			if (file.exists()) {
				file.delete(); // TODO:check and report success
			}
		} else if (request.getParameter("getthumb") != null
				&& !request.getParameter("getthumb").isEmpty()) {
			File file = new File(fileUploadRootPath,
					request.getParameter("getthumb"));
			if (file.exists()) {
				String mimetype = getMimeType(file);
				if (mimetype.endsWith("png") || mimetype.endsWith("jpeg")
						|| mimetype.endsWith("gif")) {
					BufferedImage im = ImageIO.read(file);
					if (im != null) {
						BufferedImage thumb = Scalr.resize(im, 75);
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						if (mimetype.endsWith("png")) {
							ImageIO.write(thumb, "PNG", os);
							response.setContentType("image/png");
						} else if (mimetype.endsWith("jpeg")) {
							ImageIO.write(thumb, "jpg", os);
							response.setContentType("image/jpeg");
						} else {
							ImageIO.write(thumb, "GIF", os);
							response.setContentType("image/gif");
						}
						ServletOutputStream srvos = response.getOutputStream();
						response.setContentLength(os.size());
						response.setHeader("Content-Disposition",
								"inline; filename=\"" + file.getName() + "\"");
						os.writeTo(srvos);
						srvos.flush();
						srvos.close();
					}
				}
			} // TODO: check and report success
		} else {
			PrintWriter writer = response.getWriter();
			writer.write("call POST with multipart form data");
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// temp code
		System.out.println("StoreRegister.html post recieved!");
		// upload image files
		// Long storeId = store.getIdentifier();
		
		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
		}

		ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
//		JSONArray json = new JSONArray();
		String founderName, founderPasswd, founderRepasswd, founderEmail;
		String storeName,storeEnBrand,storeZhBrand,storeTel,storeAddr,storeProvince,storeCity,storeDist,storePost,storeFax,storeWebsite,storeDesc;
		founderName = founderPasswd = founderRepasswd = founderEmail = null;
		storeName = storeEnBrand = storeZhBrand = storeTel = storeAddr = storeProvince = storeCity = storeDist = storePost = storeFax = storeWebsite = storeDesc = null;
		//String validatorInput = null;
		Long storeId = new Long(-1);
		boolean hasImages = false;
		boolean saveSuccessed = false;
		try {
			List<FileItem> items = uploadHandler.parseRequest(request);
			List<FileItem> fileItems = new ArrayList<FileItem>();
			////////////////////////////////////////////////////////////////////////////////////
			/**
			 * Step 1: get form parameters 
			 */
			for (FileItem item : items) {
				if(item.isFormField()) {
					String paramKey = item.getFieldName();
					String paramValue = new String(item.getString().getBytes("ISO8859-1"), "UTF-8");
					System.out.println(paramKey+" : "+paramValue);
					// Prepare founder information and store information:
					if(paramKey.equalsIgnoreCase(paramFounder)) {
						founderName = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramFounderEmail)) {
						founderEmail = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramFounderPassword)) {
						founderPasswd = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramFounderRepasswd)) {
						founderRepasswd = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramStoreName)) {
						storeName = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramEnBrand)) {
						storeEnBrand = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramZhBrand)) {
						storeZhBrand = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramStoreTel)) {
						storeTel = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramStoreProvince)) {
						storeProvince = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramStoreCity)) {
						storeCity = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramStoreDist)) {
						storeDist = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramStoreAddress)) {
						storeAddr = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramStorePost)) {
						storePost = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramStoreFax)) {
						storeFax = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramWebsite)) {
						storeWebsite = paramValue;
					} else if (paramKey.equalsIgnoreCase(paramStoreDesc)) {
						storeDesc = paramValue;
					} else {
						System.out.print("Unkown paramater: "+paramKey+"="+paramValue);
					}
				} else {
					System.out.println(item.getName());
					fileItems.add(item);
				}
			}
			////////////////////////////////////////////////////////////////////////////////////
			/**
			 * Step 2: Fill informations to database
			 */
			/* Random code checker take over this at front pages. 
			String validaterGener = request.getSession().getAttribute("randomString").toString();
			if (!validatorInput.equalsIgnoreCase(validaterGener)) {
				// Dispatch to original page to show error information??
				writer.print("<h>Validator is not matched with shown!</h>");
				return;
			}*/
			if (!founderPasswd.equals(founderRepasswd)) {
				// Dispatch to original page to show error information??
				writer.print("<h>Input password and reinput password is not matched.</h>");
				return;
			}
			// Set all pojos and relationships
			// save store info
			//find exs
			WatchStoreDao storeDAO = new WatchStoreDao();
			WatchStore existing = storeDAO.getStoreByName(storeName);
			if (existing != null) {
				writer.print("<h>该表店名已被使用，请输入另一个！</h>");
				return;
			}
			WatchStore store = new WatchStore();
			store.setAuthorised(false);
			store.setName(storeName);
			// now, brand filling will be complicated
			WatchBrand brand = new WatchBrand();
			brand.setEngName(storeEnBrand);
			brand.setChnName(storeZhBrand);
			store.setBrand(brand);

			store.setPhoneNumber(storeTel);
			store.setFax(Long.parseLong(storeFax));
			store.setDescription(storeDesc);
			store.setAddress(storeAddr);
			store.setProvince(storeProvince);
			store.setCity(storeCity);
			store.setDistrict(storeDist);
			store.setPostcode(Integer.parseInt(storePost));
			store.setWebsite(storeWebsite);
			// watch.set
			// save founder info
			StoreAdministrator storeAdmin = new StoreAdministrator();
			storeAdmin.setAuthorised(false);
			storeAdmin.setLoginName(founderName);
			storeAdmin.setPassword(founderPasswd);
			storeAdmin.setRole(StoreAdminHelper.RoleAttributeMap.get(StoreAdminHelper.ROLE.FOUNDER));

			StoreAdminHistory log = new StoreAdminHistory();
			Timestamp now = new Timestamp(System.currentTimeMillis());
			log.setComments("Reigster store: " + store.getName() + " at " + now
					+ ".");
			log.setTimestamp(now);
			String IPAddress = RemoteClientInfoUtil.getRealRemoteIPAddress(request);
			log.setIPAddress(IPAddress);
			log.setOperatedWatch(null);
			log.setOwner(storeAdmin);
			// ///////////////////////////////////////////////////////////////////////////////
			// update database
			storeAdmin.setPasswordMD5(EncryptUtil.MD5(founderPasswd));
			storeAdmin.setVerifyEmail(founderEmail);
			storeAdmin.setStore(store);
			
			Transaction tx = null;
			Session session = HibernateUtil.currentSession();
			try {
				tx = session.beginTransaction();
				WatchBrandDao brandDAO = new WatchBrandDao();
				brandDAO.saveOrUpdate(brand);
				//storeDAO.save(store);
				StoreAdministratorDao storeAdminDAO = new StoreAdministratorDao();
				storeAdminDAO.saveOrUpdate(storeAdmin); // TODO: set this store cascade to update-save
				// Do we need to save store separately again??
				storeAdmin.setStore(store);
				storeDAO.saveOrUpdate(store);
				storeId = store.getIdentifier();
				System.out.println("storeId = "+storeId.toString());
				String storeDescSrcPath = ResourcePathHelper.getServerFolderForStoreDescSource(storeId);
				System.out.println("path = "+storeDescSrcPath);
				store.setDescResourceURL(storeDescSrcPath);
				storeDAO.saveOrUpdate(store);
				storeAdminDAO.saveOrUpdate(storeAdmin); //TODO: does store be saving?
				tx.commit();
			} catch (HibernateException e) {
				if (tx != null) {
					tx.rollback();
				}
				e.printStackTrace();
				return;
			} finally {
				HibernateUtil.closeSession();
				//Show user and store information to register user
			}
			////////////////////////////////////////////////////////////////////////////////////
			/**
			 * Step 3: Upload files, then update database
			 */

			for (FileItem item : fileItems) {
				if (!item.isFormField()) {
					String itemName = item.getName();
					if (itemName == null || itemName =="") {
						continue;
					}
					String destFilename = ResourcePathHelper.generateServerPathForStoringStoreFile(itemName, storeId);
					String path = fileUploadRootPath + File.separator + destFilename;
					File file = FileManagerUtil.createFile(path, itemName);
					item.write(file);

				} 
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {

			Boolean saved =false;
			if (hasImages == false || saveSuccessed == true) {
				saved = true;
			}
			String url = request.getContextPath() + "/ReturnLoginPage.jsp?sid="+storeId+"&user="+founderName+"&password="+founderPasswd + "&saved="+saved;
			response.sendRedirect(url);

		}

	}

	private String getMimeType(File file) {
		String mimetype = "";
		if (file.exists()) {
			if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
				mimetype = "image/png";
			} else {
				javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
				mimetype = mtMap.getContentType(file);
			}
		}
		System.out.println("mimetype: " + mimetype);
		return mimetype;
	}

	private String getSuffix(String filename) {
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) {
			suffix = filename.substring(pos + 1);
		}
		System.out.println("suffix: " + suffix);
		return suffix;
	}

} // class end
