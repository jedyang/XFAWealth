package com.fsll.wmes.distributor.action.front;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.WSConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.distributor.vo.AccountFitlerVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountBank;
import com.fsll.wmes.entity.InvestorAccountContact;
import com.fsll.wmes.entity.InvestorAccountContactAddr;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberConsoleRoleMember;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.service.InvestorServiceForConsole;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.investor.vo.FileVO;
import com.fsll.wmes.investor.vo.WorkFlowVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.role.service.MemberConsoleRoleService;
import com.fsll.wmes.rpq.service.RpqService;
import com.fsll.wmes.web.service.WebFriendService;
import com.fsll.wmes.web.service.WebReadToDoService;

/*****
 * 
 * 
 * @author mqzou 2016-11-09
 */
@Controller
@RequestMapping("/front/distributor")
public class DistributorInfoAct extends CoreBaseAct {
	public static class FlowStates {
		public static final String WAIT_TO_DO = "0";
		public static final String RETURN_NEXT = "1";
		public static final String RETURN_PRE = "2";
		public static final String DONE = "9";
		public static final String TODO_DONE = "1";
		public static final String TODO_WAIT_FOR = "0";
	}
	
	@Autowired
	private InvestorServiceForConsole investorServiceForConsole;
	@Autowired
	private MemberManageServiceForConsole memberManageServiceForConsole;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private InvestorService investorService;
	
	@Autowired
	private IfafirmManageService ifafirmManageService;

	@Autowired
	private RpqService rpqService;
	
	@Autowired
	private MemberConsoleRoleService memberConsoleRoleService;
	
	@Autowired
	private CrmCustomerService customerService;
	@Autowired
	private PortfolioHoldService portfolioHoldService;

	@Autowired
	private WebReadToDoService webReadToDoService;

	@Autowired
	private IfaInfoService ifaInfoService;

	@Autowired
	private WebFriendService webFriendService;
	@Autowired
	private DistributorService distributorService;
	
