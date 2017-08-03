/**
 * 
 */
package com.fsll.wmes.ifafirm.action.console;

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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.ChineseToEnglish;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.service.SysParamService;
import com.fsll.oms.service.ITraderSendService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.crm.service.KycDocCheckServic;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.crm.vo.ClientsBasicVO;
import com.fsll.wmes.crm.vo.CrmClientForIfaVO;
import com.fsll.wmes.crm.vo.CrmClientVO;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.distributor.vo.AccountFitlerVO;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmSchedule;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorBackground;
import com.fsll.wmes.entity.InvestorTraining;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifafirm.service.IfaClientService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmBaseVO;
import com.fsll.wmes.ifafirm.vo.TransRecordVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.InvestorAccountCurrencyVO;
import com.fsll.wmes.investor.vo.InvestorAccountHoldVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.strategy.vo.CharDataVO;
import com.fsll.wmes.trade.vo.TransactionRecordFilterVO;

/**
 * IFA Firm的客户管理 IFA Firm，Distributor交易记录
 * 
 * @author scshi 2016-12-19
 */
@Controller
@RequestMapping("/console/ifafirm/client")
public class ConsoleIfaClientsAct extends WmesBaseAct {
	@Autowired
	private IfaClientService ifaClientService;

	@Autowired
	private CrmCustomerService crmCustomerService;

	@Autowired
	private ITraderSendService iTraderSendService;

	@Autowired
	private InvestorService investorService;

	@Autowired
	private PortfolioHoldService portfolioHoldService;

	@Autowired
	private MemberBaseService memberBaseService;

	@Autowired
	private FundInfoService fundInfoService;
	
	@Autowired
	private KycDocCheckServic kycDocCheckServic;
	
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
	
