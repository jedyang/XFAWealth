/**
 * 
 */
package com.fsll.wmes.investor.action.front;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.ResponseUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysParamService;
import com.fsll.core.service.SysParamTypeService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebPush;
import com.fsll.wmes.entity.WebView;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.ifa.service.InsightInfoService;
import com.fsll.wmes.ifa.vo.IfaMessageVO;
import com.fsll.wmes.ifa.vo.InsightSearchParamVo;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.strategy.service.StrategyInfoService;
import com.fsll.wmes.web.service.WebInvestorVisitService;
import com.mongodb.util.JSON;

/**
 * @author scshi
 *我的观点
 */
@Controller
@RequestMapping("/front/investor/myInsight")
public class FrontInvestorInsightAct extends WmesBaseAct{
	
	@Autowired
	private InsightInfoService insightInfoService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private WebInvestorVisitService webInvestorVisitService;
	@Autowired
	private SysParamService sysParamService;

	
	/**
	 * 我的观点列表页面
	 * @author scshi modify wwlin
	 * */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String insightList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String langCode = this.getLoginLangFlag(request);
		String memberId = request.getParameter("memberId");
		if (StringUtils.isNotBlank(memberId)){
			IfaMessageVO ifa = insightInfoService.getByMemberId(memberId, langCode);
			//MemberSsoVO curMember = (MemberSsoVO) request.getSession().getAttribute(CoreConstants.FRONT_USER_SSO);
			model.put("ifa", ifa);
		}
		
		//geoallocation
		List<SysParamConfig> regionList = sysParamService.findParamConfigByType("region");
		model.put("regionList", regionList);
	
		//Sector
		List<SysParamConfig> typeList = sysParamService.findParamConfigByType("fund_sector");
		model.put("typeList", typeList);
		return "investor/insight/insightList";
	}
	
	/**
	 * 观点列表->查看IFA的观点界面，自定义的查看方式需要根据权限来判断
	 * @author scshi modify wwlin
	 * */ 
	@RequestMapping(value = "/listJson", method = RequestMethod.POST)
	public void insightListJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
//		MemberBase baseMem = this.getLoginUser(request);
//		String userLang = this.getLoginLangFlag(request);
		String langCode = this.getLoginLangFlag(request);
		String memberId = request.getParameter("memberId");
		MemberBase baseMem = memberBaseService.findById(memberId);
		MemberBase curMember = this.getLoginUser(request);//当前登录用户，需要判断当前登录的用户与查看的IFA之间的权限关系
		if(null != curMember){
			String viewMemberId = curMember.getId();
			
			jsonPaging = this.getJsonPaging(request);
			InsightSearchParamVo searchParam = new InsightSearchParamVo();
			searchParam.setIssuedDate(request.getParameter("issuedDate"));
			searchParam.setIssuedDateSort(request.getParameter("issuedDateSort"));
			searchParam.setViewSort(request.getParameter("viewSort"));
			searchParam.setAllocation(request.getParameter("allocation"));
			//searchParam.setSector(request.getParameter("sector"));
			searchParam.setSector(this.toUTF8String(request.getParameter("sector")));
			searchParam.setKeyWord(request.getParameter("keyWord"));
			
			// add wwluo 2016/11/22
			searchParam.setFromDate(request.getParameter("fromDate"));
			searchParam.setToDate(request.getParameter("toDate"));
			searchParam.setGeoAllocation(request.getParameter("geoAllocation"));
			searchParam.setAllocation(this.toUTF8String(request.getParameter("allocation")));
			searchParam.setStatus(request.getParameter("status"));
			// add end
			searchParam.setLangCode(langCode);
			
			jsonPaging = insightInfoService.findIfaInsightListForWeb(jsonPaging, baseMem.getId(), searchParam,viewMemberId);
			this.toJsonString(response, jsonPaging);
			//ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
		}
	}
	
	
	/**
	 * 获取查看观点的investorFriends
	 * @author scshi
	 * */
	@RequestMapping(value = "/viewList", method = RequestMethod.POST)
	public void friendViewList(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		jsonPaging = this.getJsonPaging(request);
		String langCode = this.getLoginLangFlag(request);
		String insightId = request.getParameter("insightId");
		jsonPaging =  insightInfoService.loadVistorList(jsonPaging,insightId,"insight",langCode);
		ResponseUtils.renderHtml(response,JsonUtil.toJson(jsonPaging));
	}
	
	/***
	 * 查看观点
	 * @author Yan
	 * */
	@RequestMapping(value="/insightView", method = RequestMethod.GET)
	public void insightView(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		jsonPaging = this.getJsonPaging(request);
		String langCode = this.getLoginLangFlag(request);
		//查询观点
		String insightId = request.getParameter("insightId");
		InsightInfo insight = insightInfoService.findInsightById(insightId, langCode);
		//插入浏览记录
		MemberBase member = this.getLoginUser(request);
		String ip = this.getRemoteHost(request);
		
		
		//插入访问信息记录
		WebInvestorVisit log = new WebInvestorVisit();
		if(member!=null){
			saveVisit(member, null, insightId, "view", "insight_count", ip);
			log.setMember(member);
		}
		
		log.setModuleType("insight");
		log.setRelateId(insightId);
		log.setVistiTime(new Date());
		log = webInvestorVisitService.addLog(log);
		JsonUtil.toWriter(insight, response);
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
