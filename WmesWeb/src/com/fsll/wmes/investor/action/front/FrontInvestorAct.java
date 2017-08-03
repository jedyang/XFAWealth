package com.fsll.wmes.investor.action.front;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.common.util.UploadUtil;
import com.fsll.common.util.WebServiceUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.WSConstants;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.core.service.SysParamService;
import com.fsll.core.vo.SysParamVO;
import com.fsll.oms.service.ITraderSendService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.service.CommunitySpaceService;
import com.fsll.wmes.community.vo.CommunityNewsListVO;
import com.fsll.wmes.community.vo.FocusMemberVO;
import com.fsll.wmes.community.vo.FrontCommunityTopicVO;
import com.fsll.wmes.community.vo.TopicDetailVO;
import com.fsll.wmes.crm.service.KycDocCheckServic;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountBank;
import com.fsll.wmes.entity.InvestorAccountContact;
import com.fsll.wmes.entity.InvestorAccountContactAddr;
import com.fsll.wmes.entity.InvestorAccountDeclaration;
import com.fsll.wmes.entity.InvestorAccountStream;
import com.fsll.wmes.entity.InvestorBackground;
import com.fsll.wmes.entity.InvestorDocCheck;
import com.fsll.wmes.entity.InvestorReport;
import com.fsll.wmes.entity.InvestorTraining;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MyAsset;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioHoldCumperf;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.entity.WfProcedureInstanseTodo;
import com.fsll.wmes.fund.service.FundAnnoService;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundAnnoVO;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifa.service.IfaSpaceService;
import com.fsll.wmes.ifa.vo.IfaListfiltersVO;
import com.fsll.wmes.ifa.vo.IfaSpaceLiveVO;
import com.fsll.wmes.ifa.vo.MyBuddyVO;
import com.fsll.wmes.ifa.vo.MyFavoritesPortfolios;
import com.fsll.wmes.ifa.vo.MyFavoritesStrategy;
import com.fsll.wmes.ifa.vo.MyFavoritesWatchingTypeVOList;
import com.fsll.wmes.ifa.vo.StrategyInfoVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.service.InvestorServiceForConsole;
import com.fsll.wmes.investor.vo.AccountProgressVO;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.investor.vo.IndividualDataVO;
import com.fsll.wmes.investor.vo.InvestorAccountCurrencyVO;
import com.fsll.wmes.investor.vo.InvestorAccountHoldVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.investor.vo.InvestorHomeMostSelectedFundsVO;
import com.fsll.wmes.investor.vo.InvestorHomeTopPerformanceFundsVO;
import com.fsll.wmes.investor.vo.InvestorHomeTopPerformancePortfolioVO;
import com.fsll.wmes.investor.vo.InvestorHomeVO;
import com.fsll.wmes.investor.vo.InvestorMyPortfolioVO;
import com.fsll.wmes.investor.vo.InvestorMyPortfolios;
import com.fsll.wmes.investor.vo.PortfolioAssetsVO;
import com.fsll.wmes.investor.vo.WorkFlowVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberManageServiceForConsole;
import com.fsll.wmes.member.vo.BasicInfoCombVo;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.member.vo.MemberVO;
import com.fsll.wmes.news.service.NewsInfoService;
import com.fsll.wmes.news.vo.NewsInfoForListVO;
import com.fsll.wmes.news.vo.NewsInfoVO;
import com.fsll.wmes.notice.service.NoticeService;
import com.fsll.wmes.notice.vo.NoticeVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.portfolio.vo.InvestorPortfolioDataVO;
import com.fsll.wmes.portfolio.vo.PortfolioArenaVO;
import com.fsll.wmes.portfolio.vo.PortfolioHoldCumperfSimpleVO;
import com.fsll.wmes.rpq.service.RpqService;
import com.fsll.wmes.rpq.vo.RpqListVO;
import com.fsll.wmes.stock.service.StockInfoService;
import com.fsll.wmes.web.service.WebFriendService;
import com.fsll.wmes.web.service.WebReadToDoService;

/***
 * 开户管理Action
 * 
 * @author michael
 * @date 2016-7-7
 */
@Controller
@RequestMapping("/front/investor")
public class FrontInvestorAct extends WmesBaseAct {
	public static class FlowStates {
		public static final String WAIT_TO_DO = "0";
		public static final String RETURN_NEXT = "1";
		public static final String RETURN_PRE = "2";
		public static final String DONE = "9";
		public static final String TODO_DONE = "1";
		public static final String TODO_WAIT_FOR = "0";
	}
	
	@Autowired
	private InvestorService investorService;

	@Autowired
	private MemberManageServiceForConsole memberManageService;

	@Autowired
	private MemberBaseService memberBaseService;

	@Autowired
	private RpqService rpqService;
	@Autowired
	private IfaSpaceService ifaSpaceService;
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private KycDocCheckServic kycDocCheckServic;
	@Autowired
	private SysParamService sysParamService;
	 
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private InvestorServiceForConsole investorServiceForConsole;
	@Autowired
	private PortfolioHoldService portfolioHoldService;
	
	@Autowired
	private WebReadToDoService webReadToDoService;
	@Autowired
	private WebFriendService webFriendService;
	@Autowired
	private IfaInfoService ifaInfoService;
	@Autowired
	private CommunitySpaceService communitySpaceService;
	@Autowired
	private FundAnnoService fundAnnoService;
	@Autowired
	private StockInfoService stockInfoService;
	@Autowired
	private AccessoryFileService accessoryFileService;
	@Autowired
	private NewsInfoService newsInfoService;
	@Autowired
	private NoticeService noticeService;
	
	
	private static HashMap<Long, String> moduleRelate = new HashMap<Long, String>();
	static {
		moduleRelate.put(5L, "investor_doc");// kyc与开户文档审查
		moduleRelate.put(387L, "submit_doc");// submitdoc
	}

	/**
	 * step1 - start(individual)
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/accountStart", method = RequestMethod.GET)
	public String accountStart(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		String ifaId = request.getParameter("ifaId");// 处理邀请开户的情景
		model.put("ifaId", ifaId);
		if (loginUser != null) {
			//非investor和IFA用户，跳转到首页
			String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
			if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role)))
				return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
			
			if (CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)){
				List<MemberIfa> ifaFriends = webFriendService.findIfaFriends(loginUser.getId());
				model.put("ifaFriends", ifaFriends);
			}
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}

		// 根据填写步骤跳转页面，当前阶段:Start RPQ Basic Doc Dec Submit
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		if (null != account && null != account.getId()) {
			//判断是否已有主账户
			try {
				if (checkIfHadAcount(account.getDistributor().getId(),account.getMember().getId())){
					//return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
					model.put("title", getProperty(request, "open.account.msgTitle"));
					model.put("label", getProperty(request, "open.account.msgLabel"));
					String result = getProperty(request, "open.account.exist");//Main account is exists.
					model.put("result", result);
					return "investor/public/showmsg";
				}
            } catch (Exception e) {
	            // TODO: handle exception
            }
			
			if (null != account.getCurStep()) {
				String step = StrUtils.getString(account.getCurStep());
				if ("RPQ".equalsIgnoreCase(step))
					return accountRPQ(request, response, model);
				else if ("Basic".equalsIgnoreCase(step))
					return accountBasic(request, response, model);
				else if ("Doc".equalsIgnoreCase(step))
					return accountDoc(request, response, model);
				else if ("Declare".equalsIgnoreCase(step))
					return accountDeclare(request, response, model);
				else if ("Submit".equalsIgnoreCase(step))
					return accountSubmit(request, response, model);
			}

			// 邀请开户由IFA发送待办，由投资者自己填写，不允许修改IFA和代理商，故直接跳转到RPQ页面。
			if (null != account.getSubmitStatus() && "ifa".equals(account.getSubmitStatus())) {
				// return accountByIFA(request, response, model);
				return accountRPQ(request, response, model);
			}
		}

		// 获取IFA信息
		try {
			// 已保存开户信息
			if (null != account && null != account.getIfa() && null != account.getIfa().getMember() 
					&& null != account.getIfa().getMember().getId()
			        && !"".equals(account.getIfa().getMember().getId()))
				ifaId = account.getIfa().getMember().getId();
			model.put("ifaId", ifaId);
			model.put("distributorId", account.getDistributor().getId());

		} catch (Exception e) {
			// TODO: handle exception
		}
		// 获取代理商信息
		model.put("distributors", investorService.findIfaDistributors(ifaId));

		// 是否ifa
		MemberIfa ifa = investorService.findIfaByMemberId(loginUser.getId());
		if (null != ifa && null != ifa.getId() && !"".equals(ifa.getId())) {
			String indId = request.getParameter("indId");// 独立投资人ID由其他功能页面传入，暂时起名
			model.put("indId", indId == null ? "" : indId);
			return accountByIFA(request, response, model);
		}
		return "investor/open/accountStart";// 默认跳转到投资人开户
	}

	/**
	 * step1 - get IFA distributors
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/getIfaDistributors", method = RequestMethod.GET)
	public void getIfaDistributors(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// MemberBase loginUser = getLoginUser(request);
		String ifaId = request.getParameter("ifaId");
		List<MemberDistributor> distributors = null;
		// if (null!=ifaId && !"".equals(ifaId))
		distributors = investorService.findIfaDistributors(ifaId);
		if (!distributors.isEmpty())
		for (MemberDistributor x: distributors){
			x.setLogofile(PageHelper.getLogoUrl(x.getLogofile(), "D"));
		}
		ResponseUtils.renderHtml(response, JsonUtil.toJson(distributors));
	}

	/**
	 * step1 - save selection
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/saveAccountStart", method = RequestMethod.POST)
	public String saveAccountStart(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		Map<String, Object> result = new HashMap<String, Object>();

		if (loginUser != null) {
			//非investor和IFA用户，拒绝访问
			String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
			if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role))){
				result.put("result", false);
				result.put("code", "error.accessForbidden.title");
				result.put("msg", getProperty(request, "error.accessForbidden"));
				JsonUtil.toWriter(result, response);				
				return null;
			}			

			try {
				String accountId = request.getParameter("accountId");
				String investment = request.getParameter("investment");
				String ifaId = request.getParameter("ifaId");
				String distributorId = request.getParameter("distributorId");
				
				
				//判断是否已有主账户
				try {
					if (checkIfHadAcount(distributorId,loginUser.getId())){
						result.put("result", false);
						result.put("code", "wsconstants.msg2002");
						result.put("msg", getProperty(request, "open.account.exist"));//Main account is exists.
						JsonUtil.toWriter(result, response);
						return null;
					}
	            } catch (Exception e) {
		            // TODO: handle exception
	            }
	            
				InvestorAccount account = investorService.findInvestorAccountById(accountId);
				if (null == accountId || "".equals(accountId) || null == account || null == account.getId() || "".equals(account.getId())) {
					account = new InvestorAccount();
					account.setCreateTime(new Date());
					account.setCreateBy(loginUser);
					account.setIsValid("1");

					account.setMember(loginUser);
					account.setAccountNo("");// 非空
					account.setSubmitStatus("invest");// invest投资人提交,ifa提交
					
					//旧开户状态:-2待推送,-1已推送等待结果,1成功开户,0失败
					//新开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
					account.setOpenStatus("0");
					account.setFinishStatus("0");// 是否完成:0草稿,1已提交
					account.setFlowStatus("-1");// -1还未进入流程,0流程进行中,1流程审核通过结束返回,2流程审核不通过结束返回
					account.setCurStep("Start");// 当前阶段:Start RPQ Basic Doc
					                            // Declare Submit

				} else {// 已存在 开户记录
					    // 邀请开户由IFA发送待办，由投资者自己填写，不允许修改IFA和代理商，故直接跳转到RPQ页面。
					if (null != account.getSubmitStatus() && "ifa".equals(account.getSubmitStatus())) {
						result.put("result", true);
						result.put("accountId", account.getId());
						result.put("code", "global.success.save");
						result.put("msg", getProperty(request, "global.success.save"));
						JsonUtil.toWriter(result, response);
						return null;
					}
				}
				account.setLastUpdate(new Date());
				account.setFromType(investment);

				if (null != investment && "ifa".equalsIgnoreCase(investment)) {
					MemberIfa ifa = investorService.findIfaByMemberId(ifaId);
					account.setIfa(ifa);
				} else {
					account.setIfa(null);
				}

				MemberDistributor distributor = memberManageService.findDistributorById(distributorId);
				account.setDistributor(distributor);

				account = investorService.saveOrUpdateInvestorAccount(account);
				result.put("result", true);
				result.put("accountId", account.getId());
				result.put("code", "global.success.save");
				result.put("msg", getProperty(request, "global.success.save"));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				result.put("result", false);
				result.put("code", "global.failed.save");
				result.put("msg", getProperty(request, "global.failed.save"));
			}
		} else {// 未登录，跳转到登录页面
			result.put("result", false);
			result.put("code", "error.noLogin");
			result.put("msg", getProperty(request, "error.noLogin"));
		}

		JsonUtil.toWriter(result, response);
		return null;
	}

	/**
	 * step 2 开户填写RPQ问卷 获取开户需要填的问题
	 * 
	 * @author scshi
	 * @date 20160811
	 * @param
	 * */
	@RequestMapping(value = "/accountRPQ", method = RequestMethod.GET)
	public String accountRPQ(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		
		if (loginUser != null) {
			//非investor和IFA用户，跳转到首页
			String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
			if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role)))
				return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页

			String accountId = request.getParameter("accountId");
			InvestorAccount investorAccount = rpqService.getInvestorAccount(accountId);
			boolean modifyFlag = true;
			if (checkAccountId(request, model)) {
				// 更新开户状态
				investorAccount = saveCurrentStep(accountId, "RPQ");
				
				// 是否ifa审批，如果是，表单为只读
				MemberIfa ifa = investorService.findIfaByMemberId(loginUser.getId());
				boolean ifaApproveCheck = this.checkIfIfaApprove(ifa, investorAccount);

				String pageType = "O";
				List<RpqListVO> examList = rpqService.findRpqExamList(accountId, pageType, this.getLoginLangFlag(request));
				if (null == examList || examList.isEmpty()) {
					// 新增，copy rpq_page
					String distributorId = investorAccount.getDistributor().getId();
					String clientType = "Individual";
					examList = rpqService.copyExamFromPage(distributorId, clientType, pageType, request, accountId);
				}

				for (int el = 0; el < examList.size(); el++) {
					RpqListVO obj = examList.get(el);
					if (null == obj.getLevel().getRiskLevel() && "1".equals(obj.getIsCalScore())) {
						modifyFlag = false;// 有需要评分但未评分的问卷
						break;
					}
				}
				model.put("rpqList", examList);
				model.put("ifaApproveCheck", ifaApproveCheck);
				model.put("validity", "true");
				model.put("accountId", accountId);
				model.put("investorAccount", investorAccount);
			}
			model.put("modifyFlag", modifyFlag);
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
		return "investor/open/accountExam";
	}

	/**
	 * step 2 - 根据传入的问卷id及得分查询 回答的风险等级
	 * 
	 * @author tejay zhu
	 * @param request
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/queryMyRpqLevel", method = RequestMethod.POST)
	public void queryMyRpqLevel(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String pageId = request.getParameter("pageId");
		int score = Integer.parseInt(request.getParameter("score"));
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("ret", WSConstants.SUCCESS);
		obj.put("data", investorService.findRpqPageLevel(pageId, score, "using", "1", this.getLoginLangFlag(request)));
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * 開戶step3 accountBasic
	 * 
	 * @author scshi
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/accountBasic", method = RequestMethod.GET)
	public String accountBasic(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
	
		if (loginUser != null) {
			//非investor和IFA用户，跳转到首页
			String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
			if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role)))
				return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
			
			String lang = this.getLoginLangFlag(request);
			// 财富来源
			List<GeneralDataVO> fundsSource = findSysParameters("", lang);
			String accountId = request.getParameter("accountId");
			InvestorAccount iAccount = investorService.findInvestorAccountById(accountId);
			if (checkAccountId(request, model)) {
				// 查询是否ifa角色
				MemberIfa ifa = investorService.findIfaByMemberId(loginUser.getId());
				boolean ifaApproveCheck = this.checkIfIfaApprove(ifa, iAccount);

				// 更新开户步骤
				saveCurrentStep(accountId, "Basic");

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
				model.put("ifaApproveCheck", ifaApproveCheck);
				model.put("accountId", accountId);
				model.put("iAccount", iAccount);
				model.put("iContact", iContact);
				model.put("iContactAddrR", iContactAddrR);
				model.put("iContactAddrP", iContactAddrP);
				model.put("iContactAddrC", iContactAddrC);
				model.put("iContactJoint", iContactJoint);
				model.put("iContactJointAddrR", iContactJointAddrR);
				model.put("iContactJointAddrP", iContactJointAddrP);
				model.put("iContactJointAddrC", iContactJointAddrC);
				model.put("iBank", iBank);
				model.put("fundSource", fundsSource);
			}
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
		return "investor/open/accountBasic";
	}

	/**
	 * 账户基本信息保存 作废--20160808--采用分步保存
	 * 
	 * @author scshi
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/accountBasicSave", method = RequestMethod.POST)
	public void accountBasicSave(HttpServletRequest request, HttpServletResponse response, ModelMap model, BasicInfoCombVo basicVo) {
		//非investor和IFA用户，拒绝访问
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role))){
			Map<String, Object> result = new HashMap<String, Object>();

			result.put("result", false);
			result.put("code", "error.accessForbidden.title");
			result.put("msg", getProperty(request, "error.accessForbidden"));
			JsonUtil.toWriter(result, response);				
			return;
		}			

		// investorService.saveOrUpdateBasicInfo(basicVo);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("msg", getProperty(request, "global.success.save"));
		obj.put("result", true);
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * step 4 Document Check 当前因仅有一个distributor的数据，为方便开发及测试，先写死 distributor的id 及
	 * clientType的类型
	 * 
	 * @author scshi
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accountDoc", method = RequestMethod.GET)
	public String accountDoc(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			//非investor和IFA用户，跳转到首页
			String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
			if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role)))
				return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
			
			if (checkAccountId(request, model)) {
				String accountId = request.getParameter("accountId");
				InvestorAccount iAccount = investorService.findInvestorAccountById(accountId);
				iAccount = saveCurrentStep(accountId, "Doc");
				
				String memberId = iAccount.getMember().getId();
				String accType = iAccount.getAccType();
				String distrubuteId = iAccount.getDistributor().getId();
				// test 假设每个登录用户只能开一个户（暂不考虑一个账号多次开户，且每次开户的联系人都不同的情况）；
				String clientType = "J".equals(accType)?"JonitAccount":"Individual";

				// 判断是是否IFA角色
				MemberIfa ifa = investorService.findIfaByMemberId(loginUser.getId());
				boolean ifaApproveCheck = this.checkIfIfaApprove(ifa, iAccount);

				String mainContactId = investorService.getAccountContactId(accountId, "M");
				List<DocListVO> contactDocList = investorService.findContactDocList(memberId, distrubuteId, mainContactId,
				        this.getLoginLangFlag(request),iAccount.getId());
				if (null == contactDocList || contactDocList.isEmpty()) {
					// 如果是首次进入文档检查，从模板复制doclist到invest
					investorService.copyDocListfromTemp(distrubuteId, clientType, this.getLoginLangFlag(request), memberId, mainContactId,iAccount);
					contactDocList = investorService.findContactDocList(memberId, distrubuteId, mainContactId, this.getLoginLangFlag(request),iAccount.getId());
				}
				model.put("contactDocList", contactDocList);

				/*** 作废，文档列表与联系人关系脱离，使用账户ID关联，无法判断文档与联系人关系
				if ("J".equals(accType)) {
					String jointContactId = investorService.getAccountContactId(accountId, "J");
					List<DocListVO> jContactDocList = investorService.findContactDocList(memberId, distrubuteId, jointContactId,
					        this.getLoginLangFlag(request),iAccount.getId());
					if (null == jContactDocList || jContactDocList.isEmpty()) {
						// 如果是首次进入文档检查，从模板复制doclist到invest
						investorService.copyDocListfromTemp(distrubuteId, clientType, this.getLoginLangFlag(request), memberId, jointContactId,iAccount);
						jContactDocList = investorService.findContactDocList(memberId, distrubuteId, jointContactId, this.getLoginLangFlag(request),iAccount.getId());
					}
					// 获取关联用户附件
					model.put("jContactDocList", jContactDocList);
				}
				***/
				model.put("ifaApproveCheck", ifaApproveCheck);
				model.put("accountId", accountId);
				model.put("acc_type", accType);// 账户类型,I:Individual Account
				                               // J:Joint Account
				// model.put("accountDocList",
				// investorService.findDocumentCheckList(distrubuteId,
				// clientType, this.getLoginLangFlag(request),
				// this.getLoginUser(request) ));
			}
			return "investor/open/accountDoc";
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/**
	 * step 4 - save Document Check 当前因仅有一个distributor的数据，为方便开发及测试，先写死
	 * distributor的id 及 clientType的类型
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accountDocSave", method = RequestMethod.POST)
	public void accountDocSave(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//非investor和IFA用户，拒绝访问
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role))){
			Map<String, Object> result = new HashMap<String, Object>();

			result.put("result", false);
			result.put("code", "error.accessForbidden.title");
			result.put("msg", getProperty(request, "error.accessForbidden"));
			JsonUtil.toWriter(result, response);				
			return;
		}			
		boolean submitFlag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", getProperty(request, "global.success.save"));
		map.put("result", submitFlag);
		JsonUtil.toWriter(map, response);
	}

	/**
	 * step 4 - 异步上传开户所需之附件
	 * 
	 * @author tejay zhu
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/uploadDoc")
	public void investorDocUpload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> obj = new HashMap<String, Object>();
		// String ctxPath =
		// request.getSession().getServletContext().getRealPath("/");
		Long moduleId = Long.parseLong(request.getParameter("moduleId"));
		String investDocId = request.getParameter("investDocId");
		String filePaths = "";
		String fileNames = "";
		try {
			// 创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// 取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if (file != null) {
						// 取得当前上传文件的文件名称
						String oriFileName = file.getOriginalFilename();
						// 如果名称不为"",说明该文件存在，否则说明该文件不存在
						if (!"".equals(oriFileName.trim())) {
							String myFilePath = UploadUtil.getFileName(oriFileName, true, moduleRelate.get(moduleId));
							// File localFile = new File(ctxPath + myFilePath);
							File localFile = new File(myFilePath);
							if (!localFile.getParentFile().exists()) {
								localFile.getParentFile().mkdirs();
							}
							file.transferTo(localFile);

							/********** 文件分隔存储的方法 mjhuang begin *************/
