package com.fsll.wmes.crm.action.front;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.recycler.Recycler.C;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import sun.misc.BASE64Decoder;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.MailUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.common.util.UploadUtil;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.service.AccessoryFileService;
import com.fsll.core.service.SysParamService;
import com.fsll.oms.service.ITraderSendService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.crm.vo.ClientsPortfolioVO;
import com.fsll.wmes.crm.vo.CrmSelectVO;
import com.fsll.wmes.crm.vo.ProposalCheckVO;
import com.fsll.wmes.crm.vo.ProposalDataVO;
import com.fsll.wmes.crm.vo.ProposalEmailRecordsVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmMemo;
import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.CrmProposalCheck;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MemberSso;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.OrderPlanCheck;
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.PortfolioInfoAip;
import com.fsll.wmes.entity.PortfolioInfoProduct;
import com.fsll.wmes.entity.PortfolioInfoProductDetail;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.WebEmailLog;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebTaskList;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.investor.service.InvestorManageService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.portfolio.service.PortfolioInfoService;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.StrategyForSelectVO;
import com.fsll.wmes.trade.service.TradeStepService;
import com.fsll.wmes.trade.vo.OrderPlanCheckVO;
import com.fsll.wmes.web.service.WebReadToDoService;
import com.fsll.wmes.web.service.WebTaskListService;
import com.lowagie.text.pdf.BaseFont;
import com.taobao.api.request.OpenimCustmsgPushRequest.CustMsg;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 控制器:投资方案信息管理
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-9-6
 */

@Controller
@RequestMapping("/front/crm/proposal")
public class FrontCrmProposalAct extends WmesBaseAct {
	
	@Autowired
	private CrmProposalService crmProposalService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
    private FundInfoService fundInfoService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private StrategyInfoService strategyInfoService;
	@Autowired
	private PortfolioInfoService portfolioInfoService ;
	@Autowired
	private AccessoryFileService accessoryFileService;
	@Autowired
	private WebReadToDoService webReadToDoService;
	@Autowired
	private PortfolioHoldService portfolioHoldService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private IfaManageService ifaManageService;
	@Autowired
	private InvestorManageService investorManageService;
	@Autowired
	private WebTaskListService webTaskListService;
	@Autowired
	private TradeStepService tradeStepService;
	
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/listForIfa", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "crm/proposal/listForIfa";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJsonForIfa", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
//    	MemberAdmin memberAdmin = (MemberAdmin) request.getSession().getAttribute(CoreConstants.CONSOLE_USER_ADMIN);
    	String langCode = this.getLoginLangFlag(request);
    	MemberBase  curMember = this.getLoginUser(request);    
    	String ifaMemberId = curMember.getId(); 
    	String customerMemberId = request.getParameter("customerMemberId");

        jsonPaging = this.getJsonPaging(request);
        jsonPaging = crmProposalService.findProposalListForIfa(jsonPaging, ifaMemberId, customerMemberId, langCode);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
//    /**
//     * 详细信息
//     */
//    @RequestMapping(value = "/input", method = RequestMethod.GET)
//    public String getProposalDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
//    	String id = request.getParameter("id");
//    	CrmProposal obj = crmProposalService.findProposalById(id);
//    	model.put("proposalVO", obj);
//    	return "crm/proposal/input";
//    } 
	
