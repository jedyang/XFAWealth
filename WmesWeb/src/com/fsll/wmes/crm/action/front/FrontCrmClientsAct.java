package com.fsll.wmes.crm.action.front;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.buscore.trade.vo.TradeIfaVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.ChineseToEnglish;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.vo.ClientRemindVO;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.crm.vo.CrmClientForIfaVO;
import com.fsll.wmes.crm.vo.CrmClientVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmSchedule;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.strategy.vo.CharDataVO;

/**
 * 控制器:Ifa Client
 * 
 * @author mqzou 
 * @date 2016-12-15
 */

@Controller
@RequestMapping("/front/crm/client")
public class FrontCrmClientsAct extends WmesBaseAct {
	
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private InvestorService investorService;
	@Autowired
	private PortfolioHoldService portfolioHoldService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private SysParamService sysParamService;
	
	 /**
	  * @author mqzou
	  * @date 2016-12-15
     * 分页列表
     */
    @RequestMapping(value = "/clientList", method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	MemberSsoVO ssoVO=getLoginUserSSO(request);
    	String langCode=getLoginLangFlag(request);
    	if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==ssoVO.getMemberType()){
    		String currency=request.getParameter("cur");
    		if(null==currency || "".equals(currency)){
    			currency=ssoVO.getDefCurrency();
    		}
    		if(null==currency || "".equals(currency)){
    			currency=CommonConstants.DEF_CURRENCY;
    		}
    		/*
    		String check=request.getParameter("check"); 			
 			if(check==null || check=="" || check=="0"){
 				AccountValidVO validVO=iTraderSendService.saveCheckExistValid(request, ssoVO.getId());
 				if (null!=validVO)
 					model.put("checkList", validVO.getValidAccountNo());
 				model.put("accountType", CommonConstantsWeb.WEB_ACCOUNTCHECK_IFA);
 			}
 			*/
    		TradeIfaVO ifaVO = tradeCoreService.getIfaBasic(ssoVO.getIfaId(), currency);
    		model.put("baseNumber", ifaVO);
    		model.put("currency", currency);
    		String currencyName=sysParamService.findNameByCode(currency, langCode);
    		model.put("currencyName", currencyName);
    		String defColor=ssoVO.getDefDisplayColor();
    		if(null==defColor || "".equals(defColor))defColor=CommonConstants.DEF_DISPLAY_COLOR;
    		model.put("defColor", defColor);
    		jsonPaging=new JsonPaging();
    		jsonPaging.setRows(100);
    		jsonPaging=crmCustomerService.findGroupList(jsonPaging, ssoVO.getIfaId());
    		model.put("groupList", jsonPaging.getList());    		
    		/*
    		IfaVO ifaVO=new IfaVO();
    		MemberIfa ifa=memberBaseService.findIfaMember(ssoVO.getId());
    		ifaVO.setIfafirmId(ifa.getIfafirm().getId());
    		ifaVO.setId(ssoVO.getIfaId());
    		jsonPaging=new JsonPaging();
    		jsonPaging.setRows(100);
    		jsonPaging=crmCustomerService.findInvestorByIfa(jsonPaging, ifaVO, langCode);
    		*/
    		jsonPaging=new JsonPaging();
    		jsonPaging.setRows(100);
    		jsonPaging=crmCustomerService.findClientForIfa(jsonPaging,ssoVO.getId(),"","1",langCode); 
    		//List clientList=crmCustomerService.findClientForIfa(ssoVO.getId(),"","1");    		
    		List list=new ArrayList();
    		if(null!=jsonPaging && null!=jsonPaging.getList()){
    			Iterator it=jsonPaging.getList().iterator();
    			while (it.hasNext()) {
    				CrmClientForIfaVO customer = (CrmClientForIfaVO) it.next();
					GeneralDataVO vo = new GeneralDataVO();
					vo.setName(customer.getNickName());
					vo.setValue(customer.getId());
					vo.setKey(ChineseToEnglish.getPinYinHeadChar(customer.getNickName().substring(0,1).toUpperCase()));
					list.add(vo);
				}
    		}
    		model.put("crmList", list);
    		List<GeneralDataVO>  itemList=findSysParameters("currency_type", langCode);
 	        model.put("currencyType", itemList);
    	}    	
		return "ifa/crm/clientsList";
	}
    
    /**
     * @author mqzou
	  * @date 2016-12-15
     * 分页列表数据
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public void listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	MemberSsoVO ssoVO=getLoginUserSSO(request);
    	String langCode=getLoginLangFlag(request);
    	if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA==ssoVO.getMemberType()){
    		String clientId=request.getParameter("clientId");
    		String totalReturn=request.getParameter("totalReturn");
    		String aum=request.getParameter("aum");
    		String groupId=request.getParameter("groupId");
    		String currency=request.getParameter("cur");
    		String period=request.getParameter("period");
    		String statistic=request.getParameter("statistic");
    		String type=request.getParameter("type");

    		
    		Date startDate=DateUtil.getCurDate();
    		Date endDate=DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 1);
    		if("today".equals(period)){
    			endDate= DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 1);
    		}else if ("1W".equals(period)) {
    			endDate=DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 8);
			}else if ("2W".equals(period)) {
				endDate=DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 15);
			}else if ("1M".equals(period)) {
				endDate=DateUtil.getInternalDateByMon(DateUtil.getCurDate(), 1);
			}
    		int days=0;
    		try {
				 days=DateUtil.daysBetween(startDate, endDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		if(null==currency || "".equals(currency)){
    			currency=ssoVO.getDefCurrency();
    		}
    		if(null==currency || "".equals(currency)){
    			currency=CommonConstants.DEF_CURRENCY;
    		}
    		
    		ClientSearchVO vo=new ClientSearchVO();
    		vo.setIfaId(ssoVO.getIfaId());
    		
    		vo.setClientGroup(groupId);
    		String minReturn="";
    		String maxReturn="";
    		String minAum="";
    		String maxAum="";
    		if(null!=totalReturn && !"".equals(totalReturn)){
    			totalReturn=totalReturn.replace("%", "");
    			if(totalReturn.contains("&lt;")){
    				maxReturn=totalReturn.replace("&lt;", "");
    			}else if (totalReturn.contains("&gt;")) {
					minReturn=totalReturn.replace("&gt;", "");
				}else if (totalReturn.contains("~")) {
					String[] values=totalReturn.split("~");
					if(values.length==2){
						minReturn=String.valueOf(Double.parseDouble(values[0].trim()));//String.valueOf(Double.parseDouble(values[0].trim())/100);
						maxReturn=String.valueOf(Double.parseDouble(values[1].trim()));//String.valueOf(Double.parseDouble(values[1].trim())/100);
					}
				}
    		}
    		if(null!=aum && !"".equals(aum)){
    			aum=aum.replace("M", "");
    			if(aum.contains("&lt;")){
    				maxAum=String.valueOf(Double.parseDouble(aum.replace("&lt;", ""))*1000000);
    			}else if (aum.contains("&gt;")) {
					minAum=String.valueOf(Double.parseDouble(aum.replace("&gt;", ""))*1000000);
				}else if (aum.contains("~")) {
					String[] values=aum.split("~");
					if(values.length==2){
						minAum=String.valueOf(Double.parseDouble(values[0])*1000000);
						maxAum=String.valueOf(Double.parseDouble(values[1])*1000000);
					}
				}
    		}
    		
    		if(null!=statistic && !"".equals(statistic)){
    			if("total".equals(statistic)){
    				vo.setStatistic("h.`total_return_rate`");
    				vo.setReCondition("");
    			}else {
    				vo.setReCondition(CommonConstantsWeb.WEB_RETURN_PERIOD_CODE_PRE+statistic);
    				vo.setStatistic("t.increase");
				}
    		}
    		if(null==vo.getStatistic() || "".equals(vo.getStatistic())){
    			vo.setStatistic("h.`total_return_rate`");
    			vo.setReCondition("");
    		}
    		vo.setMinAuM(minAum);
    		vo.setMaxAuM(maxAum);
    		vo.setMinReturnRate(minReturn);
    		vo.setMaxReturnRate(maxReturn);
    		vo.setCurrency(currency);
    		
    		MemberIfa ifa=memberBaseService.findIfaMember(ssoVO.getId());
    		String periodCode="";
    	    if(null!=period && !"".equals(period) && !"today".equals(period)){
    	    	periodCode=CommonConstantsWeb.WEB_RETURN_PERIOD_CODE_PRE+period;
    	    }
    	    String critical="";
    	    if(null!=ifa && null!=ifa.getPortfolioCriticalValue() && !"".equals(ifa.getPortfolioCriticalValue()))
    	    	critical=ifa.getPortfolioCriticalValue();
    	    else {
				critical=CommonConstantsWeb.IFA_PORTFOLIO_CRITICAL_VALUE;
			}
    		
    		if("birth".equals(type)){
    			clientId="";
    			List list=crmCustomerService.checkBirthDayCustomer(ssoVO.getIfaId(), "", days);
    			if(null!=list && !list.isEmpty()){
    				Iterator it=list.iterator();
    				while (it.hasNext()) {
						HashMap map = (HashMap) it.next();
						clientId+="'"+map.get("member_id")+"',";
						
					}
    				if(clientId.endsWith(",")){
    					clientId=clientId.substring(0,clientId.length()-1);
    				}
    				vo.setClientId(clientId);
    			}else {
					vo.setClientId("null");
				}
    			
    		}else if ("schedule".equals(type)) {
    			clientId="";
    			List list=crmCustomerService.checkSchedultCustomer(ssoVO.getIfaId(), "", startDate, endDate);
    			if(null!=list && !list.isEmpty()){
    				Iterator it=list.iterator();
    				while (it.hasNext()) {
    					CrmSchedule schedule = (CrmSchedule) it.next();
						clientId+="'"+schedule.getCustomer().getMember().getId()+"',";
						
					}
    				if(clientId.endsWith(",")){
    					clientId=clientId.substring(0,clientId.length()-1);
    				}
    				vo.setClientId(clientId);
    			}else {
					vo.setClientId("null");
				}
			}else if ("portfolio".equals(type)) {
				clientId="";
    			List list=portfolioHoldService.findCriticalPortfolioByIfa(critical, ssoVO.getIfaId(), periodCode);
    			if(null!=list && !list.isEmpty()){
    				Iterator it=list.iterator();
    				while (it.hasNext()) {
    					CrmCustomer schedule = (CrmCustomer) it.next();
						clientId+="'"+schedule.getMember().getId()+"',";
						
					}
    				if(clientId.endsWith(",")){
    					clientId=clientId.substring(0,clientId.length()-1);
    				}
    				vo.setClientId(clientId);
    			}else {
					vo.setClientId("null");
				}
			}else if ("kyc".equals(type)) {
				clientId="";
    			List list=crmCustomerService.findRpqPre(ssoVO.getIfaId(), "",startDate, endDate);
    			if(null!=list && !list.isEmpty()){
    				Iterator it=list.iterator();
    				while (it.hasNext()) {
    					CrmCustomer schedule = (CrmCustomer) it.next();
    					clientId+="'"+schedule.getMember().getId()+"',";
						
					}
    				if(clientId.endsWith(",")){
    					clientId=clientId.substring(0,clientId.length()-1);
    				}
    				vo.setClientId(clientId);
    			}else {
					vo.setClientId("null");
				}
			}
    		else {
    			vo.setClientId(clientId);
			}
    		
    		jsonPaging =getJsonPaging(request);
    		if("".equals(jsonPaging.getOrder())){
    			jsonPaging.setOrder("desc");
        		jsonPaging.setSort("totalAuM");
    		}
    		vo.setLangCode(langCode);
    		jsonPaging=crmCustomerService.findClentsByIFA(jsonPaging, vo);
    		String numFormat="#,##0.00";
			if("JPY".equals(currency)){
				numFormat="#,##0";
			}
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
					
					
					List checkList=crmCustomerService.checkBirthDayCustomer(ssoVO.getIfaId(), clientVO.getMemberId(), days);
					if(null!=checkList && !checkList.isEmpty()){
						clientVO.setIsBirthDay("1");
					}else {
						clientVO.setIsBirthDay("0");
					}
					
					checkList=crmCustomerService.checkSchedultCustomer(ssoVO.getIfaId(), clientVO.getMemberId(), startDate, endDate);
					if(null!=checkList && !checkList.isEmpty()){
						clientVO.setHasSchedule("1");
					}else {
						clientVO.setHasSchedule("0");
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
							chart.setDisplayColor(null!=obj[2]?obj[2].toString():"");
							chartData.add(chart);
						}
					}*/
					clientVO.setProductProJson(JsonUtil.toJson(chartData));
					String risk=investorService.findInvestorMaxRiskLevel(clientVO.getMemberId());
					clientVO.setRiskLevel(risk);
					clientVO.setAumStr(StrUtils.getNumberString(clientVO.getAum(),numFormat));
					clientVO.setTotalReturnRateStr(StrUtils.getNumberString(clientVO.getTotalReturnRate(),numFormat));
					clientVO.setTotalReturnStr(StrUtils.getNumberString(clientVO.getTotalReturn(),numFormat));
				
				}
    		}
    		
    		JsonUtil.toWriter(jsonPaging, response);
    		
    	}
    }
    /**
     * @author mqzou
	  * @date 2016-12-16
     * 获取客户的提醒信息
     */
    @RequestMapping(value="/getRemindData")
    public void getRemind(HttpServletRequest request, HttpServletResponse response, ModelMap model){
    	MemberSsoVO ssoVO=getLoginUserSSO(request);
    	String period=request.getParameter("period");
    	if(null!=ssoVO){
    		String startDate=DateUtil.getCurDateStr();
    		String endDate=DateUtil.getDateStr( DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 1));
    		if("today".equals(period)){
    			endDate=DateUtil.getDateStr( DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 1));
    		}else if ("1W".equals(period)) {
    			endDate=DateUtil.getDateStr( DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 8));
			}else if ("2W".equals(period)) {
				endDate=DateUtil.getDateStr( DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 15));
			}else if ("1M".equals(period)) {
				endDate=DateUtil.getDateStr(DateUtil.getInternalDateByMon(DateUtil.getCurDate(), 1));
			}
    		
    		ClientRemindVO vo=crmCustomerService.findClientRemind(ssoVO.getIfaId(), "", startDate, endDate);
    	    MemberIfa ifa=memberBaseService.findIfaMember(ssoVO.getId());
    	    String periodCode="";
    	    if(null!=period && !"".equals(period) && !"today".equals(period)){
    	    	periodCode="fund_return_period_code_"+period;
    	    }
    	    String critical="";
    	    if(null!=ifa && null!=ifa.getPortfolioCriticalValue() && !"".equals(ifa.getPortfolioCriticalValue()))
    	    	critical=ifa.getPortfolioCriticalValue();
    	    else {
				critical=CommonConstantsWeb.IFA_PORTFOLIO_CRITICAL_VALUE;
			}
    	    List list=portfolioHoldService.findCriticalPortfolioByIfa(critical, ssoVO.getIfaId(), periodCode);
    	    if(null!=vo && null!=list)
    	    	vo.setPortfolioPre(String.valueOf(list.size()));
    	    
    	    list=crmCustomerService.findRpqPre(ssoVO.getIfaId(), "", DateUtil.getDate(startDate), DateUtil.getDate(endDate));
    	    if(null!=vo && null!=list)
    	    	vo.setKycPre(String.valueOf(list.size()));
    	    
    	    JsonUtil.toWriter(vo, response);
    	}
    }

}
