package com.fsll.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "member_account_sso")
public class MemberAccountSso implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
    @Column(name = "type")
	private String type;
    
	@Column(name = "account_code")
	private String accountCode;
    
	@Column(name = "session_id")
	private String sessionId;
	
	@Column(name = "trade_id")
	private String tradeId;
	
	@Column(name = "pin_pos")
	private String pinPos;
	
	@Column(name = "pin_check")
	private String pinCheck;
	
	@Column(name = "verify_flag")
	private String verifyFlag;
	
	@Column(name = "verify_time")
	private Date verifyTime;
	
	@Column(name = "pwd_expire_time")
	private Date pwdExpireTime;

	@Column(name = "trade_system_date")
	private Date tradeSystemTime;
	
	@Column(name = "last_login_time")
	private Date lastLoginTime;
	
	@Column(name = "expire_time")
	private Date expireTime;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "error_code")
	private String errorCode;
	
	@Column(name = "error_msg")
	private String errorMsg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getPinPos() {
		return pinPos;
	}

	public void setPinPos(String pinPos) {
		this.pinPos = pinPos;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
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

	public String getPinCheck() {
		return pinCheck;
	}

	public void setPinCheck(String pinCheck) {
		this.pinCheck = pinCheck;
	}

	public Date getPwdExpireTime() {
		return pwdExpireTime;
	}

	public void setPwdExpireTime(Date pwdExpireTime) {
		this.pwdExpireTime = pwdExpireTime;
	}

	public Date getTradeSystemTime() {
		return tradeSystemTime;
	}

	public void setTradeSystemTime(Date tradeSystemTime) {
		this.tradeSystemTime = tradeSystemTime;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getVerifyFlag() {
		return verifyFlag;
	}

	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

}