	@Autowired
	private DistributorService distributorService;
	/**
	 * 客户管理
	 */
	@RequestMapping(value = "/clients", method = RequestMethod.GET)
	public String clients(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberSsoVO ssoVO = getLoginUserSSO(request);
		if (null == ssoVO)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		String langCode = getLoginLangFlag(request);
		
		String currency = request.getParameter("cur");
		if (null == currency || "".equals(currency)) {
			currency = ssoVO.getDefCurrency();
		}
		if (null == currency || "".equals(currency)) {
			currency = CommonConstants.DEF_CURRENCY;
		}
		model.put("currency", currency);
		String curName = sysParamService.findNameByCode(currency, langCode);
		model.put("curName", curName);
		String defColor = ssoVO.getDefDisplayColor();
		if (null == defColor || "".equals(defColor))
			defColor = CommonConstants.DEF_DISPLAY_COLOR;
		model.put("defColor", defColor);

		List<GeneralDataVO> itemList = findSysParameters("currency_type", langCode);
		model.put("currencyType", itemList);
		
		if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == ssoVO.getMemberType()) {

			// String check=request.getParameter("check");
			//
			// if(check==null || check=="" || check=="0"){
			// AccountValidVO
			// validVO=iTraderSendService.saveCheckExistValid(request,
			// ssoVO.getId());
			// if (null!=validVO)
			// model.put("checkList", validVO.getValidAccountNo());
			// model.put("accountType",
			// CommonConstantsWeb.WEB_ACCOUNTCHECK_IFA);
			// }

			ClientsBasicVO basicVO = ifaClientService.findClientsBasicByFirm(ssoVO.getIfafirmId());
			double totalAuM = ifaClientService.findClientAuMByFirm(ssoVO.getIfafirmId(), currency);
			basicVO.setAum(totalAuM);
			model.put("baseNumber", basicVO);

			List clientList = ifaClientService.findClientForIfaFirm(ssoVO.getIfafirmId(), "", "1");

			List list = new ArrayList();
			if (null != clientList) {
				Iterator it = clientList.iterator();
				while (it.hasNext()) {
					CrmClientForIfaVO customer = (CrmClientForIfaVO) it.next();
					GeneralDataVO vo = new GeneralDataVO();
					vo.setName(customer.getNickName());
					vo.setValue(customer.getMemberId());
					vo.setKey(ChineseToEnglish.getPinYinHeadChar(customer.getNickName()).substring(0, 1).toUpperCase());
					list.add(vo);
				}
			}
			model.put("crmList", list);

		}
		if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR == ssoVO.getMemberType()) {

			List clientList = ifaClientService.findClientForDistributor(ssoVO.getDistributorId(), "", "1");

			List list = new ArrayList();
			if (null != clientList) {
				Iterator it = clientList.iterator();
				while (it.hasNext()) {
					CrmClientForIfaVO customer = (CrmClientForIfaVO) it.next();
					GeneralDataVO vo = new GeneralDataVO();
					vo.setName(customer.getNickName());
					vo.setValue(customer.getMemberId());
					vo.setKey(ChineseToEnglish.getPinYinHeadChar(customer.getNickName()).substring(0, 1).toUpperCase());
					list.add(vo);
				}
			}
			model.put("crmList", list);

		}
		return "console/ifafirm/client/clients";
	}
	
	/**
	 * 客户管理列表
	 * 
	 * @author mqzou
	 * @date 2017-06-06
	 * */
	@RequestMapping(value = "/clientList", method = RequestMethod.GET)
	public String clientList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO ssoVO = getLoginUserSSO(request);
		if (null == ssoVO)
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		String langCode = getLoginLangFlag(request);
		
		String currency = request.getParameter("cur");
		if (null == currency || "".equals(currency)) {
			currency = ssoVO.getDefCurrency();
		}
		if (null == currency || "".equals(currency)) {
			currency = CommonConstants.DEF_CURRENCY;
		}
		model.put("currency", currency);
		String curName = sysParamService.findNameByCode(currency, langCode);
		model.put("curName", curName);
		String defColor = ssoVO.getDefDisplayColor();
		if (null == defColor || "".equals(defColor))
			defColor = CommonConstants.DEF_DISPLAY_COLOR;
		model.put("defColor", defColor);

		if("JPY".equals(currency)){
			model.put("decimals", 0);
		}else {
			model.put("decimals", 2);
		}
		
		
		List<GeneralDataVO> itemList = findSysParameters("currency_type", langCode);
		model.put("currencyType", itemList);
		return "console/ifafirm/client/clientManage";
	}

	/**
	 * 分页数据
	 * 
	 * @author scshi_u330p
	 * @date 20161220
	 * */
	@RequestMapping(value = "/existingClientListJsonForIfa", method = RequestMethod.POST)
	public String existingClientListJsonForIfa(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// MemberBase curMember = this.getLoginUser(request);
		// String ifaMemberId = curMember.getId();
		Boolean isDis = false;// 判断当前登录是否代理商
		MemberSsoVO sso = this.getLoginUserSSO(request);
		String ifaMemberId = null;
		if (31 == sso.getSubMemberType()) {// 代理商登录
			ifaMemberId = sso.getDistributorId();
			isDis = true;
		} else {// ifa公司登录
			ifaMemberId = sso.getIfafirmId();
		}
		String areaId = request.getParameter("areaId");
		String period = request.getParameter("period");
		String clientStatus = request.getParameter("clientStatus");
		String keyword = request.getParameter("keyword");
		String character = request.getParameter("char");

		jsonPaging = this.getJsonPaging(request);
		jsonPaging = ifaClientService
		        .findExistingCustomerListForIfa(jsonPaging, ifaMemberId, areaId, period, clientStatus, keyword, character, isDis);
		this.toJsonString(response, jsonPaging);
		return null;
	}
	
	
	/**
	 * 客户管理列表分页数据
	 * 
	 * @author mqzou
	 * @date 2017-06-06
	 * */
	@RequestMapping(value = "/ifaFirmClientManageJson", method = RequestMethod.POST)
	public void ifaFirmClientManageJson(HttpServletRequest request, HttpServletResponse response, ModelMap mode){
		MemberSsoVO ssoVO = getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		String dateFormat=ssoVO.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		//获取参数
		String clientName = request.getParameter("clientName");
		String totalReturn = request.getParameter("totalReturn");
		String aum = request.getParameter("aum");
		String marketValue=request.getParameter("marketValue");
		String ifaName = request.getParameter("ifaName");
		String currency = request.getParameter("cur");
		
		if(null==currency || "".equals(currency)){
			currency=ssoVO.getDefCurrency();
		}
		if(null==currency || "".equals(currency)){
			currency=CommonConstants.DEF_CURRENCY;
		}
		
		
		String minReturn = "";
		String maxReturn = "";
		String minAum = "";
		String maxAum = "";
		String maxMarket="";
		String minMarket="";
		if (null != totalReturn && !"".equals(totalReturn)) {
			totalReturn = totalReturn.replace("%", "");
			if (totalReturn.contains("&lt;")) {
				maxReturn = totalReturn.replace("&lt;", "");
			} else if (totalReturn.contains("&gt;")) {
				minReturn = totalReturn.replace("&gt;", "");
			} else if (totalReturn.contains("~")) {
				String[] values = totalReturn.split("~");
				if (values.length == 2) {
					minReturn = String.valueOf(Double.parseDouble(values[0].trim()) / 100);
					maxReturn = String.valueOf(Double.parseDouble(values[1].trim()) / 100);
				}
			}
		}
		if (null != aum && !"".equals(aum)) {
			aum = aum.replace("M", "");
			if (aum.contains("&lt;")) {
				maxAum = String.valueOf(Double.parseDouble(aum.replace("&lt;", "")) * 1000000);
			} else if (aum.contains("&gt;")) {
				minAum = String.valueOf(Double.parseDouble(aum.replace("&gt;", "")) * 1000000);
			} else if (aum.contains("~")) {
				String[] values = aum.split("~");
				if (values.length == 2) {
					minAum = String.valueOf(Double.parseDouble(values[0]) * 1000000);
					maxAum = String.valueOf(Double.parseDouble(values[1]) * 1000000);
				}
			}
		}
		if (null != marketValue && !"".equals(marketValue)) {
			marketValue = marketValue.replace("M", "");
			if (marketValue.contains("&lt;")) {
				maxMarket = String.valueOf(Double.parseDouble(marketValue.replace("&lt;", "")) * 1000000);
			} else if (marketValue.contains("&gt;")) {
				minMarket = String.valueOf(Double.parseDouble(marketValue.replace("&gt;", "")) * 1000000);
			} else if (marketValue.contains("~")) {
				String[] values = marketValue.split("~");
				if (values.length == 2) {
					minMarket = String.valueOf(Double.parseDouble(values[0]) * 1000000);
					maxMarket = String.valueOf(Double.parseDouble(values[1]) * 1000000);
				}
			}
		}
		//设置搜索对象
		ClientSearchVO vo = new ClientSearchVO();
		vo.setMinAuM(minAum);
		vo.setMaxAuM(maxAum);
		vo.setMinReturnRate(minReturn);
		vo.setMaxReturnRate(maxReturn);
		vo.setCurrency(currency);
		vo.setIfaFirmId(ssoVO.getIfafirmId());
		vo.setIfaName(ifaName);
		vo.setClientName(clientName);
		vo.setMinMarket(minMarket);
		vo.setMaxMarket(maxMarket);
		vo.setLangCode(langCode);
		vo.setDateFormat(dateFormat);
		vo.setStatistic("h.`total_return_rate`");
		jsonPaging=getJsonPaging(request);
		jsonPaging = ifaClientService.findClentsByIfaFirm(jsonPaging, vo);
		JsonUtil.toWriter(jsonPaging, response);
	}

	/**
	 * 分页数据
	 * 
	 * @author scshi_u330p
	 * @date 20161220
	 * */
	@RequestMapping(value = "/ifaFirmClientListJson", method = RequestMethod.POST)
	public String ifaFirmClientListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		MemberSsoVO ssoVO = getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		String dateFormat=ssoVO.getDateFormat();
		if(null==dateFormat || "".equals(dateFormat)){
			dateFormat=CommonConstants.FORMAT_DATE;
		}
		//获取参数
		String clientId = request.getParameter("clientId");
		String totalReturn = request.getParameter("totalReturn");
		String aum = request.getParameter("aum");
		
		String ifaFirmName = request.getParameter("ifaFirmName");
		String ifaName = request.getParameter("ifaName");
		String sortBy = request.getParameter("sort");
		String order = request.getParameter("order");

		String currency = request.getParameter("cur");
		String period = request.getParameter("period");
		String statistic = request.getParameter("statistic");
		String type = request.getParameter("type");
		Date startDate = DateUtil.getCurDate();
		Date endDate = DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 1);
		if ("today".equals(period)) {
			endDate = DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 1);
		} else if ("1W".equals(period)) {
			endDate = DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 8);
		} else if ("2W".equals(period)) {
			endDate = DateUtil.getInternalDateByDay(DateUtil.getCurDate(), 15);
		} else if ("1M".equals(period)) {
			endDate = DateUtil.getInternalDateByMon(DateUtil.getCurDate(), 1);
		}
		int days = 0;
		try {
			days = DateUtil.daysBetween(startDate, endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String minReturn = "";
		String maxReturn = "";
		String minAum = "";
		String maxAum = "";
		if (null != totalReturn && !"".equals(totalReturn)) {
			totalReturn = totalReturn.replace("%", "");
			if (totalReturn.contains("&lt;")) {
				maxReturn = totalReturn.replace("&lt;", "");
			} else if (totalReturn.contains("&gt;")) {
				minReturn = totalReturn.replace("&gt;", "");
			} else if (totalReturn.contains("~")) {
				String[] values = totalReturn.split("~");
				if (values.length == 2) {
					minReturn = String.valueOf(Double.parseDouble(values[0].trim()) / 100);
					maxReturn = String.valueOf(Double.parseDouble(values[1].trim()) / 100);
				}
			}
		}
		if (null != aum && !"".equals(aum)) {
			aum = aum.replace("M", "");
			if (aum.contains("&lt;")) {
				maxAum = String.valueOf(Double.parseDouble(aum.replace("&lt;", "")) * 1000000);
			} else if (aum.contains("&gt;")) {
				minAum = String.valueOf(Double.parseDouble(aum.replace("&gt;", "")) * 1000000);
			} else if (aum.contains("~")) {
				String[] values = aum.split("~");
				if (values.length == 2) {
					minAum = String.valueOf(Double.parseDouble(values[0]) * 1000000);
					maxAum = String.valueOf(Double.parseDouble(values[1]) * 1000000);
				}
			}
		}

		//设置搜索对象
		ClientSearchVO vo = new ClientSearchVO();
		if (null != statistic && !"".equals(statistic)) {
			if ("total".equals(statistic)) {
				vo.setStatistic("h.`total_return_rate`");
			} else {
				vo.setStatistic("t.`period_code`='fund_return_period_code_" + statistic + "'");
			}
		}
		if (null == vo.getStatistic() || "".equals(vo.getStatistic())) {
			vo.setStatistic("h.`total_return_rate`");
		}
		vo.setMinAuM(minAum);
		vo.setMaxAuM(maxAum);
		vo.setMinReturnRate(minReturn);
		vo.setMaxReturnRate(maxReturn);
		vo.setCurrency(currency);
		vo.setLangCode(langCode);
		vo.setDateFormat(dateFormat);
		
		if (!StrUtils.isEmpty(sortBy)) jsonPaging.setSort(sortBy);
		if (!StrUtils.isEmpty(order))	jsonPaging.setOrder(order);
		
		if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == ssoVO.getMemberType()) {// IFA,IFA Firm

			if (null == currency || "".equals(currency)) {
				currency = ssoVO.getDefCurrency();
			}
			if (null == currency || "".equals(currency)) {
				currency = CommonConstants.DEF_CURRENCY;
			}

			vo.setIfaFirmId(ssoVO.getIfafirmId());
			vo.setIfaName(ifaName);
			vo.setIfaFirmName(ifaFirmName);
			
			MemberIfa ifa = memberBaseService.findIfaMember(ssoVO.getId());
			String periodCode = "";
			if (null != period && !"".equals(period) && !"today".equals(period)) {
				periodCode = "fund_return_period_code_" + period;
			}
			String critical = "";
			if (null != ifa && null != ifa.getPortfolioCriticalValue() && !"".equals(ifa.getPortfolioCriticalValue()))
				critical = ifa.getPortfolioCriticalValue();
			else {
				critical = CommonConstantsWeb.IFA_PORTFOLIO_CRITICAL_VALUE;
			}

			if ("birth".equals(type)) {
				clientId = "";
				List list = crmCustomerService.checkBirthDayCustomer(ssoVO.getIfaId(), "", days);
				if (null != list && !list.isEmpty()) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						HashMap map = (HashMap) it.next();
						clientId += "'" + map.get("member_id") + "',";

					}
					if (clientId.endsWith(",")) {
						clientId = clientId.substring(0, clientId.length() - 1);
					}
					vo.setClientId(clientId);
				} else {
					vo.setClientId("null");
				}

			} else if ("schedule".equals(type)) {
				clientId = "";
				List list = crmCustomerService.checkSchedultCustomer(ssoVO.getIfaId(), "", startDate, endDate);
				if (null != list && !list.isEmpty()) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						CrmSchedule schedule = (CrmSchedule) it.next();
						clientId += "'" + schedule.getCustomer().getMember().getId() + "',";

					}
					if (clientId.endsWith(",")) {
						clientId = clientId.substring(0, clientId.length() - 1);
					}
					vo.setClientId(clientId);
				} else {
					vo.setClientId("null");
				}
			} else if ("portfolio".equals(type)) {
				clientId = "";
				List list = portfolioHoldService.findCriticalPortfolioByIfa(critical, ssoVO.getIfaId(), periodCode);
				if (null != list && !list.isEmpty()) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						CrmCustomer schedule = (CrmCustomer) it.next();
						clientId += "'" + schedule.getMember().getId() + "',";

					}
					if (clientId.endsWith(",")) {
						clientId = clientId.substring(0, clientId.length() - 1);
					}
					vo.setClientId(clientId);
				} else {
					vo.setClientId("null");
				}
			} else if ("kyc".equals(type)) {
				clientId = "";
				List list = crmCustomerService.findRpqPre(ssoVO.getIfaId(), "", startDate, endDate);
				if (null != list && !list.isEmpty()) {
					Iterator it = list.iterator();
					while (it.hasNext()) {
						CrmCustomer schedule = (CrmCustomer) it.next();
						clientId += "'" + schedule.getMember().getId() + "',";

					}
					if (clientId.endsWith(",")) {
						clientId = clientId.substring(0, clientId.length() - 1);
					}
					vo.setClientId(clientId);
				} else {
					vo.setClientId("null");
				}
			} else {
//				vo.setClientId(clientId);
				String clients="";
				if (!StrUtils.isEmpty(clientId)) {
					String[] it = clientId.split(",");
					for (String k: it){
						if (!StrUtils.isEmpty(k))
						clients += "'" + k + "',";
					}
					if (clients.endsWith(",")) {
						clients = clients.substring(0, clients.length() - 1);
					}
					vo.setClientId(clients);
				} else {
					vo.setClientId(clientId);
				}
			}

			jsonPaging = getJsonPaging(request);
			if ("".equals(jsonPaging.getOrder())) {
				jsonPaging.setOrder("desc");
				jsonPaging.setSort("total_return_rate");
			}
			jsonPaging = ifaClientService.findClentsByIfaFirm(jsonPaging, vo);

			if (null != jsonPaging.getList()) {
				Iterator it = jsonPaging.getList().iterator();
				while (it.hasNext()) {
					CrmClientVO clientVO = (CrmClientVO) it.next();
					boolean result = investorService.checkFacaOrCies(clientVO.getMemberId(), "faca");
					if (result) {
						clientVO.setFaca("1");
					} else {
						clientVO.setFaca("0");
					}
					result = investorService.checkFacaOrCies(clientVO.getMemberId(), "cies");
					if (result) {
						clientVO.setCies("1");
					} else {
						clientVO.setCies("0");
					}

					List checkList = crmCustomerService.checkBirthDayCustomer(ssoVO.getIfaId(), clientVO.getMemberId(), days);
					if (null != checkList && !checkList.isEmpty()) {
						clientVO.setIsBirthDay("1");
					} else {
						clientVO.setIsBirthDay("0");
					}

					checkList = crmCustomerService.checkSchedultCustomer(ssoVO.getIfaId(), clientVO.getMemberId(), startDate, endDate);
					if (null != checkList && !checkList.isEmpty()) {
						clientVO.setHasSchedule("1");
					} else {
						clientVO.setHasSchedule("0");
					}

					List productList = portfolioHoldService.findPortfolioAllocation(clientVO.getMemberId());
					List<CharDataVO> chartData = new ArrayList<CharDataVO>();
					if (null != productList) {
						Iterator iterator = productList.iterator();
						while (iterator.hasNext()) {
							Object[] obj = (Object[]) iterator.next();
							if (null == obj[0] || "".equals(obj[0]))
								continue;
							CharDataVO chart = new CharDataVO();
							chart.setName(obj[0].toString());
							chart.setValue(null != obj[1] ? Double.parseDouble(obj[1].toString()) : 0);
							chartData.add(chart);
						}
					}
					clientVO.setProductProJson(JsonUtil.toJson(chartData));
					String risk = investorService.findInvestorMaxRiskLevel(clientVO.getMemberId());
					clientVO.setRiskLevel(risk);
					clientVO.setAumStr(StrUtils.getNumberString(clientVO.getAum(), "#,##0.00"));
					clientVO.setTotalReturnRateStr(StrUtils.getNumberString(clientVO.getTotalReturnRate(), "#,##0.00"));
					clientVO.setTotalReturnStr(StrUtils.getNumberString(clientVO.getTotalReturn(), "#,##0.00"));

				}
			}

			JsonUtil.toWriter(jsonPaging, response);
		}
		if (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_DISTRIBUTOR == ssoVO.getMemberType()) {// Distributor

			if (null == currency || "".equals(currency)) {
				currency = ssoVO.getDefCurrency();
			}
			if (null == currency || "".equals(currency)) {
				currency = CommonConstants.DEF_CURRENCY;
			}

			vo.setDistributorId(ssoVO.getDistributorId());
			vo.setIfaName(ifaName);
			vo.setIfaFirmName(ifaFirmName);
			vo.setClientId(clientId);

			String clients="";
			if (!StrUtils.isEmpty(clientId)) {
				String[] it = clientId.split(",");
				for (String k: it){
					if (!StrUtils.isEmpty(k))
					clients += "'" + k + "',";
				}
				if (clients.endsWith(",")) {
					clients = clients.substring(0, clients.length() - 1);
				}
				vo.setClientId(clients);
			} 
			
			jsonPaging = getJsonPaging(request);
			if ("".equals(jsonPaging.getOrder())) {
				jsonPaging.setOrder("desc");
//				jsonPaging.setSort("totalAuM");
				jsonPaging.setSort("total_return_rate");
			}
			
			jsonPaging = ifaClientService.findClentsByIfaFirm(jsonPaging, vo);

			if (null != jsonPaging.getList()) {
				Iterator it = jsonPaging.getList().iterator();
				while (it.hasNext()) {
					CrmClientVO clientVO = (CrmClientVO) it.next();
					boolean result = investorService.checkFacaOrCies(clientVO.getMemberId(), "faca");
					if (result) {
						clientVO.setFaca("1");
					} else {
						clientVO.setFaca("0");
					}
					result = investorService.checkFacaOrCies(clientVO.getMemberId(), "cies");
					if (result) {
						clientVO.setCies("1");
					} else {
						clientVO.setCies("0");
					}

					List checkList = crmCustomerService.checkBirthDayCustomer(ssoVO.getIfaId(), clientVO.getMemberId(), days);
					if (null != checkList && !checkList.isEmpty()) {
						clientVO.setIsBirthDay("1");
					} else {
						clientVO.setIsBirthDay("0");
					}

					checkList = crmCustomerService.checkSchedultCustomer(ssoVO.getIfaId(), clientVO.getMemberId(), startDate, endDate);
					if (null != checkList && !checkList.isEmpty()) {
						clientVO.setHasSchedule("1");
					} else {
						clientVO.setHasSchedule("0");
					}

					List productList = portfolioHoldService.findPortfolioAllocation(clientVO.getMemberId());
					List<CharDataVO> chartData = new ArrayList<CharDataVO>();
					if (null != productList) {
						Iterator iterator = productList.iterator();
						while (iterator.hasNext()) {
							Object[] obj = (Object[]) iterator.next();
							if (null == obj[0] || "".equals(obj[0]))
								continue;
							CharDataVO chart = new CharDataVO();
							chart.setName(obj[0].toString());
							chart.setValue(null != obj[1] ? Double.parseDouble(obj[1].toString()) : 0);
							chartData.add(chart);
						}
					}
					clientVO.setProductProJson(JsonUtil.toJson(chartData));
					String risk = investorService.findInvestorMaxRiskLevel(clientVO.getMemberId());
					clientVO.setRiskLevel(risk);
					clientVO.setAumStr(StrUtils.getNumberString(clientVO.getAum(), "#,##0.00"));
					clientVO.setTotalReturnRateStr(StrUtils.getNumberString(clientVO.getTotalReturnRate(), "#,##0.00"));
					clientVO.setTotalReturnStr(StrUtils.getNumberString(clientVO.getTotalReturn(), "#,##0.00"));

				}
			}

			JsonUtil.toWriter(jsonPaging, response);
		}
		return null;
	}

	/**
	 * 查看客户资料
	 */
	@RequestMapping(value = "/clientDetail", method = RequestMethod.GET)
	public String clientDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		return "console/ifaFirm/client/clientDetail";
	}

	/**
	 * 投资账户
	 */
	@RequestMapping(value = "/investAccounts", method = RequestMethod.GET)
	public String investAccounts(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String ifafirmId=ssoVO.getIfafirmId();
		String langCode=getLoginLangFlag(request);
		List list=ifafirmManageService.findAllDistributorList(ifafirmId);
		model.put("distributor", list);
		
		List<GeneralDataVO>  currencList=findSysParameters("currency_type", langCode);
		model.put("currencList", currencList);
		String currency=ssoVO.getDefCurrency();
		if(null==currency || "".equals(currency)){
			currency=CommonConstants.DEF_CURRENCY;
		}
		String currencyName=sysParamService.findNameByCode(currency, langCode);
		model.put("currency", currency);
		model.put("curName", currencyName);
		
		int decimal=2;
		if("JPY".equals(currency)){
			decimal=0;
		}
		model.put("decimal", decimal);
		return "console/ifafirm/client/investAccounts";
	}

	/**
	 * 查看客户投资账户
	 */
	@RequestMapping(value = "/investAccountInfo", method = RequestMethod.GET)
	public String investAccountInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifaFirm/client/investAccountInfo";
	}

	/**
	 * 查看客户交易记录
	 */
	@RequestMapping(value = "/transRecordDetail", method = RequestMethod.GET)
	public String transRecordDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "ifaFirm/client/transRecordDetail";
	}

	/**
	 * KYC记录
	 */
	@RequestMapping(value = "/kyc", method = RequestMethod.GET)
	public String kyc(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		this.isMobileDevice(request, model);
		MemberSsoVO sso = this.getLoginUserSSO(request);
		//未登录，跳转到登录页面
		if (null==sso){
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		Integer subMemberType = sso.getSubMemberType();
		String langCode = this.getLoginLangFlag(request);
		List<GeneralDataVO>  itemList=findSysParameters("currency_type", langCode);
		model.put("currencyType", itemList);
		
		//设置查询条件
		String inUse=request.getParameter("in_use");//账户状态
		String inApproval=request.getParameter("inApproval");
		String cancellation=request.getParameter("cancellation");
		String currency=request.getParameter("cur");//基本货币
		
        String check=request.getParameter("check");
		
        //openStatus: -1 退回, 0 草稿, 1 等待投资人确认, 2 处理中 ,3 开户成功,4 开户失败
		String status="";
		if(null!=inUse && "1".equals(inUse)){
			status+="'3'";
		}
		if(null!=inApproval && "1".equals(inApproval)){
			status+="'-1','1','2','4'";
		}
		if(status.endsWith(",")){
			status=status.substring(0,status.length()-1);
		}
		
		String isValid="";
		if(null!=cancellation && "1".equals(cancellation)){
			isValid="0";
			
		}
		if(null==currency || "".equals(currency))
			currency=sso.getDefCurrency();
		if(null==currency || "".equals(currency))
			currency=CommonConstants.DEF_CURRENCY;
		
		if(31==subMemberType){//distributor登录
			
			return "console/ifaFirm/client/distributorKyc";
		}else if(22==subMemberType){//ifaFirm登录
			ClientSearchVO searchVo = new ClientSearchVO();
			searchVo.setIfaFirmId(sso.getIfafirmId());
//			searchVo.setDistributorId(sso.getDistributorId());
			searchVo.setOpenStatus(status);
			//account.setBaseCurrency(currency);//货币类型不作为查询条件
			searchVo.setIsValid(isValid);
			
			List distributorList=investorService.findDistributorByCriteria(searchVo);
			model.put("distributorList", distributorList);
			
			return "console/ifaFirm/client/ifaFirmKyc";
		}
		return "redirect:"+this.getFullPath(request)+"index.do";		
//		return "console/ifaFirm/client/kyc";
	}

	/**
	 * 查看客户KYC记录
	 */
	@RequestMapping(value = "/kycInfo", method = RequestMethod.GET)
	public String kycInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		MemberBase loginUser = getLoginUser(request);
		String accountId = request.getParameter("id");
		String currency = request.getParameter("cur");
		InvestorAccount account = investorService.findInvestorAccountById(accountId);
		String langCode = this.getLoginLangFlag(request);

		if (null != account) {
			if (null == currency || "".equals(currency))
				currency = account.getBaseCurrency();

			/*
			 * String check=request.getParameter("check"); if(check==null ||
			 * check=="0"){ AccountValidVO
			 * validVO=iTraderSendService.saveCheckExistValid(request,
			 * loginUser.getId()); if(null!=validVO){ model.put("checkList",
			 * validVO.getValidAccountNo());
			 * if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR
			 * ==loginUser.getMemberType()) model.put("accountType",
			 * CommonConstantsWeb.WEB_ACCOUNTCHECK_INVESTOR); else {
			 * model.put("accountType",
			 * CommonConstantsWeb.WEB_ACCOUNTCHECK_IFA); } }
			 * 
			 * }
			 */

			InvestorAccountVO vo = new InvestorAccountVO();
			vo.setAccountNo(account.getAccountNo());
			vo.setAccType(account.getAccType());
			vo.setAuthorized(account.getAuthorized());
			vo.setBaseCurrency(account.getBaseCurrency());
			if (null != account && null != account.getMember())
				vo.setIconUrl(account.getMember().getIconUrl());
			vo.setCies(account.getCies());
			vo.setFaca(account.getFaca());
			
			if (null!=account.getDistributor()){
				vo.setDistributorId(account.getDistributor().getId());
				vo.setDistributorName(account.getDistributor().getCompanyName());
				vo.setDistributorIconUrl(PageHelper.getLogoUrl(account.getDistributor().getLogofile(), "D"));
			}
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
			vo.setMemberId(account.getMember().getId());

			model.put("account", vo);

			// List<InvestorAccount> subList =
			// investorService.findSubAccountList(account);
			List<InvestorAccountCurrencyVO> subList = investorService.findAccountCurrencyList(vo.getAccountNo());
			model.put("subList", subList);
			// 获取hold数据
			int number = 0;
			List<InvestorAccountHoldVO> investorAccountHoldVOList = new ArrayList<InvestorAccountHoldVO>();
			List<PortfolioHoldProduct> portfolioHoldProductList = investorService.findPortfolioHoldProductByAccountId(accountId);
			if (null != portfolioHoldProductList && !portfolioHoldProductList.isEmpty()) {
				for (PortfolioHoldProduct each : portfolioHoldProductList) {
					number++;
					InvestorAccountHoldVO holdvo = new InvestorAccountHoldVO();
					holdvo.setNumber(number);
					holdvo.setAccountId(accountId);
					holdvo.setAvailableUnit(each.getAvailableUnit());
					holdvo.setBaseCurrency(each.getBaseCurrency());
					// holdvo.setMemberId(memberId);
					holdvo.setReferencecost(each.getReferenceCost());
					holdvo.setHoldingUnit(each.getHoldingUnit());
					// 获取产品信息
					ProductInfo product = each.getProduct();
					if (null != product && "" != product.getId()) { // 获取持苍产品，判断是基金或股票或债卷
																	// 产品类型,fund基金,stock股票,bond债券,futures期货
						String productType = product.getType();
						String productId = product.getId();
						if ("fund".equals(productType)) {// 如果是基金
							FundInfo fund = fundInfoService.getFundInfoByProductId(productId, langCode);
							if (null != fund) {
								holdvo.setProductInformation(fund.getTempFundName());
								holdvo.setLatestMarketPrice(fund.getLastNav());
								holdvo.setProductId(fund.getId());
							}
						} else if ("stock".equals(productType)) {

						} else if ("bond".equals(productType)) {
							BondInfo bond = fundInfoService.getBondInfoByProductId(productId, langCode);
							if (null != bond) {
								holdvo.setProductInformation(bond.getBondName());
								holdvo.setLatestMarketPrice(StrUtils.getDouble(bond.getExchangeCode()));
							}
						}
						holdvo.setProductType(productType);
					}
					investorAccountHoldVOList.add(holdvo);
				}
			}
			model.put("investorAccountHoldVOList", investorAccountHoldVOList);
			// 获取该账号的KYC、背景信息、培训信息、交易记录
			// 判断当前登录的角色是投资人还是IFA
			Integer memberType = 0;
			// MemberBase loginUser = getLoginUser(request);
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
			for (int i = 0; i < trainingList.size(); i++) {
				trainingList.get(i).setNumber(i + 1);
			}
			model.put("trainingList", trainingList);

			List<RpqExam> rqpExamList = investorService.findRpqExamByMemberId(langCode, account.getMember().getId());
			if (1 == memberType) { // 如果是投资人，取最新一条
				if (null != rqpExamList && !rqpExamList.isEmpty()) {
					RpqExam latestRpq = rqpExamList.get(0);
					if (null != latestRpq) {
						Integer riskLevel = latestRpq.getRiskLevel();
						model.put("riskLevel", riskLevel);
						Date lastUpdate = latestRpq.getLastUpdate();
						Date expireDate = latestRpq.getExpireDate();
						try {
							if (null != expireDate && null != expireDate) {
								int betweenDay = DateUtil.daysBetween(lastUpdate, expireDate);
								model.put("betweenDay", betweenDay);
							} else {
								model.put("betweenDay", "");
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

			//List<InvestorAccountStream> orderReturnList = investorService.findAccountOrderReturn(langCode, accountId, "");
			jsonPaging = this.getJsonPaging(request);
			jsonPaging = investorService.findAccountOrderReturn(jsonPaging,"", accountId,"");
			model.put("orderReturnList", jsonPaging.getList());
			// **************获取该账号的KYC、背景信息、培训信息、交易记录结束*****************/

			// ****************kyc文档审查begin
			// *************************************************/
			String clientType = "J".equals(account.getAccType()) ? "JonitAccount" : "Individual";
			// 1==sub_flag 子账户，0==sub_flag 主账户
			InvestorAccount masterAccount = "1".equals(account.getSubFlag()) ? account.getMasterAccount() : account;

			// kyc文档审核列表信息-scshi
			String mainContactId = investorService.getAccountContactId(accountId, "M");
			List<DocListVO> mainDoc = investorService.findContactDocList(vo.getMemberId(), vo.getDistributorId(), mainContactId, langCode,
			        masterAccount.getId());
			if (null == mainDoc || mainDoc.isEmpty()) {
				// 如果是首次进入文档检查，从模板复制doclist到invest
				investorService.copyDocListfromTemp(vo.getDistributorId(), clientType, this.getLoginLangFlag(request), vo.getMemberId(),
				        mainContactId, account);
			} else {
				// 判断文档是否有更新
				kycDocCheckServic.checkTemplateUpdate(vo, mainContactId, clientType, masterAccount.getId());
			}
			mainDoc = investorService.findContactDocList(vo.getMemberId(), vo.getDistributorId(), mainContactId, this.getLoginLangFlag(request),
			        masterAccount.getId());
			model.put("mainDoc", mainDoc);

			/****
			 * 废除，investorDoc与联系人关系脱离，无法判断文档归属 if
			 * ("J".equals(account.getAccType())) { String jointContactId =
			 * investorService.getAccountContactId(accountId, "J");
			 * List<DocListVO> jointDoc =
			 * investorService.findContactDocList(vo.getMemberId(),
			 * vo.getDistributorId(), jointContactId, langCode,account.getId());
			 * if (null == jointDoc || jointDoc.isEmpty()) { //
			 * 如果是首次进入文档检查，从模板复制doclist到invest
			 * investorService.copyDocListfromTemp(vo.getDistributorId(),
			 * clientType, this.getLoginLangFlag(request), vo.getMemberId(),
			 * jointContactId,account); }else{ //判断文档是否有更新
			 * kycDocCheckServic.checkTemplateUpdate
			 * (vo,jointContactId,clientType,docCode); }
			 * 
			 * jointDoc = investorService.findContactDocList(vo.getMemberId(),
			 * vo.getDistributorId(), jointContactId,
			 * this.getLoginLangFlag(request),account.getId());
			 * model.put("jointDoc", jointDoc); }
			 ***/
			// ****************kyc文档审查end
			// *************************************************/

			model.put("cur", currency);

			List<GeneralDataVO> itemList = findSysParameters("currency_type", langCode);
			model.put("currencyType", itemList);

		}

		return "console/ifaFirm/client/clientDetailKyc";
	}
	
	
	
	/**
	 * 交易记录
	 * @author wwluo
	 * @date 2017-06-12
	 */
	@RequestMapping(value = "/transRecords", method = RequestMethod.GET)
	public String transRecords(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		String role = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE);
		if (memberAdmin != null || CoreConstants.FRONT_USER_SYS_ADMIN.equals(role)){
			String currencyCode = memberAdmin.getMember().getDefCurrency();
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = CommonConstants.DEF_CURRENCY;
			}
			if("2".equals(memberAdmin.getType()) && memberAdmin.getDistributor() != null){
				List<MemberIfafirmBaseVO> ifafirms = distributorService.findAllIfafirm(memberAdmin.getDistributor().getId(), langCode);
				model.put("ifafirms", ifafirms);
			}
			model.put("adminType", memberAdmin.getType());
			model.put("currencyCode", currencyCode);
			return "console/crm/transactionRecord/transactionPage";
		}else{
			return "redirect:" + this.getFullPath(request) + "front/index.do";
		}
	}

	/**
	 * 交易记录分页信息
	 * @author wwluo
	 * @date 2017-06-12
	 * @param transRecordVO 条件查询字段
	 */
	@RequestMapping(value = "/transRecordsJson", method = RequestMethod.POST)
	public void transRecordsJson(TransRecordVO transRecordVO, HttpServletRequest request, HttpServletResponse response, ModelMap model, TransactionRecordFilterVO filter) {
		MemberAdmin memberAdmin = this.getLoginMemberAdmin(request);
		String role = (String) request.getSession().getAttribute(CoreConstants.FRONT_USER_ROLE);
		jsonPaging = this.getJsonPaging(request);
		if (memberAdmin != null || CoreConstants.FRONT_USER_SYS_ADMIN.equals(role)){
			String currencyCode = request.getParameter("currencyCode");
			String langCode = this.getLoginLangFlag(request);
			jsonPaging = ifaClientService.getTransRecords(memberAdmin, jsonPaging, transRecordVO, langCode, currencyCode);
		}	
		JsonUtil.toWriter(jsonPaging, response);
	}
	
	/**
	 * 获取ifafirm的客户的投资帐号列表数据
	 * @author mqzou 2017-06-15
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/accountListJson")
	public void findAccountListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO ssoVO=getLoginUserSSO(request);
		String ifafirmId=ssoVO.getIfafirmId();
		String langCode=getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		
		String currency = request.getParameter("cur");// 基本货币
		String distributorId = request.getParameter("distributorId");
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
		vo.setKeyword(keyword);
		
		jsonPaging = getJsonPaging(request);
		jsonPaging = distributorService.findAccountList(jsonPaging, vo);


		result.put("accountList", jsonPaging.getList());
		result.put("total", jsonPaging.getTotal());
		result.put("currency", currency);
		JsonUtil.toWriter(result, response);
	}
}
