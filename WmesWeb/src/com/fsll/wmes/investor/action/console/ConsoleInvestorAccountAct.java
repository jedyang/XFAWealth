package com.fsll.wmes.investor.action.console;

import java.util.ArrayList;
import java.util.Date;
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

import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.service.SysRoleService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountBank;
import com.fsll.wmes.entity.InvestorAccountContact;
import com.fsll.wmes.entity.InvestorAccountContactAddr;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifafirm.service.IfaFirmForDistributorService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.service.InvestorServiceForConsole;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.investor.vo.FileVO;
import com.fsll.wmes.investor.vo.IndividualVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberDistributorService;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.rpq.service.RpqService;

/*****
 * Investor开户信息
 * 
 * @author zxtan 2016-08-22
 */
@Controller
@RequestMapping(value = "/console/investor/account")
public class ConsoleInvestorAccountAct extends CoreBaseAct {

	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private InvestorServiceForConsole investorServiceForConsole;
	@Autowired
	private InvestorService investorService;
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
	@Autowired
	private MemberDistributorService memberDistributorService;
	@Autowired
	private IfaFirmForDistributorService ifaFirmForDistributorService;
	
	/*
	 * @Autowired private MemberManageServiceForConsole
	 * memberManageServiceForConsole;
	 */
	@Autowired
	private InvestorService frontinvestorService;

	@Autowired
	private RpqService rpqService;

	private final static String compWSServer = "http://192.168.138.70/comp";

	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 投资人开户列表分页列表
	 * 
	 * @author zxtan
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/list")
	public String accountlist(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		this.isMobileDevice(request, model);
		return "console/investor/account/list";
	}

