package com.fsll.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.core.entity.SysCountry;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "investor_account_contact_addr")
public class InvestorAccountContactAddr implements java.io.Serializable {
	private String id;
	private InvestorAccount account;
	private InvestorAccountContact contact;
	private String addrType;
	private String room;
	private String floor;
	private String building;
	private String nameOfEstate;
	private String streetNo;
	private String district;
	private String province;
	private SysCountry country;
	private String postCode;
	private Date createTime;
	private Date lastUpdate;

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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	public InvestorAccount getAccount() {
		return this.account;
	}

	
	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contact_id")
	@JsonIgnore
	public InvestorAccountContact getContact() {
		return contact;
	}

	public void setContact(InvestorAccountContact contact) {
		this.contact = contact;
	}
	
	@Column(name = "addr_type")
	public String getAddrType() {
		return this.addrType;
	}

	public void setAddrType(String addrType) {
		this.addrType = addrType;
	}

	@Column(name = "room")
	public String getRoom() {
		return this.room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	@Column(name = "floor")
	public String getFloor() {
		return this.floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	@Column(name = "building")
	public String getBuilding() {
		return this.building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	@Column(name = "name_of_estate")
	public String getNameOfEstate() {
		return this.nameOfEstate;
	}

	public void setNameOfEstate(String nameOfEstate) {
		this.nameOfEstate = nameOfEstate;
	}

	@Column(name = "street_no")
	public String getStreetNo() {
		return this.streetNo;
	}

	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}

	@Column(name = "district")
	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "province")
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country")
	@JsonIgnore
	public SysCountry getCountry() {
		return this.country;
	}

	public void setCountry(SysCountry country) {
		this.country = country;
	}

	@Column(name = "post_code")
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}