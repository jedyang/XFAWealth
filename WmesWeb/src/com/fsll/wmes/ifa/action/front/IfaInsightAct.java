package com.fsll.wmes.ifa.action.front;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.entity.WebViewDetail;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.IfaInfoService;
import com.fsll.wmes.ifa.service.InsightInfoService;
import com.fsll.wmes.ifa.vo.IfaClientVO;
import com.fsll.wmes.ifa.vo.InsightSearchParamVo;
import com.fsll.wmes.ifa.vo.InsightVistorVo;
import com.fsll.wmes.portfolio.vo.PortfolioWebPushVO;
import com.fsll.wmes.portfolio.vo.PortfolioWebViewVO;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.strategy.vo.StrategyWebViewVO;
import com.fsll.wmes.web.service.WebFriendService;

/**
 * @author scshi
 *我的观点
 */
@Controller
@RequestMapping("/front/ifa/myInsight")
public class IfaInsightAct extends WmesBaseAct{
	
	@Autowired
	private InsightInfoService insightInfoService;
	
	@Autowired
	private StrategyInfoService strategyInfoService;
	
	@Autowired
	private IfaInfoService ifaInfoService;
	
	@Autowired
	private WebFriendService webFriendService;
	
	/**
	 * 我的观点列表页面
	 * @author scshi
	 * */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String insightList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		

