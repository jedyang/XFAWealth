package com.fsll.wmes.portfolio.action.console;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.SysAdmin;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.portfolio.service.PortfolioArenaService;
import com.fsll.wmes.portfolio.vo.CorePortfolioVO;
import com.fsll.wmes.portfolio.vo.PortfolioArenaVO;

/**
 * 控制器:投资组合信息管理
 * 
 * @author mqzou
 * @version 1.0.0 Created On: 2016-8-17
 */

@Controller
@RequestMapping("/console/portfolio/arena")
public class ConsolePortfolioAreaAct extends WmesBaseAct {
	@Autowired
	private PortfolioArenaService portfolioArenaService;

	/**
	 * 投资组合列表页面
	 * @author mqzou 2017-06-02
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		//风险等级
    	List<GeneralDataVO> itemList = findSysParameters(CommonConstantsWeb.SYS_PARM_TYPE_STRATEGY_RISK_RATING, this.getLoginLangFlag(request));
        model.put("riskList", itemList);
		return "console/portfolio/list";
	}
	
	/**
	 * 投资组合列表数据
	 * @author mqzou 2017-06-02
	 */
	@RequestMapping(value="/listJson")
	public void listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String langCode=getLoginLangFlag(request);
		String keyword=request.getParameter("keyword");
		String riskLevel=request.getParameter("riskLevel");
		jsonPaging=getJsonPaging(request);
		jsonPaging=portfolioArenaService.findPortfolioList(jsonPaging, keyword, riskLevel, langCode);
		toJsonString(response,jsonPaging);
	}
	
	/**
	 * 投资组合详情
	 * @author mqzou 2017-06-02
	 */
	@RequestMapping(value="/detail",method=RequestMethod.GET)
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String id = request.getParameter("id");
		String langCode=getLoginLangFlag(request);
		PortfolioArenaVO vo=portfolioArenaService.findPortfolioDetial(id, langCode);
		model.put("detail", vo);
		List sectorList=portfolioArenaService.findPortfolioProductRate("sector_type", id, langCode);
		model.put("sectorList", JsonUtil.toJson(sectorList));
		
		List geoList=portfolioArenaService.findPortfolioProductRate("geo_allocation", id, langCode);
		model.put("geoList", JsonUtil.toJson(geoList));
		
		List typeList=portfolioArenaService.findPortfolioProductRate("fund_type", id, langCode);
		model.put("typeList", JsonUtil.toJson(typeList));
		return "console/portfolio/detail";
	}
	
	/**
	 * 获取组合的收益情况
	 * @author mqzou 2016-11-15
	 */
	@RequestMapping(value = "/getPortfolioCumperf", method = RequestMethod.POST)
	public void getPortfolioCumperf(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String id=request.getParameter("id");
		//获取发布日期条件
        String period = StrUtils.getString(request.getParameter("period"));
		List<CorePortfolioVO> list=portfolioArenaService.getPortfolioReturnRate(id, period, "HKD");
        JsonUtil.toWriter(list,response);
	}
	
}
