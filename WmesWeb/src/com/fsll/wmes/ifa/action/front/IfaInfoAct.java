/**
 * 
 */
package com.fsll.wmes.ifa.action.front;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.buscore.fund.service.CorePortfolioService;
import com.fsll.buscore.fund.service.impl.CoreFundServiceImpl;
import com.fsll.buscore.fund.service.impl.CorePortfolioServiceImpl;
import com.fsll.buscore.fund.vo.CoreFundNavAlignVO;
import com.fsll.buscore.fund.vo.CoreFundNavVO;
import com.fsll.buscore.fund.vo.CoreFundsReturnForChartsVO;
import com.fsll.buscore.fund.vo.CorePortfolioVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.service.CommunitySpaceService;
import com.fsll.wmes.community.vo.FocusMemberVO;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.service.CrmMemoService;
import com.fsll.wmes.crm.service.CrmProposalService;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.crm.vo.CrmClientForIfaVO;
import com.fsll.wmes.crm.vo.CrmClientVO;
import com.fsll.wmes.crm.vo.CrmSelectVO;
import com.fsll.wmes.distributor.service.DistributorOrgService;
import com.fsll.wmes.entity.CornerInfo;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmMemo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MyAsset;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebQueryCriteria;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.entity.WebWatchlistType;
import com.fsll.wmes.fund.service.FundHouseService;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.service.FundReturnService;
import com.fsll.wmes.fund.vo.FundBasicDataVO;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.service.IfaSpaceService;
import com.fsll.wmes.ifa.service.InsightInfoService;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.ifa.vo.IfaCorner;
import com.fsll.wmes.ifa.vo.IfaCornerInfoDetailVO;
import com.fsll.wmes.ifa.vo.IfaCountryVO;
import com.fsll.wmes.ifa.vo.IfaDetailVO;
import com.fsll.wmes.ifa.vo.IfaListfiltersVO;
import com.fsll.wmes.ifa.vo.IfafirmVO;
import com.fsll.wmes.ifa.vo.MyFavoritesPortfolios;
import com.fsll.wmes.ifa.vo.MyFavoritesStrategy;
import com.fsll.wmes.ifa.vo.MyFavoritesWatchingTypeVOList;
import com.fsll.wmes.ifa.vo.RecommendInfoVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.InvestorAccountCurrencyVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.service.MemberDistributorService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.notice.service.NoticeService;
import com.fsll.wmes.notice.vo.NoticeVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.StrategyWebPushVO;
import com.fsll.wmes.web.service.WebChatService;
import com.fsll.wmes.web.service.WebFriendService;
import com.fsll.wmes.web.service.WebQueryCriteriaService;
import com.fsll.wmes.web.service.WebReadToDoService;
import com.fsll.wmes.web.service.WebWatchListService;
import com.sun.jndi.toolkit.url.UrlUtil;

/**
 * @author scshi
 * 
 */
@Controller
@RequestMapping("/front/ifa/info")
public class IfaInfoAct extends WmesBaseAct {

	@Autowired
	private IfaInfoService ifaInfoService;

	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private IfaSpaceService ifaSpaceService;

	/*@Autowired
	private IfaManageService ifaManageService;*/

	@Autowired
	private DistributorOrgService distributorOrgService;

	@Autowired
	private FundReturnService fundReturnService;

	@Autowired
	private WebQueryCriteriaService webQueryCriteriaService;
	
	@Autowired
	private SysParamService sysParamService;
	
	@Autowired
	private MemberDistributorService memberDistributorService;
	
	@Autowired
	private FundHouseService fundHouseService;
	
	@Autowired
	private WebReadToDoService webReadToDoService;
	
	@Autowired
	private CrmMemoService crmMemoService;
	
	@Autowired
	private MemberBaseService memberBaseService;
	
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private InvestorService investorService;
	@Autowired
	private PortfolioHoldService portfolioHoldService;

	@Autowired
	private CrmProposalService crmProposalService;
	@Autowired
	private InsightInfoService insightInfoService;


	@Autowired
	private WebChatService webChatService;
	
	@Autowired
	private WebFriendService webFriendService;
	@Autowired
	private StrategyInfoService strategyInfoService;
	@Autowired
	private CommunitySpaceService communitySpaceService;
	@Autowired
	private WebWatchListService webWatchListService;
	@Autowired
	private CoreFundServiceImpl coreFundServiceImpl;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private CorePortfolioService corePortfolioService;
	
	/**
	 * 打开列表界面初始化基础数据
	 * 
	 * @author 林文伟
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String ifaList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		String lang = this.getLoginLangFlag(request);
		String loginMemberId = "";
		MemberBase curMember = this.getLoginUser(request);
		if (null != curMember&&null != curMember.getMemberType()) {
			loginMemberId = curMember.getId();
		}
		model.put("loginMemberId", loginMemberId);
		// 获取公司列表
		List<IfafirmVO> ifafirmList = ifaInfoService.loadIFAFirmList(lang);
		model.put("ifafirmList", ifafirmList);
		// 获取投资风格
		List<GeneralDataVO> investmentList = findSysParameters("investment_style", lang);
		model.put("investmentList", investmentList);
		// 获取服务区域
		List<GeneralDataVO> serviceRegionList = findSysParameters("service_region", lang);
		model.put("serviceRegionList", serviceRegionList);
		// 获取服务语言
		List<GeneralDataVO> serviceLanguageList = findSysParameters("service_language", lang);
		model.put("serviceLanguageList", serviceLanguageList);
		// 获取擅长投资领域
		List<GeneralDataVO> expertiseList = findSysParameters("invest_field", lang);
		model.put("expertiseList", expertiseList);
		// 获取ifa所在国籍地区
		List<IfaCountryVO> countryList = ifaInfoService.findCountryList(lang);
		model.put("countryList", countryList);

		return "ifa/info/ifaList";
	}

	/**
	 * 分页获得记录，供前台调用
	 * 
	 * @author 林文伟
	 * 
	 */
	@RequestMapping(value = "/getifalistJson", method = RequestMethod.POST)
	public void getIfaListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		MemberIndividual investor = null;
		if(null!=loginUser){
			int memberType = loginUser.getMemberType();
			if(1==memberType){
				 String memberId = loginUser.getId();
				 investor = new MemberIndividual();
				 investor = ifaInfoService.getMemberIndividual(memberId);
				 
			}
		}
		IfaListfiltersVO filtersVO = new IfaListfiltersVO();
		String lang = this.getLoginLangFlag(request);
		String keyword = toUTF8String(request.getParameter("keyword"));
		String ifaFirmIds = toUTF8String(request.getParameter("ifaFirmIds"));
		String investmentType = request.getParameter("InvestmentType");
		String serviceRegion = request.getParameter("serviceRegion");
		String serviceLanguage = toUTF8String(request.getParameter("serviceLanguage"));
		String expertise = toUTF8String(request.getParameter("expertise"));
		String workingYearsFrom = toUTF8String(request.getParameter("workingYearsFrom"));
		String workingYearsTo = toUTF8String(request.getParameter("workingYearsTo"));
		String belongCountry = toUTF8String(request.getParameter("belongCountry"));
		String sort = toUTF8String(request.getParameter("sort"));

		filtersVO.setBelongCountry(belongCountry);
		filtersVO.setExpertise(expertise);
		filtersVO.setIfaFirmIds(ifaFirmIds);
		filtersVO.setInvestmentType(investmentType);
		filtersVO.setKeyword(keyword);
		filtersVO.setLang(lang);
		filtersVO.setServiceLanguage(serviceLanguage);
		filtersVO.setServiceRegion(serviceRegion);
		filtersVO.setWorkingYearsFrom(workingYearsFrom);
		filtersVO.setWorkingYearsTo(workingYearsTo);
		filtersVO.setSort(sort);

		jsonPaging = this.getJsonPaging(request);
		jsonPaging = ifaInfoService.loadIFAList(jsonPaging, filtersVO,investor);
		// this.toJsonString(response, jsonPaging);
		model.put("jsonPaging", jsonPaging);

