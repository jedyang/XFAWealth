package com.fsll.app.investor.market.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.market.service.AppStrategyMessService;
import com.fsll.app.investor.market.vo.AppStrategyAllocationVO;
import com.fsll.app.investor.market.vo.AppStrategyDetailVO;
import com.fsll.app.investor.market.vo.AppStrategyInfoVO;
import com.fsll.app.investor.market.vo.AppStrategyMessVo;
import com.fsll.app.investor.market.vo.AppStrategyProductVo;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 投资策略接口服务
 * @author zpzhou
 * @date 2016-9-18
 */
@RestController
@RequestMapping("/service/strategy")
public class AppStrategyMessRest extends WmesBaseRest {

	@Autowired
	private AppStrategyMessService strategyMessService;

	/**
	 * 得到我有权限查看的投资策略列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/strategy/findStrategyList.r
	 * {"langCode":"sc", "page":1,"rows":2,"memberId":"40280ad8888327690155e3291c580000","keyWord":""}
	 */
	@RequestMapping(value = "/findStrategyList")
	@ResponseBody
	public ResultWS findStrategyList(HttpServletRequest request,@RequestBody String body) {
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
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		//String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String keyWord = jsonObject.optString("keyWord", "");// 获得查询关键词

		if ("".equals(page)||"".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		jsonPaging= strategyMessService.findStrategyList(jsonPaging,memberId,keyWord,langCode);
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
	 * 得到某个投资策略基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/strategy/findStrategyInfoMess.r
	 * {"strategyId":"40280ad456e09b900156e0a3b08f0003" }
	 */
	@RequestMapping(value = "/findStrategyInfoMess")
	@ResponseBody
	public ResultWS findStrategyInfoMess(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String strategyId = jsonObject.optString("strategyId", "");// 获得策略ID
		if ("".equals(strategyId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppStrategyMessVo vo= strategyMessService.findStrategyInfoMess(strategyId);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 得到投资策略的产品信息列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/strategy/findStrategyProductList.r
	 * {"strategyId":"40280ad456e09b900156e0a3b08f0003","langCode":"sc","dateType":"return_period_code_1M","productType":"fund"}
	 */
	@RequestMapping(value = "/findStrategyProductList")
	@ResponseBody
	public ResultWS findStrategyProductList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String strategyId = jsonObject.optString("strategyId", "");// 获得组合ID
		String periodCode = jsonObject.optString("periodCode", "");
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String productType = jsonObject.optString("productType", "");//产品类型,fund基金,stock股票,bond债券,futures期货  ALL全部
		
		if ("".equals(strategyId) || "".equals(periodCode) || "".equals(productType)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppStrategyProductVo> list= strategyMessService.findStrategyProductList(strategyId,langCode,periodCode,productType,toCurrency);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	
	/**
	 * 按分类得到某个策略配置情况列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/strategy/findStrategyAllocationList.r
	 * {"strategyId":"40280ad45707c1fa015707d8acfe0007","langCode":"sc","allocationType":"fund"}
	 */
	@RequestMapping(value = "/findStrategyAllocationList")
	@ResponseBody
	public ResultWS findStrategyAllocationList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String strategyId = jsonObject.optString("strategyId", "");// 获得策略ID
		String allocationType = jsonObject.optString("allocationType", "");//分类类型：fund基金,stock股票,bond债券
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		
		if ( "".equals(strategyId) || "".equals(allocationType) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppStrategyAllocationVO> list= strategyMessService.findStrategyAllocationList(strategyId, allocationType, langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到某个策略配置详情接口， 通过body传递参数 
	 * 调用示例:[地址]/service/strategy/findStrategyDetail.r
	 * {"strategyId":"40280ad45707c1fa015707d8acfe0007","langCode":"sc","periodCode":"return_period_code_1M","allocationType":"fund","productType":"fund","productNum":5,"toCurrency":"USD"}
	 */
	@RequestMapping(value = "/findStrategyDetail")
	@ResponseBody
	public ResultWS findStrategyDetail(HttpServletRequest request,@RequestBody String body) {
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
		String strategyId = jsonObject.optString("strategyId", "");// 获得策略ID
		String allocationType = jsonObject.optString("allocationType", "");//分类类型：fund基金,stock股票,bond债券
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		String periodCode = jsonObject.optString("periodCode", "");
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String productType = jsonObject.optString("productType", "");//产品类型,fund基金,stock股票,bond债券,futures期货  ALL全部
		int productNum = jsonObject.optInt("productNum", 5);//产品数量
		
		if ( "".equals(memberId) ||  "".equals(strategyId) || "".equals(allocationType) || "".equals(periodCode) || "".equals(productType) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppStrategyInfoVO strategyInfo = strategyMessService.findStrategyInfo(memberId,strategyId, langCode);
		List<AppStrategyAllocationVO> allocationList = strategyMessService.findStrategyAllocationList(strategyId, allocationType, langCode);
		List<AppStrategyProductVo> productList = strategyMessService.findStrategyProductList(strategyId, langCode, periodCode, productType,productNum, toCurrency);
				
		AppStrategyDetailVO detailVO = new AppStrategyDetailVO();
		detailVO.setAppStrategyInfo(strategyInfo);
		detailVO.setAllocationList(allocationList);
		detailVO.setProductList(productList);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(detailVO);
		return result;
	}
}