		String langCode=this.getLoginLangFlag(request);
    	// 地区列表
        List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, langCode);
        model.put("regionList", itemList);
        // 主题分类
        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR,langCode);
        model.put("sectorList", itemList);
        // 搜索时段
        itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SEARCH_PERIOD, this.getLoginLangFlag(request));
        model.put("periodList", itemList);
		return "ifa/insight/insihtList";
	}
	
	/**
	 * 观点列表
	 * @author scshi      modify by wwluo 2017/1/19
	 * */
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void insightListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase baseMem = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		jsonPaging = this.getJsonPaging(request);
		InsightSearchParamVo searchParam = new InsightSearchParamVo();
		searchParam.setIssuedDate(request.getParameter("issuedDate"));
		searchParam.setIssuedDateSort(request.getParameter("issuedDateSort"));
		searchParam.setViewSort(request.getParameter("viewSort"));
		searchParam.setSector(this.toUTF8String(request.getParameter("sector")));
		searchParam.setKeyWord(request.getParameter("keyWord"));
		
		searchParam.setFromDate(request.getParameter("fromDate"));
		searchParam.setToDate(request.getParameter("toDate"));
		searchParam.setAllocation(this.toUTF8String(request.getParameter("allocation")));
		searchParam.setStatus(request.getParameter("status"));
		searchParam.setLangCode(langCode);
		jsonPaging = insightInfoService.findIfaInsightListForWeb(jsonPaging, baseMem.getId(), searchParam,baseMem.getId());
		this.toJsonString(response, jsonPaging);
	}
	
	
	/**
	 * 获取查看观点的ifaFriends
	 * @author scshi
	 * */
	@RequestMapping(value = "/viewList", method = RequestMethod.POST)
	public void friendViewList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		jsonPaging = this.getJsonPaging(request);
		String langCode=getLoginLangFlag(request);
		String insightId = request.getParameter("insightId");
		jsonPaging =  insightInfoService.loadVistorList(jsonPaging,insightId,"insight",langCode);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
	}
	/**
	 * 观点编辑
	 * @author scshi
	 * */
	@RequestMapping(value="/insightEdit",method= RequestMethod.GET)
	public String insightEdit(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String insightId = request.getParameter("insightId");
		String langCode = this.getLoginLangFlag(request);
		MemberBase loginUser = this.getLoginUser(request);
		InsightInfo insight = new InsightInfo();
		model.put("insightId", insightId);
		WebView webView = null;
		if(null==insightId || "".equals(insightId)){
			insight.setCreateTime(new Date());
			insight.setCreator(this.getLoginUser(request));
			
			model.put("notMySector", findSysParameters("fund_sector",langCode) );
			model.put("notMyAllocation", findSysParameters("service_region", langCode) );
		}else{
			insight = insightInfoService.findInsightById(insightId, langCode);
			if(insight != null){
				webView = insightInfoService.getViewByRelateAndModule(insight.getId(),CommonConstantsWeb.WEB_VIEW_MODULE_INSIGHT);
				if (webView != null && "3".equals(webView.getScopeFlag())) {
					List<IfaClientVO> prospects = new ArrayList<IfaClientVO>();
					List<IfaClientVO> existings = new ArrayList<IfaClientVO>();
					List<IfaClientVO> buddies = new ArrayList<IfaClientVO>();
					List<IfaClientVO> colleagues = new ArrayList<IfaClientVO>();
					String prospectIds = "";
					String existingIds = "";
					String buddyIds = "";
					String colleagueIds = "";
					List<IfaClientVO> prospectsAll = ifaInfoService.findMyClientList(loginUser.getId(), "0",langCode);
					List<IfaClientVO> existingAll = ifaInfoService.findMyClientList(loginUser.getId(), "1",langCode);
					List<IfaClientVO> buddiesAll = webFriendService.findMyFriendList(loginUser.getId());
					List<IfaClientVO> colleaguesAll = ifaInfoService.findMyColleagueList(loginUser.getId(), langCode);
					List<WebViewDetail> details = insightInfoService.getViewDetailByView(webView.getId());
					if(details != null && !details.isEmpty()){
						if(prospectsAll != null && !prospectsAll.isEmpty()){
							for (IfaClientVO prospect : prospectsAll) {
								boolean flag = false;
								String id = prospect.getId();
								for (WebViewDetail webViewDetail : details) {
									if(id.equals(webViewDetail.getToMember().getId())){
										flag = true;
										break;
									}
								}
								if(flag && !prospects.contains(prospect)){
									prospects.add(prospect);
									prospectIds += prospect.getId() + ",";
								}
							}
							if(StringUtils.isNotBlank(prospectIds) && ',' == prospectIds.charAt(prospectIds.length()-1)){
								prospectIds = prospectIds.substring(0, prospectIds.length()-1);
							}
						}
						if(existingAll != null && !existingAll.isEmpty()){
							for (IfaClientVO existing : existingAll) {
								boolean flag = false;
								String id = existing.getId();
								for (WebViewDetail webViewDetail : details) {
									if(id.equals(webViewDetail.getToMember().getId())){
										flag = true;
										break;
									}
								}
								if(flag && !existings.contains(existing)){
									existings.add(existing);
									existingIds += existing.getId() + ",";
								}
							}
							if(StringUtils.isNotBlank(existingIds) && ',' == existingIds.charAt(existingIds.length()-1)){
								existingIds = existingIds.substring(0, existingIds.length()-1);
							}
						}
						if(buddiesAll != null && !buddiesAll.isEmpty()){
							for (IfaClientVO buddy : buddiesAll) {
								boolean flag = false;
								String id = buddy.getId();
								for (WebViewDetail webViewDetail : details) {
									if(id.equals(webViewDetail.getToMember().getId())){
										flag = true;
										break;
									}
								}
								if(flag && !buddies.contains(buddy)){
									buddies.add(buddy);
									buddyIds += buddy.getId() + ",";
								}
							}
							if(StringUtils.isNotBlank(buddyIds) && ',' == buddyIds.charAt(buddyIds.length()-1)){
								buddyIds = buddyIds.substring(0, buddyIds.length()-1);
							}
						}
						if(colleaguesAll != null && !colleaguesAll.isEmpty()){
							for (IfaClientVO colleague : colleaguesAll) {
								boolean flag = false;
								String id = colleague.getId();
								for (WebViewDetail webViewDetail : details) {
									if(id.equals(webViewDetail.getToMember().getId())){
										flag = true;
										break;
									}
								}
								if(flag && !colleagues.contains(colleague)){
									colleagues.add(colleague);
									colleagueIds += colleague.getId() + ",";
								}
							}
							if(StringUtils.isNotBlank(colleagueIds) && ',' == colleagueIds.charAt(colleagueIds.length()-1)){
								colleagueIds = colleagueIds.substring(0, colleagueIds.length()-1);
							}
						}
					}
					model.put("prospectIds", prospectIds);
					model.put("existingIds", existingIds);
					model.put("buddyIds", buddyIds);
					model.put("colleagueIds", colleagueIds);
					
					model.put("prospects", prospects);
					model.put("existings", existings);
					model.put("buddies", buddies);
					model.put("colleagues", colleagues);
				}
			}
			//Sector选项--擅长领域
			List<GeneralDataVO> sectorList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_FUND_SECTOR,langCode);
			model.put("mySector", getMyGeneralList(sectorList, insight.getSector()));
			model.put("notMySector", getNonMyGeneralList(sectorList, insight.getSector()));//用于擅长领域选择页面
			//Allocation选项--服务区域
			List<GeneralDataVO> locationList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_SERVICE_REGION, langCode);
			model.put("myAllocation", getMyGeneralList(locationList,insight.getGeoAllocation()));
			model.put("notMyAllocation", getNonMyGeneralList(locationList, insight.getGeoAllocation()));
		}
		
		insight.setContent(HtmlUtils.htmlUnescape(insight.getContent()));
		model.put("insight", insight);
		model.put("webView", webView);
		return "ifa/insight/input";
	}
	
	/**
	 * 观点保存
	 * @author scshi modify by wwluo 
	 * */
	@RequestMapping(value="/saveInsight",method= RequestMethod.POST)
	public void saveInsightEdit(HttpServletRequest request, HttpServletResponse response,
			ModelMap model,InsightInfo insight) throws Exception{
		String content = request.getParameter("content");
		if(content != null) {
			content=URLDecoder.decode(content,"UTF-8");
		}
		
		insight.setContent(content);
		InsightInfo obj = insightInfoService.createOrUpdateInsight(insight);
		MemberBase loginUser = this.getLoginUser(request);
		String scopeFlag = request.getParameter("scopeFlag");
		String clientFlag = request.getParameter("clientFlag");
		String prospectFlag = request.getParameter("prospectFlag");
		String buddyFlag = request.getParameter("buddyFlag");
		String colleagueFlag = request.getParameter("colleagueFlag");
		String prospectsDetail = request.getParameter("prospectsDetail");
		String clientsDetail = request.getParameter("clientsDetail");
		String buddiesDetail = request.getParameter("buddiesDetail");
		String colleaguesDetail = request.getParameter("colleaguesDetail");
       	//保存查看权限信息
   		WebView webView = null;
   		webView = insightInfoService.getViewByRelateAndModule(obj.getId(),CommonConstantsWeb.WEB_VIEW_MODULE_INSIGHT);
   		if (null == webView){//新建
   			webView = new WebView();
       		webView.setModuleType(CommonConstantsWeb.WEB_VIEW_MODULE_INSIGHT);
       		webView.setRelateId(obj.getId());
       		webView.setCreateTime(new Date());
       		webView.setFromMember(loginUser);
   		}
   		webView.setLastUpdate(new Date());
   		webView.setScopeFlag(scopeFlag);
   		webView.setBuddyFlag(buddyFlag);
   		webView.setClientFlag(clientFlag);
   		webView.setColleagueFlag(colleagueFlag);
   		webView.setProspectFlag(prospectFlag);
   		strategyInfoService.delWebViewDetail(webView.getId());
       	webView = strategyInfoService.saveWebView(webView,clientsDetail,prospectsDetail,buddiesDetail,colleaguesDetail);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("insightId", obj.getId());
		result.put("result", true);
		result.put("code","global.success.save");
		result.put("msg",getProperty(request,"global.success.save"));
		JsonUtil.toWriter(result, response);
		// add end
	}
	
	/**
	 * 观点删除
	 * @author scshi
	 * */
	@RequestMapping(value="/delInsight",method= RequestMethod.GET)
	public void delInsight(HttpServletRequest request, HttpServletResponse response, ModelMap model,InsightInfo insight){
		String insightId = request.getParameter("insightId");
		insightInfoService.delInsightById(insightId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		result.put("code","global.success.save");
		result.put("msg",getProperty(request,"global.success.save"));
		JsonUtil.toWriter(result, response);
	}
	
	
	/**
	 * 置顶状态修改
	 * @author scshi
	 * */
	@RequestMapping(value="/headStatusChange",method= RequestMethod.GET)
	public void insightUporDown(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String	insightId = request.getParameter("insightId");
		String	headStatus =  request.getParameter("headStatus");
		insightInfoService.overHeadStatusChange(insightId,headStatus);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		result.put("code","global.success.save");
		result.put("msg",getProperty(request,"global.success.save"));
		JsonUtil.toWriter(result, response);
	}
	
	
	/**
	 *基础数据列表处理
	 *@author scshi
	 *@date 20160821
	 *@param list 基础数据list
	 *@param codes	实体已选类型code
	 *
	 * */	
	private List<GeneralDataVO> getMyGeneralList(List<GeneralDataVO> list, String codes){
		List<GeneralDataVO> result = new ArrayList<GeneralDataVO>();
		if (list!=null && !list.isEmpty() && codes!=null && codes.length()>0){
			List<String> codeArr = Arrays.asList(StrUtils.splitAndTrim(codes,",",""));
			for (GeneralDataVO v: list){
				if (codeArr.contains(v.getItemCode())){
					result.add(v);
				}
			}
		}
		return result;
	}
	
	private List<GeneralDataVO> getNonMyGeneralList(List<GeneralDataVO> list, String codes){
		List<GeneralDataVO> result = new ArrayList<GeneralDataVO>();
		if (list!=null && !list.isEmpty()){
			if (codes!=null && codes.length()>0){
				List<String> codeArr = Arrays.asList(StrUtils.splitAndTrim(codes,",",""));
				for (GeneralDataVO v: list){
					if (!codeArr.contains(v.getItemCode()))
						result.add(v);
				}
			}else{
				result = list;
			}
		}
		return result;
	}
	
	/**
	 * 观点 点赞、踩
	 * @author wwluo
	 * 
	 */
	@RequestMapping(value="/insightUpCounter",method= RequestMethod.POST)
	public void insightUpCounter(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		InsightInfo insightInfo = new InsightInfo();
		try {
			String insightId = request.getParameter("insightId");
			String type = request.getParameter("type");
			if (StringUtils.isNotBlank(insightId) && StringUtils.isNotBlank(type)) {
				insightInfo = insightInfoService.findInsightById(insightId, null);
				if("1".equals(type)){
					Integer upCounter = insightInfo.getUpCounter();
					if(upCounter == null){
						upCounter = 1;
					}else{
						upCounter++;
					}
					insightInfo.setUpCounter(upCounter);
					insightInfo = insightInfoService.updateInsight(insightInfo);
					flag = true;
				}else{
					Integer downCounter = insightInfo.getDownCounter();
					if(downCounter == null){
						downCounter = 1;
					}else{
						downCounter++;
					}
					insightInfo.setDownCounter(downCounter);
					insightInfo = insightInfoService.updateInsight(insightInfo);
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("flag", flag);
		result.put("insightInfo", insightInfo);
		JsonUtil.toWriter(result, response);
	}
}
