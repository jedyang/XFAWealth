package com.fsll.wmes.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection="oper_log")
@CompoundIndexes({
    @CompoundIndex(name="code_idx",def="{'login_code':1,'nickName':-1}")
})
public class OperLog implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@Field("member_id")
	@Indexed(name="member_id_idx",unique=false)
	private String memberId;
	
	@Field("login_code")
	private String loginCode;
	
	@Field("nick_name")
	private String nickName;
	
	@Field("client_type")
	private String clientType;
	
	@Field("module_type")
	private String moduleType;
	
	@Field("relate_data")
	private String relateData;
	
	@Field("ip")
	private String ip;
	
	@Field("remark")
	private String remark;
	
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
	@Field("create_time")
	private Date createTime;
	
	//@DBRef 一对多的定义
	//private List<MemberBase> memberBase;
	
	/****** 以下字段不存储数据库,仅用于页面显示 *******/
	@Transient
    private String startTime;//搜索开始时间
	@Transient
    private String endTime;//搜索结束时间
	/****** 以上字段不存储数据库,仅用于页面显示 *******/
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getRelateData() {
		return relateData;
	}

	public void setRelateData(String relateData) {
		this.relateData = relateData;
	}

}