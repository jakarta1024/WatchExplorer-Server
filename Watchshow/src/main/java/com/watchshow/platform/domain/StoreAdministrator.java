package com.watchshow.platform.domain;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class StoreAdministrator extends BaseDomainObject {

	private String loginName;
	private String verifyEmail;
	private String password;
	private String passwordMD5;
	private String role;
	private Boolean authorised;

	private WatchStore store;
	private Set<StoreAdminHistory> adminHistories = new HashSet<StoreAdminHistory>(0);
	private Set<Publication> publications = new HashSet<Publication>(0);
	/**
	 * Constructor
	 */
	public StoreAdministrator() {
		super();
	}
	/**
	 * Getters and Setters
	 */
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getVerifyEmail() {
		return verifyEmail;
	}
	public void setVerifyEmail(String verifyEmail) {
		this.verifyEmail = verifyEmail;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public WatchStore getStore() {
		return store;
	}

	public void setStore(WatchStore store) {
		this.store = store;
	}
	public Boolean getAuthorised() {
		return authorised;
	}
	public void setAuthorised(Boolean authorised) {
		this.authorised = authorised;
	}
	public Set<StoreAdminHistory> getAdminHistories() {
		return adminHistories;
	}
	public void setAdminHistories(Set<StoreAdminHistory> adminHistories) {
		this.adminHistories = adminHistories;
	}
	public Set<Publication> getPublications() {
		return publications;
	}
	public void setPublications(Set<Publication> publications) {
		this.publications = publications;
	}

}
