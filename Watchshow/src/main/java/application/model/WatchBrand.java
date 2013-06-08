 package application.model;

import java.util.Set;

public class WatchBrand extends BaseDomainObject {

	private static final long serialVersionUID = 1L;
	
	private String chnName;
	private String engName;
	
	private String introduction;
	private String backgroundIntro;
	private String introResourcesURL;
	
	private Set<Watch> watches;
	private Set<WatchStore> stores;
	public WatchBrand() {
		super();
	}
	public String getChnName() {
		return chnName;
	}
	public void setChnName(String chnName) {
		this.chnName = chnName;
	}
	public String getEngName() {
		return engName;
	}
	public void setEngName(String engName) {
		this.engName = engName;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getBackgroundIntro() {
		return backgroundIntro;
	}
	public void setBackgroundIntro(String backgroundIntro) {
		this.backgroundIntro = backgroundIntro;
	}
	public String getIntroResourcesURL() {
		return introResourcesURL;
	}
	public void setIntroResourcesURL(String introResourcesURL) {
		this.introResourcesURL = introResourcesURL;
	}
	public Set<Watch> getWatches() {
		return watches;
	}
	public void setWatches(Set<Watch> watches) {
		this.watches = watches;
	}
	public Set<WatchStore> getStores() {
		return stores;
	}
	public void setStores(Set<WatchStore> stores) {
		this.stores = stores;
	}

}