		this.toJsonString(response, jsonPaging);
		// ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
	}

	/****
	 * 添加好友
	 * 
	 * @author 林文伟
	 * @date 2016-07-18
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/addFriend", method = RequestMethod.POST)
	public void addFriend(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode=getLoginLangFlag(request);
		String toMemberId = request.getParameter("toMemberId");
		// 获取当前登录者信息
		MemberBase curMember = this.getLoginUser(request);
		Map<String, Object> obj = new HashMap<String, Object>();
		if(null==curMember)
		{
			obj.put("result", false);
			obj.put("msg", PropertyUtils.getPropertyValue(langCode, "ifalist.list.logintoaddfriendmsg", null));
		} else {
			String nickName = curMember.getNickName();
			String applyMsg = nickName+" " + PropertyUtils.getPropertyValue(langCode, "ifalist.list.applyfriendmsg", null);
			String fromMemberId = curMember.getId();
			String rs = webFriendService.saveWebFriend(fromMemberId, toMemberId, applyMsg);

			if (rs.length() > 20) { // 添加成功
				//发送消息通知
				WebReadToDo webReadToDo = new WebReadToDo();
				webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL);
				webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_FRIENDS);
				webReadToDo.setRelateId(rs);
				webReadToDo.setFromMember(curMember);
				webReadToDo.setIsRead("0");//待阅
				webReadToDo.setIsValid("1");
				webReadToDo.setTitle(applyMsg);
				
				MemberBase toUser = new MemberBase();
				toUser.setId(toMemberId);
				webReadToDo.setOwner(toUser);
				
				webReadToDo.setCreateTime(new Date());
				
				//webReadToDoService.sendToRead(webReadToDo);
				webReadToDoService.saveWebReadToDo(webReadToDo, nickName+" " + PropertyUtils.getPropertyValue("en", "ifalist.list.applyfriendmsg", null), nickName+" " + PropertyUtils.getPropertyValue("sc", "ifalist.list.applyfriendmsg", null), nickName+" " + PropertyUtils.getPropertyValue("tc", "ifalist.list.applyfriendmsg", null));
			    
				obj.put("result", true);
				obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success", null));

			} else if("1" == rs){
				obj.put("result", false);
				obj.put("msg", PropertyUtils.getPropertyValue(langCode, "ifalist.list.repeataddfriendmsg", null));
			} 
			else {
				obj.put("result", false);
				obj.put("msg", "error!");
			} 
		}
		
		ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
	}

	/**
	 * ifa详情
	 * @author mqzou 2016-12-26
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String ifaDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser=getLoginUser(request);
		String currency=loginUser.getDefCurrency();
		if(null==currency || "".equals(currency))
			currency=CommonConstants.DEF_CURRENCY;
		model.put("currency", currency);
		String defColor=loginUser.getDefDisplayColor();
		if(null==defColor || "".equals(defColor)){
			defColor=CommonConstants.DEF_DISPLAY_COLOR;
		}
		
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat))
			dateFormat=CommonConstants.FORMAT_DATE;
		model.put("dateFormat", dateFormat);
		model.put("dateTimeFormat", dateFormat+" "+CommonConstants.FORMAT_TIME);
		 
		 /*String check=request.getParameter("check");
			if(check==null || check=="0"){
				AccountValidVO validVO=iTraderSendService.saveCheckExistValid(request, loginUser.getId());
				if(null!=validVO){
					model.put("checkList", validVO.getValidAccountNo());
					model.put("accountType", CommonConstantsWeb.WEB_ACCOUNTCHECK_INVESTOR);
				}
				
			}*/
		model.put("defColor", defColor);
        String id=request.getParameter("memberId");
        String langCode=getLoginLangFlag(request);
        if(null!=id){
        	MemberIfa ifa=memberBaseService.findIfaMember(id);
            if(null!=ifa){
            	
            	MemberBase member=ifa.getMember();
            	
            	IfaDetailVO vo=new IfaDetailVO();
            	
            	String webchatCode=null!=member.getWebchatCode()&& !"".equals(member.getWebchatCode())?member.getWebchatCode():"N/A";
            	String linkedinCode=null!=member.getLinkedInCode() && !"".equals(member.getLinkedInCode())?member.getLinkedInCode():"N/A";
            	String facebookCode=null!=member.getFacebookCode()  && !"".equals(member.getFacebookCode())?member.getFacebookCode():"N/A";
            	String qqCode=null!=member.getQqCode()&& !"".equals(member.getQqCode())?member.getQqCode() :"N/A";
            	String weiboCode=null!=member.getWeiboCode() && !"".equals(member.getWeiboCode())?member.getWeiboCode():"N/A";
            	String twitterCode=null!=member.getTwitterCode() && !"".equals(member.getTwitterCode())?member.getTwitterCode():"N/A";
            	String lastName=null!=ifa.getLastName() && !"".equals(ifa.getLastName())?ifa.getLastName():"N/A";
            	String firstName=null!=ifa.getFirstName() && !"".equals(ifa.getFirstName())?ifa.getFirstName():"N/A";
            	String email=null!=member.getEmail()  && !"".equals(member.getEmail())?member.getEmail():"N/A";
            	String nameChn=null!=ifa.getNameChn() && !"".equals(ifa.getNameChn())?ifa.getNameChn():"N/A";
            	String nickName=null!=member.getNickName() && !"".equals(member.getNickName())?member.getNickName():"N/A";
            	String moble=null!=member.getMobileNumber()  && !"".equals(member.getMobileNumber())?member.getMobileNumber():"N/A";
            	member.setWebchatCode(webchatCode);
            	member.setLinkedInCode(linkedinCode);
            	member.setFacebookCode(facebookCode);
				member.setQqCode(qqCode);
				member.setWeiboCode(weiboCode);
				member.setTwitterCode(twitterCode);
				ifa.setLastName(lastName);
				ifa.setFirstName(firstName);
				member.setEmail(email);
				ifa.setNameChn(nameChn);
				member.setNickName(nickName);
				member.setMobileNumber(moble);
            	
                
            	String privateSetting=member.getPrivacySetting();
            	if(null!=privateSetting){
            		if(privateSetting.contains("webchat_code:0")){
            			member.setWebchatCode(getPrivateStr(webchatCode, "N/A"));
                	}
                	if(privateSetting.contains("linkedin_code:0")){
                		member.setLinkedInCode(getPrivateStr(linkedinCode, "N/A"));
                	}
                    if(privateSetting.contains("facebook_code:0")){
                    	member.setFacebookCode(getPrivateStr(facebookCode, "N/A"));
                	}
                    if(privateSetting.contains("qq_code:0")){
                    	member.setQqCode(getPrivateStr(qqCode, "N/A"));
                	}
                    if(privateSetting.contains("weibo_code:0")){
                    	member.setWeiboCode(getPrivateStr(weiboCode, "N/A"));
                	}
                    if(privateSetting.contains("twitter_code:0")){
                    	member.setTwitterCode(getPrivateStr(twitterCode, "N/A"));
                	} 
                    
                    if(privateSetting.contains("last_name:0")){
                        ifa.setLastName(getPrivateStr(lastName, "N/A"));
                	}
                    if(privateSetting.contains("first_name:0")){
                    	 ifa.setFirstName(getPrivateStr(firstName, "N/A"));
                	}
                    if(privateSetting.contains("email:0")){
                    	 member.setEmail(getPrivateStr(email, "N/A"));
                	}
                    if(privateSetting.contains("name_chn:0")){
                    	ifa.setNameChn(getPrivateStr(nameChn, "N/A"));
                	}
                    if(privateSetting.contains("nick_name:0")){
                    	member.setNickName(getPrivateStr(nickName, "N/A"));
                	}
                    if(privateSetting.contains("mobile_number:0")){
                       member.setMobileNumber(getPrivateStr(moble, "N/A"));
                	}
                    
            	}
            	vo.setMember(member);
            	vo.setIfaInfo(ifa);
                
            	vo.setNationality(getCountryString(ifa.getNationality(), langCode));
            	vo.setEducation(sysParamService.findNameByCode(ifa.getEducation(), langCode));
            	vo.setEmployment(sysParamService.findNameByCode(ifa.getEmployment(), langCode));
            	vo.setOccupation(sysParamService.findNameByCode(ifa.getOccupation(), langCode));
            	vo.setCertType(sysParamService.findNameByCode(ifa.getCertType(), langCode));
            	Date investBegin=ifa.getInvestLifeBegin();
            	if(null!=investBegin){
            		long days=DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(investBegin, "yyyy-MM-dd"), DateUtil.getCurDateStr());
            		 int year=Math.round(days/365);
            		 vo.setInvestLife(String.valueOf(year+1));
            	}else {
					vo.setInvestLife("0");
				}
            	
            	model.put("ifa", vo);
            	
            	 //兴趣爱好
                List<GeneralDataVO> itemList = findSysParameters("hobby_type", langCode);
                
                itemList = getMyGeneralList(itemList, member.getHobbyType(),"hobbyList");
                model.put("hobbyList", itemList);
              
                
                itemList = findSysParameters("invest_field", langCode);
                itemList = getMyGeneralList(itemList, member.getInvestField(),"investField");
                model.put("investField", itemList);
             
                itemList = findSysParameters("investment_style", langCode);
                itemList = getMyGeneralList(itemList, member.getInvestStyle(),"investStyle");
                model.put("investStyle", itemList);

                
                itemList = findSysParameters("service_language", langCode);
                itemList = getMyGeneralList(itemList, member.getLanguageSpoken(),"languageSpoken");
                model.put("languageSpoken", itemList);
               

               
                itemList = findSysParameters("service_region", langCode);
                itemList = getMyGeneralList(itemList, member.getLiveRegion(),"liveRegion");
                model.put("liveRegion", itemList);
                
                model.put("memberId", id);
                
                InvestorAccount account=new InvestorAccount();
    			account.setIfa(ifa);
    			//新开户状态:-1 退回, 0 草稿, 1 等待(投资人)确认, 2 处理中 ,3 成功开户,4 失败
    			account.setOpenStatus("3");
    			account.setIsValid("1");
                List distributorList=investorService.findInvestorDistributor(account);
    			model.put("distributorList", distributorList);

    			List<String> chanelList=insightInfoService.findInsightChanel(id);
    			model.put("chanelList", chanelList);
            }
        }
        
		return "ifa/info/ifaAdministration";
	}
	
	private String getPrivateStr(String string,String filter){
	  return	!filter.equals(string) && null!=string?"*****"+string.substring(string.length()-1,string.length()):"";
	}
	
	/**
	 * 从基础参数列表中获取指定编码的参数对象
	 * @param request
	 * @param response
	 * @param model
	 */
	private List<GeneralDataVO> getMyGeneralList(List<GeneralDataVO> list, String codes,String type){
		List<GeneralDataVO> result = new ArrayList<GeneralDataVO>();
		if (list!=null && !list.isEmpty() && codes!=null && codes.length()>0){
			List<String> codeArr = Arrays.asList(StrUtils.splitAndTrim(codes,",",""));
			List<String> codeList=new ArrayList<String>();
			Iterator it=codeArr.iterator();
			while (it.hasNext()) {
				String object = (String) it.next();
				codeList.add(object);
			}
			for (GeneralDataVO v: list){
				if (codeArr.contains(v.getItemCode())){
					result.add(v);
				 //  int index=	codeArr.indexOf(v.getItemCode());
					codeList.remove(v.getItemCode());
				}
			}
			if("hobbyList".equals(type)  && !codeList.isEmpty()){//爱好因可自定义需要特殊处理 modify by mqzou 2016-11-29
				 it=codeList.iterator();
				while (it.hasNext()) {
					String str = (String) it.next();
					GeneralDataVO vo=new GeneralDataVO();
					vo.setName(str.replace("{", "").replace("}", ""));
					result.add(vo);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 获取ifa的客户列表
	 * @author mqzou 2016-12-26
	 */
	@RequestMapping(value = "/ifaClient", method = RequestMethod.POST)
	public void ifaClient(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		//MemberBase loginUser=getLoginUser(request);
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String defCurrency=ssoVO.getDefCurrency();
		String langCode=getLoginLangFlag(request);
		if(null==defCurrency || "".equals(defCurrency)){
			defCurrency=CommonConstants.DEF_CURRENCY;
		}
		String id=request.getParameter("memberId");
		String type=request.getParameter("type");
		String keyWord=request.getParameter("keyWord");
		if(null!=keyWord  && !"".equals(keyWord))
			keyWord=toUTF8String(keyWord);
		
		jsonPaging=getJsonPaging(request);
		jsonPaging=crmCustomerService.findClientForIfa(jsonPaging,id, keyWord, type,langCode);
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator<CrmClientForIfaVO> it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				CrmClientForIfaVO vo = (CrmClientForIfaVO) it.next();
				//InvestorAccountCurrencyVO accountCurrency=investorService.findInvestorCurrency(id, defCurrency,ssoVO.getIfaId());
				MyAsset myAsset = investorService.getMyAsset(vo.getMemberId(), defCurrency);
				if(null!=myAsset){
					vo.setTotalAsset(myAsset.getTotalAsset());
					vo.setTotalAssetStr(StrUtils.getNumberString(vo.getTotalAsset()	, "#,##0.00"));
				}
			}
		}
		JsonUtil.toWriter(jsonPaging, response);
	}
	/**
	 * 获取ifa创建的proposal
	 * @author mqzou 2016-12-27
	 */
	@RequestMapping(value = "/ifaProposal", method = RequestMethod.POST)
	public void ifaProposal(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String currency=loginUser.getDefCurrency();
		String langCode=getLoginLangFlag(request);
		if(null==currency|| "".equals(currency))
			currency=CommonConstants.DEF_CURRENCY;
		String memberId=request.getParameter("memberId");
		String keyWord=request.getParameter("keyWord");
		
		StringBuilder strCondiction=new StringBuilder();
		if(null!=keyWord && !"".equals(keyWord)){
			keyWord=toUTF8String(keyWord);
			strCondiction.append(" and r.proposalName like '%"+keyWord+"%'");
		}
		
		if(!loginUser.getId().equals(memberId)){
			strCondiction.append(" and r.status !='0'");
		}
		
		
		jsonPaging=getJsonPaging(request);
		jsonPaging=crmProposalService.findProposal(jsonPaging, strCondiction.toString(), memberId,currency,langCode);
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * 获取ifa的客户账户列表
	 * @author mqzou 2016-12-27
	 */
	@RequestMapping(value = "/ifaClientAccount", method = RequestMethod.POST)
	public void ifaClientAccount(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String currency=loginUser.getDefCurrency();
		String langCode=getLoginLangFlag(request);
		if(null==currency|| "".equals(currency))
			currency=CommonConstants.DEF_CURRENCY;
		String memberId=request.getParameter("memberId");
		String keyWord=request.getParameter("keyWord");
		if(null!=keyWord && !"".equals(keyWord)){
			keyWord=toUTF8String(keyWord);
		}
		String distributorId=request.getParameter("distributorId");
		jsonPaging =getJsonPaging(request);
		jsonPaging=investorService.findCrmAccountList(jsonPaging,memberId, distributorId, keyWord, currency,langCode);
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * 获取ifa的投资组合列表
	 * @author mqzou 2016-12-27
	 */
	@RequestMapping(value = "/ifaClientPortfolio", method = RequestMethod.POST)
	public void ifaClientPortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String currency=loginUser.getDefCurrency();
		if(null==currency|| "".equals(currency))
			currency=CommonConstants.DEF_CURRENCY;
		String memberId=request.getParameter("memberId");
		String keyWord=request.getParameter("keyWord");
		if(null!=keyWord && !"".equals(keyWord)){
			keyWord=toUTF8String(keyWord);
		}
		jsonPaging=getJsonPaging(request);
		jsonPaging=portfolioHoldService.findPortfolioForIfa(jsonPaging, memberId, currency, keyWord);
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * 获取ifa的推荐的基金列表
	 * @author mqzou 2016-12-27
	 */
	@RequestMapping(value = "/ifaRecomentFund", method = RequestMethod.POST)
	public void ifaRecomentFund(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String memberId=request.getParameter("memberId");
		MemberBase member=memberBaseService.findById(memberId);
		String langCode=getLoginLangFlag(request);
		List<WebRecommended> list = ifaInfoService.getIfaRecommendFundIds(member, "", "", "", "", langCode);
		String recommendFundIds = this.listRecommendFundIdToString(list, ",");
		if (!"".equals(recommendFundIds)){
			jsonPaging = queryFundsByFilters(request, recommendFundIds, model);
			List dataList=jsonPaging.getList();
			if(null!=dataList && !dataList.isEmpty()){
				Iterator it= dataList.iterator();
				while (it.hasNext()) {
					FundInfoDataVO vo = (FundInfoDataVO) it.next();
					RecommendInfoVO recommendInfoVO=ifaSpaceService.getRecommendInfo(memberId, vo.getFundId(), "fund");
					vo.setRecommendInfo(recommendInfoVO);
				}
			}
			List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(langCode);
			model.put("jsonPaging", jsonPaging);
			model.put("fundHouseList_in", fundHouseList);
		}else {
			jsonPaging=new JsonPaging();
		}
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * 获取ifa的观点列表
	 * @author mqzou 2016-12-27
	 */
	@RequestMapping(value = "/ifaInsight", method = RequestMethod.POST)
	public void getIfaInsight(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String memberId=request.getParameter("memberId");
		String keyWord=request.getParameter("keyWord");
		String langCode=getLoginLangFlag(request);
		if(null!=keyWord && !"".equals(keyWord)){
			keyWord=toUTF8String(keyWord);
		}
		String chanel=request.getParameter("chanel");
		jsonPaging=getJsonPaging(request);
		jsonPaging=insightInfoService.findIfaInsightList(jsonPaging, memberId, chanel, keyWord);
		if(null!=jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				InsightInfo info = (InsightInfo) it.next();
				if(null!=info.getPubDate()){
					info.setPubDateStr(DateUtil.getDateTimeGoneFormatStr(info.getPubDate(), langCode, ""));
				}
				
			}
		}
		JsonUtil.toWriter(jsonPaging, response);
	}

	@RequestMapping(value = "/space", method = RequestMethod.GET)
	public String ifaSpace(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/ifaSpace";
	}

	@RequestMapping(value = "/article", method = RequestMethod.GET)
	public String ifaArticle(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/ifaArticle";
	}

	/**
	 * modify by mqzou 2016-12-20 Clients
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ifahome", method = RequestMethod.GET)
	public String ifaHome(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser = this.getLoginUser(request);
		if (null==ssoVO)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		
		String currency=request.getParameter("currency");
		String defColor=ssoVO.getDefDisplayColor();
		if(null==defColor || "".equals(defColor))
			defColor=CommonConstants.DEF_DISPLAY_COLOR;
		
		model.put("defColor", defColor);
		if(null==currency || "".equals(currency)){
			currency=ssoVO.getDefCurrency();
		}
		if(null==currency || "".equals(currency)){
			currency=CommonConstants.DEF_CURRENCY;
		}
		model.put("currency", currency);
		String currencyName=sysParamService.findNameByCode(currency, langCode);
		model.put("currencyName", currencyName);
		
		//String numFormat="#,##0.00";
		//金额保留小数位数
		int decimals=2;
		if("JPY".equals(currency)){
			decimals=0;
		}
		model.put("decimals", decimals);
		
		
		/*String check=request.getParameter("check");
			
			if(check==null || check=="" || check=="0"){
				AccountValidVO validVO=iTraderSendService.saveCheckExistValid(request, ssoVO.getId());
				if (null!=validVO)
					model.put("checkList", validVO.getValidAccountNo());
				model.put("accountType", CommonConstantsWeb.WEB_ACCOUNTCHECK_IFA);
			}*/
		
		// 获取公告 
		List<NoticeVO> notices = noticeService.getMyNotices(loginUser, langCode, 5);	
		model.put("sysNotices", notices);
		return "ifa/home/ifaHome";
	}

	/**
	 * 获取ifa的Clients
	 * @author mqzou 2016-12-20
	 * 
	 */
	@RequestMapping(value = "/getClients", method = RequestMethod.POST)
	public void getClientProfitFive(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String currency=request.getParameter("currency");
		String type=request.getParameter("type");
		String langCode=getLoginLangFlag(request);
		if(null==currency || "".equals(currency)){
			currency=ssoVO.getDefCurrency();
		}
		if(null==currency || "".equals(currency)){
			currency=CommonConstants.DEF_CURRENCY;
		}
		ClientSearchVO vo=new ClientSearchVO();
		vo.setIfaId(ssoVO.getIfaId());
		vo.setStatistic("h.`total_return_rate`");
		vo.setCurrency(currency);
		vo.setLangCode(langCode);
		jsonPaging=new JsonPaging();
		jsonPaging.setRows(5);
		String order="";
		if("profit".equals(type)){
			order="desc";
		}else {
			order="asc";
		}
		jsonPaging.setOrder(order);
		jsonPaging.setSort("total_return_rate");
		
		
		jsonPaging=crmCustomerService.findClentsByIFA(jsonPaging, vo);
		if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				CrmClientVO clientVO = (CrmClientVO) it.next();
				boolean result=investorService.checkFacaOrCies(clientVO.getMemberId(), "faca");
				if(result){
					clientVO.setFaca("1");
				}else {
					clientVO.setFaca("0");
				}
				result=investorService.checkFacaOrCies(clientVO.getMemberId(), "cies");
				if(result){
					clientVO.setCies("1");
				}else {
					clientVO.setCies("0");
				}
				List chartData=portfolioHoldService.getProductChartData(clientVO.getMemberId(), ssoVO.getIfaId(), "", currency);
				/*List productList=portfolioHoldService.findPortfolioAllocation(clientVO.getMemberId());
				List<CharDataVO> chartData=new ArrayList<CharDataVO>();
				if(null!=productList){
					Iterator iterator=productList.iterator();
					while (iterator.hasNext()) {
						Object[] obj = (Object[]) iterator.next();
						if(null==obj[0] || "".equals(obj[0]))
							continue;
						CharDataVO chart=new CharDataVO();
						chart.setName(obj[0].toString());
						chart.setValue(null!=obj[1]?Double.parseDouble(obj[1].toString()):0);
						chartData.add(chart);
					}
				}*/
				clientVO.setProductProJson(JsonUtil.toJson(chartData));
				String risk=investorService.findInvestorMaxRiskLevel(clientVO.getMemberId());
				clientVO.setRiskLevel(risk);
				clientVO.setAumStr(StrUtils.getNumberString(clientVO.getAum(),"#,##0.00"));
				clientVO.setTotalReturnRateStr(StrUtils.getNumberString(clientVO.getTotalReturnRate(),"#,##0.00"));
				clientVO.setTotalReturnStr(StrUtils.getNumberString(clientVO.getTotalReturn(),"#,##0.00"));
			
			}
		}
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * IFA查看表现最好或最差的5个投资组合
	 * @author mqzou 2016-12-21
	 */
	@RequestMapping(value = "/getTop5Best", method = RequestMethod.POST)
	public void getTop5(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String currency=request.getParameter("currency");
		String type=request.getParameter("type");
		String statistic=request.getParameter("statistic");
		String langCode=getLoginLangFlag(request);
		jsonPaging=new JsonPaging();
		jsonPaging.setRows(5);
		if("best".equals(type)){
			jsonPaging.setOrder("desc");
		}else {
			jsonPaging.setOrder("asc");
		}
		if(null!=statistic && !"".equals(statistic))
		    jsonPaging.setSort(" r.increase");
		else {
			jsonPaging.setSort(" h.totalReturnRate");
		}
		String periodCode="";
		if(null!=statistic && !"".equals(statistic)){
			periodCode=CommonConstantsWeb.WEB_RETURN_PERIOD_CODE_PRE+statistic;
		}
		if(null==currency || "".equals(currency)){
			currency=ssoVO.getDefCurrency();
		}
		if(null==currency || "".equals(currency)){
			currency=CommonConstants.DEF_CURRENCY;
		}
		
		jsonPaging=ifaInfoService.findCustomerTopPortfolio(jsonPaging, ssoVO.getIfaId(), periodCode, currency,langCode);
		/*if(null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				Object object = (Object) it.next();
				
			}
		}*/
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	
	
	
	
	
	@RequestMapping(value = "/ifainsight", method = RequestMethod.GET)
	public String ifaInsight(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/ifaInsight";
	}

	@RequestMapping(value = "/ifaword", method = RequestMethod.GET)
	public String ifaWord(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/ifaWord";
	}

	@RequestMapping(value = "/ifaMyzone", method = RequestMethod.GET)
	public String ifaMyzone(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/ifamyzone";
	}

	/**
	 * @author xczhao
	 * @tips：funds fundsscreener以后放回新的目录
	 */
	@RequestMapping(value = "/fundsscreener", method = RequestMethod.GET)
	public String fundsscreener(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String lang = this.getLoginLangFlag(request);
		MemberBase member = getLoginUser(request);
		try {
			List<GeneralDataVO> currencyList = findSysParameters("currency_type", lang);
			model.put("currencyList", currencyList);
			List<GeneralDataVO> allocationList = findSysParameters("region", lang);
			model.put("allocationList", allocationList);
			List<GeneralDataVO> fundtypeList = findSysParameters("fund_type", lang);
			model.put("fundtypeList", fundtypeList);
			List<GeneralDataVO> sectorList = findSysParameters("fund_sector", lang);
			model.put("sectorList", sectorList);
			List<GeneralDataVO> fundsizeList = findSysParameters("criteria_fund_size", lang);
			model.put("fundsizeList", fundsizeList);
			List<GeneralDataVO> issuedateList = findSysParameters("criteria_fund_issue_date", lang);
			model.put("issuedateList", issuedateList);
			List<GeneralDataVO> mgtfeeList = findSysParameters("criteria_fund_mgt_fee", lang);
			model.put("mgtfeeList", mgtfeeList);
			List<GeneralDataVO> mininitialList = findSysParameters("criteria_fund_min_initial_amount", lang);
			model.put("mininitialList", mininitialList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			List<MemberDistributor> distributorList = distributorOrgService.findAllDistributors();
			model.put("distributorList", distributorList);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
			model.put("fundHouseList", fundHouseList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			List<WebQueryCriteria> criteriaList = webQueryCriteriaService.findMyCriteriaList( member.getId());
			model.put("criteriaList", criteriaList);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "ifa/fundsscreener";
	}

	@RequestMapping(value = "/fundsreturnstat", method = RequestMethod.GET)
	public String fundsReturnStat(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		JsonPaging jsonPaging = this.getJsonPaging(request);
		jsonPaging = fundReturnService.findPerformanceStatList(jsonPaging);
		this.toJsonString(response, jsonPaging);
		return null;
	}
	
	@RequestMapping(value = "/fundstotal", method = RequestMethod.POST)
	public String fundsTotal(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		String lang = this.getLoginLangFlag(request);
		JsonPaging jsonPaging = this.getJsonPaging(request);
        jsonPaging = fundInfoService.findFundInfoCount(jsonPaging, lang);
        
//		this.toJsonString(response, jsonPaging);
		
		Map<String, Object> statuts = new HashMap<String, Object>();
		//statuts.put("page",jsonPaging.getPage());
		statuts.put("total",jsonPaging.getTotal());
		//statuts.put("records",record);
//		statuts.put("rows",jsonPaging.getList());
		JsonUtil.toWriter(statuts, response);
		
		return null;
	}

	@RequestMapping(value = "/fundscount", method = RequestMethod.POST)
	public String fundsCount(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		JsonPaging jsonPaging = queryFundsByFilters(request,model,"count");
		jsonPaging.setList(null);
		this.toJsonString(response, jsonPaging);
		return null;
	}
	
	
	/**
	 * 
	 * @param request
	 * @param model
	 * @param dataFlag  all/count
	 * @return
	 */
    private JsonPaging queryFundsByFilters(HttpServletRequest request, ModelMap model,String dataFlag)
    {
    	FundScreenerDataVO filters = new FundScreenerDataVO();
    	String lang = this.getLoginLangFlag(request);
//        String fundSizeFrom = request.getParameter("fundSizeFrom");
//        String fundSizeTo = request.getParameter("fundSizeTo");

        String distributor = toUTF8String(request.getParameter("distributor"));
        String currency = toUTF8String(request.getParameter("currency"));
        String fundType = toUTF8String(request.getParameter("fundType"));
        String sectorType = toUTF8String(request.getParameter("sectorType"));
        String geoAllocation = toUTF8String(request.getParameter("geoAllocation"));
        String riskLevel = request.getParameter("riskLevel");  
        
        String trailingYTD = request.getParameter("trailingYTD");
        if( null != trailingYTD && !"".equals(trailingYTD) ){
        	String[] arrList = trailingYTD.split(",");
        	if(arrList.length>1){
	        	filters.setPerfYTDFrom(arrList[0]);
	            filters.setPerfYTDTo(arrList[1]);
        	}
        }
        
        String trailing1Mon = request.getParameter("trailing1Mon");
        if( null != trailing1Mon && !"".equals(trailing1Mon) ){
        	String[] arrList = trailing1Mon.split(",");
        	if(arrList.length>1){
        		filters.setPerfOneMonthFrom(arrList[0]);
        		filters.setPerfOneMonthTo(arrList[1]);
        	}
        }
        
        String trailing3Mon = request.getParameter("trailing3Mon");
        if( null != trailing3Mon && !"".equals(trailing3Mon) ){
        	String[] arrList = trailing3Mon.split(",");
        	if(arrList.length>1){
        		filters.setPerfThreeMonthFrom(arrList[0]);
        		filters.setPerfThreeMonthTo(arrList[1]);
        	}
        }
        
        String trailing6Mon = request.getParameter("trailing6Mon");
        if( null != trailing6Mon && !"".equals(trailing6Mon) ){
        	String[] arrList = trailing6Mon.split(",");
        	if(arrList.length>1){
        		filters.setPerfSixMonthFrom(arrList[0]);
        		filters.setPerfSixMonthTo(arrList[1]);
        	}
        }
        
        String trailing1Year = request.getParameter("trailing1Year");
        if( null != trailing1Year && !"".equals(trailing1Year) ){
        	String[] arrList = trailing1Year.split(",");
        	if(arrList.length>1){
        		filters.setPerfOneYearFrom(arrList[0]);
        		filters.setPerfOneYearTo(arrList[1]);
        	}
        }
        
        String trailing3Year = request.getParameter("trailing3Year");
        if( null != trailing3Year && !"".equals(trailing3Year) ){
        	String[] arrList = trailing3Year.split(",");
        	if(arrList.length>1){
        		filters.setPerfThreeYearFrom(arrList[0]);
        		filters.setPerfThreeYearTo(arrList[1]);
        	}
        }
        
        String trailing5Year = request.getParameter("trailing5Year");
        if( null != trailing5Year && !"".equals(trailing5Year) ){
        	String[] arrList = trailing5Year.split(",");
        	if(arrList.length>1){
        		filters.setPerfFiveYearFrom(arrList[0]);
        		filters.setPerfFiveYearTo(arrList[1]);
        	}
        }
        
        String sinceLaunch = request.getParameter("sinceLaunch");
        if( null != sinceLaunch && !"".equals(sinceLaunch) ){
        	String[] arrList = sinceLaunch.split(",");
        	if(arrList.length>1){
        		filters.setPerfLaunchFrom(arrList[0]);
        		filters.setPerfLaunchTo(arrList[1]);
        	}
        }
        
                 
//        filters.setMinInitialInv(request.getParameter("minInitialInv"));
//        filters.setLang(this.getLoginLangFlag(request));
        filters.setLangCode(lang);
        
        filters.setLoginUser(this.getLoginUser(request));
        
        String mgtFee = request.getParameter("mgtFee");
        if( null != mgtFee && !"".equals(mgtFee) ){
        	String mgtFeeFrom = "";
        	String mgtFeeTo = "";
        	if("criteria_fund_mgt_fee_02".equalsIgnoreCase(mgtFee)){
            	mgtFeeTo = "1.0";
        	}else if ("criteria_fund_mgt_fee_03".equalsIgnoreCase(mgtFee)) {
            	mgtFeeTo = "1.25";
			}else if ("criteria_fund_mgt_fee_04".equalsIgnoreCase(mgtFee)) {
            	mgtFeeTo = "1.50";
			}else if ("criteria_fund_mgt_fee_05".equalsIgnoreCase(mgtFee)) {
            	mgtFeeTo = "1.75";
			}else if ("criteria_fund_mgt_fee_06".equalsIgnoreCase(mgtFee)) {
            	mgtFeeTo = "2.0";
			}else if ("criteria_fund_mgt_fee_07".equalsIgnoreCase(mgtFee)) {
        		mgtFeeFrom = "2.0";
			}

            filters.setMgtFeeFrom( mgtFeeFrom);
            filters.setMgtFeeTo( mgtFeeTo);
        }
        
        String fundSize = request.getParameter("fundSize");
        if( null != fundSize && !"".equals(fundSize) ){
        	String fundSizeFrom = "";
        	String fundSizeTo = "";
        	if("criteria_fund_size_01".equalsIgnoreCase(fundSize)){
        		fundSizeTo = "10";
        	}else if ("criteria_fund_size_02".equalsIgnoreCase(fundSize)) {
        		fundSizeFrom = "10";
        		fundSizeTo = "30";
			}else if ("criteria_fund_size_03".equalsIgnoreCase(fundSize)) {
				fundSizeFrom = "30";
        		fundSizeTo = "50";
			}else if ("criteria_fund_size_04".equalsIgnoreCase(fundSize)) {
				fundSizeFrom = "50";
        		fundSizeTo = "100";
			}else if ("criteria_fund_size_05".equalsIgnoreCase(fundSize)) {
				fundSizeFrom = "100";
			}

            filters.setFundSizeFrom(fundSizeFrom);
            filters.setFundSizeTo(fundSizeTo);
        }
        
        String minInitialInv = request.getParameter("minInitialInv");
        if( null != minInitialInv && !"".equals(minInitialInv) ){
        	String minInitialInvFrom = "";
        	String minInitialInvTo = "";
        	if("criteria_fund_min_initial_amount_01".equalsIgnoreCase(minInitialInv)){
        		minInitialInvFrom = "0";
        		minInitialInvTo = "0";
        	}else if ("criteria_fund_min_initial_amount_02".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "0";
        		minInitialInvTo = "2000";
			}else if ("criteria_fund_min_initial_amount_03".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "2000";
        		minInitialInvTo = "4000";
			}else if ("criteria_fund_min_initial_amount_04".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "4000";
        		minInitialInvTo = "6000";
			}else if ("criteria_fund_min_initial_amount_05".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "6000";
        		minInitialInvTo = "8000";
			}else if ("criteria_fund_min_initial_amount_06".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "8000";
        		minInitialInvTo = "10000";
			}else if ("criteria_fund_min_initial_amount_07".equalsIgnoreCase(minInitialInv)) {
        		minInitialInvFrom = "10000";
        		minInitialInvTo = "";
			}

            filters.setMinInitialInvFrom( minInitialInvFrom);
            filters.setMinInitialInvTo( minInitialInvTo);
        }
        
        String inceptionDate = request.getParameter("inceptionDate");
        if( null != inceptionDate && !"".equals(inceptionDate) ){
        	String inceptionDateFrom = "";
        	String inceptionDateTo = "";
        	if("criteria_fund_issue_date_01".equalsIgnoreCase(inceptionDate)){
        		inceptionDateFrom = "1";
        		inceptionDateTo = "";
        	}else if ("criteria_fund_issue_date_02".equalsIgnoreCase(inceptionDate)) {
        		inceptionDateFrom = "5";
        		inceptionDateTo = "1";
			}else if ("criteria_fund_issue_date_03".equalsIgnoreCase(inceptionDate)) {
				inceptionDateFrom = "10";
        		inceptionDateTo = "5";
			}else if ("criteria_fund_issue_date_04".equalsIgnoreCase(inceptionDate)) {
				inceptionDateFrom = "";
        		inceptionDateTo = "10";
			}

            filters.setInceptionDateFrom(inceptionDateFrom);
            filters.setInceptionDateTo(inceptionDateTo);
        }

        filters.setCurrency(currency);
        filters.setFundType(fundType);
        filters.setSectorType(sectorType);
        filters.setGeoAllocation(geoAllocation);
        filters.setRiskRating(riskLevel);
        filters.setFundHouseIds(request.getParameter("fundHouseIds")==null?"":request.getParameter("fundHouseIds"));
        filters.setDistributor(distributor);
        filters.setKeyword(request.getParameter("keyword"));
                
        model.put("filters", filters);

        
        JsonPaging jsonPaging = this.getJsonPaging(request);

        if("riskLevel".equals(jsonPaging.getSort())){
        	jsonPaging.setSort("f.riskLevel");
        }
        jsonPaging = fundInfoService.findFundInfoListData(jsonPaging, filters,dataFlag);
        
        return jsonPaging;
    }
	
	/**
	 * @author zxtan
	 * @tips：funds fundslist
	 */
	@RequestMapping(value = "/fundslist", method = RequestMethod.GET)
	public String fundslist(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String lang = this.getLoginLangFlag(request);
		MemberBase member = this.getLoginUser(request);
		jsonPaging = this.getJsonPaging(request);
		try {
			List<GeneralDataVO> currencyList = findSysParameters("currency_type", lang);
			model.put("currencyList", currencyList);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		try {
			List<WebQueryCriteria> criteriaList = webQueryCriteriaService.findMyCriteriaList( member.getId());
			model.put("criteriaList", criteriaList);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		String displayColor = "";
		//MemberBase loginUser = getLoginUser(request);
		if(null != member) displayColor = member.getDefDisplayColor();
		if(null==displayColor || "".equals(displayColor)){
		    displayColor=CommonConstants.DEF_DISPLAY_COLOR;
		}
		model.put("displayColor", displayColor);
		model.put("defCurrency", member.getDefCurrency());
		
		return "ifa/fundslist";
	}
	
	/**
	 * @author zxtan
	 * @tips：funds queryCriteriaList
	 */
	@RequestMapping(value = "/queryCriteriaList", method = RequestMethod.GET)
	public String queryCriteriaList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		
		MemberBase member = this.getLoginUser(request);
		List<WebQueryCriteria> criteriaList = webQueryCriteriaService.findMyCriteriaList( member.getId());
		
		
		Map<String, Object> statuts = new HashMap<String, Object>();
		//statuts.put("page",jsonPaging.getPage());
		statuts.put("flag", true);
		statuts.put("rows",criteriaList);
		JsonUtil.toWriter(statuts, response);
		return null;
	}
    
	
	/**
	 * @author zxtan
	 * @tips：funds queryCriteria
	 */
	@RequestMapping(value = "/queryCriteria", method = RequestMethod.GET)
	public String queryCriteria(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		
		String criteriaId = request.getParameter("criteriaId");
		WebQueryCriteria obj = webQueryCriteriaService.findById(criteriaId);
		
//		Map<String, Object> statuts = new HashMap<String, Object>();
//		//statuts.put("page",jsonPaging.getPage());
//		statuts.put("result",obj);
		JsonUtil.toWriter(obj, response);
		return null;
	}
	
	
	@RequestMapping(value = "/saveQueryCriteria", method = RequestMethod.POST)
	public String saveQueryCriteria(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		MemberBase member = this.getLoginUser(request);
		String criteriaId = request.getParameter("criteriaId");
		String description = request.getParameter("description");
		String type = request.getParameter("type");
		
				
		WebQueryCriteria obj = webQueryCriteriaService.findById(criteriaId);
		if(null == obj){
			String criteria = request.getParameter("criteria");
			try {
				criteria = UrlUtil.decode(criteria);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			obj = new WebQueryCriteria();
			obj.setCriteria(criteria);
			obj.setDataType("fund");
			obj.setMember(member);
		}
		
		obj.setDescription(description);
		obj.setCreateDate(new Date());
		obj.setType(type);
		
		WebQueryCriteria objNew = webQueryCriteriaService.saveOrUpdate(obj);
		
//		Map<String, Object> statuts = new HashMap<String, Object>();
//		//statuts.put("page",jsonPaging.getPage());
//		statuts.put("result",obj);
		JsonUtil.toWriter(objNew, response);
		return null;
	}
	
	@RequestMapping(value = "/deleteQueryCriteria", method = RequestMethod.POST)
	public String deleteQueryCriteria(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
//		MemberBase member = this.getLoginUser(request);
		String criteriaId = request.getParameter("criteriaId");
		
				
		Boolean flag = webQueryCriteriaService.deleteById(criteriaId);
		
		
		Map<String, Object> statuts = new HashMap<String, Object>();
		//statuts.put("page",jsonPaging.getPage());
		statuts.put("result",flag);
		JsonUtil.toWriter(statuts, response);
		return null;
	}
	
	
	@RequestMapping(value = "/criteriaNameList", method = RequestMethod.GET)
	public String getCriteriaNameList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){		
		String langCode = this.getLoginLangFlag(request);
		String codeList = request.getParameter("codeList");
		String criteriaType = request.getParameter("criteriaType");
		
		String nameList = "";
		
		if("paramConfig".equalsIgnoreCase(criteriaType)){
			nameList = sysParamService.findNameByCode(codeList, langCode);
		}else if ("distributor".equalsIgnoreCase(criteriaType)) {
			nameList = memberDistributorService.getNameList(codeList);
		}else if ("fundHouse".equalsIgnoreCase(criteriaType)) {
			nameList = fundHouseService.getNameList(codeList, langCode);
		}
		
				
		Map<String, Object> result = new HashMap<String, Object>();
		//statuts.put("page",jsonPaging.getPage());
		result.put("nameList",nameList);
		JsonUtil.toWriter(result, response);
		return null;
	}
	
	
	
    /**
     * 分页获得记录for ajax Json 调用
     * 	  与fundsscreener()一样
     * @author zxtan
     * 
     */
    @RequestMapping(value = "/getFundListJson", method = RequestMethod.POST)
    public void getFundListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	jsonPaging = queryFundsByFilters(request, model,"all");
        
        
        String lang = this.getLoginLangFlag(request);
        List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
        //this.toJsonString(response, jsonPaging);
        model.put("jsonPaging", jsonPaging);
//        model.put("filters", filters);
        model.put("fundHouseList_in", fundHouseList);
        
        ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
    }
	

	/***
	 * 我收藏的策略
	 * 
	 * @author 林文伟
	 * @date 2016-09-28
	 */
	@RequestMapping(value = "/myfavorites", method = RequestMethod.GET)
	public String myFavorites(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取登录用户信息
		MemberBase curMember = this.getLoginUser(request);
		String lang = this.getLoginLangFlag(request);
		if(null!=curMember)
		{
			String defDisplayColor = curMember.getDefDisplayColor();
			String defFormatDate = curMember.getDateFormat();
			if(null==defFormatDate || "".equals(defFormatDate)){
				defFormatDate=CommonConstants.FORMAT_DATE;
			}
			String fromMemberId = curMember.getId();
			// 获取收藏策略列表
			List<MyFavoritesStrategy> myFavoritesStrategyList = ifaInfoService.loadIFAMyFavoritesStrategyList(fromMemberId, lang, defFormatDate, null,lang);
			model.put("myFavoritesStrategyList", myFavoritesStrategyList);
			// 获取收藏组合列表
			List<MyFavoritesPortfolios> myFavoritesPortfoliosList = ifaInfoService.loadIFAMyFavoritesPortfoliosList(fromMemberId, null,lang);
			model.put("myFavoritesPortfoliosList", myFavoritesPortfoliosList);
			model.put("defDisplayColor", defDisplayColor);
			model.put("defFormatDate", defFormatDate);
	
			List<WebWatchlistType> list=webWatchListService.findAllType(curMember.getId());
			model.put("WebWatchlistTypeList", list);
			
			return "ifa/myFavorites";
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/**
	 * @author xczhao
	 * @tips：my ifa
	 */
	@RequestMapping(value = "/myifa", method = RequestMethod.GET)
	public String myIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/myIfa";
	}

	/**
	 * @author xczhao
	 * @tips：my buddy modify by mqzou 2016-09-27
	 */
	@RequestMapping(value = "/mybuddy", method = RequestMethod.GET)
	public String myBuddy(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase memberBase = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		if (null != memberBase) {
			WebFriend webFriend = new WebFriend();
			//webFriend.setFromMember(memberBase);

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
						vo.setNickName(ifa.getMember().getNickName());
						focusList.add(vo);
					}

				}

			}*/
			model.put("focusList", list);

			model.put("newBuddy", newList);
			model.put("myBuddy", myBuddyList);
			
			List colList=webChatService.findColleagueForIfa(ssoVO.getIfaId(),langCode);
			model.put("teamList", colList);
			
			return "ifa/myBuddy";
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}

	}
	
	/**
	 * @author mqzou 2016-11-15
	 * @tips：删除好友
	 */
	@RequestMapping(value = "/deleteBuddy", method = RequestMethod.POST)
	public void deleteBuddy(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id=request.getParameter("id");
		MemberBase loginUser=getLoginUser(request);
		Map<String, Object> result=new HashMap<String, Object>();
		if(null!=loginUser){
			WebFriend friend=webFriendService.findWebFriendById(id);
			if(null!=friend){
				friend.setCheckResult("3");//删除状态
				friend= webFriendService.updateWebFriend(friend);
				if(null!=friend){
					result.put("result", true);
				}else {
					result.put("result", false);
				}
			}
			else {
				result.put("result", false);
			}
		}
		else {
			result.put("result", false);
		}
		
		
		JsonUtil.toWriter(result, response);
	}

	/**
	 * @author mqzou
	 * @tips：myBuddy处理（接受，拒绝）
	 */
	@RequestMapping(value = "/dealWithBuddy", method = RequestMethod.POST)
	public void dealWithBuddy(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		//String webtodoid = request.getParameter("todoid");////如果是通过待办点击过来的，则同时更新待办 if modify please say to linwenwei
		WebFriend webFriend = webFriendService.findWebFriendById(id);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (null != webFriend) {
			webFriend.setCheckResult(status);
			webFriend.setLastUpdate(new Date());
			WebFriend vo = webFriendService.updateWebFriend(webFriend);
			
			if (null != vo) {
				
				//更新所有的相关待办
				webReadToDoService.updateReadTodoReaded(CommonConstantsWeb.WEB_READ_MODULE_FRIENDS, vo.getFromMember().getId(), vo.getToMember().getId());
				/*if(StringUtils.isNotBlank(webtodoid)){ //如果是通过待办点击过来的，则同时更新待办
					webReadToDoService.updateReadToDoReaded(webtodoid);
				}*/
				if("1".equals(status)){
					WebFriend friend=new WebFriend();
					friend.setFromMember(vo.getToMember());
					friend.setToMember(vo.getFromMember());
					friend.setCheckResult(vo.getCheckResult());
					friend.setCreateTime(DateUtil.getCurDate());
					friend.setRelationships(vo.getRelationships());
					friend.setCheckTime(DateUtil.getCurDate());
				
					
					vo=webFriendService.updateWebFriend(friend);
				}
				
				obj.put("result", true);
			} else {
				obj.put("result", true);
			}
		} else {
			obj.put("result", true);
		}
		JsonUtil.toWriter(obj, response);
	}

	/**
	 * @author xczhao
	 * @tips：投资策略,先放在ifa,以后放回新的目录
	 */
	@RequestMapping(value = "/strategies", method = RequestMethod.GET)
	public String Strategies(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/strategies";
	}

	/**
	 * @author xczhao
	 * @tips：投资策略详细页面,先放在ifa,以后放回新的目录
	 *//*
	@RequestMapping(value = "/strategiesdetail", method = RequestMethod.GET)
	public String Strategiesdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/strategiesdetail";
	}
*/
	/**
	 * @author xczhao
	 * @tips：投资组合,先放在ifa,以后放回新的目录
	 */
	@RequestMapping(value = "/porfolios", method = RequestMethod.GET)
	public String Porfolios(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/porfolios";
	}

	/**
	 * @author xczhao
	 * @tips：投资组合详细页面,先放在ifa,以后放回新的目录
	 */
	@RequestMapping(value = "/porfoliosdetail", method = RequestMethod.GET)
	public String Porfoliosdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/porfoliosdetail";
	}

	/**
	 * @author xczhao
	 * @tips：my pipeline,先放在ifa,以后放回新的目录
	 */
	@RequestMapping(value = "/mypipeline", method = RequestMethod.GET)
	public String myPipeline(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase memberBase = getLoginUser(request);
		if (null != memberBase) {
			return "ifa/temp/myPipeline";
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/**
	 * @author xczhao
	 * @tips：report,先放在ifa,以后放回新的目录
	 */
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public String report(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase memberBase = getLoginUser(request);
		if (null != memberBase) {
			return "ifa/temp/report";
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public String demo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/temp/demo";
	}

	/**
	 * @author tejay zhu IFA的推荐基金列表
	 */
	@RequestMapping(value = "/ifarecommendfund", method = RequestMethod.GET)
	public String ifarecommendfund(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String lang = this.getLoginLangFlag(request);
		if (loginUser != null) {
			try {
				// 地区列表
				List<GeneralDataVO> itemList = findSysParameters("region", this.getLoginLangFlag(request));
				model.put("regionList", itemList);

				// 主题分类
				List<GeneralDataVO> sectorList = findSysParameters("fund_sector", this.getLoginLangFlag(request));
				model.put("sectorList", sectorList);

				// 基金公司分类列表
				List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(lang);
				model.put("fundHouseList", fundHouseList);
				
				//List<FundInfoDataVO> fundInfoList = new ArrayList<FundInfoDataVO>();
				//基金信息
		        List<GeneralDataVO> houseList = new ArrayList<GeneralDataVO>();
		        houseList = fundInfoService.findFundHouseList(this.getLoginLangFlag(request));
		        model.put("houseList", houseList);
		        
		        String color=loginUser.getDefDisplayColor();
		        if(null==color || "".equals(color)){
		        	color=CommonConstants.DEF_DISPLAY_COLOR;
		        }
		        model.put("displayColor", color);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "ifa/recommend/myRecommendFund";
		} else {
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}
	/**
	 * 删除推荐信息
	 * 
	 * @author mqzou 2017-04-18
	 */
	@RequestMapping(value = "/deleteRecommend", method = RequestMethod.POST)
	public void deleteRecommend(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		HashMap<String, Object> result=new HashMap<String, Object>();
		MemberBase loginUser=getLoginUser(request);
		String id=request.getParameter("id");
		WebRecommended recommended=ifaInfoService.findRecommendedById(id);
		if(null!=recommended && loginUser.getId().equals(recommended.getCreator().getId())){
			ifaInfoService.deleteWebRecommend(recommended);
			result.put("result", true);
		}else {
		    result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
		
	}

	/**
	 * 分页获得基金信息 ajax 接口
	 * 
	 * @author tejay zhu
	 */
	@RequestMapping(value = "/getifarecommendfundlistJson", method = RequestMethod.POST)
	public void getIfaRecommendFundListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String period = request.getParameter("period");
		String sector = request.getParameter("sector");
		String geo = request.getParameter("region");
		String langCode = getLoginLangFlag(request);
		String startDate = "";// DateUtil.getCurDateStr();
		String endDate = DateUtil.getCurDateStr();
		if (null != period && !"".equals(period)) {
			if ("1W".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
			else if ("1M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
			else if ("2M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -2);
			else if ("3M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
			else if ("6M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
			else if ("1Y".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
			else {
				String[] dates = period.split("to");
				if (null != dates && dates.length > 0) {
					startDate = dates[0].trim();
					endDate = dates[1].trim();
				}
			}
		}
		List<WebRecommended> list = ifaInfoService.getIfaRecommendFundIds(loginUser, startDate, endDate, sector, geo, langCode);
		String recommendFundIds = this.listRecommendFundIdToString(list, ",");
		if (!"".equals(recommendFundIds)){
			jsonPaging = queryFundsByFilters(request, recommendFundIds, model);

			List dataList=jsonPaging.getList();
			if(null!=dataList && !dataList.isEmpty()){
				Iterator it= dataList.iterator();
				while (it.hasNext()) {
					FundInfoDataVO vo = (FundInfoDataVO) it.next();
					RecommendInfoVO recommendInfoVO=ifaSpaceService.getRecommendInfo(loginUser.getId(), vo.getFundId(), "fund");
					vo.setRecommendInfo(recommendInfoVO);
				}
			}
			List<FundHouseDataVO> fundHouseList = fundInfoService.loadFundHouseList(langCode);
			model.put("jsonPaging", jsonPaging);
			model.put("fundHouseList_in", fundHouseList);
		}else {
			jsonPaging=new JsonPaging();
		}
			

		ResponseUtils.renderHtml(response, JsonUtil.toJson(jsonPaging));
	}
	
	/**
	 * 推荐基金置顶
	 * 
	 * @author mqzou 2016-10-19
	 */
	@RequestMapping(value = "/recommendFundOverhead", method = RequestMethod.POST)
	public void recommendFundOverhead(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		String id = request.getParameter("id");
		String overhead = request.getParameter("overhead");
		boolean b = ifaSpaceService.checkRecommendOverhead(loginUser.getId(), id, overhead, "fund");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", b);
		JsonUtil.toWriter(result, response);
	}

	private String listRecommendFundIdToString(List<WebRecommended> list, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).getRelateId()).append(separator);
		}

		if (sb.toString().length() < 1) {
			return sb.toString();
		} else {
			return sb.toString().substring(0, sb.toString().length() - 1);
		}
	}
    
    private JsonPaging queryFundsByFilters(HttpServletRequest request, String recommendFundIds, ModelMap model){
    	FundScreenerDataVO filters = new FundScreenerDataVO();
    	
    	/*System.out.println( "" );System.out.println( "" );
		System.out.println( "** queryFundsByFilters keyword:\n:"+(request.getParameter("keyword")) );
		System.out.println( "" );System.out.println( "" );*/

        String keyword = toUTF8String(request.getParameter("keyword"));
        String fundName = toUTF8String(request.getParameter("fundName"));
        String fundSizeFrom = request.getParameter("fundSizeFrom");
        String fundSizeTo = request.getParameter("fundSizeTo");
        String domicile = toUTF8String(request.getParameter("domicile"));
        String currency = toUTF8String(request.getParameter("currency"));
        String fundType = toUTF8String(request.getParameter("fundType"));
        String sectorType = toUTF8String(request.getParameter("sectorType"));
        String geoAllocation = toUTF8String(request.getParameter("geoAllocation"));
        String riskLevel = request.getParameter("riskLevel");
        String perfLaunchFrom = request.getParameter("perfLaunchFrom");
        
        String perfLaunchTo = request.getParameter("perfLaunchTo");
        
        filters.setFundID( recommendFundIds );
        filters.setPerfYTDFrom(request.getParameter("perfYTDFrom"));
        filters.setPerfYTDTo(request.getParameter("perfYTDTo"));
        filters.setPerfOneWeekFrom(request.getParameter("perfOneWeekFrom"));
        filters.setPerfOneWeekTo(request.getParameter("perfOneWeekTo"));
        filters.setPerfOneMonthFrom(request.getParameter("perfOneMonthFrom"));
        filters.setPerfOneMonthTo(request.getParameter("perfOneMonthTo"));
        filters.setPerfThreeMonthFrom(request.getParameter("perfThreeMonthFrom"));
        filters.setPerfThreeMonthTo(request.getParameter("perfThreeMonthTo"));
        filters.setPerfSixMonthFrom(request.getParameter("perfSixMonthFrom"));
        filters.setPerfSixMonthTo(request.getParameter("perfSixMonthTo"));
        filters.setPerfOneYearFrom(request.getParameter("perfOneYearFrom"));
        filters.setPerfOneYearTo(request.getParameter("perfOneYearTo"));
        filters.setPerfThreeYearFrom(request.getParameter("perfThreeYearFrom"));
        filters.setPerfThreeYearTo(request.getParameter("perfThreeYearTo"));
        filters.setPerfThreeYearFrom(request.getParameter("perfFiveYearFrom"));
        filters.setPerfThreeYearTo(request.getParameter("perfFiveYearTo"));
        
        filters.setBeforeYears(request.getParameter("beforeYears"));//获取的年度回报数量
        filters.setBeforeYears("5");
//        filters.setLipperCR_ID( request.getParameter("lipperCR_ID"));
        filters.setRatingOrg("1");
        filters.setLipperCr(request.getParameter("lipperCR"));
        
//        filters.setFitch_ID(request.getParameter("fitch_ID"));
        filters.setRatingOrg("2");
        filters.setFitch( request.getParameter("fitch"));
        
//        filters.setCitywire_ID( request.getParameter("citywire_ID"));
        filters.setRatingOrg("3");
        filters.setCitywire( request.getParameter("citywire"));
        
        filters.setMinInitialInv(request.getParameter("minInitialInv"));
        filters.setMgtFee( request.getParameter("mgtFee"));
//        filters.setLang(this.getLoginLangFlag(request));
        filters.setLangCode(request.getParameter("langCode"));
        
       // filters.setLoginUser(this.getLoginUser(request));
        filters.setKeyword(keyword);
        filters.setFundName(fundName);
        filters.setFundSizeFrom(fundSizeFrom);
        filters.setFundSizeTo(fundSizeTo);
        filters.setDomicile(domicile);
        filters.setCurrency(currency);
        filters.setFundType(fundType);
        filters.setSectorType(sectorType);
        filters.setGeoAllocation(geoAllocation);
        filters.setRiskRating(riskLevel);
        filters.setPerfLaunchFrom(perfLaunchFrom);
        filters.setPerfLaunchTo(perfLaunchTo);
        filters.setFundHouseIds(request.getParameter("fundHouseIds")==null?"":request.getParameter("fundHouseIds"));
        model.put("filters", filters);


		jsonPaging = this.getJsonPaging(request);
		if ("riskLevel".equals(jsonPaging.getSort())) {
			jsonPaging.setSort("f.riskLevel");
		}
		jsonPaging = fundInfoService.findFundInfoList(jsonPaging, filters);

		return jsonPaging;
	}
    
    /**
	 * 获取基金的基本信息
	 * 
	 * @author mqzou 2017-04-18
	 */
	@RequestMapping(value = "/getFundSimpleInfo", method = RequestMethod.POST)
    public void getFundSimpleInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	String id=request.getParameter("id");
    	MemberBase loginUser=getLoginUser(request);
    	String langCode=getLoginLangFlag(request);
    	FundBasicDataVO vo=fundInfoService.getFundBasicDataMess(id, langCode, loginUser.getId(), null);
    	JsonUtil.toWriter(vo, response);
    }

	/**
	 * 基金推荐详细信息页面
	 * 
	 * @author tejay zhu
	 */
	@RequestMapping(value = "/ifaRecommendFundsdetail", method = RequestMethod.GET)
	public String ifaRecommendFundsdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		List<GeneralDataVO> fundFeesList = null;
		List<GeneralDataVO> fundDocsList = null;
		List<GeneralDataVO> fundBonusList = null;
		List<GeneralDataVO> fundAnnoList = null;
		List<GeneralDataVO> fundCumReturnList = null;
		List<GeneralDataVO> fundYearReturnList = null;

		String toCurrency = request.getParameter("toCurrency");
		String fundId = request.getParameter("id");
		model.put("id", fundId);
		FundInfoDataVO fundInfoVO = fundInfoService.findFundFullInfoById(fundId, this.getLoginLangFlag(request), loginUser == null ? null : loginUser.getId(), toCurrency);
		model.put("fundInfoVO", fundInfoVO);

		fundFeesList = fundInfoService.findFundFees(fundId, this.getLoginLangFlag(request));
		model.put("fundFeesList", fundFeesList);
		fundDocsList = fundInfoService.findFundDocs(fundId, this.getLoginLangFlag(request));
		model.put("fundDocsList", fundDocsList);
		fundBonusList = fundInfoService.findFundBonus(fundId);
		model.put("fundBonusList", fundBonusList);
		fundAnnoList = fundInfoService.findFundAnnouncement(fundId, this.getLoginLangFlag(request));
		model.put("fundAnnoList", fundAnnoList);

		// 累积表现
		fundCumReturnList = fundInfoService.findFundReturnList(fundId, "heap", "", this.getLoginLangFlag(request));
		model.put("fundCumReturnList", fundCumReturnList);

		// 年度表现
		fundYearReturnList = fundInfoService.findFundReturnList(fundId, "year", "", this.getLoginLangFlag(request));
		model.put("fundYearReturnList", fundYearReturnList);

		return "ifa/ifaRecommendFundsdetail";
	}

	/**
	 * 更新Ifa的基金推荐信息
	 * 
	 * @author tejay zhu
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * */
	@RequestMapping(value = "/updateWebRecommended", method = RequestMethod.POST)
	public void updateWebRecommended(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		String langCode = getLoginLangFlag(request);
		if (loginUser != null) {
			String id = request.getParameter("id");
			String content = request.getParameter("content");
			String pushBuddy=request.getParameter("pushBuddy");
			String pushClient=request.getParameter("pushClient");
			String pushColleague=request.getParameter("pushColleague");
			String pushProspect=request.getParameter("pushProspect");
			String pushClients=request.getParameter("pushClients");
			String pushProspects=request.getParameter("pushProspects");
			String pushBuddies=request.getParameter("pushBuddies");
			String pushColleagues=request.getParameter("pushColleagues");
			String push=request.getParameter("scopeFlag");
			

			WebRecommended webRecommended = ifaInfoService.getIfaRecommendFund(loginUser, id);
			if (null != webRecommended) {
				webRecommended.setRecommendTime(new Date());
			} else {
				webRecommended=new WebRecommended();
				webRecommended.setCreator(loginUser);
				webRecommended.setRecommendTime(DateUtil.getCurDate());
				webRecommended.setRelateId(id);
				webRecommended.setModuleType(CommonConstantsWeb.WEB_RECOMMENDED_MODULE_TYPE_FUND);
			}
			webRecommended.setReason(content);
			
			if("2".equals(push)){//all
				 pushBuddy="1";
				 pushClient="1";
				 pushProspect="1";
	    		 pushColleague="1";
	       	}else if("1".equals(push)){//none
		       	 pushBuddy="0";
				 pushClient="0";
				 pushProspect="0";
	   		     pushColleague="0";
	       	}

			if ("0".equals(pushBuddy)) {
				pushBuddies = "";
			} else if ("1".equals(pushBuddy) && "".equals(pushBuddies)) {
				List<IfaClientVO> userList = webFriendService.findMyFriendList(loginUser.getId());
				pushBuddies = getIds(userList);
			}

			if ("0".equals(pushClient)) {
				pushClients = "";
			} else if (pushClient.equals("1") && "".equals(pushClients)) {
				List<IfaClientVO> userList = ifaInfoService.findMyClientList(loginUser.getId(), "1",langCode);
				pushClients = getIds(userList);
			}
			if ("0".equals(pushProspect)) {
				pushProspects = "";
			} else if (pushProspect.equals("1") && "".equals(pushProspects)) {
				List<IfaClientVO> userList = ifaInfoService.findMyClientList(loginUser.getId(), "0",langCode);
				pushProspects = getIds(userList);
			}
			if ("0".equals(pushColleague)) {
				pushColleagues = "";
			} else if ("1".equals(pushColleague) && "".equals(pushColleagues)) {
				List<IfaClientVO> userList = ifaInfoService.findMyColleagueList(loginUser.getId(), CommonConstants.DEF_LANG_CODE);
				pushColleagues = getIds(userList);
			}
			
			WebPush webPush = new WebPush();
	   		StrategyWebPushVO webPushVO = ifaSpaceService.findWebPushByNews(id,CommonConstantsWeb.WEB_PUSH_MODULE_FUND);
	   		if (null!=webPushVO && null!=webPushVO.getId() && !"".equals(webPushVO.getId())){
	   			webPush = strategyInfoService.findWebPushById(webPushVO.getId());
	   		}else{//新建
	       		
	       		webPush.setRelateId(id);
	       		webPush.setCreateTime(new Date());
	       		webPush.setFromMember(loginUser);
	   		}
	   		webPush.setModuleType(CommonConstantsWeb.WEB_PUSH_MODULE_FUND);
	   		webPush.setScopeFlag(push);
	   		webPush.setLastUpdate(new Date());
	   		webPush.setBuddyFlag(pushBuddy);
      		webPush.setClientFlag(pushClient);
      		webPush.setColleagueFlag(pushColleague);
      		webPush.setProspectFlag(pushProspect);
	       	
	       	
	       	webPush = strategyInfoService.saveWebPush(webPush,pushClients,pushProspects,pushBuddies,pushColleagues);
	       	webRecommended=ifaInfoService.updateWebRecommended(webRecommended);
	     // 发送消息提醒
	       	saveTodoRead(id, loginUser, pushClients);
	       	saveTodoRead(id, loginUser, pushProspects);
	       	saveTodoRead(id, loginUser, pushBuddies);
	       	saveTodoRead(id, loginUser, pushColleagues);
	       	
	       	result.put("result", true);
			result.put("code", "global.success.save");
			result.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success.save", null));
	       	
		} else {// 未登录，跳转到登录页面
			result.put("result", true);
			result.put("code", "error.noLogin");
			result.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "error.noLogin", null));
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 检查基金是否已推荐
	 * @author mqzou  2017-06-16
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/checkFundRecommend",method=RequestMethod.POST)
	public void checkFundRecommend(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> result=new HashMap<String, Object>();
		String id=request.getParameter("id");
		MemberBase loginUser=getLoginUser(request);
		WebRecommended webRecommended = ifaInfoService.getIfaRecommendFund(loginUser, id);
		if(null!=webRecommended){
			result.put("hasRecommend", true);
		}else {
			result.put("hasRecommend", false);
		}
		JsonUtil.toWriter(result, response);
		
	}
	
	/**
	 * 发送消息提醒
	 * @author mqzou
	 * @data 2017-02-21
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	private void saveTodoRead(String fundId,MemberBase loginUser, String clientsDetail) {
		if (StringUtils.isNotBlank(clientsDetail)) {
			Object[] idArrStrings = null;
			if(clientsDetail.indexOf(",") > -1){
				idArrStrings = clientsDetail.split(",");
			}else{
				idArrStrings = new Object[1];
				idArrStrings[0] = clientsDetail;
			}
			if(idArrStrings != null && idArrStrings.length > 1){
				Set<Object> idSet = new TreeSet<Object>();
				for (int i = 0; i < idArrStrings.length; i++) {
					idSet.add(idArrStrings[i]);
				}
				idArrStrings = idSet.toArray();
			}
		    if (idArrStrings!=null && idArrStrings.length>0){
		    	for(int k=0;k<idArrStrings.length;k++){
		    		String id = idArrStrings[k].toString();
		    		if(id!=null && id.length()>0){
		    			WebReadToDo webReadToDo = new WebReadToDo();
					 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_NORMAL);
					 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_FUND);
					 	webReadToDo.setRelateId(fundId);
					 	webReadToDo.setFromMember(loginUser);
					 	webReadToDo.setMsgUrl("/front/fund/info/fundsdetail.do");
					 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.fund.activity.FundDetailActivity");
					 	webReadToDo.setAppParam("fundId="+fundId);
					 	webReadToDo.setMsgParam("id="+fundId);
					 	webReadToDo.setIsApp("0");
					 	MemberBase memberBase = memberBaseService.findById(id);
					 	webReadToDo.setOwner(memberBase);
					 	webReadToDo.setCreateTime(new Date());
					 	webReadToDo.setIsValid("1");
					 	List<Object> params = new ArrayList<Object>();
					 	MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
					 	params.add(ifa.getMember().getNickName());
					 	
					 	FundInfoEn en=fundInfoService.findFundInfoEnById(fundId);
					 	params.add(en.getFundName());
					 	String titleEn = PropertyUtils.getSmsPropertyValue("fund.info.push",CommonConstants.LANG_CODE_EN,params.toArray());
					 	 params = new ArrayList<Object>();
					 	params.add(ifa.getMember().getNickName());
					 	FundInfoSc sc=fundInfoService.findFundInfoScById(fundId);
					 	params.add(sc.getFundName());
						String titleSc = PropertyUtils.getSmsPropertyValue("fund.info.push",CommonConstants.LANG_CODE_SC,params.toArray());
						params = new ArrayList<Object>();
						params.add(ifa.getMember().getNickName());
					 	FundInfoTc tc=fundInfoService.findFundInfoTcById(fundId);
					 	params.add(tc.getFundName());
					 	String titleTc = PropertyUtils.getSmsPropertyValue("fund.info.push",CommonConstants.LANG_CODE_TC,params.toArray());
					 	
					 	webReadToDo.setTitle(titleEn);
					 	webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
					 	
		    		}
		    	}
		    }
		}
	}	
	
	 private String getIds(List<IfaClientVO> list){
	    	List<String> ids=new ArrayList<String>();
	    	if(null!=list && !list.isEmpty()){
	    		Iterator<IfaClientVO> it=list.iterator();
	    		while (it.hasNext()) {
					IfaClientVO ifaClientVO = (IfaClientVO) it.next();
					ids.add(ifaClientVO.getId());
				}
	    	}
	    	return StrUtils.arrayListToString(ids, ",");
	    }

	@RequestMapping(value = "/testslider")
	public String testslider(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifa/testslider";
	}

	/********************************** 我收藏的自选、策略、组合 *******************************************/
	/**
	 * 分页获得记录，供前台调用
	 * 
	 * @author 林文伟
	 * 
	 */
	@RequestMapping(value = "/getMyWatchlistJson", method = RequestMethod.POST)
	public void getMyWatchlistJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase curMember = this.getLoginUser(request);
		String fromMemberId = curMember.getId();
		String toCurrency = request.getParameter("toCurrency");
		String typeId = request.getParameter("typeId");
		toCurrency = "USD";
		List<MyFavoritesWatchingTypeVOList> watchingTypeVOList = ifaInfoService.loadIFAMyFavoritesWatchingList(this.getLoginLangFlag(request), fromMemberId, toCurrency,typeId);
		ResponseUtils.renderHtml(response, JsonUtil.toJson(watchingTypeVOList));
		// ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
	}

	/**
	 * @author 林文伟 通过类型删除自选基金列表
	 */
	@RequestMapping(value = "/delMyWatchlist", method = RequestMethod.POST)
	public void delMyWatchlist(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String typeId = request.getParameter("typeId");
		MemberBase curMember = this.getLoginUser(request);
		String fromMemberId = curMember.getId();
		Boolean rs = ifaInfoService.delMyWatchlist(fromMemberId, typeId);

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
	 * @author 林文伟 更新自选基金列表
	 */
	@RequestMapping(value = "/saveMyWatchlistRemark", method = RequestMethod.POST)
	public void saveMyWatchlistRemark(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		String remark = request.getParameter("remark");
		Boolean rs = ifaInfoService.saveMyWatchlistRemark(id, remark);

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
	 * @author 林文伟 更新自选基金类型的名称
	 */
	@RequestMapping(value = "/modifyMyWatchTypeName", method = RequestMethod.POST)
	public void modifyMyWatchTypeName(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String typeId = request.getParameter("typeId");
		String typeName = request.getParameter("typeName");
		Boolean rs = ifaInfoService.modifyMyWatchTypeName(typeId, typeName);

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

	/***************************************** 发现、圈子分享功能 ********************************************/

	/**
	 * 发现图子列表，根据登录人员类型进行筛选
	 * 1、如果登录的是IFA，则可以查看该IFA所属的公司的该公司所有IFA圈子
	 * 2、如果登录的是investor，则查看是自己好友的IFA圈子
	 * @author 林文伟
	 * 
	 */
	@RequestMapping(value = "/discover", method = RequestMethod.GET)
	public String discover(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	this.saveLastVisitUrl(request, response);
		//需要根据登录
		String langCode = this.getLoginLangFlag(request);
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			String memberId = loginUser.getId();
			String defFormatDate = loginUser.getDateFormat();
			model.put("memberType", loginUser.getMemberType());
			//
			WebReadToDo dealsOne = new  WebReadToDo();
			List<WebReadToDo> dealsList = ifaInfoService.findWaitWebReadTodo(memberId, "1", langCode);
			model.put("dealsOneCount", dealsList.size());
			if(null!=dealsList&&!dealsList.isEmpty()){
				dealsOne = dealsList.get(0);
				//转换时间格式
				 Date favoritesTime = dealsOne.getCreateTime();
				 //dealsOne.setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr2(favoritesTime,langCode,defFormatDate));
				 
				 if(StringUtils.isNotBlank(defFormatDate)){
					 dealsOne.setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr2(favoritesTime,langCode,defFormatDate));
				 } else{
						dealsOne.setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr2(favoritesTime,langCode,CoreConstants.DATE_FORMAT));
				 }
			}

			model.put("dealsOne", dealsOne);
			
			WebReadToDo notesOne = new  WebReadToDo();
			List<WebReadToDo> notesList = ifaInfoService.findWaitWebReadTodo(memberId, "2", langCode);
			model.put("notesOneCount", notesList.size());
			if(null!=notesList&&!notesList.isEmpty()){
			//if(notesList.size()>0){
				notesOne = notesList.get(0);
				//转换时间格式
				Date favoritesTime = notesOne.getCreateTime();
				if(StringUtils.isNotBlank(defFormatDate)){
					notesOne.setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr2(favoritesTime,langCode,defFormatDate));
				} else{
					notesOne.setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr2(favoritesTime,langCode,CoreConstants.DATE_FORMAT));
				}
			}
			model.put("notesOne", notesOne);
			
			WebReadToDo alertOne = new  WebReadToDo();
			List<WebReadToDo> alertsList = ifaInfoService.findWaitWebReadTodo(memberId, "3", langCode);
			model.put("alertsOneCount", alertsList.size());
			if(null!=alertsList&&!alertsList.isEmpty()){
			//if(alertsList.size()>0){
				alertOne = alertsList.get(0);
				//转换时间格式
				Date favoritesTime = alertOne.getCreateTime();
				//alertOne.setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr2(favoritesTime,langCode,defFormatDate));
				if(StringUtils.isNotBlank(defFormatDate)){
					alertOne.setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr2(favoritesTime,langCode,defFormatDate));
				 } else{
					alertOne.setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr2(favoritesTime,langCode,CoreConstants.DATE_FORMAT));
				 }
			}
			model.put("alertsOne", alertOne);
			
			List<CornerInfo> cornerInfoList = ifaInfoService.findCornerInfoList(loginUser,langCode);// 圈子分享信息
			model.put("cornerInfoList", cornerInfoList);

			return "ifa/discover/discover";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
		
		
	}
	
	/**
	 * @author mqzou 2017-02-22
	 * @tips：获取所有的未读消息数
	 */
	@RequestMapping(value = "/getAllUnreadCount")
	public void getAllUnreadCount(HttpServletRequest request, HttpServletResponse response, ModelMap mode){
		String langCode = this.getLoginLangFlag(request);
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			List<WebReadToDo> dealsList = ifaInfoService.findWaitWebReadTodo(loginUser.getId(), "1", langCode);
			List<WebReadToDo> notesList = ifaInfoService.findWaitWebReadTodo(loginUser.getId(), "2", langCode);
			List<WebReadToDo> alertsList = ifaInfoService.findWaitWebReadTodo(loginUser.getId(), "3", langCode);
			int count=dealsList.size()+notesList.size()+alertsList.size();
			Map<String, Object> result=new HashMap<String, Object>();
			result.put("count", count);
			JsonUtil.toWriter(result, response);
		}
	}

	/**
	 * @author linwenwei
	 * @tips：显示待阅信息、圈子分享 信息等
	 */
	@RequestMapping(value = "/discoverMsg", method = RequestMethod.GET)
	public String discoverMsg(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			//String memberId = loginUser.getId();
			String typeCode = request.getParameter("typeCode");
			//String status = request.getParameter("status");
			//String dateformat = loginUser.getDateFormat();
//			List<WebReadToDo> webReadToDoList = ifaInfoService.findWebReadTodo(memberId, typeCode, status,langCode);// 待阅信息
//			if(null!=webReadToDoList&&!webReadToDoList.isEmpty()){
//				for(int i=0;i<webReadToDoList.size();i++){
//					Date createTime = webReadToDoList.get(i).getCreateTime();
//					webReadToDoList.get(i).setCreateTimeFmt(DateUtil.getDateTimeGoneFormatStr(createTime, langCode, dateformat));
//				}
//			}
//
//			model.put("webReadToDoList", webReadToDoList);
			model.put("typeCode", typeCode);
			return "ifa/discover/discoverMsg";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}
	
	/**
	 * 获取系统消息json信息
	 * @author wwlin     
	 * */
	@RequestMapping(value = "/discoverMsgJson", method = RequestMethod.POST)
	public void discoverMsgJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase baseMem = this.getLoginUser(request);
		//String defFormatDate = baseMem.getDateFormat();
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		String typeCode = request.getParameter("typecode");
		String readStatus = request.getParameter("status");
		String keyWord = request.getParameter("keyWord");

		jsonPaging = ifaInfoService.findDiscoverMsgForWeb(jsonPaging, baseMem, typeCode, readStatus, langCode,keyWord);
		this.toJsonString(response, jsonPaging);
	}

	/**
	 * @author 林文伟
	 * @tips：查看某个IFA的所有分享主题列表，每个主题有点赞评论等各方 面信息
	 */
	@RequestMapping(value = "/cornerDetail", method = RequestMethod.GET)
	public String connerDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String memberId = request.getParameter("memberId");
		String langCode = this.getLoginLangFlag(request);
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			// 个人最新观点

			IfaCorner ifaCorner = ifaInfoService.findIfaCornerInfo(memberId,langCode);
			// 拆分出来主题
			List<IfaCornerInfoDetailVO> ifaCornerInfoDetailVOList = ifaCorner.getIfaCornerInfoDetailVOList();

			model.put("ifaCorner", ifaCorner);
			model.put("ifaCornerInfoDetailVOList", ifaCornerInfoDetailVOList);
			model.put("loginUser", loginUser);
			return "ifa/discover/cornerDetail";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/**
	 * @author 林文伟 评论圈子主题
	 */
	@RequestMapping(value = "/replyCornerInfo", method = RequestMethod.POST)
	public void replyCornerInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String cornerId = request.getParameter("cornerId");
		MemberBase loginUser = getLoginUser(request);
		String memberId = loginUser.getId();
		String content = request.getParameter("content");
		//对图标进行转换
		content = parseEmotion(content,request);
		Boolean rs = ifaInfoService.replyCornerInfo(cornerId, memberId, content);

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
	
	public String parseEmotion(String content,HttpServletRequest request){ 
		String newContent = content.replace("[FACE1001]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/pcmoren_huaixiao_thumb.png\" />");
		newContent = newContent.replace("[FACE1002]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/pcmoren_tian_thumb.png\" />");
		newContent = newContent.replace("[FACE1003]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/pcmoren_wu_thumb.png\" />");
		newContent = newContent.replace("[FACE1004]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/huanglianwx_thumb.gif\" />");
		newContent = newContent.replace("[FACE1005]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/tootha_thumb.gif\" />");
		newContent = newContent.replace("[FACE1006]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/laugh.gif\" />");
		newContent = newContent.replace("[FACE1007]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/tza_thumb.gif\" />");
		newContent = newContent.replace("[FACE1008]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/kl_thumb.gif\" />");
		newContent = newContent.replace("[FACE1009]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/wabi_thumb.gif\" />");
		newContent = newContent.replace("[FACE1010]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/cj_thumb.gif\" />");
		newContent = newContent.replace("[FACE1011]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/shamea_thumb.gif\" />");
		newContent = newContent.replace("[FACE1012]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/zy_thumb.gif\" />");
		newContent = newContent.replace("[FACE1013]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/bz_thumb.gif\" />");
		newContent = newContent.replace("[FACE1014]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/bs2_thumb.gif\" />");
		newContent = newContent.replace("[FACE1015]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/lovea_thumb.gif\" />");
		newContent = newContent.replace("[FACE1016]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/sada_thumb.gif\" />");
		newContent = newContent.replace("[FACE1017]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/heia_thumb.gif\" />");
		newContent = newContent.replace("[FACE1018]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/qq_thumb.gif\" />");
		newContent = newContent.replace("[FACE1019]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/sb_thumb.gif\" />");
		newContent = newContent.replace("[FACE1020]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/mb_thumb.gif\" />");
		newContent = newContent.replace("[FACE1021]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/landeln_thumb.gif\" />");
		newContent = newContent.replace("[FACE1022]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/yhh_thumb.gif\" />");
		newContent = newContent.replace("[FACE1023]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/zhh_thumb.gif\" />");
		newContent = newContent.replace("[FACE1024]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/x_thumb.gif\" />");
		newContent = newContent.replace("[FACE1025]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/cry.gif\" />");
		newContent = newContent.replace("[FACE1026]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/wq_thumb.gif\" />");
		newContent = newContent.replace("[FACE1027]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/t_thumb.gif\" />");
		newContent = newContent.replace("[FACE1028]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/haqianv2_thumb.gif\" />");
		newContent = newContent.replace("[FACE1029]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/bba_thumb.gif\" />");
		newContent = newContent.replace("[FACE1030]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/angrya_thumb.gif\" />");
		newContent = newContent.replace("[FACE1031]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/yw_thumb.gif\" />");
		newContent = newContent.replace("[FACE1032]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/cza_thumb.gif\" />");
		newContent = newContent.replace("[FACE1033]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/88_thumb.gif\" />");
		newContent = newContent.replace("[FACE1034]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/sk_thumb.gif\" />");
		newContent = newContent.replace("[FACE1035]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/sweata_thumb.gif\" />");
		newContent = newContent.replace("[FACE1036]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/kunv2_thumb.gif\" />");
		newContent = newContent.replace("[FACE1037]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/huangliansj_thumb.gif\" />");
		newContent = newContent.replace("[FACE1038]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/money_thumb.gif\" />");
		newContent = newContent.replace("[FACE1039]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/sw_thumb.gif\" />");
		newContent = newContent.replace("[FACE1040]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/cool_thumb.gif\" />");
		newContent = newContent.replace("[FACE1041]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/huanglianse_thumb.gif\" />");
		newContent = newContent.replace("[FACE1042]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/hatea_thumb.gif\" />");
		newContent = newContent.replace("[FACE1043]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/gza_thumb.gif\" />");
		newContent = newContent.replace("[FACE1044]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/dizzya_thumb.gif\" />");
		newContent = newContent.replace("[FACE1045]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/bs_thumb.gif\" />");
		newContent = newContent.replace("[FACE1046]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/crazya_thumb.gif\" />");
		newContent = newContent.replace("[FACE1047]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/h_thumb.gif\" />");
		newContent = newContent.replace("[FACE1048]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/yx_thumb.gif\" />");
		newContent = newContent.replace("[FACE1049]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/numav2_thumb.gif\" />");
		newContent = newContent.replace("[FACE1050]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/hufen_thumb.gif\" />");
		newContent = newContent.replace("[FACE1051]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/hearta_thumb.gif\" />");
		newContent = newContent.replace("[FACE1052]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/unheart.gif\" />");
		newContent = newContent.replace("[FACE1053]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/pig.gif\" />");
		newContent = newContent.replace("[FACE1054]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/panda_thumb.gif\" />");
		newContent = newContent.replace("[FACE1055]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/rabbit_thumb.gif\" />");
		newContent = newContent.replace("[FACE1056]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/ok_thumb.gif\" />");
		newContent = newContent.replace("[FACE1057]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/ye_thumb.gif\" />");
		newContent = newContent.replace("[FACE1058]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/good_thumb.gif\" />");
		newContent = newContent.replace("[FACE1059]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/buyao_org.gif\" />");
		newContent = newContent.replace("[FACE1060]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/z2_thumb.gif\" />");
		newContent = newContent.replace("[FACE1061]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/come_thumb.gif\" />");
		newContent = newContent.replace("[FACE1062]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/sad_thumb.gif\" />");
		newContent = newContent.replace("[FACE1063]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/shenshou_thumb.gif\" />");
		newContent = newContent.replace("[FACE1064]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/horse2_thumb.gif\" />");
		newContent = newContent.replace("[FACE1065]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/j_thumb.gif\" />");
		newContent = newContent.replace("[FACE1066]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/fuyun_thumb.gif\" />");
		newContent = newContent.replace("[FACE1067]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/geiliv2_thumb.gif\" />");
		newContent = newContent.replace("[FACE1068]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/wg_thumb.gif\" />");
		newContent = newContent.replace("[FACE1069]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/vw_thumb.gif\" />");
		newContent = newContent.replace("[FACE1070]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/huatongv2_thumb.gif\" />");
		newContent = newContent.replace("[FACE1071]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/lazhuv2_thumb.gif\" />");
		newContent = newContent.replace("[FACE1072]", "<img class=\"wmes-emotion\" src=\""+request.getContextPath()+"/res/images/cornericon/cakev2_thumb.gif\" />");
		return newContent;
	}

	/**
	 * @author 林文伟点赞圈子主题
	 */
	@RequestMapping(value = "/likedCornerInfo", method = RequestMethod.POST)
	public void likedCornerInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String cornerId = request.getParameter("cornerId");
		MemberBase loginUser = getLoginUser(request);
		String memberId = loginUser.getId();

		Boolean rs = ifaInfoService.likedCornerInfo(cornerId, memberId);

		Map<String, Object> obj = new HashMap<String, Object>();
		if (rs) {
			obj.put("result", true);
			obj.put("nickname", loginUser.getNickName());
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success", null));
		} else {
			obj.put("result", false);
			obj.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.failed", null));
		}

		ResponseUtils.renderHtml(response, JsonUtil.toJson(obj));
	}
	
	/**
	 * @author 林文伟
	 * @tips：分享主题【从新闻等其它地方进行操作】
	 */
	@RequestMapping(value = "/shareInfo", method = RequestMethod.POST)
	public void shareInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String url = request.getParameter("url");
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			String memberId = loginUser.getId();
			Boolean rs = ifaInfoService.shareInfo( memberId, title, content, url);

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
	}

	/**
	 * @author 林文伟
	 * @tips：转载圈子主题
	 */
	@RequestMapping(value = "/reprintCornerInfo", method = RequestMethod.POST)
	public void reprintCornerInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String cornerId = request.getParameter("cornerId");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String cornerAuthorId = request.getParameter("cornerAuthorId");
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			String memberId = loginUser.getId();
			String url = this.getFullPath(request) + "front/ifa/info/connerDetail.do?memberId=" + cornerAuthorId;
			Boolean rs = ifaInfoService.reprintCornerInfo(cornerId, memberId, title, content, url);

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
	}

	/**
	 * @author 林文伟
	 * @tips：discove
	 */
	@RequestMapping(value = "/cornerInfoForm2", method = RequestMethod.GET)
	public String cornerInfoForm2(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//return "ifa/discover/cornerInfoForm";
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			return "ifa/discover/cornerInfoForm";
		}
		else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}
	
	/**
	 * @author 林文伟
	 * @tips：discove
	 */
	@RequestMapping(value = "/cornerForm", method = RequestMethod.GET)
	public String cornerInfoForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//return "ifa/discover/cornerInfoForm";
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			return "ifa/discover/cornerForm";
		}
		else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}
	
	/**
	 * @author 林文伟
	 * @tips：跳转论坛
	 */
	@RequestMapping(value = "/bbs", method = RequestMethod.GET)
	public String bbs(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		//return "ifa/discover/cornerInfoForm";
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			Cookie cookie = new Cookie("jforumUserInfo", loginUser.getLoginCode());
			cookie.setMaxAge(-1); // -1为永不过期,或者指定过期时间
			cookie.setPath("/");
			response.addCookie( cookie );
			return "ifa/discover/bbs";
		}
		else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}
	
	/**
	 * @author 林文伟
	 * @throws ParseException 
	 * @tips：discove
	 */
	@RequestMapping(value = "/facebook", method = RequestMethod.GET)
	public String facebook(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ParseException {
		return "ifa/discover/facebook";
	}
	
	/**
	 * @author 林文伟
	 * @tips：发表圈子主题
	 */
	@RequestMapping(value = "/saveCornerInfo", method = RequestMethod.POST)
	public void saveCornerInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		content = content.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"");

		String imgpath = request.getParameter("imgpath");
		MemberBase loginUser = getLoginUser(request);
		if (loginUser != null) {
			String memberId = loginUser.getId();
			Boolean rs = ifaInfoService.saveCornerInfo( memberId, title, content,imgpath);

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
	}
	
	/**
	 * 上传插件DEMO
	 * 
	 * @author 林文伟
	 * 
	 */
	@RequestMapping(value = "/uploadDemo", method = RequestMethod.GET)
	public String uploadDemo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		/*Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -8);
		Date lastDate = calendar.getTime();
		String a1 = DateUtil.getDateStr(lastDate);
		calendar.add(calendar.DATE, 1);
		Date lastDate1 = calendar.getTime();
		String a11 = DateUtil.getDateStr(lastDate1);
		calendar.add(calendar.DATE, 2);
		Date lastDate2 = calendar.getTime();
		String a111 = DateUtil.getDateStr(lastDate2);
		//List<CornerInfo> cornerInfoList = ifaInfoService.findCornerInfoList();// 圈子分享信息 List<CoreFundNavAlignVO> getFundNavAlign
		//model.put("cornerInfoList", cornerInfoList);
		List<CoreFundNavAlignVO> list = coreFundServiceImpl.getFundNavAlign("d4a9c3a7b51046b19363473602858b2d,6d5f7b07474a4fddbf3c83e937735af6", "1Y", "");
		//model.put("list", list);
*/		return "ifa/discover/upload_demo";
	}
	
	/**
	 * @author 测试
	 */
	@RequestMapping(value = "/getCorefundNavList", method = RequestMethod.GET)
	public void getCorefundNavList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		/*String typeId = request.getParameter("typeId");
		String typeName = request.getParameter("typeName");

		Map<String, Object> obj = new HashMap<String, Object>();*/
		//List<CoreFundNavVO> list = coreFundServiceImpl.getFundNav("ddf4b0a227164acaa51ab51c7b3632b2","YTD","");
		//List<CoreFundNavVO> list = coreFundServiceImpl.getMoreFundReturnRateForAPP("STOCK_02838,STOCK_02801,STOCK_03009,BOND_XS0521073428,FUND_LU0094541447,FUND_IE0001148372,FUND_LU0140363002,FUND_LU0251130802,","0.0425,0.042,0.0328,0.003,0.2928,0.4147,0.0719,0.1133,", "3M","tc");
		CoreFundsReturnForChartsVO list = coreFundServiceImpl.getMoreFundReturnRate("ddf4b0a227164acaa51ab51c7b3632b2,579ded9dec95411dbb9ff80b76394784","0.5,0.5", "3M","tc");
		//List<CoreFundsReturnForChartsVO> list = coreFundServiceImpl.getMulFundReturnRate("ddf4b0a227164acaa51ab51c7b3632b2,579ded9dec95411dbb9ff80b76394784", "3Y","","tc");
		//List<CorePortfolioVO> list = corePortfolioService.findArenaReturnRate("808080405bc7d6d2015bcc23025800e5", "LAUNCH","");
		//List<CorePortfolioVO> list = corePortfolioService.findArenaReturnRate(portfolioId, frequencyType,"");
		//int length = list.size();
		//CorePortfolioVO vo = list.get(length-1);
		//把数据
		//609ac4eeac7147e99aa5d0ab6d91f890 579ded9dec95411dbb9ff80b76394784 d4a9c3a7b51046b19363473602858b2d
		//6d5f7b07474a4fddbf3c83e937735af6  80b7559ccf8e41c5bf7c2f2d9320612c
		JsonUtil.toWriter(list, response);
		//ResponseUtils.renderHtml(response, JsonUtil.toJson(list));
	}
	
	/**
	 * @author mqzou 2016-12-01
	 * @tips：客户沟通备忘
	 */
	@RequestMapping(value = "/communicationRecord")
	public String communicationRecord(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String dateFormat=loginUser.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat))
			dateFormat=CommonConstants.FORMAT_DATE;
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		MemberIfa ifa=memberBaseService.findIfaMember(loginUser.getId());
		String memberId=request.getParameter("memberId");
		if(null!=memberId && !"".equals(memberId)){
			CrmCustomer customer=crmCustomerService.findByIfaAndMember(ssoVO.getIfaId(), memberId);
			if(null!=customer)
				model.put("customer", customer);
			else {
				model.put("notfind", "1");
			}
		}
		jsonPaging=new JsonPaging();
    	jsonPaging.setRows(100);
    	jsonPaging.setPage(1);
    	jsonPaging=crmCustomerService.findCustomerByIfa(jsonPaging, ifa);
    	List<CrmSelectVO> list=new ArrayList<CrmSelectVO>();
    	Iterator it=jsonPaging.getList().iterator();
    	while (it.hasNext()) {
			CrmCustomer crm = (CrmCustomer) it.next();
			CrmSelectVO vo=new CrmSelectVO();
			if(null!=crm.getNickName() && ""!=crm.getNickName()){
				vo.setNickName(crm.getNickName());
			}else {
				vo.setNickName(crm.getMember().getNickName());
			}
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
    	
    	model.put("dateFormat", dateFormat.replace("yyyy", "yy"));
		return "ifa/crm/communicationRecord";
	}
	
	/**
	 * @author mqzou 2016-12-01
	 * @tips：客户沟通备忘记录列表
	 */
	@RequestMapping(value = "/communicationRecordJson")
	public void findCommunicationRecord(HttpServletRequest request, HttpServletResponse response, ModelMap mode){
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		jsonPaging=getJsonPaging(request);
		String period=request.getParameter("period");
		String clientName=request.getParameter("clientName");
		String keyWord=request.getParameter("keyWord");
		if(null !=keyWord && !"".equals(keyWord))
			keyWord=toUTF8String(keyWord);
		String startDate = "";// DateUtil.getCurDateStr();
		String endDate = DateUtil.getCurDateStr(Calendar.DATE, 1);
		if (null != period && !"".equals(period)) {
			if ("1W".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
			else if ("2W".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.DATE, -14);
			else if ("1M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
			else if ("2M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -2);
			else if ("3M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
			else if ("6M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
			else if ("1Y".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
			else if ("3Y".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
			else if ("5Y".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
			else if ("YTD".equals(period))
				startDate = DateUtil.dateToDateString(DateUtil.getCurrYearFirst(), DateUtil.DEFAULT_DATE_FORMAT);
			else {
				String[] dates = period.split("to");
				if (null != dates && dates.length ==2) {
					startDate = dates[0].trim();
					endDate = dates[1].trim();
				}
			}
		}
		
		if("undefined".equals(startDate))
			startDate="";
		if("undefined".equals(endDate))
			endDate="";
		if(!"".equals(endDate)){
			endDate=endDate+" 23:59:59";
		}
		
		jsonPaging=ifaInfoService.findCommunicationRecord(jsonPaging, ssoVO.getIfaId(), startDate, endDate, clientName,keyWord);
		ResponseUtils.renderHtml(response, JsonUtil.toJson(jsonPaging));
	}
	
	
	/**
	 * @author mqzou 2016-12-01
	 * @tips：保存客户沟通备忘
	 */
	@RequestMapping(value = "/saveMemo")
	public void saveMemo(HttpServletRequest request, HttpServletResponse response, ModelMap mode){
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String id=request.getParameter("id");
		String content=request.getParameter("content");
		String imgStr=request.getParameter("imglist");
		String memberId=request.getParameter("memberId");
		Map<String, Object> result=new HashMap<String, Object>();
	    
		CrmMemo vo=new CrmMemo();
		if(null!=id && !"".equals(id))
			vo=crmMemoService.findById(id);
		else {
			
			MemberIfa ifa=memberBaseService.findIfaMember(ssoVO.getId());
			vo.setIfa(ifa);
			vo.setCreateTime(DateUtil.getCurDate());
		}
		String[] img=imgStr.split(",");
		List<String> imgList = java.util.Arrays.asList(img);
		MemberBase member=memberBaseService.findById(memberId);
		vo.setMember(member);
		if(null!=vo){
			vo.setMemoText(content);
			vo.setLastModify(DateUtil.getCurDate());
			//vo= crmMemoService.saveOrUpdate(vo);
			vo=crmMemoService.saveMemo(vo, imgList);
			CrmCustomer customer=crmCustomerService.findByIfaAndMember(ssoVO.getIfaId(), memberId);
			if(null!=customer && null!=customer.getSalesStageId()&& customer.getSalesStageId().equals(CommonConstantsWeb.PIPELINE_SALES_NEW)){
				customer.setSalesStageId(CommonConstantsWeb.PIPELINE_SALES_CONTACT);
				crmCustomerService.saveCustomer(customer);
			}
			result.put("result", true);
			result.put("id", vo.getId());
		}else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
		//ResponseUtils.renderHtml(response, JsonUtil.toJson(result));
	}
	
	/**
	 * @author mqzou 2016-12-02
	 * @tips：删除客户沟通备忘
	 */
	@RequestMapping(value = "/deleteMemo")
	public void deleteMemo(HttpServletRequest request, HttpServletResponse response, ModelMap mode){
		String id=request.getParameter("id");
		Map<String, Object> result=new HashMap<String, Object>();
		CrmMemo memo=crmMemoService.findById(id);
		if(null!=memo){
			if(crmMemoService.deleteCrmMemo(memo)){
				result.put("result", true);
			}else {
				result.put("result", false);
			}
		}else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * @author wwlin 2016-12-02
	 * @tips：删除我关注的web_follow信息
	 */
	@RequestMapping(value = "/deleteMyWebFollow")
	public void deleteMyWebFollow(HttpServletRequest request, HttpServletResponse response, ModelMap mode){
		String id=request.getParameter("id");
		Map<String, Object> result=new HashMap<String, Object>();
		Boolean rs=ifaInfoService.deleteMyWebFollow(id);
		if(rs){
			result.put("result", true);
		}else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
	}
	
	   /**
     * 选人页面
     * @author zxtan
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/userSelector", method = RequestMethod.GET)
    public String userSelector(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String userType = request.getParameter("userType");
		String memberIdsObj = request.getParameter("memberIdsObj");
		String memberNamesObj = request.getParameter("memberNamesObj");
		model.put("memberIdsObj", memberIdsObj);
		model.put("memberNamesObj", memberNamesObj);
		String langCode = getLoginLangFlag(request);
		
		if (loginUser!=null){
			//客户类型：Buddy， Client， Advisor， Prospect
			String memberId = loginUser.getId();
			List<IfaClientVO> userList;
			if("prospect".equalsIgnoreCase(userType)){
				userList = ifaInfoService.findMyClientList(memberId, "0",langCode);
			}else if("existing".equalsIgnoreCase(userType)){
				userList = ifaInfoService.findMyClientList(memberId, "1",langCode);
			}else if("client".equalsIgnoreCase(userType)){
				userList = ifaInfoService.findMyClientList(memberId, "all",langCode);
			}else if("buddy".equalsIgnoreCase(userType)){
				userList = webFriendService.findMyFriendList(memberId);
			}else if("colleague".equalsIgnoreCase(userType)){
				userList = ifaInfoService.findMyColleagueList(memberId, langCode);
			}else {
				userList = null;
			}
			
			model.put("clients", userList);
			return "ifa/selectNameList";
	        //return "strategy/info/userSelector";//页面摆放路径
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
	
    
	   /**
     * 选人页面
     * @author zxtan
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/userSelectorTest", method = RequestMethod.GET)
    public String userSelectorTest(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (loginUser!=null){

			return "ifa/selectNameListTest";
	        //return "strategy/info/userSelector";//页面摆放路径
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		

    }
    
	   /**
     * 选人页面
     * @author zxtan
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/selectUserListJson", method = RequestMethod.GET)
    public String selectUserListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String userType = request.getParameter("userType");
		String memberIdsObj = request.getParameter("memberIdsObj");
		String memberNamesObj = request.getParameter("memberNamesObj");
		model.put("memberIdsObj", memberIdsObj);
		model.put("memberNamesObj", memberNamesObj);
		String langCode = getLoginLangFlag(request);
		
		if (loginUser!=null){
			//客户类型：Buddy， Client， Advisor， Prospect
			String memberId = loginUser.getId();
			List<IfaClientVO> userList;
			if("prospect".equalsIgnoreCase(userType)){
				userList = ifaInfoService.findMyClientList(memberId,"0",langCode);
			}else if("existing".equalsIgnoreCase(userType)){
				userList = ifaInfoService.findMyClientList(memberId,"1",langCode);
			}else if("client".equalsIgnoreCase(userType)){
				userList = ifaInfoService.findMyClientList(memberId,"all",langCode);
			}else if("buddy".equalsIgnoreCase(userType)){
				userList = webFriendService.findMyFriendList(memberId);
			}else if("colleague".equalsIgnoreCase(userType)){
				userList = ifaInfoService.findMyColleagueList(memberId, langCode);
			}else {
				userList = null;
			}
			
	    	Map<String, Object> result = new HashMap<String, Object>();
	    	result.put("total",userList== null?0: userList.size());
	    	result.put("rows",userList);
			JsonUtil.toWriter(result, response);
			return null;
			
		}else{//未登录，跳转到登录页面
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
    }
    /**
     * 直播大厅
	 * @author xczhao
	 */
    @RequestMapping(value = "/liveHall", method = RequestMethod.GET)
	public String liveHall(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		return "ifa/discover/liveHall";
	}
    
    /**
     * 直播节目表
	 * @author xczhao
	 */
	@RequestMapping(value = "/liveSchedule", method = RequestMethod.GET)
	public String liveSchedule(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		return "ifa/discover/liveSchedule";
	}
	
	/**
	 * 我的直播节目表
	 * @author xczhao
	 */
	@RequestMapping(value = "/myLiveSchedule", method = RequestMethod.GET)
	public String myLiveSchedule(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		return "ifa/discover/myLiveSchedule";
	}
	
	/**
	 * @author 林文伟 删除系统信息
	 */
	@RequestMapping(value = "/delWebReadToDo", method = RequestMethod.POST)
	public void delWebReadToDo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String idList = request.getParameter("idlist");
		
		Boolean rs = false;
		String[] arrList = idList.split(",");
    	if(arrList.length>0){
    		for(int i=0;i<arrList.length;i++){
    			String id = arrList[i];
    			if(StringUtils.isNotBlank(id)){
    				rs = ifaInfoService.delWebReadToDo(id);
    			}
    		}
    	}

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
	 * @author 林文伟 批量更新系统信息读状态
	 */
	@RequestMapping(value = "/bitchDealMsgReadStatus", method = RequestMethod.POST)
	public void bitchDealMsgReadStatus(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String idList = request.getParameter("idlist");
		String status = request.getParameter("status");
		Boolean rs = false;
		String[] arrList = idList.split(",");
    	if(arrList.length>0){
    		for(int i=0;i<arrList.length;i++){
    			String id = arrList[i];
    			if(StringUtils.isNotBlank(id)){
    				rs = ifaInfoService.updateMsgReadStatus(id,status);
    			}
    		}
    	}

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
	
	
}
