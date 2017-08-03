package com.fsll.wmes.ifa.action.front;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.app.investor.discover.service.AppNewsInfoService;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.service.SysCountryService;
import com.fsll.core.service.SysParamService;
import com.fsll.core.vo.SysParamVO;
import com.fsll.oms.service.ITraderSendService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.IfaFeeItem;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.LiveScheduler;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebRecommended;
import com.fsll.wmes.entity.WfProcedureInstanseTodo;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.service.IfaManageService;
import com.fsll.wmes.ifa.service.IfaSpaceService;
import com.fsll.wmes.ifa.vo.IfaInfoVO;
import com.fsll.wmes.ifa.vo.IfaInvestorVO;
import com.fsll.wmes.ifa.vo.IfaSpaceFundVO;
import com.fsll.wmes.ifa.vo.IfaSpaceInsightVO;
import com.fsll.wmes.ifa.vo.IfaSpaceLiveVO;
import com.fsll.wmes.ifa.vo.IfaSpaceNewsVO;
import com.fsll.wmes.ifa.vo.IfaSpacePortfoliosVO;
import com.fsll.wmes.ifa.vo.IfaSpaceStrategyInfoVO;
import com.fsll.wmes.ifa.vo.IfaSpaceVO;
import com.fsll.wmes.ifa.vo.IfaSpaceVisitVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.AccountProgressVO;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.portfolio.service.PortfolioArenaService;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.CriteriaVO;
import com.fsll.wmes.strategy.vo.StrategyWebPushVO;
import com.fsll.wmes.web.service.WebFriendService;

/**
 * @author 林文伟
 * IFA空间操作类
 */

@Controller
@RequestMapping("/front/ifa/space")
public class IfaSpaceAct extends WmesBaseAct{
	@Autowired
	private IfaSpaceService ifaSpaceService;
	@Autowired
	private IfaInfoService ifaInfoService;
	@Autowired
	private InvestorService investorService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private AppNewsInfoService newsInfoService; 
	@Autowired
	private StrategyInfoService strategyInfoService;
	/*@Autowired
	private IfaInfoService ifaInfoService;
	@Autowired
	private FundInfoService fundInfoService;*/
	@Autowired
	private SysParamService paramService;
	@Autowired
	private SysCountryService countryService;;
	@Autowired
	private PortfolioArenaService portfolioArenaService;
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
	@Autowired
	private IfaManageService ifaManageService;
	
	@Autowired
	private WebFriendService webFriendService;
	
