package com.watchshow.platform.helper;

import java.io.File;
import java.util.Hashtable;


/**
 * This class is used to control how to generate downloadable URL for a source object which stored at somewhere on server.
 * Also, for security considering, we can convert a downloadable URL to a virtual URL. and this URL can be convert to be downloadable
 * via this class.
 * Especially, Hibernate will use this for fill in some field for some pojos. resourceURL and something else.
 * @author Kipp Li
 *
 */
public class ServerResourceHelper {
	private static String resourcesRootFolder = "watchshow_resources";
	public static final String imageRoot = "images";
	public static final String audioRoot = "audios";
	public static final String videoRoot = "videos";
	public static final String plainRoot = "plain";
	public static final String archiveRoot = "archived";
	public static final String othersRoot = "others";
	private static final String watchesLocalFolder =  "watch_resource";
	private static final String storeResourceFolder = "store_resource";
	private static final String brandResourceFolder = "brand_resource";
	private static final String publicationResourceFolder = "publication_resource";
	
	private static final Hashtable<String,String> mimetypeDict = new Hashtable<String,String>();
	//TODO: useless
	public static ServerResourceHelper sharedInstance(String hostRealRootPath) {
		ServerResourceHelper helper = new ServerResourceHelper();
		ServerResourceHelper.resourcesRootFolder = hostRealRootPath + ServerResourceHelper.resourcesRootFolder;
		return helper;
	}
	static {
		try {
			initMimetypeDictionary();
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static String getServerFolderForStoreDescSource(Long storeId) {
		return resourcesRootFolder + File.separator + storeId.toString() + File.separator + storeResourceFolder;
	}
	public static String getServerFolderForStorePubSource(Long storeId, Long pubId) {
		return resourcesRootFolder + File.separator + storeId.toString() + File.separator + storeResourceFolder + File.separator +publicationResourceFolder + File.separator + pubId;
	}
	public static String getServerFolderForBrandDescSource(Long storeId) {
		return resourcesRootFolder + File.separator + storeId.toString() + File.separator + brandResourceFolder;
	}
	public static String getServerFolderForWatchDescSource(Long storeId, Long watchId) {
		return resourcesRootFolder + File.separator + storeId.toString() + File.separator + watchesLocalFolder + File.separator + watchId.toString();
	}
	
	// looks like : xxxx/watchshow_resources/90932423(storeid)/brand_resource/iamge:video:audio
	public static String generateServerPathForStoringBrandFile(String filename, Long storeID) {
		String path = null;
		if (filename != null) {
			String mimetype = getMimeTypeByFileName(filename);
			String parentFolder = fileFolderForFileWithMimetype(mimetype);
			parentFolder = getServerFolderForBrandDescSource(storeID) +File.separator+ parentFolder; //brand_resource/iamge:video:audio
			path = parentFolder + File.separator;
		}
		return path;
	}
	public static String generateServerPathForStoringPubFile(String filename, Long storeId, Long pubId) {
		String path = null;
		if (filename != null) {
			String mimetype = getMimeTypeByFileName(filename);
			String parentFolder = fileFolderForFileWithMimetype(mimetype);
			parentFolder = getServerFolderForStorePubSource(storeId, pubId) + File.separator + parentFolder;
			path = parentFolder + File.separator;
		}
		return path;
	}
	//looks like: xxxx/watchshow_resources/897987324(storeid)/watch_resource/004392492(watchid)/image:video:audio
	public static String generateServerPathForStoringWatchFile(String filename, Long storeID, Long watchID) {
		String path = null;
		if (filename != null) {
			String mimetype = getMimeTypeByFileName(filename);
			String parentFolder = fileFolderForFileWithMimetype(mimetype);
			parentFolder = getServerFolderForWatchDescSource(storeID, watchID) +File.separator+ parentFolder;
			path = parentFolder + File.separator;
		}
		return path;
	}
	// looks like: xxxx/watchshow_resources/900334242(storeid)/store_resource/iamge:video:audio
	public static String generateServerPathForStoringStoreFile(String filename, Long storeID) {
		String path = null;
		if (filename != null) {
			String mimetype = getMimeTypeByFileName(filename);
			String parentFolder = fileFolderForFileWithMimetype(mimetype);
			parentFolder = getServerFolderForStoreDescSource(storeID) +File.separator+ parentFolder;
			path = parentFolder + File.separator;
		}
		return path;
	}
	private static String fileFolderForFileWithMimetype(String mimetype) {
		String returnFolder = null;
		String prefix = "";
		int pos = mimetype.lastIndexOf('/');
		if (pos > 0 && pos < mimetype.length() - 1) {
			prefix = mimetype.substring(0, pos);
		}
		System.out.println("suffix: " + prefix);
		if (prefix.equalsIgnoreCase("image")) {
			returnFolder = imageRoot;
		} else if (prefix.equalsIgnoreCase("video")) {
			returnFolder = videoRoot;
		} else if (prefix.equalsIgnoreCase("audio")) {
			returnFolder = audioRoot;
		} else if (prefix.equalsIgnoreCase("text")) {
			returnFolder = plainRoot;
		} else if (prefix.equalsIgnoreCase("application")) {
			returnFolder = archiveRoot;
		} else {
			returnFolder = othersRoot;
		}
		return returnFolder;
	}
	
	private static String getMimeTypeByFileName(String filename) {
		String mimetype = "";
		String ext = getSuffix(filename);
		mimetype = mimetypeDict.get(ext);
		System.out.println("mimetype: " + mimetype);
		return mimetype;
	}
	
	public static String getMimeType(String filename) {
		String mimetype = "";
		if (getSuffix(filename).equalsIgnoreCase("png")) {
			mimetype = "image/png";
		} else {
			javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
			mimetype = mtMap.getContentType(filename);
		}
		System.out.println("mimetype: " + mimetype);
		return mimetype;
	}

	private static String getSuffix(String filename) {
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) {
			suffix = filename.substring(pos + 1);
		}
		System.out.println("suffix: " + suffix);
		return suffix;
	}
	
	
	private static void initMimetypeDictionary() {
		mimetypeDict.put("apk","application/vnd.android.package-archive");
		mimetypeDict.put("3gp","video/3gpp");
		mimetypeDict.put("ai" ,"application/postscript"); 
		mimetypeDict.put("aif","audio/x-aiff");
		mimetypeDict.put("aifc","audio/x-aiff");
		mimetypeDict.put("aiff","audio/x-aiff");
		mimetypeDict.put("asc","text/plain");
		mimetypeDict.put("atom","application/atom+xml");
		mimetypeDict.put("au" ,"audio/basic");
		mimetypeDict.put("avi","video/x-msvideo");
		mimetypeDict.put("bcpio","application/x-bcpio");
		mimetypeDict.put("bin","application/octet-stream");
		mimetypeDict.put("bmp","image/bmp");
		mimetypeDict.put("cdf","application/x-netcdf");
		mimetypeDict.put("cgm","image/cgm");
		mimetypeDict.put("class","application/octet-stream");
		mimetypeDict.put("cpio","application/x-cpio");
		mimetypeDict.put("cpt","application/mac-compactpro");
		mimetypeDict.put("csh","application/x-csh");
		mimetypeDict.put("css","text/css");
		mimetypeDict.put("dcr","application/x-director");
		mimetypeDict.put("dif","video/x-dv");
		mimetypeDict.put("dir","application/x-director");
		mimetypeDict.put("djv","image/vnd.djvu");
		mimetypeDict.put("djvu","image/vnd.djvu");
		mimetypeDict.put("dll","application/octet-stream");
		mimetypeDict.put("dmg","application/octet-stream");
		mimetypeDict.put("dms","application/octet-stream");
		mimetypeDict.put("doc","application/msword");
		mimetypeDict.put("dtd","application/xml-dtd");
		mimetypeDict.put("dv" ,"video/x-dv");
		mimetypeDict.put("dvi","application/x-dvi");
		mimetypeDict.put("dxr","application/x-director");
		mimetypeDict.put("eps","application/postscript");
		mimetypeDict.put("etx","text/x-setext");
		mimetypeDict.put("exe","application/octet-stream");
		mimetypeDict.put("ez" ,"application/andrew-inset");
		mimetypeDict.put("flv","video/x-flv");
		mimetypeDict.put("gif","image/gif");
		mimetypeDict.put("gram","application/srgs");
		mimetypeDict.put("grxml","application/srgs+xml");
		mimetypeDict.put("gtar","application/x-gtar");
		mimetypeDict.put("gz" ,"application/x-gzip");
		mimetypeDict.put("hdf","application/x-hdf");
		mimetypeDict.put("hqx","application/mac-binhex40");
		mimetypeDict.put("htm","text/html");
		mimetypeDict.put("html","text/html");
		mimetypeDict.put("ice","x-conference/x-cooltalk");
		mimetypeDict.put("ico","image/x-icon");
		mimetypeDict.put("ics","text/calendar");
		mimetypeDict.put("ief","image/ief");
		mimetypeDict.put("ifb","text/calendar");
		mimetypeDict.put("iges","model/iges");
		mimetypeDict.put("igs","model/iges");
		mimetypeDict.put("jnlp","application/x-java-jnlp-file");
		mimetypeDict.put("jp2","image/jp2");
		mimetypeDict.put("jpe","image/jpeg");
		mimetypeDict.put("jpeg","image/jpeg");
		mimetypeDict.put("jpg","image/jpeg");
		mimetypeDict.put("js" ,"application/x-javascript");
		mimetypeDict.put("kar","audio/midi");
		mimetypeDict.put("latex","application/x-latex");
		mimetypeDict.put("lha","application/octet-stream");
		mimetypeDict.put("lzh","application/octet-stream");
		mimetypeDict.put("m3u","audio/x-mpegurl");
		mimetypeDict.put("m4a","audio/mp4a-latm");
		mimetypeDict.put("m4p","audio/mp4a-latm");
		mimetypeDict.put("m4u","video/vnd.mpegurl");
		mimetypeDict.put("m4v","video/x-m4v");
		mimetypeDict.put("mac","image/x-macpaint");
		mimetypeDict.put("man","application/x-troff-man");
		mimetypeDict.put("mathml","application/mathml+xml");
		mimetypeDict.put("me" ,"application/x-troff-me");
		mimetypeDict.put("mesh","model/mesh");
		mimetypeDict.put("mid","audio/midi");
		mimetypeDict.put("midi","audio/midi");
		mimetypeDict.put("mif","application/vnd.mif");
		mimetypeDict.put("mov","video/quicktime");
		mimetypeDict.put("movie","video/x-sgi-movie");
		mimetypeDict.put("mp2","audio/mpeg");
		mimetypeDict.put("mp3","audio/mpeg");
		mimetypeDict.put("mp4","video/mp4");
		mimetypeDict.put("mpe","video/mpeg");
		mimetypeDict.put("mpeg","video/mpeg");
		mimetypeDict.put("mpg","video/mpeg");
		mimetypeDict.put("mpga","audio/mpeg");
		mimetypeDict.put("ms" ,"application/x-troff-ms");
		mimetypeDict.put("msh","model/mesh");
		mimetypeDict.put("mxu","video/vnd.mpegurl");
		mimetypeDict.put("nc" ,"application/x-netcdf");
		mimetypeDict.put("oda","application/oda");
		mimetypeDict.put("ogg","application/ogg");
		mimetypeDict.put("ogv","video/ogv");
		mimetypeDict.put("pbm","image/x-portable-bitmap");
		mimetypeDict.put("pct","image/pict");
		mimetypeDict.put("pdb","chemical/x-pdb");
		mimetypeDict.put("pdf","application/pdf");
		mimetypeDict.put("pgm","image/x-portable-graymap");
		mimetypeDict.put("pgn","application/x-chess-pgn");
		mimetypeDict.put("pic","image/pict");
		mimetypeDict.put("pict","image/pict");
		mimetypeDict.put("png","image/png");
		mimetypeDict.put("pnm","image/x-portable-anymap");
		mimetypeDict.put("pnt","image/x-macpaint");
		mimetypeDict.put("pntg","image/x-macpaint");
		mimetypeDict.put("ppm","image/x-portable-pixmap");
		mimetypeDict.put("ppt","application/vnd.ms-powerpoint");
		mimetypeDict.put("ps" ,"application/postscript");
		mimetypeDict.put("qt" ,"video/quicktime");
		mimetypeDict.put("qti","image/x-quicktime");
		mimetypeDict.put("qtif","image/x-quicktime");
		mimetypeDict.put("ra" ,"audio/x-pn-realaudio");
		mimetypeDict.put("ram","audio/x-pn-realaudio");
		mimetypeDict.put("ras","image/x-cmu-raster");
		mimetypeDict.put("rdf","application/rdf+xml");
		mimetypeDict.put("rgb","image/x-rgb");
		mimetypeDict.put("rm" ,"application/vnd.rn-realmedia");
		mimetypeDict.put("roff","application/x-troff");
		mimetypeDict.put("rtf","text/rtf");
		mimetypeDict.put("rtx","text/richtext");
		mimetypeDict.put("sgm","text/sgml");
		mimetypeDict.put("sgml","text/sgml");
		mimetypeDict.put("sh" ,"application/x-sh");
		mimetypeDict.put("shar","application/x-shar");
		mimetypeDict.put("silo","model/mesh");
		mimetypeDict.put("sit","application/x-stuffit");
		mimetypeDict.put("skd","application/x-koan");
		mimetypeDict.put("skm","application/x-koan");
		mimetypeDict.put("skp","application/x-koan");
		mimetypeDict.put("skt","application/x-koan");
		mimetypeDict.put("smi","application/smil");
		mimetypeDict.put("smil","application/smil");
		mimetypeDict.put("snd","audio/basic");
		mimetypeDict.put("so" ,"application/octet-stream");
		mimetypeDict.put("spl","application/x-futuresplash");
		mimetypeDict.put("src","application/x-wais-source");
		mimetypeDict.put("sv4cpio","application/x-sv4cpio");
		mimetypeDict.put("sv4crc" ,"application/x-sv4crc");
		mimetypeDict.put("svg","image/svg+xml");
		mimetypeDict.put("swf","application/x-shockwave-flash");
		mimetypeDict.put("t"  ,"application/x-troff");
		mimetypeDict.put("tar","application/x-tar");
		mimetypeDict.put("tcl","application/x-tcl");
		mimetypeDict.put("tex","application/x-tex");
		mimetypeDict.put("texi","application/x-texinfo");
		mimetypeDict.put("texinfo","application/x-texinfo");
		mimetypeDict.put("tif","image/tiff");
		mimetypeDict.put("tiff","image/tiff");
		mimetypeDict.put("tr" ,"application/x-troff");
		mimetypeDict.put("tsv","text/tab-separated-values");
		mimetypeDict.put("txt","text/plain");
		mimetypeDict.put("ustar","application/x-ustar");
		mimetypeDict.put("vcd","application/x-cdlink");
		mimetypeDict.put("vrml","model/vrml");
		mimetypeDict.put("vxml","application/voicexml+xml");
		mimetypeDict.put("wav","audio/x-wav");
		mimetypeDict.put("wbmp","image/vnd.wap.wbmp");
		mimetypeDict.put("wbxml","application/vnd.wap.wbxml");
		mimetypeDict.put("webm","video/webm");
		mimetypeDict.put("wml","text/vnd.wap.wml");
		mimetypeDict.put("wmlc","application/vnd.wap.wmlc");
		mimetypeDict.put("wmls","text/vnd.wap.wmlscript");
		mimetypeDict.put("wmlsc","application/vnd.wap.wmlscriptc");
		mimetypeDict.put("wmv","video/x-ms-wmv");
		mimetypeDict.put("wrl","model/vrml");
		mimetypeDict.put("xbm","image/x-xbitmap");
		mimetypeDict.put("xht","application/xhtml+xml");
		mimetypeDict.put("xhtml","application/xhtml+xml");
		mimetypeDict.put("xls","application/vnd.ms-excel");
		mimetypeDict.put("xml","application/xml");
		mimetypeDict.put("xpm","image/x-xpixmap");
		mimetypeDict.put("xsl","application/xml");
		mimetypeDict.put("xslt","application/xslt+xml");
		mimetypeDict.put("xul","application/vnd.mozilla.xul+xml");
		mimetypeDict.put("xwd","image/x-xwindowdump");
		mimetypeDict.put("xyz","chemical/x-xyz");
		mimetypeDict.put("zip","application/zip");
	}
	
}
