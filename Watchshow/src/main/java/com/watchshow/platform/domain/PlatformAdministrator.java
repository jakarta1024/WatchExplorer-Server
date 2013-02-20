package com.watchshow.platform.domain;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
/**
 * This entity indicates top-level system administrator for 'Watchshow' platform
 * @author Kipp Li
 *
 */
public class PlatformAdministrator extends BaseDomainObject {
	private String loginName;
	private String password;
	private String passwordMD5;
	
	//Relationships
	//one-to-many
	private Set<PlatformAdminLog> adminLogs = new HashSet<PlatformAdminLog>(0);
	private Set<PlatformBulletin> bulletins = new HashSet<PlatformBulletin>(0);
	/**
	 * Constructor
	 */
	public PlatformAdministrator() {
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

	public Set<PlatformAdminLog> getAdminLogs() {
		return adminLogs;
	}

	public void setAdminLogs(Set<PlatformAdminLog> adminLogs) {
		this.adminLogs = adminLogs;
	}

	public Set<PlatformBulletin> getBulletins() {
		return bulletins;
	}

	public void setBulletins(Set<PlatformBulletin> bulletins) {
		this.bulletins = bulletins;
	}
}
