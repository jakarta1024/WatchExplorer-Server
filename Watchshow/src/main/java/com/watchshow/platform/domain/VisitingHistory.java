package com.watchshow.platform.domain;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class VisitingHistory extends BaseDomainObject {
	
	static public final String CHECKIN = "CKI";
	static public final String RATE   = "RT";
	static public final String VIEW = "VW";
	static public final String NOACTION = "NA";
	
	//Attributes
	private Timestamp time;
	private String comments;
	private String action;
	//Relationships
	private User user;
	private WatchStore store;
	
	/**
	 * Constructor
	 */
	public VisitingHistory() {
		super();
	}

	/**
	 * Getters and Setters
	 */
	public WatchStore getStore() {
		return store;
	}

	public void setStore(WatchStore store) {
		this.store = store;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
