package application.model;

import java.io.Serializable;

public class BaseDomainObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//basic common attributes
	private Long identifier;
	private Integer version;
	/**
	 * Constructor
	 */
	public BaseDomainObject() {
		super();
	}
	/**
	 * Getters and Setters
	 */
	public Long getIdentifier() {
		return identifier;
	}
	protected void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
