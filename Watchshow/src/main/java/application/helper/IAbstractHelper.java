package application.helper;

import org.codehaus.jettison.json.JSONObject;

public interface IAbstractHelper {
	JSONObject sharedResponseTemplate(int returnCode, String reason, String message, JSONObject innerData);
}
