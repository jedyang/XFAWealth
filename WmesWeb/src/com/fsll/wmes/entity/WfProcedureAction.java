package com.fsll.wmes.entity;

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
@Table(name = "wf_procedure_action")
public class WfProcedureAction implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "procedure_id")
	@JsonIgnore
	private WfProcedureInfo procedure;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public WfProcedureInfo getProcedure() {
		return procedure;
	}

	public void setProcedure(WfProcedureInfo procedure) {
		this.procedure = procedure;
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

//	public MemberConsoleRole getFlowRole() {
//		return flowRole;
//	}
//
//	public void setFlowRole(MemberConsoleRole flowRole) {
//		this.flowRole = flowRole;
//	}

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

	public String getFlowRole() {
		return flowRole;
	}

	public void setFlowRole(String flowRole) {
		this.flowRole = flowRole;
	}
}