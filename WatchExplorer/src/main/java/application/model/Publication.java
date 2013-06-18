package application.model;

import java.util.Set;

public class Publication extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	public static final String NEWS = "news";
	public static final String ARTICLE = "article";
	public static final String ACTIVITY = "activity";
	public static final String ANNOUNCEMENT = "announcement";
	
	private String title;
	private String subtitle;
	private String kind; //News, Article, Activity
	private String content;
	private String externalURL; //Hyperlinker to external website
	private String resourcesURL; //current publication used resources folder(root folder)

	private StoreAdministrator publisher;
	private Set<Watch> referredWatches;
	private Set<StoreAdminHistory> histories;
	
	public Publication() {
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

	public String getExternalURL() {
		return externalURL;
	}

	public void setExternalURL(String externalURL) {
		this.externalURL = externalURL;
	}

	public StoreAdministrator getPublisher() {
		return publisher;
	}

	public void setPublisher(StoreAdministrator publisher) {
		this.publisher = publisher;
	}

	public Set<Watch> getReferredWatches() {
		return referredWatches;
	}

	public void setReferredWatches(Set<Watch> referredWatches) {
		this.referredWatches = referredWatches;
	}

	public String getResourcesURL() {
		return resourcesURL;
	}

	public void setResourcesURL(String resourcesURL) {
		this.resourcesURL = resourcesURL;
	}

	public Set<StoreAdminHistory> getHistories() {
		return histories;
	}

	public void setHistories(Set<StoreAdminHistory> histories) {
		this.histories = histories;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
