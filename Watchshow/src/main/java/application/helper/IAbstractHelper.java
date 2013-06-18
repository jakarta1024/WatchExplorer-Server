package application.helper;

import java.util.Date;
import org.codehaus.jettison.json.JSONObject;

public interface IAbstractHelper {
	JSONObject sharedResponseTemplate(int returnCode, String reason, String message, JSONObject innerData);
	String comment(String author, Date date, String commentContent);
}