	/**
     * 显示IFA个人空间数据
     * @author 林文伟
     */
	@RequestMapping(value = "/ifaSpace", method = RequestMethod.GET)
	public String ifaSpace(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		this.isMobileDevice(request, model);
		String lang = this.getLoginLangFlag(request);
		String memberId = request.getParameter("id");
		model.put("memberId", memberId);
		// 获取当前登录者信息，用于判断当前登录会员是否空间者自己，用于策略的跳转，如果刚好是自己，则策略跳转到自己维护的界面，否则跳转列表
		Boolean isShowAddFriendBtn = true;//是否显示添加好友按钮，默认显示
		String loginMemberId = "";
		Boolean viewerIsSpaceOwner = false;
		MemberBase curMember = this.getLoginUser(request);
		String curDefCurrency = CommonConstants.DEF_CURRENCY;
		if(null != curMember){
			loginMemberId = curMember.getId();
			if (null != curMember.getMemberType()&&memberId.equals(curMember.getId())) {
				viewerIsSpaceOwner = true; // 访问的ID与登录者的ID一样，则表示访问空间者刚好是自己
				curDefCurrency = curMember.getDefCurrency();
				isShowAddFriendBtn = false;//自己访问自己，不需要显示添加好友按钮
			} else{
				//判断是否已添加该IFA为好友
				isShowAddFriendBtn = !webFriendService.checkTwoMemberIsFriend(loginMemberId,memberId);
             // 更新ifa人气值 同一用户每刷新一次页面，其人气值则+1，实际同一用户24小时内只计算一次,IFA访问自己空间，不应该增加人气
        		Boolean prs = ifaSpaceService.updateIfaPopularityTotal(memberId,loginMemberId);
        		if(prs){
        			//插入浏览记录
    				WebInvestorVisit investorVisit = new WebInvestorVisit();
    				investorVisit.setMember(curMember);
                    investorVisit.setModuleType("ifa");
                    investorVisit.setRelateId(memberId);
                    investorVisit.setVistiTime(new Date());
                    fundInfoService.saveWebInvestorVisit(investorVisit);
        		}
        		//判断该IFA与自己的匹配度
        		MemberBase ifa = memberBaseService.findById(memberId);
        		int matchNumber = ifaInfoService.getInvestorIfaMatchDegree(curMember,ifa);
        		model.put("matchNumber", matchNumber);
			}
			String defDisplayColor = curMember.getDefDisplayColor();
			model.put("defDisplayColor", defDisplayColor);
		}
		model.put("isShowAddFriendBtn", isShowAddFriendBtn);
		model.put("curDefCurrency", curDefCurrency);
		model.put("loginMemberId", loginMemberId);

		String regionId = request.getParameter("regionId");
		// String sectionId = request.getParameter("sectionId");
		if ("null".equals(regionId) || null == regionId)
			regionId = "";
		// if(null==regionId)regionId="";
		jsonPaging = this.getJsonPaging(request);
		//获取公用区域与板块数据
		List<SysParamVO> serviceRegionList =  sysParamService.getParamList(lang, "region");
		List<SysParamVO> sectionTypeList =  sysParamService.getParamList(lang, "fund_sector");//拿基金主题
		
		// 空间整个实体
		IfaSpaceVO spaceVO = ifaSpaceService.loadIfaSpaceData(loginMemberId,memberId,lang,regionId,curDefCurrency);
		MemberIfa memberIfa = memberBaseService.findIfaMember(memberId);   
	      if(memberIfa != null){
	    	  spaceVO.setIntroduction(memberIfa.getIntroduction());
		      String language = paramService.findNameByCode(memberIfa.getMember().getLanguageSpoken(), lang); 
		      spaceVO.setLanguageDesc(language);
		      spaceVO.setAddress(memberIfa.getAddress());
		      if(memberIfa.getNationality() != null && StringUtils.isNotBlank(memberIfa.getNationality().getId())){
		    	  String nationality = countryService.findBycountryId(memberIfa.getNationality().getId(), lang);
			      spaceVO.setNationality(nationality); 
		      }
	      }
	      
	      //策略实体
	      List<IfaSpaceStrategyInfoVO> ifaSpaceStrategyInfoList = spaceVO.getRecommendedStrategies();
	      //投资组合
	      List<IfaSpacePortfoliosVO> ifaSpacePortfoliosVO = spaceVO.getRecommendedPortfolios();
	      //推荐基金
	      List<IfaSpaceFundVO> ifaSpaceFundList = spaceVO.getFundList();
	      //推荐新闻实体列表
	      //List<IfaSpaceNewsVO> ifaSpaceNewsList = spaceVO.getNewsList();
	      //推荐观点实体列表
	      List<IfaSpaceInsightVO> ifaSpaceInsightList = spaceVO.getInsightList();
	      //访问实体列表
	      List<IfaSpaceVisitVO> ifaSpaceVisitList = spaceVO.getVisitList();
	      //直播左侧实体列表
	      //List<IfaSpaceLiveVO> ifaSpaceLiveList = spaceVO.getLiveList();
	      //
	      List<IfaFeeItem>  ifaFeeItemList = spaceVO.getIfaFeeItemList();
	      
	      //输出到前端页面
          model.put("spaceVO", spaceVO);
          //输出策略
          model.put("IfaSpaceStrategyInfoList", ifaSpaceStrategyInfoList);
          model.put("IfaSpaceStrategyInfoListCount",ifaSpaceStrategyInfoList==null?0:ifaSpaceStrategyInfoList.size());
          model.put("latestStrategyProductCount",0);
          if(null!=ifaSpaceStrategyInfoList&&!ifaSpaceStrategyInfoList.isEmpty())
          {
        	  model.put("latestStrategyId", ifaSpaceStrategyInfoList.get(0).getId());
        	  model.put("latestStrategyName", ifaSpaceStrategyInfoList.get(0).getStrategyName());
        	  model.put("latestStrategyProductCount", ifaSpaceStrategyInfoList.get(0).getContainProCount());
          }
          //输出组合
          model.put("IfaSpacePortfoliosCount",ifaSpacePortfoliosVO==null?0:ifaSpacePortfoliosVO.size());
          model.put("latestPortfoliosRate",0);
          if(null!=ifaSpacePortfoliosVO&&!ifaSpacePortfoliosVO.isEmpty())
          {
        	  model.put("latestPortfoliosId", ifaSpacePortfoliosVO.get(0).getId());
        	  model.put("latestPortfoliosName", ifaSpacePortfoliosVO.get(0).getPortfoliosName());
        	  model.put("latestPortfoliosRate", ifaSpacePortfoliosVO.get(0).getReturnRate());
          }
          //输出推荐基金
          model.put("recommendedFundCount",ifaSpaceFundList==null?0:ifaSpaceFundList.size());
          if(null!=ifaSpaceFundList&&!ifaSpaceFundList.isEmpty())
          {
        	  model.put("latestFundId", ifaSpaceFundList.get(0).getFundId());
	    	  model.put("latestFundName", ifaSpaceFundList.get(0).getFundName());
	    	  model.put("latestFundincreaseRate", ifaSpaceFundList.get(0).getIncreaseRate());
	    	  model.put("latestPeriodName", ifaSpaceFundList.get(0).getPeriodName());
          }
          //输出推荐观点
          model.put("IfaSpaceInsightList", ifaSpaceInsightList);
          if(null!=ifaSpaceInsightList&&!ifaSpaceInsightList.isEmpty())
        	  model.put("ifaSpaceInsightCount",ifaSpaceInsightList.isEmpty()?0:ifaSpaceInsightList.get(0).getInsightCount());
          else
        	  model.put("ifaSpaceInsightCount","0");
          //获取最新的一条观点
          if(ifaSpaceInsightList!=null&&!ifaSpaceInsightList.isEmpty()){
        	  IfaSpaceInsightVO lateInsightVo = ifaSpaceInsightList.get(0);
        	  model.put("lateInsightVo", lateInsightVo);
          }
          //输出推荐新闻
          //model.put("IfaSpaceNewsList", ifaSpaceNewsList);
          //输出访客
          model.put("IfaSpaceVisitList", ifaSpaceVisitList);
          //输出直播左侧列表
          //model.put("IfaSpaceLiveList", ifaSpaceLiveList);
          //已播
//          List<IfaSpaceLiveVO> ifaSpaceLivedList = new ArrayList<IfaSpaceLiveVO>();
//          for(IfaSpaceLiveVO live : ifaSpaceLiveList){
//        	  LiveScheduler ls = live.getLiveScheduler();
//        	  if(null!=ls){
//        		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        		  String endTime = df.format(ls.getEndTime());
//        		  String nowTime = df.format(new Date());// new Date()为获取当前系统时间
//        	        try {
//        	            Date dt1 = df.parse(endTime);
//        	            Date dt2 = df.parse(nowTime);
//        	            if(dt1.getTime() < dt2.getTime()){ //当前时间大于已结束播放时间，表示该LIVE已过期
//        	            	ifaSpaceLivedList.add(live);
//        	            }
//        	        } catch (Exception exception) {
//        	            exception.printStackTrace();
//        	        }
//        	  }
//        	  
//          }
//          model.put("ifaSpaceLivedList", ifaSpaceLivedList);
          //设置直播左侧图文首条记录
//          if(null!=ifaSpaceLiveList&&!ifaSpaceLiveList.isEmpty()){
//        	  model.put("leftFirstLiveTitle", ifaSpaceLiveList.get(0).getTitle());
//        	  model.put("leftFirstLiveContent", ifaSpaceLiveList.get(0).getContent());
//        	  model.put("leftFirstLiveIssuedTime", ifaSpaceLiveList.get(0).getIssuedTime());
//        	  //如果直播数据超过2条
//        	  if(ifaSpaceLiveList.size()>1){
//        		  List<IfaSpaceLiveVO> notLiveList = ifaSpaceLiveList;
//        		  notLiveList.remove(0);
//        		  model.put("notLiveList", notLiveList);
//        	  }
//          }else{
//        	  model.put("leftFirstLiveDisplay", "none");
//          }
          model.put("viewerIsSpaceOwner", viewerIsSpaceOwner);
          //输出区域与板块类型
          model.put("serviceRegionList", serviceRegionList);
          model.put("sectionTypeList", sectionTypeList);
          //显示货币类型列表，并选中个人默认的设置货币
          SysParamVO sysParamVO = sysParamService.findParamByCode(lang, spaceVO.getDefCurrency());
          List<SysParamVO> sysParamList = sysParamService.getParamList(lang,"currency_type");//currency_type
          model.put("sysParamList", sysParamList);
          model.put("sysParamVO", sysParamVO);
          //
          for(int i=0;i<ifaFeeItemList.size();i++){
        	  ifaFeeItemList.get(i).setIndex(i+1);
          }
          model.put("ifaFeeItemList", ifaFeeItemList);
          //System.out.println(IfaSpacePortfoliosVO.getPortfoliosName());
		return "ifa/space/ifaSpace";
	}
	
