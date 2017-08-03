package com.fsll.app.investor.me.ws;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.market.vo.AppPortfolioArenaAllocationDetailVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaAllocationVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaCumperfVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaDetailVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaProductVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaReturnVO;
import com.fsll.app.investor.me.service.AppPortfolioMessService;
import com.fsll.app.investor.me.vo.AppHoldProductVO;
import com.fsll.app.investor.me.vo.AppMyAssetsDetailVO;
import com.fsll.app.investor.me.vo.AppMyAssetsHisMessVo;
import com.fsll.app.investor.me.vo.AppMyAssetsMessVo;
import com.fsll.app.investor.me.vo.AppPieChartItemVO;
import com.fsll.app.investor.me.vo.AppPortfolioAllocationVO;
import com.fsll.app.investor.me.vo.AppPortfolioChartDataVo;
import com.fsll.app.investor.me.vo.AppPortfolioDetailVO;
import com.fsll.app.investor.me.vo.AppPortfolioFeeVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldCumperfVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldProductCumperfVO;
import com.fsll.app.investor.me.vo.AppPortfolioHoldVO;
import com.fsll.app.investor.me.vo.AppPortfolioMarketMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductDetailVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductVo;
import com.fsll.app.investor.me.vo.AppPortfolioReturnVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;

/**
 * 投资组合接口服务
 * @author zpzhou
 * @date 2016-9-14
 */
@RestController
@RequestMapping("/service/portfolio")
public class AppPortfolioMessRest extends WmesBaseRest {

