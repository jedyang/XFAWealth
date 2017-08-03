package com.fsll.app.investor.me.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.me.service.AppMeIndexService;
import com.fsll.app.investor.me.vo.AppInsightItemVo;
import com.fsll.app.investor.me.vo.AppPortfolioInfoVO;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.app.investor.me.vo.AppVisitedInfoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 【我的】 首页接口实现
 * @author zxtan
 * @date 2016-11-29
 */
@RestController
@RequestMapping("/service/me")
public class AppMeIndexRest extends WmesBaseRest {
	@Autowired
	private AppMeIndexService meIndexService;
	
	/**
	 * 得到我感兴趣的投资组合列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/findVisitedList.r
	 * {"langCode":"sc","memberId":"40280ad65601004c0156010188370004","periodCode":"return_period_code_1M","rows":2}
	 */
	@RequestMapping(value = "/findVisitedList")
	@ResponseBody
	public ResultWS findVisitedList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1M);
		int rows = jsonObject.optInt("rows", 2);
		
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppVisitedInfoVO visitedInfoVO = new AppVisitedInfoVO();
		List<AppPortfolioInfoVO> portfolioList = meIndexService.findVisitedPortfolioList(memberId, langCode, periodCode, rows);
		AppInsightItemVo insightInfo = meIndexService.findVisitedInsightInfo(memberId);
		visitedInfoVO.setPortfolioList(portfolioList);
		visitedInfoVO.setInsightInfo(insightInfo);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(visitedInfoVO);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/me/findOrderHistoryList.r
	 * {"memberId":"40280a25559b852e01559b89c80f0004","toCurrency":"HKD","langCode":"sc","keyword":"","orderType":"Buy" }
	 */
	@RequestMapping(value = "/findOrderHistoryList")
	@ResponseBody
	public ResultWS findOrderHistoryList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String page = jsonObject.optString("page", "1");// 获得当前页数
		String rows = jsonObject.optString("rows", "10");// 获得每页记录数
		String memberId = jsonObject.optString("memberId", "");// 获得MemberBase ID
		String toCurrency = jsonObject.optString("toCurrency", "");// 获得货币
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		String orderType = jsonObject.optString("orderType", "");// 获得交易类型（Buy：买入，Sell：卖出，空则为其他类型）
		String keyword = jsonObject.optString("keyword", "");// 获得货币
		
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		
		jsonPaging = meIndexService.findOrderHistoryList(jsonPaging, memberId, toCurrency, langCode, orderType, keyword);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	}
}