	/**
	 * 待审批的投资人开户列表分页列表
	 * 
	 * @author mqzou
	 * @date  2016-08-03
	 */
	@RequestMapping(value = "/approvalList")
	public String approvalList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		this.isMobileDevice(request, model);
		return "console/investor/approvallist";
	}
	
	/**
	 * 获取待审批的投资人开户列表
	 * 
	 * @author mqzou
	 * @date 2016-08-03
	 */
	@RequestMapping(value = "/approvalJsonList")
	public void findInvestorAccountForApprove(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);
		String keyWord = request.getParameter("");
		if (null != keyWord && !"".equals(keyWord)) {
			try {
				keyWord = URLDecoder.decode(keyWord, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		InvestorAccount account = new InvestorAccount();
		jsonPaging = investorServiceForConsole.findInvestorAccountForApproval(jsonPaging, account);
		/*MemberAdmin admin=memberManageServiceForConsole.findAdminByMemberId(curMember.getId());
		if(null!=admin && (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(admin.getType())|| CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(admin.getType()))){
			InvestorAccount account = new InvestorAccount();
			MemberDistributor distributor = admin.getDistributor();
			

			account.setDistributor(distributor);

			jsonPaging = this.getJsonPaging(request);
			jsonPaging = investorService.findInvestorAccountForApproval(jsonPaging, account);
		}else {
			jsonPaging=new JsonPaging();
		}*/
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 获取投资人账户列表
	 * 
	 * @author mqzou
	 * @date 2016-08-23
	 */
	@RequestMapping(value = "/JsonList")
	public void findInvestorAccountList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_LOGIN);

		String memberId=request.getParameter("memberId");
		String langCode=getLoginLangFlag(request);
		InvestorAccount account =new InvestorAccount();
		if(null!=memberId && !"".equals(memberId)){
		    MemberBase memberBase=memberBaseService.findById(memberId);
		    account.setMember(memberBase);
		}
		
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==curMember.getMemberType()){
			MemberAdmin admin=memberManageServiceForConsole.findAdminByMemberId(curMember.getId());
			MemberIfa ifa=new MemberIfa();
			ifa.setIfafirm(admin.getIfafirm());
			account.setIfa(ifa);
		}
		jsonPaging=this.getJsonPaging(request);
		jsonPaging=investorServiceForConsole.findInvestorAccountList(jsonPaging, account, langCode);

		this.toJsonString(response, jsonPaging);
	}
	
	/**
     * Distributor 的客户账户列表
     * @author mqzou
	 * @throws ParseException 
     * @date 2016-09-19
     */
	@RequestMapping(value = "/clientManagement", method = RequestMethod.GET)
	public String ifaAccountList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ParseException{
        String langCode=getLoginLangFlag(request);
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR != curMember.getMemberType()) {
			return "";
		}
		MemberAdmin admin = memberManageServiceForConsole.findAdminByMemberId(curMember.getId());
		MemberDistributor distributor = admin.getDistributor();
		InvestorAccount account = new InvestorAccount();
		account.setDistributor(distributor);
		List ifafirmList = investorService.findAccountIfafirm(account,langCode);
		model.put("ifafirmList", ifafirmList);
		return "distributor/clientManagement";
		
		
	}
	/**
     * distributor 的客户账户列表
     * @author mqzou
	 * @throws ParseException 
     * @date 2016-11-10
     * modify by mqzou  2017-06-08
     */
	@RequestMapping(value="/accountListJson")
	public void findAccountList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ParseException{
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String distributorId=ssoVO.getDistributorId();
		String langCode=getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String currency = request.getParameter("cur");// 基本货币
		String ifafirmId = request.getParameter("ifafirmId");
		String keyword=request.getParameter("keyword");
		String accountStatus=request.getParameter("accountStatus");
		String docDate=request.getParameter("docDate");
		String rpqDate=request.getParameter("rpqDate");
		String market=request.getParameter("market");
		String asset=request.getParameter("asset");
		
		if(null!=keyword && !"".equals(keyword)){
			keyword=toUTF8String(keyword);
		}
		
		if(null==currency || "".equals(currency))
			currency=ssoVO.getDefCurrency();
		if(null==currency || "".equals(currency))
			currency=CommonConstants.DEF_CURRENCY;
		
		AccountFitlerVO vo=new AccountFitlerVO();
		vo.setIfafirmId(ifafirmId);
		vo.setDistributorId(distributorId);
		vo.setCurrency(currency);
		vo.setLangCode(langCode);
		
		if (null != docDate && !"".equals(docDate)) {
			String endDate = "";
			if ("3D".equals(docDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, 3);
			}
			if ("1W".equals(docDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, 7);
			}
			if ("2W".equals(docDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, 14);
			}
			if ("1M".equals(docDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, 30);
			}
			if ("expire".equals(docDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, -1);
			}
			vo.setDocEnd(endDate);
			
		}
		if (null != rpqDate && !"".equals(rpqDate)) {
			String endDate = "";
			if ("3D".equals(rpqDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, 3);
			}
			if ("1W".equals(rpqDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, 7);
			}
			if ("2W".equals(rpqDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, 14);
			}
			if ("1M".equals(rpqDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, 30);
			}
			if ("expire".equals(rpqDate)){
				endDate = DateUtil.getCurDateStr(Calendar.DATE, -1);
			}
			vo.setRpqEnd(endDate);
			
		}

		String maxMarket="";
		String minMarket="";
		if(null!=market && !"".equals(market)){
			market=market.replace("M", "");
			if(market.contains("&lt;")){
				maxMarket=String.valueOf(Double.parseDouble(market.replace("&lt;", ""))*1000000);
			}else if (market.contains("&gt;")) {
				minMarket=String.valueOf(Double.parseDouble(market.replace("&gt;", ""))*1000000);
			}else if (market.contains("~")) {
				String[] values=market.split("~");
				if(values.length==2){
					minMarket=String.valueOf(Double.parseDouble(values[0])*1000000);
					maxMarket=String.valueOf(Double.parseDouble(values[1])*1000000);
				}
			}
		}
		
		vo.setMinMarket(minMarket);
		vo.setMaxMarket(maxMarket);
		
		String minAsset="";
		String maxAsset="";
		if(null!=asset && !"".equals(asset)){
			asset=asset.replace("M", "");
			if(asset.contains("&lt;")){
				maxAsset=String.valueOf(Double.parseDouble(asset.replace("&lt;", ""))*1000000);
			}else if (asset.contains("&gt;")) {
				minAsset=String.valueOf(Double.parseDouble(asset.replace("&gt;", ""))*1000000);
			}else if (asset.contains("~")) {
				String[] values=asset.split("~");
				if(values.length==2){
					minAsset=String.valueOf(Double.parseDouble(values[0])*1000000);
					maxAsset=String.valueOf(Double.parseDouble(values[1])*1000000);
				}
			}
		}
		vo.setMinAsset(minAsset);
		vo.setMaxAsset(maxAsset);
		
		vo.setIsValid(accountStatus);
		
	//	vo.setAsset(asset);
		vo.setKeyword(keyword.trim());
		
		jsonPaging = getJsonPaging(request);
		jsonPaging = distributorService.findAccountList(jsonPaging, vo);


		result.put("accountList", jsonPaging.getList());
		result.put("total", jsonPaging.getTotal());
		result.put("currency", currency);
		JsonUtil.toWriter(result, response);
	}
	
    
    /***********************************开户审批******************************************/
    /**
	 * 投资账号审批（Ifafirm,Distributor审批）
	 * 
	 * @author mqzou
	 * @date 2016-11-11
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/updateFlowStatus", method = RequestMethod.POST)
	public void updateFlowStatus(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// modify by mqzou 2016-08-31
		String actionMsg = "global.success";
		String comment = request.getParameter("comment");
		String status = request.getParameter("status");
		String accountId = request.getParameter("accountId");
		String todoUrl = "/front/distributor/accountApprove.do?accountId=" + accountId;
		
		MemberBase curMember=getLoginUser(request);
		
		String userId = curMember.getId();

		//获取配置文件的配置
		String wsServer = getCompWsServer();
		
		// 获取当前工作流信息
		String urlString = wsServer + CoreConstants.COMP_WS_WORKFLOW_QUERY;
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("invokCode", CommonConstantsWeb.WORK_FLOW_OPENACCOUNT);
		jsonObj.put("businessId", accountId);
		jsonObj.put("flowUserId", userId);
		String jsonString = jsonObj.toString();
		String getResult = sendWebServiceByBody(urlString, jsonString);

		String instanseId = "";
		String flowCode="";
		Map<String, Object> obj = new HashMap<String, Object>();
		JSONObject memberDataJSON = (JSONObject) JSONSerializer.toJSON(getResult);
		if (null != memberDataJSON) {
			JSONObject data = memberDataJSON.getJSONObject("data");
			if (null != data ) {
				
				WorkFlowVO workFlowVO=new WorkFlowVO(data);
				flowCode=workFlowVO.getFlowCode();
				instanseId=workFlowVO.getInstanseId();
				
				
				// 更新工作流
				urlString = wsServer + CoreConstants.COMP_WS_WORKFLOW_UPDATE;
				jsonObj = new JSONObject();

				jsonObj.put("invokCode", CommonConstantsWeb.WORK_FLOW_OPENACCOUNT);
				jsonObj.put("businessId", accountId);
				jsonObj.put("flowCode", flowCode);
				jsonObj.put("instanseId",instanseId);
				jsonObj.put("checkStatus", status);
				jsonObj.put("comment", comment);
				jsonObj.put("flowUserId", userId);

				jsonString = jsonObj.toString();
				String result = sendWebServiceByBody(urlString, jsonString);

				JSONObject resultObject = (JSONObject) JSONSerializer.toJSON(result);

				
				String completeStatus = "";// 完成状态 0 待选择然后下一步 1本次流程已完成不需要下一步操作并且待下一个流程
											// 2开户流程已完全完成
				if (null != resultObject) {
					String ret = resultObject.getString("ret");
					if (WSConstants.SUCCESS.equals(ret)) {
						String curFlowStatus = resultObject.getString("currFlowState");
						 data = resultObject.getJSONObject("data");
						 workFlowVO =new WorkFlowVO(data);
						if (FlowStates.RETURN_NEXT.equals(curFlowStatus)) {// 当前环节已完成，进入下一环节
							//if (null != workFlowVO ) {
								instanseId = workFlowVO.getInstanseId();
								String roleId = workFlowVO.getFlowRoleId();
								String flowRoleAlluser =String.valueOf(workFlowVO.getFlowRoleAlluser());
								String beginOrEnd = String.valueOf(workFlowVO.getBeginOrEnd());
								flowCode=workFlowVO.getFlowCode();

								if (!"2".equals(beginOrEnd)) {// 如果流程未结束
									if ("1".equals(flowRoleAlluser)) {
//										List<MemberConsoleRoleMember> userList = memberConsoleRoleService.findConsoleRoleMembers(roleId);
										List<MemberBase> userList = investorService.findMemberByRole(null, accountId, roleId);
										workFlowVO = addFlow(accountId, userId, instanseId,workFlowVO, userList, flowCode, todoUrl);

										InvestorAccount account = investorService.findInvestorAccountById(accountId);

										if ("1".equals(status)) {
											if (StrUtils.isEmpty(roleId) || "IFA".equals(roleId)){
												account.setFlowStatus("0");//流程进行中
											}else if (roleId.startsWith("I")) {
												account.setFlowStatus("3");//等待IFAFIRM处理
		                                    }else if (roleId.startsWith("D")) {
												account.setFlowStatus("4");//等待Distributor处理
		                                    }
										} else {
											account.setFlowStatus("2");// 2流程审核不通过结束返回
											//旧开户状态:-2待推送,-1已推送等待结果,1成功开户,0失败
											//新开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
//											account.setOpenStatus("0");//流程不通过
											account.setOpenStatus("4");//流程不通过
										}

										String flowId = "";
										if (null != data && !"".equals(data) && !"null".equals(data)) {
											jsonObj = (JSONObject) JSONSerializer.toJSON(data);
											flowId = jsonObj.getString("instanseId");
										}
										account.setFlowId(flowId);

										// 更新账户信息
										investorService.saveOrUpdateInvestorAccount(account);
										completeStatus = "1";// 本次环节已完成不需要下一步操作，进入下一个环节
									} else {
										completeStatus = "0";// 0 待选择然后下一步
										obj.put("roleId", roleId);
										obj.put("instanseId", instanseId);
										obj.put("actionStatus", status);
										//obj.put("", value)
									}
								} else {// 流程结束
									//obj.put("accountId", accountId);
									completeStatus = "2";// 整个流程结束
									
								}
							//}
						} else if (FlowStates.RETURN_PRE.equals(curFlowStatus)) {// 退回上一环节
							instanseId = workFlowVO.getInstanseId();
							String roleId = workFlowVO.getFlowRoleId();
							String flowRoleAlluser = String.valueOf(workFlowVO.getFlowRoleAlluser());
							
							InvestorAccount account = investorService.findInvestorAccountById(accountId);
							List<MemberBase> userList = new ArrayList<MemberBase>();
							if ("1".equals(flowRoleAlluser)) {//需所有人审批，则发待办给所有人，无需选择
								if (StrUtils.isEmpty(roleId) || "IFA".equals(roleId)){
									//当流程退回给IFA（目前所有审批角色都会退回到IFA）
									userList.add(account.getIfa().getMember());
									todoUrl = "/front/investor/accountSubmit.do?accountId=" + accountId;
								}else{
	//								List<MemberConsoleRoleMember> userList = memberConsoleRoleService.findConsoleRoleMembers(roleId);
									userList = investorService.findMemberByRole(null, accountId, roleId);
								}
								
								//发送待办到下一环节人
								workFlowVO = addFlow(accountId, userId, instanseId, workFlowVO, userList, flowRoleAlluser, todoUrl);

								if ("1".equals(status)) {
									if (StrUtils.isEmpty(roleId) || "IFA".equals(roleId)){
										account.setFlowStatus("0");//流程进行中
									}else if (roleId.startsWith("I")) {
										account.setFlowStatus("3");//等待IFAFIRM处理
                                    }else if (roleId.startsWith("D")) {
										account.setFlowStatus("4");//等待Distributor处理
                                    }
								} else {
									account.setFlowStatus("2");// 2流程审核不通过结束返回
									account.setOpenStatus("4");//流程不通过
								}
								String flowId = "";
								if (null != data && !"".equals(data) && !"null".equals(data)) {
									jsonObj = (JSONObject) JSONSerializer.toJSON(data);
									flowId = jsonObj.getString("instanseId");
								}
								account.setFlowId(flowId);

								// 更新账户信息
								investorService.saveOrUpdateInvestorAccount(account);
								completeStatus = "1";// 本次流程已完成不需要下一步操作并且待下一个流程
								
							} else {//非所有人审批，则选人
								if (StrUtils.isEmpty(roleId) || "IFA".equals(roleId)){
									//当流程退回给IFA（目前所有审批角色都会退回到IFA）
									userList.add(account.getIfa().getMember());
									//发送待办到下一环节人
									workFlowVO = addFlow(accountId, userId, instanseId, workFlowVO, userList, flowRoleAlluser, todoUrl);

									if (!"1".equals(status)) {
										account.setFlowStatus("2");// 2流程审核不通过结束返回
										account.setOpenStatus("4");//流程不通过
									}
									String flowId = "";
									if (null != data && !"".equals(data) && !"null".equals(data)) {
										jsonObj = (JSONObject) JSONSerializer.toJSON(data);
										flowId = jsonObj.getString("instanseId");
									}
									account.setFlowId(flowId);

									// 更新账户信息
									investorService.saveOrUpdateInvestorAccount(account);
									completeStatus = "1";// 本次流程已完成不需要下一步操作并且待下一个流程
								}else{
									completeStatus = "0";// 0 待选择然后下一步
									obj.put("roleId", roleId);
									obj.put("instanseId", instanseId);
									obj.put("actionStatus", status);
								}
							}

						} else if (FlowStates.DONE.equals(curFlowStatus)) {// 整个流程结束
							//obj.put("accountId", accountId);
							completeStatus = "2";// 整个流程结束
						} else {
							completeStatus = "0";// 0 待当前环节下一审批人处理，或选择下一环节处理人
							String roleId = workFlowVO.getFlowRoleId();
							String flowCompleted =String.valueOf(workFlowVO.getFlowCompleted());
							String beginOrEnd = String.valueOf(workFlowVO.getBeginOrEnd());

							if (!"2".equals(beginOrEnd)) {// 如果流程未结束
								if ("A".equals(flowCompleted)) {
									completeStatus = "3";//待当前环节下一审批人处理
								}
							}
							obj.put("roleId", roleId);
							obj.put("instanseId", instanseId);
							obj.put("actionStatus", status);
						}
						obj.put("result", true);
						obj.put("status", completeStatus);
						obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), actionMsg, null));
					} else {
						obj.put("result", false);
						String msg = resultObject.getString("errorMsg");
						obj.put("msg", msg);
					}
				}
			}else {
				obj.put("result", false);
				obj.put("status", "10");//获取待办流程失败
				obj.put("msg", "获取待办流程失败");
			}
		}else {
			obj.put("result", false);
			obj.put("status", "10");//获取待办流程失败
			obj.put("msg", "获取待办流程失败");
		}
		obj.put("accountId", accountId);
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 添加待办
	 * @param accountId
	 * @param userId
	 * @param instanseId
	 * @param data
	 * @param userList
	 * @return
	 */
	private WorkFlowVO addFlow(String accountId, String userId, String instanseId,
			WorkFlowVO data, List<MemberBase> userList,String flowCode, String todoUrl) {
		String urlString;
		JSONObject jsonObj;
		String jsonString;
		String result;
		JSONObject resultObject;
		WorkFlowVO vo=new WorkFlowVO();
		//获取配置文件的配置
		String wsServer = getCompWsServer();
		List<MemberBase> resultList = new ArrayList<MemberBase>();
		for (MemberBase x:userList){
			if (!x.getId().equals(userId))//过滤本人
				resultList.add(x);
		}
		if (!resultList.isEmpty()) {
			urlString = wsServer +  CoreConstants.COMP_WS_WORKFLOW_INSERT;
			Iterator it = userList.iterator();
			while (it.hasNext()) {
				MemberBase user = (MemberBase) it.next();
				jsonObj = new JSONObject();
				jsonObj.put("invokCode",CommonConstantsWeb.WORK_FLOW_OPENACCOUNT);
				jsonObj.put("businessId", data.getBusinessId());
				jsonObj.put("instanseId", data.getInstanseId());
				jsonObj.put("flowUserIds",  user.getId());
				jsonObj.put("userId", userId);
				jsonObj.put("todoUrl", todoUrl);
				jsonObj.put("flowCode", flowCode);
				//jsonObj.put("userId", user.getMember().getId());
				// jsonArray.add(jsonObj);
				jsonString = jsonObj.toString();
				result = sendWebServiceByBody(urlString, jsonString);
				resultObject = new JSONObject();
				resultObject = (JSONObject) JSONSerializer.toJSON(result);
				JSONObject jsonData=resultObject.getJSONObject("data");
				vo=new WorkFlowVO(jsonData);
			}
		}
		return vo;
	}

	/**
	 * 设置审批人后添加待办
	 * 
	 * @author mqzou
	 * @date 2016-09-02
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/submitApprover", method = RequestMethod.POST)
	public void insertFlow(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		JSONObject jsonObj = new JSONObject();
		Map<String, Object> obj = new HashMap<String, Object>();
		
		String userId = request.getParameter("userId");
		String accountId = request.getParameter("accountId");
		String instanseId = request.getParameter("instanceId");
		String status = request.getParameter("status");

		if (StrUtils.isEmpty(userId) || StrUtils.isEmpty(accountId) || StrUtils.isEmpty(instanseId)|| StrUtils.isEmpty(status)){
			obj.put("result", false);
			obj.put("msg", "Next flow user is missing.");
			JsonUtil.toWriter(obj, response);
		}
		
		//获取配置文件的配置
		String wsServer = getCompWsServer();
		
		String urlString = wsServer + CoreConstants.COMP_WS_WORKFLOW_INSERT;
		
		String todoUrl = "/front/distributor/accountApprove.do?accountId=" + accountId;
		jsonObj.put("invokCode", CommonConstantsWeb.WORK_FLOW_OPENACCOUNT);
		jsonObj.put("businessId", accountId);
		jsonObj.put("instanseId", instanseId);
		jsonObj.put("flowUserIds", userId);
		jsonObj.put("todoUrl", todoUrl);
		jsonObj.put("userId", userId);
		String jsonString = jsonObj.toString();
		String result = sendWebServiceByBody(urlString, jsonString);

		jsonObj = (JSONObject) JSONSerializer.toJSON(result);
		
		if (WSConstants.SUCCESS.equals(jsonObj.getString("ret"))) {
			obj.put("result", true);
		} else {
			obj.put("result", false);
			obj.put("msg", jsonObj.getString("errorMsg"));

		}
		JSONObject data=jsonObj.getJSONObject("data");
		WorkFlowVO vo=new WorkFlowVO(data);
		accountId=vo.getBusinessId();
		
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		String flowStatus = account.getFlowStatus();
		int flowInt = Integer.parseInt(flowStatus);
		String flowId = "";
		flowId=vo.getInstanseId();
		if ("1".equals(status)) {
			account.setFlowStatus(String.valueOf(flowInt + 1));// 审批通过转到下一步
		} else {
			account.setFlowStatus("2");// 退回到ifa提交以前
		}
		account.setFlowId(flowId);

		// 更新账户信息
		investorService.saveOrUpdateInvestorAccount(account);
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 输入帐号页面
	 * 
	 * @author mqzou
	 * @date 2016-09-05
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/inputAccountNo", method = RequestMethod.GET)
	public String showAccountComplete(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		/*
		 * String roleId = request.getParameter("roleId"); List userList =
		 * sysRoleService.findUserInRole(roleId); model.put("users", userList);
		 */

		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		return "console/investor/approve/dialog_completeaccount";
	}

	/**
	 * 写入帐号
	 * 
	 * @author mqzou
	 * @date 2016-09-05
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/completeAccountNo", method = RequestMethod.POST)
	public void completeAccountNo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode="sc";//暂定
		MemberBase loginUser=getLoginUser(request);
		String accountId = request.getParameter("accountId");
		String accountNo = request.getParameter("accountNo");
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		MemberIndividual individual=memberBaseService.findIndividualMember(account.getMember().getId());
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != account) {
			account.setAccountNo(accountNo);
			//旧开户状态:-2待推送,-1已推送等待结果,1成功开户,0失败
			//新开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
			account.setOpenStatus("3");//开户成功
			account.setFlowStatus("");
			account = investorService.saveOrUpdateInvestorAccount(account);
			if (null != account)
				obj.put("result", true);
			else {
				obj.put("result", false);
			}
		} else {
			obj.put("result", false);
		}
		
		PortfolioHoldAccount holdAccount=new PortfolioHoldAccount();
		holdAccount.setAccount(account);
		holdAccount.setAccountNo(account.getAccountNo());
		holdAccount.setMember(account.getMember());
		holdAccount.setCreateTime(DateUtil.getCurDate());
		holdAccount.setLastUpdate(DateUtil.getCurDate());
		//holdAccount.setCurrencyCode(account.getBaseCurrency());
		holdAccount.setIfa(account.getIfa());
		holdAccount.setAccountType("1000000000");
		holdAccount.setBaseCurrency(account.getBaseCurrency());
		holdAccount=portfolioHoldService.saveHoldAccount(holdAccount);
		CrmCustomer customer=customerService.findByIfaAndMember(account.getIfa().getId(), account.getMember().getId());
		customer.setClientType("1");
		customerService.saveCustomer(customer);
		
		
		String msg=getProperty(langCode, "open.success.investor");
		msg=msg.replace("{investor_name}", individual.getLastName()+" "+individual.getFirstName()).replace("{distributor_name}", account.getDistributor().getCompanyName());
		sendWebReadToDo(loginUser, account.getMember(),"2",  CommonConstantsWeb.WORK_FLOW_OPENACCOUNT, accountId, msg, "开户通知");
		msg=getProperty(langCode, "open.success.ifa");
		msg=msg.replace("{investor_name}", individual.getLastName()+" "+individual.getFirstName()).replace("{distributor_name}", account.getDistributor().getCompanyName());
	    sendWebReadToDo(loginUser, account.getMember(),"2",  CommonConstantsWeb.WORK_FLOW_OPENACCOUNT, accountId, msg, "开户通知");
	    sendAccountEmail(accountId, langCode);
	    sendPwdEmail(accountId, langCode);
	   	    
	    //添加数据到好友表
	    MemberBase member=account.getMember();
	    MemberBase memberIfa=account.getIfa().getMember();
	    WebFriend friend=webFriendService.getWebFriend(memberIfa.getId(), member.getId(),CommonConstantsWeb.WEB_FRIEND_BUDDY);
	    if(null==friend){
	    	saveFriend(memberIfa, member, CommonConstantsWeb.WEB_FRIEND_BUDDY);
	    }
	    friend=webFriendService.getWebFriend( member.getId(),memberIfa.getId(),CommonConstantsWeb.WEB_FRIEND_BUDDY);
	    if(null==friend){
	    	saveFriend(member,memberIfa, CommonConstantsWeb.WEB_FRIEND_BUDDY);
	    }
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 新增好友
	 * @author mqzou 2017-02-16
	 * @param fromMember
	 * @param toMember
	 * @param relationship
	 */
	private void saveFriend(MemberBase fromMember,MemberBase toMember,String relationship){
		WebFriend friend=new WebFriend();
		friend.setCheckResult("1");
		friend.setCheckTime(DateUtil.getCurDate());
		friend.setCreateTime(DateUtil.getCurDate());
		friend.setFromMember(fromMember);
		friend.setLastUpdate(DateUtil.getCurDate());
		friend.setRelationships(relationship);
		friend.setToMember(toMember);
		webFriendService.updateWebFriend(friend);
	}
	
	/**
	 * 发送账户邮件给客户 
	 * @author mqzou 2016-12-06
	 * @param accountId
	 */
	private void sendAccountEmail(String accountId,String langCode){
		List<String> valList=new ArrayList<String>();
		InvestorAccount account=investorService.findInvestorAccountById(accountId);
		if(null!=account){
			MemberIfa ifa=account.getIfa();
			MemberIndividual individual=memberBaseService.findIndividualMember(account.getMember().getId());
			MemberDistributor distributor=account.getDistributor();
			if(null!=ifa){
				//MemberIfafirm ifafirm=ifa.getIfafirm();
				valList.add("http://192.168.138.99:8181/wmes/");
				valList.add("http://192.168.138.99:8181/wmes/");
				valList.add(individual.getLastName()+" "+individual.getFirstName() );
				valList.add(distributor.getCompanyName());
				valList.add(account.getAccountNo());
				valList.add(ifa.getLastName()+" "+ifa.getFirstName());
				valList.add(DateUtil.getCurDateStr());
				valList.add("");
				valList.add("");
				valList.add(distributor.getCompanyName());
				String subject=PropertyUtils.getPropertyValue(langCode,"investor.open.account.subject",null);
		        String msg=PropertyUtils.getPropertyValue(langCode,"investor.open.account.msg",valList.toArray());
		        //System.out.println(msg);
		        sendEmail("openAccount", ifa.getMember(), account.getMember(), account.getMember().getEmail(), subject, msg, account.getId());
			}
		}
	}
	/**
	 * 发送账户密码邮件给客户 
	 * @author mqzou 2016-12-06
	 * @param accountId
	 */
	private void sendPwdEmail(String accountId,String langCode){
		List<String> valList=new ArrayList<String>();
		InvestorAccount account=investorService.findInvestorAccountById(accountId);
		if(null!=account){
			MemberIfa ifa=account.getIfa();
			MemberIndividual individual=memberBaseService.findIndividualMember(account.getMember().getId());
			MemberDistributor distributor=account.getDistributor();
			if(null!=ifa){
				//MemberIfafirm ifafirm=ifa.getIfafirm();
				valList.add("http://192.168.138.99:8181/wmes/");
				valList.add("http://192.168.138.99:8181/wmes/");
				valList.add(individual.getLastName()+" "+individual.getFirstName() );
				valList.add(distributor.getCompanyName());
				//valList.add(account.getAccountNo());
				valList.add(ifa.getLastName()+" "+ifa.getFirstName());
				valList.add(DateUtil.getCurDateStr());
				valList.add("");
				valList.add("");
				valList.add(distributor.getCompanyName());
				String subject=PropertyUtils.getPropertyValue(langCode,"investor.open.pwd.subject",null);
		        String msg=PropertyUtils.getPropertyValue(langCode,"investor.open.pwd.msg",valList.toArray());
		        //System.out.println(msg);
		        sendEmail("openAccount", ifa.getMember(), account.getMember(), account.getMember().getEmail(), subject, msg, account.getId());
			}
		}
	}
	
	

	/**
	 * 开户申请审批选择审批人页面（IFA、IFA supervisor）
	 * 
	 * @author mqzou
	 * @date 2016-09-01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showSelectUserDialog", method = RequestMethod.GET)
	public String showSelectUserDialog(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String roleId = request.getParameter("roleId");
		String accountId = request.getParameter("accountId");
//		List<MemberConsoleRoleMember> userList = memberConsoleRoleService.findConsoleRoleMembers(roleId);
//		List list=new ArrayList();
//		if(null!=userList){
//			Iterator it=userList.iterator();
//			while (it.hasNext()) {
//				MemberConsoleRoleMember object = (MemberConsoleRoleMember) it.next();
//				list.add(object.getMember());
//			}
//		}
		List<MemberBase> userList = investorService.findMemberByRole(null, accountId, roleId);
		List<MemberBase> result = new ArrayList<MemberBase>();
		for (MemberBase x:userList){
			if (!x.getId().equals(curMember.getId()))//过滤本人
				result.add(x);
		}
		model.put("users", result);

		return "console/investor/approve/dialog_selectapproveuser";
	}

     /**
	 * 开户申请审批页面（IFA、IFA supervisor）
	 * 
	 * @author mqzou
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accountApprove", method = RequestMethod.GET)
	public String accountApprove(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String accountId = request.getParameter("accountId");
		String langCode = getLoginLangFlag(request);
		model.put("accountId", accountId);
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		//更新待办消息状态为已读
		try {
			webReadToDoService.updateReadToDoReaded(curMember, accountId);
        } catch (Exception e) {
	        // TODO: handle exception
        	e.printStackTrace();
        }
		
		
		if (null != curMember) {
			if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == curMember.getMemberType()) {
				model.put("Processing", "IFA");
			} else if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR == curMember.getMemberType()) {
				model.put("Processing", "Distributor");
			}
		}

		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		if (null != account) {
			MemberIfa ifa = account.getIfa();
			if (null != ifa) {
				// ifa信息
				IfaVO vo = new IfaVO();
				vo.setId(ifa.getId());
				vo.setAddress(ifa.getAddress());
				if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
					MemberIfafirmEn ifafirm = ifafirmManageService.findIfafirmEnById(ifa.getIfafirm().getId());
					vo.setCompanyIfafirm(null != ifafirm ? ifafirm.getCompanyName() : "");
				} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					MemberIfafirmSc ifafirm = ifafirmManageService.findIfafirmScById(ifa.getIfafirm().getId());
					vo.setCompanyIfafirm(null != ifafirm ? ifafirm.getCompanyName() : "");
				} else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					MemberIfafirmTc ifafirm = ifafirmManageService.findIfafirmTcById(ifa.getIfafirm().getId());
					vo.setCompanyIfafirm(null != ifafirm ? ifafirm.getCompanyName() : "");
				}
				vo.setIconUrl(ifa.getMember().getIconUrl());
				vo.setCompanyIfafirmId(null != ifa.getIfafirm() ? ifa.getIfafirm().getId() : "");
				vo.setCompanyType(ifa.getCompanyType());
				vo.setCountry(null != ifa.getCountry() ? ifa.getCountry().getCountryName(langCode) : "");
				vo.setEmail(null != ifa.getMember() ? ifa.getMember().getEmail() : "");

				vo.setFirstName(ifa.getFirstName());
				vo.setGender(ifa.getGender());
				vo.setIntroduction(ifa.getIntroduction());
				vo.setInvestLifeBegin(ifa.getInvestLifeBegin());
				/*vo.setInvestStyle(ifa.getInvestStyle());
				vo.setLanguageDesc(ifa.getLanguageDesc());*/
				vo.setLastName(ifa.getLastName());
				vo.setLoginCode(null != ifa.getMember() ? ifa.getMember().getLoginCode() : "");
				vo.setMemberId(null != ifa.getMember() ? ifa.getMember().getId() : "");
				vo.setMobileNumber(null != ifa.getMember() ? ifa.getMember().getMobileNumber() : "");
				vo.setNameChn(ifa.getNameChn());
				vo.setNationality(null != ifa.getNationality() ? ifa.getNationality().getCountryName(langCode) : "");
				vo.setNickName(ifa.getFirstName());
				model.put("ifa", vo);

				// Application list信息

				List<AccessoryFile> fileList = investorService.findSubmitDocList("submit_doc", accountId);
				model.put("fileList", fileList);


				List hisList = investorServiceForConsole.findApproveHis(accountId);
				model.put("approveHis", hisList);

				// 文档审核列表信息-scshi
				String mainContactId = investorService.getAccountContactId(accountId, "M");
				List<DocListVO> mainDoc = investorService.findContactDocList(account.getMember().getId(), account.getDistributor().getId(), mainContactId, langCode,accountId);
				model.put("mainDoc", mainDoc);

			}
		}

		// return "console/investor/approve/approveform";
		return "distributor/approve/accountApproveForm";
	}

	/**
	 * 开户申请审批页面（IFA、IFA supervisor）
	 * 
	 * @author mqzou
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accountInfomartion", method = RequestMethod.GET)
	public String accountInfomartion(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		String lang = this.getLoginLangFlag(request);

		List hisList = investorServiceForConsole.findApproveHis(accountId);
		model.put("approveHis", hisList);

		List imgList = investorServiceForConsole.findAccountFileList(accountId, lang, "appli_form_image");// 开户的扫描件

		List list = new ArrayList();

		Iterator it = imgList.iterator();
		while (it.hasNext()) {
			AccessoryFile file = (AccessoryFile) it.next();
			FileVO vo = new FileVO();
			vo.setAlt("");
			vo.setPid(file.getId());
			vo.setSrc("${base}" + file.getFilePath());
			vo.setThumb("${base}" + file.getFilePath());
			list.add(vo);
		}
		model.put("imgList", list);
		model.put("imgJson", JsonUtil.toJson(list));

		List docList = investorServiceForConsole.findAccountFileList(accountId, lang, "doc_check");// 开户的扫描件

		list = new ArrayList();

		it = docList.iterator();
		while (it.hasNext()) {
			AccessoryFile file = (AccessoryFile) it.next();
			FileVO vo = new FileVO();
			vo.setAlt("");
			vo.setPid(file.getId());
			vo.setSrc("${base}" + file.getFilePath());
			vo.setThumb("${base}" + file.getFilePath());
			list.add(vo);
		}
		model.put("docList",  list);
		model.put("docJson", JsonUtil.toJson(list));

		return "console/investor/approve/accountInfomartion";
		// return "investor/accountApprove";
	}

	// basic
	@RequestMapping(value = "/basicInformation", method = RequestMethod.GET)
	public String basicInformation(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String lang = this.getLoginLangFlag(request);
		// 财富来源
		List<GeneralDataVO> fundsSource = findSysParameters("", lang);
		String iAccountId = request.getParameter("accountId");
		InvestorAccount iAccount = investorService.findInvestorAccountById(iAccountId);

		InvestorAccountContact iContact = investorService.findIContactByType(iAccount, "M", lang);// 主要联系人
		InvestorAccountContactAddr iContactAddrR = investorService.findIContactAddr(iContact, "R");
		InvestorAccountContactAddr iContactAddrP = investorService.findIContactAddr(iContact, "P");
		InvestorAccountContactAddr iContactAddrC = investorService.findIContactAddr(iContact, "C");
		InvestorAccountBank iBank = investorService.findBankAgeasByAccid(iAccount);

		InvestorAccountContact iContactJoint = new InvestorAccountContact();
		InvestorAccountContactAddr iContactJointAddrR = new InvestorAccountContactAddr();
		InvestorAccountContactAddr iContactJointAddrP = new InvestorAccountContactAddr();
		InvestorAccountContactAddr iContactJointAddrC = new InvestorAccountContactAddr();
		if (null != iAccount && "J".equals(iAccount.getAccType())) {
			iContactJoint = investorService.findIContactByType(iAccount, "J", lang);// 关联联系人
			iContactJointAddrR = investorService.findIContactAddr(iContactJoint, "R");
			iContactJointAddrP = investorService.findIContactAddr(iContactJoint, "P");
			iContactJointAddrC = investorService.findIContactAddr(iContactJoint, "C");
		}
		model.put("accountId", iAccountId);
		model.put("iAccount", iAccount);
		model.put("iContact", iContact);
		model.put("iContactAddr_r", iContactAddrR);
		model.put("iContactAddr_p", iContactAddrP);
		model.put("iContactAddr_c", iContactAddrC);
		model.put("iContactJoint", iContactJoint);
		model.put("iContactJointAddr_r", iContactJointAddrR);
		model.put("iContactJointAddr_p", iContactJointAddrP);
		model.put("iContactJointAddr_c", iContactJointAddrC);
		model.put("iBank", iBank);
		model.put("fundSource", fundsSource);

		return "console/investor/approve/basicInfo";
	}

	// applicantsform
	@RequestMapping(value = "/applicantsformation", method = RequestMethod.GET)
	public String applicantsform(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String lang = this.getLoginLangFlag(request);
		String accountId = request.getParameter("accountId");

		List<AccessoryFile> imgList = investorServiceForConsole.findAccountFileList(accountId, lang, "appli_form_image");// 开户的扫描件

		List list = new ArrayList();

		Iterator it = imgList.iterator();
		while (it.hasNext()) {
			AccessoryFile file = (AccessoryFile) it.next();
			FileVO vo = new FileVO();
			vo.setAlt("");
			vo.setPid(file.getId());
			vo.setSrc("${base}" + file.getFilePath());
			vo.setThumb("${base}" + file.getFilePath());
			list.add(vo);
		}
		model.put("imgList", list);
		return "console/investor/approve/applicantsform";
	}

	// dialogshowpdf
	@RequestMapping(value = "/dialogshowpdf", method = RequestMethod.GET)
	public String dialogshowpdf(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String accountId = request.getParameter("accountId");
		String lang = this.getLoginLangFlag(request);

		List pdfList = investorServiceForConsole.findAccountFileList(accountId, lang, "appli_form_pdf");// 开户的pdf文件
		model.put("pdfList", pdfList);
		return "console/investor/approve/dialog_showpdf";
	}

	// rqp
	@RequestMapping(value = "/rpqInformation", method = RequestMethod.GET)
	public String rpqInformation(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String accountId = request.getParameter("accountId");
		InvestorAccount investorAccount = rpqService.getInvestorAccount(accountId);

		/*String examId = investorAccount.getRpqExam().getId();
		RpqPageLevel level = rpqService.loadRpqLevelMsg(examId, this.getLoginLangFlag(request));
		List<RpqListVO> examList = rpqService.findExamList(examId, this.getLoginLangFlag(request));

		model.put("level", level);
		model.put("rpqList", examList);
		model.put("examId", examId);*/
		model.put("validity", "true");
		model.put("accountId", accountId);
		model.put("investorAccount", investorAccount);

		return "console/investor/approve/rqpInfo";

	}

	// docCheck
	@RequestMapping(value = "/docCheckInformation", method = RequestMethod.GET)
	public String docCheckInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String accountId = request.getParameter("accountId");
		InvestorAccount iAccount = investorService.findInvestorAccountById(accountId);
		String memberId = iAccount.getMember().getId();
		String mainContactId = investorService.getAccountContactId(accountId, "M");
		String accType = iAccount.getAccType();
		String distrubuteId = "1";

		List<DocListVO> contactDocList = investorService.findContactDocList(memberId, distrubuteId, mainContactId, this.getLoginLangFlag(request),accountId);
		if ("J".equals(accType)) {
			// 获取关联用户附件
			model.put("jContactDocList", null);
		}
		model.put("accountId", accountId);
		model.put("acc_type", accType);// 账户类型,I:Individual Account J:Joint
										// Account
		model.put("contactDocList", contactDocList);
		return "console/investor/approve/docCheckInfo";
	}

	// declare
	@RequestMapping(value = "/declareInformation", method = RequestMethod.GET)
	public String declareInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		model.put("declaration", investorService.findInvestorAccountDeclarationAgeasForAccount(accountId));
		return "console/investor/approve/declareInfo";
	}

	// approveList
	@RequestMapping(value = "/approvalListInfo", method = RequestMethod.GET)
	public String approvalListInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = getLoginUser(request);
		if (null==curMember)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		// model.put("declaration",
		// investorService.findInvestorAccountDeclarationAgeasForAccount(accountId));
		return "console/investor/approve/approveList";
	}
}
