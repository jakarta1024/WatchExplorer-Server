package application.model;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class StoreAdminHistory extends BaseDomainObject {
	
	static public final String CREATE = "C";
	static public final String READ   = "R";
	static public final String UPDATE = "U";
	static public final String DELETE = "D";
	static public final String NOACTION = "NA";
	static public final String ALL = "CRUD";

	private Timestamp timestamp;
	private String IPAddress;
	private String comments;
	private Watch operatedWatch;
	private String action;
	//constraints
	private StoreAdministrator owner;
	private Publication publication;
	
	/**
	 * Constructors
	 */
	public StoreAdminHistory() {
		super();
	}
	/**
	 * Getters and setters
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String IPAddress) {
		this.IPAddress = IPAddress;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public Watch getOperatedWatch() {
		return operatedWatch;
	}

	public void setOperatedWatch(Watch operatedWatch) {
		this.operatedWatch = operatedWatch;
	}

	public StoreAdministrator getOwner() {
		return owner;
	}

	public void setOwner(StoreAdministrator owner) {
		this.owner = owner;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
    public Publication getPublication() {
        return publication;
    }
    public void setPublication(Publication publication) {
        this.publication = publication;
    }
	
}
