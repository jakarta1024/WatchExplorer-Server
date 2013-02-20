package com.watchshow.platform.domain;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class PlatformAdminLog extends BaseDomainObject {
	
	static public final String CREATE = "C";
	static public final String READ   = "R";
	static public final String UPDATE = "U";
	static public final String DELETE = "D";
	static public final String NOACTION = "NA";
	static public final String ALL = "CRUD";
	
	private String comments;
	private Timestamp timestamp;
	private String action;
	//Relationships
	//many-to-one inversion relation 
	private PlatformAdministrator admin;
	private PlatformBulletin publishedBulletin;
	/**
	 * Constructor
	 */
	public PlatformAdminLog() {
		super();
	}
	/**
	 * Getters and Setters
	 */
	public PlatformAdministrator getAdmin() {
		return admin;
	}
	public void setAdmin(PlatformAdministrator admin) {
		this.admin = admin;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public PlatformBulletin getPublishedBulletin() {
		return publishedBulletin;
	}
	public void setPublishedBulletin(PlatformBulletin publishedBulletin) {
		this.publishedBulletin = publishedBulletin;
	}

}