	/**
     * 打开列表界面初始化基础数据
     * @author 林文伟
     */
	@RequestMapping(value = "/ifaSpaceJson", method = RequestMethod.POST)
	public void ifaSpaceJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		  this.isMobileDevice(request, model);
	      String lang = this.getLoginLangFlag(request);
	      String ifaId = request.getParameter("id");
	      String regionId = request.getParameter("regionId");
	      //String sectionId = request.getParameter("sectionId");
	      MemberBase curMember = this.getLoginUser(request);
	      jsonPaging=this.getJsonPaging(request);
	      if(null != curMember){
				String loginMemberId = curMember.getId();
				//空间整个实体
			    IfaSpaceVO spaceVO = ifaSpaceService.loadIfaSpaceData(loginMemberId,ifaId,lang,regionId,"HKD");
			  //输出前端
			      JsonUtil.toWriter(spaceVO, response);
	      }
	      
	      //策略实体
	      //List<IfaSpaceStrategyInfoVO> IfaSpaceStrategyInfoVO = spaceVO.getOneStrategies();
	      //投资组合
	      //IfaSpacePortfoliosVO IfaSpacePortfoliosVO = spaceVO.getPortfolios();
	}
	
	/**
     * IFA个人空间推荐区域，根据搜索条件【区域与投资领域板块】获取策略、组合、基金、观点、新闻
     * @author 林文伟
     */
	@RequestMapping(value = "/ifaSpaceFilterRecommendJson", method = RequestMethod.POST)
	public void ifaSpaceFilterRecommendJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		  this.isMobileDevice(request, model);
	      String lang = this.getLoginLangFlag(request);
	      String memberId = request.getParameter("memberId");
	      String regionCodeType = request.getParameter("regionCodeType");
	      String sectorCodeType = request.getParameter("sectorCodeType");
	      MemberBase curMember = this.getLoginUser(request);
	      if(null != curMember){
	    	  String loginMemberId = curMember.getId();
	    	//空间整个实体
		      IfaSpaceVO spaceVO = ifaSpaceService.loadIfaSpaceFilterRecommendData(loginMemberId,memberId,lang,regionCodeType,sectorCodeType);
		    //输出前端
		      JsonUtil.toWriter(spaceVO, response);
	      }   
	}
	
	
	/**
     * ifa 的客户账户列表
     * @author mqzou
     * @date 2016-09-18
     */
	@RequestMapping(value = "/clientManagement", method = RequestMethod.GET)
	public String ifaAccountList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		if(null!=loginUser){
			String inUse=request.getParameter("in_use");//账户状态
			String inApproval=request.getParameter("inApproval");
			String cancellation=request.getParameter("cancellation");
			String currency=request.getParameter("cur");//基本货币
			String langCode=getLoginLangFlag(request);
			
            /*String check=request.getParameter("check");
			
            if(check==null || check=="0"){
				AccountValidVO validVO=iTraderSendService.saveCheckExistValid(request, loginUser.getId());
				if(null!=validVO){
					model.put("checkList", validVO.getValidAccountNo());
					model.put("accountType", CommonConstantsWeb.WEB_ACCOUNTCHECK_INVESTOR);
				}
				
			}
			*/
			String status="";
			if(null!=inUse && "1".equals(inUse)){
				status+="'3',";
			}
			if(null!=inApproval && "1".equals(inApproval)){
				status+="'2'";
			}
			if(status.endsWith(",")){
				status=status.substring(0,status.length()-1);
			}
			
			String isValid="";
			if(null!=cancellation && "1".equals(cancellation)){
				isValid="0";
				
			}
			if(null==currency || "".equals(currency))
				currency=loginUser.getDefCurrency();
			if(null==currency || "".equals(currency))
				currency=CommonConstants.DEF_CURRENCY;
			MemberIfa ifa=memberBaseService.findIfaMember(loginUser.getId());
			InvestorAccount account=new InvestorAccount();
			account.setIfa(ifa);
			//account.setMember(loginUser);
			account.setOpenStatus(status);
			//account.setBaseCurrency(currency);//货币类型不作为查询条件
			account.setIsValid(isValid);
		
			List distributorList=investorService.findInvestorDistributor(account);
			model.put("distributorList", distributorList);
			model.put("in_use", inUse);
			model.put("inApproval", inApproval);
			model.put("cancellation", cancellation);
			model.put("cur", currency);
			String curName=sysParamService.findNameByCode(currency, langCode);
			model.put("curName", curName);
			//金额保留小数位数
			int decimals=2;
			if("JPY".equals(currency)){
				decimals=0;
			}
			model.put("decimals", decimals);
			List<GeneralDataVO>  itemList=findSysParameters("currency_type", langCode);
 	        model.put("currencyType", itemList);
			//model.put("approvalCount", approvalCount);
			return "ifa/space/clientManagement";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		
	}
	
	/**
     * ifa 的客户账户列表
     * @author mqzou
	 * @throws ParseException 
     * @date 2016-09-19
     */
	@RequestMapping(value = "/accountList", method = RequestMethod.POST)
	public void getAccountList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ParseException{
		MemberBase loginUser = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null!=loginUser){
			String inUse=request.getParameter("in_use");//账户状态
			String cancellation=request.getParameter("cancellation");
			String currency=request.getParameter("cur");//基本货币
			String distributorId=request.getParameter("distributorId");
			String period=request.getParameter("period");
			String order=request.getParameter("order");
			String sort=request.getParameter("sort");
			String keyWord=request.getParameter("keyWord");
			
			if(null==currency || "".equals(currency))
				currency=loginUser.getDefCurrency();
			if(null==currency || "".equals(currency))
				currency=CommonConstants.DEF_CURRENCY;
			//String status="";
			//String isValid="";
			
			String condition="";
			
			
			if(null!=inUse && "1".equals(inUse)   ){
				condition=" and  a.open_status='3' and a.is_valid='1'";
			}
			if(null!=cancellation && !"".equals(cancellation)){
				//isValid="0";
				condition=" and  a.`is_valid`='0'";
			}
			
			if(((null==inUse || "".equals(inUse))&& (null==cancellation || "".equals(cancellation)))|| (null!=inUse && "1".equals(inUse) && null!=cancellation && !"".equals(cancellation))){
				condition=" and ( a.open_status='3' or a.is_valid='0')";
				
			}
			if(null!=keyWord && !"".equals(keyWord)){
				condition+=" and a.nick_name like '%"+keyWord+"%'";
			}
			
			MemberIfa ifa=memberBaseService.findIfaMember(loginUser.getId());
			
			AccountVO newVo=new AccountVO();
			newVo.setIfaId(ifa.getId());
			if(null!=distributorId && !"".equals(distributorId)){
				newVo.setDistributorId(distributorId);
			}
            if(null!=period && !"".equals(period)){
    			String startDate = "";//DateUtil.getCurDateStr();
    			if ("7".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 7);
    			else if ("14".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 14);
    			else if ("30".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 30);
    			else if ("60".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 60);
    			else if ("90".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 90);
    			newVo.setNextRPQDate(startDate);
			}
           
            newVo.setOpenStatus(condition);
            newVo.setBaseCurrency(currency);
			jsonPaging=getJsonPaging(request);
			jsonPaging.setSort(sort);
			jsonPaging.setOrder(order);
			jsonPaging=investorService.findAccountList(jsonPaging, newVo,langCode);
		
			Iterator it=jsonPaging.getList().iterator();
			int approvalCount=0;
			while (it.hasNext()) {
				AccountVO vo=(AccountVO)it.next();
				if("2".equals(vo.getOpenStatus())){
					approvalCount++;
				}
				
			}
			
			result.put("accountList", jsonPaging.getList());
			result.put("approvalCount", approvalCount);
			result.put("total", jsonPaging.getTotal());
			result.put("currency", currency);
			
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取未完成的账户列表
	 * @author mqzou
	 * @data 2017-01-05
	 * @return
	 */
	@RequestMapping(value = "/unCompleteAccountList", method = RequestMethod.POST)
	public void unCompleteAccountList(HttpServletRequest request,HttpServletResponse response,ModelMap mod){
		MemberBase loginUser = getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null!=loginUser){
			String currency=request.getParameter("cur");//基本货币
			String distributorId=request.getParameter("distributorId");
			String period=request.getParameter("period");
			String keyWord=request.getParameter("keyWord");
			
			if(null==currency || "".equals(currency))
				currency=loginUser.getDefCurrency();
			if(null==currency || "".equals(currency))
				currency=CommonConstants.DEF_CURRENCY;
		
			String condition=" and   a.open_status<>'3' and a.is_valid='1'";
			if(null!=keyWord && !"".equals(keyWord)){
				condition+=" and a.nick_name like '%"+keyWord+"%'";
			}
			MemberIfa ifa=memberBaseService.findIfaMember(loginUser.getId());
			
			AccountVO newVo=new AccountVO();
			newVo.setIfaId(ifa.getId());
			if(null!=distributorId && !"".equals(distributorId)){
				newVo.setDistributorId(distributorId);
			}
            if(null!=period && !"".equals(period)){
    			String startDate = "";//DateUtil.getCurDateStr();
    			if ("7".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 7);
    			else if ("14".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 14);
    			else if ("30".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 30);
    			else if ("60".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 60);
    			else if ("90".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 90);
    			newVo.setNextRPQDate(startDate);
			}
           
            newVo.setOpenStatus(condition);

            newVo.setBaseCurrency(currency);
			jsonPaging=getJsonPaging(request);
			jsonPaging.setSort("a.last_update");
			jsonPaging.setOrder("desc");
			jsonPaging=investorService.findAccountList(jsonPaging, newVo,langCode);
		
			
			
			result.put("accountList", jsonPaging.getList());
			result.put("total", jsonPaging.getTotal());
			result.put("currency", currency);
			
		}
		JsonUtil.toWriter(result, response);
	}
	
	
	/**
	 * 开户进度查询
	 * @author mqzou
	 * @data 2016-09-20
	 * @return
	 */
	@RequestMapping(value = "/accountProgress", method = RequestMethod.GET)
	public String accountProgress(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String accountId=request.getParameter("id");
		InvestorAccount account=investorService.findInvestorAccountById(accountId);
		model.put("account", account);
		List<WfProcedureInstanseTodo> todoList=investorService.findAccountWfHistory(accountId);
		List<WfProcedureInstanseTodo> list=new ArrayList<WfProcedureInstanseTodo>();
		if(null!=todoList && !todoList.isEmpty()){
			Iterator<WfProcedureInstanseTodo> it=todoList.iterator();
			AccountProgressVO progress=new AccountProgressVO();
			while (it.hasNext()) {
				WfProcedureInstanseTodo todo = (WfProcedureInstanseTodo) it.next();
				
				String flowCode=todo.getFlowCode();
				int flowState=todo.getFlowState();
				if(0==flowState){
					if ("10".equals(flowCode)) {
						progress.setIfaApproval("0");//待处理
					}else if ("20".equals(flowCode)) {
						progress.setIfafirmApproal("0");//待处理
					}else if ("30".equals(flowCode)) {
						progress.setIfafirmRoApproval("0");//待处理
					}else if ("40".equals(flowCode)) {
						progress.setDistributorApproval("0");//待处理
					}else if ("50".equals(flowCode)) {
						progress.setDistributorRoApproval("0");//待处理
					}
					
				}else {
					if ("10".equals(flowCode)) {
						int checkStatus=todo.getCheckStatus();
						if(1==checkStatus){
							progress.setIfaApproval("1");//通过
						}else {
							progress.setIfaApproval("2");//退回
						}
					}else if ("20".equals(flowCode)) {
						int checkStatus=todo.getCheckStatus();
						if(1==checkStatus){
							progress.setIfafirmApproal("1");//通过
						}else {
							progress.setIfafirmApproal("2");//退回
						}
					}else if ("30".equals(flowCode)) {
						int checkStatus=todo.getCheckStatus();
						if(1==checkStatus){
							progress.setIfafirmRoApproval("1");//通过
						}else {
							progress.setIfafirmRoApproval("2");//退回
						}
					}else if ("40".equals(flowCode)) {
						int checkStatus=todo.getCheckStatus();
						if(1==checkStatus){
							progress.setDistributorApproval("1");//通过
						}else {
							progress.setDistributorApproval("2");//退回
						}
					}else if ("50".equals(flowCode)) {
						int checkStatus=todo.getCheckStatus();
						if(1==checkStatus){
							progress.setDistributorRoApproval("1");//通过
						}else {
							progress.setDistributorRoApproval("2");//退回
						}
					}else if ("60".equals(flowCode)) {
						progress.setCompleteApproval("1");//待处理
					}
					list.add(todo);
				}
			}
			/*while (it.hasNext()) {
				WfProcedureInstanseTodo todo = (WfProcedureInstanseTodo) it.next();
				int flowState=todo.getFlowState();
				if(1==flowState){
					list.add(todo);
				}
			}*/
			model.put("progress", progress);
		}
		model.put("isIFA", true);
		
		
		model.put("todoList", list);
		return "ifa/space/accountProgress";
	}
	
	/**
	 * 查找投资人
	 * @author mqzou
	 * @data 2016-09-26
	 * @return
	 */
	@RequestMapping(value = "/findInvestor", method = RequestMethod.GET)
	public String findInvestor(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		MemberBase curMember=getLoginUser(request);
		if(null!=curMember){
			List<IfaInvestorVO> list=getInvestorList(request);
			model.put("investorList", JsonUtil.toJson(list));
			List<SysParamVO> styleList=sysParamService.getParamList("en", "investment_style");
			List<SysParamVO> intresteList=sysParamService.getParamList("en", "hobby_type");
			model.put("styleList", styleList);
			model.put("intresteList", intresteList);
			return "ifa/findinvestor";
		}else {
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		
	}
	/**
	 * 查找投资人列表
	 * @author mqzou
	 * @data 2016-09-26
	 * @return
	 */
	@RequestMapping(value = "/findInvestorList", method = RequestMethod.POST)
	public void findInvestorList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		List<IfaInvestorVO> list=getInvestorList(request);
		JsonUtil.toWriter(list, response);
	}
	
	private List<IfaInvestorVO> getInvestorList(HttpServletRequest request){
		MemberBase curMember=getLoginUser(request);
		String langCode = request.getParameter("langCode");
		 String invStyle=request.getParameter("invStyle");
		 String intrest=request.getParameter("intrest");
		 String noIfa=request.getParameter("noIfa");
		MemberBase memberBase=new MemberBase();
	/*	String _langCode="";
		String _invStyle="";
		String _intrest="";
		
		if(null!=langCode && !"".equals(langCode)){
			String[] str=langCode.split(",");
			if(null!=str && str.length>0){
				for (String string : str) {
					_langCode+="'"+string+"'";
				}
			}
		}
		if(!"".equals(_langCode)){
			_langCode=_langCode.substring(0,_langCode.length()-1);
		}
		
		if(null!=invStyle && !"".equals(invStyle)){
			String[] str=invStyle.split(",");
			if(null!=str && str.length>0){
				for (String string : str) {
					_invStyle+="'"+string+"'";
				}
			}
		}
		if(!"".equals(_invStyle)){
			_invStyle=_invStyle.substring(0,_invStyle.length()-1);
		}
		
		if(null!=intrest && !"".equals(intrest)){
			String[] str=intrest.split(",");
			if(null!=str && str.length>0){
				for (String string : str) {
					_intrest+="'"+string+"'";
				}
			}
		}
		
		if(!"".equals(_intrest)){
			_intrest=_intrest.substring(0,_intrest.length()-1);
		}*/
		memberBase.setInvestStyle(invStyle);
		memberBase.setHobbyType(intrest);
		memberBase.setLangCode(langCode);
		List<MemberBase> list=investorService.findInvestorList(memberBase, noIfa);
		Iterator<MemberBase> it=list.iterator();
		List<IfaInvestorVO> resultList=new ArrayList<IfaInvestorVO>();
		while (it.hasNext()) {
			MemberBase member = (MemberBase) it.next();
			IfaInvestorVO vo=new IfaInvestorVO();
			vo.setEmail(member.getEmail());
			vo.setLoginCode(member.getLoginCode());
			vo.setMemberId(member.getId());
			vo.setMobileNumber(member.getMobileNumber());
			boolean readInsight=ifaSpaceService.checkInsightRead(curMember.getId(), member.getId());
			if(readInsight)
				vo.setReadInsight("1");
			else {
				vo.setReadInsight("0");
			}
			int count=ifaSpaceService.checkIfaInvSameStyle(curMember.getId(), member.getId());
			vo.setSameStyle(String.valueOf(count));
			if(null!=curMember.getLangCode() && null!=member.getLangCode()){
				if(curMember.getLangCode().equals(member.getLangCode())){
					vo.setSameLang("1");
				}else {
					vo.setSameLang("0");
				}
			}else {
				vo.setSameLang("0");
			}
			
			boolean withIfa=ifaSpaceService.checkInvestorWithIfa(member.getId());
			if(withIfa)
				vo.setNoIfa("0");
			else {
				vo.setNoIfa("1");
			}
			resultList.add(vo);
		}
		return resultList;
	}
	
	
	/**
	 * IFA的推荐新闻列表
	 * @author mqzou
	 * @date 2016-09-30
	 */
	@RequestMapping(value = "/ifarecommendnews", method = RequestMethod.GET)
	public String ifaRecommendNews(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		if (loginUser!=null){
			try {
		        //地区列表
		        List<GeneralDataVO> itemList = findSysParameters("service_region", this.getLoginLangFlag(request));
		        model.put("regionList", itemList);
		        
		        //版块分类
		        List<GeneralDataVO> sectionList = findSysParameters("section_type", this.getLoginLangFlag(request));
		        model.put("sectionList", sectionList);
		        
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return "ifa/recommend/ifaRecommendNews";			
		}else{
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
	}
	
	/**
	 * IFA的推荐新闻列表
	 * @author mqzou
	 * @date 2016-09-30
	 */
	@RequestMapping(value = "/ifarecommendnewsJson")
	public void ifaRecommendNewsJson(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		if(null!=loginUser){
			jsonPaging=getJsonPaging(request);
			jsonPaging=findStrategyList(request,model);
			ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
		}
	}
	
	/**
     * IFA的推荐新闻列表（公共方法）
     * @author mqzou
     * @date 2016-09-30
     * @param request
     * @param model
     * @return
     */
    private JsonPaging findStrategyList(HttpServletRequest request, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		//CriteriaVO criteria = new CriteriaVO();
		
        //获取发布日期条件
        String period = StrUtils.getString(request.getParameter("period"));
		String startDate = "";//DateUtil.getCurDateStr();
		String endDate = DateUtil.getCurDateStr(Calendar.DATE, 1);//DateUtil.getCurDateStr();
		/*if ("1D".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -1);
		if ("1W".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
		if ("1M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
		if ("3M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
		if ("6M".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
		if ("1Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
		if ("2Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -2);
		if ("3Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
		if ("5Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
		if ("10Y".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.YEAR, -10);
		if ("YTD".equals(period)) startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());//今年以来
        String fromDate = request.getParameter("fromDate");
        if (null!=fromDate && fromDate.length()==10)
        	startDate = fromDate;
        String toDate = request.getParameter("toDate");
        if (null!=toDate && toDate.length()==10)
        	endDate = toDate;*/
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
		//开始日期统一提前一天
       /* if (!"".equals(startDate))
        	startDate = DateUtil.addDateToString(startDate,Calendar.DATE, -1);*/
		
       // String keyWord = toUTF8String(request.getParameter("keyWord"));
        String section = StrUtils.getString(request.getParameter("section"));
        String regions = StrUtils.getString(request.getParameter("regions"));
       // String source = StrUtils.getString(request.getParameter("source"));
      //  String riskLevel = StrUtils.getString(request.getParameter("riskLevel"));
        
        String viewSort = request.getParameter("viewSort");
        String issuedDateSort = request.getParameter("issuedDateSort");
        
    
        NewsInfo newsInfo=new NewsInfo();
        WebRecommended webRecommended=new WebRecommended();
        webRecommended.setCreator(loginUser);
        String orderBy = "";
		
        //设置筛选条件
        newsInfo.setSectionType(section);
        newsInfo.setRegionType(regions);
        

        
        jsonPaging = this.getJsonPaging(request);
        
        if(null!=issuedDateSort&& !"".equals(issuedDateSort)){//发布日期排序
        	orderBy = " views ";
        	 jsonPaging.setOrder(orderBy);
        	 jsonPaging.setSort(issuedDateSort);
        	 
			
		}else if(null!=viewSort && !"".equals(viewSort)){//点击次数排序
			orderBy = " recommend_time ";
			 jsonPaging.setOrder(orderBy);
        	 jsonPaging.setSort(viewSort);
		}
       // criteria.setOrderBy(orderBy);
        
       
        
       // jsonPaging = strategyInfoService.findByUser(jsonPaging, criteria);
        jsonPaging=ifaSpaceService.findIfaRecommendNews(jsonPaging, newsInfo, webRecommended, startDate,endDate);

		return jsonPaging;
    }
    
    /**
	 * 新闻的访客列表
	 * @author mqzou
	 * @date 2016-09-30
	 */
	@RequestMapping(value = "/newsVisitorList")
    public void findNewsVisitorList(HttpServletRequest request,HttpServletResponse response, ModelMap model){
    	
    	MemberBase loginUser=getLoginUser(request);
    	if(null!=loginUser){
    		String newsId=request.getParameter("itemId");
    		List list=ifaSpaceService.findNewsVisitorList(loginUser.getId(), newsId);
    		JsonUtil.toWriter(list, response);
    		//ResponseUtils.renderHtml(response,JsonUtil.toJson(list));
    	}
    }
	
	/**
	 * 推荐新闻置顶
	 * @author mqzou
	 * @date 2016-10-21
	 */
	@RequestMapping(value = "/newsOverhead")
	public void setNewsOverhead(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		String id = request.getParameter("itemId");
		String overhead = request.getParameter("type");
		boolean b = ifaSpaceService.checkRecommendOverhead(loginUser.getId(), id, overhead, "news");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", b);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 新闻推荐理由编辑页面
	 * @author mqzou
	 * @date 2016-10-08
	 */
	@RequestMapping(value = "/newsRecommendSetting")
	public String newsRecommendSetting(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String id=request.getParameter("id");
		String newsId=request.getParameter("newsId");
		
		WebRecommended recommended=ifaSpaceService.getIfaRecommendNews(id);
		model.put("recommended", recommended);
		
		NewsInfo newsInfo=newsInfoService.findNewsInfoById(newsId);
		model.put("newsInfo", newsInfo);
		
		//推送权限
    	StrategyWebPushVO webPushVO = ifaSpaceService.findWebPushByNews(newsId,CommonConstantsWeb.WEB_PUSH_MODULE_NEWS);
    	if (null==webPushVO) webPushVO = new StrategyWebPushVO();
    	model.put("webPushVO", webPushVO);
    	
		//客户类型：Buddy， Client， Advisor， Prospect
		List<MemberIndividual> clients = investorService.findClientByIFA(loginUser.getId(),"Client");
		model.put("cnt_clients", null==clients?0:clients.size());

		List<MemberIndividual> prospects = investorService.findClientByIFA(loginUser.getId(),"Prospect");
		model.put("cnt_prospects", null==prospects?0:prospects.size());

		List<MemberIndividual> buddies = investorService.findClientByIFA(loginUser.getId(),"Buddy");
		model.put("cnt_buddies", null==buddies?0:buddies.size());

		MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
		List<MemberIfa> colleagues = investorService.findIfaColleagues(ifa.getMember().getId(), ifa.getIfafirm().getId());
		model.put("cnt_colleagues", null==colleagues?0:colleagues.size());
		
		return "ifa/recommend/ifaRecommendSetting";
	}
	
	/**
	 * 保存新闻推荐信息
	 * @author mqzou
	 * @date 2016-10-08
	 */
	@RequestMapping(value = "/saveNewsRecommendSetting")
	public void  saveNewsRecommendSetting(HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		MemberBase loginUser=this.getLoginUser(request);
		String id=request.getParameter("id");
		String newsId=request.getParameter("newsId");
		String reason=request.getParameter("reason");
		String push=request.getParameter("pushto");
		String pushClients=request.getParameter("push_clients");
		String pushProspects=request.getParameter("push_prospects");
		String pushBuddies=request.getParameter("push_buddies");
		String pushColleagues=request.getParameter("push_colleagues");
		
		String pushClient=request.getParameter("push_client");
		String pushProspect=request.getParameter("push_prospect");
		String pushBuddy=request.getParameter("push_buddy");
		String pushColleague=request.getParameter("push_colleague");
		
		
		NewsInfo info=newsInfoService.findNewsInfoById(newsId); 
		//保存
		WebRecommended recommended=ifaSpaceService.getIfaRecommendNews(id);
		recommended.setReason(reason);
		recommended=ifaSpaceService.updateWebRecommended(recommended);
		
		WebPush webPush = new WebPush();
   		StrategyWebPushVO webPushVO = ifaSpaceService.findWebPushByNews(newsId,CommonConstantsWeb.WEB_PUSH_MODULE_NEWS);
   		if (null!=webPushVO && null!=webPushVO.getId() && !"".equals(webPushVO.getId())){
   			webPush = strategyInfoService.findWebPushById(webPushVO.getId());
   		}else{//新建
       		webPush.setModuleType(CommonConstantsWeb.WEB_PUSH_MODULE_NEWS);
       		webPush.setRelateId(info.getId());
       		webPush.setCreateTime(new Date());
       		webPush.setFromMember(loginUser);
   		}
   		webPush.setScopeFlag(push);
   		webPush.setLastUpdate(new Date());
       	if(null!=push && "2".equals(push)){//setting
       		//1 - 全选，0 - 无， -1 - 选部分
       		webPush.setBuddyFlag(pushBuddy);
       		webPush.setClientFlag(pushClient);
       		webPush.setColleagueFlag(pushColleague);
       		webPush.setProspectFlag(pushProspect);
       	}else if("1".equals(push)){//all
       		webPush.setBuddyFlag("1");
       		webPush.setClientFlag("1");
       		webPush.setColleagueFlag("1");
       		webPush.setProspectFlag("1");
       	}else{//only me
       		webPush.setBuddyFlag("0");
       		webPush.setClientFlag("0");
       		webPush.setColleagueFlag("0");
       		webPush.setProspectFlag("0");
       	}
       	webPush = strategyInfoService.saveWebPush(webPush,pushClients,pushProspects,pushBuddies,pushColleagues);
       	
		HashMap<String, Object> result=new HashMap<String, Object>();
		result.put("result", true);
		JsonUtil.toWriter(result,response);
	}
	/**
	 * 查看ifa推荐的策略列表
	 * @author mqzou
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/viewStrategiesList")
	public String viewIfaRecommendStrategy(HttpServletRequest request,HttpServletResponse response, ModelMap model){
	    MemberBase loginUser=getLoginUser(request);
		String ifaMemberId=request.getParameter("id");
		String langCode=getLoginLangFlag(request);
		IfaInfoVO ifa=ifaInfoService.findIfaInfo(ifaMemberId, langCode);
		model.put("ifa", ifa);
    	String dateFormat=loginUser.getDateFormat();
    	if(null==dateFormat || "".equals(dateFormat) )
    	   dateFormat=CommonConstants.FORMAT_DATE;
    	model.put("dateFormat", dateFormat);
		
		return "ifa/recommend/ifaRecommendStrategyList";
	}
	/**
	 * 查看ifa推荐的策略列表
	 * @author mqzou
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/viewStrategiesListJson")
	public void findStrategyList(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		String ifaMemberId=request.getParameter("id");
		String keyWord=toUTF8String(request.getParameter("keyWord"));
		/*if (null != keyWord && !"".equals(keyWord)) {
			keyWord=toUTF8String(keyWord);
		}*/
		CriteriaVO criteria = new CriteriaVO();
		criteria.setUserId(ifaMemberId);
		criteria.setKeyword(keyWord);
		jsonPaging=getJsonPaging(request);
		jsonPaging=strategyInfoService.findByIfaRecommend(jsonPaging, ifaMemberId, keyWord, langCode,loginUser);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
	}
	
	
	
	/**
	 * 查看ifa推荐的组合列表
	 * @author mqzou
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/viewPofolioList")
	public String viewPofolioList(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String langCode=getLoginLangFlag(request);
		String ifaMemberId=request.getParameter("id");
		/*MemberIfa ifa=memberBaseService.findIfaMember(ifaMemberId);
		if(null!=ifa){
		//	ifa.getMember().setIconUrl(getUserHeadUrl(ifa.getMember().getIconUrl(), ifa.getGender()));
			MemberIfafirm ifafirm=ifafirmManageService.findIfafirmByIfa(ifa, langCode);
			ifa.setIfafirm(ifafirm);
			Date investBegin=null!= ifa.getInvestLifeBegin()?ifa.getInvestLifeBegin():DateUtil.getCurDate();
			int year=0;
	    	if(null!=investBegin){
	    		long days=DateUtil.getDaysOfTwoDate(DateUtil.getDateStr(investBegin, "yyyy-MM-dd"), DateUtil.getCurDateStr());
	    		  year=Math.round(days/365);
	    		 model.put("year", year);
	    	}
		}*/
		
		IfaInfoVO ifa=ifaInfoService.findIfaInfo(ifaMemberId, langCode);
		model.put("ifa", ifa);
		model.put("defColor", loginUser.getDefDisplayColor());
		String dateFormat=loginUser.getDateFormat();
    	if(null==dateFormat || "".equals(dateFormat) )
    	   dateFormat=CommonConstants.FORMAT_DATE;
    	model.put("dateFormat", dateFormat);
		return "ifa/recommend/ifaRecommendPofolioList";
	}
	/**
	 * 查看ifa推荐的组合列表
	 * @author mqzou
	 * @date 2016-11-30
	 */
	@RequestMapping(value = "/viewPofolioListJson")
	public void viewPofolioListJson(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		String langCode=getLoginLangFlag(request);
		MemberBase loginUser=getLoginUser(request);
		String ifaMemberId=request.getParameter("id");
		String keyWord=request.getParameter("keyWord");
		if (null != keyWord && !"".equals(keyWord)) {
			keyWord=toUTF8String(keyWord);
		}
		CriteriaVO criteria = new CriteriaVO();
		criteria.setUserId(ifaMemberId);
		criteria.setKeyword(keyWord);
		jsonPaging=getJsonPaging(request);
		jsonPaging=portfolioArenaService.findByIfaRecommend(jsonPaging, ifaMemberId, keyWord, langCode,loginUser);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
	}
	
	/**
	 * 查看ifa推荐的基金列表
	 * @author mqzou
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/viewFundList")
	public String viewFundList(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);;
		String ifaMemberId=request.getParameter("id");
		String langCode=getLoginLangFlag(request);
		IfaInfoVO ifa=ifaInfoService.findIfaInfo(ifaMemberId, langCode);
		model.put("ifa", ifa);
		model.put("defColor", loginUser.getDefDisplayColor());
		String dateFormat=loginUser.getDateFormat();
    	if(null==dateFormat || "".equals(dateFormat) )
    	   dateFormat=CommonConstants.FORMAT_DATE;
    	model.put("dateFormat", dateFormat);
		return "ifa/recommend/ifaRecommendFundList";
	}
	
	/**
	 * 查看ifa推荐的基金列表
	 * @author mqzou
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/viewFundListJson")
	public void viewFundListJson(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		String langCode=getLoginLangFlag(request);
		String ifaMemberId=request.getParameter("id");
		String keyWord=request.getParameter("keyWord");
		if (null != keyWord && !"".equals(keyWord)) {
			keyWord=toUTF8String(keyWord);
			/*try {
				keyWord = URLDecoder.decode(keyWord, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}*/
		}
		jsonPaging=getJsonPaging(request);
		jsonPaging=fundInfoService.findByIfaRecommend(jsonPaging, langCode, keyWord, ifaMemberId);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
	}
	
	/**
	 * IFA空间，更新个人hightlight观点
	 * @author 林文伟
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/updateHightLight")
	public void updateHightLight(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		String ifaMemberId=request.getParameter("id");
		String hightLight=request.getParameter("hightlight");
		MemberIfa ifa=memberBaseService.findIfaMember(ifaMemberId);
		String ifaId = ifa.getId();
		Boolean rs = ifaSpaceService.updateHightLight(ifaId,hightLight);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 通过不同的货币转换
	 * @author 林文伟
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/exchangeCurrency")
	public void exchangeCurrency(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		String aum = request.getParameter("ifaAum");
		String fromCurrency = request.getParameter("ifaDefCurrency");
		String toCurrency = request.getParameter("toCurrency");
		
		 String newAum = ifaSpaceService.exchangeCurrency(StrUtils.getDouble(aum),fromCurrency,toCurrency);
		
		
		Map<String, Object> statuts = new HashMap<String, Object>();
		//statuts.put("page",jsonPaging.getPage());
		statuts.put("newaum",newAum);
		JsonUtil.toWriter(statuts, response);
	}
	
	/**
	 * 设置是否显示AUM给投资者看
	 * @author 林文伟
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/updateIfaIsShowAum")
	public void updateIfaIsShowAum(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String isShowAum=request.getParameter("isShowAum");
		Boolean rs = false;
		if(loginUser!=null){
			rs = ifaSpaceService.updateIfaIsShowAum(loginUser.getId(),isShowAum);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 设置是否在IFA空间显示个人业绩
	 * @author 林文伟
	 * @date 2016-11-29
	 */
	@RequestMapping(value = "/updateIfaIsShowFerformance")
	public void updateIfaIsShowFerformance(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		MemberBase loginUser=getLoginUser(request);
		String isShowperFormance=request.getParameter("isShowperFormance");
		Boolean rs = false;
		if(loginUser!=null){
			rs = ifaSpaceService.updateIfaIsShowFerformance(loginUser.getId(),isShowperFormance);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", rs);
		JsonUtil.toWriter(result, response);
	}
	
}
