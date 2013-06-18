package application.model;

import java.sql.Timestamp;
import java.util.Set;

public class PlatformBulletin extends BaseDomainObject {
	
	public static final String ANNOUCEMENT = "annoucement";
	public static final String NOTICE = "notice";
	
	private static final long serialVersionUID = 1L;
	private String title;
	private String subtitle;
	private String kind; //News, Article, Activity
	private String content;
	private Boolean isActive;
	private Timestamp publishTime;
	private Integer effectiveTime; //unit: day
	private PlatformAdministrator publisher;
	private Set<PlatformAdminLog> logs;
	
	public PlatformBulletin() {
		super();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Timestamp getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}
	public Integer getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(Integer effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public PlatformAdministrator getPublisher() {
		return publisher;
	}
	public void setPublisher(PlatformAdministrator publisher) {
		this.publisher = publisher;
	}
	public Set<PlatformAdminLog> getLogs() {
		return logs;
	}
	public void setLogs(Set<PlatformAdminLog> logs) {
		this.logs = logs;
	}

}
