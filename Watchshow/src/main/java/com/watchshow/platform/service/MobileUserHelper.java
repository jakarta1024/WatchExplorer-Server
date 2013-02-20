package com.watchshow.platform.service;

import java.io.File;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.watchshow.common.util.FileManagerUtil;
import com.watchshow.platform.dao.UserHistoryDao;
import com.watchshow.platform.domain.BaseDomainObject;
import com.watchshow.platform.domain.User;
import com.watchshow.platform.domain.UserHistory;
import com.watchshow.platform.domain.Watch;
import com.watchshow.platform.domain.WatchStore;


public class MobileUserHelper {
	
    public enum ACTION {
        REGISTER,
        SIGNUP,
        //For watch
        MARK_AS_FAVORITE, //user driven
        UNMARK_FAVORITE,
        RATE,
        CHECK_IN, //only used for VisitingHistory object
        COMPLAINT,
        COMMENT,
        //
        NO_ACTION,
    }
    public static Map<ACTION, String> ActionAttributesMap = new HashMap<ACTION, String>();
    private static Map<ACTION, String> ActionDetailDescMap = new HashMap<ACTION, String>();
    static {
        ActionAttributesMap.put(ACTION.REGISTER, "RIG");
        ActionAttributesMap.put(ACTION.SIGNUP, "SUP");
        ActionAttributesMap.put(ACTION.MARK_AS_FAVORITE, "MAF");
        ActionAttributesMap.put(ACTION.UNMARK_FAVORITE, "UMF");
        ActionAttributesMap.put(ACTION.RATE, "RT");
        ActionAttributesMap.put(ACTION.CHECK_IN, "CKI");
        ActionAttributesMap.put(ACTION.COMMENT, "CMT");
        ActionAttributesMap.put(ACTION.COMPLAINT, "CMPLT");
        ActionAttributesMap.put(ACTION.NO_ACTION, "NA");
        
        ActionDetailDescMap.put(ACTION.REGISTER, "RIGISTER");
        ActionDetailDescMap.put(ACTION.SIGNUP, "SIGN UP");
        ActionDetailDescMap.put(ACTION.MARK_AS_FAVORITE, "MARK AS FAVORITE");
        ActionDetailDescMap.put(ACTION.UNMARK_FAVORITE, "UNMARK FAVORITE");
        ActionDetailDescMap.put(ACTION.RATE, "RATE");
        ActionDetailDescMap.put(ACTION.CHECK_IN, "CHECK IN");
        ActionDetailDescMap.put(ACTION.COMMENT, "COMMENT");
        ActionDetailDescMap.put(ACTION.COMPLAINT, "COMPLAINT");
        ActionDetailDescMap.put(ACTION.NO_ACTION, "N/A");
    }
    private static final String emailPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	
    /**
     * This method will be used to generate simple comment for VisitingHistory and UserHistory
     * @param user - current user
     * @param operatedObject - this object must be Watch instance or Store instance
     * @param action - action
     * @return A generated comment string
     */
	public static String createSimpleComment(User user, BaseDomainObject operatedObject, ACTION action) {
		String comment = null;
		if (operatedObject != null) {
		    if (operatedObject instanceof Watch) {
		        Watch watch = (Watch )operatedObject;
		        comment = user.getUserName() + "[" + user.getIdentifier() + "]" + "commits ACTION: " + ActionDetailDescMap.get(action) + "for Watch ["+watch.getIdentifier()+"] : " + watch.getName();
		    } else if (operatedObject instanceof WatchStore) {
		        WatchStore store = (WatchStore) operatedObject;
		        comment = user.getUserName() + "[" + user.getIdentifier() + "]" + "commits ACTION: " + ActionDetailDescMap.get(action) + "for Store ["+store.getIdentifier()+"] : " + store.getName();
		    }
		} else {
		    comment = user.getUserName() + "[" + user.getIdentifier() + "]" + "commits ACTION: " + ActionDetailDescMap.get(action);
		}
		return comment;
	}
	/**
	 * This method will be used to generate a common response template for any service.
	 * @param returnCode - Return code from server
	 * @param outputData - This parameter is passed by specified service, add to response as data part
	 * @return A JSON object data, later should be serialize to be a string
	 */
	public static JSONObject sharedResponseTemplate(int returnCode, String reason, String message, JSONObject outputData ) {
		JSONObject template = null;
		try {
			JSONObject context = new JSONObject();
			context.put("version", MobileUserService.ServiceVersion.toString());
			context.put("returnCode", new Integer(returnCode).toString());
			if (reason != null && !reason.isEmpty()) {
				context.put("reason", reason);
			}
			if (message != null && !message.isEmpty()) {
				context.put("message", message);
			}
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			context.put("timestamp", timestamp.toString());
			context.put("outputData", outputData);
			template = context;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return template;
	}
	
	public static boolean isUserDrivenAction(ACTION action) {
		if (action.compareTo(ACTION.MARK_AS_FAVORITE) >= 0 && action.compareTo(ACTION.COMMENT) <= 0 ) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String generatePlaceholderEmailForUser(User user) {
	    String email = "auto_"+user.getUserName()+"@wsmail.com";
	    return email;
	}
	
	public static String generateAutoPassword() {
	    String str = "";
        str += (int)(Math.random()*9+1);
        for(int i = 0; i < 5; i++){
            str += (int)(Math.random()*10);
        }
        return str;
	}
	
	public JSONArray getCommentAttachmentInfos(Long commentId, String hostPath) {
		JSONArray infos = new JSONArray();
		UserHistoryDao DAO = new UserHistoryDao();
		UserHistory comment =  DAO.get(commentId);
		//just one folder, we do not need any more sub-folders to separate different files 
		String relativePath = comment.getCommentAttachmentsPath(); 
		String srcFolder = hostPath + relativePath + File.separator;
		
		File folder = new File(srcFolder);
		if (folder.exists()) {
			List<File> files = FileManagerUtil.getAllSubFiles(folder);
			for (File f : files) {
				JSONObject item = new JSONObject();
				String mimetype = ResourcePathHelper.getMimeType(f.getName());
				try {
					item.put("mimetype", mimetype);
					item.put("name", f.getName());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String path = f.getPath();
				int start = path.indexOf(relativePath);
				path = path.substring(start, path.length());
				System.out.println("file path = " + path);
				
				
			}
		}
		return infos;
	}
	public static boolean isEmailAddress(String input) {
	    boolean matched = false;
	    Pattern regex = Pattern.compile(emailPattern);
	    Matcher matcher = regex.matcher(input);
	    matched = matcher.matches();
	    return matched;
	}
	
	public static List<String> composeDownloadableURLs(List<String> pathset) {
		List<String> urls = new ArrayList<String>();
		for (String path : pathset) {
			String url = "/dl?client=mobile&path=" + path;
			urls.add(url);
		}
		return urls;
	}
}
