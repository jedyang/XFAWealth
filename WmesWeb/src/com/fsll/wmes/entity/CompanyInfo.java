package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "company_info")
public class CompanyInfo implements java.io.Serializable {
	private String id;
	private String code;
	private String webUrl;
	private String logoUrl;
	private Short orderBy;
	private String backgroundUrl;
	private String cssUrl;
	private String loginUrl;
	private String tabLogo;
	private String isValid;

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "code")
	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "web_url")
	public String getWebUrl() {
		return this.webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	@Column(name = "logo_url")
	public String getLogoUrl() {
		return this.logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	@Column(name = "order_by")
	public Short getOrderBy() {
		return this.orderBy;
	}
	public void setOrderBy(Short orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	@Column(name = "background_url")
	public String getBackgroundUrl() {
		return backgroundUrl;
	}
	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}
	
	@Column(name = "css_url")
	public String getCssUrl() {
		return cssUrl;
	}
	public void setCssUrl(String cssUrl) {
		this.cssUrl = cssUrl;
	}
	
	@Column(name = "login_url")
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	@Column(name = "tab_logo")
	public String getTabLogo() {
    	return tabLogo;
    }
	public void setTabLogo(String tabLogo) {
    	this.tabLogo = tabLogo;
    }

}