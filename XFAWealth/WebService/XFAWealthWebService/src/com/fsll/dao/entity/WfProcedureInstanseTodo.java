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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "wf_procedure_instanse_todo")
public class WfProcedureInstanseTodo implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @Column(name = "business_id")
	private String businessId;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "instanse_id")
	@JsonIgnore
	private WfProcedureInstanse instanse;
    
    @Column(name = "flow_code")
	private String flowCode;
    
    @Column(name = "flow_name")
	private String flowName;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "flow_user_id")
	@JsonIgnore
	private MemberBase flowUser;
	
    @Column(name = "check_status")
	private Integer checkStatus;
	
    @Column(name = "action_type")
	private String actionType;
	
    @Column(name = "process_date")
	private Date processDate;
    
    @Column(name = "comment")
	private String comment;
    
    @Column(name = "flow_state")
	private Integer flowState;
    
    @Column(name = "order_no")
	private Integer orderNo;
    
    @Column(name = "is_valid")
	private Integer isValid;
    
    @Column(name = "create_time")
	private Date createTime;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "create_by")
	@JsonIgnore
	private MemberBase creator;
    
    @Column(name = "todo_url")
    private String toDoUrl;
    
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public WfProcedureInstanse getInstanse() {
		return instanse;
	}

	public void setInstanse(WfProcedureInstanse instanse) {
		this.instanse = instanse;
	}

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public MemberBase getFlowUser() {
		return flowUser;
	}

	public void setFlowUser(MemberBase flowUser) {
		this.flowUser = flowUser;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getFlowState() {
		return flowState;
	}

	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public String getToDoUrl() {
		return toDoUrl;
	}

	public void setToDoUrl(String toDoUrl) {
		this.toDoUrl = toDoUrl;
	}
	
	
	
}