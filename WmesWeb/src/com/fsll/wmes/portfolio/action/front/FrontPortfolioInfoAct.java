package com.fsll.wmes.portfolio.action.front;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import com.fsll.buscore.fund.service.CorePortfolioService;
import com.fsll.buscore.fund.vo.CorePortfolioVO;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.PortfolioInfoProduct;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebPushDetail;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.portfolio.service.PortfolioInfoService;
import com.fsll.wmes.portfolio.vo.CriteriaVO;
import com.fsll.wmes.portfolio.vo.PorfolioProductVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebPushVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebViewVO;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.web.service.WebFollowStatusService;

/**
 * 控制器:基金组合管理（主网站）
 * 
 * @author michael
 * @version 1.0.0 Created On: 2016-8-17
 */

@Controller
@RequestMapping("/front/portfolio/info")
public class FrontPortfolioInfoAct extends WmesBaseAct {

	@Autowired
	private PortfolioInfoService portfolioInfoService;

	@Autowired
	private StrategyInfoService strategyInfoService;

	@Autowired
	private FundInfoService fundInfoService;

	// @Autowired
	// private WebFollowStatusService webFollowService;

	@Autowired
	private InvestorService investorService;

	@Autowired
	private MemberBaseService memberBaseService;
	
	@Autowired
	private CorePortfolioService corePortfolioService;

