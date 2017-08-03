package com.fsll.wmes.company.vo;

import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;

/**
 * 运行公司
 * @author Yan
 * @version 1.0.0 Created On: 2016-11-29
 */
public class CompanyInfoVO {
	
	private String id;
	private String code;
	private String webUrl;
	private String logoUrl;
	private Short orderBy;
	private String backgroundUrl;
	private String cssUrl;
	private String loginUrl;
	private String isValid;
	
	private String name;
	private String sysName;
	private String copyright;
    
	private CompanyInfoEn companyInfoEn;
	private CompanyInfoSc companyInfoSc;
	private CompanyInfoTc companyInfoTc;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public Short getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Short orderBy) {
		this.orderBy = orderBy;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public CompanyInfoEn getCompanyInfoEn() {
		return companyInfoEn;
	}
	public void setCompanyInfoEn(CompanyInfoEn companyInfoEn) {
		this.companyInfoEn = companyInfoEn;
	}
	public CompanyInfoSc getCompanyInfoSc() {
		return companyInfoSc;
	}
	public void setCompanyInfoSc(CompanyInfoSc companyInfoSc) {
		this.companyInfoSc = companyInfoSc;
	}
	public CompanyInfoTc getCompanyInfoTc() {
		return companyInfoTc;
	}
	public void setCompanyInfoTc(CompanyInfoTc companyInfoTc) {
		this.companyInfoTc = companyInfoTc;
	}
	public String getBackgroundUrl() {
    	return backgroundUrl;
    }
	public void setBackgroundUrl(String backgroundUrl) {
    	this.backgroundUrl = backgroundUrl;
    }
	public String getCssUrl() {
    	return cssUrl;
    }
	public void setCssUrl(String cssUrl) {
    	this.cssUrl = cssUrl;
    }
	public String getLoginUrl() {
    	return loginUrl;
    }
	public void setLoginUrl(String loginUrl) {
    	this.loginUrl = loginUrl;
    }
	public String getSysName() {
    	return sysName;
    }
	public void setSysName(String sysName) {
    	this.sysName = sysName;
    }
	public String getCopyright() {
    	return copyright;
    }
	public void setCopyright(String copyright) {
    	this.copyright = copyright;
    }
	public String getName() {
    	return name;
    }
	public void setName(String name) {
    	this.name = name;
    }
	
	public void setInfos(String langCode){
		if ("en".equals(langCode)) {// 英文
			if (this.companyInfoEn != null) {
				this.name = this.companyInfoEn.getName();
				this.sysName = this.companyInfoEn.getSysName();
				this.copyright = this.companyInfoEn.getCopyright();
			}
		} else if ("tc".equals(langCode)) {// 繁体
			if (this.companyInfoTc != null) {
				this.name = this.companyInfoTc.getName();
				this.sysName = this.companyInfoTc.getSysName();
				this.copyright = this.companyInfoTc.getCopyright();
			}
		} else {// 简体
			if (this.companyInfoSc != null) {
				this.name = this.companyInfoSc.getName();
				this.sysName = this.companyInfoSc.getSysName();
				this.copyright = this.companyInfoSc.getCopyright();
			}
		} 
	}

}
