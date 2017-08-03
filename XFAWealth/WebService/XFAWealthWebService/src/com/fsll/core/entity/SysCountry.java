package com.fsll.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fsll.common.CommonConstants;
import com.fsll.core.CoreConstants;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sys_country")
public class SysCountry implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
	
	@Column(name = "name_sc")
	private String nameSc;
	
	@Column(name = "name_tc")
	private String nameTc;
	
	@Column(name = "name_en")
	private String nameEn;
	
	@Column(name = "iso_code_2")
	private String isoCode2;
	
	@Column(name = "iso_code_3")
	private String isoCode3;
	
	@Column(name = "area_code")
	private String areaCode;
	
	@Column(name = "order_by")
	private Integer orderBy;
	
	@Column(name = "pinyin")
	private String pinYin;

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

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

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
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
}