package application.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class WatchStore extends BaseDomainObject {
	
	//Attributes
	private String name;
	private Float rate;
	private Float credits;
	private String province;
	private String city;
	private String district;
	private String address;
	private String longitude;
	private String latitude;
	private String phoneNumber;
	private String website;
	private Long fax;
	private Integer postcode;
	private String description;
	private String descResourceURL;
	private Timestamp registerTime;
	private Date foundTime;
	private String weiboAccount;
	private String renrenAccount;
	private String gplusAccount;
	private String facebookAccount;
	private String twitterAccount;
	private String QQAccount;
	private Boolean authorised;
	
	//Relationships
	private WatchBrand brand;
	//one-to-many
	private Set<StoreAdministrator> admins = new HashSet<StoreAdministrator>(0); //class = StoreAdminstrator
	private Set<Watch> watches = new HashSet<Watch>(0); //class = Watch
	private Set<VisitingHistory> visitingHistories = new HashSet<VisitingHistory>(0); //class = CheckinLog
	
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * Constructions
	 */
	public WatchStore() {
		super();
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * Getters and Setters
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public WatchBrand getBrand() {
		return brand;
	}
	public void setBrand(WatchBrand brand) {
		this.brand = brand;
	}
	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
	}
	public Float getCredits() {
		return credits;
	}
	public void setCredits(Float credits) {
		this.credits = credits;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public Long getFax() {
		return fax;
	}
	public void setFax(Long fax) {
		this.fax = fax;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescResourceURL() {
		return descResourceURL;
	}
	public void setDescResourceURL(String descResourceURL) {
		this.descResourceURL = descResourceURL;
	}
	public Timestamp getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}


	public Set<StoreAdministrator> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<StoreAdministrator> admins) {
		this.admins = admins;
	}

	public Set<Watch> getWatches() {
		return watches;
	}

	public void setWatches(Set<Watch> watches) {
		this.watches = watches;
	}

	public Date getFoundTime() {
		return foundTime;
	}

	public void setFoundTime(Date foundTime) {
		this.foundTime = foundTime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getWeiboAccount() {
		return weiboAccount;
	}

	public void setWeiboAccount(String weiboAccount) {
		this.weiboAccount = weiboAccount;
	}

	public Boolean getAuthorised() {
		return authorised;
	}

	public void setAuthorised(Boolean authorised) {
		this.authorised = authorised;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Integer getPostcode() {
		return postcode;
	}

	public void setPostcode(Integer postcode) {
		this.postcode = postcode;
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

	public Set<VisitingHistory> getVisitingHistories() {
		return visitingHistories;
	}

	public void setVisitingHistories(Set<VisitingHistory> visitingHistories) {
		this.visitingHistories = visitingHistories;
	}
}
