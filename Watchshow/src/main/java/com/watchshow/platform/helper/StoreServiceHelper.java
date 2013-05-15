package com.watchshow.platform.helper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.watchshow.platform.domain.BaseDomainObject;
import com.watchshow.platform.domain.Publication;
import com.watchshow.platform.domain.StoreAdministrator;
import com.watchshow.platform.domain.Watch;
import com.watchshow.platform.service.PlatformServiceContext;


public class StoreServiceHelper {
	public enum ROLE {
		FOUNDER, //Indicates Store Creator aka the boss.
		ADMIN, //do anything
		ASSADMIN, //do anything?
		RECORDER, //just for insert data
		VIEWER //just can view data
		
	}
	public enum ACTION {
		CREATE,
		READ,
		UPDATE,
		DELETE
	}
	
	private static final String emailPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	
	public static Map<ROLE, String> RoleAttributeMap = new HashMap<ROLE, String>();
	public static Map<ACTION, String> ActionAttributeMap = new HashMap<ACTION, String>();
	
	static {
	    RoleAttributeMap.put(ROLE.FOUNDER, "FOUNDER");
	    RoleAttributeMap.put(ROLE.ADMIN, "ADMINISTRATOR");
	    RoleAttributeMap.put(ROLE.ASSADMIN, "ASSOCIATED ADMINISTRATOR");
	    RoleAttributeMap.put(ROLE.RECORDER, "RECORDER");
	    RoleAttributeMap.put(ROLE.VIEWER, "VIEWER");
	    
	    ActionAttributeMap.put(ACTION.CREATE, "ADD");
	    ActionAttributeMap.put(ACTION.READ, "READ");
	    ActionAttributeMap.put(ACTION.UPDATE, "UPDATE");
	    ActionAttributeMap.put(ACTION.DELETE, "DELETE");
	}
	
    /**
     * Generate a simple comment string by given parameters
     * @param admin - administrator
     * @param operatedObject - operated object, must be watch or publication
     * @param action - action
     * @return a comment string
     */
	public static String createSimpleComment(StoreAdministrator admin, BaseDomainObject operatedObject, ACTION action) {
		String comments = null;
		String roleString = admin.getRole();
		String actionString = ActionAttributeMap.get(action);
		//comments: AdminName [123124]: [Founder] commits ACTION: [insert] at: [2012-12-24 14:23:33]
		comments = "STOREADMIN:[" + admin.getLoginName() +"] - ["+admin.getIdentifier()+"]: "+"[" + roleString + "]" + " commits ACTION:[" + actionString +"]";
		if (operatedObject != null) {
		    if (operatedObject instanceof Publication) {
		        Publication pub = (Publication)operatedObject;
	            comments += " towards "+pub.getKind()+" ["+pub.getIdentifier()+"]: "+pub.getTitle();
	        } else if (operatedObject instanceof Watch) {
	            Watch watch = (Watch)operatedObject;
	            comments += " towards Watch ["+watch.getIdentifier()+"]: "+watch.getName();
	        }
		}
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString = formater.format(new Date());
		comments += " at: " + timeString; 
		return comments;
	}
	/**
	 * This method will be used to check if the given string is an available email formatted string.
	 * @param input - The string under checking
	 * @return <code>true</code>, if it is an email address, else return <code>false</code>
	 */
	public static boolean isEmailAddress(String input) {
	    boolean matched = false;
	    Pattern regex = Pattern.compile(emailPattern);
	    Matcher matcher = regex.matcher(input);
	    matched = matcher.matches();
	    return matched;
	}
	
	public static JSONObject sharedResponseTemplate(int returnCode, String reason, String message, JSONObject outputData ) {
		JSONObject template = null;
		try {
			JSONObject context = new JSONObject();
			context.put("version", PlatformServiceContext.ServiceVersion.toString());
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
}