	/**
	 * 我的投资组合分页列表
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/myList", method = RequestMethod.GET)
	public String myList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		if (loginUser != null) {
			this.isMobileDevice(request, model);

			// 地区列表
			List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, this.getLoginLangFlag(request));
			model.put("regionList", itemList);

			// 主题分类
			itemList = findSysParameters("fund_sector", this.getLoginLangFlag(request));
			model.put("sectorList", itemList);

			// jsonPaging = findList(request, response, model);
			// model.put("dataList", jsonPaging.getList());

			return "portfolio/info/myList";// 页面摆放路径
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/**
	 * 获取基金
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public JsonPaging findList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		CriteriaVO criteria = new CriteriaVO();
		if (loginUser != null) {

			// 获取发布日期条件
			String period = StrUtils.getString(request.getParameter("period"));
			String startDate = "";// DateUtil.getCurDateStr();
			String endDate = DateUtil.getCurDateStr(Calendar.DATE, 1);// DateUtil.getCurDateStr();
			if ("1W".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
			if ("1M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -1);
			if ("3M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -3);
			if ("6M".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.MONTH, -6);
			if ("1Y".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.YEAR, -1);
			if ("2Y".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.YEAR, -2);
			if ("3Y".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.YEAR, -3);
			if ("5Y".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.YEAR, -5);
			if ("10Y".equals(period))
				startDate = DateUtil.getCurDateStr(Calendar.YEAR, -10);
			if ("YTD".equals(period))
				startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());// 今年以来
			String fromDate = request.getParameter("fromDate");
			if (null != fromDate && fromDate.length() == 10)
				startDate = fromDate;
			String toDate = request.getParameter("toDate");
			if (null != toDate && toDate.length() == 10)
				endDate = toDate;
			// 开始日期统一提前一天
			if (!"".equals(startDate))
				startDate = DateUtil.addDateToString(startDate, Calendar.DATE, -1);

			String keyword = toUTF8String(request.getParameter("keyWord"));
			String sector = toUTF8String(request.getParameter("sectors"));
			String geoAllocation = toUTF8String(request.getParameter("regions"));

			String viewSort = request.getParameter("viewSort");
			String issuedDateSort = request.getParameter("issuedDateSort");

			String status = StrUtils.getString(request.getParameter("status"));
			if ("".equals(status))
				status = "1";// 默认查询已发布的记录

			String orderBy = "";

			// 设置筛选条件
			criteria.setUserId(loginUser.getId());
			criteria.setPeriod(period);
			criteria.setStartDate(startDate);
			criteria.setEndDate(endDate);
			criteria.setKeyword(keyword);
			criteria.setSector(sector);
			criteria.setGeoAllocation(geoAllocation);
			criteria.setStatus(status);

			jsonPaging = this.getJsonPaging(request);

			if (null != issuedDateSort && !"".equals(issuedDateSort)) {// 发布日期排序
				orderBy = " lastUpdate " + issuedDateSort;

			} else if (null != viewSort && !"".equals(viewSort)) {// 点击次数排序
				orderBy = " click " + viewSort;
			}
			criteria.setOrderBy(orderBy);

			jsonPaging = portfolioInfoService.findByUser(jsonPaging, criteria);
		}
		return jsonPaging;
	}

	/**
	 * 分页获得Json记录
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/myListJson", method = RequestMethod.POST)
	public void listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		jsonPaging = findList(request, response, model);
		ResponseUtils.renderHtml(response, JsonUtil.toJson(jsonPaging));

	}

	/**
	 * 新增投资组合
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		if (loginUser != null) {
			PortfolioInfo obj = new PortfolioInfo();
			model.put("portfolioVO", obj);
			return "portfolio/info/add";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/**
	 * 修改投资组合
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		if (loginUser != null) {
			String id = request.getParameter("id");
			PortfolioInfo obj = portfolioInfoService.findById(id);
			model.put("portfolioVO", obj);

			// 基金列表
			List<FundInfo> fundList = portfolioInfoService.findFundListByPortfolio(id);
			String fundIds = "";
			if (!fundList.isEmpty())
				for (FundInfo x : fundList) {
					if (null != x && null != x.getId() && !"".equals(x.getId()))
						fundIds += x.getId() + ",";
				}
			model.put("fundIds", fundIds);

			// 查看权限
			PortfolioWebViewVO webViewVO = portfolioInfoService.findWebViewByPortfolio(id);
			if (null == webViewVO)
				webViewVO = new PortfolioWebViewVO();
			model.put("webViewVO", webViewVO);
			// 推送权限
			PortfolioWebPushVO webPushVO = portfolioInfoService.findWebPushByPortfolio(id);
			if (null == webPushVO)
				webPushVO = new PortfolioWebPushVO();
			model.put("webPushVO", webPushVO);

			// 客户类型：Buddy， Client， Advisor， Prospect
			List<MemberIndividual> clients = investorService.findClientByIFA(loginUser.getId(), "Client");
			model.put("cnt_clients", null == clients ? 0 : clients.size());

			List<MemberIndividual> prospects = investorService.findClientByIFA(loginUser.getId(), "Prospect");
			model.put("cnt_prospects", null == prospects ? 0 : prospects.size());

			List<MemberIndividual> buddies = investorService.findClientByIFA(loginUser.getId(), "Buddy");
			model.put("cnt_buddies", null == buddies ? 0 : buddies.size());

			MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
			List<MemberIfa> colleagues = investorService.findIfaColleagues(ifa.getMember().getId(), ifa.getIfafirm().getId());
			model.put("cnt_colleagues", null == colleagues ? 0 : colleagues.size());

			return "portfolio/info/edit";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/**
	 * 修改投资组合
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);

		if (loginUser != null) {
			String id = request.getParameter("id");
			PortfolioInfo obj = portfolioInfoService.findById(id);
			model.put("portfolioVO", obj);

			// 基金列表
			// List<FundInfo> fundList =
			// portfolioInfoService.findFundListByPortfolio(id);
			String fundIds = "";
			// if (!fundList.isEmpty())
			// for (FundInfo x : fundList){
			// if (null!=x && null!=x.getId() && !"".equals(x.getId()))
			// fundIds+=x.getId()+",";
			// }
			model.put("fundIds", fundIds);

			// 查看权限
			PortfolioWebViewVO webViewVO = portfolioInfoService.findWebViewByPortfolio(id);
			if (null == webViewVO)
				webViewVO = new PortfolioWebViewVO();
			model.put("webViewVO", webViewVO);
			// 推送权限
			PortfolioWebPushVO webPushVO = portfolioInfoService.findWebPushByPortfolio(id);
			if (null == webPushVO)
				webPushVO = new PortfolioWebPushVO();
			model.put("webPushVO", webPushVO);

			// 客户类型：Buddy， Client， Advisor， Prospect
			List<MemberIndividual> clients = investorService.findClientByIFA(loginUser.getId(), "Client");
			model.put("cnt_clients", null == clients ? 0 : clients.size());

			List<MemberIndividual> prospects = investorService.findClientByIFA(loginUser.getId(), "Prospect");
			model.put("cnt_prospects", null == prospects ? 0 : prospects.size());

			List<MemberIndividual> buddies = investorService.findClientByIFA(loginUser.getId(), "Buddy");
			model.put("cnt_buddies", null == buddies ? 0 : buddies.size());

			MemberIfa ifa = memberBaseService.findIfaMember(loginUser.getId());
			List<MemberIfa> colleagues = investorService.findIfaColleagues(ifa.getMember().getId(), ifa.getIfafirm().getId());
			model.put("cnt_colleagues", null == colleagues ? 0 : colleagues.size());

			return "portfolio/info/view";
		} else {// 未登录，跳转到登录页面
			return "redirect:" + this.getFullPath(request) + "front/viewLogin.do";
		}
	}

	/**
	 * 保存投资组合
	 * 
	 * @author michael
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/savePortfolio", method = RequestMethod.POST)
	public String savePortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser != null) {
			try {
				// 编辑状态时使用
				String id = request.getParameter("id");// portfolio id

				String fundIds = request.getParameter("ids");

				String portfolioName = toUTF8String(request.getParameter("portfolioName"));
				// String investmentGoal =
				// toUTF8String(request.getParameter("investmentGoal"));
				// String suitability =
				// toUTF8String(request.getParameter("suitability"));

				String permit = StrUtils.getString(request.getParameter("permit"));
				String permitClients = StrUtils.getString(request.getParameter("permit_clients"));
				String permitProspects = StrUtils.getString(request.getParameter("permit_prospects"));
				String permitBuddies = StrUtils.getString(request.getParameter("permit_buddies"));
				String permitColleagues = StrUtils.getString(request.getParameter("permit_colleagues"));
				String permitClient = StrUtils.getString(request.getParameter("permit_client"));
				String permitProspect = StrUtils.getString(request.getParameter("permit_prospect"));
				String permitBuddy = StrUtils.getString(request.getParameter("permit_buddy"));
				String permitColleague = StrUtils.getString(request.getParameter("permit_colleague"));

				String push = StrUtils.getString(request.getParameter("push"));
				String pushClients = StrUtils.getString(request.getParameter("push_clients"));
				String pushProspects = StrUtils.getString(request.getParameter("push_prospects"));
				String pushBuddies = StrUtils.getString(request.getParameter("push_buddies"));
				String pushColleagues = StrUtils.getString(request.getParameter("push_colleagues"));
				String pushClient = StrUtils.getString(request.getParameter("push_client"));
				String pushProspect = StrUtils.getString(request.getParameter("push_prospect"));
				String pushBuddy = StrUtils.getString(request.getParameter("push_buddy"));
				String pushColleague = StrUtils.getString(request.getParameter("push_colleague"));

				PortfolioInfo info = new PortfolioInfo();
				if (null != id && !"".equals(id)) {
					info = portfolioInfoService.findById(id);
				}
				if (null == info || null == info.getId()) {
					info = new PortfolioInfo();
					info.setCreator(loginUser);
					info.setCreateTime(new Date());
					info.setIsValid("1");
					// info.setOverhead("0");//默认不置顶
					// info.setStatus("0");//草稿
				}
				info.setPortfolioName(portfolioName);
				// info.setInvestmentGoal(investmentGoal);
				// info.setSuitability(suitability);
				info.setLastUpdate(new Date());

				// info.setIsPublic(isPublic);
				// info.setOverheadTime(overheadTime);
				// info.setDescription(portfolioName);

				// 保存投资组合信息
				info = portfolioInfoService.saveOrUpdate(info);

				// 保存投资组合的产品信息
				try {
					List regionList = new ArrayList();
					List sectorList = new ArrayList();
					if (null != fundIds && !"".equals(fundIds)) {
						String[] idArrStrings = fundIds.split(",");
						if (idArrStrings != null && idArrStrings.length > 0) {
							for (int k = 0; k < idArrStrings.length; k++) {
								String fundId = idArrStrings[k].toString();
								if (fundId != null && fundId.length() > 0) {
									FundInfoEn fundInfo = fundInfoService.findFundInfoEnById(fundId);

									if (!regionList.contains(fundInfo.getGeoAllocationCode()))
										regionList.add(fundInfo.getGeoAllocationCode());
									if (!sectorList.contains(fundInfo.getSectorTypeCode()))
										sectorList.add(fundInfo.getSectorTypeCode());

									// 保存组合与产品信息
									// portfolioInfoService.saveOrUpdate(info,
									// fundId);
								}
							}
						}
					}

					// 更新补充信息
					// info.setGeoAllocation(StrUtils.arrayListToString(regionList,
					// ","));
					// info.setSector(StrUtils.arrayListToString(sectorList,
					// ","));
					info = portfolioInfoService.saveOrUpdate(info);
				} catch (Exception e) {
					// TODO: handle exception
				}

				// 保存推送权限信息
				try {
					WebPush webPush = new WebPush();
					PortfolioWebPushVO webPushVO = portfolioInfoService.findWebPushByPortfolio(id);
					if (null != webPushVO && null != webPushVO.getId() && !"".equals(webPushVO.getId())) {
						webPush = portfolioInfoService.findWebPushById(webPushVO.getId());
						// BeanUtils.copyProperties(webPushVO,webPush);//拷贝信息
					} else {// 新建
						webPush.setModuleType(CommonConstantsWeb.WEB_PUSH_MODULE_STRATEGY);
						webPush.setRelateId(info.getId());
						webPush.setCreateTime(new Date());
						webPush.setFromMember(loginUser);
					}
					webPush.setScopeFlag(push);
					webPush.setLastUpdate(new Date());
					if ("2".equals(push)) {// setting
						// 1 - 全选，0 - 无， -1 - 选部分
						webPush.setBuddyFlag(pushBuddy);
						webPush.setClientFlag(pushClient);
						webPush.setColleagueFlag(pushColleague);
						webPush.setProspectFlag(pushProspect);
					} else if ("1".equals(push)) {// all
						webPush.setBuddyFlag("1");
						webPush.setClientFlag("1");
						webPush.setColleagueFlag("1");
						webPush.setProspectFlag("1");
					} else {// only me
						webPush.setBuddyFlag("0");
						webPush.setClientFlag("0");
						webPush.setColleagueFlag("0");
						webPush.setProspectFlag("0");
					}
					webPush = strategyInfoService.saveWebPush(webPush, pushClients, pushProspects, pushBuddies, pushColleagues);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				// 保存查看权限信息
				try {
					WebView webView = new WebView();
					PortfolioWebViewVO webViewVO = portfolioInfoService.findWebViewByPortfolio(id);
					if (null != webViewVO && null != webViewVO.getId() && !"".equals(webViewVO.getId())) {
						webView = portfolioInfoService.findWebViewById(webViewVO.getId());
						// BeanUtils.copyProperties(webViewVO,webView);//拷贝信息
					} else {// 新建
							// 对应模块,insight观点,news新闻,portfolio组合,portfolio_info组合,portfolio_arena组合,live_info直播
						webView.setModuleType(CommonConstantsWeb.WEB_VIEW_MODULE_STRATEGY);
						webView.setRelateId(info.getId());
						webView.setCreateTime(new Date());
						webView.setFromMember(loginUser);
					}
					webView.setLastUpdate(new Date());
					webView.setScopeFlag(permit);
					if ("2".equals(permit)) {// setting
						// 1 - 全选，0 - 无， -1 - 选部分
						webView.setBuddyFlag(permitBuddy);
						webView.setClientFlag(permitClient);
						webView.setColleagueFlag(permitColleague);
						webView.setProspectFlag(permitProspect);
					} else if ("1".equals(permit)) {// all
						webView.setBuddyFlag("1");
						webView.setClientFlag("1");
						webView.setColleagueFlag("1");
						webView.setProspectFlag("1");
					} else {
						webView.setBuddyFlag("0");
						webView.setClientFlag("0");
						webView.setColleagueFlag("0");
						webView.setProspectFlag("0");
					}
					webView = strategyInfoService.saveWebView(webView, permitClients, permitProspects, permitBuddies, permitColleagues);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				// 发布
				// try {
				// String ifPublish =
				// StrUtils.getString(request.getParameter("ifPublish"));
				// if ("true".equals(ifPublish)){
				// info.setStatus("1");
				// portfolioInfoService.saveOrUpdate(info);
				// }
				// } catch (Exception e) {
				// // TODO: handle exception
				// e.printStackTrace();
				// }

				result.put("result", true);
				result.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success", null));
				this.saveOperLog(request, loginUser.getLoginCode(), "", CoreConstants.FRONT_LOG_PERSONAL_INFO, "保存密码成功");
			} catch (Exception e) {
				// TODO: handle exception
				result.put("result", false);
				result.put("msg", e.getMessage());
				this.saveOperLog(request, loginUser.getLoginCode(), "", CoreConstants.FRONT_LOG_PERSONAL_INFO, "保存密码失败");
			}

		} else {// 未登录，跳转到登录页面
			result.put("result", false);
			result.put("msg", "User is not login.");
		}
		ResponseUtils.renderHtml(response, JsonUtil.toJson(result));

		return null;
	}

	/**
	 * 设置/取消【置顶】
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/setOverhead", method = RequestMethod.POST)
	public void setOverhead(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser != null) {
			String id = StrUtils.getString(request.getParameter("itemId"));
			String type = StrUtils.getString(request.getParameter("type"));

			// 如果是设置【置顶】，那么先清除之前的所有置顶。
			if (null != type && "1".equals(type))
				strategyInfoService.clearOverhead(loginUser.getId());

			PortfolioInfo info = portfolioInfoService.findById(id);
			if (null != info) {
				// info.setOverhead(type);
				// info.setOverheadTime(new Date());
				portfolioInfoService.saveOrUpdate(info);
			}
			result.put("result", true);
			result.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success", null));

		} else {// 未登录，跳转到登录页面
			result.put("result", false);
			result.put("code", "error.noLogin");
			result.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "error.noLogin", null));
		}
		JsonUtil.toWriter(result, response);
	}

	/**
	 * 删除组合
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/delPortfolio", method = RequestMethod.POST)
	public void delPortfolio(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if (loginUser != null) {
			String id = StrUtils.getString(request.getParameter("itemId"));

			PortfolioInfo info = portfolioInfoService.findById(id);
			portfolioInfoService.delete(info);

			result.put("result", true);
			result.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "global.success", null));

		} else {// 未登录，跳转到登录页面
			result.put("result", false);
			result.put("code", "error.noLogin");
			result.put("msg", PropertyUtils.getPropertyValue(this.getLoginLangFlag(request), "error.noLogin", null));
		}
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 供定时服务调用，请勿删除或改动
	 */
	@RequestMapping(value = "/getCorefundNavList", method = RequestMethod.GET)
	public void getCorefundNavList(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		/*String typeId = request.getParameter("typeId");
		String typeName = request.getParameter("typeName");

		Map<String, Object> obj = new HashMap<String, Object>();*/
		//List<CoreFundNavVO> list = coreFundServiceImpl.getFundNav("ddf4b0a227164acaa51ab51c7b3632b2","YTD","");
		//List<CoreFundNavVO> list = coreFundServiceImpl.getMoreFundReturnRateForAPP("STOCK_02838,STOCK_02801,STOCK_03009,BOND_XS0521073428,FUND_LU0094541447,FUND_IE0001148372,FUND_LU0140363002,FUND_LU0251130802,","0.0425,0.042,0.0328,0.003,0.2928,0.4147,0.0719,0.1133,", "3M","tc");
		//CoreFundsReturnForChartsVO list = coreFundServiceImpl.getMoreFundReturnRate("673309b8651a484f9757d7a67d330fff,5aa1f6ded207493b8329a4c9520537c0,80b7559ccf8e41c5bf7c2f2d9320612c","0.3,0.2,0.5", "3M","tc");
		//List<CoreFundsReturnForChartsVO> list = coreFundServiceImpl.getMulFundReturnRate("579ded9dec95411dbb9ff80b76394784,ddf4b0a227164acaa51ab51c7b3632b2", "1Y","","tc");
		String portfolioId = request.getParameter("portfolioId");
		String frequencyType = request.getParameter("frequencyType");
		List<CorePortfolioVO> list = corePortfolioService.findArenaReturnRate(portfolioId, frequencyType,"");
		int length = list.size();
		CorePortfolioVO vo = list.get(length-1);
		//把数据
		//609ac4eeac7147e99aa5d0ab6d91f890 579ded9dec95411dbb9ff80b76394784 d4a9c3a7b51046b19363473602858b2d
		//6d5f7b07474a4fddbf3c83e937735af6  80b7559ccf8e41c5bf7c2f2d9320612c
		JsonUtil.toWriter(vo.getReturnRate(), response);
		//ResponseUtils.renderHtml(response, JsonUtil.toJson(list));
	}
}
