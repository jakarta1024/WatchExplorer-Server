package com.watchshow.platform.domain;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public class UserHistory extends BaseDomainObject {
	
	static public final String MARK_AS_FAVORITE = "MF";
	static public final String UNMARK_FAVORITE  = "UMF";
	static public final String RATE = "RT";
	static public final String COMMENT = "CMT";
	static public final String NOACTION = "NA";

	private Timestamp timestamp;
	private String deviceUUID;
	private String deviceSN;
	private String deviceOS;
	private String deviceMacAddress;
	private String IPAddress;
	//private Boolean isFavorite;
	private Integer rateIdea;
	private Integer rateMovement;
	private Integer rateLook;
	private Integer rateProspect;
	private Integer ratePrice;
	private Integer rateSum;
	private String subject;
	private String comment;
	private String commentAttachmentsPath;
	private String action;
	
	//geographical information
	private String longitude;
	private String latitude;
	private String location;
	
	private Watch viewedWatch; //viewed watch at this time
	private User owner;

	/**
	 * Constructor
	 */
	public UserHistory() {
		super();
	}
	/**
	 * Getters and Setters 
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getDeviceUUID() {
		return deviceUUID;
	}
	public void setDeviceUUID(String deviceUUID) {
		this.deviceUUID = deviceUUID;
	}
	public String getDeviceSN() {
		return deviceSN;
	}
	public void setDeviceSN(String deviceSN) {
		this.deviceSN = deviceSN;
	}
	public String getDeviceOS() {
		return deviceOS;
	}
	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}
	public String getDeviceMacAddress() {
		return deviceMacAddress;
	}
	public void setDeviceMacAddress(String deviceMacAddress) {
		this.deviceMacAddress = deviceMacAddress;
	}
	public Watch getViewedWatch() {
		return viewedWatch;
	}
	public void setViewedWatch(Watch viewedWatch) {
		this.viewedWatch = viewedWatch;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCommentAttachmentsPath() {
		return commentAttachmentsPath;
	}
	public void setCommentAttachmentsPath(String commentAttachmentsPath) {
		this.commentAttachmentsPath = commentAttachmentsPath;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getRateIdea() {
		return rateIdea;
	}
	public void setRateIdea(Integer rateIdea) {
		this.rateIdea = rateIdea;
	}
	public Integer getRateMovement() {
		return rateMovement;
	}
	public void setRateMovement(Integer rateMovement) {
		this.rateMovement = rateMovement;
	}
	public Integer getRateLook() {
		return rateLook;
	}
	public void setRateLook(Integer rateLook) {
		this.rateLook = rateLook;
	}
	public Integer getRateProspect() {
		return rateProspect;
	}
	public void setRateProspect(Integer rateProspect) {
		this.rateProspect = rateProspect;
	}
	public Integer getRatePrice() {
		return ratePrice;
	}
	public void setRatePrice(Integer ratePrice) {
		this.ratePrice = ratePrice;
	}
	public Integer getRateSum() {
		return rateSum;
	}
	public void setRateSum(Integer rateSum) {
		this.rateSum = rateSum;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
