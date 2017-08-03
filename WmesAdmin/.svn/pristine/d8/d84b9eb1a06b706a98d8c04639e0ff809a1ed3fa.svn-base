package com.fsll.wmes.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "member_sso")
public class MemberSso implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
    @Column(name = "from_type")
	private String fromType;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	@JsonIgnore
	private MemberBase member;
    
	@Column(name = "token_id")
	private String tokenId;
	
	@Column(name = "secure_key")
	private String secureKey;
	
	@Column(name = "ip")
	private String ip;
	
	@Column(name = "terminal")
	private String terminal;
	
	@Column(name = "ter_Model")
	private String terModel;
	
	@Column(name = "browser")
	private String browser;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "expire_time")
	private Date expireTime;
	
	@Column(name = "remark")
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getSecureKey() {
		return secureKey;
	}

	public void setSecureKey(String secureKey) {
		this.secureKey = secureKey;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getTerModel() {
		return terModel;
	}

	public void setTerModel(String terModel) {
		this.terModel = terModel;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}