    /**
	 * 跳转客户的投资方案列表页面(new)
	 * @author wwluo
	 * @date 2016-09-06 
	 */
	@RequestMapping(value = "/clientsProposal", method = RequestMethod.GET)
	public String clientsProposal(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		String currencyCode = loginUser.getDefCurrency();
		if (StringUtils.isBlank(currencyCode)) {
			currencyCode = CommonConstants.DEF_CURRENCY;
		}
		model.put("currency", currencyCode);
		String currencyName = sysParamService.findNameByCode(currencyCode, langCode);
		model.put("currencyName", currencyName);
		String dateFormat = loginUser.getDateFormat();
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = CommonConstants.FORMAT_DATE;
		}
        model.put("dateFormat", dateFormat);
		return "ifa/crm/clientsProposal";
	}
	
	/**
	 * 获取ifa的投资方案
	 * @date 2016-09-14
	 */
	@RequestMapping(value = "/getProposal", method = RequestMethod.POST)
	public void getProposal(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		jsonPaging = this.getJsonPaging(request);
		MemberBase loginUser = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		String period = request.getParameter("period");//期间
		String fromDate = request.getParameter("fromDate");//结束时间
		String toDate = request.getParameter("toDate");//开始时间
		String status = request.getParameter("status");//状态
		String keyWord = request.getParameter("keyWord");//关键字
		String clients = request.getParameter("clients");
		jsonPaging = crmProposalService.getProposal(jsonPaging,loginUser,period,fromDate,toDate,status,keyWord,clients,langCode);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 删除投资方案（逻辑删除）
	 * @author wwluo
	 * @date 2016-09-18
	 */
	@RequestMapping(value = "/delProposal")
	public void delProposal(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String proposalId = request.getParameter("proposalId");
		boolean flag = false;
		CrmProposal crmProposal = crmProposalService.findProposalById(proposalId);
		if(crmProposal != null){
			flag = crmProposalService.delProposal(proposalId);
			List<CrmProposal> proposals = crmProposalService.getProposal(crmProposal.getIfaMember().getId(), crmProposal.getMember().getId());
			CrmCustomer customer = crmCustomerService.findByIfaAndMember(crmProposal.getIfaMember().getId(), crmProposal.getMember().getId());
			if(flag && (proposals == null || proposals.isEmpty()) && customer != null){
				List<CrmMemo> memos = crmCustomerService.findCrmMemo(crmProposal.getIfaMember().getId(), crmProposal.getMember().getId());
				if(memos != null && !memos.isEmpty()){
					customer.setSalesStageId(CommonConstantsWeb.PIPELINE_SALES_CONTACT);
				}else{
					customer.setSalesStageId(CommonConstantsWeb.PIPELINE_SALES_NEW);
				}
				crmCustomerService.saveCustomer(customer);
			}
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("flag", flag);
		JsonUtil.toWriter(obj, response);
	}
	
	/**
	 * 我的投资方案列表（投资人）
	 * @author mqzou   modify by wwluo 20170424
	 * @date 2016-10-27
	 */
	@RequestMapping(value = "/myProposal")
	public String myProposal(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase curMember = getLoginUser(request);
		String langCode = getLoginLangFlag(request);
		String currencyCode = curMember.getDefCurrency();
		if (StringUtils.isBlank(currencyCode)) {
			currencyCode = CommonConstants.DEF_CURRENCY;
		}
		model.put("currencyCode", currencyCode);
		String dateFormat=curMember.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat))
			dateFormat=CommonConstants.FORMAT_DATE_TIME;
		List list=crmProposalService.findMyProposal(curMember.getId(), currencyCode, langCode, dateFormat);
		model.put("proposalList", list);
		return "strategy/temp/myProposalList";
	}
	
       /**
	    *新建Proposal第一步
	    */
	    @RequestMapping(value = "/createProposalSetOne")
		public String createProposalSetOne(HttpServletRequest request, HttpServletResponse response, ModelMap model){
	    	MemberBase loginUser = this.getLoginUser(request);
	    	String langCode = this.getLoginLangFlag(request);
    		String id=request.getParameter("id");
    		String memberId=request.getParameter("memberId");
    		CrmCustomer customer=null;
    		MemberIfa ifa=memberBaseService.findIfaMember(loginUser.getId());
    		if(null!=memberId){
				customer=crmCustomerService.findByIfaAndMember(ifa.getId(), memberId);
				if(null!=customer){
					 model.put("iconUrl", customer.getMember().getIconUrl());
				}
    		}
    		if (StringUtils.isNotBlank(memberId)) {
    			model.put("name", getCommonMemberName(memberId, langCode, "2"));
			}    		
    		/*弃用mjhuang 2017-06-16
        	jsonPaging=new JsonPaging();
        	jsonPaging.setRows(100);
        	jsonPaging.setPage(1);
        	jsonPaging=crmCustomerService.findCustomerByIfa(jsonPaging, ifa);
        	List<CrmSelectVO> list=new ArrayList<CrmSelectVO>();
        	Iterator it=jsonPaging.getList().iterator();
        	while (it.hasNext()){
    			CrmCustomer crm = (CrmCustomer) it.next();
    			CrmSelectVO vo=new CrmSelectVO();
    			vo.setNickName(getCommonMemberName(crm.getMember().getId(), langCode, "2"));
    			if(null!=crm.getIconUrl() && ""!=crm.getIconUrl()){
    				vo.setIconUrl(crm.getIconUrl());
    			}else {
    				if(null!=crm.getMember().getIconUrl() && ""!=crm.getMember().getIconUrl()){
    					vo.setIconUrl(crm.getMember().getIconUrl());
    				}else {
    					vo.setIconUrl("");
    				}
    			}
    			vo.setId(crm.getId());
    			vo.setMemberId(crm.getMember().getId());
    			MemberIndividual individual=memberBaseService.findIndividualMember(crm.getMember().getId());
    			vo.setGender(null!=individual?individual.getGender():"M");
    			list.add(vo);
    		}
        	model.put("crmList", list);
        	*/    		
    		if(null!=id && !"".equals(id)){
	    		CrmProposal proposal=crmProposalService.findProposalById(id);
	    		model.put("proposal", proposal);
	    		if(proposal != null){
	    			String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, proposal.getBaseCurrId(), langCode);
		    		model.put("currencyName", currencyName);
	    		}
	    		customer=crmCustomerService.findByIfaAndMember(ifa.getId(), proposal.getMember().getId());
    			if(null!=customer){
    				 model.put("name", getCommonMemberName(customer.getMember().getId(), langCode, "2"));
    				 model.put("iconUrl", customer.getIconUrl());
    				 MemberIndividual individual=memberBaseService.findIndividualMember(customer.getMember().getId());
    				 if(null!=individual)
    				    model.put("gender", individual.getGender());
    			 }
	    	}else if (null!=customer) {
	    		CrmProposal proposal=new CrmProposal();
	    		proposal.setMember(customer.getMember());
	    		model.put("proposal", proposal);
	    		String currencyCode = null;
	    		if(StringUtils.isNotBlank(loginUser.getDefCurrency())){
	    			currencyCode = loginUser.getDefCurrency();
	    		}else{
	    			currencyCode = CommonConstants.DEF_CURRENCY;
	    		}
	    		proposal.setBaseCurrId(currencyCode);
	    		String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
	    		model.put("currencyName", currencyName);
			}else {
				CrmProposal proposal=new CrmProposal();
	    		model.put("proposal", proposal);
	    		String currencyCode = null;
	    		if(StringUtils.isNotBlank(loginUser.getDefCurrency())){
	    			currencyCode = loginUser.getDefCurrency();
	    		}else{
	    			currencyCode = CommonConstants.DEF_CURRENCY;
	    		}
	    		proposal.setBaseCurrId(currencyCode);
	    		String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
	    		model.put("currencyName", currencyName);
			}
	        List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, this.getLoginLangFlag(request));
	        model.put("currencyList", itemList);
	        model.put("edit", request.getParameter("edit"));
			return "proposal/createProposal/createProposalSetOne";
		}
	    
	    /**
	     *保存Proposal
	     * @author mqzou
	     * @date 2016-09-29
	     */
	     @RequestMapping(value = "/saveProposalSet")
	    public void saveProposal(HttpServletRequest request, HttpServletResponse response, ModelMap model){
	    	MemberBase curMember=getLoginUser(request);	    	
	    	CrmProposal crmProposal=new CrmProposal();
	    	 Map<String, Object> obj = new HashMap<String, Object>();
	    	// CrmProposal crmProposal=null;
			if (null != curMember) {
                String id = request.getParameter("id");
                String memberId=request.getParameter("memberId");
		    	String proposalName = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("proposalName").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String initialInvestAmount = request.getParameter("initialInvestAmount");
		    	String baseCurrId = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("baseCurrId").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String liquidityNeed = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("liquidityNeed").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String timeFrame = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("timeFrame").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String taxConcerns = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("taxConcerns").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String lrf = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("lrf").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	String unp = toUTF8String(StringEscapeUtils.unescapeHtml(request.getParameter("unp").replaceAll("%(?![0-9a-fA-F]{2})", "%25")));
		    	if(null!=id && !"".equals(id)){
		    		crmProposal=crmProposalService.findProposalById(id);
		    	}else {
					crmProposal.setCreateTime(DateUtil.getCurDate());
					crmProposal.setCreator(curMember);
					crmProposal.setStatus("0");
					MemberIfa ifa=memberBaseService.findIfaMember(curMember.getId());
					crmProposal.setIfaMember(ifa);					
				}
		    	crmProposal.setLastUpdate(DateUtil.getCurDate());
		    	
		    	MemberBase member=memberBaseService.findById(memberId);
		    	crmProposal.setMember(member);
		    	crmProposal.setInitialInvestAmount(null!=initialInvestAmount && !"".equals(initialInvestAmount)?Double.valueOf(initialInvestAmount):0);
				crmProposal.setBaseCurrId(baseCurrId);
				crmProposal.setIsValid("1");
				crmProposal.setLiquidityNeed(liquidityNeed);
			    crmProposal.setProposalName(proposalName);
			    crmProposal.setLrf(lrf);
			    crmProposal.setTaxConcerns(taxConcerns);
			    crmProposal.setTimeFrame(timeFrame);
			    crmProposal.setUnp(unp);
			    crmProposal=crmProposalService.saveCrmProposal(crmProposal);
			    
			    //修改客户类别
				CrmCustomer customer = crmCustomerService.findByIfaAndMember(crmProposal.getIfaMember().getId(), crmProposal.getMember().getId());
				if(customer != null){
					customer.setSalesStageId(CommonConstantsWeb.PIPELINE_SALES_PROPOSAL);
				 	crmCustomerService.saveCustomer(customer);
				}
				
				/*String status=proposal.getStatus(); //request.getParameter("status");
				if(null!=proposal.getId() && !"".equals(proposal.getId())){
					CrmProposal crm=crmProposalService.findProposalById(proposal.getId());
					status=crm.getStatus();
					com.fsll.common.util.BeanUtils.copyProperties(proposal, crm);
					if(null!=status && !"".equals(status))
						crm.setStatus(status);
					else {
						crm.setStatus("0");
					}
					crm.setLastUpdate(new Date());
					 crmProposal=crmProposalService.saveCrmProposal(crm);
					// 修改客户类别
					CrmCustomer customer = crmCustomerService.findByIfaAndMember(crmProposal.getIfaMember().getId(), crmProposal.getMember().getId());
					if(customer != null){
						customer.setSalesStageId(CommonConstantsWeb.PIPELINE_SALES_PROPOSAL);
					 	crmCustomerService.saveCustomer(customer);
					}
				}else {
					if("".equals(proposal.getId()))
						proposal.setId(null);
					MemberIfa ifa=memberBaseService.findIfaMember(curMember.getId());
					proposal.setIfaMember(ifa);
					proposal.setCreator(curMember);
					proposal.setCreateTime(new Date());
				//	proposal.setStatus("0");//草稿
					proposal.setIsValid("1");
					if(null!=status && !"".equals(status))
						proposal.setStatus(status);
					else {
						proposal.setStatus("0");
					}
					proposal.setLastUpdate(new Date());
					 crmProposal=crmProposalService.saveCrmProposal(proposal);
					// 修改客户类别
					CrmCustomer customer = crmCustomerService.findByIfaAndMember(proposal.getIfaMember().getId(), proposal.getMember().getId());
					if(customer != null){
						customer.setSalesStageId(CommonConstantsWeb.PIPELINE_SALES_PROPOSAL);
					 	crmCustomerService.saveCustomer(customer);
					}
				}*/
				
				/*if(null!=proposalJson && !"".equals(proposalJson)){
		    		proposalJson=	proposalJson.replace("&quot;", "\"");
		    			JSONObject jsonObject=JSONObject.fromObject(proposalJson);
		    			CrmProposal crmProposal=(CrmProposal)JSONObject.toBean(jsonObject, CrmProposal.class);
		    			try {
							BeanUtils.copyProperties(proposal, crmProposal);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    	}*/
				if(null!=crmProposal){
					obj.put("result", true);
					obj.put("id", crmProposal.getId());
					obj.put("message", "保存成功");
				}else {
					obj.put("result", false);
					obj.put("message", "保存失败");
				}
			} else {
				obj.put("result", false);
				obj.put("message", "保存失败");
			}
			JsonUtil.toWriter(obj, response);
	    }
	     
	     public static String[] getNullPropertyNames (CrmProposal source) {
	        final BeanWrapper src = new BeanWrapperImpl(source);
	         java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
	         Set<String> emptyNames = new HashSet<String>();
	         for(java.beans.PropertyDescriptor pd : pds) {
	             Object srcValue = src.getPropertyValue(pd.getName());
	             if (srcValue == null) emptyNames.add(pd.getName());
	         }
	         String[] result = new String[emptyNames.size()];
	         return emptyNames.toArray(result);
	     }
	     
	     /**
	     * createProposal step2  保存 Proposal信息
	     * @author mqzou
	     * @date 2017-02-16
	     */
	     @RequestMapping(value = "/saveProposalTwo")
	     public void saveProposalTwo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
	    	 MemberBase curMember=getLoginUser(request);
	    	 if(null!=curMember){
	    		 String id=request.getParameter("id");
	    		 String strategyId=request.getParameter("strategyId");
	    		 CrmProposal proposal=crmProposalService.findProposalById(id);
	    		 if(null!=strategyId){
	    			 StrategyInfo strategyInfo=strategyInfoService.findById(strategyId);
	    			 proposal.setStrategy(strategyInfo);
	    		 }
	    		 proposal=crmProposalService.saveCrmProposal(proposal);
	    		 Map<String, Object> obj = new HashMap<String, Object>();
	    		 if(null!=proposal){
						obj.put("result", true);
						obj.put("id", proposal.getId());
						obj.put("message", "保存成功");
					}else {
						obj.put("result", false);
						obj.put("message", "保存失败");
					}
	    		 JsonUtil.toWriter(obj, response);
	    	 }
	     }
	     
	    /**
	     * createProposal step3  保存 Proposal信息
	     * @author wwluo
	     * @date 2016-09-29
	     */
	     @SuppressWarnings("unchecked")
		@RequestMapping(value = "/saveProposal")
	     public void saveProposal(CrmProposal proposal,PortfolioInfoAip portfolioInfoAip,HttpServletRequest request, HttpServletResponse response, ModelMap model){
	    	 MemberBase curMember = getLoginUser(request);
	    	 Map<String, Object> obj = new HashMap<String, Object>();
	    	 boolean flag = false;
	    	 try {
	    		 String proposalId = request.getParameter("proposalId");
	    		 String riskLevelStr = request.getParameter("riskLevel");
	    		 Integer riskLevel = null;
    			 if (StringUtils.isNotBlank(riskLevelStr)) {
    				 riskLevel = Integer.valueOf(riskLevelStr);
				 }
	    		 //保存 Proposal 信息
	    		 if (StringUtils.isNotBlank(proposalId)) {
	    			 //更新部分
	    			 proposal = crmProposalService.findProposalById(proposalId);
	    			 String proposalName = request.getParameter("proposalName");
	    			 String curStep = request.getParameter("hidCurStep");
	    			 proposal.setProposalName(proposalName);
	    			 proposal.setCurStep(curStep);
	    		 }
	    		 proposal.setStatus("0");
	    		 proposal.setLastUpdate(new Date());
	    		 proposal = crmProposalService.saveCrmProposal(proposal);
    			 //保存 PortfolioInfo 信息
	    		 PortfolioInfo info = null;
	    		 info = crmProposalService.findPortfolioInfoByProposal(proposal);
	    		 if ( info == null || StringUtils.isBlank(info.getId())) {
	    			 info = new PortfolioInfo();
	    			 info.setProposal(proposal);
	    			 info.setMember(proposal.getMember());
	    			 info.setMemberIfa(proposal.getIfaMember());
	    			 info.setPortfolioName(proposal.getProposalName());
	    			 info.setAipFlag("0");
	    			 info.setCreator(curMember);
	    			 info.setIsValid("1");
	    			 info.setBaseCurrency(proposal.getBaseCurrId());
	    			 info.setInvestmentGoal(proposal.getInvestmentGoal());
	    			 info.setCreateTime(new Date());
				 }
	    		 info.setRiskLevel(riskLevel);
    			 info.setLastUpdate(new Date());
    			 info = portfolioInfoService.saveOrUpdate(info);
	    		// 修改客户类别
				CrmCustomer customer = crmCustomerService.findByIfaAndMember(proposal.getIfaMember().getId(), proposal.getMember().getId());
				if(customer != null && !"1".equals(customer.getClientType())){
					customer.setSalesStageId(CommonConstantsWeb.PIPELINE_SALES_PROPOSAL);
				 	crmCustomerService.saveCustomer(customer);
				}
				 // 删除该组合之前产品
				 portfolioInfoService.deleteProduct(info.getId());
				 // 删除该组合之前产品detail记录
				 portfolioInfoService.deleteProductDetail(info.getId());
				 String portfolioProduct = this.toUTF8String(request.getParameter("portfolioProduct"));
				 if (StringUtils.isNotBlank(portfolioProduct)) {
					List<Map> products = JsonUtil.toListMap(portfolioProduct);
					for (Map map : products) {
						PortfolioInfoProduct product = new PortfolioInfoProduct();
						String fundId = (String) map.get("fundId");
						List<String> details =  (List) map.get("detail");
						Object weight = (Object) map.get("fundWeight");
						FundInfo fund = fundInfoService.findFundInfoById(fundId);
						product.setPortfolio(info);
						if(fund != null){
							product.setProduct(fund.getProduct());
						}
						if(weight.toString() != null){
							product.setAllocationRate(Double.parseDouble(weight.toString())/100);
						}
						// 保存产品表
						crmProposalService.saveAndUpdatePortfolioInfoProduct(product);
						if(details != null && !details.isEmpty()){
							for (String detail : details) {
								PortfolioInfoProductDetail productDetail = new PortfolioInfoProductDetail();
								productDetail.setIfDef(null);
								productDetail.setPortfolio(info);
								productDetail.setProduct(fund.getProduct());
								FundInfo candidateFund = fundInfoService.findFundInfoById(detail);
								if(candidateFund != null){
									productDetail.setCandidateProduct(candidateFund.getProduct());
								}
								// 保存产品详情表
								crmProposalService.saveAndUpdatePortfolioInfoProductDetail(productDetail);
							}
						}else{
							PortfolioInfoProductDetail productDetail = new PortfolioInfoProductDetail();
							productDetail.setIfDef(null);
							productDetail.setPortfolio(info);
							productDetail.setProduct(fund.getProduct());
							// 保存产品详情表
							crmProposalService.saveAndUpdatePortfolioInfoProductDetail(productDetail);
						}
					}
				 }
				 flag = true;
	    	 } catch (Exception e) {
				 e.printStackTrace();
			 }
	    	 obj.put("flag", flag);
	    	 JsonUtil.toWriter(obj, response);
	    }
	     
	    /**
	     * 创建Proposal第二步
	     * @author mqzou
	     * @date 2016-10-26
	     * @return
	     */
	    @RequestMapping(value = "/createProposalSetTwo")
		public String createProposalSetTwo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
	    	String langCode = this.getLoginLangFlag(request);
	    	MemberBase curMember=getLoginUser(request);
	    	//String name=request.getParameter("name");
	    	//String memberId=request.getParameter("memberId");
	    	String id=request.getParameter("id");
	    	/*if(null!=name && !"".equals(name))
	    	 model.put("name", name);*/
	    	//model.put("memberId", memberId);
	    	List<StrategyForSelectVO> list=strategyInfoService.findStrategyListForSelect(curMember.getId());
	        model.put("strategyList", list);
	    	/*if(null!=proposal && null!=proposalJson && !"".equals(proposalJson)){
	    		proposalJson=	proposalJson.replace("&quot;", "\"");
    			JSONObject obj=JSONObject.fromObject(proposalJson);
    			CrmProposal crmProposal=(CrmProposal)JSONObject.toBean(obj, CrmProposal.class);
    			try {
					BeanUtils.copyProperties(crmProposal, proposal);
					model.put("proposalJson", JsonUtil.toJson(crmProposal));
					model.put("proposal", crmProposal);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
	    	}*/
	        if(null!=id && !"".equals(id)){
	    		CrmProposal proposal=crmProposalService.findProposalById(id);
	    		CrmCustomer customer=crmCustomerService.findByIfaAndMember(proposal.getIfaMember().getId(), proposal.getMember().getId());
	    		model.put("name", getCommonMemberName(proposal.getMember().getId(), langCode, "2"));
	    		model.put("memberId", proposal.getMember().getId());
	    		model.put("proposal", proposal);
	    		model.put("iconUrl", customer.getIconUrl());
				MemberIndividual individual=memberBaseService.findIndividualMember(customer.getMember().getId());
				if(null!=individual)model.put("gender", individual.getGender());
	    	}
	        model.put("edit", request.getParameter("edit"));
			return "proposal/createProposal/createProposalSetTwo";
		}
	    
	    /**
	     * 创建Proposal第三步
	     * @author wwluo
	     * @date 2016-10-26
	     * @return
	     */
	    @RequestMapping(value = "/createProposalSetThree")
		public String createProposalSetThree(CrmProposal proposal,HttpServletRequest request, HttpServletResponse response, ModelMap model){
	    	MemberBase loginUser = getLoginUser(request);
	    	MemberSsoVO ssoVO=getLoginUserSSO(request);
	    	String langCode = this.getLoginLangFlag(request);
	    	String memberId = request.getParameter("memberId");
	    	MemberBase member = memberBaseService.findById(memberId);
	    	String id = request.getParameter("id");
    		CrmCustomer customer=null;
    		if(null!=memberId){
    			 customer=crmCustomerService.findByIfaAndMember(ssoVO.getIfaId(), memberId);
    			 if(null!=customer){
    				 model.put("name", getCommonMemberName(customer.getMember().getId(), langCode, "2"));
    				 if (StringUtils.isNotBlank(customer.getIconUrl())) {
    					 model.put("iconUrl", PageHelper.getUserHeadUrl(customer.getIconUrl(),""));
    				 }else{
    					 model.put("iconUrl", PageHelper.getUserHeadUrl(member.getIconUrl(),""));
    				 }
    			 }
    		}
	    	CrmProposal crmProposal = new CrmProposal();
	    	StrategyInfo strategyInfo = null;
	    	PortfolioInfoAip portfolioInfoAip = new PortfolioInfoAip();
	    	PortfolioInfo portfolioInfo = null;
	    	String currencyCode = null;
	    	if (StringUtils.isNotBlank(id)) {
	    		crmProposal = crmProposalService.findProposalById(id);
	    		if(crmProposal != null){
	    			strategyInfo = crmProposal.getStrategy();
	    			portfolioInfo = crmProposalService.findPortfolioInfoByProposal(crmProposal);
	    			if(portfolioInfo != null){
	    				portfolioInfoAip = crmProposalService.getAipByPorfolio(portfolioInfo);
	    			}
	    			currencyCode = crmProposal.getBaseCurrId();
	    			if (StringUtils.isBlank(currencyCode)) {
	    				currencyCode = loginUser.getDefCurrency();
	    				if (StringUtils.isNotBlank(currencyCode)) {
	    					currencyCode = CommonConstants.DEF_CURRENCY;
						}
					}
	    		}
	    	}
	    	String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
	    	String displayColor = loginUser.getDefDisplayColor();
	    	if (StringUtils.isBlank(loginUser.getDefDisplayColor())) {
	    		displayColor = CommonConstants.DEF_DISPLAY_COLOR;
			}
	    	String fundType = "";
			if (strategyInfo != null) {
				List<StrategyAllocation> strategyAllocations = strategyInfoService.getStrategyAllocation(strategyInfo.getId(), "F");
				if (strategyAllocations != null && !strategyAllocations.isEmpty()) {
					for (StrategyAllocation allocation : strategyAllocations) {
						if (StringUtils.isNotBlank(allocation.getItemCode())) {
							fundType += allocation.getItemCode() + ",";
						}
					}
					if(fundType != ""){
						fundType = fundType.substring(0, fundType.length() - 1);
					}
				}
			}
	    	model.put("displayColor", displayColor);
	    	model.put("currencyCode", currencyCode);
	    	model.put("currencyName", currencyName);
	    	model.put("crmProposal", crmProposal);
	    	model.put("portfolioInfo", portfolioInfo);
	    	model.put("strategyInfo", strategyInfo);
	    	model.put("aip", portfolioInfoAip);
	    	model.put("client", member);
	    	model.put("fundType", fundType);
	    	model.put("edit", request.getParameter("edit"));
	    	return "proposal/createProposal/createProposalSetThree";
		}
	    
	    /**
	     * 创建Proposal第四步
	     * @author wwluo
	     * @date 2016-11-29
	     * @return
	     */
	    @RequestMapping(value = "/createProposalSetFour", method = RequestMethod.GET)
		public String createProposalSetFour(CrmProposal proposal,PortfolioInfoAip portfolioInfoAip,HttpServletRequest request, HttpServletResponse response, ModelMap model){
	    	String id = request.getParameter("id");
	    	String memberId = request.getParameter("memberId");
	    	String langCode = this.getLoginLangFlag(request);
	    	MemberSsoVO ssoVO=getLoginUserSSO(request);
	    	CrmProposal crmProposal = new CrmProposal();
	    	if (StringUtils.isNotBlank(id)) {
	    		crmProposal = crmProposalService.findProposalById(id);
	    	}
	    	MemberBase member = memberBaseService.findById(memberId);
    		CrmCustomer customer=null;
    		if(null!=memberId){
    			 customer=crmCustomerService.findByIfaAndMember(ssoVO.getIfaId(), memberId);
    			 if(null!=customer){
    				 model.put("name", getCommonMemberName(customer.getMember().getId(), langCode, "2"));
    				 if (StringUtils.isNotBlank(customer.getIconUrl())) {
    					 model.put("iconUrl", PageHelper.getUserHeadUrl(customer.getIconUrl(),""));
    				 }else{
    					 model.put("iconUrl", PageHelper.getUserHeadUrl(member.getIconUrl(),""));
    				 }
    			 }
    		}
	    	model.put("memberId", memberId);
	    	model.put("crmProposal", crmProposal);
	    	model.put("edit", request.getParameter("edit"));
	    	return "proposal/createProposal/createProposalSetFour";
		}
	    
	/**
	 *  投资方案详情
	 * @author qgfeng
	 * @date 2016-9-29
	 */
	@RequestMapping(value = "/viewProposal",method = RequestMethod.GET)
	public String viewProposal(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String proposalId = request.getParameter("proposalId");
		CrmProposal proposal = crmProposalService.findProposalById(proposalId);
		model.put("proposal", proposal);
		model.put("loginUser",loginUser);
		return "ifa/crm/view_proposal";
	}
	
	/**
	 * 跳转客户的投资组合列表页面(new)
	 * @author wwluo
	 * @date 2016-09-06 
	 */
	@RequestMapping(value = "/clientsPortfolio", method = RequestMethod.GET)
	public String clientsPortfolio(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		this.saveLastVisitUrl(request, response);
		MemberBase loginUser = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		String currency=loginUser.getDefCurrency();
		if(null==currency || "".equals(currency)){
		     currency=CommonConstants.DEF_CURRENCY;	
		}
		model.put("currency", currency);
		String currencyName=sysParamService.findNameByCode(currency, langCode);
		model.put("currencyName", currencyName);
		
		String displayColor=loginUser.getDefDisplayColor();
		if(null==displayColor || "".equals(displayColor)){
			displayColor=CommonConstants.DEF_DISPLAY_COLOR;
		}
		model.put("displayColor", displayColor);
		String dateFormat=loginUser.getDateFormat();
	    if(null==dateFormat || "".equals(dateFormat))
	    	dateFormat=CommonConstants.FORMAT_DATE;
	     model.put("dateFormat", dateFormat);
	   //金额保留小数位数
			int decimals=2;
			if("JPY".equals(currency)){
				decimals=0;
			}
			model.put("decimals", decimals);
		return "ifa/crm/clientsPortfolio";
	}
	
	/**
	 * 获取我的客户的投资组合
	 * @author wwluo
	 * @date 2016-09-14
	 */
	@RequestMapping(value = "/getPortfolio", method = RequestMethod.POST)
	public void getPortfolio(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		jsonPaging = this.getJsonPaging(request);
		String currency=loginUser.getDefCurrency();
		if(null==currency || "".equals(currency))
			currency=CommonConstants.DEF_CURRENCY;
		String period = request.getParameter("period");//期间
		String fromDate = request.getParameter("fromDate");//结束时间
		String toDate = request.getParameter("toDate");//开始时间
		String riskLevel = request.getParameter("riskLevel");//等级
		String totalReturn = request.getParameter("totalReturn");//回报率
		String keyWord = request.getParameter("keyWord");//关键字
		String statusStr=request.getParameter("status");
		jsonPaging = portfolioInfoService.getPortfolio(jsonPaging,loginUser,period,fromDate,toDate,riskLevel,totalReturn,keyWord,statusStr,currency,getLoginLangFlag(request));
		
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * PROPOSAL 导出 PDF
	 * @author wwluo
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/proposalExportToPDF")
	public void proposalExportToPDF(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		try {
			String proposalId = request.getParameter("proposalId");
			MemberBase loginUser = this.getLoginUser(request);
			if (StringUtils.isNotBlank(proposalId)) {
				savePDF(request,proposalId, loginUser, CommonConstantsWeb.CREATE_PROPOSAL_PDF_SC,CommonConstants.LANG_CODE_SC);
				savePDF(request,proposalId, loginUser, CommonConstantsWeb.CREATE_PROPOSAL_PDF_TC,CommonConstants.LANG_CODE_TC);
				savePDF(request,proposalId, loginUser, CommonConstantsWeb.CREATE_PROPOSAL_PDF_EN,CommonConstants.LANG_CODE_EN);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存pdf信息
	 * @author wwluo
	 * @date 2016-11-30
	 */
	private void savePDF(HttpServletRequest request,String proposalId, MemberBase loginUser,String fileName,String langCode) {
		File tempFile = null;
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort() + request.getContextPath();
		String ctxPath = request.getSession().getServletContext().getRealPath("/");
		ProposalDataVO proposalDataVO = crmProposalService.getProposalDataVO(proposalId,langCode);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("proposal", proposalDataVO.getProposal());
		map.put("portfolio", proposalDataVO.getPortfolio());
		map.put("aip", proposalDataVO.getAip());
		map.put("products", proposalDataVO.getProducts());
		map.put("langCode", langCode);
		if(proposalDataVO.getProposal() != null){
			map.put("member", proposalDataVO.getProposal().getMember());
			map.put("ifaMember", proposalDataVO.getProposal().getIfaMember());
			MemberIndividual individual = memberBaseService.findIndividualMember(proposalDataVO.getProposal().getMember().getId());
			if(individual != null){
				map.put("clientName", this.getIndividualName(individual, langCode));
			}
			map.put("ifaName", this.getIfaName(proposalDataVO.getProposal().getIfaMember(), langCode));
			map.put("iconUrl", PageHelper.getUserHeadUrl(proposalDataVO.getProposal().getMember().getIconUrl(),""));
			String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, proposalDataVO.getProposal().getBaseCurrId(), langCode);
			map.put("currencyName", currencyName);
			
			String ifaIconUrl = proposalDataVO.getProposal().getIfaMember().getMember().getIconUrl();
			// 在/u目录下
			if (StringUtils.isNotBlank(ifaIconUrl) && !ifaIconUrl.startsWith("/res")) {
				File reFile = new File(ifaIconUrl);
				String defUrl = "res/images/temp";
				File dirFile = new File(ctxPath + defUrl);
				if(!dirFile.exists()){
					dirFile.mkdirs();
				}
				String name = ifaIconUrl.substring(ifaIconUrl.lastIndexOf("/")+1, ifaIconUrl.length());
				tempFile = new File(dirFile,  name);
				// 创建临时文件
				UploadUtil.createsTemporaryFiles(reFile, tempFile);
				ifaIconUrl = "/" + defUrl + "/" + name;
			}else{
				ifaIconUrl = PageHelper.getUserHeadUrl(proposalDataVO.getProposal().getIfaMember().getMember().getIconUrl(),"");
			}
			map.put("ifaIconUrl", ifaIconUrl);
		}
		String aggregateUrl = null;
		String separatenesUrl = null;
		String geoAllocationUrl = null;
		String sectorTypeUrl = null;
		String riskAnalysisUrl = null;
		List<AccessoryFile> aggregateAccessory = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_AGGREGATE, null);
		List<AccessoryFile> separatenesUrlAccessory = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SEPARATENES, null);
		List<AccessoryFile> geoAllocationAccessory = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_GEOALLCATION, null);
		List<AccessoryFile> sectorTypeAccessory = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SECTORTYPE, null);
		List<AccessoryFile> riskAnalysisAccessory = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_RISKANALYSIS, null);
		if(aggregateAccessory != null && !aggregateAccessory.isEmpty()){
			aggregateUrl = aggregateAccessory.get(0).getFilePath();
		}
		if(separatenesUrlAccessory != null && !separatenesUrlAccessory.isEmpty()){
			separatenesUrl = separatenesUrlAccessory.get(0).getFilePath();
		}
		if(geoAllocationAccessory != null && !geoAllocationAccessory.isEmpty()){
			geoAllocationUrl = geoAllocationAccessory.get(0).getFilePath();
		}
		if(sectorTypeAccessory != null && !sectorTypeAccessory.isEmpty()){
			sectorTypeUrl = sectorTypeAccessory.get(0).getFilePath();
		}
		if(riskAnalysisAccessory != null && !riskAnalysisAccessory.isEmpty()){
			riskAnalysisUrl = riskAnalysisAccessory.get(0).getFilePath();
		}
		aggregateUrl = getUrl(request, aggregateUrl);
		separatenesUrl = getUrl(request, separatenesUrl);
		geoAllocationUrl = getUrl(request, geoAllocationUrl);
		sectorTypeUrl = getUrl(request, sectorTypeUrl);
		riskAnalysisUrl = getUrl(request, riskAnalysisUrl);
		map.put("aggregateUrl", aggregateUrl);
		map.put("separatenesUrl", separatenesUrl);
		map.put("geoAllocationUrl", geoAllocationUrl);
		map.put("sectorTypeUrl", sectorTypeUrl);
		map.put("riskAnalysisUrl", riskAnalysisUrl);
		map.put("basePath", basePath);
		String filePath = null;
		//删除记录
		if (StringUtils.isNotBlank(fileName)) {
			List<AccessoryFile> accessoryFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, fileName, langCode);
			if(accessoryFile != null && !accessoryFile.isEmpty()){
				filePath = accessoryFile.get(0).getFilePath();
				if(filePath != null){
					UploadUtil.delFolder(filePath);
				}
			}
			accessoryFileService.delAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, fileName, langCode);
		}
		filePath = UploadUtil.getFileName("pdf",true,"createProposal");
		exportToPDF(request,map,filePath, "/WEB-INF/web/ifa/crm/createProposal/template/pdf","proposalPdf"+langCode.toUpperCase()+".html");
		// 删除临时文件
		if(tempFile != null && tempFile.exists()){
			tempFile.delete();
		}
		File aggregateFile = new File(ctxPath + aggregateUrl);
		if(aggregateFile.exists()){
			aggregateFile.delete();
		}
		File geoAllocationFile = new File(ctxPath + geoAllocationUrl);
		if(geoAllocationFile.exists()){
			geoAllocationFile.delete();
		}
		File sectorTypeFile = new File(ctxPath + sectorTypeUrl);
		if(sectorTypeFile.exists()){
			sectorTypeFile.delete();
		}
		File riskAnalysisFile = new File(ctxPath + riskAnalysisUrl);
		if(riskAnalysisFile.exists()){
			riskAnalysisFile.delete();
		}
		AccessoryFile accessoryFile = new AccessoryFile();
		accessoryFile.setCreateBy(loginUser);
		accessoryFile.setCreateTime(new Date());
		accessoryFile.setFileName(fileName);
		accessoryFile.setFilePath(filePath);
		accessoryFile.setLangCode(langCode);
		accessoryFile.setFileType("PDF");
		accessoryFile.setModuleType(CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL);
		accessoryFile.setRelateId(proposalId);
		accessoryFileService.saveAccessoryFile(accessoryFile);
	}

	/**
	 * 获取图片url
	 * @author wwluo
	 * @date 2016-11-30
	 */
	private String getUrl(HttpServletRequest request, String url) {
		String fileUrl = null;
		String ctxPath = request.getSession().getServletContext().getRealPath("/");
		if (StringUtils.isNotBlank(url)) {
			File reFile = new File(url);
			String defUrl = "res/images/temp";
			File dirFile = new File(ctxPath + defUrl);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			String name = url.substring(url.lastIndexOf("/")+1, url.length());
			File tempFile = new File(dirFile,  name);
			// 创建临时文件
			UploadUtil.createsTemporaryFiles(reFile, tempFile);
			fileUrl = "/" + defUrl + "/" + name;
		}
		return fileUrl;
	}

	/**
	 * 生成pdf
	 * @author wwluo
	 * @date 2016-11-30
	 */
	private void exportToPDF(HttpServletRequest request,Map<String, Object> map,String filePath,String templatePath,String templateName){
		try {
			File localFile = new File(filePath);
			if(!localFile.getParentFile().exists()){
				localFile.getParentFile().mkdirs();
			}
			Configuration configuration = new Configuration();
			configuration.setServletContextForTemplateLoading( ContextLoader.getCurrentWebApplicationContext().getServletContext() , templatePath);
			Template template = configuration.getTemplate(templateName,"UTF-8");
			StringWriter stringWriter = new StringWriter();
			BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
			template.setEncoding("UTF-8");
			template.setOutputEncoding("UTF-8");
			template.process(map, bufferedWriter);
			String htmlBody = stringWriter.toString();
			bufferedWriter.flush();
			bufferedWriter.close();
			OutputStream os = new FileOutputStream(filePath);  
			ITextRenderer renderer = new ITextRenderer();  
			ITextFontResolver fontResolver = renderer.getFontResolver();
			String ctxPath = request.getSession().getServletContext().getRealPath("/");
			//宋体
			fontResolver.addFont(ctxPath+"res/fonts/simsunb.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			//微软雅黑
			fontResolver.addFont(ctxPath+"res/fonts/msyh.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			StringBuffer html = new StringBuffer();  
			// DOCTYPE 必需写否则类似于 这样的字符解析会出现错误  
			html.append("<!DOCTYPE html>")
				.append("<html lang=\"en\"><head>")  
			    .append("<meta charset=\"utf-8\"></meta>")  
			    .append("<style type=\"text/css\" mce_bogus=\"1\">body {font-family:Microsoft YaHei,SimSunb;font-size: 16px;}</style>" )
			    .append("</head>")  
			    .append("<body>")  
				.append( htmlBody ) 
				.append("</body></html>"); 
			renderer.setDocumentFromString(html.toString());  
			renderer.layout();
			renderer.createPDF(os);
			os.flush();
			os.close();
			//切割文件
			/*String suffix = filePath.substring(filePath.indexOf(".")+1);
			String tmpPath = filePath.substring(0, filePath.length()-suffix.length()-1);
			File tmpFile = new File(tmpPath);
			tmpFile.mkdirs();
			FileCutUnionUtil.cutFile(localFile,tmpFile);//调用分割方法
			localFile.delete();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存ECherts 图片
	 * @author wwluo
	 * @throws IOException 
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/saveEChartsImage")
	public void saveEChartsImage(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws IOException {
		MemberBase loginUser = this.getLoginUser(request);
		String proposalId = request.getParameter("proposalId");
		String aggregate = request.getParameter("aggregatePng");
		String separatenes = request.getParameter("separatenesPng");
		String allocation = request.getParameter("allocationPng");
		String allocationTwo = request.getParameter("allocationTwoPng");
		String riskAnalysis = request.getParameter("riskAnalysisPng");
		if (StringUtils.isNotBlank(aggregate)) {
			String filePath = null;
			// 删除旧数据
			List<AccessoryFile> accessoryFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_AGGREGATE, null);
			if(accessoryFile != null && !accessoryFile.isEmpty()){
				filePath = accessoryFile.get(0).getFilePath();
				accessoryFileService.delAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_AGGREGATE, null);
				if(filePath != null){
					UploadUtil.delFolder(filePath);
				}
			}
			saveImgInfo(loginUser, proposalId, aggregate,CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_AGGREGATE,"createProposal",CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL);
		}
		if (StringUtils.isNotBlank(separatenes)) {
			String filePath = null;
			// 删除旧数据
			List<AccessoryFile> accessoryFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SEPARATENES, null);
			if(accessoryFile != null && !accessoryFile.isEmpty()){
				filePath = accessoryFile.get(0).getFilePath();
				accessoryFileService.delAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SEPARATENES, null);
				if(filePath != null){
					UploadUtil.delFolder(filePath);
				}
			}
			saveImgInfo(loginUser, proposalId, separatenes,CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SEPARATENES,"createProposal",CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL);
		}
		if (StringUtils.isNotBlank(allocation)) {
			String filePath = null;
			// 删除旧数据
			List<AccessoryFile> accessoryFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_GEOALLCATION, null);
			if(accessoryFile != null && !accessoryFile.isEmpty()){
				filePath = accessoryFile.get(0).getFilePath();
				accessoryFileService.delAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_GEOALLCATION, null);
				if(filePath != null){
					UploadUtil.delFolder(filePath);
				}
			}
			saveImgInfo(loginUser, proposalId, allocation,CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_GEOALLCATION,"createProposal",CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL);
		}
		if (StringUtils.isNotBlank(allocationTwo)) {
			String filePath = null;
			// 删除旧数据
			List<AccessoryFile> accessoryFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SECTORTYPE, null);
			if(accessoryFile != null && !accessoryFile.isEmpty()){
				filePath = accessoryFile.get(0).getFilePath();
				accessoryFileService.delAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SECTORTYPE, null);
				if(filePath != null){
					UploadUtil.delFolder(filePath);
				}
			}
			saveImgInfo(loginUser, proposalId, allocationTwo,CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SECTORTYPE,"createProposal",CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL);
		}
		if (StringUtils.isNotBlank(riskAnalysis)) {
			String filePath = null;
			// 删除旧数据
			List<AccessoryFile> accessoryFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_RISKANALYSIS, null);
			if(accessoryFile != null && !accessoryFile.isEmpty()){
				filePath = accessoryFile.get(0).getFilePath();
				accessoryFileService.delAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_RISKANALYSIS, null);
				if(filePath != null){
					UploadUtil.delFolder(filePath);
				}
			}
			saveImgInfo(loginUser, proposalId, riskAnalysis,CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_RISKANALYSIS,"createProposal",CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL);
		}
	}

	/**
	 * 保存ECherts图片信息
	 * @author wwluo
	 * @date 2016-11-30
	 */
	private void saveImgInfo(MemberBase loginUser, String proposalId,
			String aggregate,String fileName,String uploadModule,String accessoryFileType){
		try {
			byte[] aggregateByte = new BASE64Decoder().decodeBuffer(aggregate);
			String filePath = UploadUtil.getFileName("png", true, uploadModule);
			uploadByByte(aggregateByte,filePath);
			AccessoryFile accessoryFile = new AccessoryFile();
			accessoryFile.setCreateBy(loginUser);
			accessoryFile.setCreateTime(new Date());
			accessoryFile.setFileName(fileName);
			accessoryFile.setFilePath(filePath);
			accessoryFile.setFileType("PNG");
			accessoryFile.setModuleType(accessoryFileType);
			accessoryFile.setRelateId(proposalId);
			accessoryFileService.saveAccessoryFile(accessoryFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传图片
	 * @author wwluo
	 * @throws IOException 
	 * @date 2016-11-30
	 */
	private void uploadByByte(byte[] data,String filePath) {
		try {
		    File localFile = new File(filePath);
		    if(!localFile.getParentFile().exists()){
				localFile.getParentFile().mkdirs();
			}
		    OutputStream out = new FileOutputStream(localFile);  
		    out.write(data);  
		    out.flush();  
		    out.close(); 
		    //切割文件
			/*String suffix = filePath.substring(filePath.indexOf(".")+1);
			String tmpPath = filePath.substring(0, filePath.length()-suffix.length()-1);
			File tmpFile = new File(tmpPath);
			tmpFile.mkdirs();
			FileCutUnionUtil.cutFile(localFile,tmpFile);//调用分割方法
			localFile.delete();*/
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 方案打印预览页面
	 * @author wwluo
	 * @date 2016-12-01 
	 */
	@RequestMapping(value = "/proposalPreview")
	public String preview(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		MemberAdmin admin = getLoginMemberAdmin(request);
		String ifafirmId = null;
		if(admin != null && admin.getIfafirm() != null){
			ifafirmId = admin.getIfafirm().getId();
		}
		String proposalId = request.getParameter("id");
		String langCode =  request.getParameter("language");
		String isPrint =  request.getParameter("isPrint");
		if (StringUtils.isBlank(langCode)) {
			langCode = this.getLoginLangFlag(request);
		}
		if (StringUtils.isNotBlank(proposalId)) {
			CrmProposal proposal = crmProposalService.findProposalById(proposalId);
			String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, proposal.getBaseCurrId(), langCode);
			model.put("currencyName", currencyName);
			boolean isSupervisor = false;
			if(proposal != null && proposal.getIfaMember() != null){
				isSupervisor = ifaManageService.varifySupervisor(loginUser.getId(),proposal.getIfaMember().getMember().getId());
			}
			if(proposal != null 
					&& (loginUser.getId().equals(proposal.getMember().getId()) 
							|| loginUser.getId().equals(proposal.getIfaMember().getMember().getId())
							|| isSupervisor 
							|| proposal.getIfaMember().getIfafirm().getId().equals(ifafirmId))){
				ProposalDataVO proposalDataVO = crmProposalService.getProposalDataVO(proposalId,langCode);
				model.put("proposal", proposalDataVO.getProposal());
				model.put("portfolio", proposalDataVO.getPortfolio());
			    model.put("aip", proposalDataVO.getAip());
				model.put("products", proposalDataVO.getProducts());
				if(proposalDataVO.getProposal() != null){
					model.put("clientName", getCommonMemberName(proposalDataVO.getProposal().getMember().getId(), langCode, "2"));
					model.put("member", proposalDataVO.getProposal().getMember());
					model.put("ifaMember", proposalDataVO.getProposal().getIfaMember());
					model.put("ifaName", getCommonMemberName(proposalDataVO.getProposal().getIfaMember().getMember().getId(), langCode, "2"));
					model.put("iconUrl", PageHelper.getUserHeadUrl(proposalDataVO.getProposal().getMember().getIconUrl(),""));
				}
				String aggregateUrl = null;
				String separatenesUrl = null;
				String geoAllocationUrl = null;
				String sectorTypeUrl = null;
				List<AccessoryFile> aggregateFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_AGGREGATE, null);
				List<AccessoryFile> separatenesFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SEPARATENES, null);
				List<AccessoryFile> geoAllocationFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_GEOALLCATION, null);
				List<AccessoryFile> sectorTypeFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SECTORTYPE, null);
				if(aggregateFile != null && !aggregateFile.isEmpty()){
					aggregateUrl = aggregateFile.get(0).getFilePath();
				}
				if(separatenesFile != null && !separatenesFile.isEmpty()){
					separatenesUrl = separatenesFile.get(0).getFilePath();
				}
				if(geoAllocationFile != null && !geoAllocationFile.isEmpty()){
					geoAllocationUrl = geoAllocationFile.get(0).getFilePath();
				}
				if(sectorTypeFile != null && !sectorTypeFile.isEmpty()){
					sectorTypeUrl = sectorTypeFile.get(0).getFilePath();
				}
				// 审核信息
				List<ProposalCheckVO> checkVOs = crmProposalService.geProposalCheck(proposal.getId(), langCode);
				model.put("checkVOs", checkVOs);
				model.put("isPrint", isPrint);
				model.put("aggregateUrl", aggregateUrl);
				model.put("separatenesUrl", separatenesUrl);
				model.put("geoAllocationUrl", geoAllocationUrl);
				model.put("sectorTypeUrl", sectorTypeUrl);
				model.put("langCode", langCode);
			}
		}
		return "ifa/crm/createProposal/template/print/index";
	}
	
	/**
	 * 方案打印预览页面(APP)
	 * @author wwluo
	 * @date 2016-12-01 
	 */
	@RequestMapping(value = "/proposalPreviewApp")
	public String proposalPreviewApp(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String memberId = request.getParameter("memberId");
		if (StringUtils.isNotBlank(memberId)) {
			MemberBase loginUser = memberBaseService.findById(memberId);
			//MemberSso memberSso = memberBaseService.getMemberSsoByLoginCode(loginUser.getLoginCode());
			//if(memberSso != null && "ios,android".indexOf(memberSso.getFromType()) > -1){
				String proposalId = request.getParameter("id");
				String langCode =  request.getParameter("language");
				if (StringUtils.isNotBlank(proposalId)) {
					CrmProposal proposal = crmProposalService.findProposalById(proposalId);
					if(proposal != null && (loginUser.getId().equals(proposal.getMember().getId()) 
							|| loginUser.getId().equals(proposal.getIfaMember().getMember().getId()))){
						String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, proposal.getBaseCurrId(), langCode);
						model.put("currencyName", currencyName);
						ProposalDataVO proposalDataVO = crmProposalService.getProposalDataVO(proposalId,langCode);
						model.put("proposal", proposalDataVO.getProposal());
						model.put("portfolio", proposalDataVO.getPortfolio());
					    model.put("aip", proposalDataVO.getAip());
						model.put("products", proposalDataVO.getProducts());
						if(proposalDataVO.getProposal() != null){
							model.put("clientName", getCommonMemberName(proposalDataVO.getProposal().getMember().getId(), langCode, "2"));
							model.put("member", proposalDataVO.getProposal().getMember());
							model.put("ifaMember", proposalDataVO.getProposal().getIfaMember());
							model.put("ifaName", getCommonMemberName(proposalDataVO.getProposal().getIfaMember().getMember().getId(), langCode, "2"));
							String iconUrl = proposalDataVO.getProposal().getMember().getIconUrl();
							if (StringUtils.isNotBlank(iconUrl) && iconUrl.startsWith("/u")) {
								iconUrl = "/loadImgSrcByPathNoLogin.do?filePath=" + iconUrl;
							}else{
								iconUrl = PageHelper.getUserHeadUrl(iconUrl, null);
							}
							model.put("iconUrl", iconUrl);
						}
						String aggregateUrl = null;
						String separatenesUrl = null;
						String geoAllocationUrl = null;
						String sectorTypeUrl = null;
						List<AccessoryFile> aggregateFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_AGGREGATE, null);
						List<AccessoryFile> separatenesFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SEPARATENES, null);
						List<AccessoryFile> geoAllocationFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_GEOALLCATION, null);
						List<AccessoryFile> sectorTypeFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, CommonConstantsWeb.CREATE_PROPOSAL_IMAGE_SECTORTYPE, null);
						if(aggregateFile != null && !aggregateFile.isEmpty()){
							aggregateUrl = aggregateFile.get(0).getFilePath();
						}
						if(separatenesFile != null && !separatenesFile.isEmpty()){
							separatenesUrl = separatenesFile.get(0).getFilePath();
						}
						if(geoAllocationFile != null && !geoAllocationFile.isEmpty()){
							geoAllocationUrl = geoAllocationFile.get(0).getFilePath();
						}
						if(sectorTypeFile != null && !sectorTypeFile.isEmpty()){
							sectorTypeUrl = sectorTypeFile.get(0).getFilePath();
						}
						// 审核信息
						List<ProposalCheckVO> checkVOs = crmProposalService.geProposalCheck(proposal.getId(), langCode);
						model.put("checkVOs", checkVOs);
						model.put("aggregateUrl", aggregateUrl);
						model.put("separatenesUrl", separatenesUrl);
						model.put("geoAllocationUrl", geoAllocationUrl);
						model.put("sectorTypeUrl", sectorTypeUrl);
						model.put("langCode", langCode);
					}
				//}
			}
		}
		return "ifa/crm/createProposal/template/print/index";
	}
	
	/**
	 * 预存PDF
	 * @author wwluo
	 * @throws IOException 
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/preStoredPdf")
	public void preStoredPdf(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws IOException {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		String proposalId = request.getParameter("proposalId");
		if (StringUtils.isNotBlank(proposalId)) {
			savePDF(request, proposalId, loginUser, CommonConstantsWeb.CREATE_PROPOSAL_PDF_SC,CommonConstants.LANG_CODE_SC);
			savePDF(request, proposalId, loginUser, CommonConstantsWeb.CREATE_PROPOSAL_PDF_TC,CommonConstants.LANG_CODE_TC);
			savePDF(request, proposalId, loginUser, CommonConstantsWeb.CREATE_PROPOSAL_PDF_EN,CommonConstants.LANG_CODE_EN);
			flag = true;
		}
		Map<String, Object> result = new TreeMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	
	/**
	 * pdf下载
	 * @author wwluo
	 * @throws IOException 
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/downPdf")
	public void downPdf(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws IOException {
		String proposalId = request.getParameter("proposalId");
		String fileName = null;
		CrmProposal proposal = crmProposalService.findProposalById(proposalId);
		String langCode = request.getParameter("language");
		if (StringUtils.isBlank(langCode)) {
			langCode = this.getLoginLangFlag(request);
		}
		if(proposal != null){
			fileName = proposal.getProposalName();
			String name = getCommonMemberName(proposal.getMember().getId(), langCode, "2");
			if (StringUtils.isNotBlank(name)) {
				fileName += "_" + name;
			}
			fileName += ".pdf";
		}
		MemberBase loginUser = this.getLoginUser(request);
		String filePath = null;
		// 获取下载路径
		List<AccessoryFile> accessoryFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, "create_proposal_PDF_"+langCode, langCode);
		if(accessoryFile != null && !accessoryFile.isEmpty()){
			filePath = accessoryFile.get(0).getFilePath();
			if (StringUtils.isBlank(fileName)) {
				fileName = accessoryFile.get(0).getFileName() + ".pdf";
			}
		}
		if (StringUtils.isNotBlank(filePath)) {
			File file = new File(filePath);
			if(!file.exists() && StringUtils.isNotBlank(proposalId)) {
				savePDF(request,proposalId, loginUser, CommonConstantsWeb.CREATE_PROPOSAL_PDF + langCode.toUpperCase(),langCode);
			}
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				long fileLength = new File(filePath).length();
				response.setContentType("application/x-msdownload;");
				fileName = URLEncoder.encode(fileName, "UTF-8");
				response.setContentType("UTF-8"); 
				if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > -1) {  
		            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);  
		        } else {              
		            response.setHeader("content-disposition", "attachment; filename=" + fileName);  
		        } 
				//response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1")); 
				response.setHeader("Content-Length", String.valueOf(fileLength));
				FileInputStream fileInputStream = new FileInputStream(filePath);
				bis = new BufferedInputStream(fileInputStream);
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] by = new byte[1024];
				int len;
				while((len=bis.read(by)) != -1){
					bos.write(by, 0, len);
				}
				bos.flush();
				bos.close();
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取ECharts图的下载路径
	 * @author wwluo
	 * @throws IOException 
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/downECharts")
	public void downECharts(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String proposalId = request.getParameter("proposalId");
		String name = request.getParameter("fileName");
		String fileName = null;
		List<AccessoryFile> accessoryFile = accessoryFileService.getAccessoryFile(proposalId, CommonConstantsWeb.CREATE_PROPOSAL_CRM_PROPOSAL, name, null);
		if(accessoryFile != null && !accessoryFile.isEmpty()){
			fileName = accessoryFile.get(0).getFilePath();
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("fileName", fileName);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 邮件发送
	 * @author wwluo
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/sendForConfirmation")
	public void sendForConfirmation(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		String proposalId = request.getParameter("proposalId");
		String clientId = request.getParameter("clientId");
		String receivers = request.getParameter("receivers");
		String subject = request.getParameter("subject");
		String content = this.toUTF8String(request.getParameter("message"));
		if (StringUtils.isNotBlank(clientId) && StringUtils.isNotBlank(proposalId)) {
			MemberBase client = memberBaseService.findById(clientId);
			CrmProposal proposal = crmProposalService.findProposalById(proposalId);
			if(receivers.indexOf(",") > -1){
				String[] receiver = receivers.split(",");
				for (String rec : receiver) {
					if (StringUtils.isBlank(rec)) {
						continue;
					}
					WebEmailLog emailLog = this.sendEmail(CommonConstantsWeb.WEB_EMAIL_LOG_PROPOSAL, loginUser, client, rec, subject, content, proposal.getId());
					if(emailLog != null){
						flag = true;
					}
				}
			}else{
				WebEmailLog emailLog = this.sendEmail(CommonConstantsWeb.WEB_EMAIL_LOG_PROPOSAL, loginUser, client, receivers, subject, content, proposal.getId());
				if(emailLog != null){
					flag = true;
				}
			}
		}
		// 投资方案提交提醒
		smsProposalSummit(request,response,model);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 投资方案提交提醒
	 * @author wwluo
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/smsProposalSummit")
	public void smsProposalSummit(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		boolean flag = false;
		String proposalId = request.getParameter("proposalId");
		if (StringUtils.isNotBlank(proposalId)) {
			CrmProposal crmProposal = crmProposalService.findProposalById(proposalId);
			crmProposal.setStatus("1");
			crmProposal = crmProposalService.saveCrmProposal(crmProposal);
			if(crmProposal != null){
				List<Object> params = new ArrayList<Object>();
			 	params.add(getCommonMemberName(crmProposal.getIfaMember().getMember().getId(), langCode, "2"));
			 	String titleSc = PropertyUtils.getSmsPropertyValue("proposal.submmit",CommonConstants.LANG_CODE_SC,params.toArray());
			 	String titleTc = PropertyUtils.getSmsPropertyValue("proposal.submmit",CommonConstants.LANG_CODE_TC,params.toArray());
			 	String titleEn = PropertyUtils.getSmsPropertyValue("proposal.submmit",CommonConstants.LANG_CODE_EN,params.toArray());
				// 发送消息提醒
				WebReadToDo webReadToDo = new WebReadToDo();
			 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL);
			 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_PROPOSAL);
			 	webReadToDo.setRelateId(proposalId);
			 	webReadToDo.setFromMember(loginUser);
			 	webReadToDo.setMsgUrl("/front/crm/proposal/confirmProposal.do");
			 	webReadToDo.setMsgParam("proposalId="+proposalId);
			 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MeProposalDetailActivity");
			 	webReadToDo.setAppParam("proposalId="+proposalId+"&status=1");
			 	webReadToDo.setIsApp("1");
			 	webReadToDo.setIsRead("0");
			 	webReadToDo.setOwner(crmProposal.getMember());
			 	webReadToDo.setCreateTime(new Date());
			 	webReadToDo.setIsValid("1");
			 	webReadToDo.setTitle(titleEn);
			 	webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
			 	// 插入审核记录
			 	CrmProposalCheck check = new CrmProposalCheck();
			 	check.setProposal(crmProposal);
			 	check.setCheck(crmProposal.getMember());
			 	check.setCheckStatus("0");
			 	check = crmProposalService.saveProposalCheck(check);
		 		flag = true;
			}
			// 修改客户销售阶段   sales_proposal
			CrmCustomer customer = crmCustomerService.findByIfaAndMember(crmProposal.getIfaMember().getId(), crmProposal.getMember().getId());
			if(customer != null && !"1".equals(customer.getClientType())){
				customer.setSalesStageId(CommonConstantsWeb.PIPELINE_SALES_PROPOSAL);
			 	crmCustomerService.saveCustomer(customer);
			}
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 创建方案预览页面
	 * @author wwluo
	 * @date 2016-12-12 
	 */
	@RequestMapping(value = "/previewProposal")
	public String previewProposal(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		String proposalId = request.getParameter("proposalId");
		CrmProposal proposal = crmProposalService.findProposalById(proposalId);
		if(proposal != null){
			model.put("member", proposal.getMember());
			model.put("name", getCommonMemberName(proposal.getMember().getId(), langCode , "2"));
			model.put("iconUrl", PageHelper.getUserHeadUrl(proposal.getMember().getIconUrl(),""));
		}else{
			model.put("iconUrl", PageHelper.getUserHeadUrl("",""));
		}
		model.put("proposal", proposal);
		return "ifa/crm/createProposal/viewProposal";
	}
	
	/**
	 * 创建方案投资人确认页面
	 * @author wwluo
	 * @date 2016-12-12 
	 */
	@RequestMapping(value = "/confirmProposal")
	public String confirmProposal(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		MemberBase loginUser = this.getLoginUser(request);
		String proposalId = request.getParameter("proposalId");
		CrmProposal proposal = crmProposalService.findProposalById(proposalId);
		if(proposal != null 
				&& loginUser.getMemberType() == CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR
				&& "1".equals(proposal.getStatus())){ // 1 待签约
			model.put("member", proposal.getIfaMember().getMember());
			model.put("name", getCommonMemberName(proposal.getIfaMember().getMember().getId(), langCode , "2"));
			model.put("iconUrl", PageHelper.getUserHeadUrl(proposal.getIfaMember().getMember().getIconUrl(),""));
			List<CrmProposalCheck> checks = crmProposalService.ifApprovalForProposal(loginUser.getId(), proposal.getId(), "0");
			if(checks != null && !checks.isEmpty()){
		    // 审批权限
				model.put("approvalView", "1");
			}else{
				model.put("approvalView", "0");
			}
			if(loginUser.getId().equals(proposal.getMember().getId())){
				model.put("proposal", proposal);
			}
		}else{
			model.put("proposalId", proposalId);
			return "redirect:" + this.getFullPath(request)+ "front/crm/proposal/previewProposal.do";
		}
		return "ifa/crm/createProposal/confirmProposal";
	}
		
	/**
	 * 创建方案投资人确认操作（同意、退回）
	 * @author wwluo
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/investorConfirmProposal")
	public void investorConfirmProposal(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		boolean flag = false;
		String proposalId = request.getParameter("proposalId");
		String status = request.getParameter("status");
		String opinon = request.getParameter("opinon");
		if (StringUtils.isNotBlank(proposalId) && StringUtils.isNotBlank(status)) {
			CrmProposal proposal = crmProposalService.findProposalById(proposalId);
			if(proposal != null){
				proposal.setStatus(status);
				proposal = crmProposalService.saveCrmProposal(proposal);
				PortfolioInfo portfolioInfo = crmProposalService.getPortfolioByProposal(proposal);
				if("2".equals(status)){
					// 保存审批核信息
					List<CrmProposalCheck> checks = crmProposalService.ifApprovalForProposal(proposal.getMember().getId(), proposal.getId(), "0");
					if(checks != null && !checks.isEmpty()){
						CrmProposalCheck check = checks.get(0);
						check.setCheckStatus("1"); // 0待审核,1审核通过,2审核不通过,3未审批
						check.setCheckTime(new Date());
						check.setCheckResult(opinon);
						check.setCheckIp(this.getRemoteHost(request));
						check = crmProposalService.saveProposalCheck(check);
					}
					// 修改客户类别
					CrmCustomer customer = crmCustomerService.findByIfaAndMember(proposal.getIfaMember().getId(), proposal.getMember().getId());
					if(customer != null && !"1".equals(customer.getClientType())){
						customer.setClientType("1");
						//customer.setSalesStageId(CommonConstantsWeb.PIPELINE_SALES_NEGOTIATING);
					 	crmCustomerService.saveCustomer(customer);
					}
				    // 新增一条持仓记录
					PortfolioHold hold = new PortfolioHold();
					hold.setPortfolioName(portfolioInfo.getPortfolioName());
					hold.setBaseCurrency(proposal.getBaseCurrId());
					hold.setCreateTime(new Date());
					hold.setRiskLevel(portfolioInfo.getRiskLevel());
					hold.setLastUpdate(new Date());
					hold.setIfa(portfolioInfo.getMemberIfa());
					hold.setPortfolio(portfolioInfo);
					hold.setMember(proposal.getMember());
					hold.setIfFirst("1");
					hold = (PortfolioHold) coreBaseService.create(hold);
					flag = true;
					// 新增一条 orderPlan 信息
					OrderPlan orderPlan = new OrderPlan();
					orderPlan.setBaseCurrency(proposal.getBaseCurrId());
					orderPlan.setPortfolioHold(hold);
					orderPlan.setFinishStatus("-1");
					orderPlan.setAipFlag(portfolioInfo.getAipFlag());
					orderPlan.setCreator(portfolioInfo.getMemberIfa().getMember());
					orderPlan.setCreateTime(new Date());
					orderPlan.setLastUpdate(new Date());
					orderPlan.setIsValid("1");
					orderPlan =  (OrderPlan) coreBaseService.create(orderPlan);
					// 添加交易定投信息
					if("1".equals(portfolioInfo.getAipFlag())){
						PortfolioInfoAip infoAip = crmProposalService.getAipByPorfolio(portfolioInfo);
						OrderPlanAip orderPlanAip = new OrderPlanAip();
						try {
							BeanUtils.copyProperties(orderPlanAip,infoAip);
							orderPlanAip.setId(null);
							orderPlanAip.setPlan(orderPlan);
							orderPlanAip.setCreateTime(new Date());
							orderPlanAip.setLastUpdate(new Date());
							orderPlanAip = (OrderPlanAip) coreBaseService.create(orderPlanAip);
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
					// 添加交易产品信息
					List<PortfolioInfoProduct> infoProducts = crmProposalService.getProductByPortfolio(portfolioInfo);
					if(infoProducts != null && !infoProducts.isEmpty()){
						for (PortfolioInfoProduct portfolioInfoProduct : infoProducts) {
							ProductDistributor productDistributor = tradeStepService.getProductDistributorByMemberIdAndProductId(hold.getMember().getId(), portfolioInfoProduct.getProduct().getId());
							if(productDistributor != null && "1".equals(productDistributor.getStatus()) && "Y".equals(productDistributor.getTradable())){
								OrderPlanProduct product = new OrderPlanProduct();
								product.setPlan(orderPlan);
								product.setWeight(portfolioInfoProduct.getAllocationRate());
								product.setProduct(portfolioInfoProduct.getProduct());
								product.setTranType(CommonConstants.TRAN_TYPE_BUY);
								product.setOriginal(0);
								product.setStatus("0");
								product = (OrderPlanProduct) coreBaseService.create(product);
							}
						}
					}
				}else{
					// 保存审批核信息
					List<CrmProposalCheck> checks = crmProposalService.ifApprovalForProposal(proposal.getMember().getId(), proposal.getId(), "0");
					if(checks != null && !checks.isEmpty()){
						CrmProposalCheck check = checks.get(0);
						check.setCheckStatus("2"); // 0待审核,1审核通过,2审核不通过,3未审批
						check.setCheckTime(new Date());
						check.setCheckResult(opinon);
						check.setCheckIp(this.getRemoteHost(request));
						check = crmProposalService.saveProposalCheck(check);
					}
				}
				flag = true;
			}
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 投资方案确认提醒（确认、退回）
	 * @author wwluo
	 * @date 2016-12-26
	 */
	@RequestMapping(value = "/smsProposalConfirmed")
	public void smsProposalConfirmed(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		String proposalId = request.getParameter("proposalId");
		String status = request.getParameter("status");
		if (StringUtils.isNotBlank(proposalId) && StringUtils.isNotBlank(status)) {
			CrmProposal crmProposal = crmProposalService.findProposalById(proposalId);
			if(crmProposal != null){
				WebReadToDo webReadToDo = new WebReadToDo();
			 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL);
			 	webReadToDo.setRelateId(proposalId);
			 	webReadToDo.setFromMember(crmProposal.getMember());
			 	String key = null;
			 	if("-1".equals(status)){
			 		webReadToDo.setMsgUrl("/front/crm/proposal/createProposalSetOne.do");
			 		webReadToDo.setMsgParam("id="+proposalId+"&memberId="+crmProposal.getMember().getId());
			 		webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MeProposalDetailActivity");
			 		webReadToDo.setAppParam("proposalId="+proposalId+"&status=-1");
				 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_PROPOSAL_REJECT);
				 	key = "proposal.reject";
				 	// 插入待办
				 	WebTaskList taskList = new WebTaskList();
				 	taskList.setCreateDate(new Date());
				 	taskList.setFromMember(loginUser);
				 	taskList.setHandleStatus("0");
				 	taskList.setIsValid("1");
				 	taskList.setModuleType("P");
				 	taskList.setOwner(crmProposal.getIfaMember().getMember());
				 	taskList.setRelateId(crmProposal.getId());
				 	taskList.setTaskUrl("/front/crm/proposal/createProposalSetOne.do");
				 	taskList.setTaskParam("id="+proposalId+"&memberId="+crmProposal.getMember().getId());
				 	taskList.setTitleSc(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_SC, "2")}));
				 	taskList.setTitleTc(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_TC, "2")}));
				 	taskList.setTitleEn(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_EN, "2")}));
				 	webTaskListService.saveTaskList(taskList);
			 	}else if("2".equals(status)){
			 		webReadToDo.setMsgUrl("/front/crm/proposal/previewProposal.do");
			 		webReadToDo.setMsgParam("proposalId="+proposalId);
			 		webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MeProposalDetailActivity");
			 		webReadToDo.setAppParam("proposalId="+proposalId+"&status=2");
				 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_PROPOSAL_CONFIRMED);
				 	key = "proposal.confirmed";
				 	// 插入待办
				 	WebTaskList taskList = new WebTaskList();
				 	taskList.setCreateDate(new Date());
				 	taskList.setFromMember(loginUser);
				 	taskList.setHandleStatus("0");
				 	taskList.setIsValid("1");
				 	taskList.setModuleType("P");
				 	taskList.setOwner(crmProposal.getIfaMember().getMember());
				 	taskList.setRelateId(crmProposal.getId());
				 	taskList.setTaskUrl("/front/crm/proposal/previewProposal.do");
				 	taskList.setTaskParam("proposalId="+proposalId);
				 	taskList.setTitleSc(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_SC, "2")}));
				 	taskList.setTitleTc(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_TC, "2")}));
				 	taskList.setTitleEn(PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_EN, "2")}));
				 	webTaskListService.saveTaskList(taskList);
			 	}
			 	webReadToDo.setIsApp("1");
			 	webReadToDo.setIsRead("0");
			 	webReadToDo.setOwner(crmProposal.getIfaMember().getMember());
			 	webReadToDo.setCreateTime(new Date());
			 	webReadToDo.setIsValid("1");
			 	String titleSc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_SC, "2")});
			 	String titleTc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_TC, "2")});
			 	String titleEn = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN, new String[]{getCommonMemberName(crmProposal.getMember().getId(), CommonConstants.LANG_CODE_EN, "2")});
			 	webReadToDo.setTitle(titleEn);
			 	webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
			 	flag = true;
			}
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 保存定投信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveProposaAip")
	public void saveProposaAip(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String proposalId = request.getParameter("proposalId");
		String currencyCode = request.getParameter("currencyCode");
		String riskLevelStr = request.getParameter("riskLevel");
		String aipExecCycle = request.getParameter("aipExecCycle");
		String aipInitTime = request.getParameter("aipInitTime");
		String chargeDay = request.getParameter("chargeDay");
		String aipAmount = request.getParameter("aipAmount");
		String aipEndType = request.getParameter("aipEndType");
		String aipEndDate = request.getParameter("aipEndDate");
		String aipEndCount = request.getParameter("aipEndCount");
		String aipEndTotalAmount = request.getParameter("aipEndTotalAmount");
		PortfolioInfo portfolioInfo = null;
		if (StringUtils.isNotBlank(proposalId)) {
			CrmProposal crmProposal = crmProposalService.findProposalById(proposalId);
			if(crmProposal != null){
				portfolioInfo = crmProposalService.findPortfolioByProposalId(proposalId);
				if(portfolioInfo == null){
					portfolioInfo = new PortfolioInfo();
					portfolioInfo.setAipFlag("1");
					portfolioInfo.setBaseCurrency(crmProposal.getBaseCurrId());
					portfolioInfo.setCreateTime(new Date());
					portfolioInfo.setCreator(loginUser);
					portfolioInfo.setIsValid("1");
					portfolioInfo.setLastUpdate(new Date());
					portfolioInfo.setMember(crmProposal.getMember());
					portfolioInfo.setMemberIfa(crmProposal.getIfaMember());
					portfolioInfo.setPortfolioName(crmProposal.getProposalName());
					portfolioInfo.setProposal(crmProposal);
					portfolioInfo = portfolioInfoService.saveOrUpdate(portfolioInfo);
				}
			}
		}
		if(portfolioInfo != null){
			Double exChangeRate = null;
			if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(portfolioInfo.getBaseCurrency())) {
				WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(currencyCode, portfolioInfo.getBaseCurrency());
				if(webExchangeRate != null){
					exChangeRate = webExchangeRate.getRate();
				}
			}
			if(exChangeRate == null){
				exChangeRate = 1.00;
			}
			PortfolioInfoAip aip = crmProposalService.getAipByPorfolio(portfolioInfo);
			if(aip == null){
				aip = new PortfolioInfoAip();
				aip.setPortfolio(portfolioInfo);
				aip.setCreateTime(new Date());
				aip.setLastUpdate(new Date());
			}
			try {
				if (StringUtils.isNotBlank(aipInitTime)) {
					aip.setAipInitTime(dateFormat.parse(aipInitTime));
				}else{
					aip.setAipInitTime(null);
				}
				if (StringUtils.isNotBlank(chargeDay)) {
					aip.setAipTimeDistance(Integer.parseInt(chargeDay));
				}else{
					aip.setAipTimeDistance(null);
				}
				if (StringUtils.isNotBlank(aipAmount)) {
					aip.setAipAmount(Double.parseDouble(aipAmount) * exChangeRate);
				}else{
					aip.setAipAmount(null);
				}
				if (StringUtils.isNotBlank(aipEndDate)) {
					aip.setAipEndDate(dateFormat.parse(aipEndDate));
				}else{
					aip.setAipEndDate(null);
				}
				if (StringUtils.isNotBlank(aipEndCount)) {
					aip.setAipEndCount(Integer.parseInt(aipEndCount));
				}else{
					aip.setAipEndCount(null);
				}
				if (StringUtils.isNotBlank(aipEndTotalAmount)) {
					aip.setAipEndTotalAmount(Double.parseDouble(aipEndTotalAmount) * exChangeRate);
				}else{
					aip.setAipEndTotalAmount(null);
				}
				aip.setAipEndType(aipEndType);
				aip.setAipExecCycle(aipExecCycle);
				aip.setLastUpdate(new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}
			aip = crmProposalService.saveAndUpdatePortfolioInfoAip(aip);
			Integer riskLevel = null;
			if (StringUtils.isNotBlank(riskLevelStr)) {
				riskLevel = Integer.valueOf(riskLevelStr);
			}
			portfolioInfo.setRiskLevel(riskLevel);
			portfolioInfo.setAipFlag("1");
			portfolioInfo.setLastUpdate(new Date());
			portfolioInfoService.saveOrUpdate(portfolioInfo);
			flag = true;
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 修改定投状态
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateAipState")
	public void updateAipState(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String proposalId = request.getParameter("proposalId");
		String state = request.getParameter("state");
		if (StringUtils.isNotBlank(proposalId) && StringUtils.isNotBlank(state)) {
			PortfolioInfo portfolio = crmProposalService.findPortfolioByProposalId(proposalId);
			if(portfolio != null){
				portfolio.setAipFlag(state);
				portfolio.setLastUpdate(new Date());
				portfolio = portfolioInfoService.saveOrUpdate(portfolio);
			}
		}
	}
	
	/**
	 * 获取组合产品信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getPortfolioProducts")
	public void getPortfolioProducts(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String proposalId = request.getParameter("id");
		List<String> fundIds = null;
		boolean flag = false;
		if (StringUtils.isNotBlank(proposalId)) {
			PortfolioInfo portfolio = crmProposalService.findPortfolioByProposalId(proposalId);
			if(portfolio != null){
				List<PortfolioInfoProduct> infoProducts = crmProposalService.getProductByPortfolio(portfolio);
				if(infoProducts != null && !infoProducts.isEmpty()){
					fundIds = new ArrayList<String>();
					for (PortfolioInfoProduct portfolioInfoProduct : infoProducts) {
						FundInfo fundInfo = crmProposalService.getFundInfoByProduct(portfolioInfoProduct.getProduct());
						fundIds.add(fundInfo.getId());
					}
					flag = true;
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("fundIds", fundIds);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取组合产品详情信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getPortfolioProductDetails")
	public void getPortfolioProductDetails(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String proposalId = request.getParameter("id");
		List<Object> fundIds = null;
		boolean flag = false;
		if (StringUtils.isNotBlank(proposalId)) {
			PortfolioInfo portfolio = crmProposalService.findPortfolioByProposalId(proposalId);
			if(portfolio != null){
				List<PortfolioInfoProductDetail> infoProductDetails = crmProposalService.getProductDetailByPortfolioProduct(portfolio.getId());
				if(!infoProductDetails.isEmpty() && infoProductDetails != null){
					fundIds = new ArrayList<Object>();
					for (PortfolioInfoProductDetail detail : infoProductDetails) {
						if(detail.getProduct() != null){
							Map<String, Object> product = new HashMap<String, Object>();
							FundInfo fundInfo = crmProposalService.getFundInfoByProduct(detail.getProduct());
							if(fundInfo != null){
								product.put("fund", fundInfo.getId());
								PortfolioInfoProduct infoProduct = crmProposalService.getProduct(portfolio.getId(),fundInfo.getProduct().getId());
								Double weight = 0.00;
								if(infoProduct != null){
									weight = infoProduct.getAllocationRate();
								}
								product.put("weight", weight);
								List<PortfolioInfoProductDetail> details = crmProposalService.getProductDetailByPortfolioProduct(portfolio, detail.getProduct());
								List<String> spareFunds = null;
								if(details != null && !details.isEmpty()){
									spareFunds = new ArrayList<String>();
									for (PortfolioInfoProductDetail portfolioInfoProductDetail : details) {
										ProductInfo info = portfolioInfoProductDetail.getCandidateProduct();
										FundInfo spareFund = crmProposalService.getFundInfoByProduct(info);
										if(spareFund != null){
											spareFunds.add(spareFund.getId());
										}
									}
								}
								product.put("spareFunds", spareFunds);
							}
							if(!fundIds.contains(product)){
								fundIds.add(product);
							}
						}
					}
					flag = true;
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("fundIds", fundIds);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 投资方案邮件发送记录页面
	 * @author wwluo
	 * @date 2017-04-28
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/emailRecords")
	public String emailRecords(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "proposal/emailRecords/emailRecords";
	}
	
	/**
	 * 获取投资方案邮件发送记录
	 * @author wwluo
	 * @data 2017-04-28
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getEmailRecords")
	public void getEmailRecords(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String dateFormat = loginUser.getDateFormat();
		String langCode = this.getLoginLangFlag(request);
		Boolean flag = false;
		String proposalId = request.getParameter("proposalId");
		List<ProposalEmailRecordsVO> records = null;
		if (StringUtils.isNotBlank(proposalId)) {
			records = crmProposalService.getEmailRecords(proposalId, langCode, dateFormat);
			flag = true;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("records", records);
		JsonUtil.toWriter(result, response);
	}
	
	
	
	
	
	
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