	/**
	 * 分页获得记录
	 * 
	 * @author zxtan
	 */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// String keyword = request.getParameter("keyword");
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin){
			String adminType = curAdmin.getType();
			String adminTypeMemberId = "";
			if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(adminType)) {
				adminTypeMemberId = curAdmin.getIfafirm().getId();
			} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(adminType)) {
				adminTypeMemberId = curAdmin.getDistributor().getId();
			}
	
			// adminType = "2";
			// adminTypeMemberId = "5";
	
			jsonPaging = this.getJsonPaging(request);
			jsonPaging = investorServiceForConsole.findInvestorAccount(jsonPaging, adminType, adminTypeMemberId);
		}else{
//			obj.put("result",false);
//			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
			jsonPaging.setList(null);
		}
		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 新增投资账户
	 * 
	 * @author zxtan
	 */
	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getDistributor() && !StrUtils.isEmpty(curAdmin.getDistributor().getId())){
			String id = request.getParameter("id");
			InvestorAccountVO obj = new InvestorAccountVO();
			if (!StrUtils.isEmpty(id)){
				InvestorAccount account = investorServiceForConsole.findInvestorAccountById(id);
				if (null!=account){
					obj = new InvestorAccountVO(account);
					//ifa
					if (null!=account.getIfa()){
						obj.setIfa(account.getIfa());
						obj.setIfaId(account.getIfa().getId());
						obj.setIfaIconUrl(account.getIfa().getMember().getIconUrl());
						obj.setIfaName(account.getIfa().getFirstName()+" "+account.getIfa().getLastName());
						//ifafirm
						MemberIfafirm firm = ifafirmManageService.findIfafirmByIfa(account.getIfa(), this.getLoginLangFlag(request));
						if (null!=firm){
							obj.setIfafirm(firm);
							obj.setIfaFirmId(firm.getId());
							obj.setIfaFirmName(firm.getCompanyName());
						}
						
						//查找investor
						List<IndividualVO> resultList = new ArrayList<IndividualVO>();
						List<MemberIndividual> list = investorService.findCustomerByIFA(obj.getIfaId());
						if (null!=list && !list.isEmpty()){
							for (MemberIndividual r: list){
								IndividualVO v = new IndividualVO(r);
								v.setMemberId(r.getMember().getId());
								resultList.add(v);
							}
						}
						model.put("investorList", resultList);

					}
					//distributor
					if (null!=account.getDistributor()){
						obj.setDistributor(account.getDistributor());
						obj.setDistributorId(account.getDistributor().getId());
						obj.setDistributorIconUrl(account.getDistributor().getLogofile());
						obj.setDistributorName(account.getDistributor().getCompanyName());
					}
					
					if (null!=account.getMasterAccount()){
						obj.setSubRelateId(account.getMasterAccount().getId());
						obj.setSubFlagDisplay(account.getMasterAccount().getAccountNo());
					}
				}
			}else {
				obj.setDistributor(curAdmin.getDistributor());
				obj.setDistributorId(curAdmin.getDistributor().getId());
				obj.setDistributorIconUrl(curAdmin.getDistributor().getLogofile());
				obj.setDistributorName(curAdmin.getDistributor().getCompanyName());
			}
			model.put("infoVO", obj);
			
			//查找distributor相关的ifafirm
			List firmList = ifaFirmForDistributorService.findIfafirmByDistributor(curAdmin.getDistributor().getId(), this.getLoginLangFlag(request));
			List firmVoList = new ArrayList<MemberIfafirmVO>();
			if (null!=firmList && !firmList.isEmpty()){
				for (int i=0;i<firmList.size();i++){
					Object[] objs = (Object[])firmList.get(i);
					MemberIfafirm firm = (MemberIfafirm)objs[0];
					String name = StrUtils.getString(objs[0]);
					MemberIfafirmVO vo = new MemberIfafirmVO();
					BeanUtils.copyProperties(firm, vo);
					vo.setCompanyName(name);
					firmVoList.add(vo);
				}
			}
			model.put("firmList", firmVoList);
			
			List<GeneralDataVO> itemList=findSysParameters("currency_type", this.getLoginLangFlag(request));
	        model.put("currencyType", itemList);
	        
			return "console/investor/account/input";
		}else {
			return "redirect:"+this.getFullPath(request)+"index.do";
		}
	}
	
	/**
	 * 获得投资顾的客户列表
	 * 
	 * @author michael
	 */
	@RequestMapping(value = "/investorListJson", method = RequestMethod.POST)
	public String investorListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model, String ifaId) {
		Map<String,Object> obj = new HashMap<String, Object>();
		boolean result = false;
		MemberAdmin memberAdmin = getLoginMemberAdmin(request);
		if (null!=memberAdmin){
			List<MemberIndividual> list = null;
			List<IndividualVO> resultList = new ArrayList<IndividualVO>();
			if (!StrUtils.isEmpty(ifaId)){
				list = investorService.findCustomerByIFA(ifaId);
				if (null!=list && !list.isEmpty()){
					for (MemberIndividual r: list){
						IndividualVO v = new IndividualVO(r);
						v.setMemberId(r.getMember().getId());
						resultList.add(v);
					}
				}
			}
			result = true;
			obj.put("data",resultList);
		}else {
			jsonPaging.setList(null);
		}
		obj.put("result",result);
		JsonUtil.toWriter(obj, response);
		return null;
	}
	
	/**
	 * Distributor新增投资账户（从账户）
	 * @param request
	 * @param response
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(HttpServletRequest request, HttpServletResponse response, InvestorAccountVO info) {
		Map<String,Object> obj = new HashMap<String, Object>();
		boolean result = false;
		MemberAdmin curAdmin = getLoginMemberAdmin(request);
		if (null!=curAdmin && null!=curAdmin.getDistributor() && !StrUtils.isEmpty(curAdmin.getDistributor().getId())){
			try {
				InvestorAccount account = null;
				if(StrUtils.isEmpty(info.getId())){
					account = new InvestorAccount();
					account.setCreateTime(new Date());
					account.setCreateBy(curAdmin.getMember());
				}else{
					account = investorServiceForConsole.findInvestorAccountById(info.getId());
					if (null==account) {
						account = new InvestorAccount();
						account.setCreateTime(new Date());
						account.setCreateBy(curAdmin.getMember());
					}
				}
				account.setLastUpdate(new Date());
				MemberBase member = memberBaseService.findById(info.getMemberId());
				account.setMember(member);
				account.setAccountNo(info.getAccountNo());
				account.setTotalFlag(info.getTotalFlag());//1是,0否,投资时，显示的总投资金额
				account.setAccType(info.getAccType());//账户类型,I:Individual Account  J:Joint Account
				account.setInvestType(info.getInvestType());//F:Fund基金帐户,P:Portfolio综合资产帐户
				account.setCies(info.getCies());//资本投资人入境计划账户,1是,0否
				account.setFaca(info.getFaca());//是否美国公民,1是,0否
				account.setDividend(info.getDividend());//股息选项,R:Reinvestment再投资;D:分配到现金账户
				account.setBaseCurrency(info.getBaseCurrency());//基本货币
				account.setPurpose(info.getPurpose());//开户目的,I:投资,O:其他
				account.setPurposeOther(info.getPurposeOther());//目的_其他的说明
				account.setSentBy(info.getSentBy());//发送账单的方式,Email,Post
				account.setDiscFlag(info.getDiscFlag());//下单时是否需要投资人确认,1是,0否
				account.setSubmitStatus(info.getSubmitStatus());//invest投资人提交,ifa提交,ifafirm添加,distributor添加
				account.setAuthorized(info.getAuthorized());//是否授权IFA操作，0：不是,1：是
				account.setIsValid(info.getIsValid());
				account.setSubFlag(info.getSubFlag());
				if (!StrUtils.isEmpty(info.getSubRelateId())){
					InvestorAccount masterAccount = investorService.findInvestorAccountById(info.getSubRelateId());
					account.setMasterAccount(masterAccount);
				}
				account.setFromType("distributor");//开户的来源类型,ifa:ifa邀请开户,self:投资人自行开记,ifafirm添加,distributor添加，sys导入
				account.setOpenStatus("3");//-1 退回, 0 草稿, 1 等待投资人确认, 2 处理中 ,3 开户成功,4 开户失败
				account.setCurStep("Submit");//当前阶段,RPQ Basic Doc Dec Submit
				account.setFinishStatus("1");//是否完成,0草稿,1已提交
				account.setFlowId(null);//流程ID
				account.setFlowStatus("1");//流程状态:-1还未执行,0流程进行中,1 审核通过,2 审核不通过,3等待IFAFIRM处理,4等待distributor处理
				account.setSourceFrom("s");//来源，s表示我们系统创建,f表示从其他系统导入

				MemberIfa ifa = ifaManageService.findIfaById(info.getIfaId());
				if(ifa != null){
					account.setIfa(ifa);
				}
				MemberDistributor distributor = memberDistributorService.findById(info.getDistributorId());
				if(distributor != null){
					account.setDistributor(distributor);
				}
				
				//检查重要数据是否设置,  && null!=account.getMasterAccount()
				if (null!=account.getIfa() && null!=account.getDistributor() && null!=account.getMember() ){
					//保存
					account = frontinvestorService.saveOrUpdateInvestorAccount(account);
					if(account != null){
						result = true;
						obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
					}
				}else {
					obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.missing",null));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				obj.put("result",result);
				JsonUtil.toWriter(obj, response);
			}
		}else {
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
		return null;
	}
	
	/**
	 * 检查账户号是否已存在（用于bootstrapValidator 的 remote校验方法）
	 * @author michael
	 * @date 2016-12-7 
	 */
	@RequestMapping(value = "/checkAccountNo", method = RequestMethod.POST)
	public void checkAccountNo(HttpServletRequest request,HttpServletResponse response,ModelMap model, String accountId, String accountNo) {
		Map<String, Object> obj = new HashMap<String, Object>();
		boolean result = investorService.checkAccountNo(accountId, accountNo);
		
		//valid: true 表示合法，验证通过, false 表示不合法，验证不通过
		if (result)
			obj.put("valid",false);//已存在
		else
			obj.put("valid",true);
		JsonUtil.toWriter(obj, response);//需要的返回结果一定是json格式
	}
	
	/**
	 * 获得用户的主账户列表
	 * 
	 * @author michael
	 */
	@RequestMapping(value = "/masterListJson", method = RequestMethod.POST)
	public String masterListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model, String memberId) {
		Map<String,Object> obj = new HashMap<String, Object>();
		boolean result = false;
		MemberAdmin memberAdmin = getLoginMemberAdmin(request);
		if (null!=memberAdmin){
			List list = null;
			if (!StrUtils.isEmpty(memberId)){
				InvestorAccount c = new InvestorAccount();
				c.setIsValid("1");
				c.setSubFlag("0");
				list = investorService.findInvestorAccountList(memberId, c);
			}
			result = true;
			obj.put("data",list);
		}else {
			jsonPaging.setList(null);
		}
		obj.put("result",result);
		JsonUtil.toWriter(obj, response);
		return null;
	}
	
	/**
	 * 获得投资顾问记录
	 * 
	 * @author michael
	 */
	@RequestMapping(value = "/ifaListJson", method = RequestMethod.POST)
	public String ifaListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model, String ifaFirmId) {
		Map<String,Object> obj = new HashMap<String, Object>();
		boolean result = false;
		MemberAdmin memberAdmin = getLoginMemberAdmin(request);
		if (null!=memberAdmin){
			List list = null;
			if (!StrUtils.isEmpty(ifaFirmId)){
				IfaVO ifaVo = new IfaVO();
				ifaVo.setIfafirmId(ifaFirmId);
				list = ifafirmManageService.findIfaByIfaFirm(ifaVo, this.getLoginLangFlag(request));
			}
			result = true;
			obj.put("data",list);
		}else {
			jsonPaging.setList(null);
		}
		obj.put("result",result);
		JsonUtil.toWriter(obj, response);
		return null;
	}
	
	/**
	 * 获得投资顾问公司记录
	 * 
	 * @author michael
	 */
	@RequestMapping(value = "/ifaFirmListJson", method = RequestMethod.POST)
	public String ifaFirmListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model, String distributorId) {
		Map<String,Object> obj = new HashMap<String, Object>();
		boolean result = false;
		MemberAdmin memberAdmin = getLoginMemberAdmin(request);
		if (null!=memberAdmin){
			if (StrUtils.isEmpty(distributorId) && null!=memberAdmin.getDistributor() && !StrUtils.isEmpty(memberAdmin.getDistributor().getId()))
				distributorId = memberAdmin.getDistributor().getId();
			List list = null;
			if (!StrUtils.isEmpty(distributorId))
				list = ifaFirmForDistributorService.findIfafirmByDistributor(distributorId, this.getLoginLangFlag(request));
			result = true;
			obj.put("data",list);
		}else {
			jsonPaging.setList(null);
		}
		obj.put("result",result);
		JsonUtil.toWriter(obj, response);
		return null;
	}
	
	/**
	 * 投资顾问公司分页列表
	 * 
	 * @author zxtan
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/ifafirmList")
	public String ifafirmList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		this.isMobileDevice(request, model);
		return "console/investor/account/ifafirmlist";
	}

	/**
	 * 获得投资顾问公司分页记录
	 * 
	 * @author zxtan
	 */
	@RequestMapping(value = "/listJsonForIfafirm", method = RequestMethod.POST)
	public String listJsonForIfafirm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		jsonPaging = this.getJsonPaging(request);
		// String keyword = request.getParameter("keyword");
		MemberAdmin memberAdmin = getLoginMemberAdmin(request);
		if (null!=memberAdmin){
			String adminType = memberAdmin.getType();
			String adminTypeMemberId = "";
	
			// adminType = "1";
			if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(adminType)) {
				adminTypeMemberId = memberAdmin.getIfafirm().getId();
				jsonPaging = investorServiceForConsole.findInvestorAccountForApproval(jsonPaging, adminType, adminTypeMemberId, memberAdmin.getMember().getId());
			} else {
				jsonPaging.setList(null);
			}
		}else {
			jsonPaging.setList(null);
		}

		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 代理商分页列表
	 * 
	 * @author zxtan
	 * @date 2016-08-22
	 */
	@RequestMapping(value = "/distributorList")
	public String distributorList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		this.isMobileDevice(request, model);
		return "console/investor/account/distributorlist";
	}

	/**
	 * 获得代理商分页记录
	 * 
	 * @author zxtan
	 */
	@RequestMapping(value = "/listJsonForDistributor", method = RequestMethod.POST)
	public String listJsonForDistributor(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		jsonPaging = this.getJsonPaging(request);
		// String keyword = request.getParameter("keyword");
		MemberAdmin memberAdmin = getLoginMemberAdmin(request);
		if (null!=memberAdmin){
			String adminType = memberAdmin.getType();
			String adminTypeMemberId = "";
			if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(adminType)) {
				adminTypeMemberId = memberAdmin.getDistributor().getId();
				jsonPaging = investorServiceForConsole.findInvestorAccountForApproval(jsonPaging, adminType, adminTypeMemberId, memberAdmin.getMember().getId());
			} else {
				jsonPaging.setList(null);
			}
		}else {
			jsonPaging.setList(null);
		}

		this.toJsonString(response, jsonPaging);
		return null;
	}

	/**
	 * 投资账号审批（Ifafirm,Distributor审批）
	 * 
	 * @author zxtan
	 * @date 2016-08-24
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/approval", method = RequestMethod.POST)
	public void updateFlowStatus(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// modify by mqzou 2016-08-31
		String actionMsg = "global.success";
		String comment = request.getParameter("comment");
		// String todoId = request.getParameter("id");
		String todoId = "";
		String status = request.getParameter("status");
		String accountId = request.getParameter("accountId");
		MemberAdmin memberAdmin = getLoginMemberAdmin(request);
		String userId = memberAdmin.getMember().getId();

		// 获取当前工作流信息
		String urlString = compWSServer + "/service/component/workflow/get.r";
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("invokCode", "Investor_open_account");
		jsonObj.put("businessId", accountId);
		jsonObj.put("userId", userId);
		String jsonString = jsonObj.toString();
		String getResult = sendWebServiceByBody(urlString, jsonString);

		String instanseId = "";

		JSONObject instanseObj = new JSONObject();
		JSONObject memberDataJSON = (JSONObject) JSONSerializer.toJSON(getResult);
		if (null != memberDataJSON) {
			String dataJson = memberDataJSON.getString("data");
			if (null != dataJson && !"".equals(dataJson) && !"null".equals(dataJson)) {
				instanseObj = (JSONObject) JSONSerializer.toJSON(dataJson);
				if (null != instanseObj)
					instanseId = instanseObj.getString("instanseId");
			}
		}

		// 更新工作流
		urlString = compWSServer + "/service/component/workflow/update.r";
		jsonObj = new JSONObject();

		jsonObj.put("todoId", todoId);
		jsonObj.put("invokCode", "Investor_open_account");
		jsonObj.put("businessId", accountId);
		jsonObj.put("instanseId", instanseId);
		jsonObj.put("checkStatus", status);
		jsonObj.put("comment", comment);
		jsonObj.put("flowUserId", userId);

		jsonString = jsonObj.toString();
		String result = sendWebServiceByBody(urlString, jsonString);

		JSONObject resultObject = (JSONObject) JSONSerializer.toJSON(result);

		Map<String, Object> obj = new HashMap<String, Object>();
		String completeStatus = "";// 完成状态 0 待选择然后下一步 1本次流程已完成不需要下一步操作并且待下一个流程
									// 2开户流程已完全完成
		if (null != resultObject) {
			String ret = resultObject.getString("ret");
			if ("1".equals(ret)) {
				String curFlowStatus = resultObject.getString("currFlowState");
				String data = resultObject.getString("data");
				if ("1".equals(curFlowStatus)) {// 如果当前环节已完成
					if (null != data && !"".equals(data)) {
						jsonObj = (JSONObject) JSONSerializer.toJSON(data);
						instanseId = jsonObj.getString("id");
						String roleId = jsonObj.getString("flowRole");
						String flowRoleAlluser = jsonObj.getString("flowRoleAlluser");
						String beginOrEnd = jsonObj.getString("beginOrEnd");

						if (!"2".equals(beginOrEnd)) {// 如果流程未结束
							if ("1".equals(flowRoleAlluser)) {
//								List userList = sysRoleService.findUserInRole(roleId);//??待修改
								List<MemberBase> userList = frontinvestorService.findMemberByRole(null, accountId, roleId);
								// JSONArray jsonArray = new JSONArray();
								data = addFlow(accountId, userId, instanseId, data, userList);

								InvestorAccount account = frontinvestorService.findInvestorAccountById(accountId);

								String flowStatus = account.getFlowStatus();
								int flowInt = Integer.parseInt(flowStatus);
								String flowId = "";
								if (null != data && !"".equals(data) && !"null".equals(data)) {
									jsonObj = (JSONObject) JSONSerializer.toJSON(data);
									flowId = jsonObj.getString("instanseId");
								}
								if ("1".equals(status)) {
									account.setFlowStatus(String.valueOf(flowInt + 1));// 审批通过转到下一步
								} else {
									account.setFlowStatus("2");// 2流程审核不通过结束返回
								}
								account.setFlowId(flowId);

								// 更新账户信息
								frontinvestorService.saveOrUpdateInvestorAccount(account);
								completeStatus = "1";// 本次流程已完成不需要下一步操作并且待下一个流程
							} else {
								completeStatus = "0";// 0 待选择然后下一步
								obj.put("roleId", roleId);
								obj.put("instanseId", instanseId);
								obj.put("actionStatus", status);
							}
						} else {// 流程结束
							obj.put("accountId", accountId);
							completeStatus = "2";// 整个流程结束
						}
					}
				} else if ("2".equals(curFlowStatus)) {// 退回
					jsonObj = (JSONObject) JSONSerializer.toJSON(data);
					instanseId = jsonObj.getString("id");
					String roleId = jsonObj.getString("flowRole");
					String flowRoleAlluser = jsonObj.getString("flowRoleAlluser");
					if ("1".equals(flowRoleAlluser)) {
//						List userList = sysRoleService.findUserInRole(roleId);
						List<MemberBase> userList = frontinvestorService.findMemberByRole(null, accountId, roleId);
						data = addFlow(accountId, userId, instanseId, data, userList);

						InvestorAccount account = frontinvestorService.findInvestorAccountById(accountId);

						String flowStatus = account.getFlowStatus();
						int flowInt = Integer.parseInt(flowStatus);
						String flowId = "";
						if (null != data && !"".equals(data) && !"null".equals(data)) {
							jsonObj = (JSONObject) JSONSerializer.toJSON(data);
							flowId = jsonObj.getString("instanseId");
						}
						if ("1".equals(status)) {
							account.setFlowStatus(String.valueOf(flowInt + 1));// 审批通过转到下一步
						} else {
							account.setFlowStatus("2");// 2流程审核不通过结束返回
						}
						account.setFlowId(flowId);

						// 更新账户信息
						frontinvestorService.saveOrUpdateInvestorAccount(account);
						completeStatus = "1";// 本次流程已完成不需要下一步操作并且待下一个流程
					} else {
						completeStatus = "0";// 0 待选择然后下一步
						obj.put("roleId", roleId);
						obj.put("instanseId", instanseId);
						obj.put("actionStatus", status);
					}

				} else if ("9".equals(curFlowStatus)) {// //整个流程结束
					obj.put("accountId", accountId);
					completeStatus = "2";// 整个流程结束
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
	private String addFlow(String accountId, String userId, String instanseId,
			String data, List<MemberBase> userList) {
		String urlString;
		JSONObject jsonObj;
		String jsonString;
		String result;
		JSONObject resultObject;
		if (null != userList && !userList.isEmpty()) {
			urlString = compWSServer + "/service/component/workflow/insert.r";
			Iterator it = userList.iterator();
			while (it.hasNext()) {
				MemberBase user = (MemberBase) it.next();
				jsonObj = new JSONObject();
				jsonObj.put("invokCode", "Investor_open_account");
				jsonObj.put("businessId", accountId);
				jsonObj.put("instanseId", instanseId);
				jsonObj.put("flowUserIds", userId);
				jsonObj.put("todoUrl", "");
				jsonObj.put("userId", user.getId());
				// jsonArray.add(jsonObj);
				jsonString = jsonObj.toString();
				result = sendWebServiceByBody(urlString, jsonString);
				resultObject = new JSONObject();
				resultObject = (JSONObject) JSONSerializer.toJSON(result);
				if (null != resultObject.getString("data") && !"".equals(resultObject.getString("data")) && !"null".endsWith(resultObject.getString("data")))
					data = resultObject.getString("data");
			}
		}
		return data;
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
		String userId = request.getParameter("userId");
		String accountId = request.getParameter("accountId");
		String instanseId = request.getParameter("instanceId");
		String status = request.getParameter("status");

		String urlString = compWSServer + "/service/component/workflow/insert.r";
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("invokCode", "Investor_open_account");
		jsonObj.put("businessId", accountId);
		jsonObj.put("instanseId", instanseId);
		jsonObj.put("flowUserIds", userId);
		jsonObj.put("todoUrl", "");
		jsonObj.put("userId", userId);
		String jsonString = jsonObj.toString();
		String result = sendWebServiceByBody(urlString, jsonString);
		Map<String, Object> obj = new HashMap<String, Object>();

		jsonObj = (JSONObject) JSONSerializer.toJSON(result);
		if ("1".equals(jsonObj.getString("ret"))) {
			obj.put("result", true);
		} else {
			obj.put("result", false);
			obj.put("msg", jsonObj.getString("errorMsg"));

		}

		InvestorAccount account = frontinvestorService.findInvestorAccountById(accountId);
		String flowStatus = account.getFlowStatus();
		int flowInt = Integer.parseInt(flowStatus);
		String flowId = "";
		String data = jsonObj.getString("data");
		if (null != data && !"".equals(data) && !"null".equals(data)) {
			jsonObj = (JSONObject) JSONSerializer.toJSON(data);
			flowId = jsonObj.getString("instanseId");
		}
		if ("1".equals(status)) {
			account.setFlowStatus(String.valueOf(flowInt + 1));// 审批通过转到下一步
		} else {
			account.setFlowStatus("2");// 退回到ifa提交以前
		}
		account.setFlowId(flowId);

		// 更新账户信息
		frontinvestorService.saveOrUpdateInvestorAccount(account);
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
		String accountId = request.getParameter("accountId");
		String accountNo = request.getParameter("accountNo");
		InvestorAccount account = investorServiceForConsole.findById(accountId);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != account) {
			account.setAccountNo(accountNo);
			account = frontinvestorService.saveOrUpdateInvestorAccount(account);
			if (null != account)
				obj.put("result", true);
			else {
				obj.put("result", false);
			}
		} else {
			obj.put("result", false);
		}
		JsonUtil.toWriter(obj, response);
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
	@RequestMapping(value = "/selectApprover", method = RequestMethod.GET)
	public String showSelectUserDialog(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String roleId = request.getParameter("roleId");
		List userList = sysRoleService.findUserInRole(roleId);
		model.put("users", userList);

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
		model.put("accountId", accountId);

		return "console/investor/approve/approveform";
		// return "investor/accountApprove";
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
		String lang = this.getLoginLangFlag(request);
		// 财富来源
		List<GeneralDataVO> fundsSource = findSysParameters("", lang);
		String iAccountId = request.getParameter("accountId");
		InvestorAccount iAccount = investorServiceForConsole.findInvestorAccountById(iAccountId);

		InvestorAccountContact iContact = frontinvestorService.findIContactByType(iAccount, "M", lang);// 主要联系人
		InvestorAccountContactAddr iContactAddrR = frontinvestorService.findIContactAddr(iContact, "R");
		InvestorAccountContactAddr iContactAddrP = frontinvestorService.findIContactAddr(iContact, "P");
		InvestorAccountContactAddr iContactAddrC = frontinvestorService.findIContactAddr(iContact, "C");
		InvestorAccountBank iBank = frontinvestorService.findBankAgeasByAccid(iAccount);

		InvestorAccountContact iContactJoint = new InvestorAccountContact();
		InvestorAccountContactAddr iContactJointAddrR = new InvestorAccountContactAddr();
		InvestorAccountContactAddr iContactJointAddrP = new InvestorAccountContactAddr();
		InvestorAccountContactAddr iContactJointAddrC = new InvestorAccountContactAddr();
		if (null != iAccount && "J".equals(iAccount.getAccType())) {
			iContactJoint = frontinvestorService.findIContactByType(iAccount, "J", lang);// 关联联系人
			iContactJointAddrR = frontinvestorService.findIContactAddr(iContactJoint, "R");
			iContactJointAddrP = frontinvestorService.findIContactAddr(iContactJoint, "P");
			iContactJointAddrC = frontinvestorService.findIContactAddr(iContactJoint, "C");
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
		String accountId = request.getParameter("accountId");
		String lang = this.getLoginLangFlag(request);

		List pdfList = investorServiceForConsole.findAccountFileList(accountId, lang, "appli_form_pdf");// 开户的pdf文件
		model.put("pdfList", pdfList);
		return "console/investor/approve/dialog_showpdf";
	}

	// rqp
	@RequestMapping(value = "/rpqInformation", method = RequestMethod.GET)
	public String rpqInformation(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

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

		String accountId = request.getParameter("accountId");
		InvestorAccount iAccount = investorServiceForConsole.findInvestorAccountById(accountId);
		String memberId = iAccount.getMember().getId();
		String mainContactId = frontinvestorService.getAccountContactId(accountId, "M");
		String accType = iAccount.getAccType();
		String distrubuteId = "1";

		List<DocListVO> contactDocList = frontinvestorService.findContactDocList(memberId, distrubuteId, mainContactId, this.getLoginLangFlag(request),accountId);
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
		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		model.put("declaration", frontinvestorService.findInvestorAccountDeclarationAgeasForAccount(accountId));
		return "console/investor/approve/declareInfo";
	}

	// approveList
	@RequestMapping(value = "/approvalListInfo", method = RequestMethod.GET)
	public String approvalListInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		// model.put("declaration",
		// investorServiceForConsole.findInvestorAccountDeclarationAgeasForAccount(accountId));
		return "console/investor/approve/approveList";
	}

	
	/**
	 * 新增投资从账户
	 * @author scshi_u330p
	 * @date 20170405
	 * */
	@RequestMapping(value = "/subInvAcc", method = RequestMethod.GET)
	public String subInvestorAccountInput(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO sso = this.getLoginUserSSO(request);
		InvestorAccountVO obj = new InvestorAccountVO();
		String distributorId = sso.getDistributorId();
		MemberDistributor dis = this.memberDistributorService.findById(distributorId);
		obj.setDistributor(dis);
		obj.setDistributorId(distributorId);
		obj.setDistributorIconUrl(dis.getLogofile());
		obj.setDistributorName(dis.getCompanyName());
		
		model.put("infoVO", obj);
		
		//查找所有的主账户列表
		List  masterAccountList =  investorServiceForConsole.findMasterAccountByDisId(distributorId);
		model.put("masterList", masterAccountList);
		
		//查找distributor相关的ifafirm
		List firmList = ifaFirmForDistributorService.findIfafirmByDistributor(dis.getId(), this.getLoginLangFlag(request));
		List firmVoList = new ArrayList<MemberIfafirmVO>();
		if (null!=firmList && !firmList.isEmpty()){
			for (int i=0;i<firmList.size();i++){
				Object[] objs = (Object[])firmList.get(i);
				MemberIfafirm firm = (MemberIfafirm)objs[0];
				String name = StrUtils.getString(objs[0]);
				MemberIfafirmVO vo = new MemberIfafirmVO();
				BeanUtils.copyProperties(firm, vo);
				vo.setCompanyName(name);
				firmVoList.add(vo);
			}
		}
		model.put("firmList", firmVoList);
		
		List<GeneralDataVO> itemList=findSysParameters("currency_type", this.getLoginLangFlag(request));
        model.put("currencyType", itemList);
        
	
		return "console/investor/account/subInvestorAccount";
	}
	
	/**
	 * 加载主账户关联的ifa，ifa公司，投资人，交易账号
	 * @author scshi_u330p
	 * @date 20170405
	 * */
	@RequestMapping(value = "/subAccountInfoJson", method = RequestMethod.GET)
	public void subAccountInfoJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String masterId = request.getParameter("masterId");
		String langCode = this.getLoginLangFlag(request);
		AccountVO vo = investorServiceForConsole.loadMasterAccountInfoForSub(masterId,langCode);
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("data", vo);
		JsonUtil.toWriter(obj, response);
	}
	
	
	@RequestMapping(value = "/subSave", method = RequestMethod.POST)
	public void subSave(HttpServletRequest request, HttpServletResponse response, InvestorAccountVO info) {
		Map<String,Object> obj = new HashMap<String, Object>();
		boolean result = false;
		MemberSsoVO sso = this.getLoginUserSSO(request);
		if (null!=sso){
			try {
				InvestorAccount account = null;
				if(StrUtils.isEmpty(info.getId())){
					account = new InvestorAccount();
					account.setCreateTime(new Date());
					account.setCreateBy(sso.getBaseInfo());
				}else{
					account = investorServiceForConsole.findInvestorAccountById(info.getId());
					if (null==account) {
						account = new InvestorAccount();
						account.setCreateTime(new Date());
						account.setCreateBy(sso.getBaseInfo());
					}
				}
				account.setLastUpdate(new Date());
				MemberBase member = memberBaseService.findById(info.getMemberId());
				account.setMember(member);
				account.setAccountNo(info.getAccountNo());
				account.setTotalFlag(info.getTotalFlag());//1是,0否,投资时，显示的总投资金额
				account.setAccType(info.getAccType());//账户类型,I:Individual Account  J:Joint Account
				account.setInvestType(info.getInvestType());//F:Fund基金帐户,P:Portfolio综合资产帐户
				account.setCies(info.getCies());//资本投资人入境计划账户,1是,0否
				account.setFaca(info.getFaca());//是否美国公民,1是,0否
				account.setDividend(info.getDividend());//股息选项,R:Reinvestment再投资;D:分配到现金账户
				account.setBaseCurrency(info.getBaseCurrency());//基本货币
				account.setPurpose(info.getPurpose());//开户目的,I:投资,O:其他
				account.setPurposeOther(info.getPurposeOther());//目的_其他的说明
				account.setSentBy(info.getSentBy());//发送账单的方式,Email,Post
				account.setDiscFlag(info.getDiscFlag());//下单时是否需要投资人确认,1是,0否
				account.setSubmitStatus(info.getSubmitStatus());//invest投资人提交,ifa提交,ifafirm添加,distributor添加
				account.setAuthorized(info.getAuthorized());//是否授权IFA操作，0：不是,1：是
				account.setIsValid(info.getIsValid());
				account.setSubFlag(info.getSubFlag());
				if (!StrUtils.isEmpty(info.getSubRelateId())){
					InvestorAccount masterAccount = investorService.findInvestorAccountById(info.getSubRelateId());
					account.setMasterAccount(masterAccount);
				}
				account.setFromType("distributor");//开户的来源类型,ifa:ifa邀请开户,self:投资人自行开记,ifafirm添加,distributor添加，sys导入
				account.setOpenStatus("3");//-1 退回, 0 草稿, 1 等待投资人确认, 2 处理中 ,3 开户成功,4 开户失败
				account.setCurStep("Submit");//当前阶段,RPQ Basic Doc Dec Submit
				account.setFinishStatus("1");//是否完成,0草稿,1已提交
				account.setFlowId(null);//流程ID
				account.setFlowStatus("1");//流程状态:-1还未执行,0流程进行中,1 审核通过,2 审核不通过,3等待IFAFIRM处理,4等待distributor处理
				account.setSourceFrom("s");//来源，s表示我们系统创建,f表示从其他系统导入

				MemberIfa ifa = ifaManageService.findIfaById(info.getIfaId());
				if(ifa != null){
					account.setIfa(ifa);
				}
				MemberDistributor distributor = memberDistributorService.findById(info.getDistributorId());
				if(distributor != null){
					account.setDistributor(distributor);
				}
				
				//检查重要数据是否设置,  && null!=account.getMasterAccount()
				if (null!=account.getIfa() && null!=account.getDistributor() && null!=account.getMember() ){
					//保存
					account = frontinvestorService.saveOrUpdateInvestorAccount(account);
					if(account != null){
						result = true;
						obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"global.success",null));
					}
				}else {
					obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.missing",null));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				obj.put("result",result);
				JsonUtil.toWriter(obj, response);
			}
		}else {
			obj.put("result",false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),"error.accessForbidden.title",null));
		}
		JsonUtil.toWriter(obj, response);
	}
	

}
