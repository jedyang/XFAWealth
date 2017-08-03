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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "wf_procedure_instanse")
public class WfProcedureInstanse implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @Column(name = "business_id")
	private String businessId;
    
    @Column(name = "invoke_code")
	private String invokeCode;
    
    @Column(name = "action_id")
	private String actionId;
    
    @Column(name = "flow_code")
	private String flowCode;
    
    @Column(name = "flow_name")
	private String flowName;
    
    @Column(name = "flow_pre")
	private String flowPre;
  
    @Column(name = "flow_next")
	private String flowNext;
    
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "flow_role")
//	@JsonIgnore
//	private MemberConsoleRole flowRole;
    @Column(name = "flow_role")
	private String flowRole;
    
    @Column(name = "flow_role_alluser")
	private Integer flowRoleAlluser;
    
    @Column(name = "flow_completed")
	private String flowCompleted;
    
    @Column(name = "begin_or_end")
	private Integer beginOrEnd;
    
    @Column(name = "order_no")
	private Integer orderNo;
    
    @Column(name = "create_time")
	private Date createTime;
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "create_by")
	@JsonIgnore
	private MemberBase creator;
    
    @Column(name = "flow_state")
	private Integer flowState;

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

	public String getInvokeCode() {
		return invokeCode;
	}

	public void setInvokeCode(String invokeCode) {
		this.invokeCode = invokeCode;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
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

	public String getFlowPre() {
		return flowPre;
	}

	public void setFlowPre(String flowPre) {
		this.flowPre = flowPre;
	}

	public String getFlowNext() {
		return flowNext;
	}

	public void setFlowNext(String flowNext) {
		this.flowNext = flowNext;
	}

	public String getFlowRole() {
		return flowRole;
	}

	public void setFlowRole(String flowRole) {
		this.flowRole = flowRole;
	}

	public Integer getFlowRoleAlluser() {
		return flowRoleAlluser;
	}

	public void setFlowRoleAlluser(Integer flowRoleAlluser) {
		this.flowRoleAlluser = flowRoleAlluser;
	}

	public String getFlowCompleted() {
		return flowCompleted;
	}

	public void setFlowCompleted(String flowCompleted) {
		this.flowCompleted = flowCompleted;
	}

	public Integer getBeginOrEnd() {
		return beginOrEnd;
	}

	public void setBeginOrEnd(Integer beginOrEnd) {
		this.beginOrEnd = beginOrEnd;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public Integer getFlowState() {
		return flowState;
	}

	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
	}
    
    
}