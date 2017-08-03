package com.fsll.dao.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "fund_anno")
public class FundAnno implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fund_id")
	@JsonIgnore
	private FundInfo fund;
	
	@Column(name = "anno_name")
	private String annoName;
	
	@Column(name = "anno_date")
	private Date annoDate;
	
	@Column(name = "anno_content")
	private String annoContent;
	
	@Column(name = "lang_code")
	private String langCode;
	
    @Column(name = "create_time")
	private Date createTime;
    
    @Column(name = "last_update")
	private Date lastUpdate;
    
    @Column(name = "is_valid")
	private String isValid;
    
    @Transient
	private String fundId;
    @Transient
	private String xh;
    @Transient
	private List fileList;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FundInfo getFund() {
		return fund;
	}

	public void setFund(FundInfo fund) {
		this.fund = fund;
	}

	public String getAnnoName() {
		return annoName;
	}

	public void setAnnoName(String annoName) {
		this.annoName = annoName;
	}

	public Date getAnnoDate() {
		return annoDate;
	}

	public void setAnnoDate(Date annoDate) {
		this.annoDate = annoDate;
	}

	public String getAnnoContent() {
		return annoContent;
	}

	public void setAnnoContent(String annoContent) {
		this.annoContent = annoContent;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public List getFileList() {
		return fileList;
	}

	public void setFileList(List fileList) {
		this.fileList = fileList;
	}
	

}