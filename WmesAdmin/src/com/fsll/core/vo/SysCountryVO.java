package com.fsll.core.vo;

import com.fsll.common.CommonConstants;

public class SysCountryVO {
	private String id;
	private String name;
	private String nameSc;
	private String nameTc;
	private String nameEn;
	private String isoCode2;
	private String isoCode3;
	private String areaCode;
	private String orderBy;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNameSc() {
		return nameSc;
	}

	public void setNameSc(String nameSc) {
		this.nameSc = nameSc;
	}

	public String getNameTc() {
		return nameTc;
	}

	public void setNameTc(String nameTc) {
		this.nameTc = nameTc;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getIsoCode2() {
		return isoCode2;
	}

	public void setIsoCode2(String isoCode2) {
		this.isoCode2 = isoCode2;
	}

	public String getIsoCode3() {
		return isoCode3;
	}

	public void setIsoCode3(String isoCode3) {
		this.isoCode3 = isoCode3;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 用于根据语言编码返回国家名称
	 * @param lang
	 * @return
	 */
	public String getCountryName(String lang){
		if (null!=lang){
			if (CommonConstants.LANG_CODE_EN.equalsIgnoreCase(lang))
				return nameEn;
			else if (CommonConstants.LANG_CODE_TC.equalsIgnoreCase(lang))
				return nameTc;
			else {
				return nameSc;
			}
		}
		return "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
