package application.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class Watch extends BaseDomainObject {

	private String productSerialNumber;
	private String name;
	private Float price;
	private Float discount;
	private String description;
	private String simpleDescription;
	private String descResourceURL;
	private String barcode;
	private String twodimCode;
	private Integer generation;
	private Integer rateIdea;
	private Integer rateMovement;
	private Integer rateLook;
	private Integer rateProspect;
	private Integer ratePrice;
	private Integer rateSum;
	private String movement;
	private String material;
	private String size;
	private String style;
	private String architecture;
	private String function;
	private String watchband;
	private Date modifyDate;
	private Date marketTime;

	
	private WatchStore store;
	private WatchBrand brand;
	private Publication publication;
	private Set<UserHistory> userHistories = new HashSet<UserHistory>(0);
	private Set<StoreAdminHistory> adminHistories = new HashSet<StoreAdminHistory>(0);
	
	/**
	 * Constructor
	 */
	public Watch() {
		super();
	}
	/**
	 * Getters and Setters
	 */
	public String getProductSerialNumber() {
		return productSerialNumber;
	}
	public void setProductSerialNumber(String productSerialNumber) {
		this.productSerialNumber = productSerialNumber;
	}
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
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getTwodimCode() {
		return twodimCode;
	}
	public void setTwodimCode(String twodimCode) {
		this.twodimCode = twodimCode;
	}

	public WatchStore getStore() {
		return store;
	}

	public void setStore(WatchStore store) {
		this.store = store;
	}

	public Integer getGeneration() {
		return generation;
	}

	public void setGeneration(Integer generation) {
		this.generation = generation;
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

	public String getMovement() {
		return movement;
	}

	public void setMovement(String movement) {
		this.movement = movement;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getWatchband() {
		return watchband;
	}

	public void setWatchband(String watchband) {
		this.watchband = watchband;
	}
	public String getSimpleDescription() {
		return simpleDescription;
	}
	public void setSimpleDescription(String simpleDescription) {
		this.simpleDescription = simpleDescription;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Set<UserHistory> getUserHistories() {
		return userHistories;
	}
	public void setUserHistories(Set<UserHistory> userHistories) {
		this.userHistories = userHistories;
	}
	public Set<StoreAdminHistory> getAdminHistories() {
		return adminHistories;
	}
	public void setAdminHistories(Set<StoreAdminHistory> adminHistories) {
		this.adminHistories = adminHistories;
	}
	public Date getMarketTime() {
		return marketTime;
	}
	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
	}
    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public Publication getPublication() {
        return publication;
    }
    public void setPublication(Publication publication) {
        this.publication = publication;
    }
	public Integer getRateSum() {
		return rateSum;
	}
	public void setRateSum(Integer rateSum) {
		this.rateSum = rateSum;
	}
}