	@Autowired
	private AppPortfolioMessService portfolioMessService;

	
	/**
	 * 得到某个投资组合行情数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioList.r
	 * {"portfolioId":"40280a25559b852e01559b8615da0002" }
	 */
	@RequestMapping(value = "/findPortfolioMarketList")
	@ResponseBody
	public ResultWS findPortfolioMarketList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String portfolioId = jsonObject.optString("portfolioId", "");// 获得组合ID
		if ("".equals(portfolioId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppPortfolioMarketMessVo> list= portfolioMessService.findPortfolioMarketList(portfolioId);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到投资组合中某个产品的行情数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioProductMarketList.r
	 * {"portfolioId":"40280a25559b852e01559b8615da0002","productId":"40280a25559b852e01559b8615da0002"}
	 */
	@RequestMapping(value = "/findPortfolioProductMarketList")
	@ResponseBody
	public ResultWS findPortfolioProductMarketList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String portfolioId = jsonObject.optString("portfolioId", "");// 获得组合ID
		String productId = jsonObject.optString("productId", "");// 获得产品ID
		if ("".equals(portfolioId) || "".equals(productId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppPortfolioMarketMessVo> list= portfolioMessService.findPortfolioProductMarketList(portfolioId,productId);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到投资组合的产品信息列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioProductList.r
	 * {"portfolioId":"40280a25559b852e01559b8615da0002","langCode":"sc","dateType":"1M"}
	 */
	@RequestMapping(value = "/findPortfolioProductList")
	@ResponseBody
	public ResultWS findPortfolioProductList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String portfolioId = jsonObject.optString("portfolioId", "");// 获得组合ID
		String dateType = jsonObject.optString("dateType", "");
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ("".equals(portfolioId) || "".equals(dateType)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppPortfolioProductVo> list= portfolioMessService.findPortfolioProductList(portfolioId,langCode,dateType);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 得到某个投资组合行情图表数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioChart.r
	 *  {"portfolioId":"40280a25559b852e01559b8615da0002","langCode":"sc","period":"1WK","addiDate":"0"}
	 */
	@RequestMapping(value = "/findPortfolioChart")
	@ResponseBody
	public ResultWS findPortfolioChart(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String portfolioId = jsonObject.optString("portfolioId", "");// 获得组合ID
		String periodCode = jsonObject.optString("periodCode","return_period_code_1M");
		String addiData = jsonObject.optString("addiData", "0");// 额外返回的数据周期，可空 -- 默认250
		if ("".equals(portfolioId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppPortfolioChartDataVo vo= portfolioMessService.findPortfolioChart(portfolioId, periodCode, addiData,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	
	/**
	 * 【我的投资组合】得到投资组合列表接口， 通过body传递参数 
	 * @author zxtan
	 * @date 2016-11-16
	 * 调用示例:[地址]/service/portfolio/findPortfolioList.r
	 * {"memberId":"40280a25559b852e01559b89c80f0004","periodCode":"return_period_code_1M","toCurrency":"HKD" }
	 */
	@RequestMapping(value = "/findPortfolioList")
	@ResponseBody
	public ResultWS findPortfolioList(HttpServletRequest request,@RequestBody String body) {
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
		String toCurrency = jsonObject.optString("toCurrency","");
		String periodCode = jsonObject.optString("periodCode","return_period_code_1M");
		String status = jsonObject.optString("status", "1");
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
 		
		String startDate = getStartDate(periodCode);
		
		if ("".equals(memberId) || "".equals(status) || "".equals(startDate)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		List<AppPortfolioHoldVO> portfolioList = portfolioMessService.findPortfolioList(memberId, toCurrency, periodCode,langCode);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(portfolioList);
		return result;
	}
	
	/**
	 * 【我的投资组合】得到投资组合列表接口， 通过body传递参数 
	 * @author zxtan
	 * @date 2016-11-16
	 * 调用示例:[地址]/service/portfolio/findPortfolioHoldList.r
	 * {"memberId":"40280a25559b852e01559b89c80f0004","periodCode":"return_period_code_1M","toCurrency":"HKD","status":"1" }
	 */
	@RequestMapping(value = "/findPortfolioHoldList")
	@ResponseBody
	public ResultWS findPortfolioHoldList(HttpServletRequest request,@RequestBody String body) {
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
		String toCurrency = jsonObject.optString("toCurrency","");
		String periodCode = jsonObject.optString("periodCode","return_period_code_1M");
		String status = jsonObject.optString("status", "1");
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		String startDate = getStartDate(periodCode);
		
		if ("".equals(memberId) || "".equals(status) || "".equals(startDate)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		List<AppPortfolioHoldVO> portfolioList = portfolioMessService.findPortfolioHoldList(memberId, toCurrency, periodCode,status,langCode);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(portfolioList);
		return result;
	}
	
	/**
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioDetail.r
	 * {"portfolioHoldId":"40280a25559b852e01559b8615da0002","langCode":"sc","periodCode":"return_period_code_1M","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2016-11-16
	 */
	@RequestMapping(value = "/findPortfolioDetail")
	@ResponseBody
	public ResultWS findPortfolioDetail(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);		
		String portfolioHoldId = jsonObject.optString("portfolioHoldId", "");// 获得组合ID
		String periodCode = jsonObject.optString("periodCode", "return_period_code_1M");// 行情时间
		String toCurrency = jsonObject.optString("toCurrency","");
		if ("".equals(portfolioHoldId) || "".equals(periodCode) || "".equals(langCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		String startDate = getStartDate(periodCode);
		
		AppPortfolioDetailVO detailVO = new AppPortfolioDetailVO();
		//基本信息
		AppPortfolioHoldVO infoVO = portfolioMessService.findPortfolioHold(portfolioHoldId,toCurrency,langCode);
		if("".equals(toCurrency)){
			//组合配置、基金产品信息与组合使用相同的货币
			toCurrency = infoVO.getBaseCurrencyCode();
		}
		
		//回报
		AppPortfolioReturnVO returnVO = portfolioMessService.findPortfolioReturn(portfolioHoldId, periodCode);
		List<AppPortfolioHoldCumperfVO> cumperfList = portfolioMessService.findPortfolioCumperfList(portfolioHoldId, startDate);
		returnVO.setCumperfList(cumperfList);
		//产品信息
//		List<AppPortfolioProductVo> productList = portfolioMessService.findPortfolioProductFundList(portfolioHoldId, langCode, toCurrency);
		
		//产品列表
		List<AppHoldProductVO> fundList = portfolioMessService.findHoldProductList(portfolioHoldId, "fund", langCode, toCurrency);
		List<AppHoldProductVO> bondList = portfolioMessService.findHoldProductList(portfolioHoldId, "bond", langCode, toCurrency);
		List<AppHoldProductVO> stockList = portfolioMessService.findHoldProductList(portfolioHoldId, "stock", langCode, toCurrency);
		List<AppHoldProductVO> insureList = portfolioMessService.findHoldProductList(portfolioHoldId, "insure", langCode, toCurrency);
		
		
		//产品配置
		List<AppPortfolioAllocationVO> allocationList = portfolioMessService.findPortfolioAllocationList(portfolioHoldId, langCode, toCurrency);
		
		//基金配置（行业，类型，区域）
		List<AppPieChartItemVO> sectorTypeList = portfolioMessService.findPortfolioFundAllocationList(portfolioHoldId, langCode, toCurrency,"sector");
		//List<AppPortfolioAllocationVO> fundTypeList = portfolioMessService.findPortfolioFundAllocationList(portfolioHoldId, langCode, toCurrency,"fundType");
		List<AppPieChartItemVO> geoAllocationList = portfolioMessService.findPortfolioFundAllocationList(portfolioHoldId, langCode, toCurrency,"market");
		
	
		
		detailVO.setPortfolioHold(infoVO);
		detailVO.setReturnInfo(returnVO);
//		detailVO.setProductList(productList);	
		detailVO.setFundList(fundList);
		detailVO.setBondList(bondList);
		detailVO.setStockList(stockList);
		detailVO.setInsureList(insureList);
		detailVO.setAllocationList(allocationList);
//		detailVO.setFundTypeList(fundTypeList);
		detailVO.setSectorTypeList(sectorTypeList);
		detailVO.setGeoAllocationList(geoAllocationList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(detailVO);
		return result;
	}
	
	/**
	 * 得到某个投资组合的持仓基金产品信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioProductFundList.r
	 * {"portfolioHoldId":"40280a25559b852e01559b8615da0002","langCode":"sc"}
	 * @author zxtan
	 * @date 2017-02-23
	 */
	@RequestMapping(value = "/findPortfolioProductFundList")
	@ResponseBody
	public ResultWS findPortfolioProductFundList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);		
		String portfolioHoldId = jsonObject.optString("portfolioHoldId", "");// 获得组合ID
//		String toCurrency = jsonObject.optString("toCurrency","");
		if ("".equals(portfolioHoldId) || "".equals(langCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
			
		
		//基金产品信息
		List<AppPortfolioProductVo> productList = portfolioMessService.findPortfolioProductFundList(portfolioHoldId, langCode, null);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(productList);
		return result;
	}
	
	/**
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioReturn.r
	 * {"portfolioHoldId":"40280a25559b852e01559b8615da0002","periodCode":"return_period_code_1M" }
	 * @author zxtan
	 * @date 2016-11-16
	 */
	@RequestMapping(value = "/findPortfolioReturn")
	@ResponseBody
	public ResultWS findPortfolioReturn(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String portfolioHoldId = jsonObject.optString("portfolioHoldId", "");// 获得组合ID
		String periodCode = jsonObject.optString("periodCode", "return_period_code_1M");// 行情时间
		
		if ("".equals(portfolioHoldId) || "".equals(periodCode) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		String startDate = getStartDate(periodCode);
		
		//回报
		AppPortfolioReturnVO returnVO = portfolioMessService.findPortfolioReturn(portfolioHoldId, periodCode);
		List<AppPortfolioHoldCumperfVO> cumperfList = portfolioMessService.findPortfolioCumperfList(portfolioHoldId, startDate);
		returnVO.setCumperfList(cumperfList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(returnVO);
		return result;
	}
	
	/**
	 * 得到某个投资组合基金回报情况接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioProductDetail.r
	 * {"id":"40280a705925b9eb0159263425bb0067","langCode":"sc","periodCode":"return_period_code_1M","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2016-12-29
	 */
	@RequestMapping(value = "/findPortfolioProductDetail")
	@ResponseBody
	public ResultWS findPortfolioProductDetail(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);		
		String id = jsonObject.optString("id", "");// 获得ID		
		//String memberId = jsonObject.optString("memberId", "");// 获得MemberID
		String periodCode = jsonObject.optString("periodCode", "return_period_code_1M");// 行情时间
		String toCurrency = jsonObject.optString("toCurrency","");
		if ("".equals(id) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		String startDate = getStartDate(periodCode);
		AppPortfolioProductDetailVO detailVO = portfolioMessService.findPortfolioProductDetail(id, langCode, toCurrency);
		if(null != detailVO){
			String portfolioHoldId = detailVO.getPortfolioHoldId();
			String productId = detailVO.getProductId();
			List<AppPortfolioHoldProductCumperfVO> cumperfList = portfolioMessService.findPortfolioHoldProductCumperfList(portfolioHoldId, productId, startDate,toCurrency,langCode);
			List<AppPortfolioOrderHistoryVO> orderHistoryList = portfolioMessService.findPortfolioOrderHistoryList(portfolioHoldId, productId, toCurrency, langCode, "");
			detailVO.setCumperfList(cumperfList);
			detailVO.setOrderHistoryList(orderHistoryList);	
		}
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(detailVO);
		return result;
	}
	
	
	/**
	 * 得到某个投资组合基金产品回报情况接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioHoldProductCumperfList.r
	 * {"portfolioHoldId":"8080r0405666648d01567e22f84jk01","productId":"FUND_000000000000005","periodCode":"return_period_code_1M","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2016-12-29
	 */
	@RequestMapping(value = "/findPortfolioHoldProductCumperfList")
	@ResponseBody
	public ResultWS findPortfolioHoldProductCumperfList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String portfolioHoldId = jsonObject.optString("portfolioHoldId", "");// 获得组合ID		
		String productId = jsonObject.optString("productId", "");// 获得组合ID
		String periodCode = jsonObject.optString("periodCode", "return_period_code_1M");// 行情时间
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		if ("".equals(portfolioHoldId) || "".equals(productId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		String startDate = getStartDate(periodCode);

		List<AppPortfolioHoldProductCumperfVO> cumperfList = portfolioMessService.findPortfolioHoldProductCumperfList(portfolioHoldId, productId, startDate,toCurrency,langCode);
		
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(cumperfList);
		return result;
	}
	
	
	/**
	 * 得到某个投资组合交易记录接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioOrderHistoryList.r
	 * {"portfolioHoldId":"40280a25559b852e01559b8615da0002","langCode":"sc","toCurrency":"HKD","keyword":"" }
	 * @author zxtan
	 * @date 2016-12-28
	 */
	@RequestMapping(value = "/findPortfolioOrderHistoryList")
	@ResponseBody
	public ResultWS findPortfolioOrderHistoryList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);		
		String portfolioHoldId = jsonObject.optString("portfolioHoldId", "");// 获得组合ID
		String productId = "";// jsonObject.optString("productId", "");// 产品ID
		String keyword = jsonObject.optString("keyword", "");// keyword
		//String memberId = jsonObject.optString("memberId", "");// memberID

		if ("".equals(portfolioHoldId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppPortfolioOrderHistoryVO> voList = portfolioMessService.findPortfolioOrderHistoryList(portfolioHoldId, productId, toCurrency, langCode, keyword);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(voList);
		return result;
	}
	
	/**
	 * 得到某个投资组合交易记录接口， 通过body传递参数 
	 * 调用示例:[地址]/service/portfolio/findPortfolioFeeList.r
	 * {"portfolioHoldId":"40280a70596e8ec601596ed595eb0040","langCode":"sc","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2016-12-28
	 */
	@RequestMapping(value = "/findPortfolioFeeList")
	@ResponseBody
	public ResultWS findPortfolioFeeList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}

		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);		
		String portfolioHoldId = jsonObject.optString("portfolioHoldId", "");// 获得组合ID


		if ("".equals(portfolioHoldId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppPortfolioFeeVO> voList = portfolioMessService.findPortfolioFeeList(portfolioHoldId, langCode, toCurrency);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(voList);
		return result;
	}
	
//	/**
//	 * 根据周期获取开始日期
//	 * @param periodCode
//	 * @return
//	 */
//	private String getStartDate(String periodCode) {
//		String startDate = "";
//		if(periodCode.indexOf("return_period_code_")>-1){
//			String period = periodCode.replace("return_period_code_", "");
//			if("YTD".equalsIgnoreCase(period)){
//				startDate = DateUtil.formatDate(DateUtil.getCurrYearFirst());
//			}else if("LAUNCH".equalsIgnoreCase(period)){
//				startDate = "";
//			}else {
//				String num = period.replace("M", "").replace("Y", "");
//				
//				int unit;
//				try {
//					unit = Integer.parseInt(num);
//				} catch (Exception e) {
//					// TODO: handle exception
//					unit = 1;
//				}
//				
//				if(period.indexOf("W")>-1){
//					startDate = DateUtil.getCurDateStr(Calendar.DATE, -(unit*7));
//				}
//				
//				if(period.indexOf("M")>-1){
//					startDate = DateUtil.getCurDateStr(Calendar.MONTH, -unit);
//				}
//				
//				if(period.indexOf("Y")>-1){
//					startDate = DateUtil.getCurDateStr(Calendar.YEAR, -unit);
//				}
//			}		
//		}
//		return startDate;
//	}
}
