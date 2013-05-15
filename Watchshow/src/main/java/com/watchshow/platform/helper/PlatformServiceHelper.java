package com.watchshow.platform.helper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.watchshow.platform.domain.BaseDomainObject;
import com.watchshow.platform.domain.PlatformAdministrator;
import com.watchshow.platform.domain.PlatformBulletin;
import com.watchshow.platform.service.PlatformServiceContext;

public class PlatformServiceHelper {
	public enum ACTION {
        //For 
		ADD_ANNOUNCE,
		ADD_NOTICE,
		ADD_NEWS,
		APPROVE_STORE,
        COMMENT,
        UNKNOWN,
        //
        NO_ACTION,
    }
    public static Map<ACTION, String> ActionAttributesMap = new HashMap<ACTION, String>();
    private static Map<ACTION, String> ActionDetailDescMap = new HashMap<ACTION, String>();
    static {
        ActionAttributesMap.put(ACTION.ADD_ANNOUNCE, "AANC");
        ActionAttributesMap.put(ACTION.ADD_NEWS, "ANWS");
        ActionAttributesMap.put(ACTION.ADD_NOTICE, "ANTC");
        ActionAttributesMap.put(ACTION.APPROVE_STORE, "APVST");
        ActionAttributesMap.put(ACTION.COMMENT, "CMT");
        ActionAttributesMap.put(ACTION.UNKNOWN, "UNWN");
        ActionAttributesMap.put(ACTION.NO_ACTION, "NA");
        
        ActionDetailDescMap.put(ACTION.ADD_ANNOUNCE, "ADD ANNOUNCE");
        ActionDetailDescMap.put(ACTION.ADD_NEWS, "ADD NEWS");
        ActionDetailDescMap.put(ACTION.ADD_NOTICE, "ADD NOTICE");
        ActionDetailDescMap.put(ACTION.APPROVE_STORE, "APPROVE STORE");
        ActionDetailDescMap.put(ACTION.COMMENT, "COMMENT");
        ActionDetailDescMap.put(ACTION.UNKNOWN, "UNKNOWN");
        ActionDetailDescMap.put(ACTION.NO_ACTION, "N/A");
    }
	public PlatformServiceHelper() {
		super();
	}
	
	public static String createSimpleComment(PlatformAdministrator user, BaseDomainObject operatedObject, ACTION action) {
		String comment = null;
		if (operatedObject != null) {
		    if (operatedObject instanceof PlatformBulletin) {
		    	PlatformBulletin bulletin = (PlatformBulletin )operatedObject;
		        comment = user.getLoginName() + "[" + user.getIdentifier() + "]" + "commits ACTION: " + ActionDetailDescMap.get(action) + "for Bulletin ["+bulletin.getIdentifier()+"] : " + bulletin.getTitle();
		    }
		} else {
		    comment = user.getLoginName() + "[" + user.getIdentifier() + "]" + "commits ACTION: " + ActionDetailDescMap.get(action);
		}
		return comment;
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
