package com.watchshow.platform.domain;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class User extends BaseDomainObject {
	//!@see database script
	public static final String FEMALE = "F";
	public static final String MALE   = "M";
	public static final String UNKNOW = "N";
	
	private String userName;
	private String verifyEmail;
	private String password;
	private String passwordMD5;
	private Character sex;
	private Date birthday;
	private Boolean isMarried;
	private String avatarURL;
	private Integer credits;
	private String phoneNumber;
	private Timestamp registerTime;
	//SNS connection accounts
	private String weiboAccount;
	private String renrenAccount;
	private String gplusAccount;
	private String facebookAccount;
	private String twitterAccount;
	private String QQAccount;
	
	private Set<VisitingHistory> visitingHistories = new HashSet<VisitingHistory>();
	private Set<UserHistory> userHistories = new HashSet<UserHistory>();

	/**
	 * Constructor
	 */
	public User() {
		super();
	}
	/**
	 * Getters and Setters
	 */
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordMD5() {
		return passwordMD5;
	}
	public void setPasswordMD5(String passwordMD5) {
		this.passwordMD5 = passwordMD5;
	}
	public Character getSex() {
		return sex;
	}
	public void setSex(Character sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Boolean isMarried() {
		return isMarried;
	}
	public void setMarried(Boolean isMarried) {
		this.isMarried = isMarried;
	}

	public Integer getCredits() {
		return credits;
	}
	public void setCredits(Integer credits) {
		this.credits = credits;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Timestamp getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	public String getVerifyEmail() {
		return verifyEmail;
	}

	public void setVerifyEmail(String verifyEmail) {
		this.verifyEmail = verifyEmail;
	}
	
	public void setUserHistories(Set<UserHistory> userHistories) {
		this.userHistories = userHistories;
	}
	
	public Set<UserHistory> getUserHistories() {
		return userHistories;
	}
	public String getWeiboAccount() {
		return weiboAccount;
	}
	public void setWeiboAccount(String weiboAccount) {
		this.weiboAccount = weiboAccount;
	}
	public Set<VisitingHistory> getVisitingHistories() {
		return visitingHistories;
	}
	public void setVisitingHistories(Set<VisitingHistory> visitingHistories) {
		this.visitingHistories = visitingHistories;
	}
	public String getRenrenAccount() {
		return renrenAccount;
	}
	public void setRenrenAccount(String renrenAccount) {
		this.renrenAccount = renrenAccount;
	}
	public String getGplusAccount() {
		return gplusAccount;
	}
	public void setGplusAccount(String gplusAccount) {
		this.gplusAccount = gplusAccount;
	}
	public String getFacebookAccount() {
		return facebookAccount;
	}
	public void setFacebookAccount(String facebookAccount) {
		this.facebookAccount = facebookAccount;
	}
	public String getTwitterAccount() {
		return twitterAccount;
	}
	public void setTwitterAccount(String twitterAccount) {
		this.twitterAccount = twitterAccount;
	}
	public String getQQAccount() {
		return QQAccount;
	}
	public void setQQAccount(String QQAccount) {
		this.QQAccount = QQAccount;
	}
}
