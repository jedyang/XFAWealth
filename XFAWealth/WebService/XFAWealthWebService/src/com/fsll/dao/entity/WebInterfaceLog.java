package com.fsll.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "web_interface_log")
public class WebInterfaceLog implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @Column(name = "invoker_type")
	private String invokerType;
    
    @Column(name = "result_flag")
	private String resultFlag;
    
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "bus_key")
	private String busKey;
	
	@Column(name = "module_type")
	private String moduleType;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "method")
	private String method;
	
	@Column(name = "memo")
	private String memo;
	
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

	public String getInvokerType() {
		return invokerType;
	}

	public void setInvokerType(String invokerType) {
		this.invokerType = invokerType;
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBusKey() {
		return busKey;
	}

	public void setBusKey(String busKey) {
		this.busKey = busKey;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

}