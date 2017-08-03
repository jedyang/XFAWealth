package com.fsll.wmes.investor.vo;

import java.util.Date;

import javax.persistence.Column;

import net.sf.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.entity.WfProcedureInstanse;
import com.fsll.wmes.entity.WfProcedureInstanseTodo;


public class WorkFlowVO {

	//实例信息
	private String instanseId;
	private String businessId;
	private String invokeCode;
	private String actionId;
	private String flowCode;
	private String flowName;
	private String flowPre;
	private String flowNext;
	private String flowRoleId;
	private String flowCompleted;
	private Integer flowRoleAlluser;//是否需要发送待办给角色所有用户，1-所有，0-单个
	private Integer beginOrEnd;//0-开始，1-中间环节，2、结束
//	private String createBy;
//	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
//	private Date createTime;
	
	private Integer flowState;//流程状态：0:待处理,1:已经处理待下一步，2:已经完成并等待退回上一步，9：已完成

	//实例TODO信息
	private Integer checkStatus;
	private String actionType;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date processDate;
	private String comment;
	private String toDoUrl;
	private String flowUserId;
	
	public WorkFlowVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WorkFlowVO(JSONObject data) {
		super();

		if (null!=data){
			setInstanseId(data.containsKey("instanseId")?data.getString("instanseId"):"");
			setBusinessId(data.containsKey("businessId")?data.getString("businessId"):"");
			setInvokeCode(data.containsKey("invokeCode")?data.getString("invokeCode"):"");
			setActionId(data.containsKey("actionId")?data.getString("actionId"):"");
			setFlowCode(data.containsKey("flowCode")?data.getString("flowCode"):"");
			setFlowName(data.containsKey("flowName")?data.getString("flowName"):"");
			setFlowPre(data.containsKey("flowPre")?data.getString("flowPre"):"");
			setFlowNext(data.containsKey("flowNext")?data.getString("flowNext"):"");
			setFlowRoleId(data.containsKey("flowRoleId")?data.getString("flowRoleId"):"");
			setFlowCompleted(data.containsKey("flowCompleted")?data.getString("flowCompleted"):"");
			setFlowRoleAlluser(data.containsKey("flowRoleAlluser")?StrUtils.getInteger(data.getString("flowRoleAlluser")):null);
			setBeginOrEnd(data.containsKey("beginOrEnd")?StrUtils.getInteger(data.getString("beginOrEnd")):null);
			setFlowState(data.containsKey("flowState")?StrUtils.getInteger(data.getString("flowState")):null);

			try {
				setCheckStatus(data.containsKey("checkStatus")?StrUtils.getInteger(data.getString("checkStatus")):null);
				setActionType(data.containsKey("actionType")?StrUtils.getString(data.getString("actionType")):"");
				setProcessDate(data.containsKey("processDate")?DateUtil.StringToDate(data.getString("processDate"),CoreConstants.DATE_TIME_FORMAT):null);
				setComment(data.containsKey("comment")?StrUtils.getString(data.getString("comment")):"");
				setToDoUrl(data.containsKey("toDoUrl")?StrUtils.getString(data.getString("toDoUrl")):"");
				setFlowUserId(data.containsKey("flowUserId")?StrUtils.getString(data.getString("flowUserId")):"");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public String getInstanseId() {
		return instanseId;
	}

	public void setInstanseId(String instanseId) {
		this.instanseId = instanseId;
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

	public String getFlowRoleId() {
		return flowRoleId;
	}

	public void setFlowRoleId(String flowRoleId) {
		this.flowRoleId = flowRoleId;
	}

	public String getFlowCompleted() {
		return flowCompleted;
	}

	public void setFlowCompleted(String flowCompleted) {
		this.flowCompleted = flowCompleted;
	}

	public Integer getFlowRoleAlluser() {
		return flowRoleAlluser;
	}

	public void setFlowRoleAlluser(Integer flowRoleAlluser) {
		this.flowRoleAlluser = flowRoleAlluser;
	}

	public Integer getBeginOrEnd() {
		return beginOrEnd;
	}

	public void setBeginOrEnd(Integer beginOrEnd) {
		this.beginOrEnd = beginOrEnd;
	}

	/*public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}*/

	public Integer getFlowState() {
		return flowState;
	}

	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
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

	public String getToDoUrl() {
		return toDoUrl;
	}

	public void setToDoUrl(String toDoUrl) {
		this.toDoUrl = toDoUrl;
	}

	public String getFlowUserId() {
		return flowUserId;
	}

	public void setFlowUserId(String flowUserId) {
		this.flowUserId = flowUserId;
	}
	
	
}