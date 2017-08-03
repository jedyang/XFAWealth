package com.fsll.app.ifa.market.ws;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import com.fsll.app.me.vo.AppHoldProductVO;
//import com.fsll.app.me.vo.AppPieChartItemVO;
//import com.fsll.app.me.vo.AppPortfolioAllocationVO;
//import com.fsll.app.me.vo.AppPortfolioDetailVO;
//import com.fsll.app.me.vo.AppPortfolioHoldCumperfVO;
//import com.fsll.app.me.vo.AppPortfolioHoldVO;
//import com.fsll.app.me.vo.AppPortfolioReturnVO;
import com.fsll.app.ifa.market.service.AppPortfolioHoldService;
import com.fsll.app.ifa.market.vo.AppHoldAllocationVO;
import com.fsll.app.ifa.market.vo.AppHoldCountVO;
import com.fsll.app.ifa.market.vo.AppHoldCumperfVO;
import com.fsll.app.ifa.market.vo.AppHoldDetailVO;
import com.fsll.app.ifa.market.vo.AppHoldFeeVO;
import com.fsll.app.ifa.market.vo.AppHoldProductCumperfVO;
import com.fsll.app.ifa.market.vo.AppHoldProductDetailVO;
import com.fsll.app.ifa.market.vo.AppHoldProductFundVO;
import com.fsll.app.ifa.market.vo.AppHoldProductReturnDetailVO;
import com.fsll.app.ifa.market.vo.AppHoldProductVO;
import com.fsll.app.ifa.market.vo.AppHoldReturnVO;
import com.fsll.app.ifa.market.vo.AppHoldVO;
import com.fsll.app.ifa.market.vo.AppOrderHistoryVO;
import com.fsll.app.ifa.market.vo.AppPieChartItemVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductVo;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 日程接口
 * @author zxtan
 * @date 2017-03-23
 */
@RestController
@RequestMapping("/service/ifa/market/hold")
public class AppPortfolioHoldRest extends WmesBaseRest {

	@Autowired
	private AppPortfolioHoldService portfolioHoldService;
	
	/**
	 *获取IFA客户组合统计（盈亏数）
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioHoldCount.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"","minReturn":"","maxReturn":"","minAmount":"","maxAmount":"","periodCode":"return_period_code_1M","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findPortfolioHoldCount")
	@ResponseBody
	public ResultWS findPortfolioHoldCount(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String memberId = jsonObject.optString("memberId","");
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);
				
		if("".equals(memberId)||"".equals(periodCode)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		
		AppHoldCountVO info = portfolioHoldService.findPortfolioHoldCount(jsonObject);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	} 
	

	/**
	 *获取IFA客户组合列表
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioHoldList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"","minReturn":"","maxReturn":"","minAmount":"","maxAmount":"","periodCode":"return_period_code_1M","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findPortfolioHoldList")
	@ResponseBody
	public ResultWS findPortfolioHoldList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId","");
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);
				
		if("".equals(memberId)||"".equals(periodCode)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		int rows = jsonObject.optInt("rows",10);
		int page = jsonObject.optInt("page",1);	
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = portfolioHoldService.findPortfolioHoldList(jsonPaging, jsonObject);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	} 
	
	/**
	 *获取IFA客户详情-组合列表
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioOrderPlanList.r
	 * {"memberId":"40280ad455e327690155e3291c582000","keyword":"","finishStatus":"","minAmount":"","maxAmount":"","rows":10,"page":1}
	 */
	@RequestMapping(value = "/findPortfolioOrderPlanList")
	@ResponseBody
	public ResultWS findPortfolioOrderPlanList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId","");
				
		if("".equals(memberId)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		int rows = jsonObject.optInt("rows",10);
		int page = jsonObject.optInt("page",1);
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);
		