//							String suffix = myFilePath.substring(myFilePath.indexOf(".") + 1);
//							String tmpPath = myFilePath.substring(0, myFilePath.length() - suffix.length() - 1);
//							File tmpFile = new File(tmpPath);
//							tmpFile.mkdirs();
//							FileCutUnionUtil.cutFile(localFile, tmpFile);// 调用分割方法
//							localFile.delete();
							/********** 文件分隔存储的方法 end *************/

							if ("".equals(filePaths)) {
								filePaths = myFilePath;
								fileNames = oriFileName;
							} else {
								filePaths += ";" + myFilePath;
								fileNames += ";" + oriFileName;
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			map.put("ret", WSConstants.FAIL);
			map.put("errorCode", WSConstants.CODE1000);
			map.put("errorMsg", WSConstants.MSG1000);
			JsonUtil.toWriter(map, response);
		} catch (IOException e) {
			e.printStackTrace();
			map.put("ret", WSConstants.FAIL);
			map.put("errorCode", WSConstants.CODE1000);
			map.put("errorMsg", WSConstants.MSG1000);
			JsonUtil.toWriter(map, response);
		}

		// 文件存在，进入附件表
		AccessoryFile accessoryFile = new AccessoryFile();
		accessoryFile.setFileName(fileNames);
		accessoryFile.setFilePath(filePaths);
		accessoryFile.setFileType(fileNames.substring(fileNames.lastIndexOf(".") + 1, fileNames.length()));
		accessoryFile.setModuleType(moduleRelate.get(moduleId));// investor_doc,submit_doc
		accessoryFile.setLangCode(this.getLoginLangFlag(request));
		accessoryFile.setRelateId(investDocId);
		accessoryFile.setCreateBy(this.getLoginUser(request));
		AccessoryFile accessoryFilePr = investorService.saveOrUpdateFileToAccessoryFile(accessoryFile);

		// 设置返回对象
		obj.put("accessoryFile", accessoryFilePr);
		map.put("ret", WSConstants.SUCCESS);
		map.put("data", obj);
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * step 4 - 删除附件
	 * 
	 * @author tejay zhu
	 * @param request
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/deleteDoc")
	@ResponseBody
	public void deleteDoc(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//非investor和IFA用户，拒绝访问
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role))){
			Map<String, Object> result = new HashMap<String, Object>();

			result.put("result", false);
			result.put("code", "error.accessForbidden.title");
			result.put("msg", getProperty(request, "error.accessForbidden"));
			JsonUtil.toWriter(result, response);				
			return;
		}			

		String accessoryFileId = request.getParameter("accessoryFileId");
		AccessoryFile accessoryFile = investorService.getAccessoryFile(accessoryFileId);
		String filePath = accessoryFile.getFilePath();
		// 删除记录
		investorService.deleteAccessoryFile(accessoryFile);

		// 删除文件
		// UploadUtil.removeFileScore(filePath);

		/*** 删除文件碎片mjhuang begin ***/
		String suffix = filePath.substring(filePath.indexOf(".") + 1);
		String tmpPath = filePath.substring(0, filePath.length() - suffix.length() - 1);
		UploadUtil.delFolder(tmpPath);
		/*** 删除文件碎片mjhuang end ***/

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ret", WSConstants.SUCCESS);
		map.put("data", "");
		JsonUtil.toWriter(map, response);
	}

	/**
	 * step 5 声明 当前开发阶段暂无accountid，故为便于开发调试，先写死accountid为 tejay
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accountDeclare", method = RequestMethod.GET)
	public String accountDeclare(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			//非investor和IFA用户，跳转到首页
			String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
			if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role)))
				return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
			
			String accountId = request.getParameter("accountId");
			model.put("accountId", accountId);
			InvestorAccount account = investorService.findInvestorAccountById(accountId);
			if (checkAccountId(request, model)) {
				// 是否ifa审批
				MemberIfa ifa = investorService.findIfaByMemberId(loginUser.getId());
				boolean ifaApproveCheck = this.checkIfIfaApprove(ifa, account);

				// 更新开户状态
				saveCurrentStep(accountId, "Declare");

				model.put("ifaApproveCheck", ifaApproveCheck);
				model.put("declaration", investorService.findInvestorAccountDeclarationAgeasForAccount(accountId));
			}
			return "investor/open/accountDeclare";
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/**
	 * step 5 - 更新声明
	 * 
	 * @author tejay zhu
	 * @param request
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "/accountDeclareSave", method = RequestMethod.POST)
	@ResponseBody
	public void accountDeclareSave(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> map = new HashMap<String, Object>();
		//非investor和IFA用户，拒绝访问
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role))){
			Map<String, Object> result = new HashMap<String, Object>();

			result.put("result", false);
			result.put("code", "error.accessForbidden.title");
			result.put("msg", getProperty(request, "error.accessForbidden"));
			JsonUtil.toWriter(result, response);				
			return;
		}			

		String accountId = request.getParameter("accountId");
		if (null != accountId && !"".equals(accountId)) {

			map.put("msg", getProperty(request, "global.success.save"));

			// 保存声明确认
			InvestorAccountDeclaration ageas = new InvestorAccountDeclaration();
			ageas.setId(request.getParameter("id"));
			ageas.setDeclarationFlag(request.getParameter("declarationFlag"));
			ageas.setInformationFlag(request.getParameter("informationFlag"));
			ageas.setAimFlag(request.getParameter("aimFlag"));
			ageas.setAdvisorFlag(request.getParameter("advisorFlag"));
			ageas.setQualifiedFlag(request.getParameter("qualifiedFlag"));
			ageas = investorService.saveOrUpdateInvestorAccountDeclarationAgeas(ageas, accountId);

			map.put("result", true);
			map.put("data", ageas);

		} else {
			map.put("msg", getProperty(request, "global.failed.save"));
			map.put("result", false);
			map.put("data", null);
		}

		JsonUtil.toWriter(map, response);
	}

	/**
	 * step 5 声明 - declareByCustomer
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/declareByCustomer", method = RequestMethod.GET)
	public String declareByCustomer(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String ifIfaCheck = request.getParameter("ifIfaCheck");
		model.put("ifIfaCheck", ifIfaCheck);
		return "investor/open/declareByCustomer";
	}

	/**
	 * step 5 声明 - Personal Information Collection Statement
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/declareCollectionStatement", method = RequestMethod.GET)
	public String declareCollectionStatement(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String ifIfaCheck = request.getParameter("ifIfaCheck");
		model.put("ifIfaCheck", ifIfaCheck);
		return "investor/open/declareCollectionStatement";
	}

	/**
	 * step 5 声明 - declareByAdvisor
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/declareByAdvisor", method = RequestMethod.GET)
	public String declareByAdvisor(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String ifIfaCheck = request.getParameter("ifIfaCheck");
		model.put("ifIfaCheck", ifIfaCheck);
		return "investor/open/declareByAdvisor";
	}

	/**
	 * step 5 声明 - declareQualifiedPerson
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/declareQualifiedPerson", method = RequestMethod.GET)
	public String declareQualifiedPerson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String ifIfaCheck = request.getParameter("ifIfaCheck");
		model.put("ifIfaCheck", ifIfaCheck);
		return "investor/open/declareQualifiedPerson";
	}

	/**
	 * step 6 提交
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accountSubmit", method = RequestMethod.GET)
	public String accountSubmit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			//非investor和IFA用户，跳转到首页
			String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
			if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role)))
				return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
			
			String accountId = request.getParameter("accountId");
			model.put("accountId", accountId);
			
			//更新待办消息状态为已读
			try {
				webReadToDoService.updateReadToDoReaded(loginUser, accountId);
	        } catch (Exception e) {
		        // TODO: handle exception
	        	e.printStackTrace();
	        }
	        
			if (checkAccountId(request, model)) {
				model.put("validity", "true");

				// 更新开户步骤
				saveCurrentStep(accountId, "Submit");

				// 是否ifa审批
				InvestorAccount acc = investorService.findInvestorAccountById(accountId);
				MemberIfa ifa = investorService.findIfaByMemberId(loginUser.getId());
				model.put("account", acc);
				MemberBase accountUser = acc.getMember();
				boolean ifaApproveCheck = this.checkIfIfaApprove(ifa, acc);
				if (ifaApproveCheck) {
					List<AccessoryFile> list = investorService.findSubmitDocList("submit_doc", accountId);
					model.put("accountUser", accountUser);
					model.put("docList", list);
					return "investor/open/accountSubmitByIFA";
				}
			}
			return "investor/open/accountSubmit";
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/****************************************** 账户基本信息分步保存begin ******************************************/
	// 开户基本信息
	@RequestMapping(value = "/accountDetailSave", method = RequestMethod.POST)
	public void accountDetailSave(HttpServletRequest request, HttpServletResponse response, ModelMap model, BasicInfoCombVo basicVo) {
		//非investor和IFA用户，拒绝访问
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role))){
			Map<String, Object> result = new HashMap<String, Object>();

			result.put("result", false);
			result.put("code", "error.accessForbidden.title");
			result.put("msg", getProperty(request, "error.accessForbidden"));
			JsonUtil.toWriter(result, response);				
			return;
		}			

		InvestorAccount account = basicVo.getiAccount();
		// 更新开户状态
		if (null != account && null != account.getId() && !"".equals(account.getId())) {
			InvestorAccount acc = investorService.findInvestorAccountById(account.getId());

			acc.setAccType(account.getAccType());
			acc.setInvestType(account.getInvestType());
			acc.setCies(account.getCies());
			acc.setDividend(account.getDividend());
			acc.setBaseCurrency(account.getBaseCurrency());
			acc.setPurpose(account.getPurpose());
			acc.setPurposeOther(account.getPurposeOther());
			acc.setSentBy(account.getSentBy());
			acc.setLastUpdate(new Date());
			acc.setIsValid(account.getIsValid());
			investorService.saveOrUpdateInvestorAccount(acc);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", getProperty(request, "global.success.save"));
		map.put("result", true);
		JsonUtil.toWriter(map, response);
	}

	// 主要联系人信息保存
	@RequestMapping(value = "/mainContactSave", method = RequestMethod.POST)
	public void mainContactSave(HttpServletRequest request, HttpServletResponse response, ModelMap model, BasicInfoCombVo basicVo) {
		//非investor和IFA用户，拒绝访问
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role))){
			Map<String, Object> result = new HashMap<String, Object>();

			result.put("result", false);
			result.put("code", "error.accessForbidden.title");
			result.put("msg", getProperty(request, "error.accessForbidden"));
			JsonUtil.toWriter(result, response);				
			return;
		}			

		String accountId = request.getParameter("accountId");
		InvestorAccountContact iContact = basicVo.getiContact();
		if (null!=iContact){
			iContact.setContactType("M");
			if (StrUtils.isEmpty(iContact.getCountry().getId())) iContact.getCountry().setId(null);
			if (StrUtils.isEmpty(iContact.getNationality().getId())) iContact.getNationality().setId(null);
			if (StrUtils.isEmpty(iContact.getPrimaryResidence().getId())) iContact.getPrimaryResidence().setId(null);
		}
		String iContactId = investorService.saveOrUpdateMainContact(iContact, accountId);

		String addrRid = null;
		String addrPid = null;
		String addrCid = null;
		// 主要联系人地址信息保存
		InvestorAccountContactAddr iContactAddrR = basicVo.getiContactAddrR();
		if (null!=iContactAddrR){
			if (StrUtils.isEmpty(iContactAddrR.getCountry().getId())) iContactAddrR.getCountry().setId(null);
		}
		if (null != iContactAddrR.getDistrict() && !"".equals(iContactAddrR.getDistrict())) {
			iContactAddrR.setAddrType("R");// 居住地信息保存
			addrRid = investorService.saveOrUpdateIaddr(iContactAddrR, iContactId, accountId);
		}

		InvestorAccountContactAddr iContactAddrP = basicVo.getiContactAddrP();
		if (null!=iContactAddrP){
			if (StrUtils.isEmpty(iContactAddrP.getCountry().getId())) iContactAddrP.getCountry().setId(null);
		}
		if (null != iContactAddrP.getDistrict() && !"".equals(iContactAddrP.getDistrict())) {
			iContactAddrP.setAddrType("P");// 永久地信息保存
			addrPid = investorService.saveOrUpdateIaddr(iContactAddrP, iContactId, accountId);
		}

		InvestorAccountContactAddr iContactAddrC = basicVo.getiContactAddrC();
		if (null!=iContactAddrC){
			if (StrUtils.isEmpty(iContactAddrC.getCountry().getId())) iContactAddrC.getCountry().setId(null);
		}
		if (null != iContactAddrC.getDistrict() && !"".equals(iContactAddrC.getDistrict())) {
			iContactAddrC.setAddrType("C");// 通信地址保存
			addrCid = investorService.saveOrUpdateIaddr(iContactAddrC, iContactId, accountId);
		}

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("iContactId", iContactId);
		map.put("addr_rid", addrRid);
		map.put("addr_pid", addrPid);
		map.put("addr_cid", addrCid);
		map.put("result", true);
		JsonUtil.toWriter(map, response);
	}

	// 关联联系人保存
	@RequestMapping(value = "/jointContactSave", method = RequestMethod.POST)
	public void jointContactSave(HttpServletRequest request, HttpServletResponse response, ModelMap model, BasicInfoCombVo basicVo) {
		//非investor和IFA用户，拒绝访问
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role))){
			Map<String, Object> result = new HashMap<String, Object>();

			result.put("result", false);
			result.put("code", "error.accessForbidden.title");
			result.put("msg", getProperty(request, "error.accessForbidden"));
			JsonUtil.toWriter(result, response);				
			return;
		}			

		String accountId = request.getParameter("accountId");
		InvestorAccountContact jointContact = basicVo.getiContactJoint();
		if (null!=jointContact){
			jointContact.setContactType("J");
			if (StrUtils.isEmpty(jointContact.getCountry().getId())) jointContact.getCountry().setId(null);
			if (StrUtils.isEmpty(jointContact.getNationality().getId())) jointContact.getNationality().setId(null);
			if (StrUtils.isEmpty(jointContact.getPrimaryResidence().getId())) jointContact.getPrimaryResidence().setId(null);
		}
		String jointContactId = investorService.saveOrUpdateMainContact(jointContact, accountId);
		// 关联联系人保存
		InvestorAccountContactAddr jointContactAddrR = basicVo.getiContactJointAddrR();
		String addrRid = null;
		String addrPid = null;
		String addrCid = null;
		if (null!=jointContactAddrR){
			if (StrUtils.isEmpty(jointContactAddrR.getCountry().getId())) jointContactAddrR.getCountry().setId(null);
		}
		if (null != jointContactAddrR.getDistrict() && !"".equals(jointContactAddrR.getDistrict())) {
			jointContactAddrR.setAddrType("R");
			addrRid = investorService.saveOrUpdateIaddr(jointContactAddrR, jointContactId, accountId);
		}

		InvestorAccountContactAddr jointContactAddrP = basicVo.getiContactJointAddrP();
		if (null!=jointContactAddrP){
			if (StrUtils.isEmpty(jointContactAddrP.getCountry().getId())) jointContactAddrP.getCountry().setId(null);
		}
		if (null != jointContactAddrP.getDistrict() && !"".equals(jointContactAddrP.getDistrict())) {
			jointContactAddrR.setAddrType("P");
			addrPid = investorService.saveOrUpdateIaddr(jointContactAddrP, jointContactId, accountId);
		}

		InvestorAccountContactAddr jointContactAddrC = basicVo.getiContactJointAddrC();
		if (null!=jointContactAddrC){
			if (StrUtils.isEmpty(jointContactAddrC.getCountry().getId())) jointContactAddrC.getCountry().setId(null);
		}
		if (null != jointContactAddrC.getDistrict() && !"".equals(jointContactAddrC.getDistrict())) {
			jointContactAddrR.setAddrType("C");
			addrCid = investorService.saveOrUpdateIaddr(jointContactAddrC, jointContactId, accountId);
		}
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("jointContactId", jointContactId);
		map.put("jAddr_rid", addrRid);
		map.put("jAddr_pid", addrPid);
		map.put("jAddr_cid", addrCid);
		map.put("result", true);
		JsonUtil.toWriter(map, response);
	}

	// 银行信息保存
	@RequestMapping(value = "/accountBankSave", method = RequestMethod.POST)
	public void accountBankSave(HttpServletRequest request, HttpServletResponse response, ModelMap model, BasicInfoCombVo basicVo) {
		//非investor和IFA用户，拒绝访问
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role))){
			Map<String, Object> result = new HashMap<String, Object>();

			result.put("result", false);
			result.put("code", "error.accessForbidden.title");
			result.put("msg", getProperty(request, "error.accessForbidden"));
			JsonUtil.toWriter(result, response);				
			return;
		}			

		String accountId = request.getParameter("accountId");
		InvestorAccountBank iBank = basicVo.getiBank();

		String bankId = investorService.saveOrUpdateIbank(iBank, accountId);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", true);
		map.put("bankId", bankId);
		JsonUtil.toWriter(map, response);

	}

	/****************************************** 账户基本信息分步保存end ******************************************/

	/***************************************** 页面基本信息下拉列表begin **********************************************/
	/**
	 * 异步获取基础数据--用于下拉列表 （基金类型fund_type、教育程度education、就业情况employment、
	 * 货币类型currency_type
	 * 、主题分类fund_sector、职业分类occupation、年龄段age_scope、费用类型fees_item）
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadParamConfigJson", method = RequestMethod.POST)
	public void loadParamConfigJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String typeCode = request.getParameter("category");
		String land = this.getLoginLangFlag(request);
		List<GeneralDataVO> list = findSysParameters(typeCode, land);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", true);
		obj.put("paramConfigJson", list);
		JsonUtil.toWriter(obj, response);
	}

	/************************************** 页面基本信息下拉列表end ***************************************************/

	/***************************** 开户信息展示 *************************************/
	// 主页面
	@RequestMapping(value = "/accountInformation", method = RequestMethod.GET)
	public String accountInformation(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		MemberBase loginUser = getLoginUser(request);
		String accountId = request.getParameter("accountId");
		if (loginUser != null) {
			model.put("accountId", accountId);
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
		return "investor/approve/accountInfomartion";
	}

	// basic
	@RequestMapping(value = "/basicInformation", method = RequestMethod.GET)
	public String basicInformation(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
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

		return "investor/approve/basicInfo";
	}

	// rqp
	@RequestMapping(value = "/rpqInformation", method = RequestMethod.GET)
	public String rpqInformation(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String accountId = request.getParameter("accountId");
		InvestorAccount investorAccount = rpqService.getInvestorAccount(accountId);

		/*
		 * String examId = investorAccount.getRpqExam().getId(); RpqPageLevel
		 * level =
		 * rpqService.loadRpqLevelMsg(examId,this.getLoginLangFlag(request));
		 * List<RpqListVO> examList =
		 * rpqService.findExamList(examId,this.getLoginLangFlag(request));
		 * 
		 * model.put("level", level); model.put("rpqList",examList);
		 * model.put("examId",examId);
		 */
		model.put("validity", "true");
		model.put("accountId", accountId);
		model.put("investorAccount", investorAccount);

		return "investor/approve/rqpInfo";

	}

	// docCheck
	@RequestMapping(value = "/docCheckInformation", method = RequestMethod.GET)
	public String docCheckInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String accountId = request.getParameter("accountId");
		InvestorAccount iAccount = investorService.findInvestorAccountById(accountId);
		String memberId = iAccount.getMember().getId();
		String mainContactId = investorService.getAccountContactId(accountId, "M");
		String accType = iAccount.getAccType();
		String distrubuteId = iAccount.getDistributor().getId();

		List<DocListVO> contactDocList = investorService.findContactDocList(memberId, distrubuteId, mainContactId, this.getLoginLangFlag(request),accountId);
		if ("J".equals(accType)) {
			// 获取关联用户附件
			model.put("jContactDocList", null);
		}
		model.put("accountId", accountId);
		model.put("acc_type", accType);// 账户类型,I:Individual Account J:Joint
		                               // Account
		model.put("contactDocList", contactDocList);
		return "investor/approve/docCheckInfo";
	}

	// declare
	@RequestMapping(value = "/declareInformation", method = RequestMethod.GET)
	public String declareInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		model.put("declaration", investorService.findInvestorAccountDeclarationAgeasForAccount(accountId));
		return "investor/approve/declareInfo";
	}

	// approveList
	/**
	 * 开户流程，提交页面，嵌入显示的审批历史清单
	 */
	@RequestMapping(value = "/approvalListInfo", method = RequestMethod.GET)
	public String approvalListInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		// model.put("declaration",
		// investorService.findInvestorAccountDeclarationAgeasForAccount(accountId));
		List hisList = investorServiceForConsole.findApproveHis(accountId);
		model.put("approveHis", hisList);
		
		return "investor/open/approveList";
	}

	/***************************** 开户信息展示 *************************************/

	/**
	 * 提交开户申请
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sendEmail", method = RequestMethod.GET)
	public String sendEmail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		if (loginUser != null) {
			// String accountId = request.getParameter("accountId");
			// String urlString =
			// "http://localhost/comp/service/component/email/send.r";
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("to_member_id", "40280ad455de27960155de42f8990001");
			jsonObj.put("to_email_addr", "mic@mic.com");
			jsonObj.put("to_email_title", "emailTest");
			jsonObj.put("to_email_content", "test...");
			jsonObj.put("module_type", "fund");
			jsonObj.put("send_by", loginUser.getId());
			// String jsonString = jsonObj.toString();
			// String result = sendWebServiceByBody(urlString, jsonString);
			String result = sendEmailByWS(loginUser, jsonObj);
			model.put("result", result);
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
		return "investor/public/showmsg";
	}

	/**
	 * 提交开户申请到IFA
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/submitWorkFlow", method = RequestMethod.POST)
	public void submitWorkFlow(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> map = new HashMap<String, Object>();
		String accountId = StrUtils.getString(request.getParameter("accountId"));
		String comment = StrUtils.getString(request.getParameter("msg"));
		
		if (loginUser != null) {
			try {
				// 1、创建流程
				// String submitTo =
				// request.getParameter("submitTo");//investor,ifa,officer
				InvestorAccount account = investorService.findInvestorAccountById(accountId);
				// String msg = StrUtils.getString(request.getParameter("msg"));

				//已在走流程，非第一次提交，则转入更新流程。
				if (null!=account && null!=account.getFinishStatus() && "1".equals(account.getFinishStatus())) {
					//更新并获取下一环节审批人
					map = gotoNextFlowStep(request, accountId, loginUser.getId(), "1", comment);
					JsonUtil.toWriter(map, response);
					return;
				}
				
				//第一次提交，新建流程
				String result = WebServiceUtil.createFlow(accountId, loginUser.getId());// 适应与投资人和IFA提交流程
				// model.put("result",result);
				JSONObject jsonObject = JSONObject.fromObject(result);

				String ret = jsonObject.getString("ret");// 运行状态
				if (null != ret && WSConstants.SUCCESS.equals(ret)) {// 成功
					JSONObject data = jsonObject.getJSONObject("data");
					WorkFlowVO workFlowVO = new WorkFlowVO(data);

					String flowId = workFlowVO.getInstanseId();
					// String roleId = workFlowVO.getFlowRoleId();//接口没返回？？
					String flowCode = workFlowVO.getFlowCode();

					//2、保存流程实例ID,修改开户状态
					account.setOpenStatus("2");// 开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
					account.setFinishStatus("1");// 是否完成:0草稿,1已提交
					account.setFlowStatus("0");// 流程状态：-1还未执行,0流程进行中,1流程审核通过结束返回,2流程审核不通过结束返回
					account.setLastUpdate(new Date());
					account.setFlowId(flowId);
					account = investorService.saveOrUpdateInvestorAccount(account);
					
					// 根据下一步角色id获取用户列表
					String ifaId = account.getIfa().getMember().getId();// 目前固定

					// 3、创建IFA审核资料的todo记录
					//因可能部署到不同的二级Context，所以不应该保存request.getContextPath，需在todo显示时添加
//					String todoUrl = request.getContextPath()+"/front/investor/accountApprove.do?accountId=" + accountId;
					String todoUrl = "/front/investor/accountSubmit.do?accountId=" + accountId;

					result = WebServiceUtil.insertTodo(accountId, flowId, flowCode, loginUser.getId(), ifaId, todoUrl);
					model.put("result", result);
					jsonObject = JSONObject.fromObject(result);

					//2、保存流程实例ID,修改开户状态
					account.setOpenStatus("2");// 开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
					account.setFinishStatus("1");// 是否完成:0草稿,1已提交
					account.setFlowStatus("0");// 流程状态：-1还未执行,0流程进行中,1流程审核通过结束返回,2流程审核不通过结束返回
					account.setLastUpdate(new Date());
					account.setFlowId(flowId);
					account = investorService.saveOrUpdateInvestorAccount(account);
					
					map.put("result", true);
					map.put("msg", getProperty(request, "global.success.save"));
					map.put("data", result);

					
					// 调用公共方法，发送待办待阅消息
					// ** 接口insert中已有发送待办待阅消息sendWebReadToDo 
				} else {
					map.put("result", false);
					map.put("msg", jsonObject.getString("errorMsg"));
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				map.put("result", false);
				map.put("msg", getProperty(request, "error.exceptionThrew"));
			}
		} else {// 未登录，跳转到登录页面
			map.put("result", false);
			map.put("code", "error.noLogin");
			map.put("msg", getProperty(request, "error.noLogin"));
		}
		JsonUtil.toWriter(map, response);
	}

	/**
	 * 提交开户审批(IFA 退回investor修改 或 提交给工作台审核员审批）
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/approveWorkFlow", method = RequestMethod.POST)
	public void approveWorkFlow(HttpServletRequest request, HttpServletResponse response, ModelMap model, String accountId, String indId) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != loginUser) {
			// 1、查询流程状态
			// String accountId = request.getParameter("accountId");
			String checkStatus = StrUtils.getString(request.getParameter("checkStatus"));
			String comment = StrUtils.getString(request.getParameter("msg"));

			InvestorAccount account = investorService.findInvestorAccountById(accountId);

			
			//退回investor，如果未走流程，直接转投资人，已在流程中则走退回流程。
			if ("0".equals(checkStatus) && !(null!=account && null!=account.getFinishStatus() 
												&& "1".equals(account.getFinishStatus()))) {
				sendToClient(request, response, model, accountId, indId);
				return;
			}

			//更新并获取下一环节审批人
			map = gotoNextFlowStep(request, accountId, loginUser.getId(), checkStatus, comment);

			// 修改开户状态
			account.setOpenStatus("2");// 开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
			if ("0".equals(checkStatus))
				account.setOpenStatus("-1");
			
			if (StrUtils.isEmpty(account.getFlowStatus()))
				account.setFlowStatus("0");// 流程状态：-1还未执行,0流程进行中,1流程审核通过结束返回,2流程审核不通过结束返回
			
			if ("0".equals(checkStatus)) //退回
				account.setFlowStatus("2");
			
			account.setLastUpdate(new Date());
			account = investorService.saveOrUpdateInvestorAccount(account);
		} else {// 未登录，跳转到登录页面
			map.put("result", false);
			map.put("code", "error.noLogin");
			map.put("msg", getProperty(request, "error.noLogin"));
		}
		JsonUtil.toWriter(map, response);
	}


	private Map<String, Object> gotoNextFlowStep(HttpServletRequest request, String accountId, String currUserId, String checkStatus, String comment){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", false);
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		
		//获取下一环节审批人
		String result = WebServiceUtil.queryFlow(accountId, currUserId);
	
		JSONObject jsonObject = JSONObject.fromObject(result);
		String ret = jsonObject.getString("ret");// 运行状态
		// ret==ture 调用成功且未处理，ret==false,调用失败，读取ret.errorMsg得到失败原因
		if (null != ret && WSConstants.SUCCESS.equals(ret)) {// 成功,
			JSONObject data = jsonObject.getJSONObject("data");
			WorkFlowVO workFlowVO = new WorkFlowVO(data);
			String flowId = workFlowVO.getInstanseId();
			String roleId = workFlowVO.getFlowRoleId();// ""--investor、IFA
			String flowCode = workFlowVO.getFlowCode();// "10"
	
			// 2.更新当前流程
//			if (StrUtils.isEmpty(flowId)) {
//				flowId = account.getFlowId();
//			}
			result = WebServiceUtil.updateFlow(accountId, flowId, flowCode, currUserId, checkStatus, comment);
	
			// 3、插入下一待办(提交 officer)
			//updateFlow已返回下一个instanse
			jsonObject = JSONObject.fromObject(result);
			ret = jsonObject.getString("ret");// 运行状态
			if (null != ret && WSConstants.SUCCESS.equals(ret)) {// 成功
				data = jsonObject.getJSONObject("data");
				workFlowVO = new WorkFlowVO(data);
				flowId = workFlowVO.getInstanseId();// 获取下一流程的步骤；
				roleId = workFlowVO.getFlowRoleId();// 通过"IO"，退回""--investor
				flowCode = workFlowVO.getFlowCode();// 通过"20"，退回“1”
				if (!"0".equals(workFlowVO.getFlowState())) {//更新成功
					String idString = "";
					String todoUrl = "";
					
					if (StrUtils.isEmpty(roleId) || "null".equalsIgnoreCase(roleId)){//不存在下一环节角色，环节  1 invesotr、10 IFA
						if ("1".equals(flowCode))
							idString = account.getMember().getId();
						else if ("10".equals(flowCode))
							idString = account.getIfa().getId();
						//因可能部署到不同的二级Context，所以不应该保存request.getContextPath，需在todo显示时添加
						todoUrl = "/front/investor/accountStart.do?accountId=" + accountId;
					}else{//存在下一环节角色，环节  20-50
						//获取下一流程用户id
						idString = getFlowUserIds(accountId, roleId, currUserId);
		
						//todoUrl = "/front/investor/accountApprove.do?accountId=" + accountId;//旧
						//因可能部署到不同的二级Context，所以不应该保存request.getContextPath，需在todo显示时添加
						todoUrl = "/front/distributor/accountApprove.do?accountId=" + accountId;//新
					}
					
//					if (StrUtils.isEmpty(flowId)) {
//						flowId = account.getFlowId();
//					}
					result = WebServiceUtil.insertTodo(accountId, flowId, flowCode, currUserId, idString, todoUrl);
					jsonObject = JSONObject.fromObject(result);
					ret = jsonObject.getString("ret");// 运行状态
					if(null!=ret && WSConstants.SUCCESS.equals(ret)){//成功
						map.put("result", true);
						map.put("msg", getProperty(request, "global.success.save"));
					}else{
						
						map.put("msg", jsonObject.getString("errorMsg"));
					}
				}else{
					//更新失败，需回滚？重新提交？
					map.put("msg", jsonObject.getString("errorMsg"));
				}
			}
	
			map.put("data", result);
		}
		return map;
	}



	/**
	 * 更新工作流
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateWorkFlow", method = RequestMethod.GET)
	public String updateWorkFlow(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String actionMsg = "global.success";
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result", false);

		String completeStatus = "";// 完成状态 0 待选择然后下一步 1本次流程已完成不需要下一步操作并且待下一个流程
									// 2开户流程已完全完成
		if (loginUser != null) {
			String accountId = request.getParameter("accountId");
//			String flowCode = request.getParameter("flowCode");
			String checkStatus = request.getParameter("check_status");
			String comment = "comment";// request.getParameter("comment");
			InvestorAccount account = investorService.findInvestorAccountById(accountId);

			//1、获取下一环节审批人
			String result = WebServiceUtil.queryFlow(accountId, loginUser.getId());
			JSONObject jsonObject = JSONObject.fromObject(result);
			String ret = jsonObject.getString("ret");// 运行状态
			// ret==ture 调用成功且未处理，ret==false,调用失败，读取ret.errorMsg得到失败原因
			if (null != ret && WSConstants.SUCCESS.equals(ret)) {// 成功
				JSONObject data = jsonObject.getJSONObject("data");
				WorkFlowVO workFlowVO = new WorkFlowVO(data);
				String flowId = workFlowVO.getInstanseId();
				String roleId = workFlowVO.getFlowRoleId();// investor、IFA、IFA FIRM
				String flowCode = workFlowVO.getFlowCode();// "10"
				String beginOrEnd = "";
//				String flowRoleAlluser = "";
				
				//2、更新流程状态
				result = WebServiceUtil.updateFlow(accountId, flowId, flowCode, loginUser.getId(), checkStatus, comment);
				jsonObject = JSONObject.fromObject(result);
				ret = jsonObject.getString("ret");// 运行状态
				if (null != ret && WSConstants.SUCCESS.equals(ret)) {// 成功
					String curFlowStatus = jsonObject.getString("currFlowState");
					data = jsonObject.getJSONObject("data");
					workFlowVO = new WorkFlowVO(data);
					flowId = workFlowVO.getInstanseId();
					roleId = StrUtils.getString(workFlowVO.getFlowRoleId());
//						flowRoleAlluser = StrUtils.getString(workFlowVO.getFlowRoleAlluser());
					beginOrEnd = StrUtils.getString(workFlowVO.getBeginOrEnd());
					flowCode = workFlowVO.getFlowCode();
						
					if (FlowStates.RETURN_NEXT.equals(curFlowStatus)) {// 提交IFA 或 IFA FIRM
						if (!"2".equals(beginOrEnd)) {// 如果流程未结束
							String idString = "";
							String todoUrl = "";
//								if ("1".equals(flowRoleAlluser)) {//需所有环节人审批
								//获取下一流程用户id
							if (StrUtils.isEmpty(roleId) || "IFA".equals(roleId)){
								idString = account.getIfa().getMember().getId();
								todoUrl = "/front/investor/accountSubmit.do?accountId=" + accountId;
							}else{
								idString = getFlowUserIds(accountId, roleId, loginUser.getId());
								//因可能部署到不同的二级Context，所以不应该保存request.getContextPath，需在todo显示时添加
								todoUrl = "/front/distributor/accountApprove.do?accountId=" + accountId;//新
							}

							result = WebServiceUtil.insertTodo(accountId, flowId, flowCode, loginUser.getId(), idString, todoUrl);
							jsonObject = JSONObject.fromObject(result);
							ret = jsonObject.getString("ret");// 运行状态
							if (null != ret && WSConstants.SUCCESS.equals(ret)) {// 成功
								
							}
							
							if ("1".equals(checkStatus)) {
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
							account.setFlowId(flowId);

							// 更新账户信息
							investorService.saveOrUpdateInvestorAccount(account);
							completeStatus = "1";// 本次流程已完成不需要下一步操作并且待下一个流程
//								} else {
//									completeStatus = "0";// 0 待选择然后下一步
//									obj.put("roleId", roleId);
//									obj.put("instanseId", flowId);
//									obj.put("actionStatus", checkStatus);
//								}
						} else {// 流程结束
							obj.put("accountId", accountId);
							completeStatus = "2";// 整个流程结束
						}
					} else if (FlowStates.RETURN_PRE.equals(curFlowStatus)) {// 退回investor

//						if ("1".equals(flowRoleAlluser)) {
							String idString = "";
							String todoUrl = "/front/investor/accountSubmit.do?accountId=" + accountId;
							//获取下一流程用户id
							idString = account.getMember().getId();//investor
							data = jsonObject.getJSONObject("data");
							workFlowVO = new WorkFlowVO(data);
							flowId = workFlowVO.getInstanseId();
							result = WebServiceUtil.insertTodo(accountId, flowId, flowCode, loginUser.getId(), idString, todoUrl);
							jsonObject = JSONObject.fromObject(result);
							ret = jsonObject.getString("ret");// 运行状态
							if (null != ret && WSConstants.SUCCESS.equals(ret)) {// 成功
								
							}
							if ("1".equals(checkStatus)) {
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
							account.setFlowId(flowId);

							// 更新账户信息
							investorService.saveOrUpdateInvestorAccount(account);
							completeStatus = "1";// 本次流程已完成不需要下一步操作并且待下一个流程
//						} else {
//							completeStatus = "0";// 0 待选择然后下一步
//							obj.put("roleId", roleId);
//							obj.put("instanseId", roleId);
//							obj.put("actionStatus", checkStatus);
//						}

					} else if (FlowStates.DONE.equals(curFlowStatus)) {// //整个流程结束
						obj.put("accountId", accountId);
						completeStatus = "2";// 整个流程结束
					}
					obj.put("result", true);
					obj.put("status", completeStatus);
					obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), actionMsg, null));
				}else {
					String msg = jsonObject.getString("errorMsg");
					obj.put("msg", msg);
				}
			}
			obj.put("data", result);
			model.put("result", result);//??
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
		return "investor/public/showmsg";
	}

	/**
	 * 获取流程用户ID
	 * @param accountId
	 * @param roleId
	 * @param currUserId
	 * @return
	 */
	private String getFlowUserIds(String accountId, String roleId, String currUserId){
		String idString="";
		List<MemberBase> users = investorService.findMemberByRole(null, accountId, roleId);
		if (null != users && !users.isEmpty()){
			for (MemberBase m : users) {
				if (!m.getId().equals(currUserId))//过滤本人
					idString += m.getId() + ",";
			}
		}
		
		if (idString.endsWith(","))
			idString = idString.substring(0, idString.length() - 1);
		
		return idString;
	}
	
	/**
	 * step1 - start(IFA)
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/accountByIFA", method = RequestMethod.GET)
	public String accountByIFA(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		//非investor和IFA用户，跳转到首页
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role)))
			return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
		

		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		String indId = request.getParameter("indId");// 独立投资人ID由其他功能页面传入，暂时起名
		model.put("indId", indId == null ? "" : indId);

		if (loginUser != null) {
			MemberIfa ifa = investorService.findIfaByMemberId(loginUser.getId());
			if (null == ifa || null == ifa.getId() || "".equals(ifa.getId())) {
				// 非ifa用户，跳转到登录页面
				return "redirect:" + this.getFullPath(request) + "front/logout.do";
			} else {
				List<MemberDistributor> distributors = null;
				String ifaId = null;

				InvestorAccount account = investorService.findInvestorAccountById(accountId);
				if (null != account && null != account.getId()) {
					try {
						ifaId = account.getIfa().getMember().getId();
						indId = account.getMember().getId();
						model.put("ifaId", ifaId);
						model.put("distributorId", account.getDistributor().getId());
						model.put("indId", indId);

					} catch (Exception e) {
						// TODO: handle exception
					}
				} else {
					ifaId = loginUser.getId();
					indId = request.getParameter("indId");// 独立投资人ID由其他功能页面传入
					// MemberIndividual individual =
					// investorService.findIndividualByMemberId(indId);
				}

				// 代理商信息
				distributors = investorService.findIfaDistributors(ifaId);
				if (!distributors.isEmpty())
					for (MemberDistributor x: distributors){
						x.setLogofile(PageHelper.getLogoUrl(x.getLogofile(), "D"));
					}
				model.put("distributors", distributors);

				// 独立投资人信息
				try {
					IndividualDataVO vo = investorService.findIndividualFullInfoByMemberId(indId);
					model.put("individual", vo);
				} catch (Exception e) {
					// TODO: handle exception
					model.put("individual", null);
				}
			}
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
		return "investor/open/accountByIFA";
		// return "investor/open/inviteInvestor";
	}

	/**
	 * step1 - save selection(IFA) - accountByIFA
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/saveSelectionByIFA", method = RequestMethod.POST)
	public void saveSelectionByIFA(HttpServletRequest request, HttpServletResponse response, ModelMap model, String accountId, String indId) {
		// 保存开户信息（草稿）
		saveAccountByIFA(request, response, model, accountId, indId);
		Map<String, Object> result = (Map<String, Object>) model.get("result");
		JsonUtil.toWriter(result, response);
	}

	private void saveAccountByIFA(HttpServletRequest request, HttpServletResponse response, ModelMap model, String accountId, String indId) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();

		if (loginUser != null) {
			try {
				InvestorAccount account = investorService.findInvestorAccountById(accountId);

				MemberBase investor = null;
				// 独立投资人ID
				if ((null == indId || "".equals(indId)) && null != account) {
					investor = account.getMember();
					indId = investor.getId();
				} else
					investor = memberBaseService.findById(indId);

				// 邀请的投资者id
				if (null != investor && null != indId && !"".equals(indId)) {
					String investment = "ifa";// ifa代开户
					// String ifaId = request.getParameter("ifaId");
					String distributorId = request.getParameter("distributorId");

					MemberIfa ifa = investorService.findIfaByMemberId(loginUser.getId());
					if (null == ifa || null == ifa.getId() || "".equals(ifa.getId())) {
						// 非ifa用户，跳转到错误页面
						result.put("result", false);
						result.put("code", "global.failed.save");
						result.put("msg", getProperty(request, "global.failed.save") + "\n Login user is not a IFA.");
					} else {
						if (null == accountId || "".equals(accountId) || null == account || null == account.getId() || "".equals(account.getId())) {
							account = new InvestorAccount();
							account.setCreateTime(new Date());
							account.setIsValid("1");
							account.setFromType(investment);

							account.setIfa(ifa);

							// MemberBase investor =
							// memberBaseService.findById(indId);
							account.setMember(investor);
							account.setCreateBy(memberBaseService.findById(loginUser.getId()));

							account.setAccountNo("");// 非空
							account.setSubmitStatus("ifa");// invest投资人提交,ifa提交
							account.setOpenStatus("0");// 开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
							account.setFinishStatus("0");// 是否完成:0草稿,1已提交
							account.setFlowStatus("-1");// 流程状态：-1还未执行,0流程进行中,1流程审核通过结束返回,2流程审核不通过结束返回
							account.setCurStep("Start");// 当前阶段:Start RPQ Basic
							                            // Doc Dec Submit
						}

						account.setLastUpdate(new Date());

						// 非空则不设置代理商，不能修改
						if (null == account.getDistributor()) {
							MemberDistributor distributor = memberManageService.findDistributorById(distributorId);
							account.setDistributor(distributor);
						}

						//判断是否已有主账户
						try {
							if (checkIfHadAcount(distributorId,account.getMember().getId())){
								result.put("result", false);
								result.put("code", "wsconstants.msg2002");
								result.put("msg", getProperty(request, "open.account.exist"));//Main account is exists.
								model.put("result", result);
								return;
							}
			            } catch (Exception e) {
				            // TODO: handle exception
			            }
			            
						// checkStatus=0 发送client
						String checkStatus = StrUtils.getString(request.getParameter("checkStatus"));
						if ("0".equals(checkStatus)) {
							account.setCurStep("RPQ");// 发送给client时，设置步骤到RPQ
						}

						account = investorService.saveOrUpdateInvestorAccount(account);
						result.put("result", true);
						result.put("accountId", account.getId());
						result.put("code", "global.success.save");
						result.put("msg", getProperty(request, "global.success.save"));
					}
				} else {
					// 邀请的投资者id为空
					result.put("result", false);
					result.put("code", "global.failed.save");
					result.put("msg", getProperty(request, "global.failed.save") + "\n indId is missing.");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				result.put("result", false);
				result.put("code", "global.failed.save");
				result.put("msg", getProperty(request, "global.failed.save"));
			}
		} else {// 未登录，跳转到登录页面
			// return
			// "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
			result.put("result", false);
			result.put("code", "error.noLogin");
			result.put("msg", getProperty(request, "error.noLogin"));
		}
		model.put("result", result);
	}

	/**
	 * step1 - send to client - inviteInvestor
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/sendToClient", method = RequestMethod.POST)
	public void sendToClient(HttpServletRequest request, HttpServletResponse response, ModelMap model, String accountId, String indId) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();

		if (loginUser != null) {
			// String accountId = request.getParameter("accountId");
			InvestorAccount account = investorService.findInvestorAccountById(accountId);
			
			//已在走流程，非第一次提交，则转入更新流程。
			if (null!=account && null!=account.getFinishStatus() && "1".equals(account.getFinishStatus())) {
				approveWorkFlow(request, response, model, accountId, indId);
				return;
			}
			
			//未走流程，直接发待办信息
			MemberBase investor = null;
			// String indId = request.getParameter("indId");//独立投资人ID
			if ((null == indId || "".equals(indId)) && null != account) {
				investor = account.getMember();
				indId = investor.getId();
			} else
				investor = memberBaseService.findById(indId);

			if (null != investor && null != investor.getId()) {
				// 保存开户信息（草稿）
				saveAccountByIFA(request, response, model, accountId, indId);
				Map<String, Object> rlt = (Map<String, Object>) model.get("result");

				if (null != rlt && (Boolean) rlt.get("result")) {// 保存成功
					// String accountId = (String)rlt.get("accountId");

					// 调用公共方法，发送待办待阅消息
					sendWebReadToDo(loginUser, investor, CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL,
					        CommonConstantsWeb.WEB_READ_MODULE_INVITE_OPEN_ACCOUNT, accountId, "Invite investor to open account",
					        //"wmes.head.InviteInvestor");
					        "/front/investor/accountRPQ.do?accountId="+accountId);

					result.put("result", true);
					result.put("code", "global.success.save");
					result.put("msg", getProperty(request, "global.success.save"));

				} else {
					result.put("result", false);
					result.put("code", "global.failed.save");
					result.put("msg", getProperty(request, "global.failed.save"));
				}
			} else {
				result.put("result", false);
				result.put("msg", getProperty(request, "error.errorMessage"));
			}
		} else {// 未登录，跳转到登录页面
			result.put("result", false);
			result.put("code", "error.noLogin");
			result.put("msg", getProperty(request, "error.noLogin"));
		}
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 开户申请列表
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/applicationList", method = RequestMethod.GET)
	public String applicationList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		//非investor和IFA用户，跳转到首页
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_INVESTOR.equals(role)||CoreConstants.FRONT_USER_ROLE_IFA.equals(role)))
			return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
		
		jsonPaging = new JsonPaging();
		jsonPaging.setPage(0);
		jsonPaging.setRows(null);
		// jsonPaging.setSort("lastUpdate");
		// jsonPaging.setOrder("desc");
		if (loginUser != null) {
			// 如果登录用户非investor，则跳到首页
			if (CoreConstants.FRONT_USER_ROLE_INVESTOR.equalsIgnoreCase(StrUtils.getString(request.getSession().getAttribute(
			        CoreConstants.FRONT_USER_ROLE)))) {
				// jsonPaging = investorService.findInvestorAccountList(jsonPaging,
				// loginUser);
				// 待提交的开户申请，openSts=0 草稿，finishSts=0 草稿， flowSts="-1" 还未执行
				jsonPaging = investorService.findInvestorAccountList(jsonPaging, loginUser, "application", "", "0", "0", "-1");
				model.put("approvalList", jsonPaging.getList());
			}
			else if (CoreConstants.FRONT_USER_ROLE_IFA.equalsIgnoreCase(StrUtils.getString(request.getSession().getAttribute(
			        CoreConstants.FRONT_USER_ROLE)))) {
				// 待提交的开户申请，openSts=-2 待推送，finishSts=0 草稿， flowSts="-1" 还未执行
				jsonPaging = investorService.findInvestorAccountList(jsonPaging, loginUser, "ifaDraft", "", "-2", "0", "-1");
				model.put("approvalList", jsonPaging.getList());
			}else{
				return "redirect:" + this.getFullPath(request) + "index.do";
			}
			
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
		return "investor/list/applicationList";
	}

	/**
	 * 开户审批列表 - IFA
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/approvalList", method = RequestMethod.GET)
	public String approvalList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		if (loginUser != null) {
			// 如果登录用户非IFA，则跳到首页
			if (!CoreConstants.FRONT_USER_ROLE_IFA.equalsIgnoreCase(StrUtils.getString(request.getSession().getAttribute(
			        CoreConstants.FRONT_USER_ROLE)))) {
				return "redirect:" + this.getFullPath(request) + "index.do";
			}
			// jsonPaging = investorService.findInvestorAccountList(jsonPaging,
			// loginUser);
			// 待审批的开户，openSts=-1 已推送，finishSts=1 已提交， flowSts="0" 执行中
			jsonPaging = investorService.findInvestorAccountList(jsonPaging, loginUser, "approval", "", "-1", "1", "0");
			model.put("approvalList", jsonPaging.getList());
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
		return "investor/list/approvalList";
	}

	/**
	 * 开户申请审批（IFA、IFA supervisor）
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accountApprove", method = RequestMethod.GET)
	public String accountApprove(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		if (loginUser != null) {
			String accountId = request.getParameter("accountId");
			model.put("accountId", accountId);
			
			// 获取登录人角色，判断是 投资人/IFA/IFA supervisor/代理商
			InvestorAccount account = investorService.findInvestorAccountById(accountId);
			
			//如果账号信息不完整，返回用户首页
			if (null==account || null==account.getId() || null==account.getIfa() || null==account.getMember())
				return "redirect:"+this.getFullPath(request)+"front/investor/home.do";
			
			try {
				model.put("account", account);
            } catch (Exception e) {
	            // TODO: handle exception
            }
			
			try {
				MemberIndividual investor = memberBaseService.findIndividualMember(account.getMember().getId());
				model.put("investor", investor);
            } catch (Exception e) {
	            // TODO: handle exception
            }
			
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}

		return "investor/approve/accountInfomartion";
		// return "investor/open/accountApprove";
	}

	/**
	 * 开户申请审批 - 保存审批结果
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/saveApproval", method = RequestMethod.POST)
	public void saveApproval(HttpServletRequest request, HttpServletResponse response, ModelMap model, String accountId, String indId) {
		this.approveWorkFlow(request, response, model, accountId, indId);
	}

	/**
	 * 我的账号列表
	 * 
	 * @author michael modify by mqzou 2016-09-12 列表样式改变
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/myAccountList", method = RequestMethod.GET)
	public String myAccountList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		if (loginUser != null) {
			String langCode = getLoginLangFlag(request);
			String inUse = request.getParameter("in_use");// 账户状态
			String inApproval = request.getParameter("inApproval");
			String cancellation = request.getParameter("cancellation");
			String currency = request.getParameter("cur");// 基本货币

		//	String check=request.getParameter("check");
			if (null == currency || "".equals(currency))
				currency = loginUser.getDefCurrency();
			if (null == currency || "".equals(currency))
				currency = CommonConstants.DEF_CURRENCY;
			
			//旧开户状态:-2待推送,-1已推送等待结果,1成功开户,0失败
			//新开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
			String status = "";
			if (null != inUse && "1".equals(inUse)) {
				status += "'3',";
			}
			if (null != inApproval && "1".equals(inApproval)) {
				status += "'1','2',";
			}
			if (null != cancellation && "1".equals(cancellation)) {
//				status += "'0'";
				status += "'4'";
			}
			if (status.endsWith(",")) {
				status = status.substring(0, status.length() - 1);
			}


			InvestorAccount account = new InvestorAccount();
			account.setMember(loginUser);
			account.setOpenStatus(status);
 
			account.setBaseCurrency(currency);
			List<AccountVO> list = investorService.findAllAccountList(account);
			if(null!=list && !list.isEmpty()){
				Iterator it=list.iterator();
				while (it.hasNext()) {
					AccountVO vo = (AccountVO) it.next();
					vo.setBaseCurName(sysParamService.findNameByCode(vo.getBaseCurrency(), langCode));
					
				}
			}
			model.put("accountList", list);
			
			/*if(null==check || "".equals(check) || "0".equals(check)){
				AccountValidVO validVO=iTraderSendService.saveCheckExistValid(request, loginUser.getId());
				if (null!=validVO)
					model.put("checkList", validVO.getValidAccountNo());
				model.put("accountType", CommonConstantsWeb.WEB_ACCOUNTCHECK_INVESTOR);
			}*/
			
			int processCount = 0;
			if (null != list) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					AccountVO vo = (AccountVO) it.next();
					if ("-1".equals(vo.getOpenStatus()) || "-2".equals(vo.getOpenStatus())) {
						processCount++;
					}
				}
			}
			model.put("processCount", processCount);
			List distributorList = investorService.findInvestorDistributor(account);
			model.put("distributorList", distributorList);
			model.put("in_use", inUse);
			model.put("inApproval", inApproval);
			model.put("cancellation", cancellation);
			model.put("cur", currency);
			String curName=sysParamService.findNameByCode(currency, langCode);
			model.put("curName", curName);
			String numFormat="#,##0.00";
			if("JPY".equals(currency)){
				numFormat="#,##0";
			}
			model.put("numFormat", numFormat);
			
			String displayColor=loginUser.getDefDisplayColor();
			if(null==displayColor || "".equals(displayColor)){
				displayColor=CommonConstants.DEF_DISPLAY_COLOR;
			}
			model.put("displayColor", displayColor);

			List<GeneralDataVO> itemList = findSysParameters("currency_type", langCode);
			model.put("currencyType", itemList);

		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}

		return "investor/list/myAccountList";
	}

	/**
	 * 我的账号详细信息
	 * 
	 * @author mqzou
	 * @date 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/myAccountDetail", method = RequestMethod.GET)
	public String accountDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser=getLoginUser(request);
		String accountId = request.getParameter("id");
		String currency = request.getParameter("cur");
		String langCode = this.getLoginLangFlag(request);
		
		if(null==currency || "".equals(currency))
			currency=loginUser.getDefCurrency();
		if(null==currency || "".equals(currency))
			currency=CommonConstants.DEF_CURRENCY; 
         
		
		InvestorAccountVO vo=investorService.findAccountDetail(accountId, langCode, currency);
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		

		if (null != account) {
			/*if (null == currency || "".equals(currency))
				currency = account.getBaseCurrency();
			
			
			InvestorAccountVO vo = new InvestorAccountVO();
			vo.setIfa(account.getIfa());
			vo.setAccountId(account.getId());
			vo.setAccountNo(account.getAccountNo());
			vo.setAccType(account.getAccType());
			vo.setAuthorized(account.getAuthorized());
			vo.setBaseCurrency(account.getBaseCurrency());
			vo.setSourceFrom(account.getSourceFrom());
			String currencyName=sysParamService.findNameByCode(account.getBaseCurrency(), langCode);
			vo.setCurrency(currencyName);
			if (null!=account && null!=account.getMember())
				vo.setIconUrl(account.getMember().getIconUrl());
			vo.setCies(account.getCies());
			
			if (null!=account.getDistributor()){
				vo.setDistributorId(account.getDistributor().getId());
				vo.setDistributorName(account.getDistributor().getCompanyName());
				vo.setDistributorIconUrl(PageHelper.getLogoUrl(account.getDistributor().getLogofile(), "D"));
			}
			vo.setFaca(account.getFaca());
			
			if (null!=account.getMember()){
				vo.setLoginCode(account.getMember().getLoginCode());
				MemberIndividual individual = memberBaseService.findIndividualMember(account.getMember().getId());
				if (null != individual) {
					vo.setGender(individual.getGender());
					vo.setFirstName(individual.getFirstName());
					vo.setLastName(individual.getLastName());
				}
			}
			
			vo.setId(account.getId());

			InvestorAccountCurrencyVO currencyVO = investorService.findAccountCurrency(vo.getAccountNo(), currency);
			if (null != currency) {
				vo.setTotalValue(String.valueOf(currencyVO.getTotalAssets()));
				vo.setCashAvailable(String.valueOf(currencyVO.getCashAvailable()));
				vo.setCashForPendingTran(String.valueOf(currencyVO.getCashHold()));
				vo.setCashValue(String.valueOf(currencyVO.getCashValue()));
				vo.setMarketValue(currencyVO.getMarketValue());
				vo.setCashWithdrawal(String.valueOf(currencyVO.getCashWithdrawal()));
				vo.setCashRatio(StrUtils.getNumberString(currencyVO.getCashValue() / currencyVO.getTotalAssets() * 100));

				vo.setMarketRatio(StrUtils.getNumberString(currencyVO.getMarketValue() / currencyVO.getTotalAssets() * 100));
			} else {
				vo.setCashRatio("0");
				vo.setMarketRatio("0");
			}
			vo.setSubFlag(account.getSubFlag());
			vo.setMemberId(account.getMember().getId());*/

			model.put("account", vo);
			
			List currencyList=investorService.findAccountCurrency(accountId);
			model.put("currencyList", currencyList);
			
			String numFormat="#,##0.00";
			if("JPY".equals(currency)){
				numFormat="#,##0";
			}
			model.put("numFormat", numFormat);

			// List<InvestorAccount> subList =
			// investorService.findSubAccountList(account);
			/*List<InvestorAccountCurrencyVO> subList = investorService.findAccountCurrencyList(vo.getAccountNo());
			model.put("subList", subList);*/
			//获取hold数据
			int number = 0;
			List<InvestorAccountHoldVO> investorAccountHoldVOList = new ArrayList<InvestorAccountHoldVO>();
			List<PortfolioHoldProduct> portfolioHoldProductList = investorService.findPortfolioHoldProductByAccountId(accountId);
			if(null!=portfolioHoldProductList&&!portfolioHoldProductList.isEmpty()){
				for(PortfolioHoldProduct each : portfolioHoldProductList){
					number++;
					InvestorAccountHoldVO holdvo = new InvestorAccountHoldVO();
					holdvo.setNumber(number);
					holdvo.setAccountId(accountId);
					holdvo.setAvailableUnit(each.getAvailableUnit());
					holdvo.setBaseCurrency(each.getBaseCurrency());
					//holdvo.setMemberId(memberId);
					holdvo.setReferencecost(each.getReferenceCost());
					holdvo.setHoldingUnit(each.getHoldingUnit());
					//获取产品信息
					ProductInfo product = each.getProduct();
					if(null!=product&&""!=product.getId()){ //获取持苍产品，判断是基金或股票或债卷  产品类型,fund基金,stock股票,bond债券,futures期货
						String productType = product.getType();
						String productId = product.getId();
						if("fund".equals(productType)){//如果是基金
							FundInfo fund = fundInfoService.getFundInfoByProductId(productId, langCode);
							if(null!=fund){
								holdvo.setProductInformation(fund.getTempFundName());
								holdvo.setLatestMarketPrice(fund.getLastNav());
								holdvo.setProductId(fund.getId());
								
								holdvo.setProductType(productType);
								investorAccountHoldVOList.add(holdvo);
							}
						} 
//						else if("stock".equals(productType)){
//							
//						}
//						 else if("bond".equals(productType)){
//							 BondInfo bond = fundInfoService.getBondInfoByProductId(productId, langCode);	
//							 if(null!=bond){
//								 holdvo.setProductInformation(bond.getBondName());
//								 //holdvo.setLatestMarketPrice(StrUtils.getDouble(bond.getExchangeCode()));
//							 }
//						}
						
					}
					
				}
			}
			model.put("investorAccountHoldVOList", investorAccountHoldVOList);
			// 获取该账号的KYC、背景信息、培训信息、交易记录
			// 判断当前登录的角色是投资人还是IFA
			Integer memberType = 0;
			//MemberBase loginUser = getLoginUser(request);
			if (loginUser != null) {
				memberType = loginUser.getMemberType();
				model.put("curMemberType", memberType);
			}

			List<InvestorBackground> ibList = investorService.findInvestorbackground(account.getMember().getId());
			for (int i = 0; i < ibList.size(); i++) {
				ibList.get(i).setNumber(i + 1);
			}
			model.put("ibList", ibList);

			List<InvestorTraining> trainingList = investorService.findInvestorTraining(account.getMember().getId());
			/*for (int i = 0; i < trainingList.size(); i++) {
				trainingList.get(i).setNumber(i + 1);
			}*/
			model.put("trainingList", trainingList);

			List<RpqExam> rqpExamList = investorService.findRpqExamByMemberId(langCode, account.getMember().getId());
			if (1 == memberType) { // 如果是投资人，取最新一条
				if (null != rqpExamList && !rqpExamList.isEmpty()) {
					RpqExam latestRpq = rqpExamList.get(0);
					if (null != latestRpq) {
						Integer riskLevel = latestRpq.getRiskLevel();
						if(latestRpq.getUserRiskLevel() != null){
							riskLevel = latestRpq.getUserRiskLevel();
						}
						String riskLevelResult = rpqService.getRiskLevelResult(latestRpq.getId(), riskLevel, langCode);
						model.put("riskLevel", riskLevelResult);
						Date lastUpdate = latestRpq.getLastUpdate();
						Date expireDate = latestRpq.getExpireDate();
						try {
							if(null!=expireDate&&null!=expireDate){
								int betweenDay = DateUtil.daysBetween(lastUpdate, expireDate);
								model.put("betweenDay", betweenDay);
							} 
							
						} catch (ParseException e) {
							model.put("betweenDay", "");
							e.printStackTrace();
						}
					}
				}
			} else {
				model.put("rqpExamList", rqpExamList);
				if (null != rqpExamList && !rqpExamList.isEmpty()) {
					for (int i = 0; i < rqpExamList.size(); i++) {
						rqpExamList.get(i).setNumber(i + 1);
					}
					// RPQ过期提醒 如果全部RPQ过期，则按界面样例，显示提示信息，提醒用户更新RPQ
					Boolean isExpire = true;// 默认过期
					for (RpqExam each : rqpExamList) {
						String status = each.getStatus();
						if ("1".equals(status)) {
							isExpire = false;
							break;
						}
					}
					model.put("isExpire", isExpire);
				}
			}

			//List<InvestorAccountStream> orderReturnList = investorService.findAccountOrderReturn(langCode, accountId,"");
			//model.put("orderReturnList", orderReturnList);
			// **************获取该账号的KYC、背景信息、培训信息、交易记录结束*****************/
			
			//****************kyc文档审查begin *************************************************/
			String clientType = "J".equals(account.getAccType())?"JonitAccount":"Individual";
			//1==sub_flag 子账户，0==sub_flag 主账户
			InvestorAccount masterAccount = "1".equals(account.getSubFlag())?account.getMasterAccount():account;
			
			// kyc文档审核列表信息-scshi
			String mainContactId = investorService.getAccountContactId(accountId, "M");
			List<DocListVO> mainDoc = investorService.findContactDocList(vo.getMemberId(), vo.getDistributorId(), mainContactId, langCode,masterAccount.getId());
			if (null == mainDoc || mainDoc.isEmpty()) {
				// 如果是首次进入文档检查，从模板复制doclist到invest
				investorService.copyDocListfromTemp(vo.getDistributorId(), clientType, this.getLoginLangFlag(request), vo.getMemberId(),
						mainContactId,account);
			}else{
				//判断文档是否有更新
				kycDocCheckServic.checkTemplateUpdate(vo,mainContactId,clientType,masterAccount.getId());
			}
			mainDoc = investorService.findContactDocList(vo.getMemberId(), vo.getDistributorId(), mainContactId, this.getLoginLangFlag(request),masterAccount.getId());
			model.put("mainDoc", mainDoc);
			
			/****废除，investorDoc与联系人关系脱离，无法判断文档归属
			if ("J".equals(account.getAccType())) {
				String jointContactId = investorService.getAccountContactId(accountId, "J");
				List<DocListVO> jointDoc = investorService.findContactDocList(vo.getMemberId(), vo.getDistributorId(), jointContactId, langCode,account.getId());
				if (null == jointDoc || jointDoc.isEmpty()) {
					// 如果是首次进入文档检查，从模板复制doclist到invest
					investorService.copyDocListfromTemp(vo.getDistributorId(), clientType, this.getLoginLangFlag(request), vo.getMemberId(),
					        jointContactId,account);
				}else{
					//判断文档是否有更新
					kycDocCheckServic.checkTemplateUpdate(vo,jointContactId,clientType,docCode);
				}
				
				jointDoc = investorService.findContactDocList(vo.getMemberId(), vo.getDistributorId(), jointContactId,
						this.getLoginLangFlag(request),account.getId());
				model.put("jointDoc", jointDoc);
			}
			***/
			//****************kyc文档审查end *************************************************/
			
			model.put("cur", currency);
			String curName=sysParamService.findNameByCode(currency, langCode);
			model.put("curName", curName);

			List<GeneralDataVO> itemList = findSysParameters("currency_type", langCode);
			model.put("currencyType", itemList);

		}
		return "ifa/space/clientDetailKyc";
	}
	
	/**
	 * 通过类型获取hold数据
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/getPortfolioHoldProductByType", method = RequestMethod.GET)
	public void getPortfolioHoldProductByType(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//MemberBase loginUser = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		//Map<String, Object> result = new HashMap<String, Object>();
		String type=request.getParameter("type");
		String accountId = request.getParameter("id");
		//获取hold数据
		int number = 0;
		List<InvestorAccountHoldVO> investorAccountHoldVOList = new ArrayList<InvestorAccountHoldVO>();
		List<PortfolioHoldProduct> portfolioHoldProductList = investorService.findPortfolioHoldProductByAccountId(accountId);
		if(null!=portfolioHoldProductList&&!portfolioHoldProductList.isEmpty()){
			for(PortfolioHoldProduct each : portfolioHoldProductList){
				number++;
				InvestorAccountHoldVO holdvo = new InvestorAccountHoldVO();
				holdvo.setNumber(number);
				holdvo.setAccountId(accountId);
				holdvo.setAvailableUnit(each.getAvailableUnit());
				holdvo.setBaseCurrency(each.getBaseCurrency());
				//holdvo.setMemberId(memberId);
				holdvo.setReferencecost(each.getReferenceCost());
				holdvo.setHoldingUnit(each.getHoldingUnit());
				//获取产品信息
				ProductInfo product = each.getProduct();
				if(null!=product&&""!=product.getId()){ //获取持苍产品，判断是基金或股票或债卷  产品类型,fund基金,stock股票,bond债券,futures期货
					String productType = product.getType();
					String productId = product.getId();
					if(type.equals(productType)){
						if("fund".equals(productType)){//如果是基金
							FundInfo fund = fundInfoService.getFundInfoByProductId(productId, langCode);
							if(null!=fund){
								holdvo.setProductInformation(fund.getTempFundName());
								holdvo.setLatestMarketPrice(fund.getLastNav());
								holdvo.setProductId(fund.getId());
							}
						} else if("stock".equals(productType)){
							StockInfo stock = stockInfoService.getStockInfoByProductId(productId, langCode);
							if(null!=stock){
								holdvo.setProductInformation(stock.getStockName());
								holdvo.setProductId(stock.getId());
							}
						}
						 else if("bond".equals(productType)){
							 BondInfo bond = fundInfoService.getBondInfoByProductId(productId, langCode);	
							 if(null!=bond){
								 holdvo.setProductInformation(bond.getBondName());
								 //holdvo.setLatestMarketPrice(StrUtils.getDouble(bond.getExchangeCode()));
							 }
						}
						holdvo.setProductType(productType);
						investorAccountHoldVOList.add(holdvo);
					}

				}
				
			}
		}
		
		JsonUtil.toWriter(investorAccountHoldVOList, response);
	}

	/**
	 * 检查参数传递accountId是否合法
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	private boolean checkAccountId(HttpServletRequest request, ModelMap model) {
		String accountId = request.getParameter("accountId");
		model.put("accountId", accountId);
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		if (null == accountId || "".equals(accountId) || null == account || null == account.getId() || "".equals(account.getId())) {
			// 若 accountId 不正确，则进行页面提示并跳转到开户流程第一步
			model.put("validity", "false");
			return false;
		}
		return true;
	}

	/**
	 * 接口测试
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "investor/public/test";
	}

	/**
	 * 判断当前登录用户是否为账户关联IFA ifa 当前登录账号关联的ifa个人 account 当前访问账户
	 * */
	private boolean checkIfIfaApprove(MemberIfa ifa, InvestorAccount account) {
		if (null == ifa || null == ifa.getMember() || null == account || null == account.getIfa() || null == account.getIfa().getMember())
			return false;

		String ifaMemberId = ifa.getMember().getId();
		String accountMemberId = account.getIfa().getMember().getId();
		if (ifaMemberId.equals(accountMemberId))
			return true;

		return false;
	}

	/**
	 * 保存当前步骤
	 * 
	 * @param accountId
	 * @param step
	 */
	private InvestorAccount saveCurrentStep(String accountId, String step) {
		InvestorAccount account = investorService.findInvestorAccountById(accountId);

		if (!"1".equals(account.getFinishStatus())){//如已提交则不修改
			// 更新开户步骤
			account.setCurStep(step);
			return investorService.saveOrUpdateInvestorAccount(account);
		}
		return account;
	}

	/**
	 * 开户进度查询
	 * 
	 * @author mqzou
	 * @data 2016-09-20
	 * @return
	 */
	@RequestMapping(value = "/accountProgress", method = RequestMethod.GET)
	public String accountProgress(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String accountId = request.getParameter("id");
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		model.put("account", account);
		List<WfProcedureInstanseTodo> todoList = investorService.findAccountWfHistory(accountId);
		if (null != todoList && !todoList.isEmpty()) {
			Iterator<WfProcedureInstanseTodo> it = todoList.iterator();
			AccountProgressVO progress = new AccountProgressVO();
			while (it.hasNext()) {
				WfProcedureInstanseTodo todo = (WfProcedureInstanseTodo) it.next();
				String flowCode = todo.getFlowCode();
				int flowState = todo.getFlowState();

				if (0 == flowState) {
					if ("10".equals(flowCode)) {
						progress.setIfaApproval("0");// 待处理
					} else if ("20".equals(flowCode)) {
						progress.setIfafirmApproal("0");// 待处理
					} else if ("30".equals(flowCode)) {
						progress.setIfafirmRoApproval("0");// 待处理
					} else if ("40".equals(flowCode)) {
						progress.setDistributorApproval("0");// 待处理
					} else if ("50".equals(flowCode)) {
						progress.setDistributorRoApproval("0");// 待处理
					}

					// break;
				} else {
					if ("10".equals(flowCode)) {

						int checkStatus = todo.getCheckStatus();
						if (1 == checkStatus) {
							progress.setIfaApproval("1");// 通过
						} else {
							progress.setIfaApproval("2");// 退回
						}
					} else if ("20".equals(flowCode)) {
						int checkStatus = todo.getCheckStatus();
						if (1 == checkStatus) {
							progress.setIfafirmApproal("1");// 通过
						} else {
							progress.setIfafirmApproal("2");// 退回
						}
					} else if ("30".equals(flowCode)) {
						int checkStatus = todo.getCheckStatus();
						if (1 == checkStatus) {
							progress.setIfafirmRoApproval("1");// 通过
						} else {
							progress.setIfafirmRoApproval("2");// 退回
						}
					} else if ("40".equals(flowCode)) {
						int checkStatus = todo.getCheckStatus();
						if (1 == checkStatus) {
							progress.setDistributorApproval("1");// 通过
						} else {
							progress.setDistributorApproval("2");// 退回
						}
					} else if ("50".equals(flowCode)) {
						int checkStatus = todo.getCheckStatus();
						if (1 == checkStatus) {
							progress.setDistributorRoApproval("1");// 通过
						} else {
							progress.setDistributorRoApproval("2");// 退回
						}
					} else if ("60".equals(flowCode)) {
						progress.setCompleteApproval("1");// 待处理
					}

				}

			}
			model.put("progress", progress);
		}
		model.put("isIFA", false);
		return "investor/open/accountProgress";
	}

	/**
	 * 查看开户pdf
	 * 
	 * @author mqzou
	 * @data 2016-09-22
	 * @return
	 */
	@RequestMapping(value = "/dialogshowpdf", method = RequestMethod.GET)
	public String dialogshowpdf(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		/*
		 * String accountId = request.getParameter("accountId"); String lang =
		 * this.getLoginLangFlag(request);
		 */

		/*
		 * List pdfList = investorService.findAccountFileList(accountId, lang,
		 * "appli_form_pdf");// 开户的pdf文件 model.put("pdfList", pdfList);
		 */
		return "investor/open/dialog_showpdf";
	}

	/******************************************* 投资人KYC、背景调查、培训 *************************************************/
	/**
	 * @author 林文伟 获取投资人背景调查、培训列表
	 */
	@RequestMapping(value = "/background", method = RequestMethod.GET)
	public String backgroundList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String lang = this.getLoginLangFlag(request);
		MemberBase curMember = this.getLoginUser(request);
		String fromMemberId = curMember.getId();
		model.put("fromMemberId", fromMemberId);
		List<InvestorBackground> list = investorService.findInvestorbackground(fromMemberId);
		model.put("blist", list);

		List<InvestorTraining> trainingList = investorService.findInvestorTraining(fromMemberId);
		model.put("trainingList", trainingList);

		List<RpqExam> rqpExamList = investorService.findRpqExamByMemberId(lang, fromMemberId);
		model.put("rqpExamList", rqpExamList);

		return "investor/background/background";
	}

	/****
	 * 添加背景信息
	 * 
	 * @author 林文伟
	 * @date 2016-07-18
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/saveInvestorBackground", method = RequestMethod.POST)
	public void saveInvestorBackground(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String sourceFrom = request.getParameter("sourceFrom");
		sourceFrom = sourceFrom.replace('+', ',');// 表单序列化时多个数据时为+号
		String status = request.getParameter("status");
		String result = request.getParameter("result");
		String comment = request.getParameter("comment");
		String id = request.getParameter("ibId");
		// 获取当前登录者信息
		MemberBase curMember = this.getLoginUser(request);
		// String fromMemberId=curMember.getId();

		InvestorBackground ib = new InvestorBackground();
		ib.setComment(comment);
		ib.setCreateTime(new Date());
		ib.setIsValid("1");
		ib.setLastUpdate(new Date());
		ib.setMember(curMember);
		ib.setResult(result);
		ib.setReviewTime(new Date());
		ib.setSourceFrom(sourceFrom);
		ib.setStatus(status);
		if ("" != id)
			ib.setId(id);
		ib = investorService.saveOrUpdateInvestorBackground(ib);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != ib && "" != ib.getId()) {
			obj.put("result", true);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success", null));
		} else {
			obj.put("result", false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.failed", null));
		}

		ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
	}

	/**
	 * @author 林文伟 显示投资人背景调查页面
	 */
	@RequestMapping(value = "/getInvestorBackground", method = RequestMethod.GET)
	public String getInvestorBackground(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// String lang = this.getLoginLangFlag(request);
		String id = request.getParameter("id");
		InvestorBackground obj = investorService.getInvestorBackground(id);
		model.put("InvestorBackground", obj);
		return "investor/background/backgroundReview";
	}

	/**
	 * @author 林文伟 显示投资人培训记录页面
	 */
	@RequestMapping(value = "/getInvestorTraining", method = RequestMethod.GET)
	public String getInvestorTraining(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// String lang = this.getLoginLangFlag(request);
		String id = request.getParameter("id");
		InvestorTraining obj = investorService.getInvestorTraining(id);
		List<AccessoryFile> fileList = investorService.findSubmitDocList("investor_training", id);
		model.put("InvestorTraining", obj);

		List<AccessoryFile> newFileList = new ArrayList<AccessoryFile>();
		for (AccessoryFile file : fileList) {
			String filePath = file.getFilePath(); // /u/corner/201611/bcb92c76-7627-48ff-9a29-a65afa7271fd.JPG
			int lastIndex = filePath.lastIndexOf('.');
			String fistFilePath = filePath.substring(0, lastIndex);
			String lastFilePath = filePath.substring(lastIndex);
			String newFilePath = fistFilePath + "_thumbnails_112x100" + lastFilePath;
			AccessoryFile newAccessoryFile = new AccessoryFile();
			newAccessoryFile.setThumbnailFilePath112x100(newFilePath);
			newFileList.add(newAccessoryFile);
		}
		model.put("FileList", newFileList);
		return "investor/background/trainingRecord";
	}

	/**
	 * @author 林文伟 显示投资人培训记录页面
	 */
	@RequestMapping(value = "/getInvestorTrainingJson", method = RequestMethod.GET)
	public void getInvestorTrainingJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// String lang = this.getLoginLangFlag(request);
		String id = request.getParameter("id");
		InvestorTraining obj = investorService.getInvestorTraining(id);
		List<AccessoryFile> fileList = investorService.findSubmitDocList("investor_training", id);

		List<AccessoryFile> newFileList = new ArrayList<AccessoryFile>();
		for (AccessoryFile file : fileList) {
			String filePath = file.getFilePath(); // /u/corner/201611/bcb92c76-7627-48ff-9a29-a65afa7271fd.JPG
			int lastIndex = filePath.lastIndexOf('.');
			String fistFilePath = filePath.substring(0, lastIndex);
			String lastFilePath = filePath.substring(lastIndex);
			String newFilePath = fistFilePath + "_thumbnails_112x100" + lastFilePath;
			AccessoryFile newAccessoryFile = new AccessoryFile();
			newAccessoryFile.setId(file.getId());
			newAccessoryFile.setThumbnailFilePath112x100(newFilePath);
			newAccessoryFile.setFilePath(filePath);
			newAccessoryFile.setFileName(file.getFileName());
			newAccessoryFile.setFileType(file.getFileType());
			newFileList.add(newAccessoryFile);
		}
		obj.setFileList(newFileList);
		// 输出前端
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * @author 林文伟 显示投资人培训记录页面
	 */
	@RequestMapping(value = "/editInvestorTraining", method = RequestMethod.GET)
	public String editInvestorTraining(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// String lang = this.getLoginLangFlag(request);
		String id = request.getParameter("id");
		InvestorTraining obj = investorService.getInvestorTraining(id);
		List<AccessoryFile> fileList = investorService.findSubmitDocList("investor_training", id);
		model.put("InvestorTraining", obj);

		List<AccessoryFile> newFileList = new ArrayList<AccessoryFile>();
		for (AccessoryFile file : fileList) {
			String filePath = file.getFilePath(); // /u/corner/201611/bcb92c76-7627-48ff-9a29-a65afa7271fd.JPG
			int lastIndex = filePath.lastIndexOf('.');
			String fistFilePath = filePath.substring(0, lastIndex);
			String lastFilePath = filePath.substring(lastIndex);
			// System.out.println(fistFilePath);
			// System.out.println(lastFilePath);
			String newFilePath = fistFilePath + "_thumbnails_112x100" + lastFilePath;
			// System.out.println(newFilePath);
			AccessoryFile newAccessoryFile = new AccessoryFile();
			newAccessoryFile.setThumbnailFilePath112x100(newFilePath);
			newFileList.add(newAccessoryFile);
		}
		model.put("FileList", newFileList);
		return "investor/training/editTrainingRecord";
	}

	/**
	 * @author 林文伟 删除背景信息
	 */
	@RequestMapping(value = "/delInvestorBackground", method = RequestMethod.POST)
	public void delInvestorBackground(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		Boolean rs = investorService.delInvestorBackground(id);

		Map<String, Object> obj = new HashMap<String, Object>();
		if (rs) {
			obj.put("result", true);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success", null));
		} else {
			obj.put("result", false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.failed", null));
		}

		ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
	}

	/****
	 * 添加培训信息
	 * 
	 * @author 林文伟              //modify by wwluo 20170421
	 * @date 2016-07-18
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/saveInvestorTraining", method = RequestMethod.POST)
	public void saveInvestorTraining(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase logUser = this.getLoginUser(request);
		String id = request.getParameter("id");
		String name = this.toUTF8String(request.getParameter("name"));
		String starTime = request.getParameter("starTime");
		String endTime = request.getParameter("endTime");
		String organization = this.toUTF8String(request.getParameter("organization"));
		String description = this.toUTF8String(request.getParameter("description"));
		String imgData = this.toUTF8String(request.getParameter("imgData"));
		String distributorId = request.getParameter("distributorId");
		InvestorTraining it = null;
		if (StringUtils.isNotBlank(id)) {
			it = investorService.getInvestorTraining(id);
		}
		if(it == null){
			it = new InvestorTraining();
			it.setCreateTime(new Date());
			it.setCreator(logUser);
			it.setMember(logUser);
		}
		it.setContent(description);
		it.setEndTime(DateUtil.StringToDate(endTime, CommonConstants.FORMAT_DATE));
		it.setIsValid("1");
		it.setLastUpdate(new Date());
		it.setName(name);
		it.setOrganization(organization);
		it.setStartTime(DateUtil.StringToDate(starTime, CommonConstants.FORMAT_DATE));
		if (StringUtils.isNotBlank(distributorId)) {
			it.setDistributor(distributorService.findDistributorById(distributorId));
		}
		it = investorService.saveOrUpdateInvestorTraining(it);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != it && "" != it.getId()) {
			accessoryFileService.delAccessoryFile(it.getId(), "investor_training", null, null);
			// 存储上传图像
			if (StringUtils.isNotBlank(imgData)) {
				List<Map> images = JsonUtil.toListMap(imgData);
				if(images != null && !images.isEmpty()){
					for (Object image : images) {
						String img = image.toString();
						String[] each = img.split("\\|");
						AccessoryFile file = new AccessoryFile();
						file.setCreateBy(logUser);
						file.setCreateTime(new Date());
						file.setFileName(each[1]);
						file.setFilePath(each[3]);
						file.setModuleType("investor_training");
						file.setRelateId(it.getId());
						file.setFileType(each[0]);
						file = investorService.saveOrUpdateAccessoryFile(file);
					}
				}
			}
			obj.put("result", true);
		} else {
			obj.put("result", false);
		}
		ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
	}

	/**
	 * @author 林文伟 删除InvestorTraining
	 */
	@RequestMapping(value = "/delInvestorTraining", method = RequestMethod.POST)
	public void delInvestorTraining(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		Boolean rs = investorService.delInvestorTraining(id);

		Map<String, Object> obj = new HashMap<String, Object>();
		if (rs) {
			obj.put("result", true);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success", null));
		} else {
			obj.put("result", false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.failed", null));
		}

		ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
	}

	/**
	 * 我的好友列表
	 * 
	 * @author mqzou
	 * @tips： 2016-09-28
	 */
	@RequestMapping(value = "/mybuddy", method = RequestMethod.GET)
	public String myBuddy(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase memberBase = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		if (null != memberBase) {
			WebFriend webFriend = new WebFriend();
			

			webFriend.setCheckResult("0");
			webFriend.setToMember(memberBase);
			webFriend.setFromMember(null);
			List newList = webFriendService.findFriends(webFriend,langCode);// 新好友，待通过

			webFriend.setCheckResult("1");
			webFriend.setToMember(null);
			webFriend.setFromMember(memberBase);
			List myBuddyList = webFriendService.findFriends(webFriend,langCode);// 已通过的好友

			List<FocusMemberVO> list=communitySpaceService.findFocusMemberList(memberBase.getId(), memberBase.getId(),langCode);
			/*List list = ifaSpaceService.findSpaceVisit(memberBase.getId());
			List focusList = new ArrayList();
			if (null != list && !list.isEmpty()) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					String strId = (String) it.next();
					MemberIfa ifa = ifaManageService.findIfaById(strId);
					MyBuddyVO vo = new MyBuddyVO();
					if (null != ifa) {
						vo.setGender(ifa.getGender());
						vo.setId(strId);
						vo.setLoginCode(ifa.getMember().getLoginCode());
						vo.setMemberId(ifa.getMember().getId());
						focusList.add(vo);
					}

				}
			}
			*/

			model.put("focusList", list);

			model.put("newBuddy", newList);
			model.put("myBuddy", myBuddyList);
			
			model.put("isInvestor", "1");
			return "ifa/myBuddy";
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}

	}

	/****
	 * 交易记录筛选
	 * 
	 * @author 林文伟
	 * @date 2016-09-27
	 * @return 返回记录json
	 */
	@RequestMapping(value = "/getOrderReturnList", method = RequestMethod.POST)
	public void getOrderReturnList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String transactionType = request.getParameter("transactionType");
		String dataValue = request.getParameter("dataValue");
		// System.out.println("#######"+dataValue);
		String accountId = request.getParameter("id");
		jsonPaging = this.getJsonPaging(request);
		// List<OrderReturn> list =
		// investorService.findAccountOrderReturn(dataValue, accountId);
		jsonPaging = investorService.findAccountOrderReturn(jsonPaging,dataValue, accountId,transactionType);
		
		
//		MemberBase loginUser = getLoginUser(request);
//		MemberIndividual investor = null;
//		if(null!=loginUser){
//			int memberType = loginUser.getMemberType();
//			if(memberType==2){
//				String memberId = loginUser.getId();
//				MemberIfa ifa =  memberBaseService.findIfaMember(memberId);
//				if(null!=ifa&&""!=ifa.getId()){
//					String ifaId = ifa.getId();
//					IfaListfiltersVO filtersVO = new IfaListfiltersVO();
//					String lang = this.getLoginLangFlag(request);
//					String keyword = toUTF8String(request.getParameter("keyword"));
//
//					jsonPaging = this.getJsonPaging(request);
//					jsonPaging = ifaManageService.loadIFAMyTeamList(jsonPaging,ifaId, keyword);
//					// this.toJsonString(response, jsonPaging);
//					model.put("jsonPaging", jsonPaging);
//
//					this.toJsonString(response, jsonPaging);
//					// ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
//				}
//			}	
//		}

		ResponseUtils.renderHtml(response, JsonUtil.toJson(jsonPaging));
	}

	/**
	 * 查看申请表
	 * 
	 * @author wwluo
	 * @date 2016-11-01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/viewAplicationForm", method = RequestMethod.GET)
	public String viewAplicationForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = getLoginLangFlag(request);
		String accountId = request.getParameter("id");
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		if (StringUtils.isNotBlank(account.getId())) {
			InvestorAccountVO vo = new InvestorAccountVO();
			vo.setAccountNo(account.getAccountNo());
			vo.setAuthorized(account.getAuthorized());
			vo.setBaseCurrency(account.getBaseCurrency());
			vo.setCies(account.getCies());
			String companyName = account.getDistributor() != null ? account.getDistributor().getCompanyName() : null;
			vo.setDistributorName(companyName);
			vo.setFaca(account.getFaca());
			MemberIndividual individual = memberBaseService.findIndividualMember(account.getMember().getId());
			String gender = individual != null ? individual.getGender() : null;
			vo.setGender(gender);
			vo.setId(account.getId());
			String loginCode = account.getMember() != null ? account.getMember().getLoginCode() : null;
			vo.setLoginCode(loginCode);
			vo.setSubFlag(account.getSubFlag());
			model.put("account", vo);

			String memberId = account.getMember() != null ? account.getMember().getId() : null;
			String distributorId = account.getDistributor() != null ? account.getDistributor().getId() : null;
			// memberId = "40280ad455de27960155de42f8990001";
			distributorId = "5";
			String mainContactId = investorService.getAccountContactId(accountId, "M");
			List<DocListVO> mainDoc = investorService.findContactDocList(memberId, distributorId, mainContactId, langCode,account.getId());

			if ("J".equals(account.getAccType())) {
				String jointContactId = investorService.getAccountContactId(accountId, "J");
				List<DocListVO> jointDoc = investorService.findContactDocList(memberId, distributorId, jointContactId, langCode,account.getId());
				mainDoc.addAll(jointDoc);
			}
			model.put("mainDoc", mainDoc);

		}
		return "ifa/space/clientApplicationForm";
	}

	/**
	 * 查看申请表
	 * 
	 * @author wwluo
	 * @date 2016-11-01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/docCheckHistory", method = RequestMethod.POST)
	public void docCheckHistory(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<InvestorDocCheck> investorDocChecks = new ArrayList<InvestorDocCheck>();
		boolean flag = false;
		try {
			String docId = request.getParameter("docId");
			if (StringUtils.isNotBlank(docId)) {
				investorDocChecks = kycDocCheckServic.findDocCheckList(docId);
				/*
				 * if(investorDocChecks != null &&
				 * !investorDocChecks.isEmpty()){ }
				 */
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("flag", flag);
		map.put("investorDocChecks", investorDocChecks);
		JsonUtil.toWriter(map, response);
	}

	/**
	 * Investor首页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/homeOld", method = RequestMethod.GET)
	public String ifaInvestor(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String periodType = "return_period_code_LAUNCH";// 一开始默认为有史以来
		String langCode = getLoginLangFlag(request);
		String currency=request.getParameter("currency");
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			String memberId = loginUser.getId();
			
			if(null==currency || "".equals(currency)){
				currency=loginUser.getDefCurrency();
			}
			if(null==currency || "".equals(currency)){
				currency=CommonConstants.DEF_CURRENCY;
			}
			//
			String dateFormat=loginUser.getDateFormat();
			if(null==dateFormat || "".equals(dateFormat))
				dateFormat=CommonConstants.FORMAT_DATE;
			
			Date endDate=DateUtil.addDate(DateUtil.getCurDate(),Calendar.DATE, 1);
			Date beginDate=DateUtil.addDate(DateUtil.getCurDate(), Calendar.MONTH, -1);
			List<String> dateList=DateUtil.getDateList(beginDate, endDate,dateFormat);
			List<InvestorMyPortfolioVO> myPortfolioList = investorService.findMyAllPortfolio(memberId, langCode,currency);
			endDate=DateUtil.getCurDate();
			String strConditionString=" and r.valuation_date between '"+DateUtil.getDateStr(beginDate)+"' and '"+DateUtil.getDateStr(endDate)+"'";
			if(null!=myPortfolioList){
				Iterator it=myPortfolioList.iterator();
				while (it.hasNext()) {
					Map<Integer,Object> map=new HashMap<Integer, Object>();
					InvestorMyPortfolioVO object = (InvestorMyPortfolioVO) it.next();
					InvestorPortfolioDataVO dataVo=portfolioHoldService.getPortfolioHoldCumperf(object.getPortfolioId(), currency, strConditionString,loginUser.getId(),dateFormat);
					List<PortfolioHoldCumperfSimpleVO> list=dataVo.getMyPortfolioList();
					List<String> dList=dataVo.getDateList();
				
					if(list!=null && !list.isEmpty()){
						for (int i = 0; i < dateList.size(); i++) {
							String date=dateList.get(i);
						//	date=DateUtil.getDateStr(date, CommonConstants.FORMAT_DATE, dateFormat);
							if(dList.contains(date)){
								PortfolioHoldCumperfSimpleVO vo=list.get(dList.indexOf(date));
								map.put(i, vo.getDayPl());
							}else {
								map.put(i, 0);
							}
						}
						/*for (String date : dateList) {
							if(dList.contains(date)){
								PortfolioHoldCumperfSimpleVO vo=list.get(dList.indexOf(date));
								map.put(date, vo.getDayPl());
							}else {
								map.put(date, 0);
							}
						}*/
						
					}
					object.setDataList(JsonUtil.toJson(map));
				}
				
				String defDisplayColor = loginUser.getDefDisplayColor();
				model.put("defDisplayColor", defDisplayColor);
			}
			model.put("myPortfolioList", myPortfolioList);
			model.put("dateList", StringUtils.join(dateList,","));
			// 获取当前登录的投资人的投资账号所属的ifa信息
			List<MemberIfa> ifaList = investorService.findInvestorAccountListByInvestorId(memberId);
			
			// Top Performance Funds
			List<InvestorHomeTopPerformanceFundsVO> topPerformanceFundsList = investorService.findTopPerformanceFunds(langCode,periodType, null);
			if (null != topPerformanceFundsList&&!topPerformanceFundsList.isEmpty()) {
				//if (topPerformanceFundsList.size() > 0) {
					// 第一条大数据
					model.put("topPerformanceFundsOne", topPerformanceFundsList.get(0));
					topPerformanceFundsList.remove(0);
					List<InvestorHomeTopPerformanceFundsVO> topPerformanceFundsOther = topPerformanceFundsList;
					int titleNum = 1;
					if (null != topPerformanceFundsOther) {
						for (int x = 0; x < topPerformanceFundsOther.size(); x++) {
							titleNum++;
							topPerformanceFundsOther.get(x).setTitleNum(titleNum);
						}
					}
					model.put("topPerformanceFundsOther", topPerformanceFundsOther);
				//}
			}
			
			// Top Performance Portfolio
			
			List<InvestorHomeTopPerformancePortfolioVO> topPerformancePortfolio = investorService.findTopPerformancePortfolio(langCode, periodType, null,loginUser.getId());
			model.put("topPerformancePortfolio", topPerformancePortfolio);
			if (null != topPerformancePortfolio&&!topPerformancePortfolio.isEmpty()) {
				//if (topPerformancePortfolio.size() > 0) {
					// 第一条大数据
					model.put("topPerformancePortfolioOne", topPerformancePortfolio.get(0));
					topPerformancePortfolio.remove(0);
					List<InvestorHomeTopPerformancePortfolioVO> topPerformancePortfolioOther = topPerformancePortfolio;
					int titleNum = 1;
					if (null != topPerformancePortfolioOther) {
						for (int x = 0; x < topPerformancePortfolioOther.size(); x++) {
							titleNum++;
							topPerformancePortfolioOther.get(x).setTitleNum(titleNum);
						}
					}
					model.put("topPerformancePortfolioOther", topPerformancePortfolioOther);
				//}
			}
			
			List<InvestorHomeMostSelectedFundsVO> mostSelectedFunds = investorService.getMostSelectedFunds(langCode);
			// 输出组合中所包含最多的3条产品基金
			model.put("mostSelectedFunds", mostSelectedFunds);
			// 输出直播数据
			List<IfaSpaceLiveVO> liveList = ifaSpaceService.getSpaceWillLiveLeftList(memberId, langCode);
			// 输出IFA信息
			model.put("ifaList", ifaList);
			// 设置直播图文首条记录
			if (null != liveList && !liveList.isEmpty()) {
				model.put("topOneLiveTitle", liveList.get(0).getTitle());
				model.put("topOneLiveContent", liveList.get(0).getContent());
				model.put("topOneLiveBeginTime", liveList.get(0).getLiveScheduler().getBeginTime());
				// 如果直播数据超过2条
				if (liveList.size() > 1) {
					List<IfaSpaceLiveVO> notTopWillLiveList = liveList;
					notTopWillLiveList.remove(0);
					model.put("notTopWillLiveList", notTopWillLiveList);
				}
				model.put("notLiveDisplay", "show");
			} else {
				model.put("notLiveDisplay", "none");
			}
			
			/*String check=request.getParameter("check");
			
			//验证账户登录
			if(check==null || check=="0"){
				AccountValidVO validVO=iTraderSendService.saveCheckExistValid(request, loginUser.getId());
				if(null!=validVO){
					model.put("checkList", validVO.getValidAccountNo());
					model.put("accountType", CommonConstantsWeb.WEB_ACCOUNTCHECK_INVESTOR);
				}
				
			}*/
			
			 List<SysParamVO> sysParamList = sysParamService.getParamList(langCode,"currency_type");//currency_type
	         model.put("sysParamList", sysParamList);
	         
	         model.put("currency", currency);
	          
			return "investor/home/investorHome";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}

	}
	

	
	
	
	/**
	 * Investor首页，获取图表数据
	 * modify by mqzou 2017-01-11
	 * 
	 */
	@RequestMapping(value = "/queryMyPortfolioList", method = RequestMethod.POST)
	public void queryMyPortfolioList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = getLoginLangFlag(request);
		String currency = request.getParameter("currency");
		MemberBase loginUser=getLoginUser(request);
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat))
			dateFormat=CommonConstants.FORMAT_DATE;
		
		List<InvestorMyPortfolioVO> myPortfolioList = investorService.findMyAllPortfolio(loginUser.getId(), langCode,currency);
		Date endDate=DateUtil.addDate(DateUtil.getCurDate(),Calendar.DATE, 1);
		Date beginDate=DateUtil.addDate(DateUtil.getCurDate(), Calendar.MONTH, -1);
		List<String> dateList=DateUtil.getDateList(beginDate, endDate,dateFormat);
		endDate=DateUtil.getCurDate();
		String strConditionString=" and r.valuation_date between '"+DateUtil.getDateStr(beginDate)+"' and '"+DateUtil.getDateStr(endDate)+"'";
		if(null!=myPortfolioList){
			Iterator it=myPortfolioList.iterator();
			while (it.hasNext()) {
				Map<Integer,Object> map=new HashMap<Integer, Object>();
				InvestorMyPortfolioVO object = (InvestorMyPortfolioVO) it.next();
				InvestorPortfolioDataVO dataVo=portfolioHoldService.getPortfolioHoldCumperf(object.getPortfolioId(), currency, strConditionString,loginUser.getId(),dateFormat);
				List<PortfolioHoldCumperfSimpleVO> list=dataVo.getMyPortfolioList();
				List<String> dList=dataVo.getDateList();
			
				if(list!=null && !list.isEmpty()){
					for (int i = 0; i < dateList.size(); i++) {
						String date=dateList.get(i);
						if(dList.contains(date)){
							PortfolioHoldCumperfSimpleVO vo=list.get(dList.indexOf(date));
							map.put(i, vo.getDayPl());
						}else {
							map.put(i, 0);
						}
					}
					
				}
				object.setDataList(JsonUtil.toJson(map));
			}
			
		}
		
		HashMap<String, Object> result=new HashMap<String, Object>();
		result.put("portfolioList", myPortfolioList);
		JsonUtil.toWriter(result, response);
	}


	/**
	 * Investor首页，通过下拉PeriodType获取Top Performance Portfolio前5条数据
	 * 
	 */
	@RequestMapping(value = "/queryTopPerformancePortfolioList", method = RequestMethod.POST)
	public String queryTopPerformancePortfolioList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser=getLoginUser(request);
		String langCode = getLoginLangFlag(request);
		String periodCode = request.getParameter("periodCode");
		List<InvestorHomeTopPerformancePortfolioVO> topPerformancePortfolio = investorService.findTopPerformancePortfolio(langCode, periodCode, null,loginUser.getId());
		
		JsonUtil.toWriter(topPerformancePortfolio, response);
		return null;
	}

	/**
	 * Investor首页，通过下拉PeriodType获取Top Performance Funds前5条数据
	 * 
	 */
	@RequestMapping(value = "/queryTopPerformanceFundsList", method = RequestMethod.POST)
	public String queryTopPerformanceFundsList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = getLoginLangFlag(request);
		String periodCode = request.getParameter("periodCode");
		List<InvestorHomeTopPerformanceFundsVO> topPerformanceFundsList = investorService.findTopPerformanceFunds(langCode, periodCode, null);

		JsonUtil.toWriter(topPerformanceFundsList, response);
		return null;
	}

	/**
	 * Accounts Wait for Process （ IFA Firm ）
	 * 
	 * @author wwluo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/accountsWaitforProcess", method = RequestMethod.GET)
	public String accountsWaitforProcess(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//非IFA Firm用户，跳转到首页
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_IFA_FIRM.equals(role)))
			return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
		MemberSsoVO sso = this.getLoginUserSSO(request);
		List<MemberDistributor> distributors = distributorService.findAllDistributor();
		model.put("distributors", distributors);
		model.put("ifaFrimId", sso.getIfafirmId());
		return "investor/approve/accountsWaitforProcess";
	}
	
	/**
	 * ifaFirm审批列表
	 * @author scshi
	 * @date 20170626
	 * **/
	@RequestMapping(value = "/accountsWaitforProcessJson",method=RequestMethod.POST)
	public void accountsWaitforProcessJson(HttpServletRequest request, HttpServletResponse response){
		String lang = this.getLoginLangFlag(request);
		MemberBase loginUser = this.getLoginUser(request);
		String keyWord = request.getParameter("keyWord");
		String firmId = request.getParameter("ifaFirmId");
		String distributorId = request.getParameter("distributorId");
		
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = investorService.getAccountsWaitforProcess(jsonPaging, loginUser, null, null, null, lang);
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * Accounts Wait for Process （ Distributor ）
	 * 
	 * @author wwluo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accountsWaitforProcessAgent", method = RequestMethod.GET)
	public String accountsWaitforProcessAgent(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//非Distributor用户，跳转到首页
		String role = StrUtils.getString(request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE));
		if (!(CoreConstants.FRONT_USER_ROLE_DISTRIBUTOR.equals(role)))
			return "redirect:" + this.getFullPath(request) + "index.do";// 跳转到首页
		
		String lang = this.getLoginLangFlag(request);
		MemberSsoVO sso = this.getLoginUserSSO(request);
		JsonPaging jpaging = new JsonPaging();
		jpaging = ifafirmManageService.getIfaFirmJson(jpaging, null, null, null, lang);
		model.put("ifaFirms", jpaging.getList());
		model.put("distributorId", sso.getDistributorId());
		return "investor/approve/accountsWaitforProcessAgent";
	}
	
	@RequestMapping(value = "/accountsWaitforProcessAgentJson",method=RequestMethod.POST)
	public void accountsWaitforProcessAgentJson(HttpServletRequest request, HttpServletResponse response){
		String lang = this.getLoginLangFlag(request);
		MemberBase loginUser = this.getLoginUser(request);
		String keyWord = request.getParameter("keyWord");
		String firmId = request.getParameter("ifaFirmId");
		String distributorId = request.getParameter("distributorId");
		
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = investorService.getAccountsWaitforProcess(jsonPaging, loginUser, distributorId, firmId, keyWord, lang);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * get Distributor For InvestorAccount
	 * 
	 * @author wwluo
	 * @date 2016-11-01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getAccountsWaitforProcess", method = RequestMethod.POST)
	public void getAccountsWaitforProcess(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String lang = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean flag = false;
		if (loginUser != null) {
			String distributorId = request.getParameter("distributorId");
			String ifaFirmId = request.getParameter("ifaFirmId");
			String keyWord = request.getParameter("keyWord");
			jsonPaging = investorService.getAccountsWaitforProcess(jsonPaging, loginUser, distributorId, ifaFirmId, keyWord, lang);
			flag = true;
		}
		map.put("flag", flag);
		map.put("investorAccounts", jsonPaging);
		JsonUtil.toWriter(map, response);
	}

	/**
	 * 开户邮件 mqzou 2016-12-06
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showMail")
	public String showMail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "investor/open/showMail";
	}

	/**
	 * 开户邮件 mqzou 2016-12-06
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getEmail")
	public void getEmail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = getLoginLangFlag(request);
		// System.out.println(request.getRequestURL());
		String accountId = "40280ad65635b114015635ba6a470006";
		List<String> valList = new ArrayList<String>();
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		if (null != account) {
			MemberIfa ifa = account.getIfa();
			MemberIndividual individual = memberBaseService.findIndividualMember(account.getMember().getId());
			MemberDistributor distributor = account.getDistributor();
			if (null != ifa) {
				// MemberIfafirm ifafirm=ifa.getIfafirm();
				valList.add("http://192.168.138.99:8181/wmes/");
				valList.add("http://192.168.138.99:8181/wmes/");
				valList.add(individual.getLastName() + " " + individual.getFirstName());
				valList.add(distributor.getCompanyName());
				// valList.add(account.getAccountNo());
				valList.add(ifa.getLastName() + " " + ifa.getFirstName());
				valList.add(DateUtil.getCurDateStr());
				valList.add("");
				valList.add("");
				valList.add(distributor.getCompanyName());
				String subject = PropertyUtils.getPropertyValue(langCode, "investor.open.pwd.subject", null);
				String msg = PropertyUtils.getPropertyValue(langCode, "investor.open.pwd.msg", valList.toArray());
				// System.out.println(msg);
				sendEmail("openAccount", ifa.getMember(), account.getMember(), "mqzou@fsll.cn", subject, msg, accountId);
			}
		}

	}
	
	/**
	 * when open the page
	 * 
	 * @author wwlin
	 * @date 2016-11-01
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/whenOpenInvestorPage", method = RequestMethod.POST)
	public void whenOpenInvestorPage(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		//String lang = this.getLoginLangFlag(request);
		Map<String, Object> map = new HashMap<String, Object>();
		List<InvestorAccount> list = new ArrayList<InvestorAccount>();
		Boolean flag = false;
		if (loginUser != null) {
			list = investorService.findInvestorAccountListByMemberId(loginUser.getId());
			
		}
		map.put("list", list);
		map.put("flag", flag);
		JsonUtil.toWriter(map, response);
	}
	
	/**
	 * 根据holdId获取它的所有PortfolioHoldCumperf data
	 * 
	 */
	@RequestMapping(value = "/findPortfolioHoldCumperfListByHoldId", method = RequestMethod.POST)
	public String findPortfolioHoldCumperfListByHoldId(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//String langCode = getLoginLangFlag(request);
		String holdId = request.getParameter("holdId");
		List<PortfolioHoldCumperf> portfolioHoldCumperfList = investorService.findPortfolioHoldCumperfListByHoldId(holdId);

		JsonUtil.toWriter(portfolioHoldCumperfList, response);
		return null;
	}
	
	/**
	 * 检查是否已有主账户（存在状态为非“退回”的账户，即开户中，已完成开户）
	 * @param memberId 投资人id
	 * @return
	 */
	private boolean checkIfHadAcount(String distributorId, String memberId){
		return investorService.checkAccount(distributorId, memberId);
	}
	
	/**
	 * Investor 首页 (new)
	 * @author logaa
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/home")
	//@RequestMapping(value = "/myZoneOne")
	public String investorHome(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR != loginUser.getMemberType()){
			return "redirect:" + this.getFullPath(request) + "index.do";
		}
		String langCode = this.getLoginLangFlag(request);
		InvestorHomeVO vo = new InvestorHomeVO();
		// Common
		String currencyCode = loginUser.getDefCurrency();
		if (StringUtils.isBlank(currencyCode)) {
			currencyCode = CommonConstants.DEF_CURRENCY;
		}
		String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
		String displayColor = loginUser.getDefDisplayColor();
		if (StringUtils.isBlank(displayColor)) {
			displayColor = CommonConstants.DEF_DISPLAY_COLOR;
		}
		vo.setDisplayColor(displayColor);
		vo.setCurrencyCode(currencyCode);
		vo.setCurrencyName(currencyName);
		// My IFA
		List<IfaVO> ifas = investorService.getMyIfas(loginUser.getId(), langCode);
		vo.setIfas(ifas);
		// My Buddy
		List<MemberVO> buddys = webFriendService.getWebFriends(loginUser.getId(), null, langCode , 2);
		vo.setBuddys(buddys);
		// Top Performance Portfolio
		List<InvestorHomeTopPerformancePortfolioVO> topPerformancePortfolios = investorService.findTopPerformancePortfolio(langCode, CommonConstantsWeb.RETURN_PERIOD_CODE_LAUNCH, displayColor,loginUser.getId());
		vo.setTopPerformancePortfolios(topPerformancePortfolios);
		// Top Performance Funds
		List<InvestorHomeTopPerformanceFundsVO> topPerformanceFunds = investorService.findTopPerformanceFunds(langCode, CommonConstants.RETURN_PERIOD_CODE_3M, displayColor);
		vo.setTopPerformanceFunds(topPerformanceFunds);
		// Most Selected Funds
		List<InvestorHomeMostSelectedFundsVO> mostSelectedFunds = investorService.getMostSelectedFunds(langCode);
		vo.setMostSelectedFunds(mostSelectedFunds);
		model.put("vo", vo);
		return "investor/home/investor";
	}
	
	/**
	 * Investor Home Notice
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getInvestorNotice", method = RequestMethod.POST)
	public void getInvestorNotice(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String sizeStr = request.getParameter("minSize");
		if (StringUtils.isBlank(sizeStr)) {
			sizeStr = "5";
		}
		Integer maxResults = Integer.valueOf(sizeStr);
		// 获取系统公告
		List<NoticeVO> sysNotices = noticeService.getMyNotices(loginUser, langCode, maxResults);
		// 获取持仓基金公告
		List<FundAnnoVO> fundNotices = investorService.getHoldFundNotice(loginUser, langCode, maxResults);
		if(fundNotices == null || fundNotices.size() < maxResults){
			if(fundNotices != null){
				maxResults = maxResults - fundNotices.size();
			}
			// 获取 Recommended FundAnnos
			List<FundAnnoVO> fundAnnos = investorService.getRecommendedFundAnnos(loginUser, langCode, maxResults);
			if(fundAnnos != null && !fundAnnos.isEmpty()){
				if(fundNotices != null){
					fundNotices.removeAll(fundAnnos);
				}
				fundNotices.addAll(fundAnnos);
			}
		}
		if(fundNotices == null || fundNotices.size() < maxResults){
			if(fundNotices != null){
				maxResults = maxResults - fundNotices.size();
			}else{
				fundNotices = new ArrayList<FundAnnoVO>();
			}
			// 平台基金公告
			List<FundAnnoVO> fundAnnos = fundAnnoService.getFundAnnos(loginUser, langCode, maxResults);
			if(fundAnnos != null && !fundAnnos.isEmpty()){
				fundNotices.removeAll(fundAnnos);
				fundNotices.addAll(fundAnnos);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sysNotices", sysNotices);
		result.put("fundNotices", fundNotices);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * Recommended Portfolios
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getRecommendedPortfolios", method = RequestMethod.POST)
	public void recommendedPortfolios(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		// 推荐给我的
		List<PortfolioArenaVO> arenas = investorService.getRecommendedPortfolios(loginUser.getId(), null, langCode);
		if(arenas == null){
			arenas = new ArrayList<PortfolioArenaVO>();
		}
		// 浏览过的
		List<PortfolioArenaVO> visitArenas = investorService.getInvestorVisitArena(loginUser.getId(), langCode);
		if(visitArenas != null){
			arenas.removeAll(visitArenas);
			arenas.addAll(visitArenas);
		}
		// 公开的
		List<PortfolioArenaVO> publicArenas = investorService.getRecommendedPortfolios(null, "1", langCode);
		if(publicArenas != null){
			arenas.removeAll(publicArenas);
			arenas.addAll(publicArenas);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("arenas", arenas);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * Recommended Strategy
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getRecommendedStrategies", method = RequestMethod.POST)
	public void getRecommendedStrategies(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		// 推荐给我的
		List<StrategyInfoVO> strategies = investorService.getRecommendedStrategies(loginUser.getId(), null, langCode);
		if(strategies == null){
			strategies = new ArrayList<StrategyInfoVO>();
		}
		// 浏览过的
		List<StrategyInfoVO> visitStrategies = investorService.getInvestorVisitStrategies(loginUser.getId(), langCode);
		if(visitStrategies != null){
			strategies.removeAll(visitStrategies);
			strategies.addAll(visitStrategies);
		}
		// 公开的
		List<StrategyInfoVO> publicStrategies = investorService.getRecommendedStrategies(null, "1" , langCode);
		if(publicStrategies != null){
			strategies.removeAll(publicStrategies);
			strategies.addAll(publicStrategies);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("strategies", strategies);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * Recommended Fund
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getRecommendedFund", method = RequestMethod.POST)
	public void getRecommendedFund(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		// 推荐的
		List<FundInfoDataVO> funds = investorService.getRecommendedFunds(loginUser.getId(), langCode);
		if(funds == null){
			funds = new ArrayList<FundInfoDataVO>();
		}
		// 浏览过的
		List<FundInfoDataVO> visitFunds = investorService.getInvestorVisitFunds(loginUser.getId(), langCode);
		if(visitFunds != null){
			funds.removeAll(visitFunds);
			funds.addAll(visitFunds);	
		}
		// 平台的
		List<FundInfo> fundInfos = fundInfoService.findAllFunds(10);
		if(fundInfos != null && !fundInfos.isEmpty()){
			List<FundInfoDataVO> fundInfoDataVOs = new ArrayList<FundInfoDataVO>();
			for (FundInfo fundInfo : fundInfos) {
				fundInfoDataVOs.add(fundInfoService.findFundFullInfoById(fundInfo.getId(), langCode, loginUser.getId(), null));
			}
			funds.removeAll(fundInfoDataVOs);
			funds.addAll(fundInfoDataVOs);	
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("funds", funds);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * Recommended News
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getRecommendedNews", method = RequestMethod.POST)
	public void getRecommendedNews(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String sizeStr = request.getParameter("minSize");
		if (StringUtils.isBlank(sizeStr)) {
			sizeStr = "2";
		}
		Integer minSize = Integer.valueOf(sizeStr);
		// IFA推荐的新闻
		List<CommunityNewsListVO> news = new ArrayList<CommunityNewsListVO>();
		jsonPaging.setPage(1);
		jsonPaging.setRows(minSize);
		jsonPaging.setOrder("desc");
		jsonPaging.setSort(" t.create_time");
		jsonPaging = communitySpaceService.findIfaRecommendNews(jsonPaging, loginUser.getId(), null, null);
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			news.addAll(jsonPaging.getList());
		}
		// 基金相关的新闻
		// 最新新闻
		if(news.size() < minSize){
			minSize = minSize - news.size();
			List<CommunityNewsListVO> communityNews = investorService.getNews(minSize);
			if(communityNews != null && !communityNews.isEmpty()){
				news.removeAll(communityNews);
				news.addAll(communityNews);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("news", news);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * Recommended insight
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getRecommendedInsights", method = RequestMethod.POST)
	public void getRecommendedInsights(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String sizeStr = request.getParameter("minSize");
		if (StringUtils.isBlank(sizeStr)) {
			sizeStr = "2";
		}
		Integer minSize = Integer.valueOf(sizeStr);
		List<FrontCommunityTopicVO> insights = null;
		// IFA Insight 7天内
		insights = investorService.getRecommendedInsights(loginUser, CommonConstantsWeb.WEB_FRIEND_ADVISOR, "D", 7, loginUser.getId(), langCode, minSize);
		// Buddy Insight 7天内
		if(insights == null || insights.size() < minSize){
			insights = investorService.getRecommendedInsights(loginUser, CommonConstantsWeb.WEB_FRIEND_BUDDY, "D", 7, loginUser.getId(), langCode, minSize);
		}
		// 最新  7天内
		if(insights == null || insights.size() < minSize){
			insights = investorService.getRecommendedInsights(loginUser, null, "D", 7, null, langCode, minSize);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("insights", insights);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * Hot Topic
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getHotTopics", method = RequestMethod.POST)
	public void getHotTopics(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String sizeStr = request.getParameter("minSize");
		String langCode = this.getLoginLangFlag(request);
		if (StringUtils.isBlank(sizeStr)) {
			sizeStr = "2";
		}
		Integer minSize = Integer.valueOf(sizeStr);
		// Hot Topic
		List<FrontCommunityTopicVO> hotTopics = investorService.getHotTopics(loginUser, CommonConstantsWeb.COMMUNITY_RULE_HOT_TOPIC, minSize, langCode);
		if(hotTopics == null || hotTopics.size() < minSize){
			if(hotTopics == null){
				hotTopics = new ArrayList<FrontCommunityTopicVO>();
			}
			minSize = minSize - hotTopics.size();
			List<FrontCommunityTopicVO> topics = investorService.getHotTopics(loginUser, null, minSize, langCode);
			if(topics != null && !topics.isEmpty()){
				hotTopics.removeAll(topics);
				hotTopics.addAll(topics);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("hotTopics", hotTopics);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * Investor Reports
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getInvestorReports", method = RequestMethod.POST)
	public void getInvestorReports(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String sizeStr = request.getParameter("minSize");
		if (StringUtils.isBlank(sizeStr)) {
			sizeStr = "2";
		}
		Integer minSize = Integer.valueOf(sizeStr);
		List<InvestorReport> reports = investorService.getInvestorReports(loginUser.getId(), null, minSize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("reports", reports);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取我的组合资产信息
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getMyPortfolioTotalAssets", method = RequestMethod.POST)
	public void getMyPortfolioTotalAssets(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String currencyCode = request.getParameter("currencyCode");
		String periodType = request.getParameter("periodType");
		String period = request.getParameter("period");
		InvestorMyPortfolios allPortfolioAssets = investorService.getAllPortfolioAssets(loginUser.getId(), currencyCode);
		List<PortfolioAssetsVO> portfolioAssets = investorService.getMyPortfolioTotalAssets(loginUser.getId(), currencyCode, periodType, period);
		allPortfolioAssets.setPortfolioAssets(portfolioAssets);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("allPortfolioAssets", allPortfolioAssets);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * Investor 首页 (简易版)
	 * @author logaa
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/myZoneOne")
	//@RequestMapping(value = "/home")
	public String myZoneOne(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR != loginUser.getMemberType()){
			return "redirect:" + this.getFullPath(request) + "index.do";
		}
		String langCode = this.getLoginLangFlag(request);
		InvestorHomeVO vo = new InvestorHomeVO();
		// Common
		String currencyCode = loginUser.getDefCurrency();
		if (StringUtils.isBlank(currencyCode)) {
			currencyCode = CommonConstants.DEF_CURRENCY;
		}
		String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
		String displayColor = loginUser.getDefDisplayColor();
		if (StringUtils.isBlank(displayColor)) {
			displayColor = CommonConstants.DEF_DISPLAY_COLOR;
		}
		vo.setDisplayColor(displayColor);
		vo.setCurrencyCode(currencyCode);
		vo.setCurrencyName(currencyName);
		// My IFA
		List<IfaVO> ifas = investorService.getMyIfas(loginUser.getId(), langCode);
		vo.setIfas(ifas);
		// My Buddy
		List<MemberVO> buddys = webFriendService.getWebFriends(loginUser.getId(), CommonConstantsWeb.WEB_FRIEND_BUDDY, langCode , 2);
		vo.setBuddys(buddys);
		// Top Performance Portfolio
		List<InvestorHomeTopPerformancePortfolioVO> topPerformancePortfolios = investorService.findTopPerformancePortfolio(langCode, CommonConstantsWeb.RETURN_PERIOD_CODE_LAUNCH, displayColor,loginUser.getId());
		vo.setTopPerformancePortfolios(topPerformancePortfolios);
		// Top Performance Funds
		List<InvestorHomeTopPerformanceFundsVO> topPerformanceFunds = investorService.findTopPerformanceFunds(langCode, CommonConstantsWeb.RETURN_PERIOD_CODE_LAUNCH, displayColor);
		vo.setTopPerformanceFunds(topPerformanceFunds);
		// Most Selected Funds
		List<InvestorHomeMostSelectedFundsVO> mostSelectedFunds = investorService.getMostSelectedFunds(langCode);
		vo.setMostSelectedFunds(mostSelectedFunds);
		model.put("vo", vo);
		return "investor/home/myZoneOne";
	}
	
	/**
	 * 取消收藏
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/canselFavorites", method = RequestMethod.POST)
	public void canselFavorites(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Boolean flag = false;
		String favoritesId = request.getParameter("favoritesId");
		flag = investorService.deleteMyWebFollow(favoritesId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 根据类型获取收藏数据
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getFavoritesByType", method = RequestMethod.POST)
	public void getFavoritesByType(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		Boolean flag = false;
		String type = request.getParameter("type");
		Map<String, Object> result = new HashMap<String, Object>();
		if(CommonConstantsWeb.WEB_FOLLOW_TYPE_PORTFOLIO.equals(type)){
			List<MyFavoritesPortfolios> portfolios = ifaInfoService.loadIFAMyFavoritesPortfoliosList(loginUser.getId(), 1, langCode);
			if(portfolios != null && !portfolios.isEmpty()){
				flag = true;
				result.put("object", portfolios.get(0));
			}
		}else if(CommonConstantsWeb.WEB_FOLLOW_TYPE_STRATEGY.equals(type)){
			List<MyFavoritesStrategy> strategies = ifaInfoService.loadIFAMyFavoritesStrategyList(loginUser.getId(), langCode, loginUser.getDateFormat(), 1, langCode);
			if(strategies != null && !strategies.isEmpty()){
				flag = true;
				result.put("object", strategies.get(0));
			}
		}else if(CommonConstantsWeb.WEB_FOLLOW_TYPE_NEWS.equals(type)){
			List<NewsInfoVO> newsVo = investorService.getMyFavouritesNews(loginUser.getId(), langCode, 1);
			if(newsVo != null && !newsVo.isEmpty()){
				flag = true;
				result.put("object", newsVo.get(0));
			}
		}else if(CommonConstantsWeb.WEB_FOLLOW_TYPE_TOPIC.equals(type)){
			List<TopicDetailVO> topics = investorService.getMyFavouritesTopics(loginUser.getId(), langCode, 1);
			if(topics != null && !topics.isEmpty()){
				flag = true;
				result.put("object", topics.get(0));
			}
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * My favourites
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getMyfavourites", method = RequestMethod.POST)
	public void getMyfavourites(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		String currencyCode = loginUser.getDefCurrency();
		if (StringUtils.isBlank(currencyCode)) {
			currencyCode = CommonConstants.DEF_CURRENCY;
		}
		String dateFormat = loginUser.getDateFormat();
		if(StringUtils.isBlank(dateFormat)){
			dateFormat = CommonConstants.FORMAT_TIME;
		}
		MyFavoritesPortfolios portfolio = null;
		Integer portfolioCount = 0;
		MyFavoritesWatchingTypeVOList watchListType = null;
		Integer watchListTypeCount = 0;
		MyFavoritesStrategy strategy = null;
		Integer strategyCount = 0;
		NewsInfoVO news = null;
		TopicDetailVO topic = null;
		List<MyFavoritesPortfolios> portfolios = ifaInfoService.loadIFAMyFavoritesPortfoliosList(loginUser.getId(), null, langCode);
		if(portfolios != null && !portfolios.isEmpty()){
			portfolio = portfolios.get(0);
			portfolioCount = portfolios.size();
		}
		List<MyFavoritesWatchingTypeVOList> types = ifaInfoService.loadIFAMyFavoritesWatchingList(langCode, loginUser.getId(), currencyCode, null);
		if(types != null && !types.isEmpty()){
			watchListType = types.get(0);
			watchListTypeCount = types.size();
		}
		List<MyFavoritesStrategy> strategies = ifaInfoService.loadIFAMyFavoritesStrategyList(loginUser.getId(), langCode, loginUser.getDateFormat(), null, langCode);
		if(strategies != null && !strategies.isEmpty()){
			strategy = strategies.get(0);
			strategyCount = strategies.size();
		}
		jsonPaging=new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(1);
		jsonPaging.setOrder("desc");
		jsonPaging.setSort(" b.createTime");
		jsonPaging=newsInfoService.findFavouriteNews(jsonPaging, loginUser.getId());
		if(null!=jsonPaging && null!=jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
			NewsInfoForListVO vo=(NewsInfoForListVO)jsonPaging.getList().get(0);
			news=new NewsInfoVO();
			news.setTitle(vo.getTitle());
			news.setPubDateFormat(vo.getLastUpdate());
			news.setId(vo.getId());
			news.setSectionType(vo.getSectionType());
		}
		/*List<NewsInfoVO> newsVo = investorService.getMyFavouritesNews(loginUser.getId(), langCode, 1);
		if(newsVo != null && !newsVo.isEmpty()){
			news = newsVo.get(0);
		}*/
		jsonPaging=new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(1);
		jsonPaging.setSort(" b.create_time");
		jsonPaging.setOrder("desc");
		jsonPaging=communitySpaceService.findIfaFavoriteList(jsonPaging, loginUser.getId(), loginUser.getId(), langCode, dateFormat);
		if(null!=jsonPaging && null!=jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
			FrontCommunityTopicVO vo=(FrontCommunityTopicVO)jsonPaging.getList().get(0);
			topic=new TopicDetailVO();
			topic.setId(vo.getId());
			topic.setTitle(vo.getTitle());
			topic.setSection(vo.getSectionName());
			topic.setCreateDate(vo.getPublishTimeFormat());
		}
		/*List<TopicDetailVO> topics = investorService.getMyFavouritesTopics(loginUser.getId(), langCode, 1);
		if(topics != null && !topics.isEmpty()){
			topic = topics.get(0);
		}*/
		result.put("portfolio", portfolio);
		result.put("portfolioCount", portfolioCount);
		result.put("watchListType", watchListType);
		result.put("watchListTypeCount", watchListTypeCount);
		result.put("strategy", strategy);
		result.put("strategyCount", strategyCount);
		result.put("news", news);
		result.put("topic", topic);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 投资人电子账单列表页面
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statement")
	public String statementlist(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "investor/statement/statement";
	}
	
	/**
	 * 投资人表单下载列表页面
	 * @author wwluo
	 * @date 2017-03-29
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/formsDownload")
	public String formsdownloadlist(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "investor/forms/formsDownload";
	}
	
	/**
	 * 获取 Statement
	 * @author wwluo
	 * @data 2017-03-16
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/getStatement")
	public void getStatement(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		jsonPaging = this.getJsonPaging(request);
		jsonPaging = investorService.getInvestorReports(jsonPaging, loginUser.getId(), null);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 获取指定账号的培训记录
	 * @author wwluo
	 * @data 2017-03-16
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/getTrainings")
	public void getTrainings(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		Boolean flag = false;
		String accountId = request.getParameter("accountId");
		List<InvestorTraining> trainings = null;
		if (StringUtils.isNotBlank(accountId)) {
			InvestorAccount account = investorService.findInvestorAccountById(accountId);
			trainings = investorService.findInvestorTraining(account.getMember().getId());
			flag = true;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("memberType", loginUser.getMemberType());
		result.put("trainings", trainings);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 删除投资帐号
	 * @author mqzou  2017-05-15
	 * 
	 */
	@RequestMapping(value = "/deleteInvestAccount")
	public void deleteInvestAccount(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		HashMap<String, Object> result=new HashMap<String, Object>();
		try {
			String accountId=request.getParameter("id");
		    InvestorAccount account=investorService.findInvestorAccountById(accountId);
		    if(null!=account){
		    	investorService.deleteAccount(account);
		    }
		    result.put("result", true);
		} catch (Exception e) {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}
	
}
