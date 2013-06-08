package application.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import application.model.BaseDomainObject;

public abstract class AbstractServiceContext {
	public static final String INTERNAL_ERROR_REASON = "Internal error";
	public static final String INTERNAL_ERROR_MESSAGE = "Failed at Requested Server"; 
	
	public static Float ServiceVersion = new Float(1.0);
	public static String ServiceIdentifier = "com.watchshow.service.absIdentifier";
	protected String webappRealPath;
	protected String appHostURL;
	private Method currentMethod;
	private String passedServiceName;
	private BaseDomainObject currentUser;
	
	public AbstractServiceContext(BaseDomainObject user, String serviceName, String appURL ,String realpath) {
		try {
			webappRealPath = realpath;
			appHostURL = appURL;
		    passedServiceName = serviceName;
		    currentUser = user;
			currentMethod = getClass().getDeclaredMethod(serviceName, JSONObject.class, List.class);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("No such service implemented for " + serviceName);
			try {
                currentMethod = getClass().getDeclaredMethod("serviceNotFound", String.class);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (SecurityException e1) {
                e1.printStackTrace();
            }
		}
	}

	/**
	 * Execute service corresponding to your call
	 * @param inputData - passed data which will be used by corresponding service method
	 * @return response data
	 */
	public JSONObject execute(String parameters, List<FileItem> uploadFileItems) {
		JSONObject responseData = null;
		try {
			JSONObject decodedRequestParameters = decodeUpstream(parameters);
			responseData = (JSONObject) currentMethod.invoke(this, decodedRequestParameters, uploadFileItems );
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return responseData;
	}
	public JSONObject serviceNotFound(String inputData) throws JSONException {
	    Integer returnCode = 0;
	    String message = "Service["+ passedServiceName +"] is Not Found!";
    	String reason  = "Service Not Found";
    	JSONObject response = null;//TODO: ServiceResponseMaker.sharedResponseTemplate(returnCode, reason, message, new JSONObject());
	    return response;
	}
	
	protected JSONObject decodeUpstream(String inputData) {
		JSONObject inputJson = null;
		try {
			inputJson = new JSONObject(inputData);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("--------**------------------------");
		System.out.println("inputData: "+inputData);
		System.out.println("--------**------------------------");
		return inputJson;
	}
}