		jsonPaging = portfolioHoldService.findPortfolioOrderPlanList(jsonPaging, jsonObject);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	} 
	
	/**
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioDetail.r
	 * {"portfolioHoldId":"40280a25559b852e01559b8615da0002","langCode":"sc","periodCode":"return_period_code_1M","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2017-04-01
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
		
		AppHoldDetailVO detailVO = new AppHoldDetailVO();
		//基本信息
		AppHoldVO infoVO = portfolioHoldService.findPortfolioHold(portfolioHoldId,toCurrency,langCode);
		if("".equals(toCurrency)){
			//组合配置、基金产品信息与组合使用相同的货币
			toCurrency = infoVO.getBaseCurrencyCode();
		}
		
		//回报
		AppHoldReturnVO returnVO = portfolioHoldService.findPortfolioHoldReturn(portfolioHoldId, periodCode);
		List<AppHoldCumperfVO> cumperfList = portfolioHoldService.findPortfolioHoldCumperfList(portfolioHoldId, startDate);
		returnVO.setCumperfList(cumperfList);
		
		//产品列表
		List<AppHoldProductVO> fundList = portfolioHoldService.findPortfolioHoldProductList(portfolioHoldId, "fund", langCode, toCurrency);
		List<AppHoldProductVO> bondList = portfolioHoldService.findPortfolioHoldProductList(portfolioHoldId, "bond", langCode, toCurrency);
		List<AppHoldProductVO> stockList = portfolioHoldService.findPortfolioHoldProductList(portfolioHoldId, "stock", langCode, toCurrency);
		List<AppHoldProductVO> insureList = portfolioHoldService.findPortfolioHoldProductList(portfolioHoldId, "insure", langCode, toCurrency);
		
		
		//产品配置（基金、债券、股票、保险、现金）
		List<AppHoldAllocationVO> allocationList = portfolioHoldService.findPortfolioHoldAllocationList(portfolioHoldId, langCode, toCurrency);
		
		//基金配置（行业，区域）
		List<AppPieChartItemVO> sectorTypeList = portfolioHoldService.findPortfolioFundAllocationList(portfolioHoldId, langCode, toCurrency,"sector");
		List<AppPieChartItemVO> geoAllocationList = portfolioHoldService.findPortfolioFundAllocationList(portfolioHoldId, langCode, toCurrency,"market");
		
		//赋值
		detailVO.setPortfolioHold(infoVO);
		detailVO.setReturnInfo(returnVO);
		detailVO.setFundList(fundList);
		detailVO.setBondList(bondList);
		detailVO.setStockList(stockList);
		detailVO.setInsureList(insureList);
		detailVO.setAllocationList(allocationList);
		detailVO.setSectorTypeList(sectorTypeList);
		detailVO.setGeoAllocationList(geoAllocationList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(detailVO);
		return result;
	}
	
	
	/**
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioReturn.r
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
		AppHoldReturnVO returnVO = portfolioHoldService.findPortfolioHoldReturn(portfolioHoldId, periodCode);
		List<AppHoldCumperfVO> cumperfList = portfolioHoldService.findPortfolioHoldCumperfList(portfolioHoldId, startDate);
		returnVO.setCumperfList(cumperfList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(returnVO);
		return result;
	}
	
	
	/**
	 * 获取交易记录
	 *@author zxtan
	 *@date 2017-03-24
	 * 调用示例:[地址]/service/ifa/market/hold/findOrderHistoryList.r
	 * {"page":1, "rows":10,"memberId":"40280ad455e327690155e3291c582000","keyword":"","langCode":"sc"}
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
		Integer page = jsonObject.optInt("page", 1);// 获得当前页数
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
//		String clientMemberId = ""; //jsonObject.optString("clientMemberId", "");// 获得当前登录用户ID
		String keyword = jsonObject.optString("keyword", "");// 搜索关键字
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言
		Integer num = jsonObject.optInt("num", 10);// 每个组合显示的交易记录数
		
		if ( "".equals(langCode)|| "".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = portfolioHoldService.findOrderHistoryList(jsonPaging, memberId,keyword, langCode,num);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	}
	
	/**
	 * 得到某个投资组合基金回报情况接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioProductDetail.r
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
		AppHoldProductDetailVO detailVO = portfolioHoldService.findPortfolioHoldProductDetail(id, langCode, toCurrency);
		if(null != detailVO){
			String portfolioHoldId = detailVO.getPortfolioHoldId();
			String productId = detailVO.getProductId();
			List<AppHoldProductCumperfVO> cumperfList = portfolioHoldService.findPortfolioHoldProductCumperfList(portfolioHoldId, productId, startDate,toCurrency);
			List<AppOrderHistoryVO> orderHistoryList = portfolioHoldService.findPortfolioOrderHistoryList(portfolioHoldId, productId, toCurrency, langCode, "");
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
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioHoldProductCumperfList.r
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
		if ("".equals(portfolioHoldId) || "".equals(productId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		String startDate = getStartDate(periodCode);

		List<AppHoldProductCumperfVO> cumperfList = portfolioHoldService.findPortfolioHoldProductCumperfList(portfolioHoldId, productId, startDate,toCurrency);
		
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(cumperfList);
		return result;
	}
	
	/**
	 * 得到某个投资组合交易记录接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioFeeList.r
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
		
		List<AppHoldFeeVO> voList = portfolioHoldService.findPortfolioFeeList(portfolioHoldId, langCode, toCurrency);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(voList);
		return result;
	}
	
	/**
	 * 得到某个投资组合交易记录接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioOrderHistoryList.r
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
		
		List<AppOrderHistoryVO> voList = portfolioHoldService.findPortfolioOrderHistoryList(portfolioHoldId, productId, toCurrency, langCode, keyword);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(voList);
		return result;
	}
	
	/**
	 * 得到某个投资组合基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myAsset/findHoldProductReturnDetail.r
	 * {"holdProductId":"40280a19596cd56i01s96ce18eb80005","langCode":"sc","toCurrency":"HKD" }
	 * @author zxtan
	 * @date 2017-03-03
	 */
	@RequestMapping(value = "/findHoldProductReturnDetail")
	@ResponseBody
	public ResultWS findHoldProductReturnDetail(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String holdProductId = jsonObject.optString("holdProductId", "");// 获得持仓产品ID
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);	
		String toCurrency = jsonObject.optString("toCurrency","");
		if ("".equals(holdProductId)||"".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppHoldProductReturnDetailVO vo = portfolioHoldService.findHoldProductReturnDetail(holdProductId, langCode, toCurrency);
				
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 得到某个投资组合的持仓基金产品信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/hold/findPortfolioProductFundList.r
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
		List<AppHoldProductFundVO> productList = portfolioHoldService.findPortfolioProductFundList(portfolioHoldId, langCode, null);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(productList);
		return result;
	}

